package configLoader;

import general.Enums.ItemClasses;
import general.Enums.Levels;
import general.Enums.RoomTypes;

import java.util.Map;

/**RoomTemplate is used to store the default Room values.
 * 
 * @author Flo
 *
 */
public class RoomTemplate {
	
	private RoomTypes type;
	private String description, image;
	private float itemMultiplier;
	private int monsterCount, itemCount;
	private Map<Levels, Float> monster;
	private Map<ItemClasses, Float> find_probabilities;
	private boolean[] doorPositions; //0: N, 1: E, 2: S, 3: W
	
	/**Construct a RoomTemplate storing the default Room values.
	 * 
	 * @param name
	 * @param description
	 * @param image
	 * @param itemMultiplier
	 * @param itemCount
	 * @param monster
	 * @param find_probabilities
	 * @param doorPositions
	 * @see RoomTemplate
	 */
	public RoomTemplate(RoomTypes type, String description, String image, int monsterCount, float itemMultiplier, int itemCount, 
			Map<Levels, Float> monster, Map<ItemClasses, Float> find_probabilities, boolean[] doorPositions) {
		
			this.type = type;
			this.description = description;
			this.image = image;
			this.monsterCount = monsterCount;
			this.itemMultiplier = itemMultiplier;
			this.itemCount = itemCount;
			this.monster = monster;
			this.find_probabilities = find_probabilities;
			this.doorPositions = doorPositions;
			
	}
	//maze first creates rooms and doors, then afterwards checks which roomtypes those are
	//first create new room with appropriate type, then overwrite door positions
	//use module to loop in both directions of map
	 /*
	  * 4 doors: always Junction
	  * 3 doors: always T-Junction
	  *	2 doors: Turn, or Hallway, or Treasure Chamber (always in middle of Map)
	  *           - Turn: 1 in two concurrent Positions (+1/-1)
	  *           - Hallways: 1 in two opposing Positions (+2)
	  *			  - Treasure Chamber: Room is in Middle of Map (n/2,n/2)
	  * 1 door: always Dead End
	  * player start: always player start
	  */

	/**
	 * @return Room type name
	 */
	public RoomTypes getType() {
		return type;
	}

	/**
	 * @return Room description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return ground image
	 */
	public String getImage() {
		return image;
	}
	
	/**
	 * @return maximum number of monsters found in this Room
	 */
	public int getMonsterCount() {
		return monsterCount;
	}

	/**
	 * @return multiplier on items' stats found in this Room
	 */
	public float getItemMultiplier() {
		return itemMultiplier;
	}

	/**
	 * @return maximum number of items found in this Room
	 */
	public int getItemCount() {
		return itemCount;
	}

	/**
	 * @return possibilities for all monsters of each specific level to appear in this room
	 */
	public Map<Levels, Float> getMonster() {
		return monster;
	}

	/**
	 * @return probabilites for all items of each specific item class to be found in this room
	 */
	public Map<ItemClasses, Float> getFind_probabilities() {
		return find_probabilities;
	}

	/**
	 * @return possible arrangement of doors for this room<br><br>
	 * 
	 * Room type will be determined by identifying door arrangement.
	 */
	public boolean[] getDoor_positions() {
		return doorPositions;
	}
}
