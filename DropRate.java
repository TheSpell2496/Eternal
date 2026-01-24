/**
 * @author Denny Cyr
 */

public class DropRate {

	//VARIABLES
	private int itemID;
	private double dropChance;
	private int[] quantity = new int[2];

	/**
	 * primary constructor
	 *
	 * @param itemID the item to be dropped
	 * @param dropChance the chance of anything being dropped
	 * @param quantity the range of how many items can be dropped at once
	 */
	public DropRate(int itemID, double dropChance, int[] quantity) {
		this.itemID = itemID;
		this.dropChance = dropChance;
		this.quantity[0] = quantity[0];
		this.quantity[1] = quantity[1];
	}

	/**
	 * getter for itemID
	 *
	 * @return itemID
	 */
	public int getItemID() {
		return itemID;
	}

	/**
	 * getter for dropChance
	 *
	 * @return dropChance
	 */
	public double getDropChance() {
		return dropChance;
	}

	/**
	 * getter for quantity
	 *
	 * @return quantity
	 */
	public int[] getQuantity() {
		int[] quantityClone = {quantity[0],quantity[1]};
		return quantityClone;
	}
	
}