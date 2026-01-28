/**
 * @author Denny Cyr
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class CSVRecipeReader {

	//FINALS
    public static final CSVItemReader itemReader = new CSVItemReader("resources\\ItemInfo.csv");

	//vars
	private String fileName;

	/**
	 * primary constructor
	 *
	 * @param fileName the name of the file this will read from
	 */
	public CSVRecipeReader(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * reader for Recipes
	 *
	 * @param recipeID the ID for the recipe you want to read in
	 * @return the recipe indicated by the given ID
	 */
	public CraftingRecipe readRecipe(int recipeID) {
		CraftingRecipe toReturn = null;
		Scanner recipeReader = null;
		Scanner lineReader = null;
		try {
			File input = new File(fileName);
            recipeReader = new Scanner(input);
            recipeReader.useDelimiter(",");
            recipeReader.nextLine();
            while(recipeReader.nextInt() != recipeID) {
            	recipeReader.nextLine();
            }
            String correctLine = recipeReader.nextLine().replace(System.getProperty("line.separator"),"");
            lineReader = new Scanner(correctLine);
            lineReader.useDelimiter(",");
            int itemID = lineReader.nextInt();
            Item item = itemReader.readItem(itemID);
            int quantity = lineReader.nextInt();
            ArrayList<int[]> recipe = new ArrayList<int[]>();
            String formattedIngredient;
            while(lineReader.hasNext(".+")) {
            	formattedIngredient = lineReader.next();
				recipe.add(ingredientStringReader(formattedIngredient));
			}
			toReturn = new CraftingRecipe(item, recipe, quantity);
		} catch(IOException ioe) {
            System.out.println("Recipe Error!");
            System.exit(1);
        }
        if(lineReader != null) {
			lineReader.close();
		} if(recipeReader != null) {
			recipeReader.close();
		}
        return toReturn;
	}

	/**
	 * converts from an ingredient in String form to the integers
	 * representing the needed item and the quantity needed respectively
	 *
	 * @param formattedIngredient the ingredient needed in its string format, (item-quantity)
	 * @return the item and quantity needed of it for an ingredient
	 */
	public static int[] ingredientStringReader(String formattedIngredient) {
		if (formattedIngredient.matches("\\(\\d+?-\\d+?\\)")) {
			String[] ingredientString = formattedIngredient.split("\\(|-|\\)");
			int itemID = Integer.parseInt(ingredientString[1]);
			int quantity = Integer.parseInt(ingredientString[2]);
			int[] ingredient = {itemID, quantity};
			return ingredient;
		} else {
			return null;
		}
	}
}