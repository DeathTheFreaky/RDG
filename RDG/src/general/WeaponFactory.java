package general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import configLoader.ArmamentTemplate;
import configLoader.Configloader;
import configLoader.PotionTemplate;
import configLoader.WeaponTemplate;
import elements.Potion;
import elements.Weapon;
import general.Enums.Armor;
import general.Enums.ItemClasses;
import general.Enums.WeaponTypes;

/**WeaponFactory receives a Weapon's default parameters from WeaponTemplate class.<br>
 * It then sets the Weapon's variables either to the default values or to random values
 * retrieved from Chances class.<br>
 * Attack, Speed, Accuracy and Defense will be set according to a random Value returned by Chances Class 
 * which lies within statsLowMultiplier and statsHighMultiplier and is then multiplied with classMultiplier.
 */
public class WeaponFactory {
	
	/* make sure WeaponFactory can only be instantiated once*/
	private static WeaponFactory FACTORY = null;
	
	/* templates */
	Map<String, WeaponTemplate> weaponTemplates;
	
	/* list of all weapon names */
	private List<String> weapons;
	
	/* lists of weapons for each item class */
	private Map<ItemClasses, List<String>> itemClassList = null;
	
	/* resource manager needed for obtaining images */
	private static ResourceManager resources = null;
	
	/**Creates an WeaponFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @throws SlickException 
	 * @see WeaponFactory
	 */
	public WeaponFactory() throws SlickException {
		
		weapons = new ArrayList<String>(); //which type -> return random element
		itemClassList = new HashMap<ItemClasses, List<String>>();
		resources = new ResourceManager().getInstance();
		
		List weaklist = new ArrayList<String>();
		List mediumlist = new ArrayList<String>();
		List stronglist = new ArrayList<String>();
		
		try {
			weaponTemplates = new Configloader().getInstance().getWeaponTemplates();
		} catch (IllegalArgumentException | SlickException
				| ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		/* create lists of items corresponding to their item classes */
		for (Entry<String, WeaponTemplate> entry : weaponTemplates.entrySet()) {
	        if (entry.getValue().getItem_class() == ItemClasses.WEAK) weaklist.add(entry.getKey());
	        else if (entry.getValue().getItem_class() == ItemClasses.MEDIUM) mediumlist.add(entry.getKey());
	        else if (entry.getValue().getItem_class() == ItemClasses.STRONG) stronglist.add(entry.getKey());
	    }
		
		itemClassList.put(ItemClasses.WEAK, weaklist);
		itemClassList.put(ItemClasses.MEDIUM, mediumlist);
		itemClassList.put(ItemClasses.STRONG, stronglist);
	}
	
	/**Creates an WeaponFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized WeaponFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public WeaponFactory getInstance() throws SlickException {
		if (FACTORY == null) {
			FACTORY = new WeaponFactory();
		}
		return FACTORY;
	}
	
	/**
	 * @return list of all Weapons' names
	 */
	public List getAllWeapons() {
		return weapons;
	}
	
	/**
	 * @return HashMap with lists of all Weapons's names grouped by item classes
	 */
	public Map<ItemClasses, List<String>> getItemClasses() {
		return itemClassList;
	}
	
	/**Creates new Weapon with randomized stats.
	 * 
	 * @param name
	 * @return a new Weapon
	 * @see WeaponFactory
	 */
	public Weapon createWeapon(String name, float itemMultiplier) {
		
		WeaponTemplate tempTemplate = weaponTemplates.get(name);
		
		Image image = resources.IMAGES.get(name);
		float attack = tempTemplate.getAttack() * itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomValue(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float speed = tempTemplate.getSpeed() * itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomValue(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float accuracy = tempTemplate.getAccuracy() * itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomValue(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float defense = tempTemplate.getDefence() * itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomValue(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		ItemClasses itemClass = tempTemplate.getItem_class();
		WeaponTypes type = tempTemplate.getType();
		int max = tempTemplate.getMax();
		
		return new Weapon(name, image, attack, speed, accuracy, defense, itemClass, type, max);
	}
}
