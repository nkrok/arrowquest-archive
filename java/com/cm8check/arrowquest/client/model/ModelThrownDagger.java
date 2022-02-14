package com.cm8check.arrowquest.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelThrownDagger extends ModelBase{
	ModelRenderer handle;
	ModelRenderer hilt;
	ModelRenderer bladeBase;
	ModelRenderer bladeTip1;
	ModelRenderer bladeTip2;
	
	private final float size = 0.03125F;

	public ModelThrownDagger(){
		textureWidth = 64;
		textureHeight = 32;

		handle = new ModelRenderer(this, 0, 0);
		handle.addBox(0F, 0F, 0F, 1, 8, 3);
		handle.setRotationPoint(0F, 16F, -1F);
		handle.setTextureSize(64, 32);
		handle.mirror = true;
		setRotation(handle, 0F, 0F, 0F);
		hilt = new ModelRenderer(this, 0, 12);
		hilt.addBox(0F, 0F, 0F, 1, 2, 7);
		hilt.setRotationPoint(0F, 14F, -3F);
		hilt.setTextureSize(64, 32);
		hilt.mirror = true;
		setRotation(hilt, 0F, 0F, 0F);
		bladeBase = new ModelRenderer(this, 17, 0);
		bladeBase.addBox(0F, 0F, 0F, 1, 8, 5);
		bladeBase.setRotationPoint(0F, 6F, -2F);
		bladeBase.setTextureSize(64, 32);
		bladeBase.mirror = true;
		setRotation(bladeBase, 0F, 0F, 0F);
		bladeTip1 = new ModelRenderer(this, 0, 22);
		bladeTip1.addBox(0F, 0F, 0F, 1, 1, 3);
		bladeTip1.setRotationPoint(0F, 5F, -1F);
		bladeTip1.setTextureSize(64, 32);
		bladeTip1.mirror = true;
		setRotation(bladeTip1, 0F, 0F, 0F);
		bladeTip2 = new ModelRenderer(this, 9, 22);
		bladeTip2.addBox(0F, 0F, 0F, 1, 1, 1);
		bladeTip2.setRotationPoint(0F, 4F, 0F);
		bladeTip2.setTextureSize(64, 32);
		bladeTip2.mirror = true;
		setRotation(bladeTip2, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, size);
		setRotationAngles(f, f1, f2, f3, f4, size, entity);
		handle.render(size);
		hilt.render(size);
		bladeBase.render(size);
		bladeTip1.render(size);
		bladeTip2.render(size);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity){
		
	}
}