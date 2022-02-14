package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelPoliceLaser;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.entity.EntityGenericProjectile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGenericProjectile extends Render {
	private final ResourceLocation[] textures = {
		new ResourceLocation("arrowquest", "textures/entity/entityPoliceLaser.png"),
		null,
		null,
		null,
		null,
		null,
		null
	};
	
	private final ModelBase[] models = {
		new ModelPoliceLaser(),
		null,
		null,
		null,
		null,
		null,
		null
	};

	public RenderGenericProjectile() {
		super(Minecraft.getMinecraft().getRenderManager());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
		if (entity instanceof EntityGenericProjectile) {
			EntityGenericProjectile projectile = (EntityGenericProjectile) entity;
			int type = projectile.getType();
			
			if (models[type] != null) {
				GlStateManager.pushMatrix();
				GlStateManager.translate((float) x, (float) y, (float) z);
				GlStateManager.rotate(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(entity.prevRotationPitch, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.0F, -1.5F, 0.0F);
				float f2 = 0.0725F;
				GlStateManager.enableRescaleNormal();
				GlStateManager.enableAlpha();
				this.bindTexture(textures[type]);
				models[type].render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f2);
				GlStateManager.popMatrix();
			}

			super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
			
			switch (type) {
			case EntityGenericProjectile.TYPE_POLICE_LASER:
			{
				AnimationHelper.renderAnimation(AnimationHelper.policeLaserEffect, this, this.renderManager, entity, x, y - 0.35, z, 1.0F);
				break;
			}
			
			case EntityGenericProjectile.TYPE_GREEN_PROJECTILE:
			{
				AnimationHelper.renderAnimation(AnimationHelper.greenProjectile, this, this.renderManager, entity, x, y - 0.6, z, 2.0F);
				break;
			}
			
			case EntityGenericProjectile.TYPE_BLUE_PROJECTILE:
			{
				AnimationHelper.renderAnimation(AnimationHelper.blueProjectile, this, this.renderManager, entity, x, y - 0.6, z, 2.0F);
				break;
			}
			
			case EntityGenericProjectile.TYPE_EXPLOSION_BIT:
			{
				AnimationHelper.renderAnimation(AnimationHelper.flame, this, this.renderManager, entity, x, y - 0.5, z, 0.75F);
				break;
			}
			
			case EntityGenericProjectile.TYPE_PLASMA_BALL:
			{
				AnimationHelper.renderAnimation(AnimationHelper.plasmaBullet, this, this.renderManager, entity, x, y - 0.6, z, 1.5F);
				break;
			}
			
			case EntityGenericProjectile.TYPE_LITTLE_PLASMA_BALL:
			{
				AnimationHelper.renderAnimation(AnimationHelper.plasmaBullet, this, this.renderManager, entity, x, y - 0.3, z, 0.6F);
				break;
			}
			
			case EntityGenericProjectile.TYPE_SOUL_STEAL:
			{
				AnimationHelper.renderAnimation(AnimationHelper.wizardBeamEffect, this, this.renderManager, entity, x, y - 0.6, z, 2.0F);
			}
			}
		}
	}
}