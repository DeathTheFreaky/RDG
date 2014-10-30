package config_templates;

import enums.Enums.item_classes;

public class Armament_Template {
	
	private String name, type, image_big, image_small;
	private item_classes item_class;
	private float class_multiplier, stats_low_multiplier, stats_high_multiplier, armor, speed, bonus;

	public Armament_Template(String name, String type,String image_big, String image_small, item_classes item_class,
			float class_multiplier, float stats_low_multiplier, float stats_high_multiplier, float armor, float speed, float bonus) {
		
		this.name = name;
		this.item_class = item_class;
		this.type = type;
		this.image_big = image_big;
		this.image_small = image_small;
		this.class_multiplier = class_multiplier;
		this.stats_low_multiplier = stats_low_multiplier;
		this.stats_high_multiplier = stats_high_multiplier;
		this.armor = armor;
		this.speed = speed;
		this.bonus = bonus;
	}

	public String getName() {
		return name;
	}

	public item_classes getItem_class() {
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
