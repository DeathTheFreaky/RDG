package elements;

import general.Enums.Attributes;
import general.Enums.CreatureType;
import general.Enums.Levels;

import org.newdawn.slick.Image;

/**Monster can be killed by the player to improve the player's attributes.<br>
 * There can only be one monster per room.<br>
 * Monsters are split into three different difficulty levels.<br>
 * Hard Monsters can only be found in a treasure chamber.<br>
 * 
 * @see Creature
 */
public class Monster extends Creature {
	
	/* public final variables shall not be changed, private variables (attributes) will be changed */
	/* do not set variables to public -> breaks encapsulation ??? */
	/* make variables public for code reduction */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3597325742931574375L;

	/* Monster level is needed by Factory Class for Rooms to determine with which probability they can be found in which room */
	public final Levels level;
	
	/* killBonusType determines which player attributes will be increased if Player kills monster */
	public final Attributes killBonusType;
	
	/* kill bonus defines, how much of an attribute gain the player receives when killing the monster */
	public final float killBonus;
		
	/* classMultiplier, statsLowMulitplier and statsHighMultiplier, killBonusLow and killBonusHigh are needed in Factory Class; 
	 * statsLowMulitplier and statsHighMultiplier are used by randomClass to return value within this interval;
	 * killBonusLow and killBonusHigh are used by randomClass to return value within this interval;
	 * classMultiplier is used on all stats of this class for balancing */

	/**Constructs a monster.<br>
	 * Shall only be called from a Factory Class and the Monster's variables, except for its attributes, shall be final.<br>
	 * Health points, strength, speed and accuracy and killBonus will be set according to a random factor between statsLowMulitplier and statsHighMultiplier 
	 * returned by randomClass and a balancing classMultiplier.<br>
	 * 
	 * @param creatureName
	 * @param image
	 * @param level
	 * @param killBonusType
	 * @param hp
	 * @param strength
	 * @param speed
	 * @param accuracy
	 * @see Monster
	 */
	public Monster(String creatureName, Image image, Levels level, Attributes killBonusType, 
			float killBonus, float hp, float strength, float speed, float accuracy) {
		
		super(creatureName, image, CreatureType.MONSTER, hp, strength, speed, accuracy);
		
		this.level = level;
		this.killBonusType = killBonusType;
		this.killBonus = killBonus;
	}

	/**Copy constructor for Monster.
	 * @param monster
	 * @param creature
	 * @param element
	 */
	public Monster(Monster monster, Creature creature, Element element) {
		
		super(creature, element);
		
		this.level = monster.level;
		this.killBonusType = monster.killBonusType;
		this.killBonus = monster.killBonus;
	}
}
