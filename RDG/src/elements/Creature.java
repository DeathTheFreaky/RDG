package elements;

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
	private CreatureType type;

	/**Constructs a Creature (Player or Monster).<br>
	 * 
	 * @param creatureName
	 * @param image
	 * @param type
	 * @see Creature
	 */
	public Creature(String creatureName, Image image, CreatureType type) {
		
		super(creatureName, image);
		
		this.type = type;
	}
}
