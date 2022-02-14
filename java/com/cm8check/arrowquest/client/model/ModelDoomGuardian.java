package com.cm8check.arrowquest.client.model;

import com.cm8check.arrowquest.entity.EntityDoomGuardian;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDoomGuardian extends ModelBase{
	ModelRenderer Head;
	ModelRenderer Body;
	ModelRenderer FrontPole1;
	ModelRenderer FrontPole2;
	ModelRenderer BackPole1;
	ModelRenderer BackPole2;
	ModelRenderer Hat1;
	ModelRenderer Hat2;
	ModelRenderer Hat3;
	ModelRenderer Hat4;
	ModelRenderer CapeBack;
	ModelRenderer CapeHead;
	ModelRenderer CapeLeftShoulder;
	ModelRenderer CapeRightShoulder;
	ModelRenderer CapeFloor;
	
	private final float size = 0.0825F;
	
	private ModelRenderer[] magicBits = new ModelRenderer[24];

	public ModelDoomGuardian(){
		textureWidth = 128;
		textureHeight = 64;

		Head = new ModelRenderer(this, 0, 0);
		Head.addBox(-4F, 0F, -2F, 8, 6, 8);
		Head.setRotationPoint(0F, -14F, -2F);
		Head.setTextureSize(128, 64);
		Head.mirror = true;
		setRotation(Head, 0F, 0F, 0F);
		Body = new ModelRenderer(this, 0, 28);
		Body.addBox(0F, 0F, 0F, 16, 27, 8);
		Body.setRotationPoint(-8F, -7F, -4F);
		Body.setTextureSize(128, 64);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
		FrontPole1 = new ModelRenderer(this, 33, 0);
		FrontPole1.addBox(0F, 0F, 0F, 2, 17, 2);
		FrontPole1.setRotationPoint(-5F, -4F, -6F);
		FrontPole1.setTextureSize(128, 64);
		FrontPole1.mirror = true;
		setRotation(FrontPole1, 0F, 0F, -0.5235988F);
		FrontPole2 = new ModelRenderer(this, 33, 0);
		FrontPole2.addBox(0F, 0F, 0F, 2, 17, 2);
		FrontPole2.setRotationPoint(4F, -5F, -6F);
		FrontPole2.setTextureSize(128, 64);
		FrontPole2.mirror = true;
		setRotation(FrontPole2, 0F, 0F, 0.5235988F);
		BackPole1 = new ModelRenderer(this, 33, 0);
		BackPole1.addBox(0F, 0F, 0F, 2, 17, 2);
		BackPole1.setRotationPoint(4F, -5F, 4F);
		BackPole1.setTextureSize(128, 64);
		BackPole1.mirror = true;
		setRotation(BackPole1, 0F, 0F, 0.1745329F);
		BackPole2 = new ModelRenderer(this, 33, 0);
		BackPole2.addBox(0F, 0F, 0F, 2, 17, 2);
		BackPole2.setRotationPoint(-5F, -4F, 4F);
		BackPole2.setTextureSize(128, 64);
		BackPole2.mirror = true;
		setRotation(BackPole2, 0F, 0F, -0.1745329F);
		Hat1 = new ModelRenderer(this, 0, 17);
		Hat1.addBox(0F, 0F, 0F, 2, 5, 2);
		Hat1.setRotationPoint(-4F, -22F, -4F);
		Hat1.setTextureSize(128, 64);
		Hat1.mirror = true;
		setRotation(Hat1, 0F, 0F, 0F);
		Hat2 = new ModelRenderer(this, 0, 17);
		Hat2.addBox(0F, 0F, 0F, 2, 5, 2);
		Hat2.setRotationPoint(-4F, -22F, 2F);
		Hat2.setTextureSize(128, 64);
		Hat2.mirror = true;
		setRotation(Hat2, 0F, 0F, 0F);
		Hat3 = new ModelRenderer(this, 0, 17);
		Hat3.addBox(0F, 0F, 0F, 2, 5, 2);
		Hat3.setRotationPoint(1F, -22F, -4F);
		Hat3.setTextureSize(128, 64);
		Hat3.mirror = true;
		setRotation(Hat3, 0F, 0F, 0F);
		Hat4 = new ModelRenderer(this, 0, 17);
		Hat4.addBox(0F, 0F, 0F, 2, 5, 2);
		Hat4.setRotationPoint(1F, -22F, 2F);
		Hat4.setTextureSize(128, 64);
		Hat4.mirror = true;
		setRotation(Hat4, 0F, 0F, 0F);
		CapeBack = new ModelRenderer(this, 51, 0);
		CapeBack.addBox(0F, 0F, 0F, 16, 31, 1);
		CapeBack.setRotationPoint(-8F, -7F, 6F);
		CapeBack.setTextureSize(128, 64);
		CapeBack.mirror = true;
		setRotation(CapeBack, 0.0872665F, 0F, 0F);
		CapeHead = new ModelRenderer(this, 51, 50);
		CapeHead.addBox(0F, 0F, 0F, 16, 1, 9);
		CapeHead.setRotationPoint(-8F, -8F, -3F);
		CapeHead.setTextureSize(128, 64);
		CapeHead.mirror = true;
		setRotation(CapeHead, 0F, 0F, 0F);
		CapeLeftShoulder = new ModelRenderer(this, 51, 33);
		CapeLeftShoulder.addBox(0F, 0F, 0F, 1, 7, 9);
		CapeLeftShoulder.setRotationPoint(8F, -7F, -3F);
		CapeLeftShoulder.setTextureSize(128, 64);
		CapeLeftShoulder.mirror = true;
		setRotation(CapeLeftShoulder, 0F, 0F, 0F);
		CapeRightShoulder = new ModelRenderer(this, 51, 33);
		CapeRightShoulder.addBox(0F, 0F, 0F, 1, 7, 9);
		CapeRightShoulder.setRotationPoint(-9F, -7F, -3F);
		CapeRightShoulder.setTextureSize(128, 64);
		CapeRightShoulder.mirror = true;
		setRotation(CapeRightShoulder, 0F, 0F, 0F);
		CapeFloor = new ModelRenderer(this, 9, 22);
		CapeFloor.addBox(0F, 0F, 0F, 16, 1, 4);
		CapeFloor.setRotationPoint(-8F, 23F, 9F);
		CapeFloor.setTextureSize(128, 64);
		CapeFloor.mirror = true;
		setRotation(CapeFloor, 0F, 0F, 0F);
		
		for (int i = 0; i < this.magicBits.length; i++){
            this.magicBits[i] = new ModelRenderer(this, 3, 20);
            this.magicBits[i].addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
        }
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, size);
		setRotationAngles(f, f1, f2, f3, f4, size, entity);
		Head.render(size);
		Body.render(size);
		FrontPole1.render(size);
		FrontPole2.render(size);
		BackPole1.render(size);
		BackPole2.render(size);
		Hat1.render(size);
		Hat2.render(size);
		Hat3.render(size);
		Hat4.render(size);
		CapeBack.render(size);
		CapeHead.render(size);
		CapeLeftShoulder.render(size);
		CapeRightShoulder.render(size);
		CapeFloor.render(size);
		
		for (int i = 0; i < this.magicBits.length; i++){
            this.magicBits[i].render(size*2);
        }
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity){
		EntityDoomGuardian guardian = (EntityDoomGuardian) entity;
		float f7 = Math.max(guardian.magicBitLevel, 2.0F);
		float f8 = 9.0F + (9.0F - f7);
		float f9 = -0.05F;
		if (f7 < 7.0F){
			f9 = -0.1F;
		}
		
		float f6 = f2 * (float) Math.PI * f9;
		
		for (int i = 0; i < this.magicBits.length/2; i++){
            this.magicBits[i].rotationPointY = MathHelper.cos(f6) * f7;
            this.magicBits[i].rotationPointX = MathHelper.cos(f6) * f8;
            this.magicBits[i].rotationPointZ = MathHelper.sin(f6) * f8;
            f6++;
        }
		
		f6 = f2 * (float) Math.PI * f9;
		
		for (int i = this.magicBits.length/2; i < this.magicBits.length; i++){
            this.magicBits[i].rotationPointY = -MathHelper.cos(f6) * f7;
            this.magicBits[i].rotationPointX = MathHelper.cos(f6) * f8;
            this.magicBits[i].rotationPointZ = MathHelper.sin(f6) * f8;
            f6++;
        }
	}
}