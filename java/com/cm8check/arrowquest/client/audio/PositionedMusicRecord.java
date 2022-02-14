package com.cm8check.arrowquest.client.audio;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PositionedMusicRecord extends PositionedSoundRecord{
	public PositionedMusicRecord(ResourceLocation soundResource, float volume, float pitch, float xPosition, float yPosition, float zPosition){
		super(soundResource, volume, pitch, xPosition, yPosition, zPosition);
	}
	
	protected PositionedMusicRecord(ResourceLocation soundResource, float volume){
		this(soundResource, volume, 1.0F, 0.0F, 0.0F, 0.0F);
		this.repeat = true;
		this.attenuationType = ISound.AttenuationType.NONE;
	}
}