package general;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import configLoader.ArmamentTemplate;
import configLoader.PotionTemplate;
import configLoader.WeaponTemplate;
import elements.Armament;
import elements.Element;
import elements.Item;
import elements.Potion;
import elements.Weapon;
import general.Enums.Armor;
import general.Enums.Attributes;
import general.Enums.ItemClasses;
import general.Enums.ItemType;
import general.Enums.Modes;
import general.Enums.Targets;
import general.Enums.WeaponTypes;

/**ItemFactory receives an Armament's, Potion's and Weapon's default parameters from Template classes.<br>
 * It then sets the Item's variables either to the default values or to random values
 * retrieved from Chances class.<br>
 * Stats will be set according to a random Value returned by Chances Class 
 * which lies within statsLowMultiplier and statsHighMultiplier and is then multiplied with classMultiplier.
 */
public class ItemFactory {
	
	public static Element createItem(Item item, float itemMultiplier) throws SlickException {
		
		Element newItem = null;
				
		switch (item.itemType) {
			
			case ARMAMENT:
				newItem = createArmament(item.itemName, itemMultiplier);
				break;
				
			case POTION:
				newItem = createPotion(item.itemName, itemMultiplier);
				break;
			
			case WEAPON:
				newItem = createWeapon(item.itemName, itemMultiplier);
				break;	
		}
		
		return newItem;
	}
	
	/**Creates new Armament with randomized stats.
	 * 
	 * @param name
	 * @return desired Armament
	 * @throws SlickException 
	 * @see ArmamentFactory
	 */
	public static Armament createArmament(String name, float itemMultiplier) throws SlickException {
				
		ResourceManager resources = new ResourceManager().getInstance();
		ArmamentTemplate tempTemplate = resources.ARMAMENT_TEMPLATES.get(name);
		
		Image image = resources.IMAGES.get(name);
		String type = tempTemplate.getType();
		ItemClasses itemClass = tempTemplate.getItem_class();
		float armor = tempTemplate.getArmor() * itemMultiplier * tempTemplate.getClass_multiplier() * 
				Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float speed = tempTemplate.getSpeed() * itemMultiplier * tempTemplate.getClass_multiplier() * 
				Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float bonus = tempTemplate.getBonus();
		Armor armorType = tempTemplate.getArmorType();
		
		return new Armament(name, image, type, itemClass, armor, speed, bonus, armorType);
	}	
	
	/**Creates new Potion with randomized stats.
	 * 
	 * @param name
	 * @return a new Potion
	 * @see PotionFactory
	 */
	public static Potion createPotion(String name, float itemMultiplier) throws SlickException {
		
		ResourceManager resources = new ResourceManager().getInstance();
		PotionTemplate tempTemplate = resources.POTION_TEMPLATES.get(name);
		
		Image image = resources.IMAGES.get(name);
		String description = tempTemplate.getDescription();
		ItemClasses itemClass = tempTemplate.getItem_class();
		Attributes effect = tempTemplate.getEffect();
		Targets target = tempTemplate.getTarget();
		Modes mode = tempTemplate.getMode();
		float power = tempTemplate.getX() *  itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier()); 
		int duration = tempTemplate.getN();
		
		return new Potion(name, image, description, itemClass, effect, target, mode, power, duration);
	}
	
	/**Creates new Weapon with randomized stats.
	 * 
	 * @param name
	 * @return a new Weapon
	 * @throws SlickException 
	 * @see WeaponFactory
	 */
	public static Weapon createWeapon(String name, float itemMultiplier) throws SlickException {
		
		ResourceManager resources = new ResourceManager().getInstance();
		WeaponTemplate tempTemplate = resources.WEAPON_TEMPLATES.get(name);
		
		Image image = resources.IMAGES.get(name);
		float attack = tempTemplate.getAttack() * itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float speed = tempTemplate.getSpeed() * itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float accuracy = tempTemplate.getAccuracy() * itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		float defense = tempTemplate.getDefence() * itemMultiplier * tempTemplate.getClass_multiplier() * Chances.randomFloat(tempTemplate.getStats_low_multiplier(), tempTemplate.getStats_high_multiplier());
		ItemClasses itemClass = tempTemplate.getItem_class();
		WeaponTypes type = tempTemplate.getType();
		int max = tempTemplate.getMax();
		
		return new Weapon(name, image, attack, speed, accuracy, defense, itemClass, type, max);
	}
}
