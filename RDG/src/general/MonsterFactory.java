package general;

import java.util.Map;

import org.newdawn.slick.SlickException;

import configLoader.MonsterTemplate;
import configLoader.PotionTemplate;
import elements.Monster;
import elements.Potion;

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
	
	/**Creates an MonsterFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @see MonsterFactory
	 */
	public MonsterFactory() {

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
}

