package com.bayviewglen.zork;

public class UtilityItem extends Items{
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
	
	public String getDescription() {
		return description;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPower() {
		return power;
	}
	
	public int getWeight() {
		return weight;
	}
}
