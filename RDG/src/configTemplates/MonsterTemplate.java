package configTemplates;

import enums.Enums.Attributes;
import enums.Enums.Levels;

public class MonsterTemplate {

	private String name, imageBig, imageSmall;
	private Attributes killBonusType;
	private Levels level; 
	private float statsLowMultiplier, statsHighMultiplier, hp, strength, speed, accuracy, killBonusLow, killBonusHigh;
	
	public MonsterTemplate(String name, String imageBig, String imageSmall, Levels level, Attributes killBonusType, 
		float statsLowMultiplier, float statsHighMultiplier, float hp, float strength, float speed, float accuracy, float killBonusLow, float killBonusHigh) {
		
		this.name = name; 
		this.level = level; 
		this.killBonusType = killBonusType; 
		this.imageBig = imageBig; 
		this.imageSmall = imageSmall;
		this.statsLowMultiplier = statsLowMultiplier; 
		this.statsHighMultiplier = statsHighMultiplier; 
		this.hp = hp; 
		this.strength = strength; 
		this.speed = speed; 
		this.accuracy = accuracy; 
		this.killBonusLow = killBonusLow; 
		this.killBonusHigh = killBonusHigh;
	}

	public String getName() {
		return name;
	}

	public Levels getLevel() {
		return level;
	}

	public Attributes getKill_bonus_type() {
		return killBonusType;
	}

	public String getImage_big() {
		return imageBig;
	}

	public String getImage_small() {
		return imageSmall;
	}

	public float getStats_low_multiplier() {
		return statsLowMultiplier;
	}

	public float getStats_high_multiplier() {
		return statsHighMultiplier;
	}

	public float getHp() {
		return hp;
	}

	public float getStrength() {
		return strength;
	}

	public float getSpeed() {
		return speed;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public float getKill_bonus_low() {
		return killBonusLow;
	}

	public float getKill_bonus_high() {
		return killBonusHigh;
	}
}
