package com.bayviewglen.zork;

import java.util.HashMap;
import java.util.List;

public class Combat {

	public static boolean isInCombat = false;
	public static Enemy enemy;
	public static HashMap<String, Items> inventory;
	public int playerHealthChange;
	
	public Combat(Enemy enemy, HashMap inventory) {
		this.enemy = enemy;
		this.inventory = inventory;
	}
	
	public void doCombat() {
		//TODO implement player combat using inputs from parser and commands.
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
			playerHealthChange -= 5;
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
