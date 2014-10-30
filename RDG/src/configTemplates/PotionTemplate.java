package configTemplates;

import enums.Enums.Attributes;
import enums.Enums.ItemClasses;
import enums.Enums.Modes;
import enums.Enums.Targets;

public class PotionTemplate {
	
	private String name, description, imageBig, imageSmall;
	private ItemClasses itemClass;
	private Attributes effect;
	private Targets target;
	private Modes mode;
	private float classMultiplier, statsLowMultiplier, statsHighMultiplier, x; 
	private int n;
	
	public PotionTemplate(String name, String description, String imageBig, String imageSmall, ItemClasses itemClass, Targets target, Modes mode, Attributes effect,
			float classMultiplier, float statsLowMultiplier, float statsHighMultiplier, float x, int n){
		
		this.name = name;
		this.itemClass = itemClass;
		this.description = description;
		this.target = target;
		this.mode = mode;
		this.effect = effect;
		this.imageBig = imageBig;
		this.imageSmall = imageSmall;
		this.classMultiplier = classMultiplier;
		this.statsLowMultiplier = statsLowMultiplier;
		this.statsHighMultiplier = statsHighMultiplier;
		this.x = x;
		this.n = n;
	}

	public String getName() {
		return name;
	}

	public ItemClasses getItem_class() {
		return itemClass;
	}

	public String getDescription() {
		return description;
	}

	public Targets getTarget() {
		return target;
	}

	public Modes getMode() {
		return mode;
	}

	public Attributes getEffect() {
		return effect;
	}

	public String getImage_big() {
		return imageBig;
	}

	public String getImage_small() {
		return imageSmall;
	}

	public float getClass_multiplier() {
		return classMultiplier;
	}

	public float getStats_low_multiplier() {
		return statsLowMultiplier;
	}

	public float getStats_high_multiplier() {
		return statsHighMultiplier;
	}

	public float getX() {
		return x;
	}

	public int getN() {
		return n;
	}
}
