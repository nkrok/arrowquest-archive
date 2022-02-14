package com.cm8check.arrowquest.player;

import net.minecraft.item.ItemStack;

public class SkillTreeEntryType{
	public int type;
	
	public ItemStack itemstack;
	
	public String statName;
	public String description;
	public int statIncrementAmount;
	public boolean statState;
	
	/**
	 * Creates a SkillTreeEntryType which gives a specified ItemStack upon learning
	 */
	public SkillTreeEntryType(ItemStack stack){
		this.type = 0;
		this.itemstack = stack;
	}
	
	/**
	 * Creates a SkillTreeEntryType which increments a specified NBT byte by a specified amount upon learning
	 */
	public SkillTreeEntryType(String stat, int amount){
		this.type = 1;
		this.statName = stat;
		this.statIncrementAmount = amount;
	}
	
	/**
	 * Creates a SkillTreeEntryType which changes a specified NBT boolean to a specified value (true or false) upon learning
	 */
	public SkillTreeEntryType(String nbt, boolean state, String desc){
		this.type = 2;
		this.statName = nbt;
		this.statState = state;
		this.description = desc;
	}
	
	/**
	 * Creates a SkillTreeEntryType with no defined action to perform upon learning
	 */
	public SkillTreeEntryType(){
		this.type = 3;
	}
}