package general;

import elements.Armament;
import elements.Potion;
import general.Enums.ItemClasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

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
	
	/* Room type also influenced stats of this item */
	private final float itemMultiplier;
	
	/* list of all potion names */
	private List<String> potions;
	
	/* lists of potions for each item class */
	private Map<ItemClasses, List<String>> itemClassList = null;
	
	/**Creates an PotionFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @see PotionFactory
	 */
	public PotionFactory() {
		this(1);
	}
	
	/**Creates an PotionFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @see PotionFactory
	 */
	public PotionFactory(float itemMultiplier) {
		
		this.itemMultiplier = itemMultiplier;
		potions = new ArrayList<String>(); //which type -> return random element
		itemClassList = new HashMap<ItemClasses, List<String>>();
		
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
	
	/**Creates an PotionFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized PotionFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public PotionFactory getInstance(float itemMultiplier) throws SlickException {
		if (FACTORY == null) {
			FACTORY = new PotionFactory(itemMultiplier);
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
	public Potion createPotion(String name) {
		
		return new Potion(name, null, name, null, null, null, null, 0, 0);
	}
}
