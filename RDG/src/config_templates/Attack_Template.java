package config_templates;

import enums.Enums.attributes;

public class Attack_Template {
	
	private String name;
	private attributes effect;
	private float stats_low_multiplier, stats_high_multiplier, hp_damage, hit_probability, x;
	
	public Attack_Template(String name, attributes effect, float stats_low_multiplier, 
			float stats_high_multiplier, float hp_damage, float hit_probability, float x) {
		this.name = name;
		this.effect = effect;
		this.stats_low_multiplier = stats_low_multiplier;
		this.stats_high_multiplier = stats_high_multiplier;
		this.hp_damage = hp_damage;
		this.hit_probability = hit_probability;
		this.x = x;
	}

	public String getName() {
		return name;
	}

	public attributes getEffect() {
		return effect;
	}

	public float getStats_low_multiplier() {
		return stats_low_multiplier;
	}

	public float getStats_high_multiplier() {
		return stats_high_multiplier;
	}

	public float getHp_damage() {
		return hp_damage;
	}

	public float getHit_probability() {
		return hit_probability;
	}

	public float getX() {
		return x;
	}
	
}
