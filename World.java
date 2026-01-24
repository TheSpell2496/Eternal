import java.util.ArrayList;
import java.awt.Point;
import java.util.HashMap;

/**
 * @author Denny Cyr
 */
public class World {

	//finals
	public static final CSVTileReader tileReader = new CSVTileReader("TileInfo.csv");
	public final int GRASS_TILE_CHANCE = 70; //grass tile chance is the total probability it'll be a grass tile
	public final int COMMON_TILE_CHANCE = 75; //the rest are the probability IF A GRASS TILE IS NOT CHOSEN, then this is the chance
	public final int MEDIUM_RARE_TILE_CHANCE = 20; //so these bottom 3 have to all add up to 100
	public final int RARE_TILE_CHANCE = 5;

	//PARAMS
	private Player player;
	private HashMap<Point, Space> spaceMap = new HashMap<Point, Space>();
	private int seed;

	/**
     * primary constructor
     * 
     * @param seed an integer that represents your worlds initial state 
     */
	public World(int seed) {
		int initXCoord = ((int)(Math.random() * 11)-5);
		int initYCoord = ((int)(Math.random() * 11)-5);
		spaceMap = new HashMap<Point, Space>();
		this.seed = seed;
		spaceMap.put(new Point(initXCoord, initYCoord), this.createNewSurvivalSpace());
		player = new Player("mark", 10, new Point(1,1), new Point(initXCoord, initYCoord));
	}

