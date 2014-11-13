package worthy;

import org.newdawn.slick.Image;
import enums.Enums.Armor;

public class Equippment extends Element {
	
	private Armor armorType;

	public Equippment(String equippmentName, Image image, Armor armorType) {
		super(equippmentName, image);
		
		this.armorType = armorType;
	}
	
	
	public Armor getArmorType() {
		return armorType;
	}

}
