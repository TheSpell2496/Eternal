/**
 * @author Denny Cyr
 */
import java.util.ArrayList;

public class LootTile extends Tile {

	//VARIABLES
	ArrayList<DropRate> dropRates = new ArrayList<DropRate>();

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
     */
    public LootTile(int tileID, String tileName, String tileDesc, boolean moveThrough, char symbol,
    	int strength, String type, int itemForm, int remains, ArrayList<DropRate> dropRates) {
    	super(tileID, tileName, tileDesc, moveThrough, symbol, strength, type, itemForm, remains);
    	for (DropRate dropRate : dropRates) {
    		this.dropRates.add(dropRate);
    	}
		double randomNumber;
		boolean itemDropped;
		for (DropRate dropRate : dropRates) {
			int randomQuantity = 0;
			randomNumber = Math.random() * 100;
			if (dropRate.getDropChance() > randomNumber) {
				int[] quantity = dropRate.getQuantity();
				randomQuantity = (int)(Math.random() * (quantity[1] - quantity[0] + 1)) + quantity[0];
			}
			for (int i = 0; i < randomQuantity; i++) {
				drops.add(itemReader.readItem(dropRate.getItemID()));
			}
		}
    }

    /**
     * getter for the drop rates
     *
     * @return all of the drop rates for this loot tile
     */
    public ArrayList<DropRate> getDropRates() {
    	ArrayList<DropRate> ratesClone = new ArrayList<DropRate>(dropRates.size());
   		for (DropRate dropRate : dropRates) {
    		ratesClone.add(dropRate);
    	}
    	return ratesClone;
    }
}