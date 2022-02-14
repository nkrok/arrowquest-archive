package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelDoomBullet;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDoomBullet extends Render {
	private static final ResourceLocation texture = new ResourceLocation("arrowquest",
			"textures/entity/entityDoomGuardian.png");
	private ModelBase model;

	public RenderDoomBullet() {
		super(Minecraft.getMinecraft().getRenderManager());
		this.model = new ModelDoomBullet();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
		float f5 = entity.ticksExisted + partialTicks;

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		float f2 = 0.0625F;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		this.bindEntityTexture(entity);
		this.model.render(entity, 0.0F, 0.0F, f5, 0.0F, 0.0F, f2);
		GlStateManager.popMatrix();
		
		AnimationHelper.renderAnimation(AnimationHelper.doomBullet, this, this.renderManager, entity, x, y - 0.35, z, 1.0F);
	}
}