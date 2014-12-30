package gameEssentials;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.newdawn.slick.SlickException;

import at.RDG.network.NetworkManager;
import at.RDG.network.communication.MapConverter;
import at.RDG.network.communication.NetworkMessage;
import views.GameEnvironment;
import views.InventoryView;
import views.View;
import elements.Creature;
import elements.Element;
import elements.Equipment;
import elements.Item;
import elements.Potion;
import elements.Room;
import general.Chances;
import general.Enums.ItemClasses;
import general.Enums.Levels;
import general.Enums.RoomTypes;
import general.GroundFactory;
import general.ResourceManager;
import general.RoomFactory;

/**
 * Map is used to store all position information of players, rooms and items.
 * 
 */
public class Map {

	/*
	 * Static variable, so that we can access the one and only Map class without
	 * referencing an instance
	 */
	private static Map INSTANCE = null;

	/* specifies the number of tiles in width and height */
	private Dimension size = null;

	/* keep track of the player */
	private Player player = null;
	private Point playerScopePosition = null; // the upper left corner of the
												// playerScopePosition in tiles
	private Element[][] backgroundScope = null;
	private Element[][] overlayScope = null;

	/* Other Player, for displaying etc. */
	private Player opponent = null;

	/* this arrays save all Elements to be displayed */
	private Element[][] background = null;
	private Element[][] overlay = null;
	// private boolean[][] passable = null; //walls are not passable -> is being
	// determined via overlay null -> yes or no!

	/* saves the different kinds of rooms for the Minimap */
	private Element[][] minimap = null;

	/* Multidimensional Array storing all Rooms */
	private Room[][] rooms = null;
	
	private GameEnvironment gameEnvironment = null;
	
	/* ResourceManager needed for placing keys in random rooms */
	private ResourceManager resourceManager = null;
	
	/* ensure that spawned items are a little balanced */
	private java.util.Map<Levels, HashMap<String, Integer>> monsterBalance;
	private java.util.Map<ItemClasses, HashMap<Item, Integer>> itemsBalance;
	private java.util.Map<String, Integer> balanceOffsets;
	
	/* offsets for balance counters */
	private final int EASY_MONSTER_OFFSET = 1;
	private final int NORMAL_MONSTER_OFFSET = 1;
	private final int HARD_MONSTER_OFFSET = 1;
	private final int WEAK_ITEM_OFFSET = 1;
	private final int MEDIUM_ITEM_OFFSET = 1; 
	private final int STRONG_ITEM_OFFSET = 1;

	/* Network Manager for sending Map data */
	NetworkManager networkManager = null;
	
	/* Game instance */
	private Game game;
	
	/**
	 * Constructs a Map.
	 * 
	 * @see Map
	 */
	public Map() {
		this.game = Game.getInstance();
	}

	/**
	 * @return the one and only instance of Map
	 * @throws SlickException
	 */
	public Map getInstance() throws SlickException {
		if (INSTANCE == null) {
			INSTANCE = new Map();
			INSTANCE.init();
		}
		return INSTANCE;
	}

	/**
	 * Initializes the Elements of the static Map class
	 * 
	 * @throws SlickException
	 */
	public void init() throws SlickException {
		size = new Dimension(Game.ROOMSHOR * (Game.ROOMWIDTH + 1) + 1,
				Game.ROOMSVER * (Game.ROOMHEIGHT + 1) + 1); // 5 rooms + walls
		background = new Element[size.width][size.height];
		overlay = new Element[size.width][size.height];
		// passable = new boolean[size.width][size.height];
		minimap = new Element[Game.MINIMAPWIDTH][Game.MINIMAPHEIGHT];
		backgroundScope = new Element[Game.SCOPEWIDTH][Game.SCOPEHEIGHT];
		overlayScope = new Element[Game.SCOPEWIDTH][Game.SCOPEHEIGHT];
		rooms = new Room[Game.ROOMSHOR][Game.ROOMSVER];
		playerScopePosition = new Point();
		
		resourceManager = new ResourceManager().getInstance();
		try {
			networkManager = NetworkManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* null-initialize overlay */
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				overlay[i][j] = null;
			}
		}
		
