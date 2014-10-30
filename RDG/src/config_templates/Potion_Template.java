package config_templates;

import enums.Enums.attributes;
import enums.Enums.item_classes;
import enums.Enums.modes;
import enums.Enums.targets;

public class Potion_Template {
	
	private String name, description, image_big, image_small;
	private item_classes item_class;
	private attributes effect;
	private targets target;
	private modes mode;
	private float class_multiplier, stats_low_multiplier, stats_high_multiplier, x; 
	private int n;
	
	public Potion_Template(String name, String description, String image_big, String image_small, item_classes item_class, targets target, modes mode, attributes effect,
			float class_multiplier, float stats_low_multiplier, float stats_high_multiplier, float x, int n){
		
		this.name = name;
		this.item_class = item_class;
		this.description = description;
		this.target = target;
		this.mode = mode;
		this.effect = effect;
		this.image_big = image_big;
		this.image_small = image_small;
		this.class_multiplier = class_multiplier;
		this.stats_low_multiplier = stats_low_multiplier;
		this.stats_high_multiplier = stats_high_multiplier;
		this.x = x;
		this.n = n;
	}

	public String getName() {
		return name;
	}

	public item_classes getItem_class() {
		return item_class;
	}

	public String getDescription() {
		return description;
	}

	public targets getTarget() {
		return target;
	}

	public modes getMode() {
		return mode;
	}

	public attributes getEffect() {
		return effect;
	}

	public String getImage_big() {
		return image_big;
	}

	public String getImage_small() {
		return image_small;
	}

	public float getClass_multiplier() {
		return class_multiplier;
	}

	public float getStats_low_multiplier() {
		return stats_low_multiplier;
	}

	public float getStats_high_multiplier() {
		return stats_high_multiplier;
	}

	public float getX() {
		return x;
	}

	public int getN() {
		return n;
	}
}
