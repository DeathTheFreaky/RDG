package worthy;

import org.newdawn.slick.Image;

public class Equippment extends Element {
	
	public enum Armor {
		HEAD, CHEST, ARMS, LEGS, FEET, MAIN_WEAPON, SUB_WEAPON
	}
	
	private Armor armorType;

	public Equippment(String equippmentName, Image image, Armor armorType) {
		super(equippmentName, image);
		
		this.armorType = armorType;
	}
	
	
	public Armor getArmorType() {
		return armorType;
	}

}
