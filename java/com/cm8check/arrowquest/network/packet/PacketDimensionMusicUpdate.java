package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

public class PacketDimensionMusicUpdate extends AbstractPacket{
	private int dim;
	private int playDelay;
	
	public PacketDimensionMusicUpdate(){}
	public PacketDimensionMusicUpdate(int dim, int delay){
		this.dim = dim;
		this.playDelay = delay;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeByte(dim);
		buffer.writeByte(playDelay);
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		dim = buffer.readByte();
		playDelay = buffer.readByte();
	}
	
	@Override
	public void handleClientSide(EntityPlayer player){
		if (dim < ModLib.dimPoliceBaseID){
			ClientSoundHelper.stopMusic();
		}else{
			ClientSoundHelper.playCustomMusicByID(dim);
		}
	}
	
	@Override
	public void handleServerSide(EntityPlayer player){
		
	}
}