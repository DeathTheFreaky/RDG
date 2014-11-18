package views;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Armament;
import elements.Equipment;
import elements.Potion;
import elements.Weapon;
import general.Enums.Armor;
import general.Enums.ItemClasses;
import general.Enums.WeaponTypes;
import general.ResourceManager;

/**
 * InventoryView extends a View in the Inventory context.
 * 
 * @see View
 */
public class InventoryView extends View {

	/* Some Values for positioning this View */
	public int ORIGIN_X;
	public int ORIGIN_Y;
	private final int border = 5;

	/* Collection for all Items, saved in Inventory */
	private LinkedList<Weapon> weapons;
	private LinkedList<Equipment> armor;
	private LinkedList<Potion> potions;

	/* ResourceManager */
	private ResourceManager resources;

	/**
	 * Constructs an InventoryView passing its origin as single x and y
	 * coordinates in tile numbers. Dimension will be set to default values in
	 * pixels.<br>
	 * <br>
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

	/**
	 * Constructs an InventoryView passing its origin as a Point in tile
	 * numbers. Dimension will be set to default values in pixels.<br>
	 * <br>
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

	/**
	 * Constructs an InventoryView passing its origin as single x and y
	 * coordinates in tile numbers and its Dimension as single x and y
	 * coordinates in pixels.<br>
	 * <br>
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

	/**
	 * Constructs an InventoryView passing its origin as a Point in tile numbers
	 * and its Dimension as a Dimension in pixels.<br>
	 * <br>
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

		resources = new ResourceManager().getInstance();

		weapons = new LinkedList<Weapon>();
		armor = new LinkedList<Equipment>();
		potions = new LinkedList<Potion>();

		/* for testing */
		armor.add(new Weapon("Dolch", resources.IMAGES.get("M_Weapon"), 0f, 0f,
				0f, 0f, ItemClasses.MEDIUM, WeaponTypes.SINGLEHAND, 0));
		armor.add(new Weapon("Schwert", resources.IMAGES.get("S_Weapon"), 0f,
				0f, 0f, 0f, ItemClasses.MEDIUM, WeaponTypes.SINGLEHAND, 0));

		armor.add(new Armament("Helmet", resources.IMAGES.get("Helmet"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.HEAD));
		armor.add(new Armament("Chest", resources.IMAGES.get("Cuirass"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.CHEST));
		armor.add(new Armament("Arms", resources.IMAGES.get("Arms"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.ARMS));
		armor.add(new Armament("Shoes", resources.IMAGES.get("Shoes"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.FEET));
		armor.add(new Armament("Legs", resources.IMAGES.get("Legs"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.LEGS));
		/* testing */
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		graphics.setColor(new Color(0.2f, 0.2f, 0.2f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y, size.width, size.height);
		graphics.setColor(new Color(0f, 0f, 1f));
		graphics.fillRect(ORIGIN_X + border, ORIGIN_Y + border, size.width - 2
				* border, size.height - 2 * border);

		int x = 0, y = 0;

		for (Equipment a : armor) {
			graphics.drawImage(a.getImage(), 10 + ORIGIN_X + x * 40, 10
					+ ORIGIN_Y + y * 40);
			if (x == 3) {
				x = 0;
				y++;
			} else {
				x++;
			}
		}

	}

	@Override
	public void update() {

	}

	/* 160:240 */
	public Equipment getEquipment(int mouseX, int mouseY) {
		if (mouseX > ORIGIN_X && mouseX < ORIGIN_X + size.width
				&& mouseY > ORIGIN_Y && mouseY < ORIGIN_Y + size.height) {

			int x = 0, y = 0;
			for (int i = 0; i < armor.size(); i++) {

				if (mouseX > ORIGIN_X + x * 40
						&& mouseX < ORIGIN_X + x * 40 + 40
						&& mouseY > ORIGIN_Y + y * 40
						&& mouseY < ORIGIN_Y + y * 40 + 40) {
					break;
				}

				if (i % 4 == 0 && i != 0) {
					y++;
					x = 0;
				} else {
					x++;
				}
			}
			
			Equipment e = armor.get(x + y*4);
			armor.remove(x + y*4);
			
			return e;
		}
		return null;
	}
	
	public void storeEquipment(Equipment equipment) {
		armor.add(equipment);
	}

}
