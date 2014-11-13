package views;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Equippment;
import general.ResourceManager;
import general.Enums.Armor;

/**ArmorView extends a View in the Inventory context.
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
	HashMap<Armor, Equippment> armor1;
	HashMap<Armor, Equippment> armor2;

	/* Collection for the armor, is used for iterating through all armor */
	Collection<Equippment> equippment1;
	Collection<Equippment> equippment2;

	/* Factory and Resource Classes */
	private ResourceManager resources;

	public ArmorView(String contextName, int originX, int originY)
			throws SlickException {
		this(contextName, new Point(originX, originY));
	}

	public ArmorView(String contextName, Point origin) throws SlickException {
		this(contextName, origin, new Dimension(640, 480));
	}

	public ArmorView(String contextName, int originX, int originY, int width,
			int height) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height));
	}

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

		armor1 = new HashMap<Armor, Equippment>(7);
		equippment1 = armor1.values();
		armor2 = new HashMap<Armor, Equippment>(7);
		equippment2 = armor2.values();
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		graphics.setColor(new Color(0.5f, 0.5f, 0.5f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y, size.width, tabHeight+5);
		
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

		graphics.drawImage(resources.ARMOR_BACKGROUND, ORIGIN_X, ORIGIN_Y+tabHeight+10);

		graphics.drawImage(resources.HELMET, ORIGIN_X + 100, ORIGIN_Y + 39);

		if (set) {
			for (Equippment e : equippment1) {
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
			for (Equippment e : equippment2) {
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

	public void equip(Equippment equippment) {
		if (set) {
			armor1.put(equippment.getArmorType(), equippment);
			if (equippment.getArmorType() == Armor.SUB_WEAPON
					&& armor1.get(Armor.MAIN_WEAPON) != null) {
				armor1.remove(Armor.MAIN_WEAPON);
			}
			equippment1 = armor1.values();
		} else {
			armor2.put(equippment.getArmorType(), equippment);
			if (equippment.getArmorType() == Armor.SUB_WEAPON
					&& armor2.get(Armor.MAIN_WEAPON) != null) {
				armor2.remove(Armor.MAIN_WEAPON);
			}
			equippment2 = armor2.values();
		}
	}
	
	public void switchToSet(int set) {
		if(set == 1) {
			this.set = true;
		}else if(set == 2) {
			this.set = false;
		}
	}
	
	public void changeTab(int x, int y) {
		if(x > tab1X && x < tab1X+tabWidth && y > tab1Y && y < tab1Y+tabHeight) {
			this.set = true;
		}else if(x > tab2X && x < tab2X+tabWidth && y > tab2Y && y < tab2Y+tabHeight) {
			this.set = false;
		}
	}
	
}
