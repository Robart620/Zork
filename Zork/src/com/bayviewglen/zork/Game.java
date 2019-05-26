package com.bayviewglen.zork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author: Michael Kolling Version: 1.1 Date: March 2000
 * 
 * This class is the main class of the "Zork" application. Zork is a very
 * simple, text based adventure game. Users can walk around some scenery. That's
 * all. It should really be extended to make it more interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * routine.
 * 
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates the commands that
 * the parser returns.
 */
class Game {
	private Parser parser;
	private Room currentRoom;
	// This is a MASTER object that contains all of the rooms and is easily
	// accessible.
	// The key will be the name of the room -> no spaces (Use all caps and
	// underscore -> Great Room would have a key of GREAT_ROOM
	// In a hashmap keys are case sensitive.
	// masterRoomMap.get("GREAT_ROOM") will return the Room Object that is the Great
	// Room (assuming you have one)
	private HashMap<String, Room> masterRoomMap;
	private HashMap<String, KeyItem> masterKeyItemMap;
	private HashMap<String, UtilityItem> masterUtilityItemMap;
	private HashMap<String, Enemy> masterEnemyMap;
	public int inventoryWeight;
	public int playerHealth = 100;

	public final int MAX_INVENTORY_WEIGHT = 100;
	private final String STARTING_ROOM = "GRASSY_KNOLL";

