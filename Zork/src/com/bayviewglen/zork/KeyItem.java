package com.bayviewglen.zork;

/**
 * Class KeyItems - an extension of the items class that holds items which
 * advance the state of the game through their possession.
 * 
 * @author rpurcaru
 *
 */
class KeyItem extends Items {
	private String contents;

	public KeyItem() {
		description = "DEFAULT_DESCRIPTION";
		name = "DEFAULT_NAME";
		contents = "DEFAULT CONTENTS";
		weight = 0;

	}

	public KeyItem(String name, String description, String itemContents, int weight) {
		this.description = description;
		this.name = name;
		contents = itemContents;
		this.weight = weight;
	}

	public String getContents() {
		return contents;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
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

	public void setContents(String contents) {
		this.contents = contents;
	}

}
