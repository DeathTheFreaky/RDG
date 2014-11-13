package configLoader;

import general.Enums.Attributes;

/**AttackTemplate is used to store the default Attack values.
 * 
 * @author Flo
 *
 */
public class AttackTemplate {
	
	private String name;
	private Attributes effect;
	private float statsLowMultiplier, statsHighMultiplier, hpDamage, hitProbability, x;
	
	/**Construct an AttackTemplate storing the default Attack values.
	 * 
	 * @param name
	 * @param effect
	 * @param statsLowMultiplier
	 * @param statsHighMultiplier
	 * @param hpDamage
	 * @param hitProbability
	 * @param x
	 * @see AttackTemplate
	 */
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

	/**
	 * @return Attack's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return opponent's attribute which will suffer from malus<br><br>
	 * 
	 * (hp, speed, accuracy, strength)
	 */
	public Attributes getEffect() {
		return effect;
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
	 * @return damage to opponent's health phoints
	 */
	public float getHp_damage() {
		return hpDamage;
	}

	/**
	 * @return hit probability on associated body part
	 */
	public float getHit_probability() {
		return hitProbability;
	}

	/**
	 * @return attribute malus for opponent
	 */
	public float getX() {
		return x;
	}
	
}
