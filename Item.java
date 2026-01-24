/**
 * @author Denny Cyr
 */
import java.util.ArrayList;

public class Item {

	//FINALS
	public static final CSVTileReader tileReader = new CSVTileReader("TileInfo.csv");

	//VARIABLES
	protected int itemID;
	private String name;
	private boolean stackable;
	private ArrayList<String> callNames = new ArrayList<String>();


	/**
     * primary constructor
     * 
     * @param itemID the ID for the item, think like minecraft
     * @param name the name of the item
     * @param callNames the names that this item can be accessed by
     */
	public Item(int itemID, String name, ArrayList<String> callNames) {
		this.itemID = itemID;
		this.name = name;
		for (String callName : callNames) {
			this.callNames.add(callName);
		}
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
	 * returns the name of an item
	 *
	 * @return name of an item
	 */
	public String getItemName() {
		return name;
	}

	/**
	 * returns the name of an item
	 *
	 * @return name of an item
	 */
	public ArrayList<String> getCallNames() {
		ArrayList<String> callNamesCopy = new ArrayList<String>();
		for (String callName : callNames) {
			callNamesCopy.add(callName);
		}
		return callNamesCopy;
	}

	/**
	 * defines how the player will use the item
	 *
	 * @param world the state of the world before using the item
	 * @param xCoord the x coordinant you're using this item on
	 * @param yCoord the y coordinant you're using this item on
	 * @return whether the use attempt was successful for not
	 */
	public boolean useItem(World world, int xCoord, int yCoord) {
		return false;
	}

	/**
	 * uses the item on the tile the player is on
	 *
	 * @param world the state of the world before using the item
	 * @return whether the use attempt was successful for not
	 */
	public boolean useItemOnPlayer(World world) {
		return useItem(world, world.getSpaceX(), world.getSpaceY());
	}

	/**
	 * uses the item in the direction given from the player
	 *
	 * @param world the state of the world before using the item
	 * @param cardinalityVal the direction from the player, where 0 is north, 1 is east, ect.
	 * @return whether the use attempt was successful for not
	 */
	public boolean useItemDirection(World world, int cardinalityVal) {
		int xCoord = world.getSpaceX();
		int yCoord = world.getSpaceY();
		switch(cardinalityVal) {
			case 0:
				yCoord++;
				break;
			case 1:
				xCoord++;
				break;
			case 2:
				yCoord--;
				break;
			case 3:
				xCoord--;
				break;
		}
		return useItem(world, xCoord, yCoord);
	}
}
