package com.cm8check.arrowquest.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.cm8check.arrowquest.inventory.InventoryWand;
import com.cm8check.arrowquest.inventory.container.ContainerBigCraftingTable;
import com.cm8check.arrowquest.inventory.container.ContainerWand;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBigCraftingTable extends GuiContainer{
	private static ResourceLocation guiTex = new ResourceLocation("arrowquest", "textures/gui/guiBigCraftingTable.png");
	
	public GuiBigCraftingTable(InventoryPlayer inventoryPlayer){
		super(new ContainerBigCraftingTable(inventoryPlayer));
		this.ySize = 188;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		String str = "Tier 2 Crafting Table";
		this.fontRendererObj.drawString(str, this.xSize/2 - this.fontRendererObj.getStringWidth(str)/2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize-94, 4210752);
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