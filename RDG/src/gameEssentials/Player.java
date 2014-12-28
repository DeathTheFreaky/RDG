package gameEssentials;

import java.awt.Point;
import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import at.RDG.network.NetworkManager;
import at.RDG.network.communication.NetworkMessage;
import elements.Creature;
import views.GameEnvironment;
import general.Enums.CreatureType;
import general.Enums.Directions;
import general.Enums.Updates;
import general.Enums.ViewingDirections;

/**
 * Player stores all information associated with the player.
 * 
 */
public class Player extends Creature {

	/*
	 * public enum Updates { KEY_PRESSED, KEY_RELEASED }
	 * 
	 * public enum ViewingDirections { NORTH, EAST, SOUTH, WEST }
	 */

	/* is used for Players Movement and Rotation during Update */
	private boolean up, right, down, left = false;
	private boolean keyReleasedUp, keyReleasedDown, keyReleasedLeft,
			keyReleasedRight = false;
	private ViewingDirections lastViewingDirection = ViewingDirections.NORTH;

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
	
	/* network manager for moving the player */
	NetworkManager networkManager;
	
	/* save position of current enemy in a fight to avoid two players */
	private int enemyX = 0;
	private int enemyY = 0;
	private boolean activeFight = false;

	/**
	 * Constructs a Player.<br>
	 * Player will be positioned automatically in the upper left or lower right
	 * corner, depending on its player number.
	 * 
	 * @throws SlickException
	 * @see Player
	 */
	/*
	 * public Player() throws SlickException {
	 * 
	 * this("Player" + (1 + totalNumberOfPlayers), new Point(0, 0)); }
	 */

	/**
	 * Constructs a Player.<br>
	 * Player will be positioned automatically in the upper left or lower right
	 * corner, depending on its player number.
	 * 
	 * @param creatureName
	 * @param image
	 * @param type
	 * @throws SlickException
	 */
	public Player(String creatureName, Image image, CreatureType type)
			throws SlickException {

		this(creatureName, image, new Point(0, 0), type);
	}