		/* initialize the monster/item count balancing constructs */
		monsterBalance = new HashMap<Levels, HashMap<String, Integer>>();
		itemsBalance = new HashMap<ItemClasses, HashMap<Item, Integer>>();
		balanceOffsets = new HashMap<String, Integer>();
		
		HashMap<String, Integer> easyMap = new HashMap<String, Integer>();
		HashMap<String, Integer> normalMap = new HashMap<String, Integer>();
		HashMap<String, Integer> hardMap = new HashMap<String, Integer>();
		
		HashMap<Item, Integer> weakMap = new HashMap<Item, Integer>();
		HashMap<Item, Integer> mediumMap = new HashMap<Item, Integer>();
		HashMap<Item, Integer> strongMap = new HashMap<Item, Integer>();
		
		balanceOffsets.put("easy", EASY_MONSTER_OFFSET);
		balanceOffsets.put("normal", NORMAL_MONSTER_OFFSET);
		balanceOffsets.put("hard", HARD_MONSTER_OFFSET);
		balanceOffsets.put("weak", WEAK_ITEM_OFFSET);
		balanceOffsets.put("medium", MEDIUM_ITEM_OFFSET);
		balanceOffsets.put("strong", STRONG_ITEM_OFFSET);
		
		for (Entry<Levels, List<String>> entry : resourceManager.MONSTERS_LEVELED.entrySet()) {
			for (String name : entry.getValue()) {
				switch(entry.getKey()) {
					case EASY:
						easyMap.put(name, 0);
						break;
					case NORMAL:
						normalMap.put(name, 0);
						break;
					case HARD:
						hardMap.put(name, 0);
						break;
				}
			}
		}
		
		for (Entry<ItemClasses, List<Item>> entry : resourceManager.ITEMCLASSLIST.entrySet()) {
			for (Item item : entry.getValue()) {
				switch(entry.getKey()) {
					case WEAK:
						weakMap.put(item, 0);
						break;
					case MEDIUM:
						mediumMap.put(item, 0);
						break;
					case STRONG:
						strongMap.put(item, 0);
						break;
				}
			}
		}
		
		monsterBalance.put(Levels.EASY, easyMap);
		monsterBalance.put(Levels.NORMAL, normalMap);
		monsterBalance.put(Levels.HARD, hardMap);
		
		itemsBalance.put(ItemClasses.WEAK, weakMap);
		itemsBalance.put(ItemClasses.MEDIUM, mediumMap);
		itemsBalance.put(ItemClasses.STRONG, strongMap);
				
		if (game.isLobbyHost()) {
			fillWallsAndDoors(true);
			loadRooms(true);
			updateRooms(true);
			sendMap();
			game.setMapSet(true);
		} 

