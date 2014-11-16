package configLoader;

import gameEssentials.Game;
import general.ResourceManager;
import general.Enums.Attacks;
import general.Enums.RoomTypes;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.SlickException;
import org.xml.sax.SAXException;

/**Loads all necessary XML config entries from XML Files automatically generated with Excel.
 * 
 * @author Flo
 *
 */
public class Configloader {
	
	/* only one instance of Configloader is allowed */
	private static Configloader INSTANCE = null;
	
	private String configpath; //path where all XML config files are stored
	private Map<String, ArmamentTemplate> armamentTemplates;
	private Map<Attacks, AttackTemplate> attackTemplates;
	private Map<String, MonsterTemplate> monsterTemplates;
	private Map<String, PotionTemplate> potionTemplates;
	private Map<RoomTypes, RoomTemplate> roomTemplates;
	private Map<String, WeaponTemplate> weaponTemplates;
	
	/**Constructs a Configloader passing the path where all config XML files are stored.
	 * 
	 * @param configpath
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IllegalArgumentException 
	 * @see Configloader
	 */
	public Configloader() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException{
		this.configpath = Game.CONFIGPATH;
	}
	
	/**Returns the one and only instance of Configloader.
	 * 
	 * @return
	 * @throws SlickException
	 * @throws IllegalArgumentException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Configloader getInstance() throws SlickException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		if (INSTANCE == null) {
			if (Game.CONFIGPATH == null) throw new NullPointerException("Configpath must not be null");
			INSTANCE = new Configloader();
			INSTANCE.run();
		}
		else {
		}
		return INSTANCE;
	}

	/**
	 * Runs one subloader for each XML config file.
	 */
	public void run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		attackTemplates = AttacksLoader.run(configpath);
		armamentTemplates = ArmamentsLoader.run(configpath);
		monsterTemplates = MonstersLoader.run(configpath);
		potionTemplates = PotionsLoader.run(configpath);
		roomTemplates = RoomsLoader.run(configpath);
		weaponTemplates = WeaponsLoader.run(configpath);
	}

	/**
	 * @return Monster Template class
	 */
	public Map<String, MonsterTemplate> getMonsterTemplates() {
		return monsterTemplates;
	}

	/**
	 * @return Potion Template class
	 */
	public Map<String, PotionTemplate> getPotionTemplates() {
		return potionTemplates;
	}

	/**
	 * @return Room Template class
	 */
	public Map<RoomTypes, RoomTemplate> getRoomTemplates() {
		return roomTemplates;
	}

	/**
	 * @return Weapon Template class
	 */
	public Map<String, WeaponTemplate> getWeaponTemplates() {
		return weaponTemplates;
	}

	/**
	 * @return Attack Template class
	 */
	public Map<Attacks, AttackTemplate> getAttackTemplates() {
		return attackTemplates;
	}
	
	/**
	 * @return Armament Template class
	 */
	public Map<String, ArmamentTemplate> getArmamentTemplates() {
		return armamentTemplates;
	}
}
