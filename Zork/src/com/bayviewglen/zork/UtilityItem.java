package com.bayviewglen.zork;

/**
 * Class UtilityItems - an extension of the items class that holds items which
 * the user can use to figh enemies.
 * 
 * Each utility item has an int power which is the amount of damage it can do to
 * the enemy it is being used on.
 * 
 * @author rpurcaru
 *
 */

class UtilityItem extends Items {
	public int power;

	public UtilityItem() {
		power = 0;
		description = "DEFAULT_DESCRIPTION";
		name = "DEFAULT_NAME";
		weight = 0;
	}

	public UtilityItem(String name, String description, int power, int weight) {
		power = 0;
		description = "DEFAULT_DESCRIPTION";
		name = "DEFAULT_NAME";
		weight = 0;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getPower() {
		return power;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public void setPower(int power) {
		this.power = power;
	}
}
