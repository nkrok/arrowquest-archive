package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelFinalBoss;
import com.cm8check.arrowquest.entity.EntityFinalBoss;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFinalBoss extends RenderLiving {
	private static final ResourceLocation texture = new ResourceLocation("arrowquest",
			"textures/entity/entityFinalBoss.png");

	public RenderFinalBoss() {
		super(Minecraft.getMinecraft().getRenderManager(), new ModelFinalBoss(), 1.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
		EntityFinalBoss boss = (EntityFinalBoss) entity;
		super.doRender(entity, x, y + 10.0 + Math.sin(boss.floatLevel) * 0.2, z, p_76986_8_, partialTicks);
	}

	@Override
	protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {

	}
}