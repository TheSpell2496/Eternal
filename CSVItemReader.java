/**
 * @author Denny Cyr
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class CSVItemReader {
	//FINALS
	public final int MAX_CALL_NAMES = 7;

	//vars
	private String fileName;

	/**
	 * primary constructor
	 *
	 * @param fileName the name of the file this will read from
	 */
	public CSVItemReader(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * reader for Item, returns any type of item not just generic Items
	 *
	 * @param itemID the ID for the item you want to read in, the last digit is the species (type) of item
	 */
	public Item readItem(int itemID) {
		Item toReturn = null;
		int species = itemID%10;
		Scanner itemReader = null;
		try {
			File itemInfo = new File(fileName);
			itemReader = new Scanner(itemInfo);
			itemReader.useDelimiter(",");
			itemReader.nextLine();
			while(itemReader.nextInt() != (itemID)) {
				itemReader.nextLine();
			}
			itemReader = new Scanner(itemReader.nextLine());
			itemReader.useDelimiter(",");
			String name = itemReader.next();
			ArrayList<String> callNames = new ArrayList<String>();
			int i = 0;
			while(itemReader.hasNext() && i < MAX_CALL_NAMES) {
				i++;
				callNames.add(itemReader.next());
			} while(i < MAX_CALL_NAMES) {
				itemReader.next();
			}
			switch(species) {
				case 0: 
					toReturn = new Item(itemID, name, callNames);
					break;
				case 1: //tools
					int strength = itemReader.nextInt();
					String breakableMaterial = itemReader.next();
					int durability = Integer.parseInt(itemReader.next());
					ArrayList<int[]> transitions = new ArrayList<int[]>();
					while(itemReader.hasNext(".+")) {
						transitions.add(transitionStringReader(itemReader.next()));
					}
					toReturn = new Tool(itemID, name, callNames, strength, durability, breakableMaterial, transitions);
					break;
			}
		} catch(IOException ioe) {
			System.out.println("Error reading items!");
			System.exit(1);
		}
		if(itemReader != null) {
			itemReader.close();
		}
		return toReturn;
	}

	/**
	 * converts from a transition in String form to the integer values representing the tiles
	 *
	 * @param formattedTransition a transition from 1 tile to another before being converted to 2 integers
	 * @return the integers that represent the tiles initial state, and the final state respectively
	 */
	public static int[] transitionStringReader(String formattedTransition) {
		if (formattedTransition.matches("\\(\\d+?-\\d+?\\)")) {
			String[] transitionString = formattedTransition.split("[-)(]");
			int[] transition = new int[2];
			transition[0] = Integer.parseInt(transitionString[1]);
			transition[1] = Integer.parseInt(transitionString[2]);
			return transition;
		} else {
			return null;
		}
	}
}