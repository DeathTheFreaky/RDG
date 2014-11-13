package worthy;

import org.newdawn.slick.Font;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

/**An InputField is used in the Chat to enter text messages.
 *
 * @see TextField
 * @see Chat
 */
public class InputField extends TextField{

	/**Constructs an InputField.
	 * 
	 * @param container
	 * @param font
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @see InputField
	 */
	public InputField(GUIContext container, Font font, int x, int y, int width,
			int height) {
		super(container, font, x, y, width, height);
	}
}
