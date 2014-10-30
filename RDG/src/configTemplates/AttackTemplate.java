package configTemplates;

import enums.Enums.Attributes;

public class AttackTemplate {
	
	private String name;
	private Attributes effect;
	private float statsLowMultiplier, statsHighMultiplier, hpDamage, hitProbability, x;
	
	public AttackTemplate(String name, Attributes effect, float statsLowMultiplier, 
			float statsHighMultiplier, float hpDamage, float hitProbability, float x) {
		this.name = name;
		this.effect = effect;
		this.statsLowMultiplier = statsLowMultiplier;
		this.statsHighMultiplier = statsHighMultiplier;
		this.hpDamage = hpDamage;
		this.hitProbability = hitProbability;
		this.x = x;
	}

	public String getName() {
		return name;
	}

	public Attributes getEffect() {
		return effect;
	}

	public float getStats_low_multiplier() {
		return statsLowMultiplier;
	}

	public float getStats_high_multiplier() {
		return statsHighMultiplier;
	}

	public float getHp_damage() {
		return hpDamage;
	}

	public float getHit_probability() {
		return hitProbability;
	}

	public float getX() {
		return x;
	}
	
}
