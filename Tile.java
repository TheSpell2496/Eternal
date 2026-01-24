/**
 * @author Denny Cyr
 */
import java.util.ArrayList;
public class Tile {

    //FINALS
    public static final CSVItemReader itemReader = new CSVItemReader("ItemInfo.csv");

    //VARIABLES
    private int tileID;
    private String tileName;
    private String tileDesc;
    private boolean moveThrough;
    private char symbol;
    private int strength;
    private String material;
    private int itemForm;
    private int remains;
    protected ArrayList<Item> drops = new ArrayList<Item>();
    
    /**
     * primary constructor
     * 
     * @param tileID the id for the tile
     * @param tileName the name of the tile
     * @param tileDesc description of tile
     * @param moveThrough whether the tile can be moved onto or not
     * @param symbol the character that represents this tile on the map
     * @param strength the ability for this tile to withstand destruction, matched against an items strength
     * @param material the material that this tile is made out of, like trees are made of WOOD
     * @param itemForm the version of this tile that can be held as an item, set to -1 if there is no item 
     * @param remains the tile left over after this tile is destroyed
     */
    public Tile(int tileID, String tileName, String tileDesc, boolean moveThrough,
        char symbol, int strength, String material, int itemForm, int remains) {
        this.tileID = tileID;
        this.tileName = tileName;
        this.tileDesc = tileDesc;
        this.moveThrough = moveThrough;
        this.symbol = symbol;
        this.strength = strength;
        this.material = material;
        this.itemForm = itemForm;
        this.remains = remains;
        if (itemForm != -1) {
            drops.add(itemReader.readItem(itemForm));
        }
    }
    
    /**
     * getter for the tile ID
     *
     * @return returns the tile ID
     */
    public int getTileID() {
        return tileID;
    }

    /**
     * getter for description of tile
     *
     * @return returns the tile description
     */
    public String getTileDesc() {
        return tileDesc;
    }

    /**
     * getter for tileName
     *
     * @return returns the tile name
     */
    public String getTileName() {
        return tileName;
    }

    /**
     * checks if a tile can be moved through
     *
     * @return whether the tile can be moved through or not
     */
    public boolean getMoveThrough() {
        return moveThrough;
    }

    /**
     * getter for the tiles symbol
     *
     * @return symbol for that tile
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * getter for the strength of a tile
     *
     * @return the strength needed to destroy this tile
     */
    public int getStrength() {
        return strength;
    }

    /**
     * getter for the material of a tile
     *
     * @return the material the tiles made of
     */
    public String getMaterial() { 
        return material;
    }

    /**
     * getter for the item form of this tile
     *
     * @return the item that represents this tile, which is dropped upon destruction. If there is none this will be -1
     */
    public int getItemForm() {
        return itemForm;
    }

    /**
     * getter for the remains of a tile
     *
     * @return the tile left over after this one is destroyed
     */
    public int getRemains() { 
        return remains;
    }

    /**
     * getter for the drops of a tile
     *
     * @return the drops given by this tile
     */
    public ArrayList<Item> getDrops() {
        ArrayList<Item> dropsClone = new ArrayList<Item>(drops.size());
        for (Item item : drops) {
            dropsClone.add(item);
        }
        return dropsClone;
    }
}
