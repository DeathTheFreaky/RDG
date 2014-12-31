package at.RDG.network.communication;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import elements.Armament;
import elements.Creature;
import elements.Element;
import elements.Equipment;
import elements.Monster;
import elements.Potion;
import elements.Weapon;
import gameEssentials.Game;
import gameEssentials.Map;
import general.GroundFactory;
import general.ResourceManager;
import general.Enums.ImageSize;

/**Converts and Deconverts Overlay into network message.
 * @author Flo
 *
 */
public class MapConverter {
	
	/**Converts Overlay into network message.
	 * @param map
	 * @return
	 * @throws SlickException 
	 */
	public static NetworkMessage toNetworkMessage(Element[][] _overlay) throws SlickException {
		
		Element[][] overlay = new Element[_overlay.length][_overlay[0].length];
				
		for (int i = 0; i < _overlay.length; i++) {
			for (int j = 0; j < _overlay[0].length; j++) {
				overlay[i][j] = nullImage(_overlay[i][j]);
			}
		}
				
		return new NetworkMessage(overlay);	
	}
	
	/**Deconverts NetworkMessage in overlay.
	 * @param networkMessage
	 * @return
	 * @throws SlickException 
	 */
	public static Element[][] toOverlay(NetworkMessage networkMessage) throws SlickException {
		
		Element[][] overlay = networkMessage.overlay;
		
		for (int i = 0; i < overlay.length; i++) {
			for (int j = 0; j < overlay[0].length; j++) {
				if (overlay[i][j] != null) {
					/* reset image retrieved from resourceManager or GroundFactory (random tiles) */
					overlay[i][j] = fillImage(overlay[i][j], i, j);
				}
			}
		}
		
		return overlay;
	}
	
	/**Set element's image to null, creating a new copy of the element to avoid rendering failures.
	 * @param element
	 * @return
	 */
	public static Element nullImage(Element element) {
		if (element != null) {
			
			Element retElement;
			
			/* copy the element to be able to set its image to null */
			if (element instanceof Monster) {
				retElement = new Monster((Monster) element, (Creature) element,  element);
			} else if (element instanceof Weapon) {
				retElement = new Weapon((Weapon) element, (Equipment) element, element);
			} else if (element instanceof Armament) {
				retElement = new Armament((Armament) element, (Equipment) element, element);
			} else if (element instanceof Potion) {
				retElement = new Potion((Potion) element, element);
			} else {
				retElement = new Element(element);
			}
			retElement.setImage(null);
			
			return retElement;
		}
		
		return null;
	}
	
	/**Fills an element's image, which has been set to null for network transfer.
	 * @param element
	 * @param i - x position on map (in tiles)
	 * @param j - y position on map (in tiles)
	 * @return
	 * @throws SlickException 
	 */
	public static Element fillImage(Element element, int i, int j) throws SlickException {
		
		if (element != null) {
			
			ResourceManager res = new ResourceManager().getInstance();
			
			/* reset image retrieved from resourceManager or GroundFactory (random tiles) */
			if (!res.IMAGES.containsKey(element.NAME)) {
				Image myImage = GroundFactory.getTileByName(element.NAME, i, j, ImageSize.d32x32);
				if (myImage != null) {
					element.setImage(myImage);
				} 								
			} else {
				element.setImage(res.IMAGES.get(element.NAME));
			}
			
			return element;
		}
		
		return null;
	}
}
