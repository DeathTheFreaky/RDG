package config_templates;

public class Weapon_Template {
	
	private String name, item_class, type, image_big, image_small;
	private float class_multiplier, stats_low_multiplier, stats_high_multiplier, attack, speed, accuracy, defence, slots, max;
	
	public Weapon_Template(String name, String item_class, String type, String image_big, String image_small, 
			float class_multiplier, float stats_low_multiplier, float stats_high_multiplier, float attack, float speed, float accuracy, float defence, float slots, float max) {
	
		this.name = name;
		this.item_class = item_class;
		this.type = type;
		this.image_big = image_big;
		this.image_small = image_small;
		this.class_multiplier = class_multiplier;
		this.stats_low_multiplier = stats_low_multiplier;
		this.stats_high_multiplier = stats_high_multiplier;
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

	public String getItem_class() {
		return item_class;
	}

	public String getType() {
		return type;
	}

	public String getImage_big() {
		return image_big;
	}

	public String getImage_small() {
		return image_small;
	}

	public float getClass_multiplier() {
		return class_multiplier;
	}

	public float getStats_low_multiplier() {
		return stats_low_multiplier;
	}

	public float getStats_high_multiplier() {
		return stats_high_multiplier;
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

	public float getSlots() {
		return slots;
	}

	public float getMax() {
		return max;
	}
}
