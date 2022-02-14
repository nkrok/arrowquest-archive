package com.cm8check.arrowquest.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFinalBossCore extends ModelBase{
	ModelRenderer MainBox;
	ModelRenderer Leg1;
	ModelRenderer Leg2;
	ModelRenderer Leg3;
	ModelRenderer Leg4;
	ModelRenderer TopBase;
	ModelRenderer Stand1;
	ModelRenderer Stand2;
	ModelRenderer Stand3;
	ModelRenderer Stand4;
	
	private final float size = 0.09375F;

	public ModelFinalBossCore() {
		textureWidth = 256;
		textureHeight = 128;

		MainBox = new ModelRenderer(this, 0, 0);
		MainBox.addBox(0F, 0F, 0F, 16, 32, 16);
		MainBox.setRotationPoint(-8F, -8F, -8F);
		MainBox.setTextureSize(256, 128);
		MainBox.mirror = true;
		setRotation(MainBox, 0F, 0F, 0F);
		Leg1 = new ModelRenderer(this, 0, 49);
		Leg1.addBox(0F, 0F, 0F, 8, 8, 8);
		Leg1.setRotationPoint(-12F, 16F, 4F);
		Leg1.setTextureSize(256, 128);
		Leg1.mirror = true;
		setRotation(Leg1, 0F, 0F, 0F);
		Leg2 = new ModelRenderer(this, 0, 49);
		Leg2.addBox(0F, 0F, 0F, 8, 8, 8);
		Leg2.setRotationPoint(-12F, 16F, -12F);
		Leg2.setTextureSize(256, 128);
		Leg2.mirror = true;
		setRotation(Leg2, 0F, 0F, 0F);
		Leg3 = new ModelRenderer(this, 0, 49);
		Leg3.addBox(0F, 0F, 0F, 8, 8, 8);
		Leg3.setRotationPoint(4F, 16F, -12F);
		Leg3.setTextureSize(256, 128);
		Leg3.mirror = true;
		setRotation(Leg3, 0F, 0F, 0F);
		Leg4 = new ModelRenderer(this, 0, 49);
		Leg4.addBox(0F, 0F, 0F, 8, 8, 8);
		Leg4.setRotationPoint(4F, 16F, 4F);
		Leg4.setTextureSize(256, 128);
		Leg4.mirror = true;
		setRotation(Leg4, 0F, 0F, 0F);
		TopBase = new ModelRenderer(this, 65, 0);
		TopBase.addBox(0F, 0F, 0F, 24, 6, 24);
		TopBase.setRotationPoint(-12F, -2F, -12F);
		TopBase.setTextureSize(256, 128);
		TopBase.mirror = true;
		setRotation(TopBase, 0F, 0F, 0F);
		Stand1 = new ModelRenderer(this, 65, 31);
		Stand1.addBox(0F, 0F, 0F, 4, 25, 2);
		Stand1.setRotationPoint(-2F, 0F, 11F);
		Stand1.setTextureSize(256, 128);
		Stand1.mirror = true;
		setRotation(Stand1, 0.1396263F, 0F, 0F);
		Stand2 = new ModelRenderer(this, 65, 31);
		Stand2.addBox(0F, 0F, 0F, 4, 25, 2);
		Stand2.setRotationPoint(-2F, 0F, -13F);
		Stand2.setTextureSize(256, 128);
		Stand2.mirror = true;
		setRotation(Stand2, -0.1396263F, 0F, 0F);
		Stand3 = new ModelRenderer(this, 65, 31);
		Stand3.addBox(0F, 0F, 0F, 4, 25, 2);
		Stand3.setRotationPoint(-13F, 0F, 2F);
		Stand3.setTextureSize(256, 128);
		Stand3.mirror = true;
		setRotation(Stand3, -0.1396263F, 1.570796F, 0F);
		Stand4 = new ModelRenderer(this, 65, 31);
		Stand4.addBox(0F, 0F, 0F, 4, 25, 2);
		Stand4.setRotationPoint(11F, 0F, 2F);
		Stand4.setTextureSize(256, 128);
		Stand4.mirror = true;
		setRotation(Stand4, 0.122173F, 1.570796F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, size);
		setRotationAngles(f, f1, f2, f3, f4, size, entity);
		MainBox.render(size);
		Leg1.render(size);
		Leg2.render(size);
		Leg3.render(size);
		Leg4.render(size);
		TopBase.render(size);
		Stand1.render(size);
		Stand2.render(size);
		Stand3.render(size);
		Stand4.render(size);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity){
		
	}
}