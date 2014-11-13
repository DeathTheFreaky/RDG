package configTemplates;

import enums.Enums.Attributes;
import enums.Enums.ItemClasses;
import enums.Enums.Modes;
import enums.Enums.Targets;

/**PotionTemplate is used to store the default Potion values.
 * 
 * @author Flo
 *
 */
public class PotionTemplate {
	
	private String name, description, imageBig, imageSmall;
	private ItemClasses itemClass;
	private Attributes effect;
	private Targets target;
	private Modes mode;
	private float classMultiplier, statsLowMultiplier, statsHighMultiplier, x; 
	private int n;
	
	/**Construct a PotionTemplate storing the default Potion values.
	 * 
	 * @param name
	 * @param description
	 * @param imageBig
	 * @param imageSmall
	 * @param itemClass
	 * @param target
	 * @param mode
	 * @param effect
	 * @param classMultiplier
	 * @param statsLowMultiplier
	 * @param statsHighMultiplier
	 * @param x
	 * @param n
	 * @see PotionTemplate
	 */
	public PotionTemplate(String name, String description, String imageBig, String imageSmall, ItemClasses itemClass, Targets target, Modes mode, Attributes effect,
			float classMultiplier, float statsLowMultiplier, float statsHighMultiplier, float x, int n){
		
		this.name = name;
		this.itemClass = itemClass;
		this.description = description;
		this.target = target;
		this.mode = mode;
		this.effect = effect;
		this.imageBig = imageBig;
		this.imageSmall = imageSmall;
		this.classMultiplier = classMultiplier;
		this.statsLowMultiplier = statsLowMultiplier;
		this.statsHighMultiplier = statsHighMultiplier;
		this.x = x;
		this.n = n;
	}

	/**
	 * @return Potion's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return item class (weak, medium, strong)
	 */
	public ItemClasses getItem_class() {
		return itemClass;
	}

	/**
	 * @return description of the Potion
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return target of the Potion
	 */
	public Targets getTarget() {
		return target;
	}

	/**
	 * @return Potion's work mode <br><br>
	 * 
	 * (INCR, DECR, TINCR, TDECR, LIFT)
	 */
	public Modes getMode() {
		return mode;
	}

	/**
	 * @return Attributes affected by the Potion<br><br>
	 * 
	 * (hp, speed, accuracy, strength)
	 */
	public Attributes getEffect() {
		return effect;
	}

	/**
	 * @return detailed image
	 */
	public String getImage_big() {
		return imageBig;
	}

	/**
	 * @return map image
	 */
	public String getImage_small() {
		return imageSmall;
	}

	/**
	 * @return class multiplier for balancing
	 */
	public float getClass_multiplier() {
		return classMultiplier;
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
	 * @return value on associated attribute
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return number of rounds the potion takes effect
	 */
	public int getN() {
		return n;
	}
}
