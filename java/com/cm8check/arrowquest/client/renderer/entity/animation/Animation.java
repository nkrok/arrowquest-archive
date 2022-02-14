package com.cm8check.arrowquest.client.renderer.entity.animation;

import net.minecraft.util.ResourceLocation;

public class Animation {
	public final int id;
	
	public final ResourceLocation texture;
	public final int textureWidth;
	public final int[] frameSequence;
	public final int totalFrames;
	public final boolean isGroundAnimation;

	public Animation(int id, ResourceLocation texture, int textureWidth, int totalFrames, int[] frameSequence, boolean isGroundAnimation) {
		this.id = id;
		
		this.texture = texture;
		this.textureWidth = textureWidth;
		this.frameSequence = frameSequence;
		this.totalFrames = totalFrames;
		this.isGroundAnimation = isGroundAnimation;
	}
}