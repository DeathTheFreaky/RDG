package general;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import configLoader.ArmamentTemplate;
import configLoader.Configloader;
import elements.Armament;

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
	
	/**Creates an ArmamentFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @see ArmamentFactory
	 */
	public ArmamentFactory() {
		this(1);
	}
	
	/**Creates an ArmamentFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @see ArmamentFactory
	 */
	public ArmamentFactory(float itemMultiplier) {
		
		try {
			armamentTemplates = new Configloader().getInstance().getArmamentTemplates();
		} catch (IllegalArgumentException | SlickException
				| ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
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
	
	/**Creates an ArmamentFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized ArmamentFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public ArmamentFactory getInstance(float itemMultiplier) throws SlickException {
		if (FACTORY == null) {
			FACTORY = new ArmamentFactory(itemMultiplier);
		}
		return FACTORY;
	}
	
	/**Creates new Armament with default and randomized stats.
	 * 
	 * @param name
	 * @return a new Armament
	 * @see ArmamentFactory
	 */
	public Armament createArmament(String name) {
		
		return new Armament(name, null, name, null, 0, 0, 0, null);
	}
}
