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
	
	/* used on every call of this class to vary attack strength based on random values */
	public final float statsLowMultiplier, statsHighMultiplier;
		
	/* classMultiplier is needed in Factory Class; 
	 * classMultiplier is used on all stats of this class for balancing */
	
	/**Constructs an Attack.<br>
	 * The Attack's values shall be final.<br>
	 * Attack shall only be created by Fight Class.<br>
	 * hpDamageMultiplier and attributeDamageMultiplier values will be set according to a random factor between statsLowMulitplier and statsHighMultiplier 
	 * returned by randomClass and a balancing classMultiplier.
	 * 
	 * @param type
	 * @param effect
	 * @param hpDamageMultiplier
	 * @param hitProbability
	 * @param attributeDamageMultiplier
	 * @see Attack
	 */
	public Attack(Attacks type, Attributes effect, float hpDamageMultiplier, float hitProbability, float attributeDamageMultiplier, float statsLowMultiplier, float statsHighMultiplier) {
		this.type = type;
		this.effect = effect;
		this.hpDamageMultiplier = hpDamageMultiplier;
		this.hitProbability = hitProbability;
		this.attributeDamageMultiplier = attributeDamageMultiplier;
		this.statsLowMultiplier = statsLowMultiplier;
		this.statsHighMultiplier = statsHighMultiplier;
	}
}
