package configTemplates;

import enums.Enums.ItemClasses;
import enums.Enums.WeaponTypes;

/**WeaponTemplate is used to store the default Weapon values.
 * 
 * @author Flo
 *
 */
public class WeaponTemplate {
	
	private String name, imageBig, imageSmall;
	private ItemClasses itemClass;
	private WeaponTypes type;
	private float classMultiplier, statsLowMultiplier, statsHighMultiplier, attack, speed, accuracy, defence;
	private int slots, max;
	
	/**Construct a WeaponTemplate storing the default Weapon values.
	 * 
	 * @param name
	 * @param imageBig
	 * @param imageSmall
	 * @param itemClass
	 * @param type
	 * @param classMultiplier
	 * @param statsLowMultiplier
	 * @param statsHighMultiplier
	 * @param attack
	 * @param speed
	 * @param accuracy
	 * @param defence
	 * @param slots
	 * @param max
	 * @see WeaponTemplate
	 */
	public WeaponTemplate(String name, String imageBig, String imageSmall, ItemClasses itemClass, WeaponTypes type, 
			float classMultiplier, float statsLowMultiplier, float statsHighMultiplier, float attack, float speed, float accuracy, float defence, int slots, int max) {
	
		this.name = name;
		this.itemClass = itemClass;
		this.type = type;
		this.imageBig = imageBig;
		this.imageSmall = imageSmall;
		this.classMultiplier = classMultiplier;
		this.statsLowMultiplier = statsLowMultiplier;
		this.statsHighMultiplier = statsHighMultiplier;
		this.attack = attack;
		this.speed = speed;
		this.accuracy = accuracy;
		this.defence = defence;
		this.slots = slots;
		this.max = max;
	}

	/**
	 * @return Weapon name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return item class (weak, medium, strong)
	 */
	public ItemClasses getItem_class() {
		return itemClass;
	}

	/**
	 * @return Weapon type (singlehand, twohand)
	 */
	public WeaponTypes getType() {
		return type;
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
	 * @return class multiplier for balancing
	 */
	public float getClass_multiplier() {
		return classMultiplier;
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
	 * @return Weapon's attack value
	 */
	public float getAttack() {
		return attack;
	}

	/**
	 * @return Weapon's speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @return Weapon's accuracy
	 */
	public float getAccuracy() {
		return accuracy;
	}

	/**
	 * @return Weapon's defence value (0 except for shield)
	 */
	public float getDefence() {
		return defence;
	}

	/**
	 * @return number of slots (arms) occupied by weapon
	 */
	public int getSlots() {
		return slots;
	}

	/**
	 * @return maximum number of weapon's of this type that player may carry in his hands
	 */
	public int getMax() {
		return max;
	}
}
