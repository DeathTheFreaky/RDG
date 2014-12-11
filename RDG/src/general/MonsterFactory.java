package general;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import configLoader.MonsterTemplate;
import elements.Monster;
import general.Enums.Attributes;
import general.Enums.Levels;

/**MonsterFactory receives a Monster's default parameters from MonsterTemplate class.<br>
 * It then sets the Monster's variables either to the default values or to random values
 * retrieved from Chances class.<br>
 * Health points, strength, speed and accuracy and killBonus will be set according to a random Value returned by Chances Class 
 * which lies within statsLowMultiplier and statsHighMultiplier and is then multiplied with classMultiplier.
 */
public class MonsterFactory {
	
	/**Creates new Monster with randomized stats.
	 * 
	 * @param name
	 * @return a new Monster
	 * @throws SlickException 
	 * @see MonsterFactory
	 */
	public static Monster createMonster(String name) throws SlickException {
		
		ResourceManager resources = new ResourceManager().getInstance();
		MonsterTemplate tempTemplate = resources.MONSTER_TEMPLATES.get(name);
		
		Image image = resources.IMAGES.get(name);
		Levels level = tempTemplate.getLevel();
		Attributes killBonusType = tempTemplate.getKill_bonus_type();
		float killBonus = tempTemplate.getClassMultiplier() * Chances.randomFloat(tempTemplate.getKill_bonus_low(), tempTemplate.getKill_bonus_high()); 
		float hp = tempTemplate.getHp() * tempTemplate.getClassMultiplier() * Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier()); 
		float strength = tempTemplate.getStrength() * tempTemplate.getClassMultiplier() * Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier()); 
		float speed = tempTemplate.getSpeed() * tempTemplate.getClassMultiplier() * Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());  
		float accuracy = tempTemplate.getAccuracy() * tempTemplate.getClassMultiplier() * Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier()); 
		
		return new Monster(name, image, level, killBonusType, killBonus, hp, strength, speed, accuracy);
	}
}

