package general;

import elements.Armament;
import elements.Potion;
import general.Enums.Armor;
import general.Enums.Attributes;
import general.Enums.ItemClasses;
import general.Enums.Modes;
import general.Enums.Targets;

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

/**PotionFactory receives a Potion's default parameters from PotionTemplate class.<br>
 * It then sets the Potion's variables either to the default values or to random values
 * retrieved from Chances class.<br>
 * Power value will be set according to a random Value returned by Chances Class 
 * which lies within statsLowMultiplier and statsHighMultiplier and is then multiplied with classMultiplier.
 */
public class PotionFactory {
	
	/* make sure PotionFactory can only be instantiated once*/
	private static PotionFactory FACTORY = null;
	
	/* templates */
	Map<String, PotionTemplate> potionTemplates;
	
	/* list of all potion names */
	private List<String> potions;
	
	/* lists of potions for each item class */
	private Map<ItemClasses, List<String>> itemClassList = null;
	
	/* resource manager needed for obtaining images */
	private static ResourceManager resources = null;
	
	/**Creates an PotionFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @throws SlickException 
	 * @see PotionFactory
	 */
	public PotionFactory() throws SlickException {
		
		potions = new ArrayList<String>(); //which type -> return random element
		itemClassList = new HashMap<ItemClasses, List<String>>();
		resources = new ResourceManager().getInstance();
		
		List weaklist = new ArrayList<String>();
		List mediumlist = new ArrayList<String>();
		List stronglist = new ArrayList<String>();
		
		try {
			potionTemplates = new Configloader().getInstance().getPotionTemplates();
		} catch (IllegalArgumentException | SlickException
				| ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		/* create lists of items corresponding to their item classes */
		for (Entry<String, PotionTemplate> entry : potionTemplates.entrySet()) {
	        if (entry.getValue().getItem_class() == ItemClasses.WEAK) weaklist.add(entry.getKey());
	        else if (entry.getValue().getItem_class() == ItemClasses.MEDIUM) mediumlist.add(entry.getKey());
	        else if (entry.getValue().getItem_class() == ItemClasses.STRONG) stronglist.add(entry.getKey());
	    }
		
		itemClassList.put(ItemClasses.WEAK, weaklist);
		itemClassList.put(ItemClasses.MEDIUM, mediumlist);
		itemClassList.put(ItemClasses.STRONG, stronglist);
	}
	
	/**Creates an PotionFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized PotionFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public PotionFactory getInstance() throws SlickException {
		if (FACTORY == null) {
			FACTORY = new PotionFactory();
		}
		return FACTORY;
	}
	
	/**
	 * @return list of all Potions' names
	 */
	public List getAllPotions() {
		return potions;
	}
	
	/**
	 * @return HashMap with lists of all Potions's names grouped by item classes
	 */
	public Map<ItemClasses, List<String>> getItemClasses() {
		return itemClassList;
	}
	
	/**Creates new Potion with randomized stats.
	 * 
	 * @param name
	 * @return a new Potion
	 * @see PotionFactory
	 */
	public Potion createPotion(String name, float itemMultiplier) {
		
		PotionTemplate tempTemplate = potionTemplates.get(name);
		
		Image image = resources.IMAGES.get(name);
		String description = tempTemplate.getDescription();
		ItemClasses itemClass = tempTemplate.getItem_class();
		Attributes effect = tempTemplate.getEffect();
		Targets target = tempTemplate.getTarget();
		Modes mode = tempTemplate.getMode();
		float power = tempTemplate.getX() *  itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomValue(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier()); 
		int duration = tempTemplate.getN();
		
		return new Potion(name, image, description, itemClass, effect, target, mode, power, duration);
	}
}
