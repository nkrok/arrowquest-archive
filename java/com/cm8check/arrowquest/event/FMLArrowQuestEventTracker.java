package com.cm8check.arrowquest.event;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.entity.EntityOneshotAnimation;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketDimensionMusicUpdate;
import com.cm8check.arrowquest.network.packet.PacketGuiPopup;
import com.cm8check.arrowquest.network.packet.PacketPlayerLoginInfo;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FMLArrowQuestEventTracker{
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
		if (!event.player.getEntityData().hasKey(event.player.PERSISTED_NBT_TAG)){
			event.player.getEntityData().setTag(event.player.PERSISTED_NBT_TAG, new NBTTagCompound());
		}
		NBTTagCompound nbt = event.player.getEntityData().getCompoundTag(event.player.PERSISTED_NBT_TAG);
		
		boolean selectRace = false;
		int xp = nbt.getInteger(ModLib.nbtPlayerXP);
		int level = nbt.getShort(ModLib.nbtPlayerLevel);
		int levelsToSpend = nbt.getByte(ModLib.nbtPlayerLevelsToSpend);
		int levelXP = nbt.getInteger(ModLib.nbtPlayerLevelXP);
		
		if (levelXP == 0 || nbt.getByte(ModLib.nbtPlayerRace) == 10){
			levelXP = ArrowQuestPlayer.INITIAL_LEVEL_XP;
			nbt.setInteger(ModLib.nbtPlayerLevelXP, levelXP);
			nbt.setTag(ModLib.nbtPlayerSkills, new NBTTagList());
			selectRace = true;
			nbt.setByte(ModLib.nbtPlayerRace, (byte) 10);
		}
		else {
			levelXP = (int) (ArrowQuestPlayer.INITIAL_LEVEL_XP * Math.pow(1.22, level));
			nbt.setInteger(ModLib.nbtPlayerLevelXP, levelXP);
		}
		
		int atk = nbt.getByte(ModLib.nbtPlayerATK);
		int def = nbt.getByte(ModLib.nbtPlayerDEF);
		int speed = nbt.getByte(ModLib.nbtPlayerMoveSpeed);
		int jump = nbt.getByte(ModLib.nbtPlayerJumpHeight);
		int magicDMG = nbt.getByte(ModLib.nbtPlayerMagicDMG);
		int dimMusic = nbt.getByte(ModLib.nbtPlayerDimMusic);
		
		ArrowQuest.packetPipeline.sendTo(new PacketPlayerLoginInfo(xp, levelXP, level, levelsToSpend, atk, def, speed, jump, magicDMG, selectRace, dimMusic), (EntityPlayerMP) event.player);
	}
	
	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event){
		NBTTagCompound nbt = event.player.getEntityData().getCompoundTag(event.player.PERSISTED_NBT_TAG);
		nbt.setByte(ModLib.nbtPlayerDimMusic, (byte) event.toDim);
		ArrowQuest.packetPipeline.sendTo(new PacketDimensionMusicUpdate(event.toDim, 80), (EntityPlayerMP) event.player);
		
		if (event.fromDim == ModLib.dimPoliceBaseID){
			event.player.removePotionEffect(16);
		}
		
		if (event.toDim == ModLib.dimFinalBossID && !nbt.hasKey(ModLib.nbtFinalDestReached) && nbt.getByte(ModLib.nbtObjective) == 4) {
			ArrowQuest.packetPipeline.sendTo(new PacketGuiPopup(0), (EntityPlayerMP) event.player);
			nbt.setByte(ModLib.nbtObjective, (byte) 5); 
			nbt.setBoolean(ModLib.nbtFinalDestReached, true);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (Minecraft.getMinecraft().thePlayer != null) {
			ClientSoundHelper.musicCheck();
		}
	}
	
	/*
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTickEvent(TickEvent.RenderTickEvent event) {
		if (event.phase == Phase.START) {
			AnimationHelper.newOneshotAnimationsAllowed = false;
		}
		else if (event.phase == Phase.END) {
			AnimationHelper.newOneshotAnimationsAllowed = true;
			
			if (Minecraft.getMinecraft().thePlayer != null) {
				for (EntityOneshotAnimation anim : AnimationHelper.getQueuedOneshotAnimations()) {
					anim.worldObj.spawnEntityInWorld(anim);
				}
			}
			
			AnimationHelper.getQueuedOneshotAnimations().clear();
		}
	}
	*/
}