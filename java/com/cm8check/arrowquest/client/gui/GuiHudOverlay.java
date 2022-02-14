package com.cm8check.arrowquest.client.gui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.keybind.KeyBindings;
import com.cm8check.arrowquest.item.ItemEvilBlade;
import com.cm8check.arrowquest.item.ItemSingingSword;
import com.cm8check.arrowquest.item.ItemSoulStealer;
import com.cm8check.arrowquest.item.ItemWand;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHudOverlay extends Gui{
	private static final int white = Color.WHITE.hashCode();
	private static final ResourceLocation guiTex = new ResourceLocation("arrowquest", "textures/gui/guiOverlay.png");
	private static final ResourceLocation popupTex = new ResourceLocation("textures/gui/achievement/achievement_background.png");
	Minecraft mc;
	
	private static final ArrayList<GuiPopup> guiPopups = new ArrayList<GuiPopup>();
	private static GuiPopup indefiniteGuiPopup;
	private static final int POPUP_DECAY_TIME = 200;
	
	public GuiHudOverlay(Minecraft minecraft){
		this.mc = minecraft;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onRenderGuiOverlay(RenderGameOverlayEvent.Post event){
		if (!event.isCancelable() && event.type == ElementType.ALL){
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			if (guiPopups.size() > 0) {
				long time = System.currentTimeMillis();
				//int xx = event.resolution.getScaledWidth() - 160;
				int xx = event.resolution.getScaledWidth() / 2;
				int yy = 0;
				
				for (int i = 0; i < guiPopups.size(); i++) {
					GuiPopup popup = guiPopups.get(i);
					if (popup.endTime != -1 && popup.endTime - time < POPUP_DECAY_TIME) {
						yy -= (float) (POPUP_DECAY_TIME - popup.endTime + time) / POPUP_DECAY_TIME * 32.0F;
					}
					else if (time < popup.openFinishTime) {
						yy += -32 + (float) (POPUP_DECAY_TIME - popup.openFinishTime + time) / POPUP_DECAY_TIME * 32.0F;
					}
					
					this.mc.getTextureManager().bindTexture(popupTex);
					this.drawTexturedModalRect(xx - 80, yy, 96, 202, 160, 32);
					this.mc.fontRendererObj.drawString(popup.mainText, xx - this.mc.fontRendererObj.getStringWidth(popup.mainText) / 2, yy + 8, Color.YELLOW.hashCode());
					this.mc.fontRendererObj.drawString(popup.subText, xx - this.mc.fontRendererObj.getStringWidth(popup.subText) / 2, yy + 18, white);
					
					if (yy <= -32) {
						guiPopups.remove(i);
						i--;
					}
					
					yy += 32;
				}
			}
			
	        this.mc.getTextureManager().bindTexture(guiTex);
			this.drawTexturedModalRect(0, 0, 0, 0, 120, 50);
			
			this.mc.fontRendererObj.drawString("XP: " + Integer.toString(ArrowQuestPlayer.localPlayerXP) + "/" + Integer.toString(ArrowQuestPlayer.localPlayerLevelXP), 5, 6, white);
			int col;
			String str;
			if (ArrowQuestPlayer.localPlayerLevelsToSpend > 0){
				col = Color.GREEN.hashCode();
				str = "Level " + Integer.toString(ArrowQuestPlayer.localPlayerLevel+1) + " (" + ArrowQuestPlayer.localPlayerLevelsToSpend + ")";
			}else{
				col = white;
				str = "Level " + Integer.toString(ArrowQuestPlayer.localPlayerLevel+1);
			}
			this.mc.fontRendererObj.drawString(str, 5, 16, col);
			
			EntityPlayer player = mc.thePlayer;
			if (player != null) {
				if (ArrowQuestPlayer.selectRace){
					ArrowQuestPlayer.selectRace = false;
					player.openGui(ArrowQuest.instance, ModLib.guiSelectRaceID, player.worldObj, 0, 0, 0);
				}
				
				if (player.getHeldItem() != null) {
					if (player.getHeldItem().getItem() == ModItems.itemSingingSword) {
						if (player.getHeldItem().hasTagCompound() && player.getHeldItem().getTagCompound().getBoolean("Charged")) {
							int color = Color.CYAN.hashCode();
							if (mc.getSystemTime() % 300 < 150) {
								color = Color.WHITE.hashCode();
								
							}
							
							drawRect(5, 36, 40, 41, color);
						}
						else if (player.getItemInUseDuration() > 0){
							drawRect(5, 36, 5 + Math.round(35*((float) (player.getItemInUseDuration()+event.partialTicks)/ItemSingingSword.MAX_CHARGE)), 41, Color.CYAN.hashCode());
						}
					}
					else if (player.getHeldItem().getItem() == ModItems.itemSceptreOfAsmodeus) {
						if (player.getHeldItem().getMetadata() == 1) {
							int a = (int) (Math.sin(Math.PI * (mc.getSystemTime() % 1500) / 1500) * 250) + 5;
							int color = new Color(255, 255, 255, a).hashCode();
							this.mc.fontRendererObj.drawString("[CHARGED]", 5, 26, color);
						}
					}
					else if (player.getHeldItem().getItem() == ModItems.itemSceptreOfTorment) {
						if (player.getHeldItem().getMetadata() == 0) {
							int a = (int) (Math.sin(Math.PI * (mc.getSystemTime() % 1500) / 1500) * 250) + 5;
							int color = new Color(255, 255, 255, a).hashCode();
							this.mc.fontRendererObj.drawString("[CHARGED]", 5, 26, color);
						}
					}
					else if (player.getHeldItem().getItem() == ModItems.itemSoulStealer) {
						if (player.getHeldItem().hasTagCompound()) {
							/*
							int level = player.getHeldItem().getTagCompound().getInteger("AttackPrimed");
							
							if (level > 0) {
								this.mc.fontRendererObj.drawString("[" + ItemSoulStealer.skillNames[level - 1] + "]", 5, 36, white);
							}
							*/
							
							int souls = player.getHeldItem().getTagCompound().getInteger("Souls");
							if (souls == 1) {
								str = "1 Soul";
							}
							else {
								str = souls + " Souls";
							}
							
							this.mc.fontRendererObj.drawString(str, 5, 26, white);
							
							if (souls >= ItemSoulStealer.BASE_SOULS_NEEDED) {
								int level = 0;
								
								for (int i = 1; i < ItemSoulStealer.skillNames.length; i++) {
									if (souls >= ItemSoulStealer.BASE_SOULS_NEEDED * Math.pow(2, i)) {
										level = i;
									}
									else {
										break;
									}
								}
								
								if (player.getHeldItem().getTagCompound().getInteger("AttackPrimed") == 0 || mc.getSystemTime() % 250 < 125) {
									this.mc.fontRendererObj.drawString("[" + ItemSoulStealer.skillNames[level] + "]", 5, 36, white);
								}
							}
						}
					}
					else if (player.getHeldItem().getItem() instanceof ItemEvilBlade) {
						if (player.getHeldItem().hasTagCompound()) {
							int charge = player.getHeldItem().getTagCompound().getInteger("Charge");
							int nextLevelCharge = charge;
							int nextLevelChargeNeeded = ItemEvilBlade.CHARGE_LEVELS[0];
							int level = 0;
							
							for (int c : ItemEvilBlade.CHARGE_LEVELS) {
								if (charge >= c) {
									level++;
									nextLevelCharge -= nextLevelChargeNeeded;
									nextLevelChargeNeeded = c;
								}
								else {
									break;
								}
							}
							
							//System.out.println("\ncharge: " + nextLevelCharge + "\ncharge needed: " + nextLevelChargeNeeded);
							
							if (level > 0) {
								ItemEvilBlade item = (ItemEvilBlade) player.getHeldItem().getItem();
								this.mc.fontRendererObj.drawString("[" + ItemEvilBlade.SKILL_NAMES[item.getWeaponType()][level - 1] + "]", 5, 26, white);
							}
							
							if (player.getHeldItem().getTagCompound().getInteger("AttackPrimed") > 0) {
								if (mc.getSystemTime() % 250 < 125) {
									drawRect(5, 36, 40, 41, Color.RED.hashCode());
								}
							}
							else {
								if (level > 0) {
									int a = (int) (Math.sin(Math.PI * (mc.getSystemTime() % 1500) / 1500) * 250) + 5;
									int gb = Math.max(255 - 42*(level - 1), 0);
									int color = new Color(255, gb, gb, a).hashCode();
									drawRect(5, 36, 40, 41, color);
								}
								
								int gb = Math.max(255 - 42*level, 0);
								int color = new Color(255, gb, gb, 255).hashCode();
								drawRect(5, 36, 5 + Math.round(35.0F * ((float) nextLevelCharge / nextLevelChargeNeeded)), 41, color);
							}
						}
					}
					else if (player.getHeldItem().getItem() == ModItems.itemBanisher) {
						if (player.getHeldItem().hasTagCompound()) {
							int souls = player.getHeldItem().getTagCompound().getShort("Souls");
							
							if (souls == 1) {
								str = "1 Soul";
							}
							else {
								str = souls + " Souls";
							}
							
							this.mc.fontRendererObj.drawString(str, 5, 26, white);
						}
					}
					else if (player.getHeldItem().getItem() instanceof ItemWand && player.getHeldItem().hasTagCompound()){
						ItemStack wand = player.getHeldItem();
						NBTTagCompound nbt = wand.getTagCompound();
						int spell = nbt.getByte("ActiveSpell");
						
						if (spell > 0){
							String key = Keyboard.getKeyName(KeyBindings.cycleWandSpell.getKeyCode());
							str = (new ChatComponentTranslation("itemScroll_"+spell+".name", new Object[0])).getUnformattedText() + " [" + key + "]";
							this.mc.fontRendererObj.drawString(str, 5, 26, white);
							
							if (ItemWand.localCooldown > 0){
								drawRect(5, 36, 5 + Math.round(35*((float) (ItemWand.localCooldown-event.partialTicks)/ItemWand.localBaseCooldown)), 41, Color.GREEN.hashCode());
							}
						}
					}
				}
			}
		}
	}
	
	public static GuiPopup createGuiPopup(String mainText, String subText, int timeMillis) {
		GuiPopup popup = new GuiPopup(mainText, subText, timeMillis);
		guiPopups.add(popup);
		return popup;
	}
	
	public static GuiPopup createGuiPopup(String mainText, String subText) {
		return createGuiPopup(mainText, subText, 5000);
	}
	
	public static void setIndefiniteGuiPopup(String mainText, String subText) {
		clearIndefiniteGuiPopup();
		indefiniteGuiPopup = createGuiPopup(mainText, subText, -1);
	}
	
	public static void clearIndefiniteGuiPopup() {
		if (indefiniteGuiPopup != null) {
			indefiniteGuiPopup.endTime = System.currentTimeMillis() + POPUP_DECAY_TIME;
			indefiniteGuiPopup = null;
		}
	}

	private static class GuiPopup {
		protected String mainText;
		protected String subText;
		protected long openFinishTime;
		protected long endTime;
		
		public GuiPopup(String mainText, String subText, int timeMillis) {
			this.mainText = mainText;
			this.subText = subText;
			this.openFinishTime = System.currentTimeMillis() + POPUP_DECAY_TIME;
			
			if (timeMillis == -1) {
				this.endTime = -1;
			}
			else {
				this.endTime = System.currentTimeMillis() + timeMillis;
			}
		}
	}
}