package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.client.gui.GuiHudOverlay;
import com.cm8check.arrowquest.item.ItemWand;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSetCooldown extends AbstractPacket{
	private int baseCooldown;
	
	public PacketSetCooldown(){}
	public PacketSetCooldown(int baseCooldown){
		this.baseCooldown = baseCooldown;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeByte(baseCooldown);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		baseCooldown = buffer.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		ItemWand.localCooldown = baseCooldown;
		ItemWand.localBaseCooldown = baseCooldown;
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		
	}
}