		/* true-initialize passable */
		/*
		 * for (int i = 0; i < size.width; i++) { for (int j = 0; j <
		 * size.height; j++) { passable[i][j] = true; } }
		 */
	}

	/**
	 * @return a reference to Player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * @return a reference to the Players Opponent
	 */
	public Player getOpponent() {
		return this.opponent;
	}

	/**
	 * @return last horizontal block
	 */
	public int getWidth() {
		return this.size.width - 1;
	}

	/**
	 * @return last vertical block
	 */
	public int getHeight() {
		return this.size.height - 1;
	}
	
	/**Returns overlay for network transfer.
	 * @return overlay
	 */
	public Element[][] getOverlay() {
		return this.overlay;
	}

	/**
	 * Set the scopePosition for Player1 passing the scopePosition as a Point<br>
	 * This Point defines the upper left corner of the scopePosition in tiles.
	 * 
	 * @param scopePosition
	 */
	public void setScopePositionForPlayer(Point scopePosition) {
		if (scopePosition.x >= 0 && scopePosition.x <= getWidth() - 14
				&& scopePosition.y >= 0 && scopePosition.y <= getHeight() - 11) {
			this.playerScopePosition = scopePosition;
		} else {
			System.out.println("ERROR While setting ScopePosition!");
			System.out.println("Point would extend Array!");
		}
	}

	/**
	 * Set the scopePosition for Player1 passing the scopePosition single x and
	 * y coordinates.<br>
	 * X and Y define the upper left corner of the scopePosition in tiles.
	 * 
	 * @param scopePositionX
	 * @param scopePositionY
	 */
	public void setScopePositionForPlayer(int scopePositionX, int scopePositionY) {
		if (scopePositionX >= 0 && scopePositionX <= getWidth() - 14
				&& scopePositionY >= 0 && scopePositionY <= getHeight() - 11) {
			this.playerScopePosition.x = scopePositionX;
			this.playerScopePosition.y = scopePositionY;
		} else {
			System.out.println("ERROR While setting ScopePosition!");
			System.out.println("Point would extend Array!");
		}
	}

	/**
	 * Fills the background scope beginning from its upper left corner's
	 * ScopePosition position on the Map.
	 * 
	 * @return scope for the Player
	 */
	public Element[][] getBackgroundScope() {
		for (int i = 0; i < Game.SCOPEWIDTH; i++) {
			for (int j = 0; j < Game.SCOPEHEIGHT; j++) {
				/* starts filling the scope form upper left corner */
				backgroundScope[i][j] = background[playerScopePosition.x + i][playerScopePosition.y
						+ j];
			}
		}
		return backgroundScope;
	}

	/**
	 * Fills the overlay scope beginning from its upper left corner's
	 * ScopePosition position on the Map.
	 * 
	 * @return scope for the Player
	 */
	public Element[][] getOverlayScope() {
		for (int i = 0; i < Game.SCOPEWIDTH; i++) {
			for (int j = 0; j < Game.SCOPEHEIGHT; j++) {
				/* starts filling the scope form upper left corner */
				overlayScope[i][j] = overlay[playerScopePosition.x + i][playerScopePosition.y
						+ j];
			}
		}
		return overlayScope;
	}

	/**
	 * @return Minimap
	 */
	public Element[][] getMinimap() {
		return this.minimap;
	}

	/**
	 * Sets the Player the Map belongs to
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {

		/* player can only be initialized one time (start of game) */
		if (this.player == null)
			this.player = player;
	}

	/**
	 * Sets the Opponent of the player for displaying, etc.
	 * 
	 * @param opponent
	 */
	public void setOpponent(Player opponent) {

		/* opponent can only be initialized one time (start of game) */
		if (this.opponent == null) {
			this.opponent = opponent;
		}
	}
	
	/**Sets the GameEnvironment for this Map.
	 * @param gameEnvironment
	 */
	public void setGameEnvironment(GameEnvironment gameEnvironment) {
		this.gameEnvironment = gameEnvironment;
	}

	/**
	 * Checks in the passable array if the headed field is passable (not a wall
	 * etc.).
	 * 
	 * @param fieldX
	 * @param fieldY
	 * @return
	 * @throws SlickException 
	 */
	public boolean isFieldPassable(int fieldX, int fieldY) throws SlickException {
		if (fieldX < 0 || fieldX > getWidth() || fieldY < 0
				|| fieldY > getHeight()) {
			return false;
		}
		if (fieldX == opponent.getPosition().x && fieldY == opponent.getPosition().y) {
			return false;
		}
		if (overlay[fieldX][fieldY] == null) {
			return true;
		}
		if (overlay[fieldX][fieldY] != null) {
			if (((overlay[fieldX][fieldY].NAME.equals("DoorGroundTreasureChamber1") 
					|| (overlay[fieldX][fieldY].NAME.equals("DoorGroundTreasureChamber2")))
					&& InventoryView.getInstance().hasKey())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if there is an Item or a Monster in front of the player.<br>
	 * If there is an item, pick up the item.<br>
	 * If there is a monster, start a fight.<br>
	 * If the monster looses the fight, delete it from map.<br>
	 * Resets player to start, if he loses a fight.<br>
	 * 
	 * @return Element or null
	 * @throws SlickException 
	 * @throws InterruptedException 
	 */
	public Element checkInFrontOfPlayer() throws SlickException {
		Element e = null;
		int x = player.getPosition().x;
		int y = player.getPosition().y;
		
		int targetX = 0;
		int targetY = 0;
		
		switch (player.getDirectionOfView()) {
		case NORTH:
			targetX = x;
			targetY = y - 1;
			break;
		case EAST:
			targetX = x + 1;
			targetY = y;
			break;
		case SOUTH:
			targetX = x;
			targetY = y + 1;
			break;
		case WEST:
			targetX = x - 1;
			targetY = y;
			break;
		default:
			break;
		}
				
		if (overlay[targetX][targetY] != null) {
			if ((overlay[targetX][targetY] instanceof Potion) ||
				  (overlay[targetX][targetY] instanceof Equipment)) {
				e = overlay[targetX][targetY];
				overlay[targetX][targetY] = null;
				networkManager.sendMessage(new NetworkMessage(targetX, targetY, overlay[targetX][targetY]));
			} else if ((overlay[targetX][targetY] instanceof Creature)) {
				/* avoid two players fighting the same enemy */
				if (!(this.opponent.getEnemyPosition().x == targetX && this.opponent.getEnemyPosition().y == targetY)) {
					gameEnvironment.startFight((Creature) overlay[targetX][targetY]);
				}
			} else if (overlay[targetX][targetY].NAME.equals("Key") && (!(InventoryView.getInstance().hasKey()))) {
				e = overlay[targetX][targetY];
				overlay[targetX][targetY] = null;
				networkManager.sendMessage(new NetworkMessage(targetX, targetY, overlay[targetX][targetY]));
			}
		}
		if (targetX == this.opponent.getPosition().x && targetY == this.opponent.getPosition().y) {
			/* only start fight against other player if he isn't currently in a fight */
			if (!this.opponent.isInFight()) {
				gameEnvironment.startFight((Creature) opponent);
				networkManager.sendMessage(new NetworkMessage("humanFightStart", true));
			}
		}

		return e;
	} 
	
	/**
	 * Returns Element in front of player or null.
	 * 
	 * @return Element or null
	 * @throws SlickException 
	 * @throws InterruptedException 
	 */
	public Element checkInFrontOfPlayer(boolean checkNull) throws SlickException {
		
		Element e = null;
		int x = player.getPosition().x;
		int y = player.getPosition().y;

		switch (player.getDirectionOfView()) {
			case NORTH:
				e = overlay[x][y - 1];
				break;
			case EAST:
				e = overlay[x + 1][y];
				break;
			case SOUTH:
				e = overlay[x][y + 1];
				break;
			case WEST:
				e = overlay[x - 1][y];
				break;
			default:
				break;
		}
		
		return e;
	}

	/**
	 * Update the map - load rooms' overlays and backgrounds.
	 * 
	 */
	public void update() {
		// player
		// overlay

		// updateRooms(); <-- Blödsinn, unnötig Rechenleistung, Updates nur
		// durch Zugriffsoperationen!, Map ist nur Speicherklasse!
	}

	/**
	 * updates all background and overlay fields based on room data
	 * 
	 */
	public void updateRooms(boolean lobbyHost) {
		for (int i = 0; i < Game.ROOMSHOR; i++) {
			for (int j = 0; j < Game.ROOMSVER; j++) {
				for (int x = 0; x < Game.ROOMWIDTH; x++) {
					for (int y = 0; y < Game.ROOMHEIGHT; y++) {

						int posx = i * (Game.ROOMWIDTH + 1) + x + 1;
						int posy = j * (Game.ROOMHEIGHT + 1) + y + 1;

						background[posx][posy] = rooms[i][j].background[x][y];
						
						/* do not overwrite overlay if this is the lobby client which has already received a fully filled overlay */
						if (lobbyHost) {
							overlay[posx][posy] = rooms[i][j].overlay[x][y];
						}
					}
				}
			}
		}
	}

	/**
	 * Detects the type of a Room based on the neighboring doors.
	 * 
	 * @param i
	 * @param j
	 */
	public RoomTypes detectRoomType(int i, int j) {

		/* door positions -> set to true if there is a door at this position */
		boolean[] doorpos = new boolean[] { false, false, false, false }; // 0:
																			// N,
																			// 1:
																			// E,
																			// 2:
																			// S,
																			// 3:
																			// W

		int hordoorx = (i + 1) * (Game.ROOMWIDTH + 1) - Game.ROOMWIDTH / 2 - 1;
		int hordoory1 = j * (Game.ROOMHEIGHT + 1);
		int hordoory2 = (j + 1) * (Game.ROOMHEIGHT + 1);
		int verdoorx1 = i * (Game.ROOMWIDTH + 1);
		int verdoorx2 = (i + 1) * (Game.ROOMWIDTH + 1);
		int verdoory = (j + 1) * (Game.ROOMHEIGHT + 1) - Game.ROOMHEIGHT / 2
				- 1;

		/* count doors */
		int doorcount = 0;

		/* roomType */
		RoomTypes type = null;

		/* there are doors if overlay is empty -> no wall */
		if (overlay[hordoorx][hordoory1] == null) {
			doorpos[0] = true;
			doorcount++;
		}
		if (overlay[verdoorx1][verdoory] == null) {
			doorpos[1] = true;
			doorcount++;
		}
		if (overlay[hordoorx][hordoory2] == null) {
			doorpos[2] = true;
			doorcount++;
		}
		if (overlay[verdoorx2][verdoory] == null) {
			doorpos[3] = true;
			doorcount++;
		}

		/*
		 * detect room types based on number of doors - for 2 doors also check
		 * door positions
		 */
		switch (doorcount) {
		case 1:
			type = RoomTypes.DEADEND;
			break;
		case 2:
			for (int z = 0; z < 4; z++) {
				int posTurn = (z + 1) % 4;
				int posHallway = (z + 2) % 4;
				if (doorpos[z] && doorpos[posTurn])
					type = RoomTypes.TURN;
				if (doorpos[z] && doorpos[posHallway])
					type = RoomTypes.HALLWAY;
			}
			type = RoomTypes.TURN;
			break;
		case 3:
			type = RoomTypes.TJUNCTION;
			break;
		case 4:
			type = RoomTypes.JUNCTION;
			break;
		}

		/* check for treasure chamber -> middle of the map */
		if ((i == Game.ROOMSHOR / 2) && (j == Game.ROOMSVER / 2))
			type = RoomTypes.TREASURECHAMBER;

		return type;
	}

	/**
	 * Loads Rooms from RoomFactory.<br>
	 * Room Type will be detected according to door positions created by maze
	 * algorithm.
	 * 
	 * @throws SlickException
	 * 
	 */
	public void loadRooms(boolean lobbyHost) throws SlickException {

		for (int i = 0; i < Game.ROOMSHOR; i++) {
			for (int j = 0; j < Game.ROOMSVER; j++) {

				/* detect room types and load according room */
				RoomTypes type = detectRoomType(i, j);

				rooms[i][j] = RoomFactory.createRoom(type, monsterBalance, itemsBalance, this, balanceOffsets, lobbyHost);
			}
		}
		
		if (lobbyHost) {
			/* place keys for treasure chamber in random rooms */
			placeKeys();
		}
	}

	/**
	 * Fills the map with walls and doors.
	 * 
	 * @throws SlickException
	 * 
	 */
	public void fillWallsAndDoors(boolean lobbyHost) throws SlickException {

		/* load walls and doors */
		for (int i = 0; i <= getWidth(); i++) {
			for (int j = 0; j <= getHeight(); j++) {

				int wallmodx = i % (Game.ROOMWIDTH + 1);
				int wallmody = j % (Game.ROOMHEIGHT + 1);
				
				/* determine door position on host */
				if (lobbyHost) {
					
					/* load walls and set them to not passable */
					if (wallmodx == 0 || wallmody == 0) {
						overlay[i][j] = GroundFactory.createDarkGreyGround(i, j);
					}
					
					/*
					 * add doors in the middle -> testing only -> shall be moved to
					 * labyrinth algorithm
					 */
					
					/* horizontal doors */
					if ((wallmodx == Game.ROOMWIDTH / 2 || wallmodx == Game.ROOMWIDTH / 2 + 1)
							&& wallmody == 0 && j != 0 && j != getHeight()) {

						int nodoory1 = getHeight() / 2 - (Game.ROOMHEIGHT / 2);
						int nodoory2 = getHeight() / 2 + (Game.ROOMHEIGHT / 2 + 1);
						int nodoorx1 = getWidth() / 2;
						int nodoorx2 = getWidth() / 2 + 1;

						if (!((i == nodoorx1 || i == nodoorx2) && ((j == nodoory1) || (j == nodoory2)))) {
							
							overlay[i][j] = null;
							background[i][j] = GroundFactory.createGreyGround(i, j);
						}
						// passable[i][j] = true;
					}

					/* vertical doors */
					if (wallmodx == 0
							&& i != 0
							&& i != getWidth()
							&& (wallmody == Game.ROOMHEIGHT / 2 || wallmody == Game.ROOMHEIGHT / 2 + 1)) {

						int nodoory1 = getHeight() / 2;
						int nodoory2 = getHeight() / 2 + 1;
						int nodoorx1 = getWidth() / 2 - (Game.ROOMWIDTH / 2);
						int nodoorx2 = getWidth() / 2 + (Game.ROOMWIDTH / 2 + 1);

						if (!((i == nodoorx1 || i == nodoorx2) && ((j == nodoory1) || (j == nodoory2)))) {
							overlay[i][j] = null;
							background[i][j] = GroundFactory.createGreyGround(i, j);
						}
						/*
						 * place door texture on background and overlay -> if key is
						 * obtained -> remove door textures from overlay temporarily
						 * when hitting "E" key
						 */
						else {
							if (j == nodoory1) {
								background[i][j] = GroundFactory.createDoorGround2(
										i, j);
								overlay[i][j] = GroundFactory.createDoorGround2(i,
										j);
							} else {
								background[i][j] = GroundFactory.createDoorGround1(
										i, j);
								overlay[i][j] = GroundFactory.createDoorGround1(i,
										j);
							}
						}
					}
				} 
				
				/* set doors for client */
				else {
					if (overlay[i][j] != null) {
						if (overlay[i][j].NAME.equals("GreyGround")) {
							background[i][j] = GroundFactory.createGreyGround(i, j);
							overlay[i][j] = null;
						}
						else if (overlay[i][j].NAME.equals("DoorGroundTreasureChamber1")) {
							background[i][j] = GroundFactory.createDoorGround1(
									i, j);
						}
						else if (overlay[i][j].NAME.equals("DoorGroundTreasureChamber2")) {
							background[i][j] = GroundFactory.createDoorGround2(
									i, j);
						}
					}
					
					/* grey ground info for doors needs to be set */
					if (wallmodx == 0 || wallmody == 0) {
						if (overlay[i][j] == null) {
							background[i][j] = GroundFactory.createGreyGround(i, j);
						}
					}
				}
			}
		}
	}

	/**Places 2 keys for the treasure chamber at random positions on the map.
	 * 
	 */
	private void placeKeys() {
		
		/* random rooms */
		int randRoom1x = 0;
		int randRoom1y = 0;
		int randRoom2x = 0;
		int randRoom2y = 0;
		
		Point randRoom1 = Chances.randomRoom();
		randRoom1x = randRoom1.x;
		randRoom1y = randRoom1.y;
		
		Point randRoom2 = null;
		
		do {
			randRoom2 = Chances.randomRoom();
			randRoom2x = randRoom2.x;
			randRoom2y = randRoom2.y;
		} while (randRoom1x == randRoom2x && randRoom1y == randRoom2y);
		
		/* random tiles in room */
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		
		Point randTile1 = Chances.randomTile(rooms[randRoom1x][randRoom1y]);
		x1 = randTile1.x;
		y1 = randTile1.y;
		
		do {
			Point randTile2 = Chances.randomTile(rooms[randRoom2x][randRoom2y]);
			x2 = randTile2.x;
			y2 = randTile2.y;
		} while (x1 == x2 && y1 == y2);
		
		rooms[randRoom1x][randRoom1y].overlay[x1][y1] = new Element("Key", resourceManager.IMAGES.get("Key"));
		rooms[randRoom2x][randRoom2y].overlay[x1][y1] = new Element("Key", resourceManager.IMAGES.get("Key"));
	}

	/**When a monster looses a fight, it has to be removed from the map.
	 * 
	 */
	public void removeContentInFrontOfPlayer() {

		int x = player.getPosition().x;
		int y = player.getPosition().y;

		switch (player.getDirectionOfView()) {
		case NORTH:
			overlay[x][y - 1] = null;
			networkManager.sendMessage(new NetworkMessage(x, y - 1, overlay[x][y - 1]));
			break;
		case EAST:
			overlay[x + 1][y] = null;
			networkManager.sendMessage(new NetworkMessage(x + 1, y, overlay[x + 1][y]));
			break;
		case SOUTH:
			overlay[x][y + 1] = null;
			networkManager.sendMessage(new NetworkMessage(x, y + 1, overlay[x][y + 1]));
			break;
		case WEST:
			overlay[x - 1][y] = null;
			networkManager.sendMessage(new NetworkMessage(x - 1, y, overlay[x - 1][y]));
			break;
		default:
			break;
		}
	}
	
	/**Used to increase the balance counter for an added item or monster.
	 * @param balanceMap
	 * @param itemName
	 */
	public void increaseBalance(String balanceMap, String name, Item item){
		
		if (balanceMap.equals("monsterBalance")) {
			for (Entry<Levels, HashMap<String, Integer>> entry : monsterBalance.entrySet()) {
				if (entry.getValue().containsKey(name)){
					entry.getValue().put(name, entry.getValue().get(name) + 1);
				}
			}
		} else if (balanceMap.equals("itemsBalance")) {
			for (Entry<ItemClasses, HashMap<Item, Integer>> entry : itemsBalance.entrySet()) {
				if (entry.getValue().containsKey(item)){
					entry.getValue().put(item, entry.getValue().get(item) + 1);
				}
			}
		}
	}
	
	/**Sends Map overlay to other player but sets all images to null.
	 * @throws SlickException 
	 * 
	 */
	private void sendMap() throws SlickException {
		networkManager.sendMessage(MapConverter.toNetworkMessage(this.overlay));
	}
	
	/**Set overlay from network Classes.
	 * @param overlay
	 * @throws SlickException 
	 */
	public synchronized void setReceivedMapData(Element[][] overlay) throws SlickException {
		this.overlay = overlay;
		fillWallsAndDoors(false);
		loadRooms(false);
		updateRooms(false);
		game.setMapSet(true);
	}
	
	/**Drops item on the map on the field in front of the player if the field is free.
	 * @param element
	 * @param x
	 * @param y
	 * @param source
	 * @return
	 * @throws SlickException 
	 */
	public int dropItem(Element element, int x, int y) throws SlickException {
		
		if (element == null) {
			return 0;
		}
		
		/* check if dragged item is dragged to armor section */
		if (x > 0 && x < 480 && y > 0 
				&& y < 384) {
						
			Element e = checkInFrontOfPlayer(true);
					
			/* dragged element cannot be dropped */
			if (e != null) {
				return 2;
			} 
			/* drop item on the map */
			else {
				switch (player.getDirectionOfView()) {
					case NORTH:
						overlay[player.getPosition().x][player.getPosition().y - 1] = element;
						break;
					case EAST:
						overlay[player.getPosition().x + 1][player.getPosition().y] = element;
						break;
					case SOUTH:
						overlay[player.getPosition().x][player.getPosition().y + 1] = element;
						break;
					case WEST:
						overlay[player.getPosition().x - 1][player.getPosition().y] = element;
						break;
					default:
						break;
				}
			}
			
			return 1;
		}
				
		return 0;
	}
}
