package general;

import elements.Item;
import gameEssentials.Game;
import general.Enums.ItemClasses;
import general.Enums.ItemType;
import general.Enums.Levels;
import general.Enums.RoomTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.xml.sax.SAXException;

import configLoader.ArmamentTemplate;
import configLoader.Configloader;
import configLoader.MonsterTemplate;
import configLoader.PotionTemplate;
import configLoader.RoomTemplate;
import configLoader.WeaponTemplate;

/**
 * ResourceManager loads all Data from Config Files and Resources from harddrive
 * into memory.<br>
 * setConfigloader() must be called by Game Class before loadResources() can be
 * called.
 *
 */
public class ResourceManager {
	
	/* public variables are quasi constants -> they will only be set once and do never change ? */

	/* only one instance of ResourceManager is allowed */
	private static ResourceManager INSTANCE = null;

	/* more efficient picture loading */
	public SpriteSheet TILES;

	/* Hashmap for all Images */
	public Map<String, Image> IMAGES;

	/* Configloader which holds image paths */
	private Configloader configloader = null;

	/* config templates */
	public Map<String, ArmamentTemplate> ARMAMENT_TEMPLATES;
	public Map<String, PotionTemplate> POTION_TEMPLATES;
	public Map<String, WeaponTemplate> WEAPON_TEMPLATES;
	public Map<String, MonsterTemplate> MONSTER_TEMPLATES;
	public Map<RoomTypes, RoomTemplate> ROOM_TEMPLATES;

	/* lists of all item names and types */
	public List<Item> ITEMS;
	
	/* map of lists of items grouped by item class */
	public Map<ItemClasses, List<Item>> ITEMCLASSLIST = null;

	/* lists for armament, portion, weapon and monster names */
	public List<String> ARMAMENTS;
	public List<String> POTIONS;
	public List<String> WEAPONS;
	public List<String> MONSTERS;

	/* maps of lists of armaments, portions, weapons - grouped by item class */
	public Map<ItemClasses, List<String>> ARMAMENTS_CLASSIFIED;
	public Map<ItemClasses, List<String>> POTIONS_CLASSIFIED;
	public Map<ItemClasses, List<String>> WEAPONS_CLASSIFIED;

	/* map of lists of all monsters - grouped by monster level */
	public Map<Levels, List<String>> MONSTERS_LEVELED;

	/**
	 * Constructs a RessourceManager.<br>
	 * 
	 * @throws SlickException
	 * @see ResourceManager
	 */
	public ResourceManager() throws SlickException {
		IMAGES = new HashMap<String, Image>();
	}

	/**
	 * Loads all ressources from harddrive into memory.
	 * 
	 * @throws SlickException
	 */
	private void loadResources() throws SlickException {

		/* load config data */
		try {
			configloader = new Configloader().getInstance();
		} catch (IllegalArgumentException | ParserConfigurationException
				| SAXException | IOException e) {
			e.printStackTrace();
		}

		if (configloader == null) {
			throw new NullPointerException("Configloader must be set!");
		}

		/* load images */
		loadImages();

		/* create item list and classed item list  */
		ITEMS = new ArrayList<Item>();
		ITEMCLASSLIST = new HashMap<ItemClasses, List<Item>>();
		ITEMCLASSLIST.put(ItemClasses.WEAK, new ArrayList<Item>());
		ITEMCLASSLIST.put(ItemClasses.MEDIUM, new ArrayList<Item>());
		ITEMCLASSLIST.put(ItemClasses.STRONG, new ArrayList<Item>());
		
		/* load items */
		loadArmaments();
		loadPotions();
		loadWeapons();

		/* load monsters */
		loadMonsters();
		
		/* load rooms */
		loadRooms();
	}

