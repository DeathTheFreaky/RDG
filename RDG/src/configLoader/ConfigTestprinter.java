package configLoader;

import general.Enums.Attacks;
import general.Enums.RoomTypes;

import java.util.Map.Entry;

/**Prints all parsed XML config entries of a Configloader.
 * 
 * @author Flo
 *
 */
public class ConfigTestprinter {
	
	private Configloader configloader;
	
	/**Constructs the ConfigTestprinter passing a Configloader which stores all parsed XML config entries.
	 * 
	 * @param configloader
	 * @see ConfigTestprinter
	 */
	public ConfigTestprinter(Configloader configloader) {
		this.configloader = configloader;
	}
	
	/**Prints all parsed XML config entries of a Configloader.
	 * 
	 */
	public void print() {
		
		//Armaments
		System.out.println("\n\n\nArmaments\n\n");
		
		for (Entry<String, ArmamentTemplate> entry : configloader.getArmamentTemplates().entrySet()) {
			  
		  System.out.println(entry.getValue().getName());
		  System.out.println(entry.getValue().getItem_class());
		  System.out.println(entry.getValue().getClass_multiplier());
		  System.out.println(entry.getValue().getStats_low_multiplier());
		  System.out.println(entry.getValue().getStats_high_multiplier());
		  System.out.println(entry.getValue().getArmor());
		  System.out.println(entry.getValue().getSpeed());
		  System.out.println(entry.getValue().getType());
		  System.out.println(entry.getValue().getBonus());
		  System.out.println(entry.getValue().getImage());
		  System.out.println();
		}
		
		//Attacks
		System.out.println("\n\n\nAttacks\n\n");
		
		for (Entry<Attacks, AttackTemplate> entry : configloader.getAttackTemplates().entrySet()) {
		  
		  System.out.println(entry.getValue().getType());
		  System.out.println(entry.getValue().getClass_multiplier());
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
		
		for (Entry<String, MonsterTemplate> entry : configloader.getMonsterTemplates().entrySet()) {
			  
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
		  System.out.println(entry.getValue().getImage());
		  System.out.println();
		}
		
		//Potions
		System.out.println("\n\n\nPotions\n\n");
		
		for (Entry<String, PotionTemplate> entry : configloader.getPotionTemplates().entrySet()) {
			  
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
		  System.out.println(entry.getValue().getImage());
		  System.out.println();
		}
		
		//Rooms
		System.out.println("\n\n\nRooms\n\n");
		
		for (Entry<RoomTypes, RoomTemplate> entry : configloader.getRoomTemplates().entrySet()) {
			  
		  System.out.println(entry.getValue().getType());
		  System.out.println(entry.getValue().getDescription());
		  System.out.println(entry.getValue().getDoor_positions()[0]);
		  System.out.println(entry.getValue().getDoor_positions()[1]);
		  System.out.println(entry.getValue().getDoor_positions()[2]);
		  System.out.println(entry.getValue().getDoor_positions()[3]);
		  System.out.println(entry.getValue().getMonster().get("no"));
		  System.out.println(entry.getValue().getMonster().get("easy"));
		  System.out.println(entry.getValue().getMonster().get("normal"));
		  System.out.println(entry.getValue().getMonster().get("hard"));
		  System.out.println(entry.getValue().getMonsterCount());
		  System.out.println(entry.getValue().getItemMultiplier());
		  System.out.println(entry.getValue().getFind_probabilities().get("none"));
		  System.out.println(entry.getValue().getFind_probabilities().get("weak"));
		  System.out.println(entry.getValue().getFind_probabilities().get("medium"));
		  System.out.println(entry.getValue().getFind_probabilities().get("strong"));
		  System.out.println(entry.getValue().getItemCount());
		  System.out.println(entry.getValue().getImage());
		  System.out.println();
		}
		
		//Weapons
		System.out.println("\n\n\nWeapons\n\n");
		
		for (Entry<String, WeaponTemplate> entry : configloader.getWeaponTemplates().entrySet()) {
			  
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
		  System.out.println(entry.getValue().getImage());
		  System.out.println();
		}
	}
}
