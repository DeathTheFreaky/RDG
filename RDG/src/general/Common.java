package general;

import java.util.Locale;

public class Common {
	
	/**Used for rounding.
	 * @param value
	 * @param places
	 * @return rounded value for n places
	 */
	public static String round(float value, int places) {
				
		String retStr;
		
	    if (places < 0) throw new IllegalArgumentException();
	    
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    retStr = String.format(Locale.ENGLISH, "%." + places + "f", (float) tmp / factor);
	    return retStr;
	}

	/**Calculates y position for descriptions
	 * @param descriptionHeight 
	 * @param mousePositionY 
	 * @return
	 */
	public static int descriptionPositionsY(int mousePositionY, int descriptionHeight) {
				
		int yPos = mousePositionY - descriptionHeight;
		
		if (mousePositionY - descriptionHeight < 0) {
			yPos = 0;
		}
		
		return yPos;
	}
	

	/**Calculates x position for descriptions
	 * @param descriptionWidth
	 * @param mousePositionX
	 * @return
	 */
	public static int descriptionPositionsX(int mousePositionX, int descriptionWidth) {
				
		int xPos = mousePositionX - descriptionWidth;
		
		if (mousePositionX - descriptionWidth < 0) {
			xPos = 0;
		}
		
		return xPos;
	}
}
