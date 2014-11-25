package elements;

import java.util.List;

import general.Enums.CreatureType;

import org.newdawn.slick.Image;

/**A creature can be either a player or a monster.<br>
 * Both of them have a name, a position, a visibility and an image, so Creature extends Element.<br>
 * All methods that both monsters and players need during a fight shall be implemented in this class.<br>
 * 
 * @see Element
 */
public class Creature extends Element{
	
	/* Creature can either be Player or Monster */
	public final CreatureType type;
	
	/* Temporary Attributes of the Creature (for 1 Fight only) */
	private float hp;
	private float strength;
	private float speed;
	private float accuracy;
	
	/* Permanent Attributes of the Creature */
	private float orHp;
	private float orStrength;
	private float orSpeed;
	private float orAccuracy;
	
	private List<Potion> activePotions;

	/**Constructs a Creature (Player or Monster).<br>
	 * 
	 * @param creatureName
	 * @param image
	 * @param type
	 * @param hp
	 * @param strength
	 * @param speed
	 * @param accuracy
	 * @see Creature
	 */
	public Creature(String creatureName, Image image, CreatureType type, float hp, float strength, float speed, float accuracy) {
		
		super(creatureName, image);
		
		this.type = type;
		this.hp = hp;
		this.strength = strength;
		this.speed = speed;
		this.accuracy = accuracy;
		this.orHp = hp;
		this.orStrength = strength;
		this.orAccuracy = accuracy;
		this.orSpeed = speed;
	}

	/**
	 * @return the Creature's temporary (one fight) Health Points
	 */
	public float getHp() {
		return hp;
	}

	/**Sets the Creatures temporary (one fight) Health Points.
	 * 
	 * @param hp
	 */
	public void setHp(float hp) {
		this.hp = hp;
	}

	/**
	 * @return the Creature's temporary (one fight) Strength
	 */
	public float getStrength() {
		return strength;
	}

	/**Sets the Creature's temporary (one fight) Strength.
	 * 
	 * @param strength
	 */
	public void setStrength(float strength) {
		this.strength = strength;
	}

	/**
	 * @return the Creature's temporary (one fight) Speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**Sets the Creature's temporary (one fight) Speed.
	 * 
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * @return the Creature's temporary (one fight) Accuracy
	 */
	public float getAccuracy() {
		return accuracy;
	}

	/**Set the Creature's temporary (one fight) accuracy.
	 * 
	 * @param accuracy
	 */
	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * @return the Creature's permament Health Points
	 */
	public float getOrHp() {
		return orHp;
	}

	/**Sets the Creature's permament Health Points.
	 * 
	 * @param orHp
	 */
	public void setOrHp(float orHp) {
		this.orHp = orHp;
	}

	/**
	 * @return the Creature's permament Strength
	 */
	public float getOrStrength() {
		return orStrength;
	}

	/**Sets the Creature's permament Strength.
	 * 
	 * @param orStrength
	 */
	public void setOrStrength(float orStrength) {
		this.orStrength = orStrength;
	}

	/**
	 * @return the Creature's permament Speed
	 */
	public float getOrSpeed() {
		return orSpeed;
	}

	/**Set the Creature's permament Speed.
	 * 
	 * @param orSpeed
	 */
	public void setOrSpeed(float orSpeed) {
		this.orSpeed = orSpeed;
	}

	/**
	 * @return the Creature's permament Accuracy
	 */
	public float getOrAccuracy() {
		return orAccuracy;
	}

	/**Sets the Creature's permanent Accuracy.
	 * 
	 * @param orAccuracy
	 */
	public void setOrAccuracy(float orAccuracy) {
		this.orAccuracy = orAccuracy;
	}
	
	/**Resets Creature's stats to its original (before Fight) values.
	 * 
	 */
	public void resetOriginals() {
		accuracy = orAccuracy;
		speed = orSpeed;
		strength = orStrength;
		hp = orHp;
	}
	
	public void addActivePotions(Potion potion) {
		activePotions.add(potion);
	}
	
	public void removeActivePotions(Potion potion) {
		activePotions.remove(potion);
	}
	
	public List<Potion> getActivePotions() {
		return activePotions;
	}
}
