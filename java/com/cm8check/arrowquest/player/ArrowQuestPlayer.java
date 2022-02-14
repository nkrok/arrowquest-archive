package com.cm8check.arrowquest.player;

import java.util.Collection;
import java.util.Iterator;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketPlayerLevelUp;
import com.cm8check.arrowquest.network.packet.PacketPlayerXP;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

public class ArrowQuestPlayer{
	public static final int INITIAL_LEVEL_XP = 30;
	
	public static int localPlayerXP = 0;
	public static int localPlayerLevelXP = 100;
	public static int localPlayerLevel = 0;
	public static int localPlayerLevelsToSpend = 0;
	public static int localPlayerATK = 0;
	public static int localPlayerDEF = 0;
	public static int localPlayerSpeed = 0;
	public static int localPlayerJumpHeight = 0;
	public static int localPlayerMagicDMG = 0;
	public static int localPlayerRace = 10;
	public static boolean selectRace = false;
	
	public static void increasePlayerXP(EntityPlayer player, int amount){
		NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
		int newXP = nbt.getInteger(ModLib.nbtPlayerXP) + amount;
		
		//level up?
		int levelXP = nbt.getInteger(ModLib.nbtPlayerLevelXP);
		if (newXP >= levelXP){
			int levelsToSpend = nbt.getByte(ModLib.nbtPlayerLevelsToSpend);
			
			int newLevel = 0;
			while (newXP >= levelXP){
				newXP = newXP-levelXP;
				newLevel = nbt.getShort(ModLib.nbtPlayerLevel) + 1;
				levelXP = (int) (INITIAL_LEVEL_XP * Math.pow(1.22, newLevel));
				nbt.setShort(ModLib.nbtPlayerLevel, (short) newLevel);
				nbt.setInteger(ModLib.nbtPlayerLevelXP, levelXP);
				nbt.setInteger(ModLib.nbtPlayerXP, newXP);
				levelsToSpend++;
			}
			nbt.setByte(ModLib.nbtPlayerLevelsToSpend, (byte) levelsToSpend);
			
			ArrowQuest.packetPipeline.sendTo(new PacketPlayerLevelUp(newLevel, levelXP, newXP, levelsToSpend), (EntityPlayerMP) player);
		}else{
			nbt.setInteger(ModLib.nbtPlayerXP, newXP);
			ArrowQuest.packetPipeline.sendTo(new PacketPlayerXP(newXP), (EntityPlayerMP) player);
		}
	}
	
	public static void onSkillLearned(EntityPlayer player, SkillTreeEntry skill){
		NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
		if (skill.codeName.startsWith("jumpheight")){
			int jump = nbt.getByte(ModLib.nbtPlayerJumpHeight);
			if (jump > 0){
				player.addPotionEffect(new PotionEffect(8, 1000000, jump-1, true, false));
			}
		}else if (skill.codeName.startsWith("movespeed")){
			int speed = nbt.getByte(ModLib.nbtPlayerMoveSpeed);
			
			AttributeModifier modifier = new AttributeModifier(ModLib.playerSpeedUUID, "Speed", 0.2D * speed, 2);
			IAttributeInstance attribute = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
			
			AttributeModifier oldModifier = attribute.getModifier(ModLib.playerSpeedUUID);
			if (oldModifier != null){
				attribute.removeModifier(oldModifier);
			}
			
			attribute.applyModifier(modifier);
		}else if (skill.codeName.startsWith("maxhp")){
			int hp = nbt.getByte(ModLib.nbtPlayerMaxHP);
			
			AttributeModifier modifier = new AttributeModifier(ModLib.playerMaxHPUUID, "AQMaxHP", hp, 0);
			IAttributeInstance attribute = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
			
			AttributeModifier oldModifier = attribute.getModifier(ModLib.playerMaxHPUUID);
			if (oldModifier != null){
				attribute.removeModifier(oldModifier);
			}
			
			attribute.applyModifier(modifier);
		}
	}
}