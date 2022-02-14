package com.cm8check.arrowquest.client.audio;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientSoundHelper{
	private static Minecraft mc;
	private static ISound music;
	private static boolean bossMusicPlaying;
	private static int soundUpdateDelay;
	
	public static void init(){
		mc = Minecraft.getMinecraft();
	}
	
	public static boolean bossMusicCheck(String musicName, EntityMob host){
		EntityPlayer player = mc.thePlayer;
        if (player != null && host.deathTime <= 0){
	        if (player.canEntityBeSeen(host)) {
	        	if (!bossMusicPlaying) {
		        	playCustomMusic(musicName);
		        	bossMusicPlaying = true;
	        	}
	        	
	        	return true;
	        }
        }
        
        return false;
	}
	
	public static void stopMusic(){
		System.out.println("ayo");
		bossMusicPlaying = false;
		if (mc.getSoundHandler().isSoundPlaying(music)){
			mc.getSoundHandler().stopSound(music);
		}
		
		music = null;
	}
	
	public static boolean isCustomMusicPlaying(){
		return music != null && mc.getSoundHandler().isSoundPlaying(music);
	}
	
	public static ISound playCustomMusic(String name){
		if (isMusicEnabled()){
			mc.getSoundHandler().stopSounds();
			music = new PositionedMusicRecord(new ResourceLocation(name), 0.25F);
			mc.getSoundHandler().playSound(music);
			soundUpdateDelay = 20;
			
			return music;
		}
		
		return null;
	}
	
	public static void playCustomMusicByID(int id){
		switch(id){
		case ModLib.dimPoliceBaseID:
			playCustomMusic("arrowquest:musicPoliceHQ");
		break;
		case ModLib.dimFinalBossID:
			playCustomMusic("arrowquest:musicCastle");
		break;
		case 51:
			playCustomMusic("arrowquest:musicInduction");
			mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("arrowquest:sndTeleport"), 1.0F));
		break;
		case 52:
			playCustomMusic("arrowquest:musicRevolution");
		break;
		default:
			stopMusic();
		break;
		}
		
		bossMusicPlaying = false;
	}
	
	public static void musicCheck() {
		if (isMusicEnabled() && !bossMusicPlaying && music != null && !isCustomMusicPlaying()) {
			if (soundUpdateDelay <= 0) {
				System.out.println("Music restarted!");
				mc.getSoundHandler().stopSounds();
				mc.getSoundHandler().playSound(music);
				soundUpdateDelay = 20;
			}
			else {
				soundUpdateDelay--;
			}
		}
	}
	
	private static boolean isMusicEnabled(){
		return !ArrowQuest.DEV_MODE;
	}
}