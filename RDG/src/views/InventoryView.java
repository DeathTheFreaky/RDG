package views;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Equippment;
import elements.Potion;

/**InventoryView extends a View in the Inventory context.
 * 
 * @see View
 */
public class InventoryView extends View {

	/* Some Values for positioning this View */
	public int ORIGIN_X;
	public int ORIGIN_Y;
	private final int border = 5;
	
	/* Collection for all Items, saved in Inventory */
	private LinkedList<Equippment> weapons;
	private LinkedList<Equippment> armor;
	private LinkedList<Potion> potions;

	/**Constructs an InventoryView passing its origin as single x and y coordinates in tile numbers.
	 * Dimension will be set to default values in pixels.<br><br>
	 * InventoryView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @throws SlickException
	 * @see InventoryView
	 */
	public InventoryView(String contextName, int originX, int originY)
			throws SlickException {
		this(contextName, new Point(originX, originY));
	}

	/**Constructs an InventoryView passing its origin as a Point in tile numbers.
	 * Dimension will be set to default values in pixels.<br><br>
	 * InventoryView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @throws SlickException
	 * @param contextName
	 * @param origin
	 * @throws SlickException
	 * @see InventoryView
	 */
	public InventoryView(String contextName, Point origin)
			throws SlickException {
		this(contextName, origin, new Dimension(640, 480));
	}

	/**Constructs an InventoryView passing its origin as single x and y coordinates in tile numbers 
	 * and its Dimension as single x and y coordinates in pixels.<br><br>
	 * InventoryView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @throws SlickException
	 * @see InventoryView
	 */
	public InventoryView(String contextName, int originX, int originY,
			int width, int height) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height));
	}

	/**Constructs an InventoryView passing its origin as a Point in tile numbers 
	 * and its Dimension as a Dimension in pixels.<br><br>
	 * InventoryView extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @param size
	 * @throws SlickException
	 * @see InventoryView
	 */
	public InventoryView(String contextName, Point origin, Dimension size)
			throws SlickException {
		super(contextName, origin, size);

		ORIGIN_X = origin.x * GameEnvironment.BLOCK_SIZE;
		ORIGIN_Y = 240;
		
		weapons = new LinkedList<Equippment>();
		armor = new LinkedList<Equippment>();
		potions = new LinkedList<Potion>();
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		graphics.setColor(new Color(0.2f, 0.2f, 0.2f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y, size.width, size.height);
		graphics.setColor(new Color(0f, 0f, 1f));
		graphics.fillRect(ORIGIN_X + border, ORIGIN_Y + border, size.width - 2
				* border, size.height - 2 * border);
	}

	@Override
	public void update() {

	}

}
