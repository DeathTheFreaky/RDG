package views;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Armament;
import elements.Element;
import elements.Equipment;
import elements.Potion;
import elements.Weapon;
import general.Enums.ArmorStatsAttributes;
import general.Enums.ArmorStatsMode;
import general.Enums.ArmorStatsTypes;
import general.Enums.ImageSize;
import general.Enums.Potions;
import general.Enums.WeaponTypes;
import general.ItemFactory;
import general.ResourceManager;
import general.Enums.Armor;

/**
 * ArmorView extends a View in the Armor context.
 * 
 * @see View
 */
/**
 * @author Flo
 *
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

	/* width/height of ArmorImages */
	private final int IMAGE_SIZE = 20;

	/* Represents Set1 or Set2 */
	private boolean set = true;
	
	/* HashMap for all equipped Armor */
	HashMap<Armor, Equipment> armor1;
	HashMap<Armor, Equipment> armor2;
	HashMap<Potions, Potion> potion1_types;
	HashMap<Potions, Potion> potion2_types;

	/* Collection for the armor, is used for iterating through all armor */
	Collection<Equipment> equipment1;
	Collection<Equipment> equipment2;
	Collection<Potion> potions1;
	Collection<Potion> potions2;
	
	/* stores previously equipped weapons to determine if weapon is dragged 
	 * from armory or from inventory */
	Weapon prevWeaponMainSet1 = null;
	Weapon prevWeaponSubSet1 = null;
	Weapon prevWeaponMainSet2 = null;
	Weapon prevWeaponSubSet2 = null;
	
	/* stores previously equipped potions to determine if potion is dragged 
	 * from armory or from inventory */
	Potion prevPotion1Set1 = null;
	Potion prevPotion2Set1 = null;
	Potion prevPotion3Set1 = null;
	Potion prevPotion1Set2 = null;
	Potion prevPotion2Set2 = null;
	Potion prevPotion3Set2 = null;

	/* Factory and Resource Classes */
	private ResourceManager resources;
	
	/* a potion drunk by a player and set in armorView.drinkPotion() */
	Potion selectedPotion = null;
	
	/* fallback weapon: fists */
	Weapon fists1 = null;
	Weapon fists2 = null;

	/**
	 * Constructs an ArmorView passing its origin as single x and y coordinates
	 * in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
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

	/**
	 * Constructs an ArmorView passing its origin as a Point in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
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

	/**
	 * Constructs an ArmorView passing its origin as single x and y coordinates
	 * in tile numbers and its Dimension as single x and y coordinates in
	 * pixels.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
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

	/**
	 * Constructs an ArmorView passing its origin as a Point in tile numbers and
	 * its Dimension as a Dimension.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
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
		tab2X = ORIGIN_X + 2 * space + tabWidth;
		tab2Y = ORIGIN_Y + 5;
		textPositionX = ORIGIN_X + size.width - 50;
		textPositionY = ORIGIN_Y + 10;

		resources = new ResourceManager().getInstance();

		armor1 = new HashMap<Armor, Equipment>(7);
		equipment1 = armor1.values();
		armor2 = new HashMap<Armor, Equipment>(7);
		equipment2 = armor2.values();
		potion1_types = new HashMap<Potions, Potion>(3);
		potions1 = potion1_types.values();
		potion2_types = new HashMap<Potions, Potion>(3);
		potions2 = potion2_types.values();
		
		/* create fists as fallback weapons */
		fists1 = ItemFactory.createWeapon("Fists", 1);
		fists2= ItemFactory.createWeapon("Fists", 1);
		
		fists2.setAsSubWeapon();
		
		armor1.put(Armor.MAIN_WEAPON, fists1);
		armor1.put(Armor.SUB_WEAPON, fists2);
		armor2.put(Armor.MAIN_WEAPON, fists1);
		armor2.put(Armor.SUB_WEAPON, fists2);
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		
		/* grey background */
		graphics.setColor(new Color(0.5f, 0.5f, 0.5f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y, size.width, tabHeight + 5);

		/* draw sets */
		graphics.setColor(new Color(1f, 0f, 0f));
		if (set) {
			graphics.fillRect(tab1X, tab1Y, tabWidth, tabHeight);
			graphics.fillRect(tab2X, tab2Y, tabWidth, tabHeight - 2);
			graphics.setColor(new Color(0f, 0f, 0f));
			graphics.drawString("SET 1", textPositionX, textPositionY);
		} else {
			graphics.fillRect(tab1X, tab1Y, tabWidth, tabHeight - 2);
			graphics.fillRect(tab2X, tab2Y, tabWidth, tabHeight);
			graphics.setColor(new Color(0f, 0f, 0f));
			graphics.drawString("SET 2", textPositionX, textPositionY);
		}
		
		/* red field */
		graphics.setColor(new Color(1f, 0f, 0f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y + 30, size.width, size.height - 2
				* (tabHeight - 5));

		/* draw warrior image */
		graphics.drawImage(resources.IMAGES.get("Armor_Background"), ORIGIN_X,
				ORIGIN_Y + tabHeight + 10);
		
		/* potion section */
		graphics.setColor(new Color(0.5f, 0.5f, 0.5f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y + size.height - tabHeight - 5,
				size.width, tabHeight + 5);
		
		/* test potion section */
		graphics.setColor(new Color(0.25f, 0.25f, 0.25f));
		graphics.fillRect(ORIGIN_X + size.width/2 - tabWidth/2, ORIGIN_Y + size.height - tabHeight - 3,
				tabWidth, tabHeight);
		
		graphics.setColor(new Color(0.25f, 0.25f, 0.25f));
		graphics.fillRect(ORIGIN_X + size.width/2 - tabWidth/2 - tabWidth - 2, ORIGIN_Y + size.height - tabHeight - 3,
				tabWidth, tabHeight);
		
		graphics.setColor(new Color(0.25f, 0.25f, 0.25f));
		graphics.fillRect(ORIGIN_X + size.width/2 + tabWidth/2 + 2, ORIGIN_Y + size.height - tabHeight - 3,
				tabWidth, tabHeight);

		/* draw equipment and potions */
		if (set) {
			for (Equipment e : equipment1) {
				switch (e.getArmorType()) {
				case MAIN_WEAPON:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 20, ORIGIN_Y
							+ 75 + tabHeight + 10);
					break;
				case SUB_WEAPON:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 125, ORIGIN_Y
							+ 60 + tabHeight + 10);
					break;
				case HEAD:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 100, ORIGIN_Y
							+ 5 + tabHeight + 10);
					break;
				case CHEST:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 120, ORIGIN_Y
							+ 30 + tabHeight + 10);
					break;
				case ARMS:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 30, ORIGIN_Y
							+ 45 + tabHeight + 10);
					break;
				case LEGS:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 125, ORIGIN_Y
							+ 100 + tabHeight + 10);
					break;
				case FEET:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 35, ORIGIN_Y
							+ 115 + tabHeight + 10);
					break;
				}
			}
			for (Potion p : potions1) {
				switch (p.POTION_TYPE) {
				
					case POTION1:
						graphics.drawImage(p.getImage(ImageSize.d20x20), ORIGIN_X + size.width/2 - tabWidth - 2 - tabWidth/2 + 6, 
								ORIGIN_Y + size.height - tabHeight - 5 + 5);
						break;
					case POTION2:
						graphics.drawImage(p.getImage(ImageSize.d20x20), ORIGIN_X + size.width/2 - tabWidth/2 + 6, 
								ORIGIN_Y + size.height - tabHeight - 5 + 5);
						break;
					case POTION3:
						graphics.drawImage(p.getImage(ImageSize.d20x20), ORIGIN_X + size.width/2 + tabWidth/2 + 2 + 6, 
								ORIGIN_Y + size.height - tabHeight - 5 + 5);
						break;
				}
			}
		} else {
			for (Equipment e : equipment2) {
				switch (e.getArmorType()) {
				case MAIN_WEAPON:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 20, ORIGIN_Y
							+ 75 + tabHeight + 10);
					break;
				case SUB_WEAPON:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 125, ORIGIN_Y
							+ 60 + tabHeight + 10);
					break;
				case HEAD:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 100, ORIGIN_Y
							+ 5 + tabHeight + 10);
					break;
				case CHEST:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 120, ORIGIN_Y
							+ 30 + tabHeight + 10);
					break;
				case ARMS:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 30, ORIGIN_Y
							+ 45 + tabHeight + 10);
					break;
				case LEGS:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 125, ORIGIN_Y
							+ 100 + tabHeight + 10);
					break;
				case FEET:
					graphics.drawImage(e.getImage(ImageSize.d20x20), ORIGIN_X + 35, ORIGIN_Y
							+ 115 + tabHeight + 10);
					break;
				}
			}
			for (Potion p : potions2) {
				switch (p.POTION_TYPE) {
				
					case POTION1:
						graphics.drawImage(p.getImage(ImageSize.d20x20), ORIGIN_X + size.width/2 - tabWidth - 2 - tabWidth/2 + 6, 
								ORIGIN_Y + size.height - tabHeight - 5 + 5);
						break;
					case POTION2:
						graphics.drawImage(p.getImage(ImageSize.d20x20), ORIGIN_X + size.width/2 - tabWidth/2 + 6, 
								ORIGIN_Y + size.height - tabHeight - 5 + 5);
						break;
					case POTION3:
						graphics.drawImage(p.getImage(ImageSize.d20x20), ORIGIN_X + size.width/2 + tabWidth/2 + 2 + 6, 
								ORIGIN_Y + size.height - tabHeight - 5 + 5);
						break;
				}
			}
		}
	}

	@Override
	public void update() {

	}

	/**
	 * Outfits an equipment item.
	 * 
	 * @param equipment
	 */
	/*public void equip(Equipment equipment) {
		if (set) {
			/*
			 * replaces existing armor of same type or add armor if none is
			 * present yet
			 *
			armor1.put(equipment.getArmorType(), equipment);
			/*
			 * removes Main Weapon if already equipped and new Sub Weapon is
			 * about to be equipped
			 *
			if (equipment.getArmorType() == Armor.SUB_WEAPON
					&& armor1.get(Armor.MAIN_WEAPON) != null) {
				armor1.remove(Armor.MAIN_WEAPON);
			}
			/* store all equipped armors to set 1 *
			equipment1 = armor1.values();
		} else {
			armor2.put(equipment.getArmorType(), equipment);
			if (equipment.getArmorType() == Armor.SUB_WEAPON
					&& armor2.get(Armor.MAIN_WEAPON) != null) {
				armor2.remove(Armor.MAIN_WEAPON);
			}
			equipment2 = armor2.values();
		}
	}*/

	/**
	 * Switches between sets 1 and 2.
	 * 
	 * @param set
	 */
	public void switchSet() {
		System.out.println(this.set);
		System.out.println("Set switch");
		if (set) {
			this.set = false;
		} else {
			this.set = true;
		}
		System.out.println(this.set);
	}

	/**
	 * Switches between Equipment tabs 1 and 2.
	 * 
	 * @param x
	 * @param y
	 */
	public void changeTab(int x, int y) {
		if (x > tab1X && x < tab1X + tabWidth && y > tab1Y
				&& y < tab1Y + tabHeight) {
			this.set = true;
		} else if (x > tab2X && x < tab2X + tabWidth && y > tab2Y
				&& y < tab2Y + tabHeight) {
			this.set = false;
		}
	}
	
	/**
	 * Player drinks a potion.
	 * 
	 * @param potion - Element
	 * @param x - MousePosition
	 * @param y - MousePosition
	 * @return null or the Potion that has to be stored in the Inventory 
	 * 			(if max number of potions already reached during a fight)
	 */
	public Potion drinkPotion(Potion potion, int x, int y, InventoryView inventory) {
		Potion p = potion;
		if (potion == null) {
			return p;
		}
		if (x > ORIGIN_X && x < ORIGIN_X + size.width && y > ORIGIN_Y + tabHeight + 5 
				&& y < ORIGIN_Y + size.height - tabHeight - 5) {
			
			//Send info about taken potion to enemy ? -> only if potion effects enemy?
			
			//set taken potion to be later obtained by fight.java
			this.selectedPotion = p;
			
			p = null; //only for testing purposes
		}
		
		return p;
	}
	
	/**Used by Fight.java to obtain the latest taken potion by the player.
	 * @return the potion taken by a player
	 */
	public Potion getSelectedPotion() {
		
		Potion tempPotion = selectedPotion;
		selectedPotion = null;
		
		return tempPotion;
	}
	
	/**
	 * Equips the Item in a Set.
	 * 
	 * @param element - Element
	 * @param x - MousePosition
	 * @param y - MousePosition
	 * @return null or the Equipment that has to be stored in the Inventory
	 */
	public Element equipItem(Element element, int x, int y, InventoryView inventory) {
		Element e = null;
		if (element == null) {
			return e;
		}
		
		/* check if dragged item is dragged to armor section */
		if (x > ORIGIN_X && x < ORIGIN_X + size.width && y > ORIGIN_Y + tabHeight + 5 
				&& y < ORIGIN_Y + size.height - tabHeight - 5 && element instanceof Equipment) {
				
			/* in case of weapons, detect which weapon shall be replaced */
			if (element instanceof Weapon) {
				
				Weapon thisweapon = (Weapon) element;
				
				Weapon weapon1 = null;
				Weapon weapon2 = null;
				
				boolean returnImmediatlyNull = false;
				boolean returnImmediatly = false;
				boolean fromInsideArmor = false;
				
				/* decide on which side to drop the weapon -> main or sub */
				if ((x - ORIGIN_X) > size.width/2) {
					((Equipment) element).setAsSubWeapon();
					if (thisweapon == fists1) {
						thisweapon = fists2;
					}
				}
				else {
					((Equipment) element).setAsMainWeapon();
					if (thisweapon == fists2) {
						thisweapon = fists1;
					}
				}
				
				/* check if weapon is dragged from inside Armor */
				if (set) {
					
					/* check if dragged weapon is stored inside armor */
					
					if (thisweapon == prevWeaponMainSet1 || thisweapon == prevWeaponSubSet1) {
						fromInsideArmor = true;
					}
					if (thisweapon.NAME.equals("Fists")) {
						fromInsideArmor = true;
					} 
					weapon1 = (Weapon) armor1.get(Armor.MAIN_WEAPON);
					weapon2 = (Weapon) armor1.get(Armor.SUB_WEAPON);
				} else {
					if (thisweapon == prevWeaponMainSet2 || thisweapon == prevWeaponSubSet2) {
						fromInsideArmor = true;
					}
					if (thisweapon.NAME.equals("Fists")) {
						fromInsideArmor = true;
					}
					weapon1 = (Weapon) armor2.get(Armor.MAIN_WEAPON);
					weapon2 = (Weapon) armor2.get(Armor.SUB_WEAPON);
				}
												
				/* return both weapons to inventory if a twohand is added */
				if (thisweapon.TYPE == WeaponTypes.TWOHAND) {
					
					if (set) {
						armor1.remove(Armor.MAIN_WEAPON);
						armor1.remove(Armor.SUB_WEAPON);
					} else {
						armor2.remove(Armor.MAIN_WEAPON);
						armor2.remove(Armor.SUB_WEAPON);
					}		
					
					if (weapon1 != null) {
						inventory.storeItem(weapon1, this);
					}
					if (weapon2 != null) {
						inventory.storeItem(weapon2, this);
					}
					
				} else {
					
					/* exchange two single hand weapons, handle twohands, handle max 1 weapons */
					if (weapon1 != null) {
						
						/* check for two-hand weapons or weapons with maximum of 1 */
						if (weapon1.TYPE == WeaponTypes.TWOHAND || 
								(weapon1.MAX == 1 && weapon1.NAME == thisweapon.NAME)) {
							if (((Equipment) element).getArmorType() == Armor.SUB_WEAPON) {
								returnImmediatly = true;
								e = ((Equipment) element);
							}
						}
						
						/* exchange weapons inside of armory */
						else if (((Equipment) element).getArmorType() == Armor.MAIN_WEAPON 
								&& fromInsideArmor == true) {
							
							Equipment tempWeapon;
							
							if (set) {
								tempWeapon = armor1.get(Armor.MAIN_WEAPON);
								tempWeapon.setAsSubWeapon();
								armor1.put(Armor.SUB_WEAPON, tempWeapon);
								armor1.put(Armor.MAIN_WEAPON, ((Equipment) element));
							} else {
								tempWeapon = armor2.get(Armor.MAIN_WEAPON);
								tempWeapon.setAsSubWeapon();
								armor2.put(Armor.SUB_WEAPON, tempWeapon);
								armor2.put(Armor.MAIN_WEAPON, ((Equipment) element));
							}
							if (weapon2 == null) {
								returnImmediatlyNull = true;
							}
						}
					}
					if (weapon2 != null) {
						
						/* check for two-hand weapons or weapons with maximum of 1 */
						if (weapon2.TYPE == WeaponTypes.TWOHAND || 
								(weapon2.MAX == 1 && weapon2.NAME == thisweapon.NAME)) {
							if (((Equipment) element).getArmorType() == Armor.MAIN_WEAPON) {
								returnImmediatly = true;
								e = ((Equipment) element);
							}
						}
						
						/* exchange weapons inside of armory */
						else if (((Equipment) element).getArmorType() == Armor.SUB_WEAPON 
								&& fromInsideArmor == true) {
							
							Equipment tempWeapon;
							
							if (set) {
								tempWeapon = armor1.get(Armor.SUB_WEAPON);
								tempWeapon.setAsMainWeapon();
								armor1.put(Armor.MAIN_WEAPON, tempWeapon);
								armor1.put(Armor.SUB_WEAPON, ((Equipment) element));
							} else {
								tempWeapon = armor2.get(Armor.SUB_WEAPON);
								tempWeapon.setAsMainWeapon();
								armor2.put(Armor.MAIN_WEAPON, tempWeapon);
								armor2.put(Armor.SUB_WEAPON, ((Equipment) element));
							}
							if (weapon1 == null) {
								returnImmediatlyNull = true;
							}
						}
					}
				}
				
				/* store current weapons to check for future weapon drop ->
				 * is new weapon from inventory or from armorview? */
				prevWeaponMainSet1 = (Weapon) armor1.get(Armor.MAIN_WEAPON);
				prevWeaponSubSet1 = (Weapon) armor1.get(Armor.SUB_WEAPON);
				prevWeaponMainSet2 = (Weapon) armor2.get(Armor.MAIN_WEAPON);
				prevWeaponSubSet2 = (Weapon) armor2.get(Armor.SUB_WEAPON);
				
				/* do not allow adding another weapon when a twohand is equipped or max of weapon is 1 */
				if (returnImmediatly) {
					//addFists();
					return e;
				}
				/* do not allow adding another weapon when a twohand is equipped or max of weapon is 1 */
				if (returnImmediatlyNull) {
					//addFists();
					return null;
				}
			}
			
			if (set) {
				if(armor1.containsKey(((Equipment) element).getArmorType())) {
					e = armor1.get(((Equipment) element).getArmorType());
				}
				armor1.put(((Equipment) element).getArmorType(), ((Equipment) element));
			} else {
				if(armor2.containsKey(((Equipment) element).getArmorType())) {
					e = armor2.get(((Equipment) element).getArmorType());
				}
				armor2.put(((Equipment) element).getArmorType(), ((Equipment) element));
			}
			
			/* store current weapons to check for future weapon drop ->
			 * is new weapon from inventory or from armorview? */
			prevWeaponMainSet1 = (Weapon) armor1.get(Armor.MAIN_WEAPON);
			prevWeaponSubSet1 = (Weapon) armor1.get(Armor.SUB_WEAPON);
			prevWeaponMainSet2 = (Weapon) armor2.get(Armor.MAIN_WEAPON);
			prevWeaponSubSet2 = (Weapon) armor2.get(Armor.SUB_WEAPON);
			
			//addFists();
			return e;
		
		/* check if item is dragged to potion section */
		} else if (x > ORIGIN_X && x < ORIGIN_X + size.width && y > ORIGIN_Y + size.height - tabHeight - 5
				&& y < ORIGIN_Y + size.height && element instanceof Potion) {
			
			Potion thispotion = (Potion) element;
													
			/* decide if potion is dragged to potion 1,2,3 */
			if (x > ORIGIN_X + size.width/2 - tabWidth/2 - 2 && x < ORIGIN_X + size.width/2 + tabWidth/2 + 2) {
				((Potion) element).POTION_TYPE = Potions.POTION2;			}
			else if (x > ORIGIN_X + size.width/2 - tabWidth - 2 - tabWidth/2 - 2 && x < ORIGIN_X + size.width/2 - tabWidth/2 - 2) {
				((Potion) element).POTION_TYPE = Potions.POTION1;
			}
			else if (x > ORIGIN_X + size.width/2 + tabWidth/2 + 2 && x < ORIGIN_X + size.width/2 + tabWidth/2 + 2 + tabWidth + 2){
				((Potion) element).POTION_TYPE = Potions.POTION3;
			}
						
			Potion potion1 = null;
			Potion potion2 = null;
			Potion potion3 = null;
			
			boolean fromInsideArmor = false;
			
			/* check if potion is dragged from inside Armor */
			if (set) {
				if (thispotion == prevPotion1Set1 || thispotion == prevPotion2Set1 || 
						thispotion == prevPotion3Set1) {
					fromInsideArmor = true;
					
					if (thispotion == prevPotion1Set1) {
						if (thispotion.POTION_TYPE == Potions.POTION2 && prevPotion2Set1 != null) {
							prevPotion2Set1.POTION_TYPE = Potions.POTION1;
							potion1_types.put(Potions.POTION1, prevPotion2Set1);
						}
						if (thispotion.POTION_TYPE == Potions.POTION3 && prevPotion3Set1 != null) {
							prevPotion3Set1.POTION_TYPE = Potions.POTION1;
							potion1_types.put(Potions.POTION1, prevPotion3Set1);
						}
					}
					else if (thispotion == prevPotion2Set1) {
						if (thispotion.POTION_TYPE == Potions.POTION1 && prevPotion1Set1 != null) {
							prevPotion1Set1.POTION_TYPE = Potions.POTION2;
							potion1_types.put(Potions.POTION2, prevPotion1Set1);
						}
						if (thispotion.POTION_TYPE == Potions.POTION3 && prevPotion3Set1 != null) {
							prevPotion3Set1.POTION_TYPE = Potions.POTION2;
							potion1_types.put(Potions.POTION2, prevPotion3Set1);
						}
					}
					else if (thispotion == prevPotion3Set1) {
						if (thispotion.POTION_TYPE == Potions.POTION1 && prevPotion1Set1 != null) {
							prevPotion1Set1.POTION_TYPE = Potions.POTION3;
							potion1_types.put(Potions.POTION3, prevPotion1Set1);
						}
						if (thispotion.POTION_TYPE == Potions.POTION2 && prevPotion2Set1 != null) {
							prevPotion2Set1.POTION_TYPE = Potions.POTION3;
							potion1_types.put(Potions.POTION3, prevPotion2Set1);
						}
					}
				}
			} else {
				if (thispotion == prevPotion1Set2 || thispotion == prevPotion2Set2 || 
						thispotion == prevPotion3Set2) {
					fromInsideArmor = true;
					
					if (thispotion == prevPotion1Set2) {
						if (thispotion.POTION_TYPE == Potions.POTION2 && prevPotion2Set2 != null) {
							prevPotion2Set2.POTION_TYPE = Potions.POTION1;
							potion2_types.put(Potions.POTION1, prevPotion2Set2);
						}
						if (thispotion.POTION_TYPE == Potions.POTION3 && prevPotion3Set2 != null) {
							prevPotion3Set2.POTION_TYPE = Potions.POTION1;
							potion2_types.put(Potions.POTION1, prevPotion3Set2);
						}
					}
					else if (thispotion == prevPotion2Set2) {
						if (thispotion.POTION_TYPE == Potions.POTION1 && prevPotion1Set2 != null) {
							prevPotion1Set2.POTION_TYPE = Potions.POTION2;
							potion2_types.put(Potions.POTION2, prevPotion1Set2);
						}
						if (thispotion.POTION_TYPE == Potions.POTION3 && prevPotion3Set2 != null) {
							prevPotion3Set2.POTION_TYPE = Potions.POTION2;
							potion2_types.put(Potions.POTION2, prevPotion3Set2);
						}
					}
					else if (thispotion == prevPotion3Set2) {
						if (thispotion.POTION_TYPE == Potions.POTION1 && prevPotion1Set2 != null) {
							prevPotion1Set2.POTION_TYPE = Potions.POTION3;
							potion2_types.put(Potions.POTION3, prevPotion1Set2);
						}
						if (thispotion.POTION_TYPE == Potions.POTION2 && prevPotion2Set2 != null) {
							prevPotion2Set2.POTION_TYPE = Potions.POTION3;
							potion2_types.put(Potions.POTION3, prevPotion2Set2);
						}
					}
				}
			}
			
			if (set) {
				if(potion1_types.containsKey(((Potion) element).POTION_TYPE)) {
					e = potion1_types.get(((Potion) element).POTION_TYPE);
				}
				potion1_types.put(((Potion) element).POTION_TYPE, ((Potion) element));
			} else {
				if(potion2_types.containsKey(((Potion) element).POTION_TYPE)) {
					e = potion2_types.get(((Potion) element).POTION_TYPE);
				}
				potion2_types.put(((Potion) element).POTION_TYPE, ((Potion) element));
			}
			
			if (fromInsideArmor) {
				e = null;
			}
			
			/* store current potions to check for future potion drop ->
			 * is new potion from inventory or from armorview? */
			prevPotion1Set1 = (Potion) potion1_types.get(Potions.POTION1);
			prevPotion2Set1 = (Potion) potion1_types.get(Potions.POTION2);
			prevPotion3Set1 = (Potion) potion1_types.get(Potions.POTION3);
			prevPotion1Set2 = (Potion) potion2_types.get(Potions.POTION1);
			prevPotion2Set2 = (Potion) potion2_types.get(Potions.POTION2);
			prevPotion3Set2 = (Potion) potion2_types.get(Potions.POTION3);
			
		} else {
			e = element;
		}
		return e;
	}
	
	/**Always add Fist as fallback weapon when there are still free slots.
	 * 
	 */
	public void addFists() {
		
		Weapon equippedWeaponMain = null;
		Weapon equippedWeaponSub = null;
		int slotSum = 0;
		
		/* get the equipped weapons */
		if (set) {	
			equippedWeaponMain = (Weapon) armor1.get(Armor.MAIN_WEAPON);
			equippedWeaponSub = (Weapon) armor1.get(Armor.SUB_WEAPON);
		} else {
			equippedWeaponMain = (Weapon) armor2.get(Armor.MAIN_WEAPON);
			equippedWeaponSub = (Weapon) armor2.get(Armor.SUB_WEAPON);
		}
		
		/* reset fists */
		if (equippedWeaponMain != null) {
			/* delete fists if equipping two-hand */
			if (set) {
				if (equippedWeaponMain.NAME.equals(("Fists"))) {
					armor1.remove(Armor.MAIN_WEAPON);
				}
			} else {
				if (equippedWeaponMain.NAME.equals(("Fists"))) {
					armor2.remove(Armor.MAIN_WEAPON);
				}
			}
			if (equippedWeaponMain.NAME.equals("Fists")) {
				equippedWeaponMain = null;
			}
		}
		if (equippedWeaponSub != null) {
			/* delete fists if equipping two-hand */
			if (set) {
				if (equippedWeaponSub.NAME.equals(("Fists"))) {
					armor1.remove(Armor.SUB_WEAPON);
				}
			} else {
				if (equippedWeaponSub.NAME.equals(("Fists"))) {
					armor2.remove(Armor.SUB_WEAPON);
				}
			}
			if (equippedWeaponSub.NAME.equals("Fists")) {
				equippedWeaponSub = null;
			}
		} 
		fists1.setAsMainWeapon();
		fists2.setAsSubWeapon();
		
		
		/* calculate how many slots they need */
		if (equippedWeaponMain != null) {
			if (equippedWeaponMain.TYPE == WeaponTypes.SINGLEHAND) {
				slotSum += 1;
			}
			else if (equippedWeaponMain.TYPE == WeaponTypes.TWOHAND) {
				slotSum += 2;
			}
		}
		if (equippedWeaponSub != null) {
			if (equippedWeaponSub.TYPE == WeaponTypes.SINGLEHAND) {
				slotSum += 1;
			}
			else if (equippedWeaponSub.TYPE == WeaponTypes.TWOHAND) {
				slotSum += 2;
			}
		}
				
		/* if not 2 slots are used, fill up with fists */
		if (slotSum == 0) {
			fists1.setAsMainWeapon();
			fists2.setAsSubWeapon();
			if (set) {
				armor1.put(Armor.MAIN_WEAPON, fists1);
				prevWeaponMainSet1 = null;
				armor1.put(Armor.SUB_WEAPON, fists2);
				prevWeaponSubSet1 = null;
			} else {
				armor2.put(Armor.MAIN_WEAPON, fists1);
				prevWeaponMainSet2 = null;
				armor2.put(Armor.SUB_WEAPON, fists2);
				prevWeaponSubSet2 = null;
			}
		}
		else if (slotSum == 1) {
			if (equippedWeaponMain == null) {
				if (set) {
					fists1.setAsMainWeapon();
					armor1.put(Armor.MAIN_WEAPON, fists1);
					prevWeaponMainSet1 = null;
				} else {
					fists2.setAsMainWeapon();
					armor2.put(Armor.MAIN_WEAPON, fists1);
					prevWeaponMainSet2 = null;
				}
			} else if (equippedWeaponSub == null) {
				if (set) {
					fists1.setAsSubWeapon();
					armor1.put(Armor.SUB_WEAPON, fists2);
					prevWeaponSubSet1 = null;
				} else {
					fists2.setAsSubWeapon();
					armor2.put(Armor.SUB_WEAPON, fists2);
					prevWeaponSubSet2 = null;
				}
			}
		}
	}

	/**Returns an equipped item. 
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public Element getItem(int mouseX, int mouseY) {
								
		if (mouseX > ORIGIN_X && mouseX < ORIGIN_X + size.width
				&& mouseY > ORIGIN_Y && mouseY < ORIGIN_Y + size.height - tabHeight - 5) {
			
			Equipment e = null;

			/* HEAD */
			if (mouseX > ORIGIN_X + 100 && mouseX < ORIGIN_X + 100 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 5 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 5 + tabHeight + 10 + IMAGE_SIZE) {
				if(set) {
					e = armor1.get(Armor.HEAD);
					armor1.remove(Armor.HEAD);
				}else {
					e = armor2.get(Armor.HEAD);
					armor2.remove(Armor.HEAD);
				}
			}
			/* CHEST */
			else if (mouseX > ORIGIN_X + 120
					&& mouseX < ORIGIN_X + 120 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 30 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 30 + tabHeight + 10 + IMAGE_SIZE) {
				if(set) {
					e = armor1.get(Armor.CHEST);
					armor1.remove(Armor.CHEST);
				}else {
					e = armor2.get(Armor.CHEST);
					armor2.remove(Armor.CHEST);
				}
			}
			/* ARMS */
			else if (mouseX > ORIGIN_X + 30
					&& mouseX < ORIGIN_X + 30 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 45 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 45 + tabHeight + 10 + IMAGE_SIZE) {
				if(set) {
					e = armor1.get(Armor.ARMS);
					armor1.remove(Armor.ARMS);
				}else {
					e = armor2.get(Armor.ARMS);
					armor2.remove(Armor.ARMS);
				}
			}
			/* LEGS */
			else if (mouseX > ORIGIN_X + 125
					&& mouseX < ORIGIN_X + 125 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 100 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 100 + tabHeight + 10 + IMAGE_SIZE) {
				if(set) {
					e = armor1.get(Armor.LEGS);
					armor1.remove(Armor.LEGS);
				}else {
					e = armor2.get(Armor.LEGS);
					armor2.remove(Armor.LEGS);
				}
			}
			/* FEET */
			else if (mouseX > ORIGIN_X + 35
					&& mouseX < ORIGIN_X + 35 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 115 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 115 + tabHeight + 10 + IMAGE_SIZE) {
				if(set) {
					e = armor1.get(Armor.FEET);
					armor1.remove(Armor.FEET);
				}else {
					e = armor2.get(Armor.FEET);
					armor2.remove(Armor.FEET);
				}
			}
			/* MAIN WEAPON */
			else if (mouseX > ORIGIN_X + 20
					&& mouseX < ORIGIN_X + 20 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 75 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 75 + tabHeight + 10 + IMAGE_SIZE) {
				if(set) {
					e = armor1.get(Armor.MAIN_WEAPON);
					armor1.remove(Armor.MAIN_WEAPON);
				} else {
					e = armor2.get(Armor.MAIN_WEAPON);
					armor2.remove(Armor.MAIN_WEAPON);
				}
			}
			/* SUB WEAPON */
			else if (mouseX > ORIGIN_X + 125
					&& mouseX < ORIGIN_X + 125 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 60 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 60 + tabHeight + 10 + IMAGE_SIZE) {
				if(set) {
					e = armor1.get(Armor.SUB_WEAPON);
					armor1.remove(Armor.SUB_WEAPON);
				}else {
					e = armor2.get(Armor.SUB_WEAPON);
					armor2.remove(Armor.SUB_WEAPON);
				}
			}
			
			return e;
			
		} else if (mouseX > ORIGIN_X && mouseX < ORIGIN_X + size.width && mouseY > ORIGIN_Y + size.height - tabHeight - 5
				&& mouseY < ORIGIN_Y + size.height) {
						
			Potion e = null;
													
			/* decide if potion is dragged from potion 1,2,3 */
			if (mouseX > ORIGIN_X + size.width/2 - tabWidth/2 - 2 && mouseX < ORIGIN_X + size.width/2 + tabWidth/2 + 2) {
				if(set) {
					e = potion1_types.get(Potions.POTION2);
					potion1_types.remove(Potions.POTION2);
				}else {
					e = potion2_types.get(Potions.POTION2);
					potion2_types.remove(Potions.POTION2);
				}
			}
			else if (mouseX > ORIGIN_X + size.width/2 - tabWidth - 2 - tabWidth/2 - 2 && mouseX < ORIGIN_X + size.width/2 - tabWidth/2 - 2) {
				if(set) {
					e = potion1_types.get(Potions.POTION1);
					potion1_types.remove(Potions.POTION1);
				}else {
					e = potion2_types.get(Potions.POTION1);
					potion2_types.remove(Potions.POTION1);
				}
			}
			else if (mouseX > ORIGIN_X + size.width/2 + tabWidth/2 + 2 && mouseX < ORIGIN_X + size.width/2 + tabWidth/2 + 2 + tabWidth + 2){
				if(set) {
					e = potion1_types.get(Potions.POTION3);
					potion1_types.remove(Potions.POTION3);
				}else {
					e = potion2_types.get(Potions.POTION3);
					potion2_types.remove(Potions.POTION3);
				}
			}		
			
			return e;
		} 
		
		return null;
	}

	/**Return potions to armorView if dropped at wrong location.
	 * @param potion
	 */
	public void backPotion(Potion potion) {
		
		if (set) {
			potion1_types.put(potion.POTION_TYPE, potion);
		} else {
			potion2_types.put(potion.POTION_TYPE, potion);
		}
	}

	/**Bonus is added when wearing a full set of armor.
	 * @return the bonus for a full set of armor or 1
	 */
	private float armamentBonus(){
		
		float bonus = 1f;
		
		/* get one armament material */
		Equipment exampleItem = null;
		String armamentMaterial = null;
		
		if (set) {
			exampleItem = armor1.get(Armor.CHEST);
		} else {
			exampleItem = armor2.get(Armor.CHEST);
		}
		
		/* if one part in set is missing, bonus will not be applied */
		if (exampleItem == null) {
			return 1.0f;
		}
		else {
			
			armamentMaterial = ((Armament) exampleItem).TYPE;
			
			int sameCtr = 0; //must be five for a set bonus
			
			if (set) {
				//loop through all equipment in set and increase sameCtr if the types match
				for (Equipment e : equipment1) {
					if (e instanceof Armament) {
						if (((Armament) e).TYPE.equals(armamentMaterial)) {
							sameCtr++;
						}
					}
				}
			} else {
				for (Equipment e : equipment2) {
					if (e instanceof Armament) {
						if (((Armament) e).TYPE.equals(armamentMaterial)) {
							sameCtr++;
						}
					}
				}
			}
			
			if (sameCtr == 5) {
				bonus = ((Armament) exampleItem).BONUS;
			}
		}
		
		return bonus;	
	}
	
	/**Returns sum of all values for a specific armament attribute of all equipped armaments.<br>
	 * NOT YET COMPLETELY IMPLEMENTED!
	 * 
	 * @param speed
	 * @return
	 */
	public float getStats(ArmorStatsTypes type, ArmorStatsMode mode, ArmorStatsAttributes att) {
		
		float value = 0f;
		int itemCtr = 1; //needed for average calculation
		Collection<Equipment> myEquipment = null;
		
		if (set) {
			myEquipment = equipment1;
		} else {
			myEquipment = equipment2;
		}
		
		for (Equipment e : myEquipment) {
			if (type == ArmorStatsTypes.ARMAMENT) {
				if (e instanceof Armament) {
					if (mode == ArmorStatsMode.SUM) {
						if (att == ArmorStatsAttributes.SPEED) {
							value += ((Armament) e).SPEED;
						}
						if (att == ArmorStatsAttributes.ARMOR) {
							value += ((Armament) e).ARMOR;
						}
					}
				}
				if (e instanceof Weapon) {
					if (mode == ArmorStatsMode.SUM) {
						if (att == ArmorStatsAttributes.ARMOR) {
							value += ((Weapon) e).DEFENSE;
						}
					}
				}
			}
			if (type == ArmorStatsTypes.WEAPONS) {
				if (e instanceof Weapon) {
					if (mode == ArmorStatsMode.MIN) {
						if (att == ArmorStatsAttributes.SPEED) {
							if (((Weapon) e).SPEED < value && ((Weapon) e).SPEED > 0) {
								value = ((Weapon) e).SPEED;
							}
						}
					}
					if (mode == ArmorStatsMode.AVERAGE) {
						if (att == ArmorStatsAttributes.ACCURACY) {
							value = (value + ((Weapon) e).ACCURACY)/itemCtr;
							itemCtr++;
						}
					}
					if (mode == ArmorStatsMode.SUM) {
						if (att == ArmorStatsAttributes.ATTACK) {
							value += (value + ((Weapon) e).ATTACK);
						}
					}
				}
			}
		}
		
		/* add armament bonus for a full set of armor */
		if (type == ArmorStatsTypes.ARMAMENT) {
			if (mode == ArmorStatsMode.SUM) {
				if (att == ArmorStatsAttributes.ARMOR) {
					value = value * armamentBonus();
				}
			}
		}
		
		return value;
	}	
}
