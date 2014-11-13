package worthy;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class InventoryView extends View {

	/* Some Values for positioning this View */
	public int ORIGIN_X;
	public int ORIGIN_Y;
	private final int border = 5;
	
	/* Collection for all Items, saved in Inventory */
	private LinkedList<Equippment> weapons;
	private LinkedList<Equippment> armor;
	private LinkedList<Potion> potions;
	
	
	
	

	public InventoryView(String contextName, int originX, int originY)
			throws SlickException {
		this(contextName, new Point(originX, originY));
	}

	public InventoryView(String contextName, Point origin)
			throws SlickException {
		this(contextName, origin, new Dimension(640, 480));
	}

	public InventoryView(String contextName, int originX, int originY,
			int width, int height) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height));
	}

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
