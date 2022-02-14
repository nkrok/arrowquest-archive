package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelPoliceMissile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPoliceMissile extends Render {
	private static final ResourceLocation texture = new ResourceLocation("arrowquest",
			"textures/entity/entityPoliceMissile.png");
	private ModelBase model;

	public RenderPoliceMissile() {
		super(Minecraft.getMinecraft().getRenderManager());
		this.model = new ModelPoliceMissile();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.rotate(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.prevRotationPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, -2.8F, 0.0F);
		float f2 = 0.15F;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		this.bindEntityTexture(entity);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f2);
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
	}
}