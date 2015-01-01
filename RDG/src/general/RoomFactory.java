package general;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.SlickException;

import configLoader.RoomTemplate;
import elements.Armament;
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
	 * @param map 
	 * @param itemsBalance 
	 * @param monsterBalance 
	 * @param balanceOffsets 
	 * 
	 * @return an Instance of Room
	 * @throws SlickException 
	 * @see RoomFactory
	 */
	public static Room createRoom(RoomTypes type, Map<Levels, HashMap<String, Integer>> monsterBalance, Map<ItemClasses, HashMap<Item, Integer>> itemsBalance, gameEssentials.Map map, Map<String, Integer> balanceOffsets, boolean fillContents) throws SlickException {
		
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
		if (fillContents) {
			overlay = addMonster(type, overlay, tempTemplate, monsterBalance, map, balanceOffsets);
			overlay = addItems(type, overlay, tempTemplate, itemsBalance, map, balanceOffsets);
		}
		background = fillGround(type, background, size);

		//for testing only
		/*overlay[0][0] = ItemFactory.createPotion("Weakness", 1);
		overlay[0][1] = ItemFactory.createWeapon("Longsword", 1);
		overlay[1][1] = ItemFactory.createWeapon("Sword", 1);
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
	 * @param map 
	 * @param monsterBalance 
	 * @param resources
	 * @param map 
	 * @param balanceOffsets 
	 * @return an overlay with or without a placed monster
	 * @throws SlickException 
	 */
	private static Element[][] addMonster(RoomTypes type, Element[][] overlay, RoomTemplate tempTemplate, Map<Levels, HashMap<String, Integer>> monsterBalance, gameEssentials.Map map, Map<String, Integer> balanceOffsets) throws SlickException {
		
		Map<Levels, Float> monsterProbabilities = tempTemplate.getMonster();
		int monsterCount = tempTemplate.getMonsterCount();
						
		/* place monsters on random, free fields in the room */
		for (int i = 0; i < monsterCount; i++) {
			
			/* first find a free field, return null if no free field is found after 15 rounds */
			Point randPoint = Chances.randomFreeField(overlay);
			
			if (randPoint != null) { //no free field was found 
				
				/* get a random Monster, according to the monster levels allowed in this Room's definition*/ 
				String monsterName = Chances.randomMonster(monsterProbabilities, monsterBalance, balanceOffsets);
								
				if (monsterName != null) { //no monster shall be placed
					overlay[randPoint.x][randPoint.y] = MonsterFactory.createMonster(monsterName);
					map.increaseBalance("monsterBalance", monsterName, null);
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
	 * @param map 
	 * @param itemsBalance 
	 * @param balanceOffsets 
	 * @param resources
	 * @return an overlay with randomly chosen items
	 * @throws SlickException 
	 */
	private static Element[][] addItems(RoomTypes type, Element[][] overlay, RoomTemplate tempTemplate, Map<ItemClasses, HashMap<Item, Integer>> itemsBalance, gameEssentials.Map map, Map<String, Integer> balanceOffsets) throws SlickException {

		Map<ItemClasses, Float> itemProbabilities = tempTemplate.getFind_probabilities();
		int itemCount = tempTemplate.getItemCount();
		
		/* place items on random, free fields in the room */
		for (int i = 0; i < itemCount; i++) {
			
			/* first find a free field, return null if no free field is found after 15 rounds */
			Point randPoint = Chances.randomFreeField(overlay);
			
			if (randPoint != null) { //no free field was found 
				
				/* get a random Item, according to the item levels allowed in this Room's definition*/ 
				Item item = Chances.randomItem(itemProbabilities, itemsBalance, balanceOffsets);
								
				if (item != null) { //no item shall be placed
					//use itemMultiplier
					overlay[randPoint.x][randPoint.y] = ItemFactory.createItem(item, tempTemplate.getItemMultiplier());
					map.increaseBalance("itemsBalance", null, item);
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
