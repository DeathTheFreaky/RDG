package elements;

import general.Common;
import general.Enums.Attributes;
import general.Enums.ItemClasses;
import general.Enums.Modes;
import general.Enums.Potions;
import general.Enums.Targets;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = -2994153046048117917L;

	private final String itemDescription;
	
	/* used to determine which item can be found in which type of room -> see rooms config sheet */
	public final ItemClasses ITEM_CLASS;
	
	/* player's or oppenent's attributes affected by this potion */
	public final Attributes EFFECT;
	
	/* target of the potion -> player of opponent */
	public final Targets TARGET;
	
	/* mode of the potion's effect -> see Enums.Modes */
	public final Modes MODE;
	
	/* status change (how much a value in- or decreases */
	public float power; //cannot be final because power needs to be set by selected Potion
	
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
		
		this.itemDescription = description;
		itemDescription.replace("xxx", Float.toString(power));
		itemDescription.replace("nnn", Integer.toString(duration));
		
		this.ITEM_CLASS = itemClass;
		this.EFFECT = effect;
		this.TARGET = target;
		this.MODE = mode;
		this.power = power;
		this.DURATION = duration;
		this.POTION_TYPE = Potions.POTION1;
	}
	
	/**Copy constructor for Potion.
	 * 
	 * @param potion
	 * @param copy
	 * @see Potion
	 */
	public Potion(Potion potion, Element element) {
		
		super(element);
		
		this.itemDescription = potion.itemDescription;
		this.ITEM_CLASS = potion.ITEM_CLASS;
		this.EFFECT = potion.EFFECT;
		this.TARGET = potion.TARGET;
		this.MODE = potion.MODE;
		this.power = potion.power;
		this.DURATION = potion.DURATION;
		this.POTION_TYPE = potion.POTION_TYPE;
	}
	
	public String getDescription() {
		
		return NAME.toUpperCase() + "\n" + itemDescription + "\n" + "Power: " + Common.round(power, 2) + "\n" + "Duration: " + DURATION + " rounds";
	}
}
