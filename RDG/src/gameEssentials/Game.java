package gameEssentials;

import elements.Creature;
import elements.Element;
import elements.Monster;
import elements.Potion;
import fighting.Fight;
import general.Enums.AttackScreens;
import general.Enums.Attacks;
import general.Enums.Channels;
import general.Enums.CreatureType;
import general.Enums.ImageSize;
import general.Enums.UsedClasses;
import general.Enums.ViewingDirections;
import general.AttackFactory;
import general.Main;
import general.ResourceManager;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import lobby.Lobby;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import views.font.TrueTypeFont;
import org.xml.sax.SAXException;

import views.ArmorView;
import views.Chat;
import views.GameEnvironment;
import views.InventoryView;
import views.Minimap;
import views.View;
import views.chat.Message;
import at.RDG.maze.Maze;
import at.RDG.network.NetworkManager;
import at.RDG.network.communication.MapConverter;
import at.RDG.network.communication.NetworkMessage;
import configLoader.Configloader;

/**
 * Game class stores the main configuration parameters of a game and defines
 * what needs to be done in each iteration of the game loop.
 * 
 * @see BasicGame
 */
public class Game extends BasicGame {

	/* game Instance */
	private static Game INSTANCE = null;

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
	public static int ROOMSHOR = 9;
	public static int ROOMSVER = 9;

	/* number of tries when looking for a free field in a room */
	public static int MAXTRIES = 15;

	/* every milliseconds an Update is made */
	private final int UPDATE = 100;
	/* how many milliseconds passed until the next Update */
	private int timeToUpdate = 0;
	/* Updates left for Player Update */
	private int updatesUntilPlayerUpdate = 10;

	/* Origins of the different Views in tile numbers */
	/*
	 * gameEnvironment is needed for fight, because each fight is a new thread
	 * -> new instance
	 */
	Point gameEnvironmentOrigin = new Point(0, 0), chatOrigin, armorViewOrigin,
			inventoryViewOrigin, minimapOrigin;

	/* flag, if the mouse is over the chat (for scrolling) */
	private boolean mouseOverChat = false;
	/* flag, if the mouse is over the minimap (for moving) */
	private boolean mouseOverMinimap = false;

	/* flag, if the mouse is currently moving an item */
	private boolean dragging = false;
	/* flag, if the mouse is currently moving the minimap */
	private boolean draggingMinimap = false;
	/* check if item was dragged from inventory or armor view */
	private View draggingSource;
	private int draggingOldX;
	private int draggingOldY;

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
	String playerName;
	Player player;
	Player opponent;

	/* Declares all Views for the Game */
	GameEnvironment gameEnvironment;
	Chat chat;
	ArmorView armorView;
	InventoryView inventoryView;
	Minimap minimap;

	/* Map which is needed for each Player */
	Map map;

	/* resource path */
	public static final String IMAGEPATH = "./resources/images/";

	// create instance of configloader
	Configloader configloader = null;

	/* fight Thread */
	private Thread fightThread = null;

	/* needed for human fights - set to true if this is the lobby HOSTER */
	Boolean lobbyHost = false;

	/* list holding child threads */
	private List<Thread> threadList = new ArrayList<Thread>();

	/* fight Instance from gameEnvironment */
	Fight fightInstance = null;

	/* network manager for transferring data to other pc */
	NetworkManager networkManager;

	/* instance of resource Manager for loading images */
	ResourceManager resourceManager;

	/* draw a loading screen before the game has finished loading all data */
	private boolean loading = true;
	private boolean startetLoading = false;
	private boolean mapSet = false;

	private boolean opponentNameSet = false;
	
	/* game Container for access by gameLoad thread */
	GameContainer container;

	/* shown when human player has defeated other human player */
	private String endScreen = null;
	private int endCtr = 50; // 50*100ms = 5 seconds
	private boolean endFightStarted = false;
	
	/* after time is up, force a human fight */
	private long starttime = Calendar.getInstance().getTimeInMillis();
	private long endtime = starttime + 1000 * 60 * 10; //1000ms * 60 sec * 10 min
	private Calendar enddate = Calendar.getInstance();
	
	/* quit game */
	private boolean running = true;

	/* Declare all classes, we need for the game (Factory, Resourceloader) */
	// private ResourceManager resourceManager;
	// private GroundFactory groundFactory;

