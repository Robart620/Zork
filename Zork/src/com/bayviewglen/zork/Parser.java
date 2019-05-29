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
		boolean hasVerb = false;
		boolean hasDirection = false;
		boolean hasEnemy = false;
		boolean hasItem = false;

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
		//System.out.println(givenWords);
		// showCommands();

		ArrayList<String> finalWords = new ArrayList<String>();

		for (int i = 0; i < givenWords.size(); i++) {
			if (CommandWords.isCommand(givenWords.get(i))) {
				finalWords.add(givenWords.get(i));
				hasVerb = true;
				i = givenWords.size();
			}		
		}
		if (!hasVerb)
			finalWords.add(null);
		
		for (int i = 0; i < givenWords.size(); i++) {
			if (CommandWords.isDirection(givenWords.get(i))) {
				finalWords.add(givenWords.get(i));
				hasDirection = true;
				i = givenWords.size();
			}		
		}
		if (!hasDirection)
			finalWords.add(null);
		
		for (int i = 0; i < givenWords.size(); i++) {
			if (CommandWords.isEnemy(givenWords.get(i))) {
				finalWords.add(givenWords.get(i));
				hasEnemy = true;
				i = givenWords.size();
			}		
		}
		if (!hasEnemy)
			finalWords.add(null);
		
		for (int i = 0; i < givenWords.size(); i++) {
			if (CommandWords.isItem(givenWords.get(i))) {
				finalWords.add(givenWords.get(i));
				hasItem = true;
				i = givenWords.size();
			}		
		}
		if (!hasItem)
			finalWords.add(null);
		
		//System.out.println(finalWords);

		
		commandWord = finalWords.get(0);
		direction = finalWords.get(1);
		enemy = finalWords.get(2);
		item = finalWords.get(3);

	
		// note: we just ignore the rest of the input line.
		// Now check whether this word is known. If so, create a command
		// with it. If not, create a "nil" command (for unknown command)
		
			return new Command(commandWord, direction, item, enemy);
		
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
		// System.out.println(map);
		return map;

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
