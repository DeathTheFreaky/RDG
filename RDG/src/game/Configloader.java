package game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import config_loaders.Armaments_Loader;
import config_loaders.Attacks_Loader;
import config_loaders.Monsters_Loader;
import config_loaders.Potions_Loader;
import config_loaders.Rooms_Loader;
import config_loaders.Weapons_Loader;
import config_templates.Armament_Template;
import config_templates.Attack_Template;
import config_templates.Monster_Template;
import config_templates.Potion_Template;
import config_templates.Room_Template;
import config_templates.Weapon_Template;

public class Configloader {
	
	private String configpath;
	private Map<String, Armament_Template> armament_templates = new HashMap<String, Armament_Template>();
	private Map<String, Attack_Template> attack_templates = new HashMap<String, Attack_Template>();
	private Map<String, Monster_Template> monster_templates = new HashMap<String, Monster_Template>();
	private Map<String, Potion_Template> potion_templates = new HashMap<String, Potion_Template>();
	private Map<String, Room_Template> room_templates = new HashMap<String, Room_Template>();
	private Map<String, Weapon_Template> weapon_templates = new HashMap<String, Weapon_Template>();
	
	public Configloader(String configpath){
		this.configpath = configpath;
	}

	/**
	 * Run all subloaders for all xml config Files
	 */
	public void run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		attack_templates = new Attacks_Loader(configpath).run();
		armament_templates = new Armaments_Loader(configpath).run();
		monster_templates = new Monsters_Loader(configpath).run();
		potion_templates = new Potions_Loader(configpath).run();
		room_templates = new Rooms_Loader(configpath).run();
		weapon_templates = new Weapons_Loader(configpath).run();
	}

	public Map<String, Monster_Template> getMonster_templates() {
		return monster_templates;
	}

	public Map<String, Potion_Template> getPotion_templates() {
		return potion_templates;
	}

	public Map<String, Room_Template> getRoom_templates() {
		return room_templates;
	}

	public Map<String, Weapon_Template> getWeapon_templates() {
		return weapon_templates;
	}

	public Map<String, Attack_Template> getAttack_templates() {
		return attack_templates;
	}
	
	public Map<String, Armament_Template> getArmament_templates() {
		return armament_templates;
	}
}
