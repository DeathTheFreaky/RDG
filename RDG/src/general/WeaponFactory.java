package general;

import java.util.Map;

import org.newdawn.slick.SlickException;

import configLoader.PotionTemplate;
import configLoader.WeaponTemplate;
import elements.Potion;
import elements.Weapon;

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
	
	/* Room type also influenced stats of this item */
	private final float itemMultiplier;

	/**Creates an WeaponFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @see WeaponFactory
	 */
	public WeaponFactory() {
		this(1);
	}
	
	/**Creates an WeaponFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @see WeaponFactory
	 */
	public WeaponFactory(float itemMultiplier) {
		
		this.itemMultiplier = itemMultiplier;
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
	
	/**Creates an WeaponFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized WeaponFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public WeaponFactory getInstance(float itemMultiplier) throws SlickException {
		if (FACTORY == null) {
			FACTORY = new WeaponFactory(itemMultiplier);
		}
		return FACTORY;
	}
	
	/**Creates new Weapon with randomized stats.
	 * 
	 * @param name
	 * @return a new Weapon
	 * @see WeaponFactory
	 */
	public Weapon createWeapon(String name) {
		
		return new Weapon(name, null, 0, 0, 0, 0, null, null, 0);
	}
}