	private void initKeyItems(String fileName) throws Exception {
		masterKeyItemMap = new HashMap<String, KeyItem>();
		Scanner itemScanner;
		try {
			itemScanner = new Scanner(new File(fileName));
			while (itemScanner.hasNext()) {
				KeyItem item = new KeyItem();
				// Read the Name
				String itemName = itemScanner.nextLine();
				item.setName(itemName.split(":")[1].trim());
				// Read the Description
				String itemDescription = itemScanner.nextLine();
				item.setDescription(itemDescription.split(":")[1].replaceAll("<br>", "\n").trim());
				// Read the Weight
				String itemWeight = itemScanner.nextLine();
				item.setWeight(Integer.parseInt(itemWeight.split(": ")[1]));
				// Read item Contents
				String itemContents = itemScanner.nextLine();
				item.setContents(itemContents.split(":")[1]);

				masterKeyItemMap.put(itemName.toUpperCase().substring(10).trim().replaceAll(" ", "_"), item);

			}
			itemScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initUtilityItems(String fileName) throws Exception {
		masterUtilityItemMap = new HashMap<String, UtilityItem>();
		Scanner itemScanner;
		try {
			itemScanner = new Scanner(new File(fileName));
			while (itemScanner.hasNext()) {
				UtilityItem item = new UtilityItem();
				// Read the Name
				String itemName = itemScanner.nextLine();
				item.setName(itemName.split(":")[1].trim());
				// Read the Description
				String itemDescription = itemScanner.nextLine();
				item.setDescription(itemDescription.split(":")[1].replaceAll("<br>", "\n").trim());
				// Read the Weight
				String itemWeight = itemScanner.nextLine();
				item.setWeight(Integer.parseInt(itemWeight.split(": ")[1]));
				// Read item Contents
				String itemPower = itemScanner.nextLine();
				item.setPower(Integer.parseInt(itemPower.split(": ")[1]));

				masterUtilityItemMap.put(itemName.toUpperCase().substring(10).trim().replaceAll(" ", "_"), item);

			}
			itemScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initEnemies(String fileName) throws Exception {
		masterEnemyMap = new HashMap<String, Enemy>();
		Scanner enemyScanner;
		try {
			enemyScanner = new Scanner(new File(fileName));
			while (enemyScanner.hasNext()) {
				Enemy enemy = new Enemy();
				// Read the Name
				String enemyName = enemyScanner.nextLine();
				enemy.setName(enemyName.split(":")[1].trim());
				// Read Health
				String enemyHealth = enemyScanner.nextLine();
				enemy.setHealth(Integer.parseInt(enemyHealth.split(": ")[1]));
				// Read Damage Per Hit
				String enemyDamagePerHit = enemyScanner.nextLine();
				enemy.setDamagePerHit(Integer.parseInt(enemyDamagePerHit.split(": ")[1]));
				// Read Enemy Dialogue
				String allEnemyDialogue = enemyScanner.nextLine();
				String[] dialogue = allEnemyDialogue.split(":")[1].split(",");
				enemy.setDialogueOne(dialogue[0].trim());
				enemy.setDialogueTwo(dialogue[1].trim());
				enemy.setDialogueThree(dialogue[2].trim());
				// Read Death Item Name

				String item = enemyScanner.nextLine().trim();
				if (item.length() > 6) {
					String[] items = item.split(":")[1].split(",");
					for (String s : items) {
						if (!(masterKeyItemMap.get(s) == null))
							enemy.setDeathItem(masterKeyItemMap.get(s));
						else if (!(masterKeyItemMap.get(s) == null))
							enemy.setDeathItem(masterUtilityItemMap.get(s));
						else
							System.out.println("Enemy Item \"" + s + "\" was not found");
					}
				}

				masterEnemyMap.put(enemyName.toUpperCase().substring(11).trim().replaceAll(" ", "_"), enemy);

			}
			enemyScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initRooms(String fileName) throws Exception {
		masterRoomMap = new HashMap<String, Room>();
		Scanner roomScanner;
		try {
			HashMap<String, HashMap<String, String>> exits = new HashMap<String, HashMap<String, String>>();
			roomScanner = new Scanner(new File(fileName));
			while (roomScanner.hasNext()) {
				Room room = new Room();
				// Read the Name
				String roomName = roomScanner.nextLine();
				room.setRoomName(roomName.split(":")[1].trim());
				// Read the Description
				String roomDescription = roomScanner.nextLine();
				room.setDescription(roomDescription.split(":")[1].replaceAll("<br>", "\n").trim());
				// Read the Exits
				String roomExits = roomScanner.nextLine();
				// An array of strings in the format E-RoomName
				String[] rooms = roomExits.split(":")[1].split(",");
				HashMap<String, String> temp = new HashMap<String, String>();
				for (String s : rooms) {
					temp.put(s.split("-")[0].trim(), s.split("-")[1]);
				}

				exits.put(roomName.substring(10).trim().toUpperCase().replaceAll(" ", "_"), temp);

				String items = roomScanner.nextLine().trim();
				if (items.length() > 6) {
					String[] itemsInRoom = items.split(":")[1].split(",");
					for (String s : itemsInRoom) {
						if (!(masterKeyItemMap.get(s) == null))
							room.itemsList.add(masterKeyItemMap.get(s));
						else if (!(masterKeyItemMap.get(s) == null))
							room.itemsList.add(masterUtilityItemMap.get(s));
						else
							System.out.println("Room Item \"" + s + "\" was not found");
					}
				}

				String enemy = roomScanner.nextLine().trim();
				if (enemy.length() > 6) {
					room.roomEnemy = masterEnemyMap.get(enemy.substring(6).trim().toUpperCase().replaceAll(" ", "_"));
				}

				// This puts the room we created (Without the exits in the masterMap)
				masterRoomMap.put(roomName.toUpperCase().substring(10).trim().replaceAll(" ", "_"), room);

				// Now we better set the exits.
			}

			for (String key : masterRoomMap.keySet()) {
				Room roomTemp = masterRoomMap.get(key);
				HashMap<String, String> tempExits = exits.get(key);
				for (String s : tempExits.keySet()) {
					// s = direction
					// value is the room.

					String roomName2 = tempExits.get(s.trim());
					Room exitRoom = masterRoomMap.get(roomName2.toUpperCase().replaceAll(" ", "_"));
					roomTemp.setExit(s.trim().charAt(0), exitRoom);

				}
			}

			roomScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the game and initialise its internal map.
	 */
	public Game() {
		try {
			initKeyItems("data/KeyItems.dat");
			initUtilityItems("data/UtilityItems.dat");
			initEnemies("data/Enemies.dat");
			initRooms("data/Rooms.dat");
			currentRoom = masterRoomMap.get(STARTING_ROOM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		parser = new Parser();
	}

	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		printWelcome();
// Enter the main command loop.  Here we repeatedly read commands and
		// execute them until the game is over.

		boolean finished = false;
		while (!finished && playerHealth > 0) {
			Command command = parser.getCommand();
			finished = processCommand(command);
		}
		if (playerHealth < 1)
			System.out.println("You died. Nice.");
		else
			System.out.println("You Win. Thank you for playing.  Good bye.");
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("Welcome to Zork!");
		System.out.println("Zork is a new, incredibly boring adventure game.");
		System.out.println("Type 'help' if you need help.");
		System.out.println();
		System.out.println(currentRoom.longDescription());
	}

	/**
	 * Given a command, process (that is: execute) the command. If this command ends
	 * the game, true is returned, otherwise false is returned.
	 */
	private boolean processCommand(Command command) {
		if (command.isUnknown()) {
			System.out.println("I don't know what you mean...");
			return false;
		}
		String commandWord = command.getCommandWord();
		if (commandWord.equals("help"))
			printHelp();
		else if (commandWord.equals("go"))
			goRoom(command);
		else if (commandWord.equals("quit")) {
			if (command.hasSecondWord())
				System.out.println("Quit what?");
			else
				return true; // signal that we want to quit
		} else if (commandWord.equals("eat")) {
			System.out.println("Do you really think you should be eating at a time like this?");
		}
		return false;
	}

// implementations of user commands:
	/**
	 * Print out some help information. Here we print some stupid, cryptic message
	 * and a list of the command words.
	 */
	private void printHelp() {
		System.out.println("The area has been ravaged.");
		System.out.println("You get the vague feeling you should be doing something...");
		System.out.println();
		System.out.println("Your command words are:");
		parser.showCommands();
	}

	/**
	 * Try to go to one direction. If there is an exit, enter the new room,
	 * otherwise print an error message.
	 */
	private void goRoom(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println("Go where?");
			return;
		}
		String direction = command.getSecondWord();
		// Try to leave current room.
		Room nextRoom = currentRoom.nextRoom(direction);
		if (nextRoom == null)
			System.out.println("There is no door!");
		else {
			currentRoom = nextRoom;
			System.out.println(currentRoom.longDescription());
		}
	}

	/**
	 * Method conducts combat between specified enemy using player selected weapon
	 * and modifies enemy and player stats accordingly.
	 * 
	 * @param enemy  the enemy being fought.
	 * @param weapon the item being used to fight.
	 */
	private void combat(Enemy enemy, UtilityItem weapon) {
		if (enemy.getName() == "Ghost") {
			System.out.println("It's already dead... Idiot");
			return;
		} else if (randomValue(10) == 1) {
			System.out.println("In your haste to attack your foe, you hurt yourself... Moron");
			playerHealth -= 5;
		} else if (randomValue(10) == 1) {
			System.out.println("With a sudden burst of strength, your " + weapon.getName() + " critically damages "
					+ enemy.getName() + ", killing them instantly.");
			enemy.setHealth(0);
		} else {
			System.out.println("Using your mighty " + weapon.getName() + ", you mess up: " + enemy.getName());
			enemy.setHealth(enemy.getHealth() - weapon.getPower());
		}
		
		//TODO enemy damage
	}

	/**
	 * Takes place when player does not take offensive action when an enemy is
	 * present.
	 * 
	 * @param enemy
	 */
	private void defensiveCombat(Enemy enemy) {
		System.out.println("While you sat pondering the mysteries of the universe, " + enemy.getName() + " attacked you.");
		playerHealth -= enemy.getDamagePerHit();
	}

	/**
	 * 
	 * @param num a number greater than 0.
	 * @return a random number between 1 and num, inclusive.
	 */
	private int randomValue(int num) {
		return (int) (Math.random() * num + 1);
	}
}
