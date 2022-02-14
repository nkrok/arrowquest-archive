package com.cm8check.arrowquest.client.gui;

import org.lwjgl.opengl.GL11;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.inventory.container.ContainerPoliceTeleporter;
import com.cm8check.arrowquest.network.packet.PacketPoliceTeleporterUse;
import com.cm8check.arrowquest.tileentity.TileEntityPoliceTeleporter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPoliceTeleporter extends GuiContainer {
	private static ResourceLocation guiTex = new ResourceLocation("arrowquest", "textures/gui/guiPoliceTeleporter.png");
	private TileEntityPoliceTeleporter tileEntity;

	public GuiPoliceTeleporter(InventoryPlayer inventoryPlayer, TileEntityPoliceTeleporter tile) {
		super(new ContainerPoliceTeleporter(inventoryPlayer, tile));
		this.ySize = 166;
		this.tileEntity = tile;
	}
	
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height){
		super.setWorldAndResolution(mc, width, height);
		
		GuiButton button = new GuiButton(0, width / 2 - 30, height / 2 - 40, 60, 20, "Teleport");
		this.buttonList.add(button);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 94, 4210752);

		String str = "Destination:";
		this.fontRendererObj.drawString(str, this.xSize / 2 - this.fontRendererObj.getStringWidth(str) / 2, 15,
				4210752);
		
		str = TileEntityPoliceTeleporter.destinationNames[tileEntity.getDestination()];
		this.fontRendererObj.drawString(str, this.xSize / 2 - this.fontRendererObj.getStringWidth(str) / 2, 25,
				4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiTex);
		int xx = (this.width - this.xSize) / 2;
		int yy = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(xx, yy, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (buttonList.contains(button)) {
			ArrowQuest.packetPipeline.sendToServer(new PacketPoliceTeleporterUse(this.tileEntity.getDestination(), this.tileEntity.getPos()));

			if (!ArrowQuest.DEV_MODE && this.mc.thePlayer != null) {
				this.mc.thePlayer.closeScreen();
			}
		}
	}
}