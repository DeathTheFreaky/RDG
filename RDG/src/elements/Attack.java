package elements;

import general.Enums.Attacks;
import general.Enums.Attributes;

/**There are 4 types of attacks which all decrease the opponents Health Points and with differing special effects on the opponent:<br>
 * Torso, Head, Arms, Legs.<br><br>
 * 
 * Torso grants the highest hit probability, but has no special effects.<br>
 * Head has the lowest hit probability, but deals the most damage and decreases the opponent's accuracy.<br>
 * Arms has the second highest hit probability, deals the least hp damage but reduces the opponent's strength.<br>
 * Legs has the third highest hit probability, deals the second lowest hp damage and reduces the opponent's speed.<br>
 */
public class Attack {
	
	/* none of the variables shall change its value later on -> make them final */
	/* do not set variables to public -> breaks encapsulation ??? */
	/* make variables public for code reduction */
	
	/* Torso, Head, Arms, Legs */
	public final Attacks type;
	
	/* special effect -> attribute which is decreased on opponent */
	public final Attributes effect;
	
	/* Health Point Damage Multiplier inflicted on the opponent */
	public final float hpDamageMultiplier;
	
	/* Probability that this Attack hits the opponent */
	public final float hitProbability;
	
	/* Damage Multiplier on opponent's attribute dealt by attack's special effect */
	public final float attributeDamageMultiplier;
	
	/* classMultiplier, statsLowMulitplier and statsHighMultiplier are needed in Factory Class; 
	 * statsLowMulitplier and statsHighMultiplier are used by randomClass to return value within this interval;
	 * classMultiplier is used on all stats of this class for balancing */
	
	/**Constructs an Attack.<br>
	 * Shall only be called from a Factory Class and the Attack's values shall be final.<br>
	 * hpDamageMultiplier and attributeDamageMultiplier values will be set according to a random factor between statsLowMulitplier and statsHighMultiplier 
	 * returned by randomClass and a balancing classMultiplier.
	 * 
	 * @param type
	 * @param effect
	 * @param hpDamage
	 * @param hitProbability
	 * @param x
	 * @see Attack
	 */
	public Attack(Attacks type, Attributes effect, float hpDamage, float hitProbability, float x) {
		this.type = type;
		this.effect = effect;
		this.hpDamageMultiplier = hpDamage;
		this.hitProbability = hitProbability;
		this.attributeDamageMultiplier = x;
	}
}
