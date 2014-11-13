package worthy;

import org.newdawn.slick.Image;

public class Potion extends Element {
	
	public enum Effect {
		INCREASE_HP, INCREASE_STRENGTH, INCREASE_DEFENSE
	}
	
	/* What kind of Potion (what is it used for) */
	private Effect effect;
	
	/* status change (how much a value in- or decreases */
	private int power;

	public Potion(String potionName, Image image, Effect effect, int value) {
		super(potionName, image);
		
		this.effect = effect;
		this.power = value;
	}
	
	
	
	public Effect getEffect() {
		return this.effect;
	}
	
	public int getPower() {
		return this.power;
	}

}
