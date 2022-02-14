package com.cm8check.arrowquest.network.packet;

import org.lwjgl.input.Keyboard;

import com.cm8check.arrowquest.client.gui.GuiHudOverlay;
import com.cm8check.arrowquest.client.keybind.KeyBindings;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class PacketPlayerLevelUp extends AbstractPacket{
	private int newLevel;
	private int levelXP;
	private int xp;
	private int levelsToSpend;
	
	public PacketPlayerLevelUp(){}
	public PacketPlayerLevelUp(int newLevel, int levelXP, int xp, int levelsToSpend){
		this.newLevel = newLevel;
		this.levelXP = levelXP;
		this.xp = xp;
		this.levelsToSpend = levelsToSpend;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeShort(newLevel);
		buffer.writeInt(levelXP);
		buffer.writeInt(xp);
		buffer.writeByte(levelsToSpend);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		newLevel = buffer.readShort();
		levelXP = buffer.readInt();
		xp = buffer.readInt();
		levelsToSpend = buffer.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		if (ArrowQuestPlayer.localPlayerLevel == 0){
			String key = Keyboard.getKeyName(KeyBindings.openLevelUpGUI.getKeyCode());
			player.addChatMessage(new ChatComponentText("Congratulations on Levelling-Up for the first time! Press [" + key + "] open the Level-Up Menu!"));
			GuiHudOverlay.setIndefiniteGuiPopup("Level Up!", "Press [" + key + "] to Level-Up.");
		}
		
		ArrowQuestPlayer.localPlayerLevel = newLevel;
		ArrowQuestPlayer.localPlayerLevelXP = levelXP;
		ArrowQuestPlayer.localPlayerXP = xp;
		ArrowQuestPlayer.localPlayerLevelsToSpend = levelsToSpend;
		player.playSound("arrowquest:sndLevelUp", 1, 1);
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		
	}
}