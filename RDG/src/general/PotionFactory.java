package general;

import elements.Armament;
import elements.Potion;

import java.util.Map;

import org.newdawn.slick.SlickException;

import configLoader.ArmamentTemplate;
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
	
	/**Creates new Potion with default and randomized stats.
	 * 
	 * @param name
	 * @return a new Potion
	 * @see PotionFactory
	 */
	public Potion createPotion(String name) {
		
		return new Potion(name, null, name, null, null, null, null, 0, 0);
	}
}
