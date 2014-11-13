package worthy;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class ResourceManager {

	/* only one instance of ResourceManager is allowed */
	private static ResourceManager INSTANCE = null;

	public Image PLAYER;
	public SpriteSheet TILES; //more efficient picture loading 
	/*public SpriteSheet TILES32;
	public SpriteSheet TILES64; */
	
	/* we need a way to assign coordinates to spritesheet pictures 
	 * count number of small images in configloader and build sqrt(number) 32x32 spritesheet
	 * count number of big images in configloader and build sqrt(number) 64x64 spritesheet
	 * 
	 * create tileset from the given images at gamestart -> greater flexibility
	 * automatically assign coordinates of images in spritesheet;
	 * use spritesheet during the game for faster perfomance
	 * 
	 */

	/**Constructs a RessourceManager
	 * 
	 * @throws SlickException
	 */
	public ResourceManager() throws SlickException {

	}

	/**Loads all ressources from harddrive into memory.
	 * 
	 * @throws SlickException
	 */
	private void loadResources() throws SlickException {
		PLAYER = new Image("./pictures/soldier_32x32.png"); // lädt ein Bild
		TILES = new SpriteSheet("./pictures/tileset.png", 32, 32); // Bild->aufteilen
	}

	/**Returns the one and only instance of ResourceManager and triggers loading all Ressources.
	 * 
	 * @return the one and only instance of ResourceManager
	 * @throws SlickException
	 */
	public ResourceManager getInstance() throws SlickException {
		if (INSTANCE == null) {
			INSTANCE = new ResourceManager();
			INSTANCE.loadResources();
		}

		return INSTANCE;
	}
}