	/**
	 * secondary constructor, for creating bounded custom Worlds
	 *
	 * @param SpaceMatrix a matrix of spaces 
	 * @param player a custom player
	 */
	public World(Space[][] spaceMatrix, Player player) {
		seed = 0;
		int currentX = (spaceMatrix.length/-2);
		int currentY = (spaceMatrix[0].length/-2);
		int initialY = currentY;
		try {
			for (int i = 0; i < spaceMatrix.length; i++) {
				for (int j = 0; j < spaceMatrix[0].length; j++) {
					spaceMap.put(new Point(currentX, currentY), (Space)spaceMatrix[i][j].clone());
					currentY++;
				}
				currentY = initialY;
				currentX++;
			}
			this.player = (Player)player.clone();
		} catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace();
		}
	}

	/**
	 * moves to the space in the direction given
	 *
	 * @param cardinalityVal 0 = north, then they're in order going clockwise (east=1). 
	 */
	public void moveSpace(int cardinalityVal) {
		Point pointToGo = World.getPointByDirection(player.getWorldCoords(), cardinalityVal);
		player.setWorldCoords(pointToGo);
		if(!spaceMap.containsKey(pointToGo)) {
			spaceMap.put(pointToGo, createNewSurvivalSpace());
		}
	}

	/**
	 * getter for the current Space
	 *
	 * @return the currently occupied space
	 */
	public Space getCurrentSpace() {
		Space toReturn = null;
		try {
			toReturn = (Space)spaceMap.get(player.getWorldCoords()).clone();
		} catch (CloneNotSupportedException cnse) {
    	   	cnse.printStackTrace();
		}
		return toReturn;
	}

	/**
	 * creates a space given a seed using generation rules for a survival world
	 *
	 * @return the space at the given location
	 */
	public Space createNewSurvivalSpace() {
		Tile[][] tileMatrix = new Tile[16][16];
		int[] commonTiles = {11,21,71};
		int[] mediumRareTiles = {31,51};
		int[] rareTiles = {41,61};
		for(int i = 0; i < tileMatrix.length; i++) {
			for(int j = 0; j < tileMatrix[i].length; j++) {
				double grassRandomVal = Math.random()*100;
				double tileRandomVal = Math.random()*100;
				if(grassRandomVal < GRASS_TILE_CHANCE) {
					tileMatrix[i][j] = tileReader.readTile(290);
				} else if (tileRandomVal < COMMON_TILE_CHANCE) {
					int tileChooser = (int)(Math.random()*commonTiles.length);
					tileMatrix[i][j] = tileReader.readTile(commonTiles[tileChooser]);
				} else if (tileRandomVal < MEDIUM_RARE_TILE_CHANCE + COMMON_TILE_CHANCE){
					int tileChooser = (int)(Math.random()*mediumRareTiles.length);
					tileMatrix[i][j] = tileReader.readTile(mediumRareTiles[tileChooser]);
				} else {
					int tileChooser = (int)(Math.random()*rareTiles.length);
					tileMatrix[i][j] = tileReader.readTile(rareTiles[tileChooser]);
				}
			}
		}
		Space space = new Space(tileMatrix);
		return space;
	}

	/**
	 * determines the coordinants of the space in the given direction from the given coordinants
	 *
	 * @param coordinants the current coordinants of the player before moving
	 * @param cardinalityValue the direction moved, where 0 is north, 1 is east, ect. 
	 * @return the coordinants of the space in the given direction
	 */
	public static Point getPointByDirection(Point coordinants, int cardinalityVal) {
		Point toReturn = null;
		int currentXCoord = (int)coordinants.getX();
		int currentYCoord = (int)coordinants.getY();
		switch(cardinalityVal) {
			case 0:
				toReturn = new Point(currentXCoord,currentYCoord+1);
				break;
			case 1:
				toReturn = new Point(currentXCoord+1,currentYCoord);
				break;
			case 2:
				toReturn = new Point(currentXCoord,currentYCoord-1);
				break;
			case 3:
				toReturn = new Point(currentXCoord-1,currentYCoord);
				break;
		}
		return toReturn;
	}

	/**
	 * getter for the player
	 *
	 * @return the current player in this world
	 */
	public Player getPlayer() {
		Player playerClone = null;
		try {
			playerClone = (Player)player.clone();
		} catch (CloneNotSupportedException cnse) {
    	   	cnse.printStackTrace();
		}
		return playerClone;
	}

	/**
	 * getter for the worlds current x coordinant
	 *
	 * @return the current x coordinant, where 1 space = 1 unit
	 */
	public int getWorldX() {
		return (int)player.getWorldCoords().getX();
	}

	/**
	 * getter for the worlds current y coordinant
	 *
	 * @return the current y coordinant, where 1 space = 1 unit
	 */
	public int getWorldY() {
		return (int)player.getWorldCoords().getY();
	}

	/**
	 * getter for the spaces current x coordinant
	 *
	 * @return the current x coordinant, where 1 tile = 1 unit
	 */
	public int getSpaceX() {
		return (int)player.getSpaceCoords().getX();
	}

	/**
	 * getter for the spaces current y coordinant
	 *
	 * @return the current y coordinant, where 1 tile = 1 unit
	 */
	public int getSpaceY() {
		return (int)player.getSpaceCoords().getY();
	}

	/**
	 * setter for your space position
	 *
	 * @param xCoord the x coordinant you want to place your player in, where 1 tile = 1 unit
	 * @param yCoord the y coordinant you want to place your player in, where 1 tile = 1 unit
	 */
	public void setSpaceCoords(int xCoord, int yCoord) {
		player.setSpaceCoords(new Point(xCoord, yCoord));
	}

	/**
	 * setter for your world position
	 *
	 * @param xCoord the x coordinant you want to place your player in, where 1 space = 1 unit
	 * @param yCoord the y coordinant you w(ant to place your player in, where 1 space = 1 unit
	 */
	public void setWorldCoords(int xCoord, int yCoord) {
		player.setWorldCoords(new Point(xCoord, yCoord));
 	}
	
	/**
	 * setter for the currently equipped item
	 *
	 * @param item the item you would like to equip
	 */
	public void setEquippedItem(Item item) {
		player.setEquippedItem(item);
	}
	 
	/**
	 * changes the current spaces tile in the given position to the tile indicated by the itemID
	 *
	 * @param xCoord the horizontal distance from the leftmost point
	 * @param yCoord the vertical distance from the bottommost point
	 * @param tileID the ID of the tile you intend to change to
	 */
	public void changeTile(int xCoord, int yCoord, int tileID) {
		Point currentPoint = this.getPlayer().getWorldCoords();
		Space currentSpace = spaceMap.get(currentPoint);
        currentSpace.changeTile(xCoord, yCoord, tileID);
    }

	/**
	 * getter for inventory
	 *
	 * @return the inventory of the player
	 */
	public ArrayList<ArrayList<Item>> getInventory() {
		return player.getInventory();
	}

	/**
	 * adds the given element to the end of the inventory
	 *
	 * @param item the item you want to add to the inventory
	 */
	public void addToInventory(Item item) {
		player.addToInventory(item);
	}

	/**
	 * removes the given indexes item from the players inventory
	 *
	 * @param index the index of the item this method removes
	 */
	public void removeFromInventory(int index) {
		player.removeFromInventory(index);
	}

	/**
	 * removes the given itemID from the players inventory if it exists
	 *
	 * @param itemID the ID of the item you plan to remove
	 * @return whether the item was successfully removed
	 */
	public boolean removeIDFromInventory(int itemID) {
		ArrayList<ArrayList<Item>> inventory = player.getInventory();
		for (int i = 0; i < inventory.size(); i++) {
			ArrayList<Item> stack = inventory.get(i);
			if (stack.get(0).getItemID() == itemID) {
				player.removeFromInventory(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * gets the tile in the given direction from the player
	 *
	 * @param cardinalityVal the direction from the player, where 0 = north, 1 = east, ect.
	 */
	public Tile getTileByDirection(int cardinalityVal) {
		int xCoord = (int)player.getSpaceCoords().getX();
		int yCoord = (int)player.getSpaceCoords().getY();
		Space currentSpace = spaceMap.get(player.getWorldCoords());
		switch(cardinalityVal) {
        	case 0: return currentSpace.getTile(xCoord, yCoord+1);
        	case 1: return currentSpace.getTile(xCoord+1, yCoord);
        	case 2: return currentSpace.getTile(xCoord, yCoord-1);
        	case 3: return currentSpace.getTile(xCoord-1, yCoord);
        }
        return null;
	}
}