package gameEssentials;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import configLoader.Configloader;
import elements.Element;
import elements.Equipment;
import views.ArmorView;
import views.Chat;
import views.GameEnvironment;
import views.InventoryView;
import views.Minimap;
import views.View;
import general.Enums.CreatureType;
import general.Enums.ImageSize;
import general.Enums.UsedClasses;
import general.Enums.ViewingDirections;
import general.ResourceManager;
import general.Enums.Updates;

/**
 * Game class stores the main configuration parameters of a game and defines
 * what needs to be done in each iteration of the game loop.
 * 
 * @see BasicGame
 */
public class Game extends BasicGame {

	/* Game Variables in pixels */
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final int GAME_ENVIRONMENT_WIDTH = 480;
	public static final int GAME_ENVIRONMENT_HEIGHT = 384;
	public static final int CHAT_WIDTH = 480;
	public static final int CHAT_HEIGHT = 96;
	public static final int ARMOR_WIDTH = 160;
	public static final int ARMOR_HEIGHT = 240;
	public static final int INVENTORY_WIDTH = 160;
	public static final int INVENTORY_HEIGHT = 240;

	// final if these values cannot be changed later on - if we want to change
	// roomsize, minimap size, scope size -> don't make final
	/* room, minimap, scope sizes */
	public static final int ROOMWIDTH = 8; // should be even -> door's width = 2
	public static final int ROOMHEIGHT = 6; // should be even -> door's width =
											// 2
	public static final int MINIMAPWIDTH = 5;
	public static final int MINIMAPHEIGHT = 5;
	public static final int SCOPEWIDTH = 15;
	public static final int SCOPEHEIGHT = 12;

	/* room numbers */
	public static int ROOMSHOR = 5;
	public static int ROOMSVER = 5;

	/* number of tries when looking for a free field in a room */
	public static int MAXTRIES = 15;

	/* every milliseconds an Update is made */
	private final int UPDATE = 100;
	/* how many milliseconds passed until the next Update */
	private int timeToUpdate = 0;
	/* Updates left for Player Update */
	private int updatesUntilPlayerUpdate = 10;

	/* Origins of the different Views in tile numbers */
	private Point gameEnvironmentOrigin, chatOrigin, armorViewOrigin,
			inventoryViewOrigin, minimapOrigin;

	/* flag, if the mouse is over the chat (for scrolling) */
	private boolean mouseOverChat = false;
	/* flag, if the mouse is over the minimap (for moving) */
	private boolean mouseOverMinimap = false;

	/* flag, if the mouse is currently moving an item */
	private boolean dragging = false;
	/* flag, if the mouse is currently moving the minimap */
	private boolean draggingMinimap = false;

	/* For moving the player */
	private ViewingDirections goTo = null;
	/* flag, if a key is released */
	private boolean keyReleased = false;

	/* Equippment that is dragged */
	private Element draggedItem;
	/* For positioning the dragged Item */
	private int draggedX = 0;
	private int draggedY = 0;
	/* For positioning the minimap while dragging */
	private int offsetX = 0;
	private int offsetY = 0;

	/* The Game for Player ... */
	private String playerName;
	private Player player;

	/* Declares all Views for the Game */
	private GameEnvironment gameEnvironment;
	private Chat chat;
	private ArmorView armorView;
	private InventoryView inventoryView;
	private Minimap minimap;

	/* Map which is needed for each Player */
	private Map map;

	/* resource path */
	public static final String IMAGEPATH = "./resources/images/";

	// path to config files
	public static final String CONFIGPATH = "config/Results/";

	// create instance of configloader
	private Configloader configloader = null;

	/* Declare all classes, we need for the game (Factory, Resourceloader) */
	// private ResourceManager resourceManager;
	// private GroundFactory groundFactory;

	/**
	 * Construct game and sets Playername as
	 * "Find out if its Player 1 or Player2"
	 * 
	 * @param title
	 * @see Game
	 */
	public Game(String title) {
		this(title, "Find out if its Player1 or Player2");
	}

	/**
	 * Construct game and set playerName
	 * 
	 * @param title
	 * @param playerName
	 * @see Game
	 */
	public Game(String title, String playerName) {
		super(title);
		this.playerName = playerName;
	}

