package general;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import configLoader.ArmamentTemplate;
import configLoader.AttackTemplate;
import configLoader.Configloader;
import configLoader.PotionTemplate;
import elements.Attack;
import elements.Potion;
import gameEssentials.Game;
import general.Enums.Attacks;
import general.Enums.Attributes;

/**AttackFactory receives a Attack's default parameters from AttackTemplate class.<br>
 * It then sets the Attack's variables either to the default values or to random values
 * retrieved from Chances class.<br>
 * hpDamageMultiplier and attributeDamageMultiplier will be multiplied with classMultiplier.
 */
public class AttackFactory {
	
	/* make sure RoomFactory can only be instantiated once*/
	private static AttackFactory FACTORY = null;
	
	/* map of all attacks -> do not create new attacks if they already exist */
	private Map<Attacks, Attack> attacks = null;
	
	/* templates */
	private Map<Attacks, AttackTemplate> attackTemplates = null;
	
	/**Creates an AttackFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @param itemMultiplier
	 * @see AttackFactory
	 */
	public AttackFactory() {
		
		attacks = new HashMap<Attacks, Attack>();
		
		try {
			
			attackTemplates = new Configloader().getInstance().getAttackTemplates();
			
			for (Entry<Attacks, AttackTemplate> entry : attackTemplates.entrySet()) {
				
				/* get and calculate stat values and variables for new Attack instance */
				AttackTemplate tempTemplate = entry.getValue();
				float classMult = tempTemplate.getClass_multiplier();
				Attacks type = tempTemplate.getType();
				Attributes effect = tempTemplate.getEffect();
				float hpDamageMultiplier = tempTemplate.getHp_damage() * classMult;
				float hitProbability = tempTemplate.getHit_probability();
				float attributeDamageMultiplier = tempTemplate.getX() * classMult;
				float statsLowMultiplier = tempTemplate.getStats_low_multiplier();
				float statsHighMultiplier = tempTemplate.getStats_high_multiplier();
				
				Attack tempAttack = new Attack(type, effect, hpDamageMultiplier, hitProbability, 
						attributeDamageMultiplier, statsLowMultiplier, statsHighMultiplier);
				
				attacks.put(entry.getKey(), tempAttack);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	/**Creates an AttackFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized AttackFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public AttackFactory getInstance() throws SlickException {
		if (FACTORY == null) {
			FACTORY = new AttackFactory();
		}
		return FACTORY;
	}
	
	/**
	 * @param name
	 * @return desired Attack
	 * @see AttackFactory
	 */
	public Attack getAttack(String name) {
		
		return attacks.get(name);
	}
	
	/**
	 * @return a Hashmap of all instantiated Attacks
	 */
	public Map<Attacks, Attack> getAllAttacks() {
		
		return attacks;
	}
	
	/**Resets AttackFactory after a round has finished.<br>
	 * Has no effect if no Instance of AttackFactory exists yet.
	 */
	public static void reset() {
		if (AttackFactory.FACTORY != null) {
			AttackFactory.FACTORY = null;
		}
	}
}

