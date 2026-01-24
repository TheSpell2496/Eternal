/**
 * @author Denny Cyr
 */
import java.util.ArrayList;

public class Tool extends Item { //NOT IMMUTABLE, DURABILITY

	//VARIABLES 
	private int strength, durability;
	private String breakableMaterial;
	private ArrayList<int[]> transitions = new ArrayList<int[]>(); //in this case, a transition is the conversion of one tile to another that this tool can cause
	public static final CSVItemReader itemReader = new CSVItemReader("ItemInfo.csv");

	/**
     * primary constructor
     * 
     * @param itemID the ID for the item, think like minecraft
     * @param name the name of the item
     * @param callNames the names that this item can be accessed by
     */
	public Tool(int itemID, String name, ArrayList<String> callNames, int strength,
		int durability, String breakableMaterial, ArrayList<int[]> transitions) {
		super(itemID, name, callNames);
		this.strength = strength;
		this.breakableMaterial = breakableMaterial;
		this.durability = durability;
		for (int[] transition : transitions) {
			this.transitions.add(transition.clone());
		}
	}

	/**
	 * getter for the transitions
	 *
	 * @return all the transitions (conversions from one tile to another) this tool can cause
	 */
	public ArrayList<int[]> getTransitions() {
		ArrayList<int[]> transitionsCopy = new ArrayList<int[]>();
		for (int[] transition : transitions) {
			transitionsCopy.add(transition.clone());
		}
		return transitionsCopy;
	}

	/**
	 * getter for durability
	 *
	 * @return the remaining durability of the tool
	 */
	public int getDurability() {
		return durability;
	}

	/**
	 * getter for the material this tool breaks
	 *
	 * @return the material this tool breaks
	 */
	public String getMaterial() {
		return breakableMaterial;
	}

	@Override
	public boolean useItem(World world, int xCoord, int yCoord) {
		Space currentSpace = world.getCurrentSpace();
		boolean success = false; 
		for (int[] transition : transitions) {
			if (transition[0] == currentSpace.getTile(xCoord, yCoord).getTileID()) {
				world.changeTile(xCoord, yCoord, transition[1]);
				return true;
			}
		}
		if (breakableMaterial.equals(currentSpace.getTile(xCoord, yCoord).getMaterial()) &&
			strength >= currentSpace.getTile(xCoord, yCoord).getStrength() && 
			currentSpace.getTile(xCoord, yCoord).getStrength() != -1) {
			success = true;
			if (currentSpace.getTile(xCoord, yCoord).getTileID()%10 == 1) {
				LootTile lootTile = (LootTile)currentSpace.getTile(xCoord, yCoord);
				ArrayList<Item> drops = lootTile.getDrops();
				for (Item drop : drops) {
					world.addToInventory(drop);
				}
			}
			durability--;
			if (durability == 0) {
				ArrayList<ArrayList<Item>> inventory = world.getInventory();
				for (int i = 0; i < inventory.size(); i++) {
					if (inventory.get(i).get(0) == this) {
						world.removeFromInventory(i);
					}
				}
			}
			world.changeTile(xCoord, yCoord, currentSpace.getTile(xCoord, yCoord).getRemains());
		}
		return success;
	}
}