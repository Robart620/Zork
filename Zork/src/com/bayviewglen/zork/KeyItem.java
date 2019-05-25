package com.bayviewglen.zork;

public class KeyItem extends Items {
	private String itemContents;
	
	public KeyItem() {
		description = "DEFAULT_DESCRIPTION";
		name = "DEFAULT_NAME";
		itemContents = "DEFAULT CONTENTS";
		weight = 0;
				
	}
	
	public KeyItem(String name, String description, String itemContents, int weight) {
		this.description = description;
		this.name = name;
		this.itemContents = itemContents;
		this.weight = weight;
	}
	
	public String readContents() {
		return itemContents;
	}

	
	public String getDescription() {
		return description;
	}
	
	public String getName() {
		return name;
	}

	
	public int getWeight() {
		return weight;
	}
}
