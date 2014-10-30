package config_templates;

import enums.Enums.item_classes;
import enums.Enums.weapon_types;

public class Weapon_Template {
	
	private String name, image_big, image_small;
	private item_classes item_class;
	private weapon_types type;
	private float class_multiplier, stats_low_multiplier, stats_high_multiplier, attack, speed, accuracy, defence;
	private int slots, max;
	
	public Weapon_Template(String name, String image_big, String image_small, item_classes item_class, weapon_types type, 
			float class_multiplier, float stats_low_multiplier, float stats_high_multiplier, float attack, float speed, float accuracy, float defence, int slots, int max) {
	
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

	public item_classes getItem_class() {
		return item_class;
	}

	public weapon_types getType() {
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

	public int getSlots() {
		return slots;
	}

	public int getMax() {
		return max;
	}
}
