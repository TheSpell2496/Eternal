/**
 * @author Denny Cyr
 */
import java.util.ArrayList;

public class CraftingRecipe {

	//VARIABLES
	private Item item;
	private ArrayList<int[]> recipe = new ArrayList<int[]>();
	private int quantity;

	/**
	 * primary constructor
	 *
	 * @param item the item made with this recipe
	 * @param recipe the items and amount of them needed to create the item
	 * @param quantity the amount of the item this recipe makes
	 */
	public CraftingRecipe(Item item, ArrayList<int[]> recipe, int quantity) {
		this.item = item;
    	for (int[] ingredient : recipe) {
    		int itemID = ingredient[0];
    		int amount = ingredient[1];
    		int[] ingredientCopy = {itemID, amount};
    		this.recipe.add(ingredientCopy);
    	}
    	this.quantity = quantity;
	}

	/**
	 * getter for the item
	 *
	 * @return the item that this recipe makes
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * getter for the recipe
	 *
	 * @return a deepclone of the recipe
	 */
	public ArrayList<int[]> getRecipe() {
		ArrayList<int[]> recipeClone = new ArrayList<int[]>();
		for (int[] ingredient : recipe) {
			int[] ingredientClone = {ingredient[0],ingredient[1]};
			recipeClone.add(ingredientClone);
		}
		return recipeClone;
	}

	/**
	 * getter for the quantity made
	 *
	 * @return the quantity made by this recipe
	 */
	public int getQuantity() {
		return quantity;
	}

}