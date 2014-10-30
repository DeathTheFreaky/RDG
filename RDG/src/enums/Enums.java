package enums;

//how to use these enums: http://stackoverflow.com/questions/10017729/multiple-enum-classes-in-one-java-file

public class Enums {
	
	//no enum for names and armor types -> limits extensibility

	public enum item_classes {NONE, WEAK, MEDIUM, STRONG}
	public enum attributes {HP, SPEED, ACCURACY, STRENGTH}
	public enum levels {EASY, NORMAL, HARD}
	public enum targets {SELF, OPPONENT}
	public enum modes {INCR, DECR, TINCR, TDECR, LIFT} 
		//INCR -> increase x over n rounds
		//TINCR -> temporarily increase attribute for n rounds
		//LIFT -> lift poison effect
	public enum weapon_types {SINGLEHAND, TWOHAND}	
}
