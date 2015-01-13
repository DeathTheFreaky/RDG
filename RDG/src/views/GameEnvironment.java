package views;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import elements.Creature;
import elements.Element;
import fighting.Fight;
import gameEssentials.Game;
import gameEssentials.Player;
import general.Common;
import general.Enums.ImageSize;
import general.ResourceManager;

/**
 * GameEnvironment extends a View in the GameEnvironment context.
 * 
 * @see View
 */
public class GameEnvironment extends View {

	/* Defines a Block Size in pixels! */
	public static final int BLOCK_SIZE = 32;

	/* Reference to the player, for which the GameEnvironment is shown */
	private Player player;
	
	/* Reference for drawing opponent */
	private Player opponent;
	
	/*
	 * Reference to the Shapes, which are shown in the GameEnvironment. Scope
	 * Elements are passed from Map Class.
	 */
	private Element[][] backgroundScope;
	private Element[][] overlayScope;
	
	/* instance of a Fight - to be loaded into fight thread */
	private Fight fightInstance = null;
	
	/* the base Game class */
	private Game game;
	
	/* Colors */
	private final Color WHITE = new Color(1f, 1f, 1f, 0.5f);
	private final Color BLACK = new Color(0f, 0f, 0f);
	
	/* resource manager for stat images */
	private ResourceManager resources = new ResourceManager().getInstance();

	/**
	 * Constructs a GameEnvironment passing its origin as single x and y
	 * coordinates in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
	 * GameEnvironment extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param player
	 * @throws SlickException
	 * @see GameEnvironment
	 */
	public GameEnvironment(String contextName, int originX, int originY,
			Player player, Player opponent, ArmorView armorView, Game game, Chat chat) throws SlickException {
		this(contextName, new Point(originX, originY), player, opponent, armorView, game, chat);
	}

	/**
	 * Constructs a GameEnvironment passing its origin as a Point in tile
	 * numbers.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
	 * GameEnvironment extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @param player
	 * @throws SlickException
	 * @see GameEnvironment
	 */
	public GameEnvironment(String contextName, Point origin, Player player, Player opponent, ArmorView armorView, Game game, Chat chat)
			throws SlickException {
		this(contextName, origin, new Dimension(640, 480), player, opponent, armorView, game, chat);
	}

