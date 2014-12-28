package fighting;

/**Used as return type for fight formula calculations to include min, avg and max values.<br>
 * Garbage collection will delete instances that are not needed any longer.
 * @author Flo
 *
 */
public class MinAvgMax {
	
	/* for easier access */
	public float min;
	public float avg;
	public float max;
	
	/**Set min, avg and max based on passed parameters min and max which are applied on base.
	 * @param min
	 * @param max
	 * @param base
	 */
	public MinAvgMax(float min, float max, float base) {
		this.min = min * base;
		this.max = max * base;
		this.avg = this.min + (this.max - this.min) / 2;
	}
	
	/**Set min, avg, max based on passed parameters min and max.
	 * @param min
	 * @param max
	 */
	public MinAvgMax(float min, float max) {
		this.min = min;
		this.max = max;
		this.avg = min + (max - min) / 2;
	}
}
