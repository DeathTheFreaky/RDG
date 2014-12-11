package general;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import elements.Element;

/**The GroundFactory places ground textures randomly on the map tiles.<br><br>
 */
public class GroundFactory {

	/**Creates random grey grounds with images taken from tiles Spritesheet.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 * @throws SlickException 
	 */
	public static Element createGreyGround(int positionX, int positionY) throws SlickException {
		
		ResourceManager resources = new ResourceManager().getInstance();
		SpriteSheet tiles = resources.TILES;

		double random = Math.random();

		if (random > 0.875) {
			return new Element("GreyGround", tiles.getSubImage(1, 0),
					positionX, positionY);
		} else if (random > 0.75) {
			return new Element("GreyGround", tiles.getSubImage(2, 0),
					positionX, positionY);
		} else if (random > 0.625) {
			return new Element("GreyGround", tiles.getSubImage(1, 1),
					positionX, positionY);
		} else if (random > 0.5) {
			return new Element("GreyGround", tiles.getSubImage(2, 1),
					positionX, positionY);
		} else if (random > 0.375) {
			return new Element("GreyGround", tiles.getSubImage(1, 2),
					positionX, positionY);
		} else if (random > 0.25) {
			return new Element("GreyGround", tiles.getSubImage(2, 2),
					positionX, positionY);
		} else if (random > 0.125) {
			return new Element("GreyGround", tiles.getSubImage(1, 3),
					positionX, positionY);
		} else {
			return new Element("GreyGround", tiles.getSubImage(2, 3),
					positionX, positionY);
		}
	}

	/**Creates random dark grey grounds with images taken from tiles Spritesheet.<br>
	 * Used for Walls.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 * @throws SlickException 
	 */
	public static Element createDarkGreyGround(int positionX, int positionY) throws SlickException {

		ResourceManager resources = new ResourceManager().getInstance();
		SpriteSheet tiles = resources.TILES;

		double random = Math.random();

		if (random > 0.875) {
			return new Element("DarkGreyGround", tiles.getSubImage(3, 3),
					positionX, positionY);
		} else if (random > 0.75) {
			return new Element("DarkGreyGround", tiles.getSubImage(4, 3),
					positionX, positionY);
		} else if (random > 0.625) {
			return new Element("DarkGreyGround", tiles.getSubImage(3, 4),
					positionX, positionY);
		} else if (random > 0.5) {
			return new Element("DarkGreyGround", tiles.getSubImage(4, 4),
					positionX, positionY);
		} else if (random > 0.375) {
			return new Element("DarkGreyGround", tiles.getSubImage(3, 5),
					positionX, positionY);
		} else if (random > 0.25) {
			return new Element("DarkGreyGround", tiles.getSubImage(4, 5),
					positionX, positionY);
		} else if (random > 0.125) {
			return new Element("DarkGreyGround", tiles.getSubImage(3, 6),
					positionX, positionY);
		} else {
			return new Element("DarkGreyGround", tiles.getSubImage(4, 6),
					positionX, positionY);
		}
	}

	/**Creates random yellow grounds with images taken from tiles Spritesheet.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 * @throws SlickException 
	 */
	public static Element createYellowGroundOne(int positionX, int positionY) throws SlickException {

		ResourceManager resources = new ResourceManager().getInstance();
		SpriteSheet tiles = resources.TILES;

		double random = Math.random();

		if (random > 0.8888) {
			return new Element("YellowGroundOne", tiles.getSubImage(3, 0),
					positionX, positionY);
		} else if (random > 0.7777) {
			return new Element("YellowGroundOne", tiles.getSubImage(4, 0),
					positionX, positionY);
		} else if (random > 0.6666) {
			return new Element("YellowGroundOne", tiles.getSubImage(5, 0),
					positionX, positionY);
		} else if (random > 0.5555) {
			return new Element("YellowGroundOne", tiles.getSubImage(3, 1),
					positionX, positionY);
		} else if (random > 0.4444) {
			return new Element("YellowGroundOne", tiles.getSubImage(4, 1),
					positionX, positionY);
		} else if (random > 0.3333) {
			return new Element("YellowGroundOne", tiles.getSubImage(5, 1),
					positionX, positionY);
		} else if (random > 0.2222) {
			return new Element("YellowGroundOne", tiles.getSubImage(3, 2),
					positionX, positionY);
		} else if (random > 0.1111) {
			return new Element("YellowGroundOne", tiles.getSubImage(4, 2),
					positionX, positionY);
		} else {
			return new Element("YellowGroundOne", tiles.getSubImage(5, 2),
					positionX, positionY);
		}
	}

