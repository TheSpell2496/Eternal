/**
 * @author Denny Cyr
 */
import java.util.ArrayList;

public class CraftTile extends Tile {

	//VARIABLES
	private ArrayList<CraftingRecipe> craftingRecipes = new ArrayList<CraftingRecipe>();

	/**
     * primary constructor
     * 
     * @param tileID the id for the tile
     * @param tileName the name of the tile
     * @param tileDesc description of tile
     * @param moveThrough whether the tile can be moved onto or not
     * @param symbol the character that represents this tile on the map
     * @param strength the ability for this tile to withstand destruction, matched against an items strength
     * @param type the material that this tile is made out of, like trees are made of WOOD
     * @param itemForm the version of this tile that can be held as an item, = -1 if there is no item 
     * @param remains the tile left over after this tile is destroyed
     * @param dropRates the items that this tile can drop, including quantity and drop chance
     * @param craftingRecipes the recipes of the items you can craft with this tile
     */
	public CraftTile(int tileID, String tileName, String tileDesc, boolean moveThrough, char symbol,
    	int strength, String type, int itemForm, int remains, ArrayList<CraftingRecipe> craftingRecipes) {
		super(tileID, tileName, tileDesc, moveThrough, symbol, strength, type, itemForm, remains);
		for (CraftingRecipe recipe : craftingRecipes) {
			this.craftingRecipes.add(recipe);
		}
	}

	/**
	 * getter for craftingRecipes
	 *
	 * @return the crafting recipes belonging to this CraftTile
	 */
	public ArrayList<CraftingRecipe> getRecipes() {
		ArrayList<CraftingRecipe> recipesCopy = new ArrayList<CraftingRecipe>();
		for (CraftingRecipe recipe : craftingRecipes) {
			recipesCopy.add(recipe);
		}
		return recipesCopy;
	}
}