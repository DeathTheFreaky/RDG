package game;

import java.util.Map.Entry;

import config_templates.Armament_Template;
import config_templates.Attack_Template;
import config_templates.Monster_Template;
import config_templates.Potion_Template;
import config_templates.Room_Template;
import config_templates.Weapon_Template;

public class Config_Testprinter {
	
	private Configloader configloader;
	
	public Config_Testprinter(Configloader configloader) {
		this.configloader = configloader;
	}
	
	public void print() {
		
		//Armaments
		System.out.println("\n\n\nArmaments\n\n");
		
		for (Entry<String, Armament_Template> entry : configloader.getArmament_templates().entrySet()) {
			  
		  System.out.println(entry.getValue().getName());
		  System.out.println(entry.getValue().getItem_class());
		  System.out.println(entry.getValue().getClass_multiplier());
		  System.out.println(entry.getValue().getStats_low_multiplier());
		  System.out.println(entry.getValue().getStats_high_multiplier());
		  System.out.println(entry.getValue().getArmor());
		  System.out.println(entry.getValue().getSpeed());
		  System.out.println(entry.getValue().getType());
		  System.out.println(entry.getValue().getBonus());
		  System.out.println(entry.getValue().getImage_big());
		  System.out.println(entry.getValue().getImage_small());
		  System.out.println();
		}
		
		//Attacks
		System.out.println("\n\n\nAttacks\n\n");
		
		for (Entry<String, Attack_Template> entry : configloader.getAttack_templates().entrySet()) {
		  
		  System.out.println(entry.getValue().getName());
		  System.out.println(entry.getValue().getStats_low_multiplier());
		  System.out.println(entry.getValue().getStats_high_multiplier());
		  System.out.println(entry.getValue().getHp_damage());
		  System.out.println(entry.getValue().getHit_probability());
		  System.out.println(entry.getValue().getEffect());
		  System.out.println(entry.getValue().getX());
		  System.out.println();
		}
		
		//Monsters
		System.out.println("\n\n\nMonsters\n\n");
		
		for (Entry<String, Monster_Template> entry : configloader.getMonster_templates().entrySet()) {
			  
		  System.out.println(entry.getValue().getName());
		  System.out.println(entry.getValue().getLevel());
		  System.out.println(entry.getValue().getStats_low_multiplier());
		  System.out.println(entry.getValue().getStats_high_multiplier());
		  System.out.println(entry.getValue().getHp());
		  System.out.println(entry.getValue().getStrength());
		  System.out.println(entry.getValue().getSpeed());
		  System.out.println(entry.getValue().getAccuracy());
		  System.out.println(entry.getValue().getKill_bonus_type());
		  System.out.println(entry.getValue().getKill_bonus_low());
		  System.out.println(entry.getValue().getKill_bonus_high());
		  System.out.println(entry.getValue().getImage_big());
		  System.out.println(entry.getValue().getImage_small());
		  System.out.println();
		}
		
		//Potions
		System.out.println("\n\n\nPotions\n\n");
		
		for (Entry<String, Potion_Template> entry : configloader.getPotion_templates().entrySet()) {
			  
		  System.out.println(entry.getValue().getName());
		  System.out.println(entry.getValue().getItem_class());
		  System.out.println(entry.getValue().getClass_multiplier());
		  System.out.println(entry.getValue().getStats_low_multiplier());
		  System.out.println(entry.getValue().getStats_high_multiplier());
		  System.out.println(entry.getValue().getDescription());
		  System.out.println(entry.getValue().getTarget());
		  System.out.println(entry.getValue().getEffect());
		  System.out.println(entry.getValue().getMode());
		  System.out.println(entry.getValue().getX());
		  System.out.println(entry.getValue().getN());
		  System.out.println(entry.getValue().getImage_big());
		  System.out.println(entry.getValue().getImage_small());
		  System.out.println();
		}
		
		//Rooms
		System.out.println("\n\n\nRooms\n\n");
		
		for (Entry<String, Room_Template> entry : configloader.getRoom_templates().entrySet()) {
			  
		  System.out.println(entry.getValue().getName());
		  System.out.println(entry.getValue().getDescription());
		  System.out.println(entry.getValue().getDoor_positions()[0]);
		  System.out.println(entry.getValue().getDoor_positions()[1]);
		  System.out.println(entry.getValue().getDoor_positions()[2]);
		  System.out.println(entry.getValue().getDoor_positions()[3]);
		  System.out.println(entry.getValue().getMonster().get("no"));
		  System.out.println(entry.getValue().getMonster().get("easy"));
		  System.out.println(entry.getValue().getMonster().get("normal"));
		  System.out.println(entry.getValue().getMonster().get("hard"));
		  System.out.println(entry.getValue().getItem_multiplier());
		  System.out.println(entry.getValue().getFind_probabilities().get("none"));
		  System.out.println(entry.getValue().getFind_probabilities().get("weak"));
		  System.out.println(entry.getValue().getFind_probabilities().get("medium"));
		  System.out.println(entry.getValue().getFind_probabilities().get("strong"));
		  System.out.println(entry.getValue().getItem_count());
		  System.out.println(entry.getValue().getImage());
		  System.out.println();
		}
		
		//Weapons
		System.out.println("\n\n\nWeapons\n\n");
		
		for (Entry<String, Weapon_Template> entry : configloader.getWeapon_templates().entrySet()) {
			  
		  System.out.println(entry.getValue().getName());
		  System.out.println(entry.getValue().getItem_class());
		  System.out.println(entry.getValue().getClass_multiplier());
		  System.out.println(entry.getValue().getStats_low_multiplier());
		  System.out.println(entry.getValue().getStats_high_multiplier());
		  System.out.println(entry.getValue().getAttack());
		  System.out.println(entry.getValue().getSpeed());
		  System.out.println(entry.getValue().getAccuracy());
		  System.out.println(entry.getValue().getDefence());
		  System.out.println(entry.getValue().getSlots());
		  System.out.println(entry.getValue().getMax());
		  System.out.println(entry.getValue().getType());
		  System.out.println(entry.getValue().getImage_big());
		  System.out.println(entry.getValue().getImage_small());
		  System.out.println();
		}
	}
}