	/**Creates random yellow2 grounds with images taken from tiles Spritesheet.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 * @throws SlickException 
	 */
	public static Element createYellowGroundTwo(int positionX, int positionY) throws SlickException {

		ResourceManager resources = new ResourceManager().getInstance();
		SpriteSheet tiles = resources.TILES;

		double random = Math.random();

		if (random > 0.8888) {
			return new Element("YellowGroundTwo", tiles.getSubImage(6, 0),
					positionX, positionY);
		} else if (random > 0.7777) {
			return new Element("YellowGroundTwo", tiles.getSubImage(7, 0),
					positionX, positionY);
		} else if (random > 0.6666) {
			return new Element("YellowGroundTwo", tiles.getSubImage(8, 0),
					positionX, positionY);
		} else if (random > 0.5555) {
			return new Element("YellowGroundTwo", tiles.getSubImage(6, 1),
					positionX, positionY);
		} else if (random > 0.4444) {
			return new Element("YellowGroundTwo", tiles.getSubImage(7, 1),
					positionX, positionY);
		} else if (random > 0.3333) {
			return new Element("YellowGroundTwo", tiles.getSubImage(8, 1),
					positionX, positionY);
		} else if (random > 0.2222) {
			return new Element("YellowGroundTwo", tiles.getSubImage(6, 2),
					positionX, positionY);
		} else if (random > 0.1111) {
			return new Element("YellowGroundTwo", tiles.getSubImage(7, 2),
					positionX, positionY);
		} else {
			return new Element("YellowGroundTwo", tiles.getSubImage(8, 2),
					positionX, positionY);
		}
	}
	
	/**Creates random green grounds with images taken from tiles Spritesheet.<br>
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 * @throws SlickException 
	 */
	public static Element createGreenGround(int positionX, int positionY) throws SlickException {

		ResourceManager resources = new ResourceManager().getInstance();
		SpriteSheet tiles = resources.TILES;

		double random = Math.random();

		if (random > 0.8888) {
			return new Element("BrownGround", tiles.getSubImage(5, 4),
					positionX, positionY);
		} else if (random > 0.7777) {
			return new Element("BrownGround", tiles.getSubImage(6, 4),
					positionX, positionY);
		} else if (random > 0.6666) {
			return new Element("BrownGround", tiles.getSubImage(7, 4),
					positionX, positionY);
		} else if (random > 0.5555) {
			return new Element("BrownGround", tiles.getSubImage(5, 5),
					positionX, positionY);
		} else if (random > 0.4444) {
			return new Element("BrownGround", tiles.getSubImage(6, 5),
					positionX, positionY);
		} else if (random > 0.3333) {
			return new Element("BrownGround", tiles.getSubImage(7, 5),
					positionX, positionY);
		} else if (random > 0.2222) {
			return new Element("BrownGround", tiles.getSubImage(5, 6),
					positionX, positionY);
		} else if (random > 0.1111) {
			return new Element("BrownGround", tiles.getSubImage(6, 6),
					positionX, positionY);
		} else {
			return new Element("BrownGround", tiles.getSubImage(7, 6),
					positionX, positionY);
		}
	}
	
	/**Creates random brown grounds with images taken from tiles Spritesheet.<br>
	 * Used for the ground textures of Treasure Chamber.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 * @throws SlickException 
	 */
	public static Element createBrownGround(int positionX, int positionY) throws SlickException {

		ResourceManager resources = new ResourceManager().getInstance();
		SpriteSheet tiles = resources.TILES;

		double random = Math.random();

		if (random > 0.8888) {
			return new Element("BrownGround", tiles.getSubImage(0, 4),
					positionX, positionY);
		} else if (random > 0.7777) {
			return new Element("BrownGround", tiles.getSubImage(1, 4),
					positionX, positionY);
		} else if (random > 0.6666) {
			return new Element("BrownGround", tiles.getSubImage(2, 4),
					positionX, positionY);
		} else if (random > 0.5555) {
			return new Element("BrownGround", tiles.getSubImage(0, 5),
					positionX, positionY);
		} else if (random > 0.4444) {
			return new Element("BrownGround", tiles.getSubImage(1, 5),
					positionX, positionY);
		} else if (random > 0.3333) {
			return new Element("BrownGround", tiles.getSubImage(2, 5),
					positionX, positionY);
		} else if (random > 0.2222) {
			return new Element("BrownGround", tiles.getSubImage(0, 6),
					positionX, positionY);
		} else if (random > 0.1111) {
			return new Element("BrownGround", tiles.getSubImage(1, 6),
					positionX, positionY);
		} else {
			return new Element("BrownGround", tiles.getSubImage(2, 6),
					positionX, positionY);
		}
	}
	
	/**Creates door grounds with images taken from tiles Spritesheet.<br>
	 * Used for the ground textures of doors to Treasure Chamber.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 * @throws SlickException 
	 */
	public static Element createDoorGround1(int positionX, int positionY) throws SlickException {

		ResourceManager resources = new ResourceManager().getInstance();
		SpriteSheet tiles = resources.TILES;

		return new Element("DoorGround", tiles.getSubImage(5, 3), positionX, positionY);
	}
	
	/**Creates door grounds with images taken from tiles Spritesheet.<br>
	 * Used for the ground textures of doors to Treasure Chamber.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 * @throws SlickException 
	 */
	public static Element createDoorGround2(int positionX, int positionY) throws SlickException {

		ResourceManager resources = new ResourceManager().getInstance();
		SpriteSheet tiles = resources.TILES;
		
		return new Element("DoorGround", tiles.getSubImage(5, 3).getFlippedCopy(false, true), positionX, positionY);
	}
}
