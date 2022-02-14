package com.cm8check.arrowquest.player;

import java.util.ArrayList;
import java.util.List;

import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SkillTree{
	public static List<SkillTreeEntry> skills = new ArrayList<SkillTreeEntry>();
	
	public static void createSkillTree(){
		SkillTreeEntry atkdmg1 = new SkillTreeEntry("", "+1 Attack Damage", new SkillTreeEntryType(ModLib.nbtPlayerATK, 1), "atkdmg1");
		SkillTreeEntry atkdmg2 = new SkillTreeEntry("atkdmg1", "+1 Attack Damage", new SkillTreeEntryType(ModLib.nbtPlayerATK, 1), "atkdmg2");
		SkillTreeEntry atkdmg3 = new SkillTreeEntry("atkdmg2", "+1 Attack Damage", new SkillTreeEntryType(ModLib.nbtPlayerATK, 1), "atkdmg3");
		SkillTreeEntry atkdmg4 = new SkillTreeEntry("atkdmg3", "+1 Attack Damage", new SkillTreeEntryType(ModLib.nbtPlayerATK, 1), "atkdmg4");
		SkillTreeEntry atkdmg5 = new SkillTreeEntry("atkdmg4", "+1 Attack Damage", new SkillTreeEntryType(ModLib.nbtPlayerATK, 1), "atkdmg5");
		SkillTreeEntry atkdmg6 = new SkillTreeEntry("atkdmg5", "+2 Attack Damage", new SkillTreeEntryType(ModLib.nbtPlayerATK, 2), "atkdmg6");
		SkillTreeEntry axedmg = new SkillTreeEntry("atkdmg1", "+50% Axe Damage", new SkillTreeEntryType(ModLib.nbtPlayerAxeExtraDamage, true, ""), "axedmg");
		SkillTreeEntry shoveldmg = new SkillTreeEntry("atkdmg1", "+50% Shovel Damage", new SkillTreeEntryType(ModLib.nbtPlayerShovelExtraDamage, true, ""), "shoveldmg");
		SkillTreeEntry pickdmg = new SkillTreeEntry("atkdmg1", "+50% Pickaxe Damage", new SkillTreeEntryType(ModLib.nbtPlayerPickaxeExtraDamage, true, ""), "pickdmg");
		SkillTreeEntry bowdmg = new SkillTreeEntry("atkdmg1", "+10% Bow Damage", new SkillTreeEntryType(ModLib.nbtPlayerBowExtraDamage, true, ""), "bowdmg");
		SkillTreeEntry bowdmg2 = new SkillTreeEntry("atkdmg6", "+15% Bow Damage", new SkillTreeEntryType(ModLib.nbtPlayerBowExtraDamage2, true, ""), "bowdmg2");
		SkillTreeEntry swordxp = new SkillTreeEntry("atkdmg2", "Sword XP Leech", new SkillTreeEntryType(ModLib.nbtPlayerSwordXP, true, "Hitting enemies with a sword grants XP"), "swordxp");
		SkillTreeEntry enderSword = new SkillTreeEntry("swordxp", "Get: Ender Sword", new SkillTreeEntryType(new ItemStack(ModItems.itemEnderSword)), "enderSword");
		SkillTreeEntry axexp = new SkillTreeEntry("axedmg", "Axe XP Leech", new SkillTreeEntryType(ModLib.nbtPlayerAxeXP, true, "Hitting enemies with an axe grants XP"), "axexp");
		SkillTreeEntry shovelxp = new SkillTreeEntry("shoveldmg", "Shovel XP Leech", new SkillTreeEntryType(ModLib.nbtPlayerShovelXP, true, "Hitting enemies with a shovel grants XP"), "shovelxp");
		SkillTreeEntry pickxp = new SkillTreeEntry("pickdmg", "Pickaxe XP Leech", new SkillTreeEntryType(ModLib.nbtPlayerPickaxeXP, true, "Hitting enemies with a pickaxe grants XP"), "pickxp");
		SkillTreeEntry thiefsDagger = new SkillTreeEntry("atkdmg3", "Get: Thief's Dagger", new SkillTreeEntryType(new ItemStack(ModItems.itemThiefsDagger)), "thiefsDagger");
		SkillTreeEntry scimitar = new SkillTreeEntry("thiefsDagger", "Get: Scimitar", new SkillTreeEntryType(new ItemStack(ModItems.itemScimitar)), "scimitar");
		
		SkillTreeEntry def1 = new SkillTreeEntry("", "+5% Damage Resistance", new SkillTreeEntryType(ModLib.nbtPlayerDEF, 1), "def1");
		SkillTreeEntry def2 = new SkillTreeEntry("def1", "+5% Damage Resistance", new SkillTreeEntryType(ModLib.nbtPlayerDEF, 1), "def2");
		SkillTreeEntry def3 = new SkillTreeEntry("def2", "+5% Damage Resistance", new SkillTreeEntryType(ModLib.nbtPlayerDEF, 1), "def3");
		SkillTreeEntry def4 = new SkillTreeEntry("def3", "+5% Damage Resistance", new SkillTreeEntryType(ModLib.nbtPlayerDEF, 1), "def4");
		SkillTreeEntry def5 = new SkillTreeEntry("def4", "+5% Damage Resistance", new SkillTreeEntryType(ModLib.nbtPlayerDEF, 1), "def5");
		SkillTreeEntry def6 = new SkillTreeEntry("maxhp10", "+5% Damage Resistance", new SkillTreeEntryType(ModLib.nbtPlayerDEF, 1), "def6");
		SkillTreeEntry def7 = new SkillTreeEntry("def6", "+5% Damage Resistance", new SkillTreeEntryType(ModLib.nbtPlayerDEF, 1), "def7");
		
		SkillTreeEntry maxhp1 = new SkillTreeEntry("def1", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp1");
		SkillTreeEntry maxhp2 = new SkillTreeEntry("maxhp1", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp2");
		SkillTreeEntry maxhp3 = new SkillTreeEntry("maxhp2", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp3");
		SkillTreeEntry maxhp4 = new SkillTreeEntry("maxhp3", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp4");
		SkillTreeEntry maxhp5 = new SkillTreeEntry("maxhp4", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp5");
		SkillTreeEntry maxhp6 = new SkillTreeEntry("maxhp5", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp6");
		SkillTreeEntry maxhp7 = new SkillTreeEntry("maxhp6", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp7");
		SkillTreeEntry maxhp8 = new SkillTreeEntry("maxhp7", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp8");
		SkillTreeEntry maxhp9 = new SkillTreeEntry("maxhp8", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp9");
		SkillTreeEntry maxhp10 = new SkillTreeEntry("maxhp9", "+2 Max HP", new SkillTreeEntryType(ModLib.nbtPlayerMaxHP, 2), "maxhp10");
		
		SkillTreeEntry selfheal3 = new SkillTreeEntry("maxhp10", "Get: Regeneration Medallion", new SkillTreeEntryType(new ItemStack(ModItems.itemMedallion)), "regenMedallion");
		SkillTreeEntry ironChestplate = new SkillTreeEntry("def1", "Get: Iron Chestplate", new SkillTreeEntryType(new ItemStack(Items.iron_chestplate)), "ironChestplate");
		SkillTreeEntry diamondChestplate = new SkillTreeEntry("ironChestplate", "Get: Diamond Chestplate", new SkillTreeEntryType(new ItemStack(Items.diamond_chestplate)), "diamondChestplate");
		SkillTreeEntry lessfiredmg = new SkillTreeEntry("maxhp3", "+50% Fire Damage Resistance", new SkillTreeEntryType(ModLib.nbtPlayerLessFireDamage, true, ""), "lessfiredmg");
		
		SkillTreeEntry hdIngot1 = new SkillTreeEntry("def3", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot1");
		SkillTreeEntry hdIngot2 = new SkillTreeEntry("hdIngot1", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot2");
		SkillTreeEntry hdIngot3 = new SkillTreeEntry("hdIngot2", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot3");
		SkillTreeEntry hdIngot4 = new SkillTreeEntry("hdIngot3", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot4");
		SkillTreeEntry hdIngot5 = new SkillTreeEntry("hdIngot4", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot5");
		SkillTreeEntry hdIngot6 = new SkillTreeEntry("hdIngot5", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot6");
		SkillTreeEntry hdIngot7 = new SkillTreeEntry("hdIngot6", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot7");
		SkillTreeEntry hdIngot8 = new SkillTreeEntry("hdIngot7", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot8");
		SkillTreeEntry hdIngot9 = new SkillTreeEntry("hdIngot8", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot9");
		SkillTreeEntry hdIngot10 = new SkillTreeEntry("hdIngot9", "Get: Heavy-Duty Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemHeavyDutyIngot)), "hdIngot10");
		
		SkillTreeEntry tfIngot1 = new SkillTreeEntry("def5", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot1");
		SkillTreeEntry tfIngot2 = new SkillTreeEntry("tfIngot1", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot2");
		SkillTreeEntry tfIngot3 = new SkillTreeEntry("tfIngot2", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot3");
		SkillTreeEntry tfIngot4 = new SkillTreeEntry("tfIngot3", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot4");
		SkillTreeEntry tfIngot5 = new SkillTreeEntry("tfIngot4", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot5");
		SkillTreeEntry tfIngot6 = new SkillTreeEntry("tfIngot5", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot6");
		SkillTreeEntry tfIngot7 = new SkillTreeEntry("tfIngot6", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot7");
		SkillTreeEntry tfIngot8 = new SkillTreeEntry("tfIngot7", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot8");
		SkillTreeEntry tfIngot9 = new SkillTreeEntry("tfIngot8", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot9");
		SkillTreeEntry tfIngot10 = new SkillTreeEntry("tfIngot9", "Get: Trueforce Ingot", new SkillTreeEntryType(new ItemStack(ModItems.itemTrueforceIngot)), "tfIngot10");
		
		SkillTreeEntry defMedallion = new SkillTreeEntry("def5", "Get: Defense Medallion", new SkillTreeEntryType(new ItemStack(ModItems.itemDefenseMedallion)), "defMedallion");
		SkillTreeEntry foodBonus1 = new SkillTreeEntry("maxhp2", "+25% Food Potency", new SkillTreeEntryType(ModLib.nbtPlayerFoodBonus, 1), "foodBonus1");
		SkillTreeEntry foodBonus2 = new SkillTreeEntry("foodBonus1", "+25% Food Potency", new SkillTreeEntryType(ModLib.nbtPlayerFoodBonus, 1), "foodBonus2");
		
		SkillTreeEntry movespeed1 = new SkillTreeEntry("", "+20% Move Speed", new SkillTreeEntryType(ModLib.nbtPlayerMoveSpeed, 1), "movespeed1");
		SkillTreeEntry movespeed2 = new SkillTreeEntry("movespeed1", "+20% Move Speed", new SkillTreeEntryType(ModLib.nbtPlayerMoveSpeed, 1), "movespeed2");
		SkillTreeEntry movespeed3 = new SkillTreeEntry("movespeed2", "+20% Move Speed", new SkillTreeEntryType(ModLib.nbtPlayerMoveSpeed, 1), "movespeed3");
		SkillTreeEntry jumpheight1 = new SkillTreeEntry("movespeed1", "+50% Jump Height", new SkillTreeEntryType(ModLib.nbtPlayerJumpHeight, 1), "jumpheight1");
		SkillTreeEntry jumpheight2 = new SkillTreeEntry("jumpheight1", "+50% Jump Height", new SkillTreeEntryType(ModLib.nbtPlayerJumpHeight, 1), "jumpheight2");
		SkillTreeEntry jumpheight3 = new SkillTreeEntry("jumpheight2", "+50% Jump Height", new SkillTreeEntryType(ModLib.nbtPlayerJumpHeight, 1), "jumpheight3");
		
		SkillTreeEntry magicdmg1 = new SkillTreeEntry("", "+1 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 1), "magicdmg1");
		SkillTreeEntry magicdmg2 = new SkillTreeEntry("magicdmg1", "+1 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 1), "magicdmg2");
		SkillTreeEntry magicdmg3 = new SkillTreeEntry("magicdmg2", "+1 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 1), "magicdmg3");
		SkillTreeEntry magicdmg4 = new SkillTreeEntry("magicdmg3", "+1 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 1), "magicdmg4");
		SkillTreeEntry magicdmg5 = new SkillTreeEntry("magicdmg4", "+1 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 1), "magicdmg5");
		SkillTreeEntry magicdmg6 = new SkillTreeEntry("magicdmg5", "+2 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 2), "magicdmg6");
		SkillTreeEntry magicdmg7 = new SkillTreeEntry("magicdmg6", "+2 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 2), "magicdmg7");
		SkillTreeEntry magicdmg8 = new SkillTreeEntry("magicdmg7", "+1 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 1), "magicdmg8");
		SkillTreeEntry magicdmg9 = new SkillTreeEntry("magicdmg8", "+1 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 1), "magicdmg9");
		SkillTreeEntry magicdmg10 = new SkillTreeEntry("magicdmg9", "+1 Magic Damage", new SkillTreeEntryType(ModLib.nbtPlayerMagicDMG, 1), "magicdmg10");
		
		SkillTreeEntry lavapuddle = new SkillTreeEntry("magicdmg2", "Get Scroll: Lava Puddle", new SkillTreeEntryType(new ItemStack(ModItems.itemScroll, 1, 14)), "lavapuddle");
		SkillTreeEntry magicbp = new SkillTreeEntry("magicdmg3", "Get: Magic Backpack", new SkillTreeEntryType(ModLib.nbtPlayerHasMagicBackpack, true, "Gain a magic backpack (default key to open is [B])"), "magicbp");
		SkillTreeEntry selfheal2 = new SkillTreeEntry("magicbp", "Get Scroll: Self Heal II", new SkillTreeEntryType(new ItemStack(ModItems.itemScroll, 1, 8)), "selfheal2");
		SkillTreeEntry selfheal3b = new SkillTreeEntry("magicbp", "Get Scroll: Self Heal III", new SkillTreeEntryType(new ItemStack(ModItems.itemScroll, 1, 9)), "selfheal3b");
		SkillTreeEntry aoeheal2 = new SkillTreeEntry("magicdmg5", "Get Scroll: Heal Aura II", new SkillTreeEntryType(new ItemStack(ModItems.itemScroll, 1, 11)), "aoeheal2");
		SkillTreeEntry aoeheal3 = new SkillTreeEntry("aoeheal2", "Get Scroll: Heal Aura III", new SkillTreeEntryType(new ItemStack(ModItems.itemScroll, 1, 12)), "aoeheal3");
		
		System.out.println("SkillTree length: " + skills.size());
	}
	
	public static List calculateSkillOptions(EntityPlayer player){
		List<Byte> skillList = new ArrayList<Byte>();
		
		NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
		if (nbt.hasKey(ModLib.nbtPlayerSkills)){
			List<String> skillsUnlocked = new ArrayList<String>();
			
			NBTTagList taglist = nbt.getTagList(ModLib.nbtPlayerSkills, 10);
			for (int i = 0; i < taglist.tagCount(); i++){
				String str = taglist.getCompoundTagAt(i).getString("skillName");
				skillsUnlocked.add(str);
			}
			
			for (int i = 0; i < SkillTree.skills.size(); i++){
				SkillTreeEntry skill = SkillTree.skills.get(i);
				if (!skillsUnlocked.contains(skill.codeName) && (skill.requirement == "" || skillsUnlocked.contains(skill.requirement))){
					skillList.add((byte) i);
				}
			}
		}else{
			nbt.setTag(ModLib.nbtPlayerSkills, new NBTTagList());
		}
		
		return skillList;
	}
}