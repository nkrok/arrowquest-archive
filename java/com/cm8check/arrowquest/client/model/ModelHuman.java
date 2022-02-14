package com.cm8check.arrowquest.client.model;

import com.cm8check.arrowquest.entity.EntityDwarf;
import com.cm8check.arrowquest.entity.EntityPolice;
import com.cm8check.arrowquest.entity.EntityRaceBase;
import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHuman extends ModelBiped {
	public ModelRenderer bipedLeftArmwear;
	public ModelRenderer bipedRightArmwear;
	public ModelRenderer bipedLeftLegwear;
	public ModelRenderer bipedRightLegwear;
	public ModelRenderer bipedBodyWear;
	private ModelRenderer field_178729_w;
	private ModelRenderer field_178736_x;
	private boolean field_178735_y;

	public ModelHuman(float p_i46304_1_, boolean p_i46304_2_) {
		super(p_i46304_1_, 0.0F, 64, 64);
		this.field_178735_y = p_i46304_2_;
		this.field_178736_x = new ModelRenderer(this, 24, 0);
		this.field_178736_x.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, p_i46304_1_);
		this.field_178729_w = new ModelRenderer(this, 0, 0);
		this.field_178729_w.setTextureSize(64, 32);
		this.field_178729_w.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46304_1_);

		if (p_i46304_2_) {
			this.bipedLeftArm = new ModelRenderer(this, 32, 48);
			this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
			this.bipedRightArm = new ModelRenderer(this, 40, 16);
			this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
			this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
			this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
			this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
			this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
			this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
			this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
			this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
		} else {
			this.bipedLeftArm = new ModelRenderer(this, 32, 48);
			this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
			this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
			this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
			this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
			this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
			this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
			this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
		}

		this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
		this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
		this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i46304_1_ + 0.25F);
		this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity entity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,float p_78088_6_, float p_78088_7_) {
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, entity);
		GlStateManager.pushMatrix();

		if (entity instanceof EntityPolice && entity.dimension == ModLib.dimFinalBossID) {
			float f6 = 1.25F;
			GlStateManager.scale(f6, f6, f6);
			GlStateManager.translate(0.0F, -6.0F * p_78088_7_, 0.0F);
		}
		
		this.bipedHead.render(p_78088_7_);
		this.bipedBody.render(p_78088_7_);
		this.bipedRightArm.render(p_78088_7_);
		this.bipedLeftArm.render(p_78088_7_);
		this.bipedRightLeg.render(p_78088_7_);
		this.bipedLeftLeg.render(p_78088_7_);
		this.bipedHeadwear.render(p_78088_7_);
		
		
		this.bipedLeftLegwear.render(p_78088_7_);
		this.bipedRightLegwear.render(p_78088_7_);
		this.bipedLeftArmwear.render(p_78088_7_);
		this.bipedRightArmwear.render(p_78088_7_);
		this.bipedBodyWear.render(p_78088_7_);

		GlStateManager.popMatrix();
	}

	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity entity) {
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entity);

		if (entity.isSneaking()) {
			this.field_178729_w.rotationPointY = 2.0F;
		} else {
			this.field_178729_w.rotationPointY = 0.0F;
		}

		if (entity instanceof EntityRaceBase) {
			EntityRaceBase living = (EntityRaceBase) entity;
			if (living.isBlocking) {
				this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F
						- ((float) Math.PI / 10F) * 3.0F;
				this.bipedRightArm.rotateAngleY = -0.5235988F;
			} else if (living.aimCooldown == 0) {
				float f7 = 0.0F;
				float f9 = 0.0F;
				this.bipedRightArm.rotateAngleZ = 0.0F;
				this.bipedLeftArm.rotateAngleZ = 0.0F;
				this.bipedRightArm.rotateAngleY = (-(0.1F - f7 * 0.6F) + this.bipedHead.rotateAngleY);
				this.bipedLeftArm.rotateAngleY = (0.1F - f7 * 0.6F + this.bipedHead.rotateAngleY + 0.8F);
				this.bipedRightArm.rotateAngleX = (-1.570796F + this.bipedHead.rotateAngleX);
				this.bipedLeftArm.rotateAngleX = (-1.570796F + this.bipedHead.rotateAngleX);
				this.bipedRightArm.rotateAngleX -= f7 * 1.2F - f9 * 0.4F;
				this.bipedLeftArm.rotateAngleX -= f7 * 1.2F - f9 * 0.4F;
				this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
				this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
				this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
				this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
			} else if (living.bossRaiseArms) {
				this.bipedRightArm.rotateAngleX = 3.14F;
				this.bipedLeftArm.rotateAngleX = 3.14F;
			}
		}

		copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
		copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		copyModelAngles(this.bipedBody, this.bipedBodyWear);
	}

	public void setInvisible(boolean invisible) {
		super.setInvisible(invisible);
		this.bipedLeftArmwear.showModel = invisible;
		this.bipedRightArmwear.showModel = invisible;
		this.bipedLeftLegwear.showModel = invisible;
		this.bipedRightLegwear.showModel = invisible;
		this.bipedBodyWear.showModel = invisible;
		this.field_178729_w.showModel = invisible;
		this.field_178736_x.showModel = invisible;
	}

	public void postRenderArm(float p_178718_1_) {
		if (this.field_178735_y) {
			++this.bipedRightArm.rotationPointX;
			this.bipedRightArm.postRender(p_178718_1_);
			--this.bipedRightArm.rotationPointX;
		} else {
			this.bipedRightArm.postRender(p_178718_1_);
		}
	}
}