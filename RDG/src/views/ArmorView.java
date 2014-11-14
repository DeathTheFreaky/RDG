package views;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Equipment;
import general.ResourceManager;
import general.Enums.Armor;

/**ArmorView extends a View in the Armor context.
 * 
 * @see View
 */
public class ArmorView extends View {
	
	/* Values for Positioning of the View */
	public int ORIGIN_X;
	public int ORIGIN_Y;
	private int space = 2;
	private int tabWidth = 30;
	private int tabHeight = 25;
	
	/* Values for positioning the Tabs */
	private int tab1X;
	private int tab1Y;
	private int tab2X;
	private int tab2Y;
	private int textPositionX;
	private int textPositionY;

	/* Represents Set1 or Set2 */
	private boolean set = true;

	/* HashMap for all equipped Armor */
	HashMap<Armor, Equipment> armor1;
	HashMap<Armor, Equipment> armor2;

	/* Collection for the armor, is used for iterating through all armor */
	Collection<Equipment> equipment1;
	Collection<Equipment> equipment2;

	/* Factory and Resource Classes */
	private ResourceManager resources;

	/**Constructs an ArmorView passing its origin as single x and y coordinates in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br><br>
	 * ArmorView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @throws SlickException
	 * @see ArmorView
	 */
	public ArmorView(String contextName, int originX, int originY)
			throws SlickException {
		this(contextName, new Point(originX, originY));
	}

	/**Constructs an ArmorView passing its origin as a Point in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br><br>
	 * ArmorView extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @throws SlickException
	 * @see ArmorView
	 */
	public ArmorView(String contextName, Point origin) throws SlickException {
		this(contextName, origin, new Dimension(640, 480));
	}

	/**Constructs an ArmorView passing its origin as single x and y coordinates in tile numbers
	 * and its Dimension as single x and y coordinates in pixels.<br>
	 * Dimension will be set to default values in pixels.<br><br>
	 * ArmorView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @throws SlickException
	 * @see ArmorView
	 */
	public ArmorView(String contextName, int originX, int originY, int width,
			int height) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height));
	}

	/**Constructs an ArmorView passing its origin as a Point in tile numbers 
	 * and its Dimension as a Dimension.<br>
	 * Dimension will be set to default values in pixels.<br><br>
	 * ArmorView extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @param size
	 * @throws SlickException
	 * @see ArmorView
	 */
	public ArmorView(String contextName, Point origin, Dimension size)
			throws SlickException {
		super(contextName, origin, size);

		ORIGIN_X = origin.x * GameEnvironment.BLOCK_SIZE;
		ORIGIN_Y = origin.y * GameEnvironment.BLOCK_SIZE;
		
		tab1X = ORIGIN_X + space;
		tab1Y = ORIGIN_Y + 5;
		tab2X = ORIGIN_X + 2*space + tabWidth;
		tab2Y = ORIGIN_Y + 5;
		textPositionX = ORIGIN_X + size.width - 50;
		textPositionY = ORIGIN_Y + 10;

		resources = new ResourceManager().getInstance();

		armor1 = new HashMap<Armor, Equipment>(7);
		equipment1 = armor1.values();
		armor2 = new HashMap<Armor, Equipment>(7);
		equipment2 = armor2.values();
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		graphics.setColor(new Color(0.5f, 0.5f, 0.5f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y, size.width, tabHeight+5);
		
		/* draw sets */
		graphics.setColor(new Color(1f, 0f, 0f));
		if(set) {
			graphics.fillRect(tab1X, tab1Y, tabWidth, tabHeight);
			graphics.fillRect(tab2X, tab2Y, tabWidth, tabHeight-2);
			graphics.setColor(new Color(0f, 0f, 0f));
			graphics.drawString("SET 1", textPositionX, textPositionY);
		}else {
			graphics.fillRect(tab1X, tab1Y, tabWidth, tabHeight-2);
			graphics.fillRect(tab2X, tab2Y, tabWidth, tabHeight);
			graphics.setColor(new Color(0f, 0f, 0f));
			graphics.drawString("SET 2", textPositionX, textPositionY);
		}
		
		
		graphics.setColor(new Color(1f, 0f, 0f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y+30, size.width, size.height-2*(tabHeight-5));
		
		/* draw warrior image */
		graphics.drawImage(resources.ARMOR_BACKGROUND, ORIGIN_X, ORIGIN_Y+tabHeight+10);

		graphics.drawImage(resources.HELMET, ORIGIN_X + 100, ORIGIN_Y + 39);

		/* draw equipment */
		if (set) {
			for (Equipment e : equipment1) {
				switch (e.getArmorType()) {
				case MAIN_WEAPON:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case SUB_WEAPON:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case HEAD:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case CHEST:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case ARMS:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case LEGS:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case FEET:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				}
			}
		} else {
			for (Equipment e : equipment2) {
				switch (e.getArmorType()) {
				case MAIN_WEAPON:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case SUB_WEAPON:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case HEAD:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case CHEST:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case ARMS:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case LEGS:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				case FEET:
					graphics.drawImage(e.getImage(), ORIGIN_X, ORIGIN_Y);
					break;
				}
			}
		}
		
		graphics.setColor(new Color(0.5f, 0.5f, 0.5f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y+size.height-tabHeight-5, size.width, tabHeight+5);
	}

	@Override
	public void update() {

	}

	/**Outfits an equipment item.
	 * 
	 * @param equipment
	 */
	public void equip(Equipment equipment) {
		if (set) {
			/* replaces existing armor of same type or add armor if none is present yet */
			armor1.put(equipment.getArmorType(), equipment);
			/* removes Main Weapon if already equipped and new Sub Weapon is about to be equipped  */
			if (equipment.getArmorType() == Armor.SUB_WEAPON
					&& armor1.get(Armor.MAIN_WEAPON) != null) {
				armor1.remove(Armor.MAIN_WEAPON);
			}
			/* store all equipped armors to set 1 */
			equipment1 = armor1.values();
		} else {
			armor2.put(equipment.getArmorType(), equipment);
			if (equipment.getArmorType() == Armor.SUB_WEAPON
					&& armor2.get(Armor.MAIN_WEAPON) != null) {
				armor2.remove(Armor.MAIN_WEAPON);
			}
			equipment2 = armor2.values();
		}
	}
	
	/**DEPRECATED!!!<br>
	 * Switches between sets 1 and 2. 
	 * 
	 * @param set
	 */
	public void switchToSet(int set) {
		if(set == 1) {
			this.set = true;
		}else if(set == 2) {
			this.set = false;
		}
	}
	
	/**Switches between Equipment tabs 1 and 2.
	 * 
	 * @param x
	 * @param y
	 */
	public void changeTab(int x, int y) {
		if(x > tab1X && x < tab1X+tabWidth && y > tab1Y && y < tab1Y+tabHeight) {
			this.set = true;
		}else if(x > tab2X && x < tab2X+tabWidth && y > tab2Y && y < tab2Y+tabHeight) {
			this.set = false;
		}
	}
	
}