	@Override
	public void init(GameContainer container) throws SlickException {

		/* load config - must be successful in order to continue */
		try {
			configloader = new Configloader().getInstance();
		} catch (IllegalArgumentException | ParserConfigurationException
				| SAXException | IOException e) {
			e.printStackTrace();
			System.err
					.println("\nParsing Configuration Files failed\nExiting program\n");
			System.exit(1);
		}

		// Test Printing
		/*
		 * ConfigTestprinter configprinter = new
		 * ConfigTestprinter(configloader); configprinter.print();
		 */

		/* Points in tile numbers */
		gameEnvironmentOrigin = new Point(0, 0);
		chatOrigin = new Point(0, 12);
		armorViewOrigin = new Point(15, 0);
		inventoryViewOrigin = new Point(15, 12);
		minimapOrigin = new Point(20, 20);

		/* Initialize Factory and Manager classes! */
		new ResourceManager().getInstance();

		/* network lobby must be called before this to detect player type */
		CreatureType playerType = CreatureType.PLAYER1;
		if (playerType == CreatureType.PLAYER1) {
			player = new Player(playerName,
					new ResourceManager().getInstance().IMAGES.get("Player1"),
					gameEnvironmentOrigin, playerType);
		} else if (playerType == CreatureType.PLAYER2) {
			player = new Player(playerName,
					new ResourceManager().getInstance().IMAGES.get("Player2"),
					gameEnvironmentOrigin, playerType);
		}

		map = new Map().getInstance();
		map.setPlayer(player);
		// map.fillMap();

		/* Dimension is specified in pixels */
		gameEnvironment = new GameEnvironment("GameEnvironment",
				gameEnvironmentOrigin, new Dimension(GAME_ENVIRONMENT_WIDTH,
						GAME_ENVIRONMENT_HEIGHT), player);

		minimap = new Minimap("Minimap", gameEnvironmentOrigin.x
				+ minimapOrigin.x, gameEnvironmentOrigin.y + minimapOrigin.y);

		chat = new Chat("Chat", chatOrigin, new Dimension(CHAT_WIDTH,
				CHAT_HEIGHT), container);

		armorView = new ArmorView("ArmorInventory", armorViewOrigin,
				new Dimension(ARMOR_WIDTH, ARMOR_HEIGHT));

		inventoryView = new InventoryView("Inventory", inventoryViewOrigin,
				new Dimension(INVENTORY_WIDTH, INVENTORY_HEIGHT));
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

		/* run an Update */
		if (timeToUpdate > UPDATE) {
			if (updatesUntilPlayerUpdate == 0) {
				player.update(goTo);
				if (keyReleased) {
					goTo = null;
					keyReleased = false;
				}
				updatesUntilPlayerUpdate = 3;
			}
			gameEnvironment.update();
			chat.update();

			timeToUpdate = 0;
			updatesUntilPlayerUpdate--;
		}

		timeToUpdate += delta;
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {

		gameEnvironment.draw(container, g);
		minimap.draw(container, g);
		chat.draw(container, g);
		armorView.draw(container, g);
		inventoryView.draw(container, g);

		if (dragging && draggedItem != null) {
			g.drawImage(draggedItem.getImage(ImageSize.d20x20), draggedX,
					draggedY);
		}
	}

	@Override
	public void keyPressed(int key, char c) {

		/* Key Values for Players Movement! (a,s,d,w) */
		if ((key == 30 || key == 31 || key == 32 || key == 17)
				&& !gameEnvironment.isFightActive()) {
			// player.update(key, Updates.KEY_PRESSED);
			if (key == 30) {
				goTo = ViewingDirections.WEST;
				keyReleased = false;
			} else if (key == 31) {
				goTo = ViewingDirections.SOUTH;
				keyReleased = false;
			} else if (key == 32) {
				goTo = ViewingDirections.EAST;
				keyReleased = false;
			} else {
				goTo = ViewingDirections.NORTH;
				keyReleased = false;
			}
		} else if (key == 15) {
			if (chat.hasFocus()) {
				chat.setFocus(false);
			} else {
				chat.setFocus(true);
			}
		} else if (key == 18) {
			Element e = map.getItemInFrontOfPlayer();
			if (e != null) {
				// inventoryView.storeEquipment((Equipment) e);
				inventoryView.storeItem(e);
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {

		/* Key Values for Players Movement! (a,s,d,w) */
		if ((key == 30 || key == 31 || key == 32 || key == 17)
				&& !gameEnvironment.isFightActive()) {
			// player.update(key, Updates.KEY_RELEASED);
			if (key == 30 && goTo == ViewingDirections.WEST) {
				keyReleased = true;
			} else if (key == 31 && goTo == ViewingDirections.SOUTH) {
				keyReleased = true;
			} else if (key == 32 && goTo == ViewingDirections.EAST) {
				keyReleased = true;
			} else if (key == 17 && goTo == ViewingDirections.NORTH) {
				keyReleased = true;
			}
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (button == 0) { // linke Maustaste
			armorView.changeTab(x, y);
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {

		/*
		 * add potion check -> potion may be activated only during a fight and
		 * after that the next round continues -> use variable to determine when
		 * potion taking is possiple
		 */

		/*
		 * pulls a weapon or armament from the inventory to armor set and vice
		 * versa -> equip and unequip weapon
		 */
		if (!dragging) {
			if (gameEnvironment.isFightActive()) {
				System.out
						.println("You cannot change your Equipment during fight");
				dragging = true;
				return;
			}

			if (!mouseOverMinimap) {
				this.draggedItem = inventoryView.getItem(oldx, oldy,
						UsedClasses.Equipment);
				if (draggedItem == null) {
					this.draggedItem = armorView.getEquipment(oldx, oldy);
				}
				dragging = true;
			}

		}
		draggedX = newx;
		draggedY = newy;

		if (mouseOverMinimap) {
			if (!draggingMinimap) {
				System.out.println("Set new offsets");
				draggingMinimap = true;
				offsetX = oldx - minimap.positionX;
				offsetY = oldy - minimap.positionY;
				minimap.setMoved(true);
			}

			minimap.positionX = newx - offsetX;
			minimap.positionY = newy - offsetY;

		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {

		/*
		 * add potion check -> potion may be activated only during a fight and
		 * after that the next round continues -> use variable to determine when
		 * potion taking is possiple
		 */

		/*
		 * drops a weapon or armament from the inventory to armor set and vice
		 * versa -> equip and unequip weapon
		 */
		if (button == 0) { // linke Maustaste
			if (dragging) {
				if (!draggingMinimap) {
					Equipment e;
					if ((e = armorView.equipArmor((Equipment) draggedItem, x,
							y, inventoryView)) != null) {
						inventoryView.storeItem((Element) e);
					}
					dragging = false;
					draggedItem = null;
				}
			}

			if (draggingMinimap) {
				draggingMinimap = false;
				minimap.setMoved(false);

				if (x - offsetX < GAME_ENVIRONMENT_WIDTH - x - offsetX) {
					minimap.positionX = 20;
				} else {
					minimap.positionX = GAME_ENVIRONMENT_WIDTH - minimap.WIDTH
							- 20;
				}

				if (y - offsetY < GAME_ENVIRONMENT_HEIGHT - y - offsetY) {
					minimap.positionY = 20;
				} else {
					minimap.positionY = GAME_ENVIRONMENT_HEIGHT
							- minimap.HEIGHT - 20;
				}

			}
		}
	}

	@Override
	public void mouseMoved(int oldX, int oldY, int newX, int newY) {

		/*
		 * Check if mouse is over chat to enable chat scrolling. X and Y in
		 * pixels!
		 */
		if (newX >= 0 && newX <= CHAT_WIDTH && newY > GAME_ENVIRONMENT_HEIGHT
				&& newY <= HEIGHT) {
			mouseOverChat = true;
			mouseOverMinimap = false;
		} else if (newX >= minimap.positionX
				&& newX <= minimap.positionX + minimap.WIDTH
				&& newY >= minimap.positionY
				&& newY <= minimap.positionY + minimap.HEIGHT) {
			mouseOverMinimap = true;
			mouseOverChat = false;
		} else {
			mouseOverChat = false;
			mouseOverMinimap = false;
		}

		// Im inventory view
		if (newX > GAME_ENVIRONMENT_WIDTH
				&& newX < GAME_ENVIRONMENT_WIDTH + INVENTORY_WIDTH
				&& newY > ARMOR_HEIGHT
				&& newY < ARMOR_HEIGHT + INVENTORY_HEIGHT) {

		}

		// Im Armor view
		if (newX > GAME_ENVIRONMENT_WIDTH
				&& newX < GAME_ENVIRONMENT_WIDTH + INVENTORY_WIDTH && newY > 0
				&& newY < ARMOR_HEIGHT) {

		}

	}

	@Override
	public void mouseWheelMoved(int scroll) {

		/* Enable chat scrolling if mouse if over chat */
		if (mouseOverChat) {
			chat.scroll(scroll);
		}
	}
}
