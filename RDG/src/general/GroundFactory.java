package general;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import elements.Element;

/**The GroundFactory places ground textures randomly on the map tiles.<br><br>
 * 
 *  Watch out! If GroundFactory Method setUpFactory() isn't called first,
 *  all methods will return null!
 *  
 * @author Stefan
 */
public class GroundFactory {
	
	private static GroundFactory FACTORY = null;

	private static SpriteSheet tiles = null;
	private static ResourceManager resources = null;
	
	/**Constructs a GroundFactory.<br>
	 * 
	 * Watch out! If GroundFactory Method setUpFactory() isn't called first,
	 * all methods will return null!
	 * 
	 * @see GroundFactory
	 */
	public GroundFactory() {
		
	}

	/**Creates a GroundFactory and loads its static values only ONCE!!!<br>
	 * 
	 * Static variables only get initialized one time and all instances use the
	 * same variables --> less memory is needed!
	 * 
	 * @return initialized GroundFactory
	 * @throws SlickException
	 * @see GroundFactory
	 */
	public GroundFactory getInstance() throws SlickException {
		if (FACTORY == null) {
			FACTORY = new GroundFactory();
			FACTORY.loadFactory();
		}
		return FACTORY;
	}
	
	/**Creates an instance of ResourceManager to load all necessary resources and stores tile Spritesheet in tiles.
	 * 
	 * @throws SlickException
	 */
	public void loadFactory() throws SlickException {
		resources = new ResourceManager().getInstance();
		tiles = resources.TILES;
	}

	/**Creates random grey grounds with images taken from tiles Spritesheet.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 */
	public Element createGreyGround(int positionX, int positionY) {

		if (FACTORY == null) {
			System.out.println("Error! GroundFactory isn't initialized!");
			System.out.println("Call Method: >GroundFactory.setUpFactory()<");
			return null;
		}

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
	 */
	public Element createDarkGreyGround(int positionX, int positionY) {

		if (FACTORY == null) {
			System.out.println("Error! GroundFactory isn't initialized!");
			System.out.println("Call Method: >GroundFactory.setUpFactory()<");
			return null;
		}

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
	 */
	public Element createYellowGroundOne(int positionX, int positionY) {

		if (FACTORY == null) {
			System.out.println("Error! GroundFactory isn't initialized!");
			System.out.println("Call Method: >GroundFactory.setUpFactory()<");
			return null;
		}

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
	 */
	public Element createYellowGroundTwo(int positionX, int positionY) {

		if (FACTORY == null) {
			System.out.println("Error! GroundFactory isn't initialized!");
			System.out.println("Call Method: >GroundFactory.setUpFactory()<");
			return null;
		}

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
	
	/**Creates random brown grounds with images taken from tiles Spritesheet.<br>
	 * Used for the ground textures of Treasure Chamber.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 */
	public Element createBrownGround(int positionX, int positionY) {

		if (FACTORY == null) {
			System.out.println("Error! GroundFactory isn't initialized!");
			System.out.println("Call Method: >GroundFactory.setUpFactory()<");
			return null;
		}

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
	 */
	public Element createDoorGround1(int positionX, int positionY) {

		if (FACTORY == null) {
			System.out.println("Error! GroundFactory isn't initialized!");
			System.out.println("Call Method: >GroundFactory.setUpFactory()<");
			return null;
		}

		double random = Math.random();

		return new Element("DoorGround", tiles.getSubImage(5, 3), positionX, positionY);
	}
	
	/**Creates door grounds with images taken from tiles Spritesheet.<br>
	 * Used for the ground textures of doors to Treasure Chamber.
	 * 
	 * @param positionX
	 * @param positionY
	 * @return
	 */
	public Element createDoorGround2(int positionX, int positionY) {

		if (FACTORY == null) {
			System.out.println("Error! GroundFactory isn't initialized!");
			System.out.println("Call Method: >GroundFactory.setUpFactory()<");
			return null;
		}

		double random = Math.random();

		return new Element("DoorGround", tiles.getSubImage(5, 3).getFlippedCopy(false, true), positionX, positionY);
	}
}
