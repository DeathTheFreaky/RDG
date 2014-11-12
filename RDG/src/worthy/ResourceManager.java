package worthy;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class ResourceManager {

	private static ResourceManager INSTANCE = null;

	public Image PLAYER;
	public SpriteSheet TILES;

	/*
	 * public enum Direction { NORTH, SOUTH, EAST, WEST }
	 */

	public ResourceManager() throws SlickException {

	}

	private void loadResources() throws SlickException {
		PLAYER = new Image("./pictures/soldier_32x32.png"); // lädt ein Bild
		TILES = new SpriteSheet("./pictures/tileset.png", 32, 32); // Bild->aufteilen
	}

	public ResourceManager getInstance() throws SlickException {
		if (INSTANCE == null) {
			INSTANCE = new ResourceManager();
			INSTANCE.loadResources();
		}

		return INSTANCE;
	}
}
