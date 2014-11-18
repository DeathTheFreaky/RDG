package gameEssentials;

import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import elements.Creature;
import views.GameEnvironment;
import general.Enums.CreatureType;
import general.Enums.Directions;
import general.Enums.Updates;
import general.Enums.ViewingDirections;

/**Player stores all information associated with the player.
 * 
 */
public class Player extends Creature {

	/*public enum Updates {
		KEY_PRESSED, KEY_RELEASED
	}

	public enum ViewingDirections {
		NORTH, EAST, SOUTH, WEST
	}*/

	/* is used for Players Movement and Rotation during Update */
	boolean up, right, down, left = false;
	ViewingDirections lastViewingDirection = ViewingDirections.NORTH;

	private Point originOfGameEnvironment;

	/* is needed to find out if its Player 1 or Player 2 */
	private static int totalNumberOfPlayers = 0;

	/* specifies if it is player1 or player2 */
	private int playerNumber;

	/* tracks the position of the camera on the map */
	private Point cameraPosition;

	/* tracks the position of the player in the camera */
	private Point playerPositionInCamera;

	/* this array keeps track of all visited rooms for the minimap */
	private boolean[][] visited = new boolean[5][5];

	/* kind of camera simulation, that is used for special background movement */
	private boolean[][] camera = new boolean[15][12];

	/* The Map associated with this player for getting some Values */
	Map map;

	/**Constructs a Player.<br>
	 * Player will be positioned automatically in the upper left or lower right corner, depending on its player number.
	 * 
	 * @throws SlickException
	 * @see Player
	 */
	/*public Player() throws SlickException {
		
		this("Player" + (1 + totalNumberOfPlayers), new Point(0, 0));
	}*/
	
	/**Constructs a Player.<br>
	 * Player will be positioned automatically in the upper left or lower right corner, depending on its player number.
	 * 
	 * @param creatureName
	 * @param image
	 * @param type
	 * @throws SlickException
	 */
	public Player(String creatureName, Image image, CreatureType type) throws SlickException {
		
		this(creatureName, image, new Point(0, 0), type);
	}

