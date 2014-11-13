package worthy;

import org.newdawn.slick.Image;

public class Weapon extends Equippment {

	private String itemClass;	//wofür?
	public int ATTACK;
	public int SPEED;
	public int ACCURACY;
	public int DEFENSE;
	private int slots;	//wofür?
	private int max;	//wofür?
	private boolean doubleHand;

	public Weapon(String weaponName, Image image, int attack, int speed,
			int accuracy, int defense) {
		this(weaponName, image, attack, speed, accuracy, defense, false);
	}
	
	public Weapon(String weaponName, Image image, int attack, int speed, int accuracy, int defense, boolean isDoubleHand) {
		this(weaponName, image, attack, speed, accuracy, defense, isDoubleHand, Armor.MAIN_WEAPON);
	}
	
	public Weapon(String weaponName, Image image, int attack, int speed,
			int accuracy, int defense, boolean doubleHand, Armor weapon) {
		super(weaponName, image, weapon);
		
		this.ATTACK = attack;
		this.SPEED = speed;
		this.ACCURACY = accuracy;
		this.DEFENSE = defense;
		this.doubleHand = doubleHand;
	}
	
	

	public String getItemClass() {
		return itemClass;
	}

	public int getSlots() {
		return slots;
	}

	public int getMax() {
		return max;
	}

	public boolean isDoubleHand() {
		return doubleHand;
	}

}
