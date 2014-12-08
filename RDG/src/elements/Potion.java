package elements;

import general.Enums.Attributes;
import general.Enums.ItemClasses;
import general.Enums.Modes;
import general.Enums.Potions;
import general.Enums.Targets;
import general.Enums.WeaponTypes;

import org.newdawn.slick.Image;

/**Potion can be used to increase the player's attributes or to decrease the opponent's attributes.<br>
 * Some Potions take effect instantly, others over a couple of rounds.<br><br>
 * 
 * Potion extends Element.
 *
 * @see Element
 */
public class Potion extends Element {
	
	/* none of the variables shall change its value later on -> make them final */
	/* do not set variables to public -> breaks encapsulation ??? */
	/* make variables public for code reduction */

	public final String DESCRIPTION;
	
	/* used to determine which item can be found in which type of room -> see rooms config sheet */
	public final ItemClasses ITEM_CLASS;
	
	/* player's or oppenent's attributes affected by this potion */
	public final Attributes EFFECT;
	
	/* target of the potion -> player of opponent */
	public final Targets TARGET;
	
	/* mode of the potion's effect -> see Enums.Modes */
	public final Modes MODE;
	
	/* status change (how much a value in- or decreases */
	public final float POWER;
	
	/* states how many rounds this potion will take effect */
	public int DURATION; 
	
	/* needed to determine a potions position in armor view */
	public Potions POTION_TYPE;
	
	/* Potion's total Effect is made up of EFFECT (Attributes),
	 * TARGET, MODE, POWER and DURATION */
	
	/* classMultiplier, statsLowMulitplier and statsHighMultiplier are needed in Factory Class; 
	 * statsLowMulitplier and statsHighMultiplier are used by randomClass to return value within this interval;
	 * classMultiplier is used on all stats of this class for balancing */

	/**Constructs a Potion.<br>
	 * Shall only be called from a Factory Class and the Potion's values shall be final.<br>
	 * Power value will be set according to a random factor between statsLowMulitplier and statsHighMultiplier 
	 * returned by randomClass and a balancing classMultiplier.
	 * 
	 * @param potionName
	 * @param image
	 * @param description
	 * @param itemClass
	 * @param effect
	 * @param target
	 * @param mode
	 * @param x
	 * @param n
	 * @see Potion
	 */
	public Potion(String potionName, Image image, String description, ItemClasses itemClass,
			Attributes effect, Targets target, Modes mode, float power, int duration) {
		
		super(potionName, image);
		
		this.DESCRIPTION = description;
		this.ITEM_CLASS = itemClass;
		this.EFFECT = effect;
		this.TARGET = target;
		this.MODE = mode;
		this.POWER = power;
		this.DURATION = duration;
		this.POTION_TYPE = Potions.POTION1;
	}
}
