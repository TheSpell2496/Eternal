import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;
import java.util.NoSuchElementException;
import java.io.*;

/**
 * @author Denny Cyr
 */
public class TheEternalDesert {

    public static final int GAME_RULE_COUNT = 4;
    public static final Scanner KBD = new Scanner(System.in);
    public static final CSVTileReader tileReader = new CSVTileReader("TileInfo.csv");
    public static final CSVItemReader itemReader = new CSVItemReader("ItemInfo.csv");

    public static void main(String[] args) {

        //variables
        Player defaultPlayer = new Player("mark", 10, new Point(1,1), new Point(0,0));
        Player survivalPlayer = new Player("mark", 10, new Point(8,8), new Point(0,1));
        int width = 0, length = 0, gameRule, seed = 0;
        String userInput = "";
        char[][] map;
        World newWorld = null;
        Space currentSpace = null;

        //asking rules & creating space
        System.out.println("what do you want the game rule to be?\n"
                + "1 is a very basic test room\n"
                + "2 has randomized coloured tiles\n"
                + "3 is an ascii art room! (WIP)\n"
                + "4 is the main demo");
        userInput = KBD.next();
        while(!userInput.matches("\\d+?") || Integer.parseInt(userInput) <= 0 ||
        	Integer.parseInt(userInput) > GAME_RULE_COUNT) {
        	System.out.println(String.format("please enter an integer between 1 - %d", GAME_RULE_COUNT));
        	userInput = KBD.next();
        }
        gameRule = Integer.parseInt(userInput);
        Space[][] spaceWrapper = new Space[1][1];
        switch(gameRule) { 
            case 1:
            	width = getWidthUser() + 2;
            	length = getLengthUser() + 2;
        	    spaceWrapper[0][0] = new Space(width, length, gameRule);
            	newWorld = new World(spaceWrapper, defaultPlayer);
            	currentSpace = newWorld.getCurrentSpace();
            	break;
            case 2: 
	            width = getWidthUser() + 2;
    	        length = getLengthUser() + 2;
        	    currentSpace = new Space(width, length, gameRule);
    	        spaceWrapper[0][0] = new Space(width, length, gameRule);
            	newWorld = new World(spaceWrapper, defaultPlayer);
            	currentSpace = newWorld.getCurrentSpace();
        	    break;
            case 3:
	            currentSpace = new Space(mapMaker("Map3Info.txt"));
    	        width = currentSpace.getTiles().length;
        	    length = currentSpace.getTiles()[0].length;
    	        spaceWrapper[0][0] = currentSpace;
            	newWorld = new World(spaceWrapper, defaultPlayer);
            	currentSpace = newWorld.getCurrentSpace();
            	break;
            case 4:
	            //System.out.print("what do you want the seed of your world to be? ");
    	        //seed = KBD.nextInt();
            	Space spawnSpace = new Space(mapMaker("demoPrebuiltMaps\\SpawnSpace.txt"));
				Space alterSpace = new Space(mapMaker("demoPrebuiltMaps\\AlterRoom.txt"));
				Space partySpace = new Space(mapMaker("demoPrebuiltMaps\\PartyRoom!.txt"));
				Space[][] builtSpaces = {{partySpace, alterSpace, spawnSpace}};
        	    newWorld = new World(builtSpaces, survivalPlayer);
        	    newWorld.addToInventory(itemReader.readItem(11));
        	    newWorld.addToInventory(itemReader.readItem(21));
    	        currentSpace = newWorld.getCurrentSpace();
        	    width = currentSpace.getTiles().length;
            	length = currentSpace.getTiles()[0].length;
        }
        KBD.nextLine();
        map = new char[width][length];
        //enter world
        String textOutput = "SLOP";
        String commands = "type up, down, left, right to move those "
                    + "directions\nyou can also type cardinal directions!"
                    + " (North = up)\nlook - to look around room\ninventory - "
                    + "lets you look at and use your items\nexit/leave - to "
                    + "leave simulation.\nhelp - repeats commands";
        System.out.println(commands); // ^^^^^ sets x and y Coords each to a random val between -5 to 5
        map = currentSpace.getMap();
        map[newWorld.getSpaceX()][newWorld.getSpaceY()] = '&';
        while (true) {
            userInput = KBD.nextLine();
            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("Have a good day!");
                System.exit(0);
            } else if (userInput.equalsIgnoreCase("look")) {
            	textOutput = currentSpace.getDescOfTile(newWorld.getSpaceX(), newWorld.getSpaceY());
            } else if (userInput.matches("(up)|(north)")) {
            	textOutput = movePlayer(0, newWorld);
            } else if (userInput.matches("(right)|(east)")) {
                textOutput = movePlayer(1, newWorld);
            } else if (userInput.matches("(down)|(south)")) {
                textOutput = movePlayer(2, newWorld);
            } else if (userInput.matches("(left)|(west)")) {
                textOutput = movePlayer(3, newWorld);
            } else if (userInput.equalsIgnoreCase("inventory")) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
                    + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                int position = 1;
                String inventoryInput = "";
                String inventoryHelp = "type up and down to traverse the inventory\n"
                 + "walk into a solid tile with an item equipped to use it in that direction\n"
                 + "to equip or unequip an item, simply type equip or unequip!\n"
                 + "you can always type help if you need a reminder!\n"
                 + "and of course just type leave if you want to leave the inventory\n";
                String inventoryOutput = inventoryHelp;
                while (!inventoryInput.equalsIgnoreCase("leave")) {
                    //ALL THE CODING FOR INVENTORY COMMANDS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    textOutput = currentSpace.getDescOfTile(newWorld.getSpaceX(), newWorld.getSpaceY());
                    System.out.println("-*=INVENTORY=*-");
                    drawInventory(newWorld.getPlayer(), position);
                    System.out.print(inventoryOutput);
                    inventoryInput = KBD.nextLine();
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
                    + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    if (inventoryInput.equalsIgnoreCase("down")) {
                        if (position != newWorld.getInventory().size()) {
                            position++;
                        } else {
                            position = 1;
                        }
                        inventoryOutput = "";
                    } else if (inventoryInput.equalsIgnoreCase("up")) {
                        if (position != 1) {
                            position--;
                        } else {
                            position = newWorld.getInventory().size();
                        }
                        inventoryOutput = "";
                    } else if (inventoryInput.equalsIgnoreCase("equip")) {
                    	newWorld.setEquippedItem(newWorld.getInventory().get(position - 1).get(0));
                    	inventoryOutput = newWorld.getInventory().get(position - 1).get(0).getItemName() + " equipped!\n";
                    } else if (inventoryInput.equalsIgnoreCase("unequip")) {
                    	if (newWorld.getInventory().get(position - 1).get(0) == newWorld.getPlayer().getEquippedItem()) {
                    		newWorld.setEquippedItem(null);
                    		inventoryOutput = "";
                    	} else {
                    		inventoryOutput = "This item is not equipped\n";
                    	}
                    } else if (inventoryInput.equalsIgnoreCase("help")) {
                    	inventoryOutput = String.format("%s", inventoryHelp);
                    } else {
                    	inventoryOutput = "Invalid command, please repeat\n";
                    }
                }
            } else if (userInput.equalsIgnoreCase("help")) {
                textOutput = commands;
            } else {
                textOutput = "Invalid command, please repeat";    
            }
            currentSpace = newWorld.getCurrentSpace();
            map = currentSpace.getMap();
            width = currentSpace.getWidth();
            length = currentSpace.getLength();
            map[newWorld.getSpaceX()][newWorld.getSpaceY()] = '&';
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
               + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            printMap(width, length, map);
            System.out.println("\n" + textOutput);
        }
    }

    /**
     * asks the user for the width of a space
     *
     * @return the users chosen width
     */
    public static int getWidthUser() {
        System.out.print("what do you want the width of your room to be? ");
        String userInput = KBD.next();
        while(!userInput.matches("\\d+?") || Integer.parseInt(userInput) <= 0) {
        	System.out.println("invalid width, please try again");
        	userInput = KBD.next();
        }
        int width = Integer.parseInt(userInput);
        return width;
    }

    /**
     * asks the user for the length of a space
     *
     * @return the users chosen length
     */
    public static int getLengthUser() {
        System.out.print("what do you want the length of your room to be? ");
        String userInput = KBD.next();
        while(!userInput.matches("\\d+?") || Integer.parseInt(userInput) <= 0) {
        	System.out.println("invalid length, please try again");
        	userInput = KBD.next();
        }
        int length = Integer.parseInt(userInput);
        return length;
    }
    
    /**
     * checks if your running into a wall
     * 
     * @param xCoord the x coordinant within the space, where 1 tile = 1 unit
     * @param yCoord the y coordinant within the space, where 1 tile = 1 unit
     * @param cardinalityVal an integer representing a direction, where 0 = north, 1 = east, ect...
     * @param space the current space that has been created THE SIMULATION
     * @return whether or not there's a non-movethroughable object where the player is moving
     */
    public static boolean checkIfWall(int xCoord, int yCoord,
            int cardinalityVal, Space space) {
        switch(cardinalityVal) {
        	case 0: return space.getMoveThroughOfTile(xCoord, yCoord+1);
        	case 1: return space.getMoveThroughOfTile(xCoord+1, yCoord);
        	case 2: return space.getMoveThroughOfTile(xCoord, yCoord-1);
        	case 3: return space.getMoveThroughOfTile(xCoord-1, yCoord);
        }
        return false;
    }

    /**
     * draws a map using ASCII characters, key for common symbols is in game
     * 
     * @param width the horizontal space taken by the space
     * @param length the vertical space taken by the space
     * @param map the grid of characters that represent the actual map data
     */
    public static void printMap(int width, int length, char[][] map) {
        for (int i = length - 1; i >= 0; i--) {
            System.out.println("");
            for (int j = 0; j < width; j++) {
                System.out.print(map[j][i] + " ");
            }
        }
    }

    /**
     * draws your inventory
     *
     * @param player the player of the game
     * @param position where you are in the inventory, set to -1 if you don't want a position marker
     */
    public static void drawInventory(Player player, int position) {
    	ArrayList<ArrayList<Item>> inventory = player.getInventory();
        String[] itemList = new String[inventory.size()];
        if (itemList.length == 0) {
            String[] itemListEmpty = {"~ Empty :( ~"};
            drawMenu(itemListEmpty);
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                itemList[i] = stackToString(inventory.get(i));
                if (inventory.get(i).get(0) == player.getEquippedItem()) {
                	itemList[i] += " (E)";
                }
            }
            if (position != -1) {
            	itemList[position-1] = itemList[position-1] + " <";
            }
            drawMenu(itemList);
        }
    }

    /**
     * draws the crafting menu 
     *
     * @param crafter the crafting tile that the recipes belong to
     * @param position where you are in the crafting menu
     */
    public static void drawCraftingMenu(CraftTile crafter, int position) {
    	ArrayList<CraftingRecipe> recipes = crafter.getRecipes();
    	String[] readableRecipes = new String[recipes.size()];
    	for(int i = 0; i < readableRecipes.length; i++) {
    		readableRecipes[i] = String.format("%d %s:", recipes.get(i).getQuantity(), recipes.get(i).getItem().getItemName());
    		for (int[] ingredient : recipes.get(i).getRecipe()) {
    			String ingredientName = itemReader.readItem(ingredient[0]).getItemName();
    			readableRecipes[i] = String.format("%s %s x%d", readableRecipes[i], ingredientName, ingredient[1]);
    		}
    	}
    	readableRecipes[position-1] = String.format("%s %s", readableRecipes[position-1], "<");
    	drawMenu(readableRecipes);
    }

    /**
     * generic menu drawer, to be applied for item and possibly turn based
     * combat menu drawing. given an array of text, it will create a nice
     * looking menu for the text
     * 
     * @param textListed the list of strings that are listed in this menu
     */
    public static void drawMenu(String[] textListed) {
        int longestStringLength = 0;
        for (int i = 0; i < textListed.length; i++) {
            if(textListed[i].length() > longestStringLength) {
                longestStringLength = textListed[i].length();
            }
        }
        System.out.print("+");
        for (int i = 0; i < (longestStringLength); i++) {
            System.out.print("-");
        }
        System.out.println("+");
        for (int i = 0; i < textListed.length; i++) {
            System.out.print("|");
            System.out.print(textListed[i]);
            for(int j = 0; j < (longestStringLength - textListed[i].length()); j++) {
                System.out.print(" ");
            }
            System.out.println("|");
        }
        System.out.print("+");
        for (int i = 0; i < (longestStringLength); i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }

    /**
     * checks if a player has left the border of their current space, where they should transition to a different one
     * 
     * @param xCoord the horizontal position of the character, where 1 unit = 1 tile
     * @param yCoord the vertical position of the character, where 1 unit = 1 tile
     * @param cardinalityVal the direction being moved indicated by a number, where north = 0, east = 1, ect...
     * @param currentSpace the current space the player is in
     * @return the direction of the transition, where 0 = north, 1 = east, ect. -1 = no transition or invalid user input
     */
    public static int checkForSpaceTransition(int xCoord, int yCoord,
            int cardinalityVal, Space currentSpace) {
    	if (cardinalityVal == 0 && yCoord == currentSpace.getLength() - 1) {
            return 0;
        } else if (cardinalityVal == 1 && xCoord == currentSpace.getWidth() - 1) {
        	return 1;
        } else if (cardinalityVal == 2 && yCoord <= 0) {
            return 2;
        } else if (cardinalityVal == 3 && xCoord <= 0) {
        	return 3;
        } else {
        	return -1;
        }
    }

    /**
     * converts a user input like up or east into a cardinalityVal, where north = 0, east = 1, ect...
     *
     * @param direction a string like up or north, representing a direction. if the string doesn't, -1 will be returned
     * @return the cardinalityVal representing the direction the user inputted
     */
    public static int directionToCardinalityVal(String direction) {
    	if (direction.matches("(up)|(north)")) {
            return 0;
        } else if (direction.matches("(right)|(east)")) {
        	return 1;
        } else if (direction.matches("(down)|(south)")) {
            return 2;
        } else if (direction.matches("(left)|(west)")) {
        	return 3;
        } else {
        	return -1;
        }
    }

    /**
     * converts an inventory stack into a string
     *
     * @param stack a stack of items 
     * @return a string representing the type and amount of the item that are in the stack
     */
    public static String stackToString(ArrayList<Item> stack) {
    	int amount = 0;
    	for (Item item : stack) {
    		amount++;
    	}
    	if (amount > 1) {
    		return stack.get(0).getItemName() + " x" + amount;
    	} else {
    		return stack.get(0).getItemName();
    	}
    }

    /**
     * defines the interaction that happens when a player walks into an impermeable tile
     *
     * @param world the current state of the world
     * @param xCoord the x coordinant of the player in the current space
     * @param yCoord the y coordinant of the player in the current space
     * @return the readable output given to describe the interaction
     */
    public static String interactWithBlock(World world, int xCoord, int yCoord) {
    	String tileName = null;
        boolean canDestroy = false;
        Item equippedItem = world.getPlayer().getEquippedItem();
    	ArrayList<ArrayList<Item>> drops = Player.formatItemList(world.getCurrentSpace().getTile(xCoord, yCoord).getDrops());
    	if (equippedItem != null) {
    		tileName = world.getCurrentSpace().getTile(xCoord, yCoord).getTileName();
    		canDestroy = equippedItem.useItem(world, xCoord, yCoord);
        }
        String textOutput = world.getCurrentSpace().getTile(xCoord, yCoord).getTileDesc();
       	if (canDestroy) {
       		textOutput = "The " + tileName + " was vanquished!";
    		if (drops.size() != 0) {
    			textOutput += "\nYou got " + stackToString(drops.get(0));
    			for (int i = 1; i < drops.size(); i++) {
    				textOutput += ", " + stackToString(drops.get(i));
    			}
    		} else {
            	textOutput += "\nYou got nothing :(";
        	}
    	} else if (world.getCurrentSpace().getTile(xCoord, yCoord).getTileID() % 10 == 2) {
    		CraftTile craftTile = (CraftTile)world.getCurrentSpace().getTile(xCoord, yCoord);
     		useCraftingMenu(world, craftTile);
       	} else if (equippedItem != null && equippedItem.getItemID() % 10 == 1) {
       		Tool tool = (Tool)equippedItem;
       		if (world.getCurrentSpace().getTile(xCoord, yCoord).getMaterial().equals(tool.getMaterial())) {
       			textOutput = "Your tools not strong enough!";
       		} else {
       			textOutput = "You're using the wrong tool!";
       		}
       		
       	}
       	return textOutput;
    }

    /**
     * attempts to move the player in the given direction
     *
     * @param cardinalityVal the direction you plan to move the player, where 0 = north, 1 = east, ect...
     * @param world the current state of the world before moving
     * @return the output from the action
     */
    public static String movePlayer(int cardinalityVal, World world) {
    	String textOutput;
    	Point currentPoint = world.getPlayer().getSpaceCoords();
    	int xCoord = (int)currentPoint.getX();
    	int yCoord = (int)currentPoint.getY();
    	Point targetPoint = World.getPointByDirection(currentPoint, cardinalityVal);
    	if (checkForSpaceTransition(xCoord, yCoord, cardinalityVal, world.getCurrentSpace()) != -1) {
    		textOutput = moveToSpace(cardinalityVal, world);
    	} else if(checkIfWall(xCoord, yCoord, cardinalityVal, world.getCurrentSpace())) {
    		textOutput = interactWithBlock(world, (int)targetPoint.getX(), (int)targetPoint.getY());
    		if (textOutput == null) {
    			textOutput = world.getCurrentSpace().getDescOfTile(xCoord, yCoord);
    		}
    	} else {
    		world.setSpaceCoords((int)targetPoint.getX(), (int)targetPoint.getY());
    		textOutput = world.getCurrentSpace().getDescOfTile(world.getSpaceX(), world.getSpaceY());
    	}
    	return textOutput;
    }

    /**
     * sends the player from one space to another
     *
     * @param cardinalityVal the direction the player is moving, where north = 0, east = 1, ect...
     * @param world the current state of the world before moving
     * @return the output from this action
     */
    public static String moveToSpace(int cardinalityVal, World world) {
    	String textOutput = null;
    	world.moveSpace(cardinalityVal);
        switch(cardinalityVal) {
        	case 0:
       			if (world.getCurrentSpace().getTile(world.getSpaceX(), 0).getMoveThrough()) {
        			textOutput = interactWithBlock(world, world.getSpaceX(), 0);
        			world.moveSpace(2);
        		} else {
        			world.setSpaceCoords(world.getSpaceX(), 0);
            		textOutput = world.getCurrentSpace().getDescOfTile(world.getSpaceX(), world.getSpaceY());
            	} break;
            case 1:
            	if (world.getCurrentSpace().getTile(0, world.getSpaceY()).getMoveThrough()) { 
            		textOutput = interactWithBlock(world, 0, world.getSpaceY());
            		world.moveSpace(3);
            	} else {
           			world.setSpaceCoords(0, world.getSpaceY());
            		textOutput = world.getCurrentSpace().getDescOfTile(world.getSpaceX(), world.getSpaceY());
            	} break;
            case 2:
            	if (world.getCurrentSpace().getTile(world.getSpaceX(), world.getCurrentSpace().getLength()-1).getMoveThrough()) {
            		textOutput = interactWithBlock(world, world.getSpaceX(), world.getCurrentSpace().getLength()-1);
            		world.moveSpace(0);
            	} else {
            		world.setSpaceCoords(world.getSpaceX(), world.getCurrentSpace().getLength()-1);
            		textOutput = world.getCurrentSpace().getDescOfTile(world.getSpaceX(), world.getSpaceY());
            	} break;
           	case 3:
            	if (world.getCurrentSpace().getTile(world.getCurrentSpace().getWidth()-1, world.getSpaceY()).getMoveThrough()) {
           			textOutput = interactWithBlock(world, world.getCurrentSpace().getWidth()-1, world.getSpaceY());
            		world.moveSpace(1);
            	} else {
            		world.setSpaceCoords(world.getCurrentSpace().getWidth()-1, world.getSpaceY());
            		textOutput = world.getCurrentSpace().getDescOfTile(world.getSpaceX(), world.getSpaceY());
            	} break;
        }
        return textOutput;
    }

    /**
     * opens the crafting menu for the given CraftTile
     *
     * @param world the current state of the world
     * @param crafter the CraftTile the recipes will come from
     */
    public static void useCraftingMenu(World world, CraftTile crafter) {
        int position = 1;
    	String userInput = "";
    	String helpMessage = "type up and down to traverse the inventory\n"
    	+ "type craft to craft the currently selected item\n"
    	+ "type inventory to see what items you current have\n"
    	+ "type help to see this message again\n"
    	+ "and of course, type leave to leave the crafting menu\n";
    	String textOutput = helpMessage;
    	boolean showInventory = false;
    	while(!userInput.equalsIgnoreCase("leave")) {
    		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
            + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("-*=" + crafter.getTileName().toUpperCase() + "=*-");
			drawCraftingMenu(crafter, position);
			if (showInventory) {
				drawInventory(world.getPlayer(), -1);
			}
			System.out.print(textOutput);
			textOutput = "";
			userInput = KBD.nextLine();
			showInventory = false;
			if(userInput.equalsIgnoreCase("up")) {
				if (position == 1) {
					position = crafter.getRecipes().size();
				} else {
					position--;
				}
			} else if (userInput.equalsIgnoreCase("down")) {
				if (position == crafter.getRecipes().size()) {
					position = 1;
				} else {
					position++;
				}
			} else if (userInput.equalsIgnoreCase("craft")) {
				CraftingRecipe recipe = crafter.getRecipes().get(position-1);
				boolean success = craftRecipe(world, recipe);
				if (success) {
					String itemName = recipe.getItem().getItemName();
					int quantity = recipe.getQuantity();
					textOutput = String.format("you got %dx %s\n", quantity, itemName);
				} else {
					textOutput = "you lack the ingredients!\n";
				}
			} else if (userInput.equalsIgnoreCase("inventory")) {
				showInventory = true;
			} else if (userInput.equalsIgnoreCase("help")) {
				textOutput = helpMessage;
			} else {
				textOutput = "Invalid command, please repeat\n";
			}
    	}
    }

    /**
     * removes an item from the given inventory given by its ID number
     *
     * @param itemID the ID of the item your looking for
     * @param inventory the inventory your removing from
     * @return true if removal was successful, false if it could not find the item to remove
     */
    public static boolean removeItemIDFromInventory(int itemID, ArrayList<ArrayList<Item>> inventory) {
    	for (ArrayList<Item> stack : inventory) {
    		if (stack.get(0).getItemID() == itemID) {
    			stack.remove(0);
    			if (stack.size() == 0) {
    				inventory.remove(stack);
    			}
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * checks if the given inventory has the ingredients to craft the item
     *
     * @param inventory the inventory your checking for ingredients
     * @param recipe the recipe your checking if you can craft
     * @return whether the item was successfully crafted or not
     */
    public static boolean InventoryCanCraft(ArrayList<ArrayList<Item>> inventory, CraftingRecipe recipe) {
    	for (int[] ingredient : recipe.getRecipe()) {
			int itemNeeded = ingredient[0];
			int quantity = ingredient[1];
			while (quantity != 0) {
				if (removeItemIDFromInventory(itemNeeded, inventory)) {
					quantity--;
				} else {
					return false;
				}
			}
		}
		return true;
    }

    /**
     * executes the recipe if the player has all the necessary ingredients
     *
     * @param world the current state of the world
     * @param recipe the recipe you are attempting to execute
     * @return true if the player has all the ingredients to finish the recipe
     */
    public static boolean craftRecipe(World world, CraftingRecipe recipe) {
    	ArrayList<ArrayList<Item>> inventory = world.getInventory();
		if(InventoryCanCraft(inventory, recipe)) {
			for (int[] ingredient : recipe.getRecipe()) {
				world.removeIDFromInventory(ingredient[0]);
			}
			for (int i = 0; i < recipe.getQuantity(); i++) {
				world.addToInventory(recipe.getItem());
			}
			return true;
		}
		return false;
    }

     /**
     * creates a double tile array for a space using map instructions from a text file
     *
     * @param fileName the name of the file you want to read map data from
     * @return a double tile array containing the created map
     */
    public static Tile[][] mapMaker(String fileName) {
        int width = 0,length = 0;
        try {
            File mapInfo = new File(fileName);
            Scanner dimensionFinder = new Scanner(mapInfo);
            dimensionFinder.useDelimiter(",|\n");
            while (!dimensionFinder.next().contains("\r")) {
                width++; //remember to end the last tileID in a row with "," everytime
            }
            try {
                while(true) {
                    dimensionFinder.nextLine();
                    length++;
                }
            }
            catch(NoSuchElementException nsee) {
            }
        }
        catch (Exception e) {
            System.out.println("DIMENSION FINDER ERROR!");
            System.exit(1);
        }
        Tile[][] tileData = new Tile[width][length];
        try {
            File mapInfo = new File(fileName);
            Scanner mapReader = new Scanner(mapInfo);
            mapReader.useDelimiter(",");
            for(int j = (length-1); j >= 0; j--) {
                for(int i = 0; i < width; i++) {
                    tileData[i][j] = tileReader.readTile(mapReader.nextInt());
                }
                mapReader.nextLine();
            }
        }
        catch (Exception e) {
            System.out.println("MAP READER ERROR!");
            System.exit(1);
        }
        return tileData;
    }
}
