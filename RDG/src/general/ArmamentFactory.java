package general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import configLoader.ArmamentTemplate;
import configLoader.Configloader;
import elements.Armament;
import elements.Attack;
import gameEssentials.Game;
import general.Enums.Armor;
import general.Enums.Attacks;
import general.Enums.ItemClasses;
import general.Enums.Levels;

/**ArmamentFactory receives an Armament's default parameters from ArmamentTemplate class.<br>
 * It then sets the Armament's variables either to the default values or to random values
 * retrieved from Chances class.<br>
 * Armor and speed will be set according to a random Value returned by Chances Class 
 * which lies within statsLowMultiplier and statsHighMultiplier and is then multiplied with classMultiplier.
 */
public class ArmamentFactory {
	
	/* make sure ArmamentFactory can only be instantiated once*/
	private static ArmamentFactory FACTORY = null;
	
	/* templates */
	Map<String, ArmamentTemplate> armamentTemplates;
	
	/* list of all armament names */
	private List<String> armaments;
	
	/* lists of armaments for each item class */
	private Map<ItemClasses, List<String>> itemClassList = null;
	
	/* resource manager needed for obtaining images */
	private static ResourceManager resources = null;
	
	/**Creates an ArmamentFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @throws SlickException 
	 * @see ArmamentFactory
	 */
	public ArmamentFactory() throws SlickException {
		
		armaments = new ArrayList<String>(); //which type -> return random element
		itemClassList = new HashMap<ItemClasses, List<String>>();
		resources = new ResourceManager().getInstance();
		
		List weaklist = new ArrayList<String>();
		List mediumlist = new ArrayList<String>();
		List stronglist = new ArrayList<String>();
		
		try {
			armamentTemplates = new Configloader().getInstance().getArmamentTemplates();
		} catch (IllegalArgumentException | SlickException
				| ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		/* create lists of items corresponding to their item classes */
		for (Entry<String, ArmamentTemplate> entry : armamentTemplates.entrySet()) {
	        if (entry.getValue().getItem_class() == ItemClasses.WEAK) weaklist.add(entry.getKey());
	        else if (entry.getValue().getItem_class() == ItemClasses.MEDIUM) mediumlist.add(entry.getKey());
	        else if (entry.getValue().getItem_class() == ItemClasses.STRONG) stronglist.add(entry.getKey());
	    }
		
		itemClassList.put(ItemClasses.WEAK, weaklist);
		itemClassList.put(ItemClasses.MEDIUM, mediumlist);
		itemClassList.put(ItemClasses.STRONG, stronglist);
	}
	
	/**Creates an ArmamentFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized ArmamentFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public ArmamentFactory getInstance() throws SlickException {
		if (FACTORY == null) {
			FACTORY = new ArmamentFactory();
		}
		return FACTORY;
	}
	
	/**
	 * @return list of all Armaments' names
	 */
	public List getAllArmaments() {
		return armaments;
	}
	
	/**
	 * @return HashMap with lists of all Armaments's names grouped by item classes
	 */
	public Map<ItemClasses, List<String>> getItemClasses() {
		return itemClassList;
	}
	
	/**Creates new Armament with randomized stats.
	 * 
	 * @param name
	 * @return desired Armament
	 * @see ArmamentFactory
	 */
	public Armament createArmament(String name, float itemMultiplier) {
		
		ArmamentTemplate tempTemplate = armamentTemplates.get(name);
		
		Image image = resources.IMAGES.get(name);
		String type = tempTemplate.getType();
		ItemClasses itemClass = tempTemplate.getItem_class();
		float armor = tempTemplate.getArmor() * itemMultiplier * tempTemplate.getClass_multiplier() * 
				Chances.randomValue(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float speed = tempTemplate.getSpeed() * itemMultiplier * tempTemplate.getClass_multiplier() * 
				Chances.randomValue(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float bonus = tempTemplate.getBonus();
		Armor armorType = tempTemplate.getArmorType();
		
		return new Armament(name, image, type, itemClass, armor, speed, bonus, armorType);
	}	
}
