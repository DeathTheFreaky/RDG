package configTemplates;

import enums.Enums.Attributes;
import enums.Enums.Levels;

/**MonsterTemplate is used to store the default Monster values.
 * 
 * @author Flo
 *
 */
public class MonsterTemplate {

	private String name, imageBig, imageSmall;
	private Attributes killBonusType;
	private Levels level; 
	private float statsLowMultiplier, statsHighMultiplier, hp, strength, speed, accuracy, killBonusLow, killBonusHigh;
	
	/**Construct a MonsterTemplate storing the default Monster values.
	 * 
	 * @param name
	 * @param imageBig
	 * @param imageSmall
	 * @param level
	 * @param killBonusType
	 * @param statsLowMultiplier
	 * @param statsHighMultiplier
	 * @param hp
	 * @param strength
	 * @param speed
	 * @param accuracy
	 * @param killBonusLow
	 * @param killBonusHigh
	 */
	public MonsterTemplate(String name, String imageBig, String imageSmall, Levels level, Attributes killBonusType, 
		float statsLowMultiplier, float statsHighMultiplier, float hp, float strength, float speed, float accuracy, float killBonusLow, float killBonusHigh) {
		
		this.name = name; 
		this.level = level; 
		this.killBonusType = killBonusType; 
		this.imageBig = imageBig; 
		this.imageSmall = imageSmall;
		this.statsLowMultiplier = statsLowMultiplier; 
		this.statsHighMultiplier = statsHighMultiplier; 
		this.hp = hp; 
		this.strength = strength; 
		this.speed = speed; 
		this.accuracy = accuracy; 
		this.killBonusLow = killBonusLow; 
		this.killBonusHigh = killBonusHigh;
	}

	/**
	 * @return Monster's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Monster's difficulty level (easy, normal, hard)
	 */
	public Levels getLevel() {
		return level;
	}

	/**
	 * @return player's attribute that will be increased for killing this monster<br><br>
	 * 
	 * (hp, speed, accuracy, strength)
	 */
	public Attributes getKill_bonus_type() {
		return killBonusType;
	}

	/**
	 * @return detailed image
	 */
	public String getImage_big() {
		return imageBig;
	}

	/**
	 * @return map image
	 */
	public String getImage_small() {
		return imageSmall;
	}

	/**
	 * @return low barrier for Stats multiplier
	 */
	public float getStats_low_multiplier() {
		return statsLowMultiplier;
	}

	/**
	 * @return high barrier for Stats multiplier
	 */
	public float getStats_high_multiplier() {
		return statsHighMultiplier;
	}

	/**
	 * @return Monster's health points
	 */
	public float getHp() {
		return hp;
	}

	/**
	 * @return Monster's strength
	 */
	public float getStrength() {
		return strength;
	}

	/**
	 * @return Monster's speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @return Monster's accuracy
	 */
	public float getAccuracy() {
		return accuracy;
	}

	/**
	 * @return low barrier for kill bonus on associated player attributes
	 */
	public float getKill_bonus_low() {
		return killBonusLow;
	}

	/**
	 * @return high barrier for kill bonus on associated player attributes
	 */
	public float getKill_bonus_high() {
		return killBonusHigh;
	}
}
