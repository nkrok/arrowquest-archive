package com.cm8check.arrowquest.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.cm8check.arrowquest.inventory.InventoryWand;
import com.cm8check.arrowquest.inventory.container.ContainerWand;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiWand extends GuiContainer{
	private static ResourceLocation guiTex = new ResourceLocation("arrowquest", "textures/gui/guiWand.png");
	private int size;
	private String name;
	
	public GuiWand(InventoryPlayer inventoryPlayer, InventoryWand inventory){
		super(new ContainerWand(inventoryPlayer, inventory));
		this.ySize = 182;
		this.size = inventory.size;
		this.name = inventory.getName();
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		this.fontRendererObj.drawString(name, this.xSize/2 - this.fontRendererObj.getStringWidth(name)/2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize-94, 4210752);
		
		if (size < 8){
			this.drawRect(71, 88, 107, 88 - (9*(8-size)), new Color(0, 0, 0, 160).hashCode());
		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTex);
        int xx = (this.width - this.xSize) / 2;
        int yy = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xx, yy, 0, 0, this.xSize, this.ySize);
	}
}