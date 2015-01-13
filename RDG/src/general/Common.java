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
}
