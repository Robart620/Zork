package com.bayviewglen.zork;

public abstract class Items {
	public int weight;
	public String description;
	public String name;
	
	public abstract int getWeight();
	public abstract String getDescription();
	public abstract String getName();

	public abstract void setWeight(int weight);
	public abstract void setName(String name);
	public abstract void setDescription(String description);

	//Test Comment
}



