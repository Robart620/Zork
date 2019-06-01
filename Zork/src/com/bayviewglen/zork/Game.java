package com.bayviewglen.zork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private HashMap<String, Room> masterRoomMap;
	private HashMap<String, KeyItem> masterKeyItemMap;
	private HashMap<String, UtilityItem> masterUtilityItemMap;
	private HashMap<String, Enemy> masterEnemyMap;
	public int inventoryWeight = 0;
	public List<Items> inventory = new ArrayList<>();
	private int playerHealth = 100;
	private int gold = 500;
	private Poker poker;

	public final int MAX_INVENTORY_WEIGHT = 100;
	private final String STARTING_ROOM = "GRASSY_KNOLL";
	private final String STARTING_ITEM = "LAMP";

	private void initKeyItems(String fileName) throws Exception {
		masterKeyItemMap = new HashMap<String, KeyItem>();
		Scanner itemScanner;
		try {
			itemScanner = new Scanner(new File(fileName));
			while (itemScanner.hasNext()) {
				KeyItem item = new KeyItem();
				// Read item Name
				String itemName = itemScanner.nextLine();
				item.setName(itemName.split(":")[1].trim());
				// Read item Description
				String itemDescription = itemScanner.nextLine();
				item.setDescription(itemDescription.split(":")[1].replaceAll("<br>", "\n").trim());
				// Read item Weight
				String itemWeight = itemScanner.nextLine();
				item.setWeight(Integer.parseInt(itemWeight.split(": ")[1]));
				// Read item Contents
				String itemContents = itemScanner.nextLine();
				item.setContents(itemContents.split(":")[1]);

				// Puts item into master map of key items.
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

				// Puts item into master map of utility items.
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
					String[] items = item.split(": ")[1].split(",");
					for (String s : items) {
						if (masterKeyItemMap.get(s.toUpperCase().trim().replaceAll(" ", "_")) != null)
							enemy.setDeathItem(masterKeyItemMap.get(s.toUpperCase().trim().replaceAll(" ", "_")));
						else if (masterUtilityItemMap.get(s.toUpperCase().trim().replaceAll(" ", "_")) != null)
							enemy.setDeathItem(masterUtilityItemMap.get(s.toUpperCase().trim().replaceAll(" ", "_")));
						else
							System.out.println("Enemy Item \"" + s + "\" was not found");
					}
				}

				// Puts enemy in master map
				masterEnemyMap.put(enemyName.toUpperCase().substring(5).trim().replaceAll(" ", "_"), enemy);

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
					String[] itemsInRoom = items.split(": ")[1].split(",");
					for (String s : itemsInRoom) {
						s = s.toUpperCase().trim().replaceAll(" ", "_");
						if (masterKeyItemMap.get(s) != null)
							room.itemsList.add(masterKeyItemMap.get(s));
						else if (masterUtilityItemMap.get(s) != null)
							room.itemsList.add(masterUtilityItemMap.get(s));
						else
							System.out.println("Room Item \"" + s + "\" was not found");
					}
				}

				String enemy = roomScanner.nextLine().trim();
				if (enemy.length() > 6) {
					room.roomEnemy = masterEnemyMap.get(enemy.substring(7).trim().toUpperCase().replaceAll(" ", "_"));
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
	 * Create the game and initialise its internal map, utility items, key items and
	 * enemies. Also used to set starting room and starting item.
	 */
	public Game() {
		try {
			initKeyItems("data/KeyItems.dat");
			initUtilityItems("data/UtilityItems.dat");
			initEnemies("data/Enemies.dat");
			initRooms("data/Rooms.dat");
			currentRoom = masterRoomMap.get(STARTING_ROOM);
			inventory.add(masterUtilityItemMap.get(STARTING_ITEM));
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
		// Enter the main command loop. Here we repeatedly read commands and
		// execute them until the game is over.
		boolean finished = false;
		while (!finished && playerHealth > 0) {
			Command command = parser.getCommand();
			finished = processCommand(command);

		}
		if (playerHealth < 1) {
			System.out.println("That went just about as well as expected. You died.");
			System.out.println("Now would be a good time to reset the game and try again.");
		} else {
			System.out.println("Wow...how'd you do that?");
			System.out.println("You defeated the evil Kard Master Kevin!");
			System.out.println("No longer can he sling bad grades at Bill...sadly...");
			System.out.println("Everybody's average is saved");
			System.out.println("Huzzah!");
			System.out.println();
			System.out.println("Goodbye");
		}
	}

	/**
	 * Print out the opening message for the player and read room description.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("Welcome to Dork!");
		System.out.println(
				"Dork is a new, incredibly interesting, exciting, and all around high mark worthy adventure game.");
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
		else if (commandWord.equals("inventory")) {
			System.out.println("Your inventory contains: ");
			System.out.println("\t- " + gold + " gold");
			if (!inventory.isEmpty()) {
				for (Items i : inventory)
					System.out.println("\t- " + i.getName());
				System.out.println();
			}
		} else if (commandWord.equals("drop"))
			drop(command);
		else if (commandWord.equals("go"))
			goRoom(command);
		else if (commandWord.equals("attack")) {
			if (!currentRoom.containsEnemy())
				System.out.println("There is no enemy to attack. Who's really the bad guy?");
			else {
				combat(command);
			}
		} else if (commandWord.equals("take")) {
			if (currentRoom.itemsList.isEmpty())
				System.out.println("Use your eyes moron, ain't nothing here!");
			else if (command.hasItemWord())
				take(command);
			else
				System.out.println("What do you want to take?");

		} else if (commandWord.equals("jump")) {
			if (currentRoom.getRoomName().equals("Abyss") || currentRoom.getRoomName().equals("Cliff Face")
					|| currentRoom.getRoomName().equals("Top of Cliff") || currentRoom.getRoomName().equals("Bridge")) {
				System.out.println(
						"\"Kowabunga it is\" you mutter, right before hurling your body to it's untimely demise below");
				playerHealth = 0;
				return false;
			} else
				System.out.println("You hop on the spot...like an idiot. All your items clunk together as you land.");
			return false;
		}

		else if (commandWord.equals("look")) {
			System.out.println(currentRoom.longDescription());
			if (currentRoom.containsEnemy())
				System.out
						.println(currentRoom.getEnemy().getName() + " is eyeing you down. He clearly wants to tussel.");
			if (!currentRoom.itemsList.isEmpty()) {
				System.out.println("The things around you are: ");
				for (Items s : currentRoom.itemsList) {
					System.out.println("\t- " + s.getName());
				}
			}
			System.out.println("Your inventory contains: ");
			System.out.println("\t- " + gold + " gold");
			if (!inventory.isEmpty()) {
				for (Items i : inventory)
					System.out.println("\t- " + i.getName());
				System.out.println();
			}
		} else if (commandWord.equals("play")) {
			if (currentRoom.getRoomName().equals("Casino")) {
				poker = new Poker(gold);
				gold = poker.play();
			} else
				System.out.println("This is not the time for play you child.");
		} else if (commandWord.equals("eat")) {
			eat(command);
		} else if (commandWord.equals("quit")) {
			if (command.hasDirectionWord())
				System.out.println("Quit what?");
			else
				return true; // signal that we want to quit
		} else if (commandWord.equals("eat")) {
			System.out.println("Do you really think you should be eating at a time like this?");
		}
		return false;
	}

	/**
	 * allows player to pick up items from the room they are in.
	 * 
	 * @param command
	 * @return whether or not the game has finished by the player picking up Kard
	 *         Master Kevin's Head.
	 */
	private boolean take(Command command) {
		for (Items i : currentRoom.itemsList) {
			if (command.getItem().equals((i.getName().toLowerCase()))) {
				if (inventoryWeight + i.getWeight() < MAX_INVENTORY_WEIGHT) {
					currentRoom.itemsList.remove(i);
					inventory.add(i);
					System.out.println("you pick up the " + i.getName());
					if (i.getName().equals("Kard Master Kevin's Head"))
						return true;
					else
						return false;
				} else {
					System.out.println("The " + i.getName() + " is too heavy... weakling");
					return false;
				}
			} else
				System.out.println("That item is not here.");
			return false;
		}
		return false;
	}

	/**
	 * Player combat with enemy in room they are in. When enemy dies, it drops an
	 * item and is removed from the room.
	 * 
	 * @param command
	 */
	private void combat(Command command) {
		if (command.hasItemWord() && !inventory.isEmpty()) {
			for (Items i : inventory) {
				if (command.getItem().equals(i.getName().toLowerCase()) && i instanceof UtilityItem) {
					try {
						UtilityItem item = (UtilityItem) i;
						currentRoom.getEnemy().setHealth(currentRoom.getEnemy().getHealth() - item.getPower());
						System.out.println(
								"You strike " + currentRoom.getEnemy().getName() + " with your " + command.getItem());
						playerHealth -= currentRoom.getEnemy().getDamagePerHit();
						System.out.println(currentRoom.getEnemy().getName() + (" hits back!"));
						System.out.println("\"" + currentRoom.getEnemy().dialogue() + "\"");
						System.out.println("You have " + playerHealth + " HP.");
						if (currentRoom.getEnemy().getHealth() < 1) {
							currentRoom.killEnemy();
						}

					} catch (Exception e) {
						System.out.println("You can't attack with " + i.getName());
					}
				}
			}
		} else
			System.out.println("You've gotta hit em with something you have.");
	}

	/**
	 * Player eats certain KeyItems to increase their health.
	 * 
	 * @param command
	 */
	private void eat(Command command) {
		if (!command.hasFoodItem()) {
			System.out.println("What're you trynna eat, home boy?");
			return;
		}
		String foodItem = command.getFoodItem();
		for (Items i : inventory) {
			if (i instanceof KeyItem && foodItem.equals(i.getName().toLowerCase())) {
				try {
					KeyItem item = (KeyItem) i;
					playerHealth += Integer.parseInt(item.getContents().trim());
					System.out.println("Delicious... You have " + playerHealth + "HP.");
					inventory.remove(i);
					return;
				} catch (Exception e) {
					System.out.println("You can't eat " + i.getName() + "... thought that was somewhat obvious");
					return;
				}

			}
		}
	}

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
	 * Player drops stuff from their inventory into the room they are in.
	 * 
	 * @param command
	 */
	private void drop(Command command) {
		if (command.hasItemWord() && !inventory.isEmpty()) {
			for (/* Items i : inventory */int i = 0; i < inventory.size(); i++) {
				if (inventory.get(i).getName().toLowerCase().equals(command.getItem())) {
					System.out.println("You drop your " + inventory.get(i).getName());
					currentRoom.itemsList.add(inventory.get(i));
					inventory.remove(inventory.get(i));
					return;

				} 
			}
		} else
			System.out.println("You have nothing in your inventory");
	}

	/**
	 * Try to go to one direction. If there is an exit, enter the new room,
	 * otherwise print an error message.
	 */
	private void goRoom(Command command) {
		if (!command.hasDirectionWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println("Go where?");
			return;
		}
		String direction = command.getDirection();
		// Try to leave current room.
		Room nextRoom = currentRoom.nextRoom(direction);
		if (nextRoom == null)
			System.out.println("There is no door!");
		else {
			currentRoom = nextRoom;
			System.out.println(currentRoom.longDescription());
		}
	}
}