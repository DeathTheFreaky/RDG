package elements;

import org.newdawn.slick.Image;

import general.Enums.Armor;

/**Equipment describes all items that can be equipped: armament and weapons.<br>
 * Equipment extends Element.
 * 
 * @see Element
 */
public class Equipment extends Element {
	
	private Armor armorType;

	/**Constructs an Equipment.
	 * 
	 * @param equipmentName
	 * @param image
	 * @param armorType
	 * @see Equipment
	 */
	public Equipment(String equipmentName, Image image, Armor armorType) {
		super(equipmentName, image);
		
		this.armorType = armorType;
	}
	
	
	/**
	 * @return armorType of an Equipment
	 */
	public Armor getArmorType() {
		return armorType;
	}

}
