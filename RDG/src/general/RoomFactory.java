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
	
	/* background shall be filled with ground textures */
	Element[][] background = null;
	
	/* overlay shall be filled with items and monsters */
	Element[][] overlay = null;
	
	/* other Factories needed to fill Room */
	private ArmamentFactory armamentFactory = null;
	private PotionFactory potionFactory = null;
	private WeaponFactory weaponFactory = null;
	private MonsterFactory monsterFactory = null;
	
	/* GroundFactory used to fill the Room's ground textures */
	//GroundFactory groundFactory; //use only if different rooms need different ground textures
	
	public RoomFactory() throws SlickException {
		try {
			this.roomTemplates = new Configloader().getInstance().getRoomTemplates();
		} catch (IllegalArgumentException | ParserConfigurationException
				| SAXException | IOException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * @return an Instance of Room
	 * @see RoomFactory
	 */
	public Room createRoom(RoomTypes type) {
		
		size = new Dimension(Game.ROOMWIDTH, Game.ROOMHEIGHT);
		
		/* null-initialize overlay */
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				overlay[i][j] = null;
			}
		}
		
		//fillGround(); -> implemented in map - only needed if rooms need specific ground
		//return new Room(type, background, overlay);
		
		return new Room(type, background, overlay); //background is created by Map but passed here for simpler changes
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
