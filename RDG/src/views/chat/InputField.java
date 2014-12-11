package views.chat;

import org.newdawn.slick.Font;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

import views.Chat;

/**An InputField is used in the Chat to enter text messages.
 *
 * @see TextField
 * @see Chat
 */
public class InputField extends TextField{
	
	private int maxLength;
	private boolean hasFocus;

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
	
	/**
	 * @return maximum Length of Input Field
	 */
	public int getMaxLength() {
		return this.maxLength;
	}
	
	@Override 
	public void setMaxLength(int length) {
		super.setMaxLength(length);
		this.maxLength = length;
	}
	
	@Override
	public void setFocus(boolean focus) {
		super.setFocus(focus);
		this.hasFocus = focus;
	}
	
	/**
	 * @return true if InputField has focus
	 */
	public boolean isFocused() {
		return this.hasFocus;
	}
}
