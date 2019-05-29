package com.bayviewglen.zork;

/*
 * Author:  Michael Kolling
 * Version: 1.0
 * Date:    July 1999
 * 
 * This class is part of Zork. Zork is a simple, text based adventure game.
 *
 * This parser reads user input and tries to interpret it as a "Zork"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

class Parser {

	private static final String[] nonImportantWords = { "a", "to", "the", "over", "at", "it" };
	private CommandWords commands; // holds all valid command words

	public HashMap<String, String[]> readSynonyms(HashMap<String, String[]> map, String fileName) {
		try {
			Scanner synScan = new Scanner(new File(fileName));
			while (synScan.hasNext()) {
				String input = synScan.nextLine();
				map.put(input.split(":")[0].trim(), input.split(":")[1].split(", "));
			}
			synScan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println(map);
		return map;

	}

	public Parser() {
		commands = new CommandWords();
	}

	public Command getCommand() {

		String inputLine = ""; // will hold the full input line
		String commandWord;
		String direction;
		String item;
		String enemy;
		HashMap<String, String[]> synonymsMap = new HashMap<String, String[]>();
		ArrayList<String> givenWords = new ArrayList<String>();

		synonymsMap = readSynonyms(synonymsMap, "data/Synonyms.dat");
		String[] keys;

		keys = getKeys(synonymsMap);

		System.out.print("> "); // print prompt
		BufferedReader reader =

				new BufferedReader(new InputStreamReader(System.in));
		try {
			inputLine = reader.readLine();
		} catch (java.io.IOException exc) {
			System.out.println("There was an error during reading: " + exc.getMessage());
		}

		/*
		 * take the input line and places it into an array list then gets rid of
		 * unimportant words
		 */
		StringTokenizer tokenizer = new StringTokenizer(inputLine);

		while (tokenizer.hasMoreTokens()) {
			givenWords.add(tokenizer.nextToken());
		}
		givenWords = reducedInput(givenWords);

		for (int i = 0; i < givenWords.size(); i++) {
			givenWords.set(i, givenWords.get(i).toLowerCase());
		}

		for (int i = 0; i < givenWords.size(); i++) {
			for (int j = 0; j < keys.length; j++) {
				if (isSynonym(keys[j], givenWords.get(i), synonymsMap))
					givenWords.set(i, keys[j]);

			}
		}

		System.out.println(givenWords);
		//System.out.println(synonymsMap.keySet());

		Command command = new Command(givenWords);
		
		return command;
		
//		
//		if (tokenizer.hasMoreTokens())
//			commandWord = tokenizer.nextToken(); // get first word
//		else
//			commandWord = null;
//		if (tokenizer.hasMoreTokens())
//			direction = tokenizer.nextToken(); // get second word
//		else
//			direction = null;
//		if (tokenizer.hasMoreTokens())
//			item = tokenizer.nextToken();
//		else
//			item = null;
//		if (tokenizer.hasMoreTokens())
//			enemy = tokenizer.nextToken();
//		else
//			enemy = null;
//		// note: we just ignore the rest of the input line.
//		// Now check whether this word is known. If so, create a command
//		// with it. If not, create a "nil" command (for unknown command).
//		if (commands.isCommand(givenWords))
//			return new Command(commandWord, direction, item, enemy);
//		else
//			return new Command(null, direction, item, enemy);
	}

	/**
	 * Print out a list of valid command words.
	 */
	public void showCommands() {
		commands.showAll();
	}

	/**
	 * takes out all the unimportant words from the input
	 * 
	 * @param input
	 * @return reduced array with only important or unknown words left
	 */

	public ArrayList<String> reducedInput(ArrayList<String> input) {
		for (int i = 0; i < input.size(); i++) {
			for (int j = 0; j < nonImportantWords.length; j++) {
				if (input.get(i).equals(nonImportantWords[j])) {
					input.remove(i);
					i--;
				}
			}
		}
		return input;

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

	public static boolean isSynonym(String commandGroup, String command, HashMap<String, String[]> map) {
		for (String s : map.get(commandGroup)) {
			if (s.equals(command)) {
				return true;
			}
		}
		return false;
	}

}
