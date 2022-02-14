package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class PacketOpenMagicBackpack extends AbstractPacket{
	public PacketOpenMagicBackpack(){}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
		
		if (nbt.getBoolean(ModLib.nbtPlayerHasMagicBackpack)){
			BlockPos pos = player.getPosition();
			player.openGui(ArrowQuest.instance, ModLib.guiMagicBackpackID, player.worldObj, pos.getX(), pos.getY(), pos.getZ());
		}
	}
}