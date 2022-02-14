package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.gui.GuiAQLog;
import com.cm8check.arrowquest.client.gui.GuiHudOverlay;
import com.cm8check.arrowquest.lib.ModLib;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PacketAQLogInfo extends AbstractPacket {
	public PacketAQLogInfo() {
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeShort(GuiAQLog.LogInfo.structuresCleared);
		buffer.writeByte(GuiAQLog.LogInfo.tier1StructuresCleared);
		buffer.writeByte(GuiAQLog.LogInfo.tier2StructuresCleared);
		buffer.writeByte(GuiAQLog.LogInfo.tier3StructuresCleared);
		buffer.writeByte(GuiAQLog.LogInfo.tier4StructuresCleared);
		
		buffer.writeByte(GuiAQLog.LogInfo.bosses);
		buffer.writeByte(GuiAQLog.LogInfo.artifactWeapons);
		buffer.writeByte(GuiAQLog.LogInfo.scrolls);
		
		buffer.writeByte(GuiAQLog.LogInfo.objective);
		buffer.writeByte(GuiAQLog.LogInfo.objectiveProgress);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		GuiAQLog.LogInfo.structuresCleared = buffer.readShort();
		GuiAQLog.LogInfo.tier1StructuresCleared = buffer.readByte();
		GuiAQLog.LogInfo.tier2StructuresCleared = buffer.readByte();
		GuiAQLog.LogInfo.tier3StructuresCleared = buffer.readByte();
		GuiAQLog.LogInfo.tier4StructuresCleared = buffer.readByte();
		
		GuiAQLog.LogInfo.bosses = buffer.readByte();
		GuiAQLog.LogInfo.artifactWeapons = buffer.readByte();
		GuiAQLog.LogInfo.scrolls = buffer.readByte();
		
		GuiAQLog.LogInfo.objective = buffer.readByte();
		GuiAQLog.LogInfo.objectiveProgress = buffer.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			GuiHudOverlay.clearIndefiniteGuiPopup();
			player.openGui(ArrowQuest.instance, ModLib.guiAQLogID, player.worldObj, 0, 0, 0);
		});
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		
	}
}