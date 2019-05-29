package com.bayviewglen.zork;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
 * Author:  Michael Kolling.
 * Version: 1.0
 * Date:    July 1999
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * This class is part of the "Zork" game.
 */
class CommandWords {

	// a constant array that holds all valid command words
	private static HashMap<String, String[]> wordsMap = new HashMap<String, String[]>();

	/**
	 * Constructor - initialise the command words.
	 */
	public CommandWords() {
		try {
			Scanner in = new Scanner(new File("data/Words.dat"));
			while (in.hasNext()) {
				String input = in.nextLine();
				wordsMap.put(input.split(":")[0].trim(), input.split(":")[1].split(", "));
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Check whether a given String is a valid command word. Return true if it is,
	 * false if it isn't.
	 **/
	public static boolean isCommand(String aString) {
		for (String a : wordsMap.get("verb")) {
			if(a.equals(aString))
				return true;
		}
		return false;
	}
	
	public static boolean isDirection(String aString) {
		for (String a : wordsMap.get("direction")) {
			if(a.equals(aString))
				return true;
		}
		return false;
	}
	
	public static boolean isEnemy(String aString) {
		for (String a : wordsMap.get("enemy")) {
			if(a.equals(aString))
				return true;
		}
		return false;
	}
	
	public static boolean isItem(String aString) {
		for (String a : wordsMap.get("item")) {
			if(a.equals(aString))
				return true;
		}
		return false;
	}
	// if we get here, the string was not found in the commands
//		return false;
//	}

	/*
	 * Print all valid commands to System.out.
	 */
	public void showAll() {
		String[] keys = getKeys(wordsMap);
		for (int i = 0; i < keys.length; i++) {
			for (String a : wordsMap.get(keys[i]))
				System.out.println(a);
			// System.out.print(wordsMap.get(keys[i]));
		}
	}

	public String[] getKeys(HashMap<String, String[]> map) {
		String[] keys = new String[map.size()];

		for (int i = 0; i < map.size(); i++) {
			keys[i] = map.keySet().toString().split(",")[i].trim();
		}
		keys[0] = keys[0].substring(1);
		keys[map.size() - 1] = keys[map.size() - 1].substring(0, keys[map.size() - 1].length() - 1);

		return keys;
	}

}
