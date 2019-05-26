package com.bayviewglen.zork;

class Enemy{
	private int health;
	private int damagePerHit;
	private String name;
	private String dialogueOne;
	private String dialogueTwo;
	private String dialogueThree;
	public Items deathItem;
	
	private final int DEFAULT_HEALTH = 1;
	private final int DEFAULT_DAMAGE_PER_HIT = 1;
	
	
	public Enemy() {
		health = DEFAULT_HEALTH;
		damagePerHit = DEFAULT_DAMAGE_PER_HIT;
		name = "DEFAULT_NAME";
		dialogueOne = "DEFAULT_DIALOGUE";
		dialogueTwo = "DEFAULT_DIALOGUE";
		dialogueThree = "DEFAULT_DIALOGUE";
	}
	
	public String dialogue() {
		int n = (int) (Math.random() * 3);
		
		if(n == 0)
			return dialogueOne;
		else if(n == 1)
			return dialogueTwo;
		else
			return dialogueThree;
	}

	public int getDamagePerHit() {
		return damagePerHit;
	}

	public void setDamagePerHit(int damagePerHit) {
		this.damagePerHit = damagePerHit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public Items getDeathItem() {
		return deathItem;
	}
	
	public void setDeathItem(Items item) {
		deathItem = item;
	}

	public void setDialogueOne(String dialogueOne) {
		this.dialogueOne = dialogueOne;
	}

	public void setDialogueTwo(String dialogueTwo) {
		this.dialogueTwo = dialogueTwo;
	}

	public void setDialogueThree(String dialogueThree) {
		this.dialogueThree = dialogueThree;
	}

}