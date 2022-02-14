package com.cm8check.arrowquest.client.gui;

import org.lwjgl.opengl.GL11;

import com.cm8check.arrowquest.inventory.container.ContainerWandFurnace;
import com.cm8check.arrowquest.tileentity.TileEntityWandFurnace;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiWandFurnace extends GuiContainer{
	private static ResourceLocation guiTex = new ResourceLocation("arrowquest", "textures/gui/guiWandFurnace.png");
	private TileEntityWandFurnace tileEntity;
	
	public GuiWandFurnace(InventoryPlayer inventoryPlayer, TileEntityWandFurnace tile){
		super(new ContainerWandFurnace(inventoryPlayer, tile));
		this.ySize = 166;
		this.tileEntity = tile;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		String str = "Spellcaster's Furnace";
		this.fontRendererObj.drawString(str, this.xSize/2 - this.fontRendererObj.getStringWidth(str)/2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize-94, 4210752);
		
		str = "Stored Charge: " + Integer.toString(tileEntity.chargeLeft);
		this.fontRendererObj.drawString(str, this.xSize - (this.fontRendererObj.getStringWidth(str)+6), this.ySize-94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTex);
        int xx = (this.width - this.xSize) / 2;
        int yy = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xx, yy, 0, 0, this.xSize, this.ySize);
	}
}