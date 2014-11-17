package general;

import java.awt.Dimension;
import java.util.Map;

import org.newdawn.slick.SlickException;

import configLoader.RoomTemplate;
import elements.Element;
import elements.Room;
import general.Enums.RoomTypes;

/**RoomFactory receives a Room's default parameters form RoomTemplate class.<br>
 * It then fills the Room's background and overlay multidimensional arrays with random items, creatures and ground textures.<br>
 * These Items and Monsters are created by their respective Factory Classes.<br>
 * 
 * @see GroundFactory
 */
public class RoomFactory {
	
	/* make sure RoomFactory can only be instantiated once*/
	protected static RoomFactory FACTORY = null;
	private static ResourceManager resources = null;
	private static boolean initialized = false;
	
	/* size of the room */
	Dimension size;
	
	/* passed from configLoader - used to load defaults */
	private Map<RoomTypes, RoomTemplate> roomTemplates;
	
	/* background shall be filled with ground textures */
	Element[][] background = null;
	
	/* overlay shall be filled with items and monsters */
	Element[][] overlay = null;
	
	/* GroundFactory used to fill the Room's ground textures */
	//GroundFactory groundFactory; //use only if different rooms need different ground textures
	
	public RoomFactory(Map<RoomTypes, RoomTemplate> roomTemplates) throws SlickException {
		this.roomTemplates = roomTemplates;
		//groundFactory = new GroundFactory().setUpFactory();
	}
	
	/**Creates a RoomFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initizialed RoomFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public RoomFactory setUpFactory(Map<RoomTypes, RoomTemplate> roomTemplates) throws SlickException {
		if (!initialized) {
			initialized = true;
			FACTORY = new RoomFactory(roomTemplates);
		}
		return FACTORY;
	}
	
	/**
	 * @return an Instance of Room
	 * @see RoomFactory
	 */
	public Room createRoom(RoomTypes type) {
		
		size = new Dimension(12, 9);
		
		/* null-initialize overlay */
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				overlay[i][j] = null;
			}
		}
		
		//fillGround(); -> implemented in map - only needed if rooms need specific ground
		
		return new Room(type, background, overlay);
	}
	
	/**fills the Room's Ground with textures 
	 * 
	 */
	/*public void fillGround() {
		
		for (int i = 0; i <= size.width; i++) {
			for (int j = 0; j <= size.height; j++) {

				background[i][j] = groundFactory
						.createYellowGroundOne(i, j);
			}
		}
	}*/
}
