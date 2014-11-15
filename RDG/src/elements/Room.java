package elements;

import general.Enums.RoomTypes;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Image;

/**There are 6 types of rooms:<br>
 * Dead End, Hallway, Turn, T-Junction, Junction, Treasure Chamber.<br><br>
 * 
 * A room's type is classified by the position of doors in its walls and determined after the labyrinth algorithm has finished putting doors in walls.<br>
 * Each room type influences which items and monster can be found in a room.<br><br>
 * 
 * Dead End has only 1 door and medium to strong monsters and items can be found here.<br>
 * Hallway has 2 opposing doors, no to normal monsters and mainly medium items can be found here.<br>
 * Turn has 2 rectangular doors, no to normal monsters and mainly medium items can be found here.<br>
 * T-Junction has 3 doors, easy to normal monsters and mainly weak items can be found here.<br>
 * Junctions have 4 doors, easy to normal monsters and mainly weak or none items can be found here.<br>
 * Treasure Chamber is a special room, placed in the middle of the map. It has 2 doors. Hard Monsters and a lot of medium to strong items can be found here.<br>
 */
public class Room {
	
	/* none of the variables shall change its value later on -> make them final */
	/* do not set variables to public -> breaks encapsulation ??? */
	/* make variables public for code reduction */
	
	/* Variable values do not change because they are only needed to fill the map at initialization! */
	
	/* Room Type: Dead End, Hallway, Turn, T-Junction, Junction, Treasure Chamber */
	public final RoomTypes type;
	
	/* Ground textures */
	public final Element[][] background;
	
	/* Monster and items placed inside this room */
	public final Element[][] overlay;
	
	
	/* The following variables from RoomTemplate will be needed by Factory Class:
	 * float itemMultiplier, int itemCount, Map<String, Float> monster, Map<String, Float> find_probabilities, boolean[] doorPositions, String tilesetpath */
	
	/**Constructs a room and positions monsters and items inside this room at random positions (only 1 per tile).<br>
	 * Shall only be called from a Factory Class and the Room's values shall be final.<br>
	 * The Room's Ground Textures, the monster and the items inside the room will be positioned by this Factory Class.<br>
	 * 
	 * @param type
	 * @param background
	 * @param overlay
	 * @see Room
	 */
	public Room(RoomTypes type, Element[][] background, Element[][] overlay) {
		this.type = type;
		this.background = background;
		this.overlay = overlay;
	}
}
