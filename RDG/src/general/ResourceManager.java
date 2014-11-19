package general;
import gameEssentials.Game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.xml.sax.SAXException;

import configLoader.ArmamentTemplate;
import configLoader.Configloader;
import configLoader.MonsterTemplate;
import configLoader.PotionTemplate;
import configLoader.WeaponTemplate;

/**ResourceManager loads all Resources from harddrive into memory.<br>
 * setConfigloader() must be called by Game Class before loadResources() can be called.
 *
 */
public class ResourceManager {

	/* only one instance of ResourceManager is allowed */
	private static ResourceManager INSTANCE = null;

	/* used for more efficient picture loading */
	public SpriteSheet TILES; 
	
	/* Hashmap for all Images */
	public Map<String, Image> IMAGES;
	
	/* Configloader which holds image paths */
	private Configloader configloader = null;

	/**Constructs a RessourceManager.<br>
	 * 
	 * @throws SlickException
	 * @see ResourceManager
	 */
	public ResourceManager() throws SlickException {
		IMAGES = new HashMap<String, Image>();
	}

	/**Loads all ressources from harddrive into memory.
	 * 
	 * @throws SlickException
	 */
	private void loadResources() throws SlickException {
		
		/* load config from XML files */
		try {
			configloader = new Configloader().getInstance();
		} catch (IllegalArgumentException | ParserConfigurationException
				| SAXException | IOException e) {
			e.printStackTrace();
		}
		
		if (configloader == null) {
			throw new NullPointerException("Configloader must be set!");
		}
		
		/* load images */
		loadImages();
	
	}
	
	/**Load Images from harddrive into memory
	 * @throws SlickException 
	 */
	private void loadImages() throws SlickException {
		
		TILES = new SpriteSheet(Game.IMAGEPATH + "/rooms/tileset.png", 32, 32);
		
		IMAGES.put("Player1", new Image(Game.IMAGEPATH + "soldier_32x32.png"));
		IMAGES.put("Player2", new Image(Game.IMAGEPATH + "soldier_32x32.png"));
		IMAGES.put("Armor_Background", new Image(Game.IMAGEPATH + "warrior_160x160.png"));
		IMAGES.put("Helmet", new Image(Game.IMAGEPATH + "Head.png"));
		IMAGES.put("Arms", new Image(Game.IMAGEPATH + "Arm.png"));
		IMAGES.put("Cuirass", new Image(Game.IMAGEPATH + "Chest.png"));
		IMAGES.put("Legs", new Image(Game.IMAGEPATH + "Legs.png"));
		IMAGES.put("Shoes", new Image(Game.IMAGEPATH + "Feet.png"));
		IMAGES.put("M_Weapon", new Image(Game.IMAGEPATH + "Weapon.png"));
		IMAGES.put("S_Weapon", new Image(Game.IMAGEPATH + "Weapon2.png"));
		
		/* load images from paths stored in config loader */
	    for (Entry<String, ArmamentTemplate> entry : configloader.getArmamentTemplates().entrySet()) {
	        IMAGES.put(entry.getKey(), new Image (Game.IMAGEPATH + entry.getValue().getImage()));
	    }
	    for (Entry<String, PotionTemplate> entry : configloader.getPotionTemplates().entrySet()) {
	        IMAGES.put(entry.getKey(), new Image (Game.IMAGEPATH + entry.getValue().getImage()));
	    }
	    for (Entry<String, WeaponTemplate> entry : configloader.getWeaponTemplates().entrySet()) {
	        IMAGES.put(entry.getKey(), new Image (Game.IMAGEPATH + entry.getValue().getImage()));
	    }
	    for (Entry<String, MonsterTemplate> entry : configloader.getMonsterTemplates().entrySet()) {
	        IMAGES.put(entry.getKey(), new Image (Game.IMAGEPATH + entry.getValue().getImage()));
	    }
	    	    
	    /* Print which images are stored now */
	    for(String s : IMAGES.keySet()) {
	    	System.out.println("Key: " + s + ", Value: " + IMAGES.get(s));
	    }
	}

	/**Returns the one and only instance of ResourceManager and triggers loading all Ressources.<br>
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
