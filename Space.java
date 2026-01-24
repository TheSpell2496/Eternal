
/**
 * @author Denny Cyr
 */
public class Space implements Cloneable {

    //FINALS
    public static final CSVTileReader tileReader = new CSVTileReader("TileInfo.csv");

    //VARIABLES
    private final int width,length;
    private Tile[][] tiles;
    private char[][] map;
    
    /**
     * primary constructor
     * 
     * @param width horizontal size of space (in 2d space)
     * @param length vertical size of space (in 2d space)
     * @param gameRule rule that determines what world is generated
     */
    public Space(int width, int length, int gameRule) {
        this.width = width;
        this.length = length;
        tiles = new Tile[width][length];
        map = new char[width][length];
        switch (gameRule) {
        	case 1:
        	formEmptyTiles(width, length, tiles);
            formWallBorder(width, length, tiles);
            formMap(tiles);
            break;
            case 2:
            int tileNum;
            formEmptyTiles(width, length, tiles);
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    tileNum = (int)(Math.random() * 10);
                    tileNum *= 10;
                    this.changeTile(j,i,(tileNum + 30));
                }
            }
            //formWallBorder(width, length, tiles);
            formMap(tiles);
            break;
            case 3:
            System.out.println("you shouldn't see this, use the secondary constructor for case 3");
            case 4: 
        }
    }

    /**
     * secondary constructor that doesn't require width and length data, only an array of tiles
     *
     * @param tileData contains the array of tiles for the whole space
     */
    public Space(Tile[][] tileData) {
        width = tileData.length;
        length = tileData[0].length;
        tiles = new Tile[width][length];
        map = new char[width][length];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                Tile tileCopier = tileData[i][j];
                tiles[i][j] = tileCopier;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                map[i][j] = tiles[i][j].getSymbol();
            }
        }
    }

    /**
     * getter for width
     *
     * @return width 
     */
    public int getWidth() {
    	return width;
    }

    /**
     * getter for length
     *
     * @return length
     */
    public int getLength() {
    	return length;
    }
    
    /**
     * getter for a specific piece of the map
     *
     * @param i the width (horizontal) of the map/space
     * @param j the length (vertical) of the map/space
     * @return the symbol for that piece of the map
     */
    public char getMapCharacter(int i, int j) {
        return map[i][j];
    }

    /**
     * getter for the map
     *
     * @return the current map of the space
     */
    public char[][] getMap() {
        char[][] mapCopy = new char[map.length][map[0].length];
        for(int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                mapCopy[i][j] = map[i][j];
            } 
        }
        return mapCopy;
    }
    
    /**
     * returns the description of the chosen tile in the array
     * 
     * @param horizMove how far this tile  horizontally from (0,0)
     * @param vertMove how far this tile is vertically from (0,0)
     * @return the description of the tile in the location described
     */
    public String getDescOfTile(int horizMove, int vertMove) {
        return tiles[horizMove][vertMove].getTileDesc();
    }

    /**
     *returns whether a tile in a space can be moved through
     *
     * @param horizMove same as for getDescOfTile
     * @param vertMove same as for getDescOfTile
     * @return whether a tile in a space can be moved through
     */
    public boolean getMoveThroughOfTile(int horizMove, int vertMove) {
        return tiles[horizMove][vertMove].getMoveThrough();
    }

    /**
     * returns the name of the chosen tile in the array
     * 
     * @param horizMove how far this tile horizontally from (0,0)
     * @param vertMove how far this tile is vertically from (0,0)
     * @return the name of the tile in the location described
     */
    public String getNameOfTile(int horizMove, int vertMove) {
        return tiles[horizMove][vertMove].getTileName();
    }
    
    /**
     * creates the border wall around the world
     * 
     * @param width the width of the space not including space taken by walls
     * @param length the length of the space not including space taken by walls
     * @param tiles the array of tiles this world contains
     */
    private void formWallBorder(int width, int length, Tile[][] tiles) {
        for (int j = 0; j < width; j++) {
            this.changeTile(j,0,20);
            this.changeTile(j,length - 1,20);
        }
        for (int i = 1; i < length - 1; i++) {
            this.changeTile(0,i,20);
            this.changeTile(width - 1,i,20);
        }
    }
    
    /**
     * creates the tile spaces
     * 
     * @param width the width of the space not including space taken by walls
     * @param length the length of the space not including space taken by walls
     * @param tiles the array of tiles this world contains
     */
    private void formEmptyTiles(int width, int length, Tile[][] tiles) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                tiles[j][i] = tileReader.readTile(10);
            }
        }
    }
    /**
     * creates the map for an area
     *
     * @param tiles a tile matrix for a given space
     */
    private void formMap(Tile[][] tiles) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                map[j][i] = tiles[j][i].getSymbol();
            }
        }
    }

    /**
     * getter for a chosen tile in the a space
     *
     * @param horizMove the horizontal distance from the leftmost point
     * @param vertMove the vertical distance from the bottommost point
     * @return a chosen tile at these coordinants in this space
     */
    public Tile getTile(int horizMove, int vertMove) {
        return tiles[horizMove][vertMove];
    }

    /**
     * getter for all the tiles in a space in a double array
     *
     * @return all the tiles in a space in the form of a double array
     */
    public Tile[][] getTiles() {
    	Tile[][] tilesCopy = new Tile[width][length];
    	Tile tileCopy;
    	for (int i = 0; i < width; i++) {
    		for (int j = 0; j < length; j++) {
    			tileCopy = tiles[i][j];
    			tilesCopy[i][j] = tileCopy;
    		}
    	}
        return tilesCopy;
    }

    /**
     * changes the tile in the given position to the tile indicated by the itemID
     *
     * @param xCoord the horizontal distance from the leftmost point
     * @param yCoord the vertical distance from the bottommost point
     * @param tileID the ID of the tile you intend to change to
     */
    public void changeTile(int xCoord, int yCoord, int tileID) {
        tiles[xCoord][yCoord] = tileReader.readTile(tileID);
        map[xCoord][yCoord] = tiles[xCoord][yCoord].getSymbol();
    }

     /**
     * copier for a Space
     *
     * @return a deep copy of Space
     */
     public Object clone() throws CloneNotSupportedException {
        Space spaceCopy = (Space)super.clone();
        Tile[][] tilesCopy = new Tile[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tilesCopy[i][j] = tiles[i][j];
                map[i][j] = tiles[i][j].getSymbol();
            }
        }
        spaceCopy.tiles = tilesCopy;
        return spaceCopy;
     }
}
