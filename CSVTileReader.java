/**
 * @author Denny Cyr
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class CSVTileReader {

	//FINALS
	public static final CSVRecipeReader recipeReader = new CSVRecipeReader("resources\\CraftingRecipes.csv");

	//vars
	private String fileName;

	/**
	 * primary constructor
	 *
	 * @param fileName the name of the file this will read from
	 */
	public CSVTileReader(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * reader for Tile, returns any type of Tile not just generic Tiles
	 *
	 * @param tileID the ID for the tile you want to read in, the last digit is the species (type) of Tile
	 * @return the tile identified by the tileID
	 */
	public Tile readTile(int tileID) {
		Tile toReturn = null;
		BufferedReader tileReader = null;
		FileInputStream fileStream = null;
        InputStreamReader inputStream = null;
		Scanner lineReader = null;
		int species = tileID%10;
        try {
            File input = new File(fileName);
            fileStream = new FileInputStream(input);
            inputStream = new InputStreamReader(fileStream);
            tileReader = new BufferedReader(inputStream);
            tileReader.readLine();
            String line = null;
            int lineID = -1;
            while(lineID != tileID) {
            	line = tileReader.readLine();
            	lineID = Integer.parseInt(line.split(",")[0]);
            }
            String correctLine = line.replace(System.getProperty("line.separator"),"");
            lineReader = new Scanner(correctLine);
            lineReader.useDelimiter(",");
            lineReader.next();
            String tileName = lineReader.next();
            String tileDesc = lineReader.next();
            tileDesc = tileDesc.replace(';',',');
            boolean moveThrough = Boolean.parseBoolean(lineReader.next());
            char symbol = lineReader.next().charAt(0);
            int strength = lineReader.nextInt();
            String type = lineReader.next();
            int itemForm = lineReader.nextInt();
            int remains = lineReader.nextInt();
            switch(species) {
				case 0:
					toReturn = new Tile(tileID, tileName, tileDesc, moveThrough, symbol, strength, type, itemForm, remains);
					break;
				case 1:
					String formattedDropRate;
					ArrayList<DropRate> dropRates = new ArrayList<DropRate>();
					while(lineReader.hasNext(".+")) {
						formattedDropRate = lineReader.next();
						dropRates.add(dropRateStringReader(formattedDropRate));
					}
					toReturn = new LootTile(tileID, tileName, tileDesc, moveThrough, symbol, strength, type, itemForm, remains, dropRates);
					break;
				case 2:
					ArrayList<CraftingRecipe> recipes = new ArrayList<CraftingRecipe>();
					while(lineReader.hasNext(".+")) {
						CraftingRecipe recipe = recipeReader.readRecipe(lineReader.nextInt());
						recipes.add(recipe);
					} 
					toReturn = new CraftTile(tileID, tileName, tileDesc, moveThrough, symbol, strength, type, itemForm, remains, recipes);
			}
        }
        catch(IOException ioe) {
            System.out.println("Tile Error!");
            System.exit(1);
        }
        if(tileReader != null) {
        	try {
        		tileReader.close();
        	} catch (IOException ioe) {
        		System.out.println("TILE READER HAS FAILED TO CLOSE");
        	}
		} if(lineReader != null) {
			lineReader.close();
		}
        return toReturn;
	}

	/**
	 * converts from a drop rate in String form to the integer values representing the tiles
	 *
	 * @param formattedDropRate the drop rate in its string format, before seperated into individual pieces of data
	 * @return the drop rate for an item 
	 */
	public static DropRate dropRateStringReader(String formattedDropRate) {
		if (formattedDropRate.matches("\\(\\d+?\\)-\\(\\d+?(\\.\\d+?)??\\)-\\(\\d+?-\\d+?\\)")) { //format: (integer)-(double)-(integer-integer)
			String[] dropRateString = formattedDropRate.split("\\(|\\)-\\(|-|\\)");
			int itemID = Integer.parseInt(dropRateString[1]);
			double percentChance = Double.parseDouble(dropRateString[2]);
			int[] quantity = {Integer.parseInt(dropRateString[3]),Integer.parseInt(dropRateString[4])};
			return new DropRate(itemID, percentChance, quantity);
		} else {
			return null;
		}
	}
}