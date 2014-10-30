package game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import configLoaders.ArmamentsLoader;
import configLoaders.AttacksLoader;
import configLoaders.MonstersLoader;
import configLoaders.PotionsLoader;
import configLoaders.RoomsLoader;
import configLoaders.WeaponsLoader;
import configTemplates.ArmamentTemplate;
import configTemplates.AttackTemplate;
import configTemplates.MonsterTemplate;
import configTemplates.PotionTemplate;
import configTemplates.RoomTemplate;
import configTemplates.WeaponTemplate;

public class Configloader {
	
	private String configpath;
	private Map<String, ArmamentTemplate> ArmamentTemplates;
	private Map<String, AttackTemplate> attackTemplates;
	private Map<String, MonsterTemplate> monsterTemplates;
	private Map<String, PotionTemplate> potionTemplates;
	private Map<String, RoomTemplate> roomTemplates;
	private Map<String, WeaponTemplate> weaponTemplates;
	
	public Configloader(String configpath){
		this.configpath = configpath;
	}

	/**
	 * Run all subloaders for all xml config Files
	 */
	public void run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		attackTemplates = AttacksLoader.run(configpath);
		ArmamentTemplates = ArmamentsLoader.run(configpath);
		monsterTemplates = MonstersLoader.run(configpath);
		potionTemplates = PotionsLoader.run(configpath);
		roomTemplates = RoomsLoader.run(configpath);
		weaponTemplates = WeaponsLoader.run(configpath);
	}

	public Map<String, MonsterTemplate> getMonsterTemplates() {
		return monsterTemplates;
	}

	public Map<String, PotionTemplate> getPotionTemplates() {
		return potionTemplates;
	}

	public Map<String, RoomTemplate> getRoomTemplates() {
		return roomTemplates;
	}

	public Map<String, WeaponTemplate> getWeaponTemplates() {
		return weaponTemplates;
	}

	public Map<String, AttackTemplate> getAttackTemplates() {
		return attackTemplates;
	}
	
	public Map<String, ArmamentTemplate> getArmamentTemplates() {
		return ArmamentTemplates;
	}
}
