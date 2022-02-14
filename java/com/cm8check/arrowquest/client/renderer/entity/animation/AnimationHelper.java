package com.cm8check.arrowquest.client.renderer.entity.animation;

import java.util.ArrayList;

import com.cm8check.arrowquest.entity.EntityOneshotAnimation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class AnimationHelper {
	private static final ArrayList<Animation> animationList = new ArrayList<Animation>();

	public static final Animation electricOrb = registerAnimation("electricOrb", 32, 10, false);
	public static final Animation heal1 = registerAnimation("heal1", 32, 20, false);
	public static final Animation heal2 = registerAnimation("heal2", 32, 20, false);
	public static final Animation policeLaserEffect = registerAnimation("policeLaserEffect", 32, 6, new int[] { 0, 1, 2, 3, 4, 5, 4, 3, 2, 1 }, false);
	public static final Animation doomBullet = registerAnimation("doomBullet", 32, 7, new int[] { 0, 1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1 }, false);
	public static final Animation fireLaserComponent = registerAnimation("fireLaserComponent", 32, 15, false);
	public static final Animation darknessAttack1 = registerAnimation("darknessAttack1", 32, 28, true);
	public static final Animation electricStrike = registerAnimation("electricStrike", 32, 20, false);
	public static final Animation darkSpirit = registerAnimation("darkSpirit", 32, 30, false);
	public static final Animation greenProjectile = registerAnimation("greenProjectile", 32, 10, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, false);
	public static final Animation blueProjectile = registerAnimation("blueProjectile", 32, 15, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, false);
	public static final Animation bigExplosion = registerAnimation("bigExplosion", 32, 18, false);
	public static final Animation bigExplosion2 = registerAnimation("bigExplosion2", 32, 20, false);
	public static final Animation plasmaBullet = registerAnimation("plasmaBullet", 16, 4, false);
	public static final Animation flame = registerAnimation("flame", 32, 5, new int[] { 0, 1, 2, 3, 4, 3, 2, 1 }, false);
	public static final Animation policeSpawn = registerAnimation("policeSpawn", 32, 15, false);
	public static final Animation policeBeam = registerAnimation("blueProjectile", 32, 15, new int[] { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 }, false);
	public static final Animation policeBossAura = registerAnimation("policeBossAura", 32, 10, false);
	public static final Animation phantasmEffect = registerAnimation("phantasmEffect", 32, 22, false);
	public static final Animation soulEffect = registerAnimation("soulEffect", 32, 24, false);
	public static final Animation greenMagicBeam = registerAnimation("greenMagicBeam", 32, 15, false);
	public static final Animation wizardBeamEffect = registerAnimation("wizardBeamEffect", 32, 6, new int[] { 0, 1, 2, 3, 4, 5, 4, 3, 2, 1 }, false);
	
	private static Animation registerAnimation(String textureName, int textureWidth, int totalFrames, boolean isGroundAnimation) {
		int[] frameSeq = new int[totalFrames];
		for (int i = 0; i < totalFrames; i++) {
			frameSeq[i] = i;
		}

		return registerAnimation(textureName, textureWidth, totalFrames, frameSeq, isGroundAnimation);
	}

	private static Animation registerAnimation(String textureName, int textureWidth, int totalFrames, int[] frameSequence, boolean isGroundAnimation) {
		Animation anim = new Animation(animationList.size(),
				new ResourceLocation("arrowquest", "textures/animations/" + textureName + ".png"), textureWidth,
				totalFrames, frameSequence, isGroundAnimation);
		animationList.add(anim);
		return anim;
	}

	public static Animation getAnimationByID(int id) {
		return animationList.get(id);
	}

	public static void spawnOneshotAnimation(World world, Animation animation, double x, double y, double z, float scale) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			EntityOneshotAnimation entity = new EntityOneshotAnimation(world, animation, scale, x, y, z);
			world.spawnEntityInWorld(entity);
		});
	}

	public static void renderAnimation(Animation animation, Render render, RenderManager renderManager, Entity entity,
			double x, double y, double z) {
		renderAnimation(animation, render, renderManager, entity, x, y, z, 1.0F);
	}
	
	public static void renderAnimation(Animation animation, Render render, RenderManager renderManager, Entity entity,
			double x, double y, double z, float scale) {
		renderAnimation(animation, render, renderManager, entity, x, y, z, scale, 0.0F);
	}

	public static void renderAnimation(Animation animation, Render render, RenderManager renderManager, Entity entity,
			double x, double y, double z, float scale, float zTranslate) {
		GlStateManager.disableLighting();
		// GlStateManager.disableDepth();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.scale(scale, scale, scale);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(0.0F, 0.0F, zTranslate);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		worldrenderer.startDrawingQuads();
		render.bindTexture(animation.texture);

		int frame = animation.frameSequence[entity.ticksExisted % animation.frameSequence.length];

		double minU = 0.0;
		double maxU = 1.0;
		double minV = (1.0 / animation.totalFrames) * frame;
		double maxV = (1.0 / animation.totalFrames) * (frame + 1);

		worldrenderer.addVertexWithUV(0.5, 0.0, 0.0, maxU, maxV);
		worldrenderer.addVertexWithUV(-0.5, 0.0, 0.0, minU, maxV);
		worldrenderer.addVertexWithUV(-0.5, 1.1, 0.0, minU, minV);
		worldrenderer.addVertexWithUV(0.5, 1.1, 0.0, maxU, minV);

		tessellator.draw();
		GlStateManager.popMatrix();
		// GlStateManager.enableDepth();
		GlStateManager.enableLighting();
	}
	
	public static void renderGroundAnimation(Animation animation, Render render, Entity entity,
			double x, double y, double z, float scale) {
		GlStateManager.disableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y + 0.1F, (float) z - scale/2.0F);
		GlStateManager.scale(scale, scale, scale);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		worldrenderer.startDrawingQuads();
		render.bindTexture(animation.texture);

		int frame = animation.frameSequence[entity.ticksExisted % animation.frameSequence.length];

		double minU = 0.0;
		double maxU = 1.0;
		double minV = (1.0 / animation.totalFrames) * frame;
		double maxV = (1.0 / animation.totalFrames) * (frame + 1);

		worldrenderer.addVertexWithUV(0.5, 0.0, 0.0, maxU, maxV);
		worldrenderer.addVertexWithUV(-0.5, 0.0, 0.0, minU, maxV);
		worldrenderer.addVertexWithUV(-0.5, 1.0, 0.0, minU, minV);
		worldrenderer.addVertexWithUV(0.5, 1.0, 0.0, maxU, minV);

		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
	}
}