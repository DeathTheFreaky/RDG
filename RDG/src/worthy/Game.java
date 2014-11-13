package worthy;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import worthy.Player.Updates;

public class Game extends BasicGame {

	/* Game Variables */
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

	/* every milliseconds an Update is made */
	private final int UPDATE = 200;
	/* how many milliseconds passed until the next Update */
	private int timeToUpdate = 0;

	/* Origins of the different Views */
	private Point gameEnvironmentOrigin, chatOrigin, armorViewOrigin,
			inventoryViewOrigin;

	/* flag, if the mouse is over the chat (for scrolling) */
	boolean mouseOverChat = false;

	/* The Game for Player ... */
	private String playerName;
	private Player player;

	/* Declares all Views for the Game */
	private GameEnvironment gameEnvironment;
	private Chat chat;
	private ArmorView armorView;
	private InventoryView inventoryView;

	/* Map which is needed for each Player */
	private Map map;

	/* Declare all classes, we need for the game (Factory, Resourceloader) */
	// private ResourceManager resourceManager;
	// private GroundFactory groundFactory;

	public Game(String title) {
		this(title, "Find out if its Player1 or Player2");
	}

	public Game(String title, String playerName) {
		super(title);
		this.playerName = playerName;
	}

	@Override
	public void init(GameContainer container) throws SlickException {

		gameEnvironmentOrigin = new Point(0, 0);
		chatOrigin = new Point(0, 12);
		armorViewOrigin = new Point(15, 0);
		inventoryViewOrigin = new Point(15, 12);

		player = new Player(playerName, gameEnvironmentOrigin);
		map = new Map().getInstance();
		map.setPlayer(player);
		map.fillMap();

		/* Initialize Factory and Manager classes! */
		new GroundFactory().setUpFactory();
		new ResourceManager().getInstance();

		gameEnvironment = new GameEnvironment("GameEnvironment",
				gameEnvironmentOrigin, new Dimension(GAME_ENVIRONMENT_WIDTH,
						GAME_ENVIRONMENT_HEIGHT), player);

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
			player.update();
			gameEnvironment.update();

			timeToUpdate = 0;
		}

		timeToUpdate += delta;
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {

		gameEnvironment.draw(container, g);
		chat.draw(container, g);
		armorView.draw(container, g);
		inventoryView.draw(container, g);
	}

	@Override
	public void keyPressed(int key, char c) {

		/* Key Values for Players Movement! (a,s,d,w) */
		if (key == 30 || key == 31 || key == 32 || key == 17) {
			player.update(key, Updates.KEY_PRESSED);
		} else if (key == 46) {
			chat.focus();
		}
		System.out.println("Key <" + key + ">, char <" + c + ">");
	}

	@Override
	public void keyReleased(int key, char c) {
		/* Key Values for Players Movement! (a,s,d,w) */
		if (key == 30 || key == 31 || key == 32 || key == 17) {
			player.update(key, Updates.KEY_RELEASED);
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		armorView.changeTab(x, y);
	}

	@Override
	public void mouseMoved(int oldX, int oldY, int newX, int newY) {
		if (newX >= 0 && newX <= CHAT_WIDTH && newY > GAME_ENVIRONMENT_HEIGHT
				&& newY <= HEIGHT) {
			mouseOverChat = true;
		} else {
			mouseOverChat = false;
		}
	}

	@Override
	public void mouseWheelMoved(int scroll) {
		if (mouseOverChat) {
			chat.scroll(scroll);
		}
	}

	public static void main(String[] args) throws SlickException {

		AppGameContainer app1 = new AppGameContainer(new Game("Battle Dungeon"));
		app1.setDisplayMode(WIDTH, HEIGHT, false); // Breite, Höhe, ???
		app1.setTargetFrameRate(60); // 60 Frames pro Sekunde
		app1.setAlwaysRender(true); // Spiel wird auch ohne Fokus aktualisiert
		app1.setShowFPS(false);
		app1.start(); // startet die App
	}

}
