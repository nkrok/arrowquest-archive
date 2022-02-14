package com.cm8check.arrowquest.client.model;

import com.cm8check.arrowquest.entity.EntityRaceBase;
import com.cm8check.arrowquest.entity.EntityWizardBoss;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWizard extends ModelBiped{
	ModelRenderer HatBase;
	ModelRenderer HatPiece1;
	ModelRenderer HatPiece2;
	ModelRenderer HatPiece3;
	ModelRenderer HatPiece4;
	
	private final float modelSize;
	
	public ModelWizard(float size){
		this.textureWidth = 64;
		this.textureHeight = 64;
		
		this.modelSize = size;
		
		HatBase = new ModelRenderer(this, 0, 33);
		HatBase.addBox(0F, 0F, 0F, 10, 3, 10);
		HatBase.setRotationPoint(-5F, -8F, -5F);
		HatBase.setTextureSize(64, 64);
		HatBase.mirror = true;
		setRotation(HatBase, 0F, 0F, 0F);
		HatPiece1 = new ModelRenderer(this, 0, 33);
		HatPiece1.addBox(1F, 0F, 0F, 8, 2, 8);
		HatPiece1.setRotationPoint(-5F, -10F, -4F);
		HatPiece1.setTextureSize(64, 64);
		HatPiece1.mirror = true;
		setRotation(HatPiece1, 0F, 0F, 0F);
		HatPiece2 = new ModelRenderer(this, 0, 33);
		HatPiece2.addBox(0F, 0F, 0F, 6, 2, 6);
		HatPiece2.setRotationPoint(-3F, -12F, -3F);
		HatPiece2.setTextureSize(64, 64);
		HatPiece2.mirror = true;
		setRotation(HatPiece2, 0F, 0F, 0F);
		HatPiece3 = new ModelRenderer(this, 0, 33);
		HatPiece3.addBox(0F, 0F, 0F, 4, 2, 4);
		HatPiece3.setRotationPoint(-1F, -14F, -2F);
		HatPiece3.setTextureSize(64, 64);
		HatPiece3.mirror = true;
		setRotation(HatPiece3, 0F, 0F, 0F);
		HatPiece4 = new ModelRenderer(this, 0, 33);
		HatPiece4.addBox(0F, 0F, 0F, 2, 2, 2);
		HatPiece4.setRotationPoint(2F, -15F, -1F);
		HatPiece4.setTextureSize(64, 64);
		HatPiece4.mirror = true;
		setRotation(HatPiece4, 0F, 0F, 0F);
		
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -5F, -4F, 8, 5, 8);
		bipedHead.setRotationPoint(0F, 3F, 0F);
		bipedHead.setTextureSize(64, 64);
		bipedHead.mirror = true;
		setRotation(bipedHead, 0F, 0F, 0F);
		bipedBody = new ModelRenderer(this, 16, 16);
		bipedBody.addBox(-4F, 0F, -2F, 8, 12, 4);
		bipedBody.setRotationPoint(0F, 0F, 0F);
		bipedBody.setTextureSize(64, 64);
		bipedBody.mirror = true;
		setRotation(bipedBody, 0F, 0F, 0F);
		bipedRightArm = new ModelRenderer(this, 40, 16);
		bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4);
		bipedRightArm.setRotationPoint(-5F, 2F, 0F);
		bipedRightArm.setTextureSize(64, 64);
		bipedRightArm.mirror = true;
		setRotation(bipedRightArm, 0F, 0F, 0F);
		bipedLeftArm = new ModelRenderer(this, 40, 16);
		bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4);
		bipedLeftArm.setRotationPoint(5F, 2F, 0F);
		bipedLeftArm.setTextureSize(64, 64);
		bipedLeftArm.mirror = true;
		setRotation(bipedLeftArm, 0F, 0F, 0F);
		bipedRightLeg = new ModelRenderer(this, 0, 16);
		bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
		bipedRightLeg.setRotationPoint(-2F, 12F, 0F);
		bipedRightLeg.setTextureSize(64, 64);
		bipedRightLeg.mirror = true;
		setRotation(bipedRightLeg, 0F, 0F, 0F);
		bipedLeftLeg = new ModelRenderer(this, 0, 16);
		bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
		bipedLeftLeg.setRotationPoint(2F, 12F, 0F);
		bipedLeftLeg.setTextureSize(64, 64);
		bipedLeftLeg.mirror = true;
		setRotation(bipedLeftLeg, 0F, 0F, 0F);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		setRotationAngles(f, f1, f2, f3, f4, modelSize, entity);
		HatBase.render(modelSize);
		HatPiece1.render(modelSize);
		HatPiece2.render(modelSize);
		HatPiece3.render(modelSize);
		HatPiece4.render(modelSize);
		
		bipedHead.render(modelSize);
		bipedBody.render(modelSize);
		bipedRightArm.render(modelSize);
		bipedLeftArm.render(modelSize);
		bipedRightLeg.render(modelSize);
		bipedLeftLeg.render(modelSize);
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity){
		this.bipedHead.rotateAngleY = f3 / (180F / (float)Math.PI);
		this.bipedHead.rotateAngleX = f4 / (180F / (float)Math.PI);
		this.bipedRightArm.rotateAngleX = (MathHelper.cos(f * 0.6662F + (float)Math.PI) * 2.0F * f1 * 0.5F) * 0.5F - ((float)Math.PI / 10F);
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;

		this.bipedBody.rotateAngleX = 0.0F;
		this.bipedRightLeg.rotationPointZ = 0.1F;
		this.bipedLeftLeg.rotationPointZ = 0.1F;
		this.bipedRightLeg.rotationPointY = 12.0F;
		this.bipedLeftLeg.rotationPointY = 12.0F;
		this.bipedHead.rotationPointY = 0.0F;

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		
		if (entity instanceof EntityWizardBoss){
			EntityWizardBoss wizard = (EntityWizardBoss) entity;
			
			if (wizard.armState == 1){
				this.bipedRightArm.rotateAngleX = 3.14F;
				this.bipedLeftArm.rotateAngleX = 3.14F;
			}else if (wizard.armState == 2){
				this.bipedRightArm.rotateAngleX = -1.57F;
			}else if (wizard.armState == 3){
				this.bipedRightArm.rotateAngleX = -1.57F;
				this.bipedLeftArm.rotateAngleX = -1.57F;
			}
		}else{
			EntityRaceBase wizard = (EntityRaceBase) entity;
			if (wizard.isBlocking){
				this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10.0F) * 3.0F;
				this.bipedRightArm.rotateAngleY = -0.5235988F;
			}
		}
	}
}