package com.cm8check.arrowquest.client.keybind;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.gui.GuiHudOverlay;
import com.cm8check.arrowquest.network.packet.PacketAQLogOpen;
import com.cm8check.arrowquest.network.packet.PacketCycleWandSpell;
import com.cm8check.arrowquest.network.packet.PacketLevelUpGUIOpen;
import com.cm8check.arrowquest.network.packet.PacketOpenMagicBackpack;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler{
	@SubscribeEvent
	public void onKeyPressed(InputEvent.KeyInputEvent event){
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		if (player != null){
			if (KeyBindings.openLevelUpGUI.isPressed()){
				ArrowQuest.packetPipeline.sendToServer(new PacketLevelUpGUIOpen());
			}else if (KeyBindings.cycleWandSpell.isPressed()){
				ArrowQuest.packetPipeline.sendToServer(new PacketCycleWandSpell());
			}else if (KeyBindings.openMagicBackpack.isPressed()){
				ArrowQuest.packetPipeline.sendToServer(new PacketOpenMagicBackpack());
			}else if (KeyBindings.openAQLogGUI.isPressed()) {
				ArrowQuest.packetPipeline.sendToServer(new PacketAQLogOpen());
			}
		}
	}
}