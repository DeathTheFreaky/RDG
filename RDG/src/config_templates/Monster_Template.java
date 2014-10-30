package config_templates;

import enums.Enums.attributes;
import enums.Enums.levels;

public class Monster_Template {

	private String name, image_big, image_small;
	private attributes kill_bonus_type;
	private levels level; 
	private float stats_low_multiplier, stats_high_multiplier, hp, strength, speed, accuracy, kill_bonus_low, kill_bonus_high;
	
	public Monster_Template(String name, String image_big, String image_small, levels level, attributes kill_bonus_type, 
		float stats_low_multiplier, float stats_high_multiplier, float hp, float strength, float speed, float accuracy, float kill_bonus_low, float kill_bonus_high) {
		
		this.name = name; 
		this.level = level; 
		this.kill_bonus_type = kill_bonus_type; 
		this.image_big = image_big; 
		this.image_small = image_small;
		this.stats_low_multiplier = stats_low_multiplier; 
		this.stats_high_multiplier = stats_high_multiplier; 
		this.hp = hp; 
		this.strength = strength; 
		this.speed = speed; 
		this.accuracy = accuracy; 
		this.kill_bonus_low = kill_bonus_low; 
		this.kill_bonus_high = kill_bonus_high;
	}

	public String getName() {
		return name;
	}

	public levels getLevel() {
		return level;
	}

	public attributes getKill_bonus_type() {
		return kill_bonus_type;
	}

	public String getImage_big() {
		return image_big;
	}

	public String getImage_small() {
		return image_small;
	}

	public float getStats_low_multiplier() {
		return stats_low_multiplier;
	}

	public float getStats_high_multiplier() {
		return stats_high_multiplier;
	}

	public float getHp() {
		return hp;
	}

	public float getStrength() {
		return strength;
	}

	public float getSpeed() {
		return speed;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public float getKill_bonus_low() {
		return kill_bonus_low;
	}

	public float getKill_bonus_high() {
		return kill_bonus_high;
	}
}