	/**
	 * Construct game and sets Playername as
	 * "Find out if its Player 1 or Player2"
	 * 
	 * @param title
	 * @throws IOException
	 * @see Game
	 */
	private Game(String title) throws IOException {
		/* Find out if its is player1 or player2 */
		this(title, "Testplayername");
	}

	/**
	 * Construct game and set playerName
	 * 
	 * @param title
	 * @param playerName
	 * @throws IOException
	 * @see Game
	 */
	private Game(String title, String playerName) throws IOException {
		super(title);
		this.playerName = playerName;
		this.networkManager = NetworkManager.getInstance();
		networkManager.sendMessage(new NetworkMessage("playerName", this.playerName));
	}

	/**
	 * Only returns existing instance of game or null if none instance exists
	 * yet.
	 * 
	 * @return
	 */
	public static Game getInstance() {
		return INSTANCE;
	}

	/**
	 * Get Instance of Game.
	 * 
	 * @param title
	 * @return
	 * @throws IOException
	 */
	public static Game getInstance(String title) throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new Game(title);
		}
		return INSTANCE;
	}

	/**
	 * Get Instance of Game.
	 * 
	 * @param title
	 * @param playerName
	 * @return
	 * @throws IOException
	 */
	public static Game getInstance(String title, String playerName)
			throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new Game(title, playerName);
		}
		return INSTANCE;
	}

	@Override
	public void init(GameContainer container) throws SlickException {

		// load ressources in first game loop to show a loading screen
		this.container = container;
	}

	/**
	 * Loads the game while drawing screen is shown.
	 * 
	 */
	private void loadGame() {

		try {
						
			/* set enddate for displaying in chat message */
			enddate.setTimeInMillis(endtime);
			
			/* load config - must be successful in order to continue */
			try {
				Configloader.init();
			} catch (IllegalArgumentException | ParserConfigurationException
					| SAXException | IOException e) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
						"Parsing Configuration Files failed.", e);
				System.exit(1);
			}

			try {
				this.configloader = new Configloader().getInstance();
				if (this.configloader == null) {
					throw new NullPointerException();
				}
			} catch (IllegalArgumentException e) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
						"Parsing Configuration Files failed.", e);
				quitGame();
			} catch (NullPointerException e2) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
						"Configloader returned null.", e2);
				quitGame();
			}
			
			// Test Printing
			/*
			 * ConfigTestprinter configprinter = new
			 * ConfigTestprinter(configloader); configprinter.print();
			 */

			/* determined by network lobby - TESTING only */
			this.lobbyHost = this.networkManager.isLobbyHost();

			/* Points in tile numbers */
			this.gameEnvironmentOrigin = new Point(0, 0);
			this.chatOrigin = new Point(0, 12);
			this.armorViewOrigin = new Point(15, 0);
			this.inventoryViewOrigin = new Point(15, 12);
			this.minimapOrigin = new Point(20, 20);

			/* Initialize Factory and Manager classes! */
			try {
				ResourceManager.init();
			} catch (SlickException e) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
						"Loading resources failed", e);
				quitGame();
			}
			
			try {
				this.resourceManager = new ResourceManager().getInstance();
				if (this.resourceManager == null) {
					throw new NullPointerException();
				}
			} catch (IllegalArgumentException e) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
						"Loading resources failed.", e);
				quitGame();
			} catch (NullPointerException e2) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
						"ResourceManager returned null.", e2);
				quitGame();
			}
			
			/* network lobby must be called before this to detect player type */
			CreatureType playerType;
			if (this.lobbyHost) {
				playerType = CreatureType.PLAYER1;
			} else {
				playerType = CreatureType.PLAYER2;
			}
			if (playerType == CreatureType.PLAYER1) {
				this.player = new Player(this.playerName,
						new ResourceManager().getInstance().IMAGES
								.get("Player1"), this.gameEnvironmentOrigin,
						CreatureType.PLAYER1, true);
				this.opponent = new Player("Testenemy",
						new ResourceManager().getInstance().IMAGES
								.get("Player2"), this.gameEnvironmentOrigin,
						CreatureType.PLAYER2, false);
			} else if (playerType == CreatureType.PLAYER2) {
				this.player = new Player(this.playerName,
						new ResourceManager().getInstance().IMAGES
								.get("Player2"), this.gameEnvironmentOrigin,
						CreatureType.PLAYER2, true);
				this.opponent = new Player("Testenemy",
						new ResourceManager().getInstance().IMAGES
								.get("Player1"), this.gameEnvironmentOrigin,
						CreatureType.PLAYER1, false);
			}

			/* Load the chat */
			this.chat = new Chat("Chat", this.chatOrigin, new Dimension(
					Game.CHAT_WIDTH, Game.CHAT_HEIGHT), this.container);

			/* Load Views - Dimension is specified in pixels */
			this.armorView = new ArmorView("ArmorInventory",
					this.armorViewOrigin, new Dimension(Game.ARMOR_WIDTH,
							Game.ARMOR_HEIGHT));

			this.gameEnvironment = new GameEnvironment("GameEnvironment",
					this.gameEnvironmentOrigin, new Dimension(
							Game.GAME_ENVIRONMENT_WIDTH,
							Game.GAME_ENVIRONMENT_HEIGHT), this.player,
					this.opponent, this.armorView, this, this.chat);

			this.minimap = new Minimap("Minimap", this.gameEnvironmentOrigin.x
					+ this.minimapOrigin.x, this.gameEnvironmentOrigin.y
					+ this.minimapOrigin.y);

			this.inventoryView = InventoryView.getInstance("Inventory",
					this.inventoryViewOrigin, new Dimension(
							Game.INVENTORY_WIDTH, Game.INVENTORY_HEIGHT));
			
			/* Load Map and place the player */
			this.map = new Map().getInstance();
			this.map.setPlayer(this.player);
			this.map.setGameEnvironment(this.gameEnvironment);

			/* needs to be changed - only for testing purposes */
			this.map.setOpponent(this.opponent);
			// map.fillMap();

			this.fightInstance = this.gameEnvironment.getFightInstance();

			this.setLoading(false);
			
		} catch (SlickException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					"Failed to load game.", e);
			Game.getInstance().getGameContainer().exit();
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
				
		/* run an Update */
		if (timeToUpdate > UPDATE) {
						
			if (!running) {
				resourceManager.destroyAllResources();
				Lobby.quitConnection();
				container.exit();
			}
						
			/* force a human fight when time runs out */
			if (Calendar.getInstance().getTimeInMillis() > endtime) {
				if (!this.opponent.isInFight()) {
					if (lobbyHost && endFightStarted == false) {

						/*
						 * load fight instance into new thread for fight to be
						 * carried out
						 */
						if (fightThread == null) {
							fightThread = new Thread(fightInstance);
						}

						/* start the fight and set the enemy */
						if (!fightInstance.isInFight()) {
							fightThread.start();
							fightInstance.setEnemy(opponent);
							networkManager.sendMessage(new NetworkMessage("humanFightStart", true));
							
							endFightStarted = true;
						}
					}
				}
			}

			if (this.loading == true) {
				if (this.startetLoading == true) {
					loadGame();
				} else {
					this.startetLoading = true;
				}
			} else {
				if (this.mapSet && this.opponentNameSet) {
					if (this.endScreen == null) {
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
						armorView.update();

						updatesUntilPlayerUpdate--;
					} else {
						if (this.endCtr == 0) {
							//return to main menu - how?
							quitGame();
						} else {
							this.endCtr--;
						}
					}
				}

				processNetworkMessages();
				timeToUpdate = 0;
			}
		}

		timeToUpdate += delta;
	}

	/**
	 * Process Network Messages according to their type.
	 * 
	 * @throws SlickException
	 * 
	 */
	private void processNetworkMessages() throws SlickException {

		NetworkMessage message = null;
		
		while((message = networkManager.getNextMessage()) != null) {
			switch(message.type) {
				case CHAT:
					chat.newMessage(new Message(message.message, 0, 0, Channels.PRIVATE));
					break;
				case FIGHT:
					fightInstance.processMessages(message);
					break;
				case GENERAL:
					if (message.event.equals("humanFightStart") && message.trigger) {
						gameEnvironment.startFight((Creature) opponent);
					} else if (message.event.equals("roundSynchro")) {
						fightInstance.setEnemyFinished();
					}
					break;
				case STRING:	
					if (message.eventString.equals("playerName")) {
						this.opponent.setPlayerName(message.nwString);
												
						Calendar cal = Calendar.getInstance();

						/*
						 * print a welcoming message and use an instance of Calendar class to
						 * get current time
						 */
						
						chat.newMessage(new Message("New Game Started! " + this.player.NAME + " vs " + this.opponent.NAME, cal));
						/* print end of this game session */
						/*
						 * if game session overlaps a full hour, react accordingly -> use up
						 * minutes until full hour is reached, increase hour, increase remaining
						 * minutes starting form 0
						 */
						
						int endHour = enddate.get(Calendar.HOUR_OF_DAY);
						int endMin = enddate.get(Calendar.MINUTE);
						
						String hourPrefix = endHour < 10 ? "0" : "";
						String minPrefix = endMin < 10 ? "0" : "";
						
						
						chat.newMessage(new Message("Instance ends at " + hourPrefix + endHour + ":" + minPrefix + endMin, cal));
						
						this.opponentNameSet = true;
						
					}
					break;
				case ITEM:
					map.getOverlay()[message.itempos[0]][message.itempos[1]] = MapConverter.fillImage(message.item, message.itempos[0], message.itempos[1]);
					break;
				case MAP:
					map.setReceivedMapData(MapConverter.toOverlay(message));
					break;
				case FIGHTPOSITION:
					if (message.enemyPosX == 0 && message.enemyPosY == 0) {
						map.getOpponent().setEnemyPosition(message.enemyPosX, message.enemyPosY, false); 
					} else {
						map.getOpponent().setEnemyPosition(message.enemyPosX, message.enemyPosY, true);
					}
					break;
				case PLAYERPOSITION:
					opponent.setPosition(message.playerpos[0], message.playerpos[1]);
					opponent.setDirectionImage(message.playerdir);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		
		if (this.running) {
			if (this.loading == true) {
				java.awt.Font fontMenuscreen = (java.awt.Font) new java.awt.Font("Arial", java.awt.Font.BOLD, 24);
				TrueTypeFont ttfMenuscreen = new TrueTypeFont(fontMenuscreen, true);
				ttfMenuscreen.drawString(Game.WIDTH/2, Game.HEIGHT/2 - ttfMenuscreen.getHeight("Loading Game...")/2,
						"Loading Game...", Color.white, TrueTypeFont.ALIGN_CENTER);
			} else {
				if (this.mapSet && this.opponentNameSet) {
					if (this.endScreen == null) {
						gameEnvironment.draw(container, g);
						/*
						 * if (!(fightInstance.isInFight())) {
						 * minimap.draw(container, g); }
						 */
						chat.draw(container, g);
						armorView.draw(container, g);
						inventoryView.draw(container, g);

						if (dragging && draggedItem != null) {
							g.drawImage(draggedItem.getImage(ImageSize.d20x20),
									draggedX, draggedY);
						}
					} else {
						((TrueTypeFont) resourceManager.DEFAULT_FONTS.get("menuscreen")).drawString(Game.WIDTH/2, Game.HEIGHT/2 - ((TrueTypeFont) resourceManager.DEFAULT_FONTS.get("description")).getHeight("Loading Game...")/2,
								this.endScreen, Color.white, TrueTypeFont.ALIGN_CENTER);
					}
				} else {
					((TrueTypeFont) resourceManager.DEFAULT_FONTS.get("menuscreen")).drawString(Game.WIDTH/2, Game.HEIGHT/2 - ((TrueTypeFont) resourceManager.DEFAULT_FONTS.get("description")).getHeight("Loading Game...")/2,
							"Loading Game...", Color.white, TrueTypeFont.ALIGN_CENTER);
				}
			}
		}
	}
	
	@Override
    public boolean closeRequested()
    {
		/* quitGame() causes lobby to quit network connection and exits game with slicks exit method */
		quitGame();
		return false;
    }

	@Override
	public void keyPressed(int key, char c) {

		/*try {
			if (key == 19) {
				map.getOverlay()[1][1] = ItemFactory.createPotion("Poison", 1);
			} else if (key == 20) {
				map.getOverlay()[1][1].setImage(resourceManager.IMAGES
						.get("Strength"));
			}
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		/* Key Values for Players Movement! (a,s,d,w) */
		if ((key == 30 || key == 31 || key == 32 || key == 17)
				&& !fightInstance.isInFight()) {
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
			Element e = null;
			try {
				e = map.checkInFrontOfPlayer();
				if (e != null) {
					// inventoryView.storeEquipment((Equipment) e);
					if (inventoryView.hasMoreRoom()) {
						inventoryView.storeItem(e, armorView);
					} else {
						map.dropItem(e, 1, 1);
					}
				}
			} catch (SlickException e1) {
				e1.printStackTrace();
			}
		} else if (key == 1) {
			// set attackScreen in Fight.java to MAIN
			if (fightInstance.isInFight()) {
				if (fightInstance.getAttackScreens() != AttackScreens.WAITING) {
					fightInstance.setAttackScreen(AttackScreens.MAIN);
					fightInstance.setChangeTabActive(false);
					fightInstance.setPotionTakingActive(false);
				}
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {

		/* Key Values for Players Movement! (a,s,d,w) */
		if ((key == 30 || key == 31 || key == 32 || key == 17)
				&& !fightInstance.isInFight()) {
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

		/*
		 * All views' mouse click implementation are called. Wrong ones do
		 * nothing
		 */
		if (button == 0) { // linke Maustaste

			if (fightInstance.isInFight()) {
				if (!(fightInstance.isChangeTabActive())) {
					fightInstance.handleFightOptions(x, y);
				} else if (fightInstance.isChangeTabActive()) {
					armorView.changeTab(x, y);
				}
			} else {
				armorView.changeTab(x, y);
			}
			
			/* end showing descriptions when mouse is clicked */
			inventoryView.endShowingDescription();
			armorView.endShowingDescription();
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
			
			inventoryView.endShowingDescription();
			armorView.endShowingDescription();
			
			if (fightInstance.isInFight()) {
				if (fightInstance.isPotionTakingActive()) {
					this.draggedItem = armorView.getItem(oldx, oldy);
					dragging = true;
				}
				return;
			}

			/*
			 * this.draggedItem = inventoryView.getItem(oldx, oldy,
			 * UsedClasses.Element);
			 * 
			 * if (draggedItem == null) { this.draggedItem =
			 * armorView.getItem(oldx, oldy);
			 */

			if (!mouseOverMinimap) {
				this.draggedItem = inventoryView.getItem(oldx, oldy,
						UsedClasses.Element);
				if (draggedItem == null) {
					this.draggedItem = armorView.getItem(oldx, oldy);
					if (this.draggedItem != null) {
						draggingSource = armorView;
						draggingOldX = oldx;
						draggingOldY = oldy;
					}
				} else {
					draggingSource = inventoryView;
					draggingOldX = oldx;
					draggingOldY = oldy;
				}
				dragging = true;
			}
		}
		draggedX = newx;
		draggedY = newy;

		if (mouseOverMinimap) {
			if (!draggingMinimap) {
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
				Element e;

				if (fightInstance.isInFight()) {
					// only allow when potionTaking is active
					if (fightInstance.isPotionTakingActive()) {
						if ((e = armorView.drinkPotion((Potion) draggedItem, x,
								y, inventoryView)) != null) {
							armorView.backPotion((Potion) e);
						} else {
							fightInstance.setActiveAttackType(Attacks.POTION);
							fightInstance.setPotionTakingActive(false);
							fightInstance
									.setAttackScreen(AttackScreens.WAITING);
						}
					}
				} else {

					if (!draggingMinimap) {
						try {
							int mapDropRet = map.dropItem(this.draggedItem, x,
									y);
							if (mapDropRet == 1) {

							} else {
								if ((e = armorView.equipItem(draggedItem, x, y,
										inventoryView)) != null) {
									if ((e = inventoryView.storeItem(e,
											armorView)) != null) {
										if (draggingSource == inventoryView) {
											map.dropItem(e, x, y);
										} else if (draggingSource == armorView) {
											armorView.equipItem(draggedItem,
													draggingOldX, draggingOldY,
													inventoryView);
										}
									}
								}
							}
						} catch (SlickException e1) {
							e1.printStackTrace();
						}
						dragging = false;
						draggingSource = null;
						draggedItem = null;
						draggingOldX = 0;
						draggingOldY = 0;
					}
				}
				dragging = false;
				draggedItem = null;
				// armorView.addFists();
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
		}
		if (minimap != null) {
			if (newX >= minimap.positionX
					&& newX <= minimap.positionX + minimap.WIDTH
					&& newY >= minimap.positionY
					&& newY <= minimap.positionY + minimap.HEIGHT) {
				// mouseOverMinimap = true;
				mouseOverMinimap = false; // uncomment when minimap is activated
				mouseOverChat = false;
			} else {
				mouseOverChat = false;
				mouseOverMinimap = false;
			}
		} else {
			mouseOverChat = false;
			mouseOverMinimap = false;
		}

		// Im inventory view
		if (newX > GAME_ENVIRONMENT_WIDTH
				&& newX < GAME_ENVIRONMENT_WIDTH + INVENTORY_WIDTH
				&& newY > ARMOR_HEIGHT
				&& newY < ARMOR_HEIGHT + INVENTORY_HEIGHT) {
			if(inventoryView != null) 
			inventoryView.showDescription(newX, newY);
		} else {
			if(inventoryView != null) 
			inventoryView.endShowingDescription();
		}

		// Im Armor view
		if (newX > GAME_ENVIRONMENT_WIDTH
				&& newX < GAME_ENVIRONMENT_WIDTH + INVENTORY_WIDTH && newY > 0
				&& newY < ARMOR_HEIGHT) {
			if(armorView != null) 
			armorView.showDescription(newX, newY);
		} else {
			if(armorView != null)
			armorView.endShowingDescription();
		}

	}

	@Override
	public void mouseWheelMoved(int scroll) {

		/* Enable chat scrolling if mouse if over chat */
		if (mouseOverChat) {
			chat.scroll(scroll);
		}
	}

	/**
	 * Returns the currently active Fight Thread or null if no fight is active.
	 * (thread that needs to be started more than once).
	 * 
	 * @return fight Instance
	 */
	public Thread getFightThread() {
		return fightThread;
	}

	/**
	 * Sets fight Thread.
	 * 
	 * @param t
	 */
	public void setFightThread(Thread t) {
		fightThread = t;
	}

	/**
	 * Called by gameEnvironment when a fight ends.<br>
	 * Takes action based on who lost the fight.
	 * 
	 * @param looser
	 */
	public void fightEnds(Creature looser) {

		fightThread = null;
		// do we need for thread to join - they should end before game anyhow?!?

		// do stuff with looser
		if (looser == this.player) {
			this.player.resetPlayerPosition();
		} else if (looser instanceof Monster) {
			map.removeContentInFrontOfPlayer();
		} else {

			// YOU HAVE WON THE GAME - SWITCH GAME STATE
		}
	}

	/**
	 * Checks if the computer is the lobbyHost.
	 * 
	 * @return true if this computer hosted the lobby
	 */
	public boolean isLobbyHost() {
		return this.lobbyHost;
	}

	/**
	 * @return instance of network Manager
	 */
	public NetworkManager getNetworkManager() {
		return this.networkManager;
	}

	/**
	 * @return the own Player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * @return the opposing Player
	 */
	public Player getOpponent() {
		return this.opponent;
	}

	/**
	 * Set loading boolean. Used by thread that loads game ressources.
	 * 
	 * @param loading
	 */
	public synchronized void setLoading(boolean loading) {
		this.loading = loading;
	}

	/**
	 * Set by map Class once all map data has been loaded.
	 * 
	 * @param mapSet
	 */
	public synchronized void setMapSet(boolean mapSet) {
		this.mapSet = mapSet;
	}

	/**
	 * Set to end to display end screen when fight has finished.
	 * 
	 * @param string
	 */
	public synchronized void setEnd(String string) {
		this.endScreen = string;
	}
	
	/**
	 * @return game Container
	 */
	public GameContainer getGameContainer () {
		return this.container;
	}

	/**Quits the game.
	 * 
	 */
	public synchronized void quitGame() {
		this.running = false;
	}

	/**Resets game after a round has finished.<br>
	 * Has no effect if no Instance of Game exists yet.
	 */
	public static void reset() {
		if (Game.INSTANCE != null) {
			Game.INSTANCE = null;
			Map.reset();
			Maze.reset();
			ResourceManager.reset();
			Configloader.reset();
			InventoryView.reset();
			AttackFactory.reset();
		}
	}
}
