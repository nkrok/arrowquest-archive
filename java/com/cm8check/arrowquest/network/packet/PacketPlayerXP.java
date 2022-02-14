package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

public class PacketPlayerXP extends AbstractPacket{
	private int xp;
	
	public PacketPlayerXP(){}
	public PacketPlayerXP(int xp){
		this.xp = xp;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeInt(xp);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		xp = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		ArrowQuestPlayer.localPlayerXP = xp;
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		
	}
}