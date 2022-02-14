package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.client.gui.GuiHudOverlay;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PacketGuiPopup extends AbstractPacket {
	private int type;
	
	public PacketGuiPopup() {
	}
	
	public PacketGuiPopup(int type) {
		this.type = type;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeByte(this.type);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.type = buffer.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			switch (this.type) {
			case 0:
			{
				player.playSound("arrowquest:sndObjectiveUpdate", 1.0F, 1.0F);
				GuiHudOverlay.setIndefiniteGuiPopup("Objective Update!", "Check the Adventure Log.");
				break;
			}
			}
		});
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		
	}
}