package configTemplates;

import enums.Enums.ItemClasses;
import enums.Enums.WeaponTypes;

public class WeaponTemplate {
	
	private String name, imageBig, imageSmall;
	private ItemClasses itemClass;
	private WeaponTypes type;
	private float classMultiplier, statsLowMultiplier, statsHighMultiplier, attack, speed, accuracy, defence;
	private int slots, max;
	
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

	public String getName() {
		return name;
	}

	public ItemClasses getItem_class() {
		return itemClass;
	}

	public WeaponTypes getType() {
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

	public float getAttack() {
		return attack;
	}

	public float getSpeed() {
		return speed;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public float getDefence() {
		return defence;
	}

	public int getSlots() {
		return slots;
	}

	public int getMax() {
		return max;
	}
}
