package general;

import elements.Element;
import elements.Item;
import elements.Room;
import gameEssentials.Game;
import general.Enums.Attacks;
import general.Enums.ItemClasses;
import general.Enums.Levels;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.OverlayLayout;

import org.newdawn.slick.SlickException;

public class Chances {
	
	private static Random r = new Random();
	private static ResourceManager resources = null;
	
	/**
	 * Return a random float between two float barriers.
	 * 
	 * @param low
	 * @param high
	 * @return
	 */
	public static float randomFloat(float low, float high) {

		return r.nextFloat() * (high - low) + low;
	}

	/**
	 * Takes a Map of MonsterLevel - Float pairs describing the probability for each type of
	 * Monster.<br>
	 * Returns the name of a random Monster.
	 * 
	 * @param monsterProbabilities
	 * @return random Monster
	 * @throws SlickException
	 */
	public static String randomMonster(Map<Levels, Float> monsterProbabilities)
			throws SlickException {

		if (resources == null) {
			resources = new ResourceManager().getInstance();
		}

		/* used to sum up all single possibility values from configloader */
		float sum = 0;

		/* used to determine which level the determined monster shall be of */
		float randomFloat = 0;

		/* the determined, random monster level */
		Levels monsterLevel = null;

		/* calculate total some to determine random interval */
		for (Entry<Levels, Float> entry : monsterProbabilities.entrySet()) {
			sum += entry.getValue();
		}

		/*
		 * get a random value within the determined interval = sum of all
		 * probability values
		 */
		randomFloat = r.nextFloat() * (sum);

		/* set thresholds for level probabilities */
		Float noneThreshold = monsterProbabilities.get(Levels.NONE);
		Float easyThreshold = noneThreshold
				+ monsterProbabilities.get(Levels.EASY);
		Float normalThreshold = easyThreshold
				+ monsterProbabilities.get(Levels.NORMAL);
		Float hardThreshold = normalThreshold
				+ monsterProbabilities.get(Levels.HARD);
		
		/* determine the random monster level */
		if (randomFloat > 0 && randomFloat <= noneThreshold) {
			monsterLevel = Levels.NONE;
		} else if (randomFloat > noneThreshold && randomFloat <= easyThreshold) {
			monsterLevel = Levels.EASY;
		} else if (randomFloat > easyThreshold && randomFloat <= normalThreshold) {
			monsterLevel = Levels.NORMAL;
		} else if (randomFloat > normalThreshold && randomFloat <= hardThreshold) {
			monsterLevel = Levels.HARD;
		}

		/* no monster shall be placed in this room */
		if (monsterLevel == Levels.NONE) {
			return null;
		}

		/* get the appriopriate list of monsters within the determined level */
		List<String> monsterLeveledList = resources.MONSTERS_LEVELED
				.get(monsterLevel);

		/*
		 * return the name of a random monster within the list of leveled
		 * monsters
		 */
		String randomMonster = monsterLeveledList.get(r
				.nextInt(monsterLeveledList.size()));

		return randomMonster;
	}
	
	/**Takes a Map of ItemClass - Float pairs describing the probability for each type of
	 * Item.<br>
	 * Returns name and type of a random Item.
	 * 
	 * @param overlay
	 * @return a random Item
	 * @throws SlickException 
	 */
	public static Item randomItem(Map<ItemClasses, Float> itemClassProbabilities) throws SlickException {
		
		if (resources == null) {
			resources = new ResourceManager().getInstance();
		}
		/* used to sum up all single possibility values for all items from configloader */
		float sum = 0;

		/* used to determine which itemClass the determined item shall be of */
		float randomFloat = 0;

		/* the determined, random monster level */
		ItemClasses itemClass = null;

		/* calculate total some to determine random interval */
		for (Entry<ItemClasses, Float> entry : itemClassProbabilities.entrySet()) {
			sum += entry.getValue();
		}
		
		/*
		 * get a random value within the determined interval = sum of all
		 * probability values
		 */
		randomFloat = r.nextFloat() * (sum);
		
		/* set thresholds for itemClass probabilities */
		Float noneThreshold = itemClassProbabilities.get(ItemClasses.NONE);
		Float weakThreshold = noneThreshold
				+ itemClassProbabilities.get(ItemClasses.WEAK);
		Float mediumThreshold = weakThreshold
				+ itemClassProbabilities.get(ItemClasses.MEDIUM);
		Float strongThreshold = mediumThreshold
				+ itemClassProbabilities.get(ItemClasses.STRONG);

		/* determine the random itemClass */
		if (randomFloat > 0 && randomFloat <= noneThreshold) {
			itemClass = ItemClasses.NONE;
		} else if (randomFloat > noneThreshold && randomFloat <= weakThreshold) {
			itemClass = ItemClasses.WEAK;
		} else if (randomFloat > weakThreshold && randomFloat <= mediumThreshold) {
			itemClass = ItemClasses.MEDIUM;
		} else if (randomFloat > mediumThreshold && randomFloat <= strongThreshold) {
			itemClass = ItemClasses.STRONG;
		}
				
		/* no item shall be placed in this room */
		if (itemClass == ItemClasses.NONE) {
			return null;
		}
		
		/* get the appriopriate list of items within the determined level */
		List<Item> itemClassedList = resources.ITEMCLASSLIST
				.get(itemClass);
		
		/*
		 * return the name of a random item within the list of leveled
		 * items
		 */
		Item randomItem = itemClassedList.get(r
				.nextInt(itemClassedList.size()));
		
		return randomItem;
	}
	
	/**Finds a random free field in a room.<br>
	 * If no free field is found after 15 rounds, return null.
	 * @return a free field or null if none is found
	 */
	public static Point randomFreeField(Element[][] overlay) {
				
		for (int i = 0; i < Game.MAXTRIES; i++) {
			
			/* if ROOMWIDTH == 8: values will be between 0 and 7 */
			int xRand = r.nextInt(Game.ROOMWIDTH);
			int yRand = r.nextInt(Game.ROOMHEIGHT);
			
			if (overlay[xRand][yRand] == null) {
				return new Point(xRand, yRand);
			}
		}
		
		return null;
	}

	/**Returns a random monster attack.
	 * @return a random monster attack
	 */
	public static Attacks randomAttackType() {

		Attacks randAttackType = null;
		
		float randFloat = r.nextFloat();
		
		if (randFloat < 0.2) {
			randAttackType = Attacks.TORSO;
		} else if (randFloat >= 0.2 && randFloat < 0.4) {
			randAttackType = Attacks.HEAD;
		} else if (randFloat >= 0.4 && randFloat < 0.6) {
			randAttackType = Attacks.ARMS;
		} else if (randFloat >= 0.6 && randFloat < 0.8) {
			randAttackType = Attacks.LEGS;
		} else if (randFloat >= 0.8 && randFloat < 1) {
			randAttackType = Attacks.PARRY;
		}
		
		return randAttackType;
	}

	/**Returns Point for a random Room on the map, excluding the treasure Chamber.
	 * @return a random Room but not the treasure Chamber
	 */
	public static Point randomRoom() {
		
		int middleX = Game.ROOMSHOR/2;
		int middleY = Game.ROOMSVER/2;
		int randX = 0;
		int randY = 0;
		
		do {
			randX = r.nextInt(Game.ROOMSHOR);
			randY = r.nextInt(Game.ROOMSVER);
		} while (randX == middleX && randY == middleY);
		
		Point randRoom = new Point(randX, randY);
		
		return randRoom;
	}

	/**Returns a random empty tile in a room.
	 * @return
	 */
	public static Point randomTile(Room randRoom) {

		int randX = 0;
		int randY = 0;
		boolean found = false;
		int ctr = 0;
		
		if (randRoom == null) {
			return null;
		}
		
		do {
			randX = r.nextInt(Game.ROOMWIDTH);
			randY = r.nextInt(Game.ROOMHEIGHT);
			
			if (randRoom.overlay[randX][randY] == null) {
				found = true;
			}
			ctr++;
		} while (found == false && ctr <= 20);
		
		Point randTile = new Point(randX, randY);
		
		return randTile;
	}
}
