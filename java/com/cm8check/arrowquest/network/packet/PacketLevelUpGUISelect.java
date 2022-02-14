package com.cm8check.arrowquest.network.packet;

import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;
import com.cm8check.arrowquest.player.SkillTree;
import com.cm8check.arrowquest.player.SkillTreeEntry;
import com.cm8check.arrowquest.player.SkillTreeEntryType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PacketLevelUpGUISelect extends AbstractPacket{
	private byte skillID;
	private Random rand = new Random();
	
	public PacketLevelUpGUISelect(){}
	public PacketLevelUpGUISelect(byte skill){
		this.skillID = skill;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeByte(skillID);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		skillID = buffer.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
		int levelsToSpend = nbt.getByte(ModLib.nbtPlayerLevelsToSpend);
		
		if (levelsToSpend > 0){
			SkillTreeEntry skill = SkillTree.skills.get((int) skillID);
			SkillTreeEntryType type = skill.skillType;
			
			if (type.type == 0){
				EntityItem entityitem = new EntityItem(player.worldObj, player.posX, player.posY + 1.0, player.posZ, type.itemstack);
	            entityitem.motionX = 0.0;
	            entityitem.motionY = 0.2;
	            entityitem.motionZ = 0.0;
	            player.worldObj.spawnEntityInWorld(entityitem);
			}else if (type.type == 1){
				int stat = nbt.getByte(type.statName);
				nbt.setByte(type.statName, (byte) (stat + type.statIncrementAmount));
			}else if (type.type == 2){
				nbt.setBoolean(type.statName, type.statState);
			}
			ArrowQuestPlayer.onSkillLearned(player, skill);
			
			NBTTagList taglist = nbt.getTagList(ModLib.nbtPlayerSkills, 10);
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("skillName", skill.codeName);
			taglist.appendTag(compound);
			nbt.setTag(ModLib.nbtPlayerSkills, taglist);
			
			levelsToSpend -= 1;
			if (levelsToSpend < 0){
				levelsToSpend = 0;
			}
			nbt.setByte(ModLib.nbtPlayerLevelsToSpend, (byte) levelsToSpend);
			
			int xp = nbt.getInteger(ModLib.nbtPlayerXP);
			int levelXP = nbt.getInteger(ModLib.nbtPlayerLevelXP);
			int level = nbt.getShort(ModLib.nbtPlayerLevel);
			int atk = nbt.getByte(ModLib.nbtPlayerATK);
			int def = nbt.getByte(ModLib.nbtPlayerDEF);
			int speed = nbt.getByte(ModLib.nbtPlayerMoveSpeed);
			int jump = nbt.getByte(ModLib.nbtPlayerJumpHeight);
			int magicDMG = nbt.getByte(ModLib.nbtPlayerMagicDMG);
			int dimMusic = nbt.getByte(ModLib.nbtPlayerDimMusic);
			ArrowQuest.packetPipeline.sendTo(new PacketPlayerStatUpdate(xp, levelXP, level, levelsToSpend, atk, def, speed, jump, magicDMG), (EntityPlayerMP) player);
			
			if (levelsToSpend > 0){
				List<Byte> skillList = SkillTree.calculateSkillOptions(player);
				
				ArrowQuest.packetPipeline.sendTo(new PacketLevelUpGUI(skillList), (EntityPlayerMP) player);
			}
		}
	}
}