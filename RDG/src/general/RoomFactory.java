package general;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

import configLoader.Configloader;
import configLoader.RoomTemplate;
import elements.Element;
import elements.Room;
import gameEssentials.Game;
import general.Enums.RoomTypes;

/**RoomFactory receives a Room's default parameters form RoomTemplate class.<br>
 * It then fills the Room's background and overlay multidimensional arrays with random items, creatures and ground textures.<br>
 * These Items and Monsters are created by their respective Factory Classes.<br>
 * 
 * @see GroundFactory
 */
public class RoomFactory {
	
	/* make sure RoomFactory can only be instantiated once*/
	private static RoomFactory FACTORY = null;
	private static ResourceManager resources = null;
	
	/* size of the room */
	Dimension size;
	
	/* passed from configLoader - used to load defaults */
	private Map<RoomTypes, RoomTemplate> roomTemplates;
	
	/* other Factories needed to fill Room */
	private ArmamentFactory armamentFactory = null;
	private PotionFactory potionFactory = null;
	private WeaponFactory weaponFactory = null;
	private MonsterFactory monsterFactory = null;
	
	/* GroundFactory used to fill the Room's ground textures */
	private GroundFactory groundFactory; //use only if different rooms need different ground textures
	
	/**Creates a RoomFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized RoomFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public RoomFactory() throws SlickException {
		
		try {
			this.roomTemplates = new Configloader().getInstance().getRoomTemplates();
		} catch (IllegalArgumentException | ParserConfigurationException
				| SAXException | IOException e) {
			e.printStackTrace();
		}
		
		size = new Dimension(Game.ROOMWIDTH, Game.ROOMHEIGHT);
		resources = new ResourceManager().getInstance();
		groundFactory = new GroundFactory().getInstance();
	}
	
	/**Creates a RoomFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized RoomFactory
	 * @throws SlickException
	 * @see RoomFactory
	 */
	public RoomFactory getInstance() throws SlickException {
		if (FACTORY == null) {
			FACTORY = new RoomFactory();
		}
		return FACTORY;
	}
	
	/**Creates a new Room with ground textures according to the room type.<br>
	 * The room will be filled randomly with monsters and items.
	 * 
	 * @return an Instance of Room
	 * @see RoomFactory
	 */
	public Room createRoom(RoomTypes type) {
		
		size = new Dimension(Game.ROOMWIDTH, Game.ROOMHEIGHT);
		
		Element[][] background = new Element[Game.ROOMWIDTH][Game.ROOMHEIGHT];
		Element[][] overlay = new Element[Game.ROOMWIDTH][Game.ROOMHEIGHT];
				
		/* null-initialize overlay */
		for (int i = 0; i < size.width-1; i++) {
			for (int j = 0; j < size.height-1; j++) {
				overlay[i][j] = null;
			}
		}
		
		background = fillGround(type, background); 
		
		overlay[1][1] = new Element("Plate Helmet", resources.IMAGES.get("Plate Helmet"),
				1, 1);
		
		//return random values in arraylist
		// http://stackoverflow.com/questions/12487592/randomly-select-an-item-from-a-list
		
		return new Room(type, background, overlay);
	}
	
	/**fills the Room's Ground with textures 
	 * 
	 */
	public Element[][] fillGround(RoomTypes type, Element[][] background) {
		
		/* null-initialize background */
		for (int i = 0; i < size.width-1; i++) {
			for (int j = 0; j < size.height-1; j++) {
				background[i][j] = null;
			}
		}
		
		/* fill background according to room type */
		for (int i = 0; i <= size.width-1; i++) {
			for (int j = 0; j <= size.height-1; j++) {
								
				switch(type) {
					case DEADEND:
						background[i][j] = groundFactory.createYellowGroundOne(i, j);
						break;
					case HALLWAY:
						background[i][j] = groundFactory.createGreenGround(i, j);
						break;
					case TURN:
						background[i][j] = groundFactory.createGreenGround(i, j);
						break;
					case TJUNCTION:
						background[i][j] = groundFactory.createYellowGroundTwo(i, j);
						break;
					case JUNCTION:
						background[i][j] = groundFactory.createYellowGroundOne(i, j);
						break;
					case TREASURECHAMBER:
						background[i][j] = groundFactory.createBrownGround(i, j);
						break;
				}
			}
		}
		
		return background;
	}
}