package elements;

import general.Enums.ItemType;

/**This class is used by Chances Class to return a random Item with its name and type.
 *
 */
public class Item {
	
	/* an Item's name */
	public final String itemName;
	
	/* an Item's type */
	public final ItemType itemType;
	
	/**Store an Item's name and type (Armament, Potion and Weapon).
	 * 
	 * @param itemName
	 * @param itemType
	 */
	public Item (String itemName, ItemType itemType) {
		this.itemName = itemName;
		this.itemType = itemType;
	}
}
