package general;

import java.util.Random;

public class Chances {
	
	public static float randomValue (float low, float high) {
		
		Random r = new Random();
		return r.nextFloat() * (high - low) + low;
	}
}
