package worthy;

import org.newdawn.slick.Font;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class InputField extends TextField{
	
	private int maxLength;
	private boolean hasFocus;

	public InputField(GUIContext container, Font font, int x, int y, int width,
			int height) {
		super(container, font, x, y, width, height);
	}
	
	
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
	
	public boolean isFocused() {
		return this.hasFocus;
	}
	
}
