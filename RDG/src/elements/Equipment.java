package elements;

import org.newdawn.slick.Image;

import general.Enums.Armor;

/**Equipment describes all items that can be equipped: armament and weapons.<br>
 * Equipment extends Element.
 * 
 * @see Element
 */
public class Equipment extends Element {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3015256401232467688L;
	
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
	
	/**Copy constructor for Equipment.
	 * @param equipment
	 * @param element
	 */
	public Equipment(Equipment equipment, Element element) {
		super(element);
		
		this.armorType = equipment.armorType;
	}
	
	/**
	 * @return armorType of an Equipment
	 */
	public Armor getArmorType() {
		return armorType;
	}
	
	/**Sets a Main Weapon to Sub Weapon.
	 * 
	 */
	public void setAsSubWeapon() {
		if (armorType == Armor.MAIN_WEAPON) {
			armorType = Armor.SUB_WEAPON;
		}
	}
	
	/**Sets a Sub Weapon to Main Weapon.
	 */
	public void setAsMainWeapon() {
		if (armorType == Armor.SUB_WEAPON) {
			armorType = Armor.MAIN_WEAPON;
		}
	}
	
}
