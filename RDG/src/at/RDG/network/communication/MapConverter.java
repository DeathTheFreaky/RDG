package at.RDG.network.communication;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import elements.Element;
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
	 */
	public static NetworkMessage toNetworkMessage(Element[][] _overlay) {
		
		Element[][] overlay = new Element[_overlay.length][_overlay[0].length];
				
		for (int i = 0; i < _overlay.length; i++) {
			for (int j = 0; j < _overlay[0].length; j++) {
				if (_overlay[i][j] != null) {
					/* copy the element to be able to set its image to null */
					overlay[i][j] = new Element(_overlay[i][j]);
					overlay[i][j].setImage(null);
				}
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
		ResourceManager res = new ResourceManager().getInstance();
		
		for (int i = 0; i < overlay.length; i++) {
			for (int j = 0; j < overlay[0].length; j++) {
				if (overlay[i][j] != null) {
					
					/* reset image retrieved from resourceManager or GroundFactory (random tiles) */
					if (!res.IMAGES.containsKey(overlay[i][j].NAME)) {
						Image myImage = GroundFactory.getTileByName(overlay[i][j].NAME, i, j, ImageSize.d32x32);
						if (myImage != null) {
							overlay[i][j].setImage(myImage);
						} 								
					} else {
						overlay[i][j].setImage(res.IMAGES.get(overlay[i][j].NAME));
					}
				}
			}
		}
		
		return overlay;
	}
}
