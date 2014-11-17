package general;

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
import configLoader.MonsterTemplate;
import configLoader.PotionTemplate;
import elements.Monster;
import elements.Potion;
import general.Enums.ItemClasses;
import general.Enums.Levels;

/**MonsterFactory receives a Monster's default parameters from MonsterTemplate class.<br>
 * It then sets the Monster's variables either to the default values or to random values
 * retrieved from Chances class.<br>
 * Health points, strength, speed and accuracy and killBonus will be set according to a random Value returned by Chances Class 
 * which lies within statsLowMultiplier and statsHighMultiplier and is then multiplied with classMultiplier.
 */
public class MonsterFactory {
	
	/* make sure RoomFactory can only be instantiated once*/
	private static MonsterFactory FACTORY = null;
	
	/* templates */
	Map<String, MonsterTemplate> monsterTemplates;
	
	/* list of all monster names */
	private List<String> monsters;
	
	/* lists of monsters for each monster level */
	private Map<Levels, List<String>> levelList = null;
	
	/**Creates an MonsterFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @see MonsterFactory
	 */
	public MonsterFactory() {
		
		monsters = new ArrayList<String>(); //which type -> return random element
		levelList = new HashMap<Levels, List<String>>();
		
		List easylist = new ArrayList<String>();
		List normallist = new ArrayList<String>();
		List hardlist = new ArrayList<String>();
		
		try {
			monsterTemplates = new Configloader().getInstance().getMonsterTemplates();
		} catch (IllegalArgumentException | SlickException
				| ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		/* create lists of items corresponding to their item classes */
		for (Entry<String, MonsterTemplate> entry : monsterTemplates.entrySet()) {
	        if (entry.getValue().getLevel() == Levels.EASY) easylist.add(entry.getKey());
	        else if (entry.getValue().getLevel() == Levels.NORMAL) normallist.add(entry.getKey());
	        else if (entry.getValue().getLevel() == Levels.HARD) hardlist.add(entry.getKey());
	    }
		
		levelList.put(Levels.EASY, easylist);
		levelList.put(Levels.NORMAL, normallist);
		levelList.put(Levels.HARD, hardlist);
	}
	
	/**Creates an MonsterFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized MonsterFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public MonsterFactory getInstance() throws SlickException {
		if (FACTORY == null) {
			FACTORY = new MonsterFactory();
		}
		return FACTORY;
	}
	
	/**Creates new Monster with randomized stats.
	 * 
	 * @param name
	 * @return a new Monster
	 * @see MonsterFactory
	 */
	public Monster createMonster(String name) {
		
		return new Monster(name, null, null, null, 0, 0, 0, 0, 0);
	}
	
	/**
	 * @return list of all Monsters' names
	 */
	public List getAllMonsters() {
		return monsters;
	}
}

