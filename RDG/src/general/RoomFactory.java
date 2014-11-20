package general;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;

import org.newdawn.slick.SlickException;

import configLoader.ArmamentTemplate;
import configLoader.RoomTemplate;
import elements.Element;
import elements.Equipment;
import elements.Monster;
import elements.Room;
import gameEssentials.Game;
import general.Enums.Armor;
import general.Enums.Levels;
import general.Enums.RoomTypes;

/**
 * RoomFactory receives a Room's default parameters form RoomTemplate class.<br>
 * It then fills the Room's background and overlay multidimensional arrays with
 * random items, creatures and ground textures.<br>
 * These Items and Monsters are created by their respective Factory Classes.<br>
 * 
 * @see GroundFactory
 */
public class RoomFactory {

	/**
	 * Creates a new Room with ground textures according to the room type.<br>
	 * The room will be filled randomly with monsters and items.
	 * 
	 * @return an Instance of Room
	 * @throws SlickException 
	 * @see RoomFactory
	 */
	public static Room createRoom(RoomTypes type) throws SlickException {
		
		ResourceManager resources = new ResourceManager().getInstance();
		RoomTemplate tempTemplate = resources.ROOM_TEMPLATES.get(type);

		Dimension size = new Dimension(Game.ROOMWIDTH, Game.ROOMHEIGHT);
		Element[][] background = new Element[Game.ROOMWIDTH][Game.ROOMHEIGHT];
		Element[][] overlay = new Element[Game.ROOMWIDTH][Game.ROOMHEIGHT];

		/* null-initialize overlay */
		for (int i = 0; i < size.width - 1; i++) {
			for (int j = 0; j < size.height - 1; j++) {
				overlay[i][j] = null;
			}
		}

		overlay = addMonster(type, overlay, tempTemplate);
		overlay = addItems(type, overlay, resources);
		background = fillGround(type, background, size);

		overlay[1][1] = new Equipment("Plate Helmet",
				resources.IMAGES.get("Plate Helmet"), Armor.HEAD);
		// new Element("Plate Helmet", resources.IMAGES.get("Plate Helmet"),1,
		// 1);

		// return random values in arraylist
		// http://stackoverflow.com/questions/12487592/randomly-select-an-item-from-a-list

		return new Room(type, background, overlay);
	}

	/**
	 * Puts a Monster into a Room.<br>
	 * Which kind of Monster and whether a Monster is placed in the Room depends
	 * on the Room type and the associated config values.
	 * 
	 * @param type
	 * @param overlay
	 * @param resources
	 * @param map 
	 * @return an overlay with or without a placed monster
	 * @throws SlickException 
	 */
	private static Element[][] addMonster(RoomTypes type, Element[][] overlay, RoomTemplate tempTemplate) throws SlickException {
		
		Map<Levels, Float> monsterProbabilities = tempTemplate.getMonster();
		int monsterCount = tempTemplate.getMonsterCount();
		
		System.out.println("monsterCount: " + monsterCount);
				
		/* place monsters on random, free fields in the room */
		for (int i = 0; i < monsterCount; i++) {
			
			/* first find a free field, return null if no free field is found after 15 rounds */
			Point randPoint = Chances.randomFreeField(overlay);
			
			if (randPoint != null) { //no free field was found 
				
				System.out.println("x: " + randPoint.x + ", y: " + randPoint.y);
				
				/* get a random Monster, according to the monster levels allowed in this Room's definition*/ 
				String monsterName = Chances.randomMonster(monsterProbabilities);
				
				if (monsterName != null) { //no monster shall be placed
					overlay[randPoint.x][randPoint.y] = MonsterFactory.createMonster(monsterName);
				}
			}
			else {
				System.out.println("null");
				break;
			}
		}
		
		return overlay;
	}

	/**
	 * Puts Items (Armament, Potion, Weapon) into a Room.<br>
	 * Which kind of Item and whether an Item is placed in the Room depends on
	 * the Room type and the associated config values.
	 * 
	 * @param type
	 * @param overlay
	 * @param resources
	 * @return an overlay with randomly chosen items
	 */
	private static Element[][] addItems(RoomTypes type, Element[][] overlay, ResourceManager resources) {

		return overlay;
	}

	/**
	 * Fills the Room's Ground with textures.
	 * 
	 * @param type
	 * @param background
	 * @param size
	 * @return a background filled with ground textures
	 * @throws SlickException 
	 */
	private static Element[][] fillGround(RoomTypes type, Element[][] background, Dimension size) throws SlickException {

		/* null-initialize background */
		for (int i = 0; i < size.width - 1; i++) {
			for (int j = 0; j < size.height - 1; j++) {
				background[i][j] = null;
			}
		}

		/* fill background according to room type */
		for (int i = 0; i <= size.width - 1; i++) {
			for (int j = 0; j <= size.height - 1; j++) {

				switch (type) {
				case DEADEND:
					background[i][j] = GroundFactory
							.createYellowGroundOne(i, j);
					break;
				case HALLWAY:
					background[i][j] = GroundFactory.createGreenGround(i, j);
					break;
				case TURN:
					background[i][j] = GroundFactory.createGreenGround(i, j);
					break;
				case TJUNCTION:
					background[i][j] = GroundFactory
							.createYellowGroundTwo(i, j);
					break;
				case JUNCTION:
					background[i][j] = GroundFactory
							.createYellowGroundOne(i, j);
					break;
				case TREASURECHAMBER:
					background[i][j] = GroundFactory.createBrownGround(i, j);
					break;
				}
			}
		}

		return background;
	}
}
