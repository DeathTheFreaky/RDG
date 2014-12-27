package fighting;

/**Used as return type for fight formula calculations to include min, avg and max values for both player and enemy.<br>
 * Garbage collection will delete instances that are not needed any longer.
 * @author Flo
 *
 */
public class AttDefMinAvgMax {
	
	/* for easier access */
	public MinAvgMax attacker;
	public MinAvgMax defender;
	
	/**Set min, avg and max values for both attacker and defender.
	 * @param attacker
	 * @param defender
	 */
	public AttDefMinAvgMax(MinAvgMax attacker, MinAvgMax defender) {
		this.attacker = attacker;
		this.defender = defender;
	}
}