	/**
	 * Load all Images from harddrive into memory.
	 * 
	 * @throws SlickException
	 */
	private void loadImages() throws SlickException {

		TILES = new SpriteSheet(Game.IMAGEPATH + "/rooms/tileset.png", 32, 32);

		IMAGES.put("Player1", new Image(Game.IMAGEPATH + "soldier_32x32.png"));
		IMAGES.put("Player2", new Image(Game.IMAGEPATH + "soldier_32x32.png"));
		IMAGES.put("Armor_Background", new Image(Game.IMAGEPATH
				+ "warrior_160x160.png"));
		/*IMAGES.put("Helmet", new Image(Game.IMAGEPATH + "Head.png"));
		IMAGES.put("Arms", new Image(Game.IMAGEPATH + "Arm.png"));
		IMAGES.put("Cuirass", new Image(Game.IMAGEPATH + "Chest.png"));
		IMAGES.put("Legs", new Image(Game.IMAGEPATH + "Legs.png"));
		IMAGES.put("Shoes", new Image(Game.IMAGEPATH + "Feet.png"));
		IMAGES.put("M_Weapon", new Image(Game.IMAGEPATH + "Weapon.png"));
		IMAGES.put("S_Weapon", new Image(Game.IMAGEPATH + "Weapon2.png"));*/
		
		/* load key picture */ 
		IMAGES.put("Key", new Image(Game.IMAGEPATH + "key.png"));
		
		/* load images from paths stored in config loader */
		for (Entry<String, ArmamentTemplate> entry : configloader
				.getArmamentTemplates().entrySet()) {
			IMAGES.put(entry.getKey(), new Image(Game.IMAGEPATH
					+ entry.getValue().getImage()));
		}
		for (Entry<String, PotionTemplate> entry : configloader
				.getPotionTemplates().entrySet()) {
			IMAGES.put(entry.getKey(), new Image(Game.IMAGEPATH
					+ entry.getValue().getImage()));
		}
		for (Entry<String, WeaponTemplate> entry : configloader
				.getWeaponTemplates().entrySet()) {
			IMAGES.put(entry.getKey(), new Image(Game.IMAGEPATH
					+ entry.getValue().getImage()));
		}
		for (Entry<String, MonsterTemplate> entry : configloader
				.getMonsterTemplates().entrySet()) {
			IMAGES.put(entry.getKey(), new Image(Game.IMAGEPATH
					+ entry.getValue().getImage()));
		}

		/* Print which images are stored now */
		/*
		 * for(String s : IMAGES.keySet()) { System.out.println("Key: " + s +
		 * ", Value: " + IMAGES.get(s)); }
		 */
	}

	/**
	 * Load Armament template from Configloader, create name list and classified name list and add Armament name to Item name list.
	 */
	private void loadArmaments() {

		ARMAMENTS = new ArrayList<String>();
		ARMAMENTS_CLASSIFIED = new HashMap<ItemClasses, List<String>>();

		List<String> weaklist = new ArrayList<String>();
		List<String> mediumlist = new ArrayList<String>();
		List<String> stronglist = new ArrayList<String>();
		
		ARMAMENT_TEMPLATES = configloader.getArmamentTemplates();

		/* create lists of items corresponding to their item classes */
		for (Entry<String, ArmamentTemplate> entry : ARMAMENT_TEMPLATES
				.entrySet()) {
			
			ARMAMENTS.add(entry.getKey());
			ITEMS.add(new Item(entry.getKey(), ItemType.ARMAMENT));
			
			if (entry.getValue().getItem_class() == ItemClasses.WEAK) {
				weaklist.add(entry.getKey());
				ITEMCLASSLIST.get(ItemClasses.WEAK).add(new Item(entry.getKey(), ItemType.ARMAMENT));
			} else if (entry.getValue().getItem_class() == ItemClasses.MEDIUM) {
				mediumlist.add(entry.getKey());
				ITEMCLASSLIST.get(ItemClasses.MEDIUM).add(new Item(entry.getKey(), ItemType.ARMAMENT));
			} else if (entry.getValue().getItem_class() == ItemClasses.STRONG) {
				stronglist.add(entry.getKey());
				ITEMCLASSLIST.get(ItemClasses.STRONG).add(new Item(entry.getKey(), ItemType.ARMAMENT));
			}
		}

		ARMAMENTS_CLASSIFIED.put(ItemClasses.WEAK, weaklist);
		ARMAMENTS_CLASSIFIED.put(ItemClasses.MEDIUM, mediumlist);
		ARMAMENTS_CLASSIFIED.put(ItemClasses.STRONG, stronglist);
	}

	/**
	 * Load Potion template from Configloader, create name list and classified name list and add Potion name to Item name list.
	 */
	private void loadPotions() {

		POTIONS = new ArrayList<String>();
		POTIONS_CLASSIFIED = new HashMap<ItemClasses, List<String>>();

		List<String> weaklist = new ArrayList<String>();
		List<String> mediumlist = new ArrayList<String>();
		List<String> stronglist = new ArrayList<String>();

		POTION_TEMPLATES = configloader.getPotionTemplates();

		/* create lists of items corresponding to their item classes */
		for (Entry<String, PotionTemplate> entry : POTION_TEMPLATES.entrySet()) {
			
			POTIONS.add(entry.getKey());
			ITEMS.add(new Item(entry.getKey(), ItemType.POTION));
			
			if (entry.getValue().getItem_class() == ItemClasses.WEAK) {
				weaklist.add(entry.getKey());
				ITEMCLASSLIST.get(ItemClasses.WEAK).add(new Item(entry.getKey(), ItemType.POTION));
			} else if (entry.getValue().getItem_class() == ItemClasses.MEDIUM) {
				mediumlist.add(entry.getKey());
				ITEMCLASSLIST.get(ItemClasses.MEDIUM).add(new Item(entry.getKey(), ItemType.POTION));
			} else if (entry.getValue().getItem_class() == ItemClasses.STRONG) {
				stronglist.add(entry.getKey());
				ITEMCLASSLIST.get(ItemClasses.STRONG).add(new Item(entry.getKey(), ItemType.POTION));
			}
		}

		POTIONS_CLASSIFIED.put(ItemClasses.WEAK, weaklist);
		POTIONS_CLASSIFIED.put(ItemClasses.MEDIUM, mediumlist);
		POTIONS_CLASSIFIED.put(ItemClasses.STRONG, stronglist);
	}