	/**
	 * Constructs a GameEnvironment passing its origin in tiles and dimension in
	 * pixels as single x and y coordinates.<br>
	 * <br>
	 * GameEnvironment extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @param player
	 * @throws SlickException
	 * @see GameEnvironment
	 */
	public GameEnvironment(String contextName, int originX, int originY,
			int width, int height, Player player, Player opponent, ArmorView armorView, Game game, Chat chat) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height), player, opponent, armorView, game, chat);
	}

	/**
	 * Constructs a GameEnvironment passing its origin as point in tile numbers
	 * and its dimension in pixels as Dimension.<br>
	 * <br>
	 * GameEnvironment extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @param size
	 * @param player
	 * @throws SlickException
	 * @see GameEnvironment
	 */
	public GameEnvironment(String contextName, Point origin, Dimension size,
			Player player, Player opponent, ArmorView armorView, Game game, Chat chat) throws SlickException {
		super(contextName, origin, size);
		
		this.game = game;
		this.player = player;
		this.opponent = opponent;
				
		if (size.width % BLOCK_SIZE != 0) {
			System.out.println("WATCH OUT! GameEnvironment only works well, "
					+ "if the width is a multiple of " + BLOCK_SIZE + "!");
		}

		if (size.height % BLOCK_SIZE != 0) {
			System.out.println("WATCH OUT! GameEnvironment only works well, "
					+ "if the height is a multiple of " + BLOCK_SIZE + "!");
		}

		/* Sets the last point down right in tile numbers (not pixels!) */
		downright.x = (int) size.width / BLOCK_SIZE;
		downright.y = (int) size.height / BLOCK_SIZE;

		this.fightInstance = new Fight(this.origin, this.size, this, player, armorView, chat);
		
		update();
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		
		graphics.setFont(resources.DEFAULT_FONTS.get("stats"));
		
		/* if the player is not in a current fight */
		if (!fightInstance.isInFight()) {
			
			/* downright is refered to in tile numbers */
			for (int i = 0; i < downright.x; i++) {
				for (int j = 0; j < downright.y; j++) {
					if ((1 + i) * BLOCK_SIZE <= size.width
							&& (1 + j) * BLOCK_SIZE <= size.height) {
						/*
						 * draw all image in GameEnvironment as 32x32, no matter
						 * if they are 32x32 or 64x64 sized
						 */
						if (backgroundScope[i][j] != null) {
							graphics.drawImage(
									backgroundScope[i][j].getImage(
											ImageSize.d32x32).getScaledCopy(32,
											32), origin.x * BLOCK_SIZE + i
											* BLOCK_SIZE, origin.y * BLOCK_SIZE
											+ j * BLOCK_SIZE);
						}
						if (overlayScope[i][j] != null) {
							graphics.drawImage(
									overlayScope[i][j].getImage(
											ImageSize.d32x32).getScaledCopy(32,
											32), origin.x * BLOCK_SIZE + i
											* BLOCK_SIZE, origin.y * BLOCK_SIZE
											+ j * BLOCK_SIZE);
						}
					} else {
						System.out.println("Image would extend the scope "
								+ "of the GameEnvironment!");
						System.out.println("Something wrong is happening - "
								+ "Fix that!");
					}
				}
			}
			
			/* draw player and opponent in player's scope */
			player.draw(container, graphics, opponent);
			
			/* draw the players stats */
			float stats[] = ArmorView.getValues();
			graphics.setColor(WHITE);
			graphics.fillRect(0, 0, Game.GAME_ENVIRONMENT_WIDTH, 31);
			graphics.setColor(BLACK);
			/*String st = "Player Stats:\nATTACK:" + stats[ArmorView.ATTACK] + "\nSPEED:"
					+ stats[ArmorView.SPEED] + "\nACCURACY:" + stats[ArmorView.ACCURACY] + 
					"\nDEFENSE:" + stats[ArmorView.DEFENSE];*/
			
			String accuracy = Common.round(this.player.getOrAccuracy(), 1);
			String speed = Common.round(this.player.getOrSpeed(), 1);
			String strength = Common.round(this.player.getOrStrength(), 1);
			
			graphics.drawString(this.player.NAME, 6, 5);
			graphics.drawImage(resources.IMAGES.get("Accuracy_Stats").getScaledCopy(32,32), Game.GAME_ENVIRONMENT_WIDTH - 80, 0);
			graphics.drawString(accuracy, Game.GAME_ENVIRONMENT_WIDTH - 45, 7);
			graphics.drawImage(resources.IMAGES.get("Strength_Stats").getScaledCopy(32,32), Game.GAME_ENVIRONMENT_WIDTH - 160, 0);
			graphics.drawString(speed, Game.GAME_ENVIRONMENT_WIDTH - 125, 7);
			graphics.drawImage(resources.IMAGES.get("Speed_Stats").getScaledCopy(32,32), Game.GAME_ENVIRONMENT_WIDTH - 240, 0);
			graphics.drawString(strength, Game.GAME_ENVIRONMENT_WIDTH - 205, 7);
			
		} else {	// show Fight
			fightInstance.draw(container, graphics);
		}
	}

	@Override
	public void update() {
		backgroundScope = map.getBackgroundScope();
		overlayScope = map.getOverlayScope();
	}
	
	
	/**
	 * Starts a Fight with the set creature as enemy.
	 * 
	 * @param creature - the enemy
	 * @throws SlickException 
	 * @throws InterruptedException 
	 */
	public void startFight(Creature enemy) throws SlickException {
		
		/* load fight instance into new thread for fight to be carried out */
		if (game.getFightThread() == null) {
			Thread thread = new Thread(fightInstance);
			game.setFightThread(thread);
		}
		
		/* start the fight and set the enemy */
		game.getFightThread().start();
		fightInstance.setEnemy(enemy);
	}
	
	/**Called by fight thread to signal that it has ended.
	 * @param looser
	 */
	public void fightEnds(Creature looser) {
		game.fightEnds(looser);
	}
	
	/**Returns the currently active Fight Instance or null if no fight is active.
	 * (thread that needs to be started more than once).
	 * @return fight Instance
	 */
	public Fight getFightInstance() {
		return fightInstance;
	}
}