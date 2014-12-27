package at.RDG.network.communication;

import org.newdawn.slick.SlickException;

import elements.Element;
import gameEssentials.Map;
import general.ResourceManager;

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
		
		Element[][] overlay = _overlay.clone();
		
		System.out.println("overlay: " + _overlay);
		System.out.println("overlayCopy: " + overlay);
		
		for (int i = 0; i < overlay.length; i++) {
			for (int j = 0; j < overlay[0].length; j++) {
				if (overlay[i][j] != null) {
					/* setting null causes game to crash right - although it should not set overlay but the copy of overlay which is never to be drawn -> maybe whole image is null?!? */
					//overlay[i][j].setImage(null);
				}
			}
		}
		
		return new NetworkMessage(overlay);	
	}
	
	/**Deconverts NetworkMessage in overlay.
	 * @param networkMessage
	 * @return
	 */
	public static Element[][] toOverlay(NetworkMessage networkMessage) {
		
		Element[][] overlay = networkMessage.overlay;
		ResourceManager resources = null;
		try {
			resources = new ResourceManager().getInstance();
		} catch (SlickException e) {
			e.printStackTrace();
			return null;
		}
		
		for (int i = 0; i < overlay.length; i++) {
			for (int j = 0; j < overlay[0].length; j++) {
				if (overlay[i][j] != null) {
					overlay[i][j].setImage(resources.IMAGES.get(overlay[i][j].NAME));
				}
			}
		}
		
		return overlay;
	}
}
