package com.cm8check.arrowquest.client.gui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.cm8check.arrowquest.item.ItemScroll;
import com.cm8check.arrowquest.world.gen.WorldGenSchematics;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiAQLog extends GuiScreen {
	private static ResourceLocation guiTex = new ResourceLocation("arrowquest", "textures/gui/guiAdventureLog.png");
	private int bookImageWidth = 256;
	private int bookImageHeight = 181;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 128).hashCode());

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiTex);
		int xx = (this.width - this.bookImageWidth) / 2;
		int yy = (this.height - this.bookImageHeight) / 2;
		this.drawTexturedModalRect(xx, yy, 0, 0, this.bookImageWidth, this.bookImageHeight);
		
		String str = "- Adventure Log -";
		xx = this.width / 2 - fontRendererObj.getStringWidth(str) / 2;
		yy = (this.height - this.bookImageHeight) / 2 + 14;
		fontRendererObj.drawString(str, xx, yy, Color.BLACK.hashCode());

		xx = (this.width - this.bookImageWidth) / 2 + 18;
		yy = (this.height - this.bookImageHeight) / 2 + 34;
		
		if (LogInfo.objective < LogInfo.objectiveStrings.length) {
			fontRendererObj.drawString("\u00A7nCurrent Objective", xx, yy - 2, Color.BLACK.hashCode());
			fontRendererObj.drawString(LogInfo.objectiveStrings[LogInfo.objective], xx, yy + 10, Color.BLACK.hashCode());
			
			if (LogInfo.objective == 0) {
				fontRendererObj.drawString(" - Progress: " + LogInfo.objectiveProgress + "/4", xx, yy + 20, Color.BLACK.hashCode());
			}
		}
		
		fontRendererObj.drawString("Structures Cleared: " + LogInfo.structuresCleared, xx, yy + 40, Color.BLACK.hashCode());
		
		fontRendererObj.drawString(" - Tier 1: " + LogInfo.tier1StructuresCleared + "/"
				+ WorldGenSchematics.getTierStructureCounts()[0], xx, yy + 50, Color.BLACK.hashCode());
		fontRendererObj.drawString(" - Tier 2: " + LogInfo.tier2StructuresCleared + "/"
				+ WorldGenSchematics.getTierStructureCounts()[1], xx, yy + 60, Color.BLACK.hashCode());
		fontRendererObj.drawString(" - Tier 3: " + LogInfo.tier3StructuresCleared + "/"
				+ WorldGenSchematics.getTierStructureCounts()[2], xx, yy + 70, Color.BLACK.hashCode());
		fontRendererObj.drawString(" - Tier SS: " + LogInfo.tier4StructuresCleared + "/"
				+ WorldGenSchematics.getTierStructureCounts()[3], xx, yy + 80, Color.BLACK.hashCode());
		
		fontRendererObj.drawString("Bosses: " + LogInfo.bosses + "/9", xx, yy + 100, Color.BLACK.hashCode());
		fontRendererObj.drawString("Artifact Weapons: " + LogInfo.artifactWeapons + "/14", xx, yy + 110, Color.BLACK.hashCode());
		fontRendererObj.drawString("Scrolls: " + LogInfo.scrolls + "/" + (ItemScroll.scrollDescriptions.length-1), xx, yy + 120, Color.BLACK.hashCode());
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public static class LogInfo {
		public static int structuresCleared;

		public static int tier1StructuresCleared;
		public static int tier2StructuresCleared;
		public static int tier3StructuresCleared;
		public static int tier4StructuresCleared;
		
		public static int bosses;
		public static int artifactWeapons;
		public static int scrolls;
		
		public static int objective;
		public static int objectiveProgress;
		
		private static String[] objectiveStrings = {
			"Kill all four main Overworld bosses.",
			"Kill the Supreme Emperor Blaze.",
			"Acquire Trueforce Dimensional Transporter.",
			"Kill the Trueforce General.",
			"The Final Destination",
			"Acquire the Arrow.",
			"???"
		};
	}
}