	/**
	 * Load Weapon template from Configloader, create name list and classified name list and add Weapon name to Item name list.
	 */
	private void loadWeapons() {

		WEAPONS = new ArrayList<String>();
		WEAPONS_CLASSIFIED = new HashMap<ItemClasses, List<String>>();

		List<String> weaklist = new ArrayList<String>();
		List<String> mediumlist = new ArrayList<String>();
		List<String> stronglist = new ArrayList<String>();

		WEAPON_TEMPLATES = configloader.getWeaponTemplates();

		/* create lists of items corresponding to their item classes */
		for (Entry<String, WeaponTemplate> entry : WEAPON_TEMPLATES.entrySet()) {
			
			WEAPONS.add(entry.getKey());
			ITEMS.add(new Item(entry.getKey(), ItemType.WEAPON));
			
			if (entry.getValue().getItem_class() == ItemClasses.WEAK) {
				weaklist.add(entry.getKey());
				ITEMCLASSLIST.get(ItemClasses.WEAK).add(new Item(entry.getKey(), ItemType.WEAPON));
			} else if (entry.getValue().getItem_class() == ItemClasses.MEDIUM) {
				mediumlist.add(entry.getKey());
				ITEMCLASSLIST.get(ItemClasses.MEDIUM).add(new Item(entry.getKey(), ItemType.WEAPON));
			} else if (entry.getValue().getItem_class() == ItemClasses.STRONG) {
				stronglist.add(entry.getKey());
				ITEMCLASSLIST.get(ItemClasses.STRONG).add(new Item(entry.getKey(), ItemType.WEAPON));
			}
		}

		WEAPONS_CLASSIFIED.put(ItemClasses.WEAK, weaklist);
		WEAPONS_CLASSIFIED.put(ItemClasses.MEDIUM, mediumlist);
		WEAPONS_CLASSIFIED.put(ItemClasses.STRONG, stronglist);
	}
	
	/**
	 * Load Monster template from Configloader, create name list and leveled name list.
	 */
	private void loadMonsters() {

		MONSTERS = new ArrayList<String>(); // which type -> return random
											// element
		MONSTERS_LEVELED = new HashMap<Levels, List<String>>();

		List<String> easylist = new ArrayList<String>();
		List<String> normallist = new ArrayList<String>();
		List<String> hardlist = new ArrayList<String>();

		MONSTER_TEMPLATES = configloader.getMonsterTemplates();

		/* create lists of items corresponding to their item classes */
		for (Entry<String, MonsterTemplate> entry : MONSTER_TEMPLATES.entrySet()) {
			if (entry.getValue().getLevel() == Levels.EASY) {
				easylist.add(entry.getKey());
			} else if (entry.getValue().getLevel() == Levels.NORMAL) {
				normallist.add(entry.getKey());
			} else if (entry.getValue().getLevel() == Levels.HARD) {
				hardlist.add(entry.getKey());
			}
		}

		MONSTERS_LEVELED.put(Levels.EASY, easylist);
		MONSTERS_LEVELED.put(Levels.NORMAL, normallist);
		MONSTERS_LEVELED.put(Levels.HARD, hardlist);
	}
	
	/**
	 * Load Room template from Configloader.
	 */
	private void loadRooms() {

		ROOM_TEMPLATES = configloader.getRoomTemplates();
	}

	/**
	 * Returns the one and only instance of ResourceManager and triggers loading
	 * all Ressources.<br>
	 * 
	 * @return the one and only instance of ResourceManager
	 * @throws SlickException
	 */
	public ResourceManager getInstance() throws SlickException {
		if (INSTANCE == null) {
			INSTANCE = new ResourceManager();
			INSTANCE.loadResources();
		}
		return INSTANCE;
	}
}
