package enums;

//how to use these enums: http://stackoverflow.com/questions/10017729/multiple-enum-classes-in-one-java-file

public class Enums {
	
	//no enum for names and armor types -> limits extensibility

	public enum ItemClasses {NONE, WEAK, MEDIUM, STRONG}
	public enum Attributes {HP, SPEED, ACCURACY, STRENGTH}
	public enum Levels {EASY, NORMAL, HARD}
	public enum Targets {SELF, OPPONENT}
	public enum Modes {INCR, DECR, TINCR, TDECR, LIFT} 
		//INCR -> increase x over n rounds
		//TINCR -> temporarily increase attribute for n rounds
		//LIFT -> lift poison effect
	public enum WeaponTypes {SINGLEHAND, TWOHAND}	
}
