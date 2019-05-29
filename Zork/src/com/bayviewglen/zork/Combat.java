package com.bayviewglen.zork;

import java.util.HashMap;
import java.util.List;

public class Combat {
	
	private Parser parser;
	public static boolean isInCombat = false;
	public static Enemy enemy;
	public static List inventory;
	public int playerHealth;
	
	public Combat(Enemy enemy, List inventory, int health) {
		this.enemy = enemy;
		this.inventory = inventory;
		this.playerHealth = health;
		parser = new Parser();
	}
	
	public int doCombat() {
		while(playerHealth > 0 && enemy.getHealth() > 0) {
			Command command = parser.getCommand();
			//TODO 
		}
		return playerHealth;
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
			System.out.println(
					"Using your mighty " + weapon.getName() + ", you mess up " + enemy.getName() + "a little.");
			enemy.setHealth(enemy.getHealth() - weapon.getPower());
		}

		if (enemy.getHealth() > 0) {
			System.out.println(enemy.getName() + "Hits back. Maybe another hit'll do it?");
		}
	}

	/**
	 * Takes place when player does not take offensive action when an enemy is
	 * present.
	 * 
	 * 
	 * @param enemy
	 */
	private int defensiveCombat(Enemy enemy) {
		System.out.println("While you sat pondering the mysteries of the universe, " + enemy.getName()
				+ " attacked you. Try to put up a fight maybe?");
		return enemy.getDamagePerHit();
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
