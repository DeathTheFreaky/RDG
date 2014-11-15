package elements;

import general.Enums.Attributes;
import general.Enums.CreatureType;
import general.Enums.Levels;

import org.newdawn.slick.Image;

public class Monster extends Creature {
	
	

	public Monster(String creatureName, Image image, Levels level, Attributes killBonusType, 
			float statsLowMultiplier, float statsHighMultiplier, float hp, float strength, float speed, float accuracy, float killBonusLow, float killBonusHigh) {
		super(creatureName, image, CreatureType.MONSTER);
		
	}

}
