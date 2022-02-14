package com.cm8check.arrowquest.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelElectricOrb extends ModelBase {
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	
	ModelRenderer[] longThings = new ModelRenderer[16];

	public ModelElectricOrb() {
		textureWidth = 16;
		textureHeight = 16;

		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.addBox(-1F, -3F, -1F, 2, 6, 2);
		Shape1.setRotationPoint(0F, 21F, 0F);
		Shape1.setTextureSize(16, 16);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 0, 0);
		Shape2.addBox(-3F, -1F, -1F, 6, 2, 2);
		Shape2.setRotationPoint(0F, 21F, 0F);
		Shape2.setTextureSize(16, 16);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 0, 0);
		Shape3.addBox(-1F, -1F, -3F, 2, 2, 6);
		Shape3.setRotationPoint(0F, 21F, 0F);
		Shape3.setTextureSize(16, 16);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape4 = new ModelRenderer(this, 0, 0);
		Shape4.addBox(-2F, -2F, -2F, 4, 4, 4);
		Shape4.setRotationPoint(0F, 21F, 0F);
		Shape4.setTextureSize(16, 16);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
		
		for (int i = 0; i < longThings.length; i++) {
			longThings[i] = new ModelRenderer(this, 0, 0);
			longThings[i].addBox(4F, -0.5F, -0.5F, 10, 1, 1);
			longThings[i].setRotationPoint(0F, 21F, 0F);
			longThings[i].setTextureSize(16, 16);
			longThings[i].mirror = true;
			setRotation(longThings[i], 0F, 0F, 0F);
		}
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		
		for (ModelRenderer model : longThings) {
			model.render(f5);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		float rot = f2 / 8;
		
		for (int i = 0; i < longThings.length; i++) {
			longThings[i].rotateAngleY = (float) (i * Math.PI / 8) + rot;
			longThings[i].rotateAngleZ = (float) (i * Math.PI / 4) + rot;
		}
	}

}