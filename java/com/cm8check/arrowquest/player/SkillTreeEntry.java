package com.cm8check.arrowquest.player;

import net.minecraft.item.ItemStack;

public class SkillTreeEntry{
	public String requirement;
	public String displayName;
	public SkillTreeEntryType skillType;
	public String codeName;
	
	public SkillTreeEntry(String req, String name, SkillTreeEntryType type, String codename){
		this.requirement = req;
		this.displayName = name;
		this.skillType = type;
		this.codeName = codename;
		
		SkillTree.skills.add(this);
	}
}