	/**Constructs a Player.<br>
	 * Player will be positioned automatically in the upper left or lower right corner, depending on its player number.
	 *  
	 * @param name
	 * @param originOfGameEnvironment
	 * @throws SlickException
	 * @see Player
	 */
	public Player(String creatureName, Image image, Point originOfGameEnvironment, CreatureType type) throws SlickException {
		
		/* at first create player as player1 - later check if it is player 1 or 2 and change type accordingly */
		super(creatureName, image, type, 100, 10, 10, 10);
		
		this.originOfGameEnvironment = originOfGameEnvironment;

		/* unique player number, to identify a player */

		/*
		 * Das mit der static Variable funktioniert wahrscheinlich nicht, weil
		 * auf 2 PCs die Klassen instanziiert werden
		 */
		playerNumber = ++totalNumberOfPlayers;	
		
		/* set player number according to CreatureType */
		if (type == CreatureType.PLAYER1) playerNumber = 1;
		else if (type == CreatureType.PLAYER2) playerNumber = 2;

		/* is needed for some values contained in the Map Class */
		map = new Map().getInstance();

		/* initialize visited and camera */
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				visited[i][j] = false;
			}
		}

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
				camera[i][j] = false;
			}
		}
		
		/* set positions related to player and camera
		   all points reference the number of tiles, starting from upper left corner */
		if (playerNumber == 1) {
			this.position = new Point(5, 4);
			this.cameraPosition = new Point(0, 0);

			/* where the player is "placed" in the scope of the camera */
			playerPositionInCamera = new Point(5, 4);

			map.setScopePositionForPlayer(0, 0);
		} else {
			this.position = new Point(map.getWidth() - 4, map.getHeight() - 3);
			this.cameraPosition = new Point(map.getWidth() - 14,
					map.getHeight() - 11);

			/* where the player is "placed" in the scope of the camera */
			playerPositionInCamera = new Point(9, 7);

			map.setScopePositionForPlayer(map.getWidth() - 13,
					map.getHeight() - 10);
		}

		camera[playerPositionInCamera.x][playerPositionInCamera.y] = true;
	}

	/**
	 * @return actual position of the player in tiles
	 */
	public Point getPosition() {
		return this.position;
	}

	/**
	 * @return number of the player (1 or 2)
	 */
	public int getPlayerNumber() {
		return this.playerNumber;
	}

	/**Updates the boolean values which represent the movement, when a key is
	 * pressed or released.
	 * 
	 * @param key
	 * @param kindOfUpdate
	 */
	public void update(int key, Updates kindOfUpdate) {

		switch (kindOfUpdate) {
		case KEY_PRESSED:
			switch (key) {
			case 30:
				left = true;
				break;
			case 31:
				down = true;
				break;
			case 32:
				right = true;
				break;
			case 17:
				up = true;
				break;
			}
			break;

		case KEY_RELEASED:
			switch (key) {
			case 30:
				left = false;
				break;
			case 31:
				down = false;
				break;
			case 32:
				right = false;
				break;
			case 17:
				up = false;
				break;
			}
		}
	}

	/**Updates the Player for each Update of the Game.<br>
	 * 
	 * Moves the Player position, moves the camera and sets scopePosition for the Player.
	 * 
	 */
	public void update() {
		if (up == true) {
			switch(lastViewingDirection) {
			case WEST:
				image.rotate(90f);
				break;
			case SOUTH:
				image.rotate(180f);
				break;
			case EAST:
				image.rotate(-90f);
				break;
			default:
				break;
			}
			lastViewingDirection = ViewingDirections.NORTH;
			if (map.isFieldPassable(position.x, position.y - 1)) {
				position.move(position.x, position.y - 1);
				moveCamera(Directions.UP);
				map.setScopePositionForPlayer(cameraPosition);
			}
		} else if (left == true) {
			switch(lastViewingDirection) {
			case NORTH:
				image.rotate(-90f);
				break;
			case SOUTH:
				image.rotate(90f);
				break;
			case EAST:
				image.rotate(180f);
				break;
			default:
				break;
			}
			lastViewingDirection = ViewingDirections.WEST;
			if (map.isFieldPassable(position.x - 1, position.y)) {
				position.move(position.x - 1, position.y);
				moveCamera(Directions.LEFT);
				map.setScopePositionForPlayer(cameraPosition);
			}
		} else if (down == true) {
			switch(lastViewingDirection) {
			case WEST:
				image.rotate(-90f);
				break;
			case NORTH:
				image.rotate(180f);
				break;
			case EAST:
				image.rotate(90f);
				break;
			default:
				break;
			}
			lastViewingDirection = ViewingDirections.SOUTH;
			if (map.isFieldPassable(position.x, position.y + 1)) {
				position.move(position.x, position.y + 1);
				moveCamera(Directions.DOWN);
				map.setScopePositionForPlayer(cameraPosition);
			}
		} else if (right == true) {
			switch(lastViewingDirection) {
			case WEST:
				image.rotate(180f);
				break;
			case SOUTH:
				image.rotate(-90f);
				break;
			case NORTH:
				image.rotate(90f);
				break;
			default:
				break;
			}
			lastViewingDirection = ViewingDirections.EAST;
			if (map.isFieldPassable(position.x + 1, position.y)) {
				position.move(position.x + 1, position.y);
				moveCamera(Directions.RIGHT);
				map.setScopePositionForPlayer(cameraPosition);
			}
		}
	}

	/**Draws Player on the map.
	 * 
	 * @param container
	 * @param graphics
	 */
	public void draw(GameContainer container, Graphics graphics) {
		graphics.drawImage(image,
				(originOfGameEnvironment.x + position.x - cameraPosition.x)
						* GameEnvironment.BLOCK_SIZE,
				(originOfGameEnvironment.y + position.y - cameraPosition.y)
						* GameEnvironment.BLOCK_SIZE);
	}

	/**Moves Camera position according to player's movements.
	 * 
	 * @param direction
	 */
	private void moveCamera(Directions direction) {
		switch (direction) {
		case UP:
			/* if player gets too close to camera viewport border camera is also moved */
			if (playerPositionInCamera.y <= 4 && cameraPosition.y > 0) {
				cameraPosition.move(cameraPosition.x, cameraPosition.y - 1);
			} else {
				camera[playerPositionInCamera.x][playerPositionInCamera.y] = false;
				camera[playerPositionInCamera.x][--playerPositionInCamera.y] = true;
			}
			break;
		case RIGHT:
			if (playerPositionInCamera.x >= 9
					&& cameraPosition.x < map.getWidth() - 14) {
				cameraPosition.move(cameraPosition.x + 1, cameraPosition.y);
			} else {
				camera[playerPositionInCamera.x][playerPositionInCamera.y] = false;
				camera[++playerPositionInCamera.x][playerPositionInCamera.y] = true;
			}
			break;
		case DOWN:
			if (playerPositionInCamera.y >= 7
					&& cameraPosition.y < map.getHeight() - 11) {
				cameraPosition.move(cameraPosition.x, cameraPosition.y + 1);
			} else {
				camera[playerPositionInCamera.x][playerPositionInCamera.y] = false;
				camera[playerPositionInCamera.x][++playerPositionInCamera.y] = true;
			}
			break;
		case LEFT:
			if (playerPositionInCamera.x <= 5 && cameraPosition.x > 0) {
				cameraPosition.move(cameraPosition.x - 1, cameraPosition.y);
			} else {
				camera[playerPositionInCamera.x][playerPositionInCamera.y] = false;
				camera[--playerPositionInCamera.x][playerPositionInCamera.y] = true;
			}
			break;
		}
	}

}
