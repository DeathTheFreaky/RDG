package worthy;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * @author Stefan
 * 
 *         Watch out! If GroundFactory Method >setUpFactory< isn't called first,
 *         all methods will return null!
 */

public class GroundFactory {
	
	protected static GroundFactory FACTORY = null;

	private static SpriteSheet tiles = null;
	private static ResourceManager resources = null;

	private static boolean initialized = false;

	
	
	public GroundFactory() {
	
	}

	
	
	public GroundFactory setUpFactory() throws SlickException {
		/*
		 * static variables only get initialized one time all instances use the
		 * same variables --> less memory is needed
		 */
		if (!initialized) {
			initialized = true;
			FACTORY = new GroundFactory();
			FACTORY.loadFactory();
		}
		
		return FACTORY;
	}
	
	public void loadFactory() throws SlickException {
		resources = new ResourceManager().getInstance();
		tiles = resources.TILES;
	}

	public Element createGreyGround(int positionX, int positionY) {

		if (!initialized) {
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

	public Element createDarkGreyGround(int positionX, int positionY) {

		if (!initialized) {
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

	public Element createYellowGroundOne(int positionX, int positionY) {

		if (!initialized) {
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

	public Element createYellowGroundTwo(int positionX, int positionY) {

		if (!initialized) {
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

	public Element createBorder(int positionX, int positionY) {
		return new Element("Border", tiles.getSubImage(0, 0), positionX,
				positionY);
	}
}
