package elements;

import java.util.LinkedList;
import java.util.List;

import general.Enums.CreatureType;

import org.newdawn.slick.Image;

/**A creature can be either a player or a monster.<br>
 * Both of them have a name, a position, a visibility and an image, so Creature extends Element.<br>
 * All methods that both monsters and players need during a fight shall be implemented in this class.<br>
 * 
 * @see Element
 */
public class Creature extends Element {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4303972964803693361L;

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
		
		this.activePotions = new LinkedList<Potion>();
	}
	
	/**Copy constructor for Creature.
	 * @param creature
	 * @param element
	 */
	public Creature(Creature creature, Element element) {
		
		super(element);
		
		this.type = creature.type;
		this.hp = creature.hp;
		this.strength = creature.strength;
		this.speed = creature.speed;
		this.accuracy = creature.accuracy;
		this.orHp = creature.orHp;
		this.orStrength = creature.orStrength;
		this.orAccuracy = creature.orAccuracy;
		this.orSpeed = creature.orSpeed;
		
		this.activePotions = new LinkedList<Potion>();
	}

	/**
	 * @return the Creature's temporary (one fight) Health Points
	 */
	public float getHp() {
		return this.hp;
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
		return this.strength;
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
		return this.speed;
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
		return this.accuracy;
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
		return this.orHp;
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
		return this.orStrength;
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
		return this.orSpeed;
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
		return this.orAccuracy;
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
	
	/**Adds a potion to the list of active potions.
	 * @param potion
	 */
	public void addActivePotions(Potion potion) {
		activePotions.add(potion);
	}
	
	/**Removes a potion from the list of active potions.
	 * @param potion
	 */
	public void removeActivePotions(Potion potion) {
		activePotions.remove(potion);
	}
	
	/**Returns a list of active potions for this creature.
	 * @return
	 */
	public List<Potion> getActivePotions() {
		return activePotions;
	}

	/**Removes all potions from the creature's list of active potions.
	 * 
	 */
	public void emptyActivePotions() {
		activePotions.clear();
	}
}
