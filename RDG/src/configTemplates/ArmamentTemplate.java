package configTemplates;

import enums.Enums.ItemClasses;

public class ArmamentTemplate {
	
	private String name, type, imageBig, imageSmall;
	private ItemClasses itemClass;
	private float classMultiplier, statsLowMultiplier, statsHighMultiplier, armor, speed, bonus;

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
	}

	public String getName() {
		return name;
	}

	public ItemClasses getItem_class() {
		return itemClass;
	}

	public String getType() {
		return type;
	}

	public String getImage_big() {
		return imageBig;
	}

	public String getImage_small() {
		return imageSmall;
	}

	public float getClass_multiplier() {
		return classMultiplier;
	}

	public float getStats_low_multiplier() {
		return statsLowMultiplier;
	}

	public float getStats_high_multiplier() {
		return statsHighMultiplier;
	}

	public float getArmor() {
		return armor;
	}

	public float getSpeed() {
		return speed;
	}

	public float getBonus() {
		return bonus;
	}
}
