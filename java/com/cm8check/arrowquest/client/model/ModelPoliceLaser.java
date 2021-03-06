package com.cm8check.arrowquest.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPoliceLaser extends ModelBase{
	ModelRenderer laser;
	
	private final float size = 0.0725F;

	public ModelPoliceLaser(){
		textureWidth = 16;
		textureHeight = 16;

		laser = new ModelRenderer(this, 0, 0);
		laser.addBox(0F, 0F, 0F, 1, 1, 6);
		laser.setRotationPoint(0F, 23F, -3F);
		laser.setTextureSize(16, 16);
		laser.mirror = true;
		setRotation(laser, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		laser.render(f5);
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
