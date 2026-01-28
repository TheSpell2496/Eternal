import java.util.ArrayList;
import java.awt.Point;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Denny Cyr
 */
public class Player implements Cloneable {

	//VARIABLES
	String name;
	int totalHealth;
	int currHealth;
	Point spaceCoords;
	Point worldCoords;
	ArrayList<ArrayList<Item>> inventory = new ArrayList<ArrayList<Item>>();
	Item equippedItem = null;

	/**
	 * primary constructor
	 *
	 * @param name the name of the player
	 * @param initHealth the health the player begins with. This is set as the total health your player has
	 * @param spaceCoords the coordinants of the player in the current space, where 1 tile crossed = 1 unit crossed
	 * @param worldCoords the coordinants of the player in the world, where 1 space crossed = 1 unit crossed
	 */
	public Player(String name, int initHealth, Point spaceCoords, Point worldCoords) {
		this.name = name;
		totalHealth = initHealth;
		currHealth = initHealth;
		this.spaceCoords = (Point)spaceCoords.clone();
		this.worldCoords = (Point)worldCoords.clone();
	}

	/**
	 * secondary constructor, for initializing a player with a certain inventory
	 *
	 * @param name the name of the player
	 * @param initHealth the health the player begins with. This is set as the total health your player has
	 * @param spaceCoords the coordinants of the player in the current space, where 1 tile crossed = 1 unit crossed
	 * @param worldCoords the coordinants of the player in the world, where 1 space crossed = 1 unit crossed
	 * @param inventory a list of items held by the player
	 */
	public Player(String name, int initHealth, Point spaceCoords, Point worldCoords, ArrayList<Item> inventory) {
		this(name, initHealth, spaceCoords, worldCoords);
		for (Item item : inventory) {
			addToInventory(item);
		}
	}

	/**
	 * getter for spaceCoords
	 *
	 * @return the coordinants of the player in the current space, where 1 tile crossed = 1 unit crossed
	 */
	public Point getSpaceCoords() {
		return (Point)spaceCoords.clone();
	}

	/**
	 * getter for worldCoords
	 *
	 * @return the coordinants of the player in the current world, where 1 space crossed = 1 unit crossed
	 */
	public Point getWorldCoords() {
		return (Point)worldCoords.clone();
	}

	/**
     * getter for name
     *
     * @return the name of your player
     */
    public String getName() {
    	return name;
    }

    /**
     * getter for total health
     *
     * @return the total health of your player
     */
    public int getTotalHealth () {
    	return totalHealth;
    }

    /**
     * getter for current health
     *
     * @return the current health of your player
     */
    public int getCurrentHealth (int health) {
    	return currHealth;
    }

    /**
	 * getter for the currently equipped item
	 *
	 * @return the item currently equipped by the player
	 */
	public Item getEquippedItem() {
		return equippedItem;
	}

	/**
	 * setter for spaceCoords
	 *
	 * @param spaceCoords the coordinants in the space, where 1 tile = 1 unit
	 */
	public void setSpaceCoords(Point spaceCoords) {
		this.spaceCoords = (Point)spaceCoords.clone();
	}

	/**
	 * setter for worldCoords
	 *
	 * @param worldCoords the coordinants in the space, where 1 space = 1 unit
	 */
	public void setWorldCoords(Point worldCoords) {
		this.worldCoords = (Point)worldCoords.clone();
	}

	/**
     * setter for name
     *
     * @param name the new name of your player
     */
    public void setName(String name) {
    	this.name = name;
    }

    /**
     * setter for health
     *
     * @param health the new current health of your player
     */
    public void setCurrentHealth (int health) {
    	this.currHealth = health;
    }

    /**
	 * setter for the currently equipped item
	 *
	 * @param item the item you would like to equip
	 */
	public void setEquippedItem(Item item) {
		equippedItem = item;
	}

	/**
	 * getter for inventory
	 *
	 * @return the inventory of the player
	 */
	public ArrayList<ArrayList<Item>> getInventory() {
		ArrayList<ArrayList<Item>> inventoryClone = new ArrayList<ArrayList<Item>>(inventory.size());
		for (int i = 0; i < inventory.size(); i++) {
			inventoryClone.add(new ArrayList<Item>());
			for (int j = 0; j < inventory.get(i).size(); j++) {
				inventoryClone.get(i).add(inventory.get(i).get(j));
			}
		}
		return inventoryClone;
	}

	/**
	 * adds the given element to the inventory
	 *
	 * @param item the item you want to add to the inventory
	 */
	public void addToInventory(Item item) {
		int index;
		for (index = 0; index < inventory.size(); index++) {
			if (item.getItemID() == inventory.get(index).get(0).getItemID() && checkStackability(item) == true) {
				inventory.get(index).add(item);
				return;
			}
		}
		inventory.add(new ArrayList<Item>());
		inventory.get(index).add(item);
	}

	/**
	 * removes the given indexes item from the inventory
	 *
	 * @param index the index of the item this method removes
	 */
	public void removeFromInventory(int index) {
		inventory.get(index).remove(0);
		if (inventory.get(index).size() == 0) {
			inventory.remove(index);
		}
	}

	/**
     * copier for a Player
     *
     * @return a deep copy of Player
     */
     public Object clone() throws CloneNotSupportedException {
        Player playerCopy = (Player)super.clone();
        ArrayList<ArrayList<Item>> inventoryCopy = getInventory();
        playerCopy.inventory = inventoryCopy;
        playerCopy.spaceCoords = this.spaceCoords;
        playerCopy.worldCoords = this.worldCoords;
        return playerCopy;
    }

    /**
     * checks if the item can be stacked, must be updated before
     * adding an unstackable item species
     *
     * @param item the item you want to check the stackability of
     */
	public static boolean checkStackability(Item item) {
    	File unstackableData = new File("resources\\listofunstackables.txt");
    	Scanner stackabilityReader = null;
    	try {
    		stackabilityReader = new Scanner(unstackableData);
    	} catch(FileNotFoundException fnfe) {
    		System.out.println("can't find stackabilty data, rename file" +
    			"with unstackable species to \"listofunstackables.txt\"");
    		System.exit(1);
    	}
    	stackabilityReader.nextLine();
    	stackabilityReader.nextLine();
    	stackabilityReader.nextLine();
    	int species = item.getItemID()%10;
    	while(stackabilityReader.hasNextInt()) {
    		if (stackabilityReader.nextInt() == species) {
    			return false;
    		}
    	}
    	return true;
    }

    /**
	 * reads in a list of items into the inventory format
	 *
	 * @param itemList the list of items you want to format
	 * @return the list of items given but in the inventory format
	 */
    public static ArrayList<ArrayList<Item>> formatItemList(ArrayList<Item> itemList) {
    	ArrayList<ArrayList<Item>> inventoryFormat = new ArrayList<ArrayList<Item>>();
    	boolean foundInList = false;
    	for (Item item : itemList) {
    		int index;
			for (index = 0; index < inventoryFormat.size(); index++) {
				if (item.getItemID() == inventoryFormat.get(index).get(0).getItemID() && checkStackability(item) == true) {
					inventoryFormat.get(index).add(item);
					foundInList = true;
					break;
				}
			}
			if (!foundInList) {
				inventoryFormat.add(new ArrayList<Item>());
				inventoryFormat.get(index).add(item);
			}
    	}
    	return inventoryFormat;
    }
}
