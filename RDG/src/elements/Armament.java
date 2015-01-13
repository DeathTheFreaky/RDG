package elements;

import org.newdawn.slick.Image;

import general.Common;
import general.Enums.Armor;
import general.Enums.ItemClasses;

/**
 * Armament is used by a player to protect himself against damage.<br>
 * There are different types of Armament available for the player's head, chest,
 * arms and feet. <br>
 * There are also different sets of Armament.<br>
 * A full set of matching Armament will grant the player an extra bonus on armor
 * and speed.<br>
 * <br>
 * 
 * Armament extends Equipment.
 * 
 * @see Equipment
 */
public class Armament extends Equipment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1599544918299157309L;

	/* none of the variables shall change its value later on -> make them final */
	/* do not set variables to public -> breaks encapsulation ??? */
	/* make variables public for code reduction */

	/*
	 * used to determine which item can be found in which type of room -> see
	 * rooms config sheet
	 */
	public final ItemClasses ITEM_CLASS;

	/* defines the type of armament (its material) */
	public final String TYPE;

	/* Attributes */
	public final float ARMOR;
	public final float SPEED;

	/* bonus received for a complete set of armor */
	public final float BONUS;

	/*
	 * classMultiplier, statsLowMulitplier and statsHighMultiplier are needed in
	 * Factory Class; statsLowMulitplier and statsHighMultiplier are used by
	 * randomClass to return value within this interval; classMultiplier is used
	 * on all stats of this class for balancing
	 */

	/**
	 * Constructs an Armament.<br>
	 * Shall only be called from a Factory Class and the Armament's values shall
	 * be final.<br>
	 * Armor and speed values will be set according to a random factor between
	 * statsLowMulitplier and statsHighMultiplier returned by randomClass and a
	 * balancing classMultiplier.
	 * 
	 * @param armamentName
	 * @param image
	 * @param type
	 * @param itemClass
	 * @param armor
	 * @param speed
	 * @param bonus
	 * @param armorType
	 * @see Armament
	 */
	public Armament(String armamentName, Image image, String type,
			ItemClasses itemClass, float armor, float speed, float bonus,
			Armor armorType) {

		super(armamentName, image, armorType);

		this.ITEM_CLASS = itemClass;
		this.TYPE = type;
		this.ARMOR = armor;
		this.SPEED = speed;
		this.BONUS = bonus;
	}

	public Armament(Armament armament, Equipment equipment, Element element) {
		
		super(equipment, element);

		this.ITEM_CLASS = armament.ITEM_CLASS;
		this.TYPE = armament.TYPE;
		this.ARMOR = armament.ARMOR;
		this.SPEED = armament.SPEED;
		this.BONUS = armament.BONUS;
	}

	@Override
	public String getDescription() {
		return NAME.toUpperCase() + "\n" + "Typ: " + TYPE + "\n" + "Armor: " + Common.round(ARMOR, 2) + "\n"
				+ "Speed: " + Common.round(SPEED, 2) + "\n" + "Bonus: " + Common.round(BONUS, 2);
	}

}
