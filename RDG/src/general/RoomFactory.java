package general;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;

import org.newdawn.slick.SlickException;

import configLoader.RoomTemplate;
import elements.Element;
import elements.Equipment;
import elements.Item;
import elements.Room;
import elements.Weapon;
import gameEssentials.Game;
import general.Enums.Armor;
import general.Enums.ItemClasses;
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
		
		/* fill the room */
		overlay = addMonster(type, overlay, tempTemplate);
		overlay = addItems(type, overlay, tempTemplate);
		background = fillGround(type, background, size);

		//for testing only
		/*overlay[0][0] = ItemFactory.createWeapon("Longsword", 1);
		overlay[0][1] = ItemFactory.createWeapon("Shield", 1);
		overlay[0][0] = ItemFactory.createPotion("Poison", 1);
		overlay[0][1] = ItemFactory.createPotion("Slowness", 1);*/
		
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
						
		/* place monsters on random, free fields in the room */
		for (int i = 0; i < monsterCount; i++) {
			
			/* first find a free field, return null if no free field is found after 15 rounds */
			Point randPoint = Chances.randomFreeField(overlay);
			
			if (randPoint != null) { //no free field was found 
				
				/* get a random Monster, according to the monster levels allowed in this Room's definition*/ 
				String monsterName = Chances.randomMonster(monsterProbabilities);
								
				if (monsterName != null) { //no monster shall be placed
					overlay[randPoint.x][randPoint.y] = MonsterFactory.createMonster(monsterName);
				}
			}
			else {
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
	 * @throws SlickException 
	 */
	private static Element[][] addItems(RoomTypes type, Element[][] overlay, RoomTemplate tempTemplate) throws SlickException {

		Map<ItemClasses, Float> itemProbabilities = tempTemplate.getFind_probabilities();
		int itemCount = tempTemplate.getItemCount();
		
		/* place items on random, free fields in the room */
		for (int i = 0; i < itemCount; i++) {
			
			/* first find a free field, return null if no free field is found after 15 rounds */
			Point randPoint = Chances.randomFreeField(overlay);
			
			if (randPoint != null) { //no free field was found 
				
				/* get a random Item, according to the item levels allowed in this Room's definition*/ 
				Item itemName = Chances.randomItem(itemProbabilities);
								
				if (itemName != null) { //no item shall be placed
					//use itemMultiplier
					overlay[randPoint.x][randPoint.y] = ItemFactory.createItem(itemName, tempTemplate.getItemMultiplier());
				}
			}
			else {
				break;
			}
		}
		
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
