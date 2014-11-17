package gameEssentials;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.SlickException;

import elements.Element;
import general.GroundFactory;
import general.ResourceManager;

/**Map is used to store all position information of players, rooms and items.
 * 
 */
public class Map {

	/* Enum for Players Movement */
	/*public enum Directions {
		UP, RIGHT, DOWN, LEFT
	}*/

	/*
	 * Static variable, so that we can access the one and only Map class without
	 * referencing an instance
	 */
	private static Map INSTANCE = null;

	/* specifies the number of tiles in width and height */
	private Dimension size = null;

	/* keep track of the player */
	private Player player = null;
	private Point playerScopePosition = null; //the upper left corner of the playerScopePosition in tiles
	private Element[][] scope = null;

	/* Other Player, for displaying etc. */
	private Player opponent = null;

	/* this arrays save all Elements to be displayed */
	private Element[][] background = null;
	private Element[][] overlay = null;
	//private boolean[][] passable = null; //walls are not passable -> is being determined via overlay null -> yes or no!

	/* saves the different kinds of rooms for the Minimap */
	private Element[][] minimap = null;

	/* Set Up the GroundFactory */
	private GroundFactory groundFactory;

	/**Constructs a Map.
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

	/**Initializes the Elements of the static Map class
	 * 
	 * @throws SlickException
	 */
	public void init() throws SlickException {
		size = new Dimension(Game.ROOMSHOR * (Game.ROOMWIDTH + 1) + 1, Game.ROOMSVER * (Game.ROOMHEIGHT + 1) + 1); //5 rooms + walls
		background = new Element[size.width][size.height];
		overlay = new Element[size.width][size.height];
		//passable = new boolean[size.width][size.height];
		minimap = new Element[Game.MINIMAPWIDTH][Game.MINIMAPHEIGHT];
		scope = new Element[Game.SCOPEWIDTH][Game.SCOPEHEIGHT];
		playerScopePosition = new Point();
		groundFactory = new GroundFactory().setUpFactory();

		/* null-initialize overlay */
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				overlay[i][j] = null;
			}
		}
		
		/* true-initialize passable */
		/*for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				passable[i][j] = true;
			}
		}*/
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

	/**Set the scopePosition for Player1 passing the scopePosition as a Point<br>
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

	/**Set the scopePosition for Player1 passing the scopePosition single x and y coordinates.<br>
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

	/**Fills the scope beginning from its upper left corner's ScopePosition position on the Map.
	 * 
	 * @return scope for the Player
	 */
	public Element[][] getScope() {
		for (int i = 0; i < Game.SCOPEWIDTH; i++) {
			for (int j = 0; j < Game.SCOPEHEIGHT; j++) {
				/* starts filling the scope form upper left corner */
				scope[i][j] = background[playerScopePosition.x + i][playerScopePosition.y + j];
				if (overlay[playerScopePosition.x + i][playerScopePosition.y + j] != null) {
					scope[i][j] = overlay[playerScopePosition.x + i][playerScopePosition.y + j];
				}
			}
		}
		return scope;
	}

	/**
	 * @return Minimap
	 */
	public Element[][] getMinimap() {
		return this.minimap;
	}

	/**Sets the Player the Map belongs to
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {

		/* player can only be initialized one time (start of game) */
		if (this.player == null)
			this.player = player;
	}

	/**Sets the Opponent of the player for displaying, etc.
	 * 
	 * @param opponent
	 */
	public void setOpponent(Player opponent) {

		/* opponent can only be initialized one time (start of game) */
		if (this.opponent == null) {
			this.opponent = opponent;
		}
	}

	/**Checks in the passable array if the headed field is passable (not a wall etc.).
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
	
	/**update the map
	 * 
	 */
	public void update() {
		// player
		// overlay
		//
	}
	
	/**Fills the map with rooms and their contents.
	 * 
	 */
	public void fillMap() {
		
		for (int i = 0; i <= getWidth(); i++) {
			for (int j = 0; j <= getHeight(); j++) {
				
				/* load ground textures -> testing only */
				background[i][j] = groundFactory
						.createYellowGroundOne(i, j);
				
				int wallmodx = i % (Game.ROOMWIDTH+1);
				int wallmody = j % (Game.ROOMHEIGHT+1);
				
				/* load walls and set them to not passable */
				if (wallmodx == 0 || wallmody == 0) {
					overlay[i][j] = groundFactory
							.createDarkGreyGround(i, j);
					//passable[i][j] = false;
				} 
				
				/* add doors in the middle -> testing only -> shall be moved to labyrinth algorithm */
								
				/* horizontal doors */
				if ((wallmodx == Game.ROOMWIDTH/2 || wallmodx == Game.ROOMWIDTH/2+1) && wallmody == 0 && j != 0 && j != getHeight()) {
					overlay[i][j] = null;
					//passable[i][j] = true;
				}
				
				/* vertical doors */
				if (wallmodx == 0 && i != 0 && i != getWidth() && (wallmody == Game.ROOMHEIGHT/2 || wallmody == Game.ROOMHEIGHT/2+1)) {
					overlay[i][j] = null;
					//passable[i][j] = true;
				}
			}
		}
		
		//test print loaded armament stuff
		ResourceManager testloader = null;
		try {
			testloader = new ResourceManager().getInstance();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		overlay[1][1] = new Element("Plate Helmet", testloader.IMAGES.get("Plate Helmet"),
				1, 1);
		
	}
}
