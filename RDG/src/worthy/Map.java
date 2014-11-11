package worthy;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.SlickException;

public class Map {

	/* Enum for Players Movement */
	public enum Directions {
		UP, RIGHT, DOWN, LEFT
	}

	/*
	 * Static variable, so that we can access the one and only Map class without
	 * referencing an instance
	 */
	private static Map INSTANCE = null;

	/* specifies the number of tiles in width and height */
	private Dimension size = null;

	/* this Points keep track of the player */
	private Player player = null;
	private Point playerScopeCenter = null;
	private Element[][] scope = null;

	/* Other Player, for displaying etc. */
	private Player opponent = null;

	/* this arrays saves all Elements to be displayed */
	private Element[][] background = null;
	private Element[][] overlay = null;
	// private boolean[][] passable = null;

	/* saves the different kind of rooms for the Minimap */
	private Element[][] minimap = null;

	/* Set Up the GroundFactory */
	private GroundFactory groundFactory;

	public Map() {

	}

	/* returns the one and only instance of Map */
	public Map getInstance() throws SlickException {
		if (INSTANCE == null) {
			INSTANCE = new Map();
			INSTANCE.init();
		}
		return INSTANCE;
	}

	/* Initializes the Elements of the static Map class */
	public void init() throws SlickException {
		size = new Dimension(56, 41);
		background = new Element[size.width][size.height];
		overlay = new Element[size.width][size.height];
		minimap = new Element[5][5];
		scope = new Element[15][12];
		playerScopeCenter = new Point();
		groundFactory = new GroundFactory().setUpFactory();

		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				overlay[i][j] = null;
			}
		}
	}

	/* returns a reference to Player */
	public Player getPlayer() {
		return this.player;
	}

	/* returns a reference to the Players Opponent */
	public Player getOpponent() {
		return this.opponent;
	}

	/* returns last horizontal block */
	public int getWidth() {
		return this.size.width - 1;
	}

	/* returns last vertical block */
	public int getHeight() {
		return this.size.height - 1;
	}

	/* Set the scopeCenter for Player1 */
	public void setScopeCenterForPlayer(Point scopeCenter) {
		if (scopeCenter.x >= 0 && scopeCenter.x <= getWidth() - 14
				&& scopeCenter.y >= 0 && scopeCenter.y <= getHeight() - 11) {
			this.playerScopeCenter = scopeCenter;
		} else {
			System.out.println("ERROR While setting ScopeCenter!");
			System.out.println("Point would extend Array!");
		}
	}

	/* Set the scopeCenter for Player1 */
	public void setScopeCenterForPlayer(int scopeCenterX, int scopeCenterY) {
		if (scopeCenterX >= 0 && scopeCenterX <= getWidth() - 14
				&& scopeCenterY >= 0 && scopeCenterY <= getHeight() - 11) {
			this.playerScopeCenter.x = scopeCenterX;
			this.playerScopeCenter.y = scopeCenterY;
		} else {
			System.out.println("ERROR While setting ScopeCenter!");
			System.out.println("Point would extend Array!");
		}
	}

	/* Get Scope for the Player */
	public Element[][] getScope() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 12; j++) {
				scope[i][j] = background[playerScopeCenter.x + i][playerScopeCenter.y
						+ j];
			}
		}
		return scope;
	}

	/* Get Minimap */
	public Element[][] getMinimap() {
		return this.minimap;
	}

	/* Sets the Player the Map belongs to */
	public void setPlayer(Player player) {

		/* player can only be initialized one time (start of game) */
		if (this.player == null)
			this.player = player;
	}

	/* Sets the Opponent of the player for displaying, etc. */
	public void setOpponent(Player opponent) {

		/* opponent can only be initialized one time (start of game) */
		if (this.opponent == null) {
			this.opponent = opponent;
		}
	}

	/* looks in the overlay table, if the headed field is passable */
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

	/* update the map */
	public void update() {
		// player
		// overlay
		//
	}

	/* fills the Map (for Experimenting, Testing) */
	public void fillMap() {
		for (int i = 0; i <= getWidth(); i++) {
			for (int j = 0; j <= getHeight(); j++) {

				if (i % 2 == 1 || j % 2 == 1) {
					background[i][j] = groundFactory
							.createYellowGroundTwo(i, j);
				} else {
					background[i][j] = groundFactory.createBorder(i, j);
				}

			}
		}
	}

}
