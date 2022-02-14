package com.cm8check.arrowquest.client.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.network.packet.PacketLevelUpGUISelect;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;
import com.cm8check.arrowquest.player.SkillTree;
import com.cm8check.arrowquest.player.SkillTreeEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiLevelUp extends GuiScreen{
	public List<Byte> skillList;
	public List<List> skillDesc;
	private boolean ready;
	private int buttonWidth = 150;
	
	public GuiLevelUp(){
		this.ready = false;
		this.skillDesc = new ArrayList<List>();
	}
	
	public void setList(List list){
		this.skillList = list;
		this.ready = true;
	}
	
	private void setButtons(){
		this.buttonList.clear();
		this.skillDesc.clear();
		
		int x = 0;
		int y = 0;
		int rows = 1;
		int size = skillList.size();
		if (50+(20*(size-1)) > height){
			rows++;
		}
		for (int i = 0; i < size; i++){
			SkillTreeEntry skill = SkillTree.skills.get((int) skillList.get(i));
			
			GuiButton button = new GuiButton(i, (width-(buttonWidth*rows) + (buttonWidth*2*x))/2, 30+(20*y), buttonWidth, 20, skill.displayName);
			button.enabled = (ArrowQuestPlayer.localPlayerLevelsToSpend > 0);
			buttonList.add(button);
			
			y++;
			if (50+(20*y) > height){
				y = 0;
				x++;
			}
			
			List<String> list = new ArrayList<String>();
			if (skill.skillType.type == 2 && skill.skillType.description != ""){
				list.add(skill.skillType.description);
			}
			skillDesc.add(list);
		}
	}
	
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height){
		super.setWorldAndResolution(mc, width, height);
		if (skillList != null){
			this.setButtons();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 128).hashCode());
		super.drawScreen(mouseX, mouseY, partialTicks);
		String str = "Level Up!";
		fontRendererObj.drawString(str, width/2 - fontRendererObj.getStringWidth(str)/2, 6, Color.WHITE.hashCode());
		str = "Select an Action:";
		fontRendererObj.drawString(str, width/2 - fontRendererObj.getStringWidth(str)/2, 20, Color.WHITE.hashCode());
		fontRendererObj.drawString("Levels to Spend: " + ArrowQuestPlayer.localPlayerLevelsToSpend, 10, height-15, Color.GREEN.hashCode());
		
		//stats
		str = "ATK: " + (1+ArrowQuestPlayer.localPlayerATK);
		fontRendererObj.drawString(str, width-fontRendererObj.getStringWidth(str)-6, height-50, Color.WHITE.hashCode());
		str = "DEF: " + (ArrowQuestPlayer.localPlayerDEF*5) + "%";
		fontRendererObj.drawString(str, width-fontRendererObj.getStringWidth(str)-6, height-40, Color.WHITE.hashCode());
		str = "Speed: " + (100 + ArrowQuestPlayer.localPlayerSpeed*20) + "%";
		fontRendererObj.drawString(str, width-fontRendererObj.getStringWidth(str)-6, height-30, Color.WHITE.hashCode());
		str = "Jump: " + (100 + ArrowQuestPlayer.localPlayerJumpHeight*50) + "%";
		fontRendererObj.drawString(str, width-fontRendererObj.getStringWidth(str)-6, height-20, Color.WHITE.hashCode());
		str = "Magic DMG: +" + (ArrowQuestPlayer.localPlayerMagicDMG);
		fontRendererObj.drawString(str, width-fontRendererObj.getStringWidth(str)-6, height-10, Color.WHITE.hashCode());
		
		for (int i = 0; i < buttonList.size(); i++){
			GuiButton button = (GuiButton) buttonList.get(i);
			List list = skillDesc.get(i);
			if (!list.isEmpty() && button.isMouseOver()){
				drawHoveringText(list, mouseX, mouseY);
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame(){
        return false;
    }
	
	@Override
	protected void actionPerformed(GuiButton button){
		if (buttonList.contains(button)){
			int index = buttonList.indexOf(button);
			byte skillID = skillList.get(index);
			
			ArrowQuest.packetPipeline.sendToServer(new PacketLevelUpGUISelect(skillID));
			ArrowQuestPlayer.localPlayerLevelsToSpend -= 1;
			
			if (ArrowQuestPlayer.localPlayerLevelsToSpend <= 0 && this.mc.thePlayer != null){
				this.mc.thePlayer.closeScreen();
			}
		}
	}
}