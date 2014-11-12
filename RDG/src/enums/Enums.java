package enums;

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
	 *  
	 * @author Flo
	 *
	 */
	public enum Modes {INCR, DECR, TINCR, TDECR, LIFT} 
		//INCR -> increase x over n rounds
		//TINCR -> temporarily increase attribute for n rounds
		//LIFT -> lift poison effect
	public enum WeaponTypes {SINGLEHAND, TWOHAND}	
}
