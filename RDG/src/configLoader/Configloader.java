package configLoader;

import general.Enums.Attacks;
import general.Enums.RoomTypes;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**Loads all necessary XML config entries from XML Files automatically generated with Excel.
 * 
 * @author Flo
 *
 */
public class Configloader {
	
	private String configpath; //path where all XML config files are stored
	private Map<String, ArmamentTemplate> ArmamentTemplates;
	private Map<Attacks, AttackTemplate> attackTemplates;
	private Map<String, MonsterTemplate> monsterTemplates;
	private Map<String, PotionTemplate> potionTemplates;
	private Map<RoomTypes, RoomTemplate> roomTemplates;
	private Map<String, WeaponTemplate> weaponTemplates;
	
	/**Constructs a Configloader passing the path where all config XML files are stored.
	 * 
	 * @param configpath
	 * @see Configloader
	 */
	public Configloader(String configpath){
		this.configpath = configpath;
	}

	/**
	 * Runs one subloader for each XML config file.
	 */
	public void run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		attackTemplates = AttacksLoader.run(configpath);
		ArmamentTemplates = ArmamentsLoader.run(configpath);
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
		return ArmamentTemplates;
	}
}
