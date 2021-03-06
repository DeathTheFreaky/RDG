package general;

//how to use these enums: http://stackoverflow.com/questions/10017729/multiple-enum-classes-in-one-java-file

/**Stores all Enums needed for RDG.
 * 
 * @author Flo
 *
 */
public class Enums {
	
	/* no Enums for names and armor types -> limits extensibility */

	public enum ItemClasses {NONE, WEAK, MEDIUM, STRONG}
	public enum Attributes {HP, SPEED, ACCURACY, STRENGTH}
	public enum Levels {EASY, NORMAL, HARD}
	public enum Targets {SELF, OPPONENT}
	/**Defines Potion's work modes.<br><br>
	 * 
	 * INCR -> increase attribute by x over n rounds<br>
	 * DECR -> decrease attribute by x over n rounds<br>
	 * TINCR -> temporarily increase attribute by x for n rounds<br>
	 * TDECR -> temporarily decrease attribute by x for n rounds<br>
	 * LIFT -> lift poison effect
	 */
	public enum Modes {INCR, DECR, TINCR, TDECR, LIFT} 
	public enum WeaponTypes {SINGLEHAND, TWOHAND}	
	public enum Directions {UP, RIGHT, DOWN, LEFT}
	public enum Updates {KEY_PRESSED, KEY_RELEASED}
	public enum ViewingDirections {NORTH, EAST, SOUTH, WEST}
	public enum Channels {PRIVATE, PUBLIC}
	public enum Armor {HEAD, CHEST, ARMS, LEGS, FEET, MAIN_WEAPON, SUB_WEAPON}
}
