package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelHuman;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.entity.EntityBanishedSoul;
import com.cm8check.arrowquest.entity.EntityPhantasm;
import com.cm8check.arrowquest.entity.EntityPolice;
import com.cm8check.arrowquest.entity.EntityPoliceBoss;
import com.cm8check.arrowquest.entity.EntityWizard;
import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHuman extends RenderLiving{
	public static ResourceLocation humanTex = new ResourceLocation("arrowquest", "textures/entity/entityHuman.png");
	public static ResourceLocation pirateTex = new ResourceLocation("arrowquest", "textures/entity/entityPirate.png");
	public static ResourceLocation castleSoldierBossTex = new ResourceLocation("arrowquest", "textures/entity/entityCastleSoldierKing.png");
	public static ResourceLocation orcTex = new ResourceLocation("arrowquest", "textures/entity/entityOrc.png");
	public static ResourceLocation elfTex = new ResourceLocation("arrowquest", "textures/entity/entityElf.png");
	public static ResourceLocation pirateCaptainTex = new ResourceLocation("arrowquest", "textures/entity/entityPirateCaptain.png");
	public static ResourceLocation dwarfTex = new ResourceLocation("arrowquest", "textures/entity/entityDwarf.png");
	public static ResourceLocation policeTex = new ResourceLocation("arrowquest", "textures/entity/entityPolice.png");
	public static ResourceLocation policeBossTex = new ResourceLocation("arrowquest", "textures/entity/entityPoliceBoss.png");
	public static ResourceLocation obsidianWarriorTex = new ResourceLocation("arrowquest", "textures/entity/entityObsidianWarrior.png");
	public static ResourceLocation vampireTex = new ResourceLocation("arrowquest", "textures/entity/entityVampire.png");
	public static ResourceLocation furnaceMonsterTex = new ResourceLocation("arrowquest", "textures/entity/entityFurnaceMonster.png");
	public static ResourceLocation phantasmTex = new ResourceLocation("arrowquest", "textures/entity/entityPhantasm.png");
	public static ResourceLocation banishedSoulTex = new ResourceLocation("arrowquest", "textures/entity/entityBanishedSoul.png");
	private ResourceLocation texture;
	
	public RenderHuman(ResourceLocation tex){
		super(Minecraft.getMinecraft().getRenderManager(), new ModelHuman(0.0F, false), 0.5F);
		this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.texture = tex;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return texture;
	}
	
	@Override
	protected void func_177093_a(EntityLivingBase entity, float p_177093_2_, float p_177093_3_, float p_177093_4_, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
		boolean flag = false;
		if (entity instanceof EntityPolice && entity.dimension == ModLib.dimFinalBossID) {
			float f6 = 1.25F;
			GlStateManager.pushMatrix();
			GlStateManager.scale(f6, f6, f6);
			GlStateManager.translate(0.0F, -6.0F * p_177093_8_, 0.0F);
			
			flag = true;
		}
		
		super.func_177093_a(entity, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
		
		if (flag) {
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks){
		super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
		
		if (entity instanceof EntityPolice) {
			if (((EntityPolice) entity).bossRaiseArms) {
				AnimationHelper.renderAnimation(AnimationHelper.blueProjectile, this, this.renderManager, entity, x, y + 2.0D, z, 2.0F);
			}
		}
		else if (entity instanceof EntityPoliceBoss) {
			AnimationHelper.renderAnimation(AnimationHelper.policeBossAura, this, this.renderManager, entity, x, y - 0.9D, z, 3.5F);
		}
		else if (entity instanceof EntityPhantasm) {
			AnimationHelper.renderAnimation(AnimationHelper.phantasmEffect, this, this.renderManager, entity, x, y, z, 2.0F);
		}
		else if (entity instanceof EntityBanishedSoul) {
			AnimationHelper.renderAnimation(AnimationHelper.soulEffect, this, this.renderManager, entity, x, y, z, 2.5F);
		}
	}
}