package worthy;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**GameEnvironment extends a View in the GameEnvironment context.
 * 
 * @see View
 */
public class GameEnvironment extends View {

	/* Defines a Block Size in pixels! */
	public static final int BLOCK_SIZE = 32;

	/* Reference to the player, for which the GameEnvironment is shown */
	private Player player;

	/* Reference to the Shapes, which are shown in the GameEnvironment. Scope Elements are passed from Map Class. */
	private Element[][] scope;

	/**Constructs a GameEnvironment passing its origin as single x and y coordinates in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br><br>
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
			Player player) throws SlickException {
		this(contextName, new Point(originX, originY), player);
	}

	/**Constructs a GameEnvironment passing its origin as a Point in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br><br>
	 * GameEnvironment extends View. 
	 * 
	 * @param contextName
	 * @param origin
	 * @param player
	 * @throws SlickException
	 * @see GameEnvironment
	 */
	public GameEnvironment(String contextName, Point origin, Player player)
			throws SlickException {
		this(contextName, origin, new Dimension(640, 480), player);
	}

	/**Constructs a GameEnvironment passing its origin in tiles and dimension in pixels as single x and y coordinates.<br><br>
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
			int width, int height, Player player) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height), player);
	}

	/**Constructs a GameEnvironment passing its origin as point in tile numbers and its dimension in pixels as Dimension.<br><br>
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
			Player player) throws SlickException {
		super(contextName, origin, size);
		this.player = player;

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

		update();
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		/* downright is refferd to in tile numbers */
		for (int i = 0; i < downright.x; i++) {
			for (int j = 0; j < downright.y; j++) {
				if ((1 + i) * BLOCK_SIZE <= size.width
						&& (1 + j) * BLOCK_SIZE <= size.height) {
					graphics.drawImage(scope[i][j].getImage(), origin.x * BLOCK_SIZE + i * BLOCK_SIZE,
							origin.y * BLOCK_SIZE + j * BLOCK_SIZE);
				} else {
					System.out.println("Image would extend the scope "
							+ "of the GameEnvironment!");
					System.out.println("Something wrong is happening - "
							+ "Fix that!");
				}
			}
		}
		
		player.draw(container, graphics);
	}

	@Override
	public void update() {
		scope = map.getScope();
	}

}