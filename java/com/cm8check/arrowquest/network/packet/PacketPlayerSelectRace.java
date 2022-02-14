package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class PacketPlayerSelectRace extends AbstractPacket{
	private int race;
	
	public PacketPlayerSelectRace(){}
	public PacketPlayerSelectRace(int race){
		this.race = race;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeByte(race);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		race = buffer.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player){
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
		nbt.setByte(ModLib.nbtPlayerRace, (byte) race);
		
		if (race == 0){
			nbt.setByte(ModLib.nbtPlayerMoveSpeed, (byte) 2);
			
			AttributeModifier modifier = new AttributeModifier(ModLib.playerSpeedUUID, "Speed", 0.4D, 2);
			IAttributeInstance attribute = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
			attribute.applyModifier(modifier);
		}else if (race == 1){
			nbt.setByte(ModLib.nbtPlayerATK, (byte) 2);
		}else if (race == 2){
			nbt.setByte(ModLib.nbtPlayerMagicDMG, (byte) 2);
		}else if (race == 3) {
			nbt.setByte(ModLib.nbtPlayerDEF, (byte) 1);
		}
	}
}