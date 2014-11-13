package worthy;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import enums.Enums.Updates;

/**Game class stores the main configuration parameters of a game and defines what needs to be done in each iteration of the game loop.
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

	/* every milliseconds an Update is made */
	private final int UPDATE = 200;
	/* how many milliseconds passed until the next Update */
	private int timeToUpdate = 0;

	/* Origins of the different Views in tile numbers */
	private Point gameEnvironmentOrigin;
	private Point chatOrigin;
	
	/* flag, if the mouse is over the chat (for scrolling) */
	boolean mouseOverChat = false;

	/* The Game for Player ... */
	private String playerName;
	private Player player;

	/* Declares all Views for the Game */
	private GameEnvironment gameEnvironment;
	private Chat chat;

	/* Map which is needed for each Player */
	private Map map;

	/* Declare all classes, we need for the game (Factory, Resourceloader) */
	private ResourceManager resourceManager;
	private GroundFactory groundFactory;

	/**Construct game and sets Playername as "Find out if its Player 1 or Player2"
	 * 
	 * @param title
	 * @see Game
	 */
	public Game(String title) {
		this(title, "Find out if its Player1 or Player2");
	}
	
	/**Construct game and set playerName
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
		
		/* Points in tile numbers */
		gameEnvironmentOrigin = new Point(0, 0);
		chatOrigin = new Point(0, 12);

		player = new Player(playerName, gameEnvironmentOrigin);
		map = new Map().getInstance();
		map.setPlayer(player);
		map.fillMap();
		

		/* Initialize Factory and Manager classes! */
		groundFactory = new GroundFactory().setUpFactory();
		resourceManager = new ResourceManager().getInstance();

		/* Dimension is specified in pixels */
		gameEnvironment = new GameEnvironment("GameEnvironment",
				gameEnvironmentOrigin, new Dimension(480, 384), player);
		
		chat = new Chat("Chat", chatOrigin, new Dimension(480, 96), container);
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
	}

	@Override
	public void keyPressed(int key, char c) {

		/* Key Values for Players Movement! (a,s,d,w) */
		if (key == 30 || key == 31 || key == 32 || key == 17) {
			player.update(key, Updates.KEY_PRESSED);
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

	}
	
	@Override
	public void mouseMoved(int oldX, int oldY, int newX, int newY) {
		/* Check if mouse is over chat to enable chat scrolling. X and Y in pixels! */
		if(newX >= 0 && newX <= CHAT_WIDTH && newY > GAME_ENVIRONMENT_HEIGHT && newY <= HEIGHT) {
			mouseOverChat = true;
		}else {
			mouseOverChat = false;
		}
	}
	
	@Override
	public void mouseWheelMoved(int scroll) {
		/* Enable chat scrolling if mouse if over chat */
		if(mouseOverChat) {
			chat.scroll(scroll);
		}
	}
}
