package configTemplates;

import java.util.Map;

public class RoomTemplate {
	
	private String name, description, image;
	private float itemMultiplier;
	private int itemCount;
	private Map<String, Float> monster, find_probabilities;
	private boolean[] doorPositions; //0: N, 1: E, 2: S, 3: W
	
	public RoomTemplate(String name, String description, String image, float itemMultiplier, int itemCount, 
			Map<String, Float> monster, Map<String, Float> find_probabilities, boolean[] doorPositions) {
			
			this.name = name;
			this.description = description;
			this.image = image;
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

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public float getItem_multiplier() {
		return itemMultiplier;
	}

	public int getItem_count() {
		return itemCount;
	}

	public Map<String, Float> getMonster() {
		return monster;
	}

	public Map<String, Float> getFind_probabilities() {
		return find_probabilities;
	}

	public boolean[] getDoor_positions() {
		return doorPositions;
	}
}
