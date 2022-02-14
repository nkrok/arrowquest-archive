package com.cm8check.arrowquest.network.packet;

import java.util.ArrayList;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.gui.GuiHudOverlay;
import com.cm8check.arrowquest.client.gui.GuiLevelUp;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

public class PacketLevelUpGUI extends AbstractPacket{
	private List<Byte> list;
	
	public PacketLevelUpGUI(){}
	public PacketLevelUpGUI(List list){
		this.list = list;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeByte(list.size());
		for (int i = 0; i < list.size(); i++){
			buffer.writeByte(list.get(i));
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		int num = buffer.readByte();
		list = new ArrayList<Byte>();
		for (int i = 0; i < num; i++){
			byte id = buffer.readByte();
			list.add(id);
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		ArrowQuest.levelUpSkillList = list;
		
		Minecraft.getMinecraft().addScheduledTask(() -> {
			GuiHudOverlay.clearIndefiniteGuiPopup();
			player.openGui(ArrowQuest.instance, ModLib.guiLevelUpID, player.worldObj, 0, 0, 0);
		});
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		
	}
}