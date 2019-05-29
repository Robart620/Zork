package com.bayviewglen.zork;

/**
 * Class Command - Part of the "Zork" game.
 * 
 * author: Michael Kolling version: 1.0 date: July 1999
 *
 * This class holds information about a command that was issued by the user. A
 * command currently consists of two strings: a command word and a second word
 * (for example, if the command was "take map", then the two strings obviously
 * are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid command
 * words. If the user entered an invalid command (a word that is not known) then
 * the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 *
 * The second word is not checked at the moment. It can be anything. If this
 * game is extended to deal with items, then the second part of the command
 * should probably be changed to be an item rather than a String.
 */
class Command {
	private String commandWord;
	private String direction;
	private String item;
	private String enemy;

	/**
	 * Create a command object. First and second word must be supplied, but either
	 * one (or both) can be null. The command word should be null to indicate that
	 * this was a command that is not recognised by this game.
	 */
	public Command(String commandWord, String direction, String item, String enemy) {
		this.commandWord = commandWord;
		this.direction = direction;
		this.item = item;
		this.enemy = enemy;
	}

	public String getCommandWord() {
		return commandWord;
	}

	public String getDirection() {
		return direction;
	}

	public String getItem() {
		return item;
	}

	public String getEnemy() {
		return enemy;
	}

	/**
	 * Return true if this command was not understood.
	 */
	public boolean isUnknown() {
		return (commandWord == null);
	}

	/**
	 * Return true if the command has a second word.
	 */
	public boolean hasDirectionWord() {
		return (direction != null);
	}

	/**
	 * Return true if the command has a target word.
	 */

	public boolean hasItemWord() {
		return (item != null);
	}
	
	public boolean hasEnemyWord() {
		return (enemy != null);
	}

	public boolean hasFoodItem() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getFoodItem() {
		// TODO Auto-generated method stub
		return null;
	}

}
