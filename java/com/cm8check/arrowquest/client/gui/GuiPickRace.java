package com.cm8check.arrowquest.client.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.keybind.KeyBindings;
import com.cm8check.arrowquest.network.packet.PacketPlayerSelectRace;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

public class GuiPickRace extends GuiScreen{
	private List<String> elfDesc = new ArrayList<String>();
	private List<String> orcDesc = new ArrayList<String>();
	private List<String> humanDesc = new ArrayList<String>();
	private List<String> dwarfDesc = new ArrayList<String>();
	private static final int buttonWidth = 100;
	
	public GuiPickRace(){
		elfDesc.add("+40% Move Speed");
		orcDesc.add("+2 Attack Damage");
		humanDesc.add("+2 Magic Damage");
		dwarfDesc.add("+5% Damage Resistance");
	}
	
	private void setButtons(){
		this.buttonList.clear();
		
		GuiButton button = new GuiButton(0, (width/2)-(buttonWidth/2), 80, buttonWidth, 20, "Elf");
		buttonList.add(button);
		button = new GuiButton(1, (width/2)-(buttonWidth/2), 110, buttonWidth, 20, "Orc");
		buttonList.add(button);
		button = new GuiButton(2, (width/2)-(buttonWidth/2), 140, buttonWidth, 20, "Human");
		buttonList.add(button);
		button = new GuiButton(3, (width/2)-(buttonWidth/2), 170, buttonWidth, 20, "Dwarf");
		buttonList.add(button);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 128).hashCode());
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		String str = "Welcome to ArrowQuest!";
		fontRendererObj.drawString(str, width/2 - fontRendererObj.getStringWidth(str)/2, 10, Color.WHITE.hashCode());
		str = "Choose Your Race:";
		fontRendererObj.drawString(str, width/2 - fontRendererObj.getStringWidth(str)/2, 25, Color.WHITE.hashCode());
		
		GuiButton button;
		
		button = (GuiButton) buttonList.get(0);
		if (button.isMouseOver()){
			drawHoveringText(elfDesc, mouseX, mouseY);
		}else{
			button = (GuiButton) buttonList.get(1);
			if (button.isMouseOver()){
				drawHoveringText(orcDesc, mouseX, mouseY);
			}else{
				button = (GuiButton) buttonList.get(2);
				if (button.isMouseOver()){
					drawHoveringText(humanDesc, mouseX, mouseY);
				}else{
					button = (GuiButton) buttonList.get(3);
					if (button.isMouseOver()){
						drawHoveringText(dwarfDesc, mouseX, mouseY);
					}
				}
			}
		}
	}
	
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height){
		super.setWorldAndResolution(mc, width, height);
		this.setButtons();
	}
	
	@Override
	public boolean doesGuiPauseGame(){
        return false;
    }
	
	@Override
	protected void actionPerformed(GuiButton button){
		if (buttonList.contains(button)){
			int index = buttonList.indexOf(button);
			ArrowQuestPlayer.localPlayerRace = index;
			ArrowQuest.packetPipeline.sendToServer(new PacketPlayerSelectRace(index));
			
			if (this.mc.thePlayer != null){
				this.mc.thePlayer.closeScreen();
			}
		}
	}
	
	@Override
	public void onGuiClosed(){
		if (ArrowQuestPlayer.localPlayerRace == 10){
			ArrowQuestPlayer.selectRace = true;
		}
		
		String key = Keyboard.getKeyName(KeyBindings.openAQLogGUI.getKeyCode());
		GuiHudOverlay.setIndefiniteGuiPopup("Adventure Log", "Press [" + key + "] to access!");
	}
}