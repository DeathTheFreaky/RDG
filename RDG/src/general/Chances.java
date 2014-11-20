package general;

import elements.Element;
import gameEssentials.Game;
import general.Enums.Levels;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.newdawn.slick.SlickException;

public class Chances {

	/**
	 * Return a random float between two float barriers.
	 * 
	 * @param low
	 * @param high
	 * @return
	 */
	public static float randomFloat(float low, float high) {

		Random r = new Random();
		return r.nextFloat() * (high - low) + low;
	}

	/**
	 * Takes an array of Integers describing the probability for each type of
	 * Monster.<br>
	 * Returns the name of a random Monster.
	 * 
	 * @param monsterProbabilities
	 * @return random Monster
	 * @throws SlickException
	 */
	public static String randomMonster(Map<Levels, Float> monsterProbabilities)
			throws SlickException {

		ResourceManager resources = new ResourceManager().getInstance();

		/* used to sum up all single possibility values form configloader */
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
		Random r = new Random();
		randomFloat = r.nextFloat() * (sum);

		/* set thresholds for level probabilities */
		Float noneThreshold = monsterProbabilities.get(Levels.NONE);
		Float easyThreshold = noneThreshold
				+ monsterProbabilities.get(Levels.EASY);
		Float normalThreshold = easyThreshold
				+ monsterProbabilities.get(Levels.NORMAL);
		Float hardThreshold = normalThreshold
				+ monsterProbabilities.get(Levels.HARD);

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
		if (monsterLevel == Levels.NONE)
			return null;

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
	
	/**Finds a random free field in a room.<br>
	 * If no free field is found after 15 rounds, return null.
	 * @return a free field or null if none is found
	 */
	public static Point randomFreeField(Element[][] overlay) {
		
		Random r = new Random();
		
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
}