	/**
	 * Constructs a Player.<br>
	 * Player will be positioned automatically in the upper left or lower right
	 * corner, depending on its player number.
	 * 
	 * @param name
	 * @param originOfGameEnvironment
	 * @throws SlickException
	 * @throws IOException 
	 * @see Player
	 */
	public Player(String creatureName, Image image,
			Point originOfGameEnvironment, CreatureType type)
			throws SlickException {

		/*
		 * at first create player as player1 - later check if it is player 1 or
		 * 2 and change type accordingly
		 */
		super(creatureName, image, type, 50, 25, 25, 25);

		this.originOfGameEnvironment = originOfGameEnvironment;
		
		/* network manager */
		networkManager = Game.getInstance().networkManager;

		/* unique player number, to identify a player */

		/*
		 * Das mit der static Variable funktioniert wahrscheinlich nicht, weil
		 * auf 2 PCs die Klassen instanziiert werden
		 */
		playerNumber = ++totalNumberOfPlayers;

		/* set player number according to CreatureType */
		if (type == CreatureType.PLAYER1)
			playerNumber = 1;
		else if (type == CreatureType.PLAYER2)
			playerNumber = 2;
		
		System.out.println(this.NAME + ": " + this.playerNumber);

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

		/*
		 * set positions related to player and camera all points reference the
		 * number of tiles, starting from upper left corner
		 */
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

			map.setScopePositionForPlayer(map.getWidth() - 14,
					map.getHeight() - 11);
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

	/**
	 * Updates the boolean values which represent the movement, when a key is
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
				up = false;
				down = false;
				right = false;
				left = true;
				break;
			case 31:
				up = false;
				left = false;
				right = false;
				down = true;
				break;
			case 32:
				up = false;
				left = false;
				down = false;
				right = true;
				break;
			case 17:
				left = false;
				down = false;
				right = false;
				up = true;
				break;
			}
			break;

		case KEY_RELEASED:
			switch (key) {
			case 30:
				if(left) 
					keyReleasedLeft = true;
				break;
			case 31:
				if(down)
					keyReleasedDown = true;
				break;
			case 32:
				if(right)
					keyReleasedRight = true;
				break;
			case 17:
				if(up)
					keyReleasedUp = true;
				break;
			}
		}
	}

	/**
	 * Updates the Player for each Update of the Game.<br>
	 * 
	 * Moves the Player position, moves the camera and sets scopePosition for
	 * the Player.
	 * @throws SlickException 
	 * 
	 */
	public void update(ViewingDirections goTo) throws SlickException {
		if (goTo == ViewingDirections.NORTH) {
			switch (lastViewingDirection) {
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
				networkManager.sendMessage(new NetworkMessage(position.x, position.y, goTo));
				moveCamera(Directions.UP);
				map.setScopePositionForPlayer(cameraPosition);
			}
			if(keyReleasedUp) {
				up = false;
				keyReleasedUp = false;
			}
		} else if (goTo == ViewingDirections.WEST) {
			switch (lastViewingDirection) {
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
				networkManager.sendMessage(new NetworkMessage(position.x, position.y, goTo));
				moveCamera(Directions.LEFT);
				map.setScopePositionForPlayer(cameraPosition);
			}
			if(keyReleasedLeft) {
				left = false;
				keyReleasedLeft = false;
			}
		} else if (goTo == ViewingDirections.SOUTH) {
			switch (lastViewingDirection) {
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
				networkManager.sendMessage(new NetworkMessage(position.x, position.y, goTo));
				moveCamera(Directions.DOWN);
				map.setScopePositionForPlayer(cameraPosition);
			}
			if(keyReleasedDown) {
				down = false;
				keyReleasedDown = false;
			}
		} else if (goTo == ViewingDirections.EAST) {
			switch (lastViewingDirection) {
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
				networkManager.sendMessage(new NetworkMessage(position.x, position.y, goTo));
				moveCamera(Directions.RIGHT);
				map.setScopePositionForPlayer(cameraPosition);
			}
			if(keyReleasedRight) {
				right = false;
				keyReleasedRight = false;
			}
		}
	}

	/**
	 * Draws Players on the map.
	 * 
	 * @param container
	 * @param graphics
	 */
	public void draw(GameContainer container, Graphics graphics, Player opponent) {
		
		if (Game.getInstance().getPlayer() == this) {
			
			/* draw player */
			graphics.drawImage(image,
					(originOfGameEnvironment.x + position.x - cameraPosition.x)
							* GameEnvironment.BLOCK_SIZE,
					(originOfGameEnvironment.y + position.y - cameraPosition.y)
							* GameEnvironment.BLOCK_SIZE);
			
			/* draw opponent */
			graphics.drawImage(opponent.image,
					(originOfGameEnvironment.x + opponent.getPosition().x - cameraPosition.x)
							* GameEnvironment.BLOCK_SIZE,
					(originOfGameEnvironment.y + opponent.getPosition().y - cameraPosition.y)
							* GameEnvironment.BLOCK_SIZE);			
		} 
	}

	/**
	 * Moves Camera position according to player's movements.
	 * 
	 * @param direction
	 */
	private void moveCamera(Directions direction) {
		switch (direction) {
		case UP:
			/*
			 * if player gets too close to camera viewport border camera is also
			 * moved
			 */
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

	/**
	 * Returns the Direction of View of the player.
	 */
	public ViewingDirections getDirectionOfView() {
		return this.lastViewingDirection;
	}
	
	/**Resets position of a player which has lost a fight.
	 * 
	 */
	public void resetPlayerPosition() {
		
		/* set positions related to player and camera
		   all points reference the number of tiles, starting from upper left corner */
		if (playerNumber == 1) {
			this.position = new Point(5, 4);
			networkManager.sendMessage(new NetworkMessage(5, 4, lastViewingDirection));
			this.cameraPosition = new Point(0, 0);

			/* where the player is "placed" in the scope of the camera */
			playerPositionInCamera = new Point(5, 4);

			map.setScopePositionForPlayer(0, 0);
		} else {
			this.position = new Point(map.getWidth() - 4, map.getHeight() - 3);
			networkManager.sendMessage(new NetworkMessage(map.getWidth() - 4, map.getHeight() - 3, lastViewingDirection));
			this.cameraPosition = new Point(map.getWidth() - 14,
					map.getHeight() - 11);

			/* where the player is "placed" in the scope of the camera */
			playerPositionInCamera = new Point(9, 7);

			map.setScopePositionForPlayer(map.getWidth() - 14,
					map.getHeight() - 11);
		}
	}
	
	/**Set position of current enemy in a fight or 0 when fight ends.
	 * @param x
	 * @param y
	 */
	public void setEnemyPosition(int x, int y, boolean activeFight) {
		this.enemyX = x;
		this.enemyY = y;
		this.activeFight = activeFight;
	}
	
	/**
	 * @return position of current enemy in fight or 0 if no fight is active
	 */
	public Point getEnemyPosition() {
		return new Point(this.enemyX, this.enemyY);
	}
	
	/**
	 * @return true if this player is currently fighting
	 */
	public boolean isInFight() {
		return this.activeFight;
	}
}
