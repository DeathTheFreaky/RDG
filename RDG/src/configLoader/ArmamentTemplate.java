package configLoader;

import general.Enums.Armor;
import general.Enums.ItemClasses;

/**ArmamentTemplate is used to store the default Armament values.
 * 
 * @author Flo
 *
 */
public class ArmamentTemplate {
	
	private String name, type, imageBig, imageSmall;
	private ItemClasses itemClass;
	private float classMultiplier, statsLowMultiplier, statsHighMultiplier, armor, speed, bonus;
	private Armor armorType;
	
	/**Construct an ArmamentTemplate storing the default Armament values.
	 * 
	 * @param name
	 * @param type
	 * @param imageBig
	 * @param imageSmall
	 * @param itemClass
	 * @param classMultiplier
	 * @param statsLowMultiplier
	 * @param statsHighMultiplier
	 * @param armor
	 * @param speed
	 * @param bonus
	 * @see ArmamentTemplate
	 */
	public ArmamentTemplate(String name, String type,String imageBig, String imageSmall, ItemClasses itemClass,
			float classMultiplier, float statsLowMultiplier, float statsHighMultiplier, float armor, float speed, float bonus) {
		
		this.name = name;
		this.itemClass = itemClass;
		this.type = type;
		this.imageBig = imageBig;
		this.imageSmall = imageSmall;
		this.classMultiplier = classMultiplier;
		this.statsLowMultiplier = statsLowMultiplier;
		this.statsHighMultiplier = statsHighMultiplier;
		this.armor = armor;
		this.speed = speed;
		this.bonus = bonus;
		
		if (name.contains("Helmet")) this.armorType = Armor.HEAD;
		else if (name.contains("Harness")) this.armorType = Armor.CHEST;
		else if (name.contains("Gauntlets")) this.armorType = Armor.ARMS;
		else if (name.contains("Cuisse")) this.armorType = Armor.LEGS;
		else if (name.contains("Boots")) this.armorType = Armor.FEET;
	}

	/**
	 * @return Armament name
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
	 * @return Armament type (leather, habergeon, plate)
	 */
	public String getType() {
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
	 * @return armor value of this armament
	 */
	public float getArmor() {
		return armor;
	}

	/**
	 * @return speed value of this armament
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @return bonus for complete Armament set
	 */
	public float getBonus() {
		return bonus;
	}
	
	/**
	 * @return armorType
	 */
	public Armor getArmorType() {
		return armorType;
	}
}
