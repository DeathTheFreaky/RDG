package gameEssentials;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.SlickException;

import elements.Element;
import elements.Equipment;
import elements.Potion;
import elements.Room;
import general.Enums.RoomTypes;
import general.GroundFactory;
import general.RoomFactory;

/**
 * Map is used to store all position information of players, rooms and items.
 * 
 */
public class Map {

	/* Enum for Players Movement */
	/*
	 * public enum Directions { UP, RIGHT, DOWN, LEFT }
	 */

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

	/* Set Up the GroundFactory */
	private GroundFactory groundFactory;

	/* Multidimensional Array storing all Rooms */
	private Room[][] rooms = null;

	/* Room Factory is needed to create and fill rooms */
	private RoomFactory roomFactory;

	/**
	 * Constructs a Map.
	 * 
	 * @see Map
	 */
	public Map() {

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
		groundFactory = new GroundFactory().getInstance();
		roomFactory = new RoomFactory().getInstance();

		/* null-initialize overlay */
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				overlay[i][j] = null;
			}
		}

		fillMap();

		// test door pos detection
		loadRooms();
		updateRooms();

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

	/**
	 * Checks in the passable array if the headed field is passable (not a wall
	 * etc.).
	 * 
	 * @param fieldX
	 * @param fieldY
	 * @return
	 */
	public boolean isFieldPassable(int fieldX, int fieldY) {
		if (fieldX < 0 || fieldX > getWidth() || fieldY < 0
				|| fieldY > getHeight()) {
			return false;
		}
		if (overlay[fieldX][fieldY] == null) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if there is an Item in front of the player. If there is, it
	 * returns the item, else it returns null.
	 * 
	 * @return Equipment or null
	 */
	public Equipment getItemInFrontOfPlayer() {
		Equipment e = null;
		int x = player.getPosition().x;
		int y = player.getPosition().y;

		switch (player.getDirectionOfView()) {
		case NORTH:
			if (/*
				 * overlay[x][y - 1] instanceof Potion ||
				 */overlay[x][y - 1] instanceof Equipment) {
				e = (Equipment) overlay[x][y - 1];
				overlay[x][y - 1] = null;
			}
			break;
		case EAST:
			if (/*
				 * overlay[x][y - 1] instanceof Potion ||
				 */overlay[x + 1][y] instanceof Equipment) {
				e = (Equipment) overlay[x + 1][y];
				overlay[x + 1][y] = null;
			}
			break;
		case SOUTH:
			if (/*
				 * overlay[x][y - 1] instanceof Potion ||
				 */overlay[x][y + 1] instanceof Equipment) {
				e = (Equipment) overlay[x][y + 1];
				overlay[x][y + 1] = null;
			}
			break;
		case WEST:
			if (/*
				 * overlay[x][y - 1] instanceof Potion ||
				 */overlay[x - 1][y] instanceof Equipment) {
				e = (Equipment) overlay[x - 1][y];
				overlay[x - 1][y] = null;
			}
			break;
		default:
			break;
		}

		return e;
	}

	/**
	 * update the map - load rooms' overlays and backgrounds
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
	public void updateRooms() {
		for (int i = 0; i < Game.ROOMSHOR; i++) {
			for (int j = 0; j < Game.ROOMSVER; j++) {
				for (int x = 0; x < Game.ROOMWIDTH; x++) {
					for (int y = 0; y < Game.ROOMHEIGHT; y++) {

						int posx = i * (Game.ROOMWIDTH + 1) + x + 1;
						int posy = j * (Game.ROOMHEIGHT + 1) + y + 1;

						background[posx][posy] = rooms[i][j].background[x][y];
						overlay[posx][posy] = rooms[i][j].overlay[x][y];
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
	 */
	public void loadRooms() {

		for (int i = 0; i < Game.ROOMSHOR; i++) {
			for (int j = 0; j < Game.ROOMSVER; j++) {

				/* detect room types and load according room */
				RoomTypes type = detectRoomType(i, j);

				// System.out.println(i + ", " + j + ": " + type);

				rooms[i][j] = roomFactory.createRoom(type);
			}
		}
	}

	/**
	 * Fills the map with rooms and their contents.
	 * 
	 */
	public void fillMap() {

		/* load walls and doors */
		for (int i = 0; i <= getWidth(); i++) {
			for (int j = 0; j <= getHeight(); j++) {

				int wallmodx = i % (Game.ROOMWIDTH + 1);
				int wallmody = j % (Game.ROOMHEIGHT + 1);

				/* load walls and set them to not passable */
				if (wallmodx == 0 || wallmody == 0) {
					overlay[i][j] = groundFactory.createDarkGreyGround(i, j);
					// passable[i][j] = false;
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
						background[i][j] = groundFactory.createGreyGround(i, j);
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
						background[i][j] = groundFactory.createGreyGround(i, j);
					}
					/*
					 * place door texture on background and overlay -> if key is
					 * obtained -> remove door textures from overlay temporarily
					 * when hitting "E" key
					 */
					else {
						if (j == nodoory1) {
							background[i][j] = groundFactory.createDoorGround2(
									i, j);
							overlay[i][j] = groundFactory.createDoorGround2(i,
									j);
						} else {
							background[i][j] = groundFactory.createDoorGround1(
									i, j);
							overlay[i][j] = groundFactory.createDoorGround1(i,
									j);
						}
					}
				}
			}
		}
	}
}
