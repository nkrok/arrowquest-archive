package com.cm8check.arrowquest.network.packet;

import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.player.SkillTree;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketLevelUpGUIOpen extends AbstractPacket{
	public PacketLevelUpGUIOpen(){}
	
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
		List<Byte> skillList = SkillTree.calculateSkillOptions(player);
		ArrowQuest.packetPipeline.sendTo(new PacketLevelUpGUI(skillList), (EntityPlayerMP) player);
	}
}