package elements;

import org.newdawn.slick.Image;

import general.Enums.Armor;
import general.Enums.ItemClasses;
import general.Enums.WeaponTypes;

/**Weapon is used by a player to damage an opponent.<br>
 * Weapon can be single or two handed.<br>
 * Either two single handed weapons or one two handed weapon can be equipped at a time.<br>
 * However, shield is a single handed weapon but only one shield might be carried.<br><br>
 * Weapon extends Equipment.
 * 
 * @see Equipment
 */
public class Weapon extends Equipment {

	/* none of the variables shall change its value later on -> make them final */
	/* do not set variables to public -> breaks encapsulation ??? */
	/* make variables public for code reduction */

	/* used to determine which item can be found in which type of room -> see rooms config sheet */
	public final ItemClasses ITEM_CLASS;
	
	/* singleHand or twoHand -> player can carry 2 singleHand (except for shield) but only 1 twoHand */
	public final WeaponTypes TYPE;
	
	/* Attributes */
	public final float ATTACK; 
	public final float SPEED;
	public final float ACCURACY;
	public final float DEFENSE;
	
	/* maximum number of weapons of same type that player may carry
	 * single hand weapons always have a max of 2, 
	 * except for shield which his a max of 1 -> shield is single hand but two shiels make no sense
	 * double hand weapons always have a max of 2 */
	public final int MAX;	
	
	/*slots is obsolete -> covered by WeaponTypes*/
	
	/* classMultiplier, statsLowMulitplier and statsHighMultiplier are needed in Factory Class; 
	 * statsLowMulitplier and statsHighMultiplier are used by randomClass to return value within this interval;
	 * classMultiplier is used on all stats of this class for balancing */

	/**Constructs a Weapon.<br>
	 * Shall only be called from a Factory Class and the Weapon's values shall be final.<br>
	 * Attack, Speed, Accuracy and Defense will be set according to a random factor between statsLowMulitplier and statsHighMultiplier 
	 * returned by randomClass and a balancing classMultiplier.
	 * 
	 * @param weaponName
	 * @param image
	 * @param attack
	 * @param speed
	 * @param accuracy
	 * @param defense
	 * @param itemClass
	 * @param type
	 * @param max
	 * @see Weapon
	 */
	public Weapon(String weaponName, Image image, float attack, float speed,
			float accuracy, float defense, ItemClasses itemClass, WeaponTypes type, int max) {

		super(weaponName, image, Armor.MAIN_WEAPON); //what is a MAIN_WEAPON (vs SUB_WEAPON)?!?
		
		this.ITEM_CLASS = itemClass;
		this.ATTACK = attack;
		this.SPEED = speed;
		this.ACCURACY = accuracy;
		this.DEFENSE = defense;
		this.TYPE = type;
		this.MAX = max;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return NAME + ":\n" + "Typ: " + TYPE + "\n" + "Strength: " + ATTACK + "\n"
				+ "Speed: " + SPEED + "\n" + "Accuracy: " + ACCURACY + "\n";
	}
}
