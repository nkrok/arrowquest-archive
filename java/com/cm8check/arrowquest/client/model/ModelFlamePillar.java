package com.cm8check.arrowquest.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFlamePillar extends ModelBase {
	ModelRenderer CenterPillar;
	
	private ModelRenderer[] sidePillars = new ModelRenderer[4];

	public ModelFlamePillar() {
		textureWidth = 32;
		textureHeight = 64;

		CenterPillar = new ModelRenderer(this, 0, 0);
		CenterPillar.addBox(-2F, 0F, -2F, 4, 32, 4);
		CenterPillar.setRotationPoint(0F, -7F, 0F);
		CenterPillar.setTextureSize(32, 64);
		CenterPillar.mirror = true;
		setRotation(CenterPillar, 0F, 0F, 0F);
		
		for (int i = 0; i < sidePillars.length; i++) {
			sidePillars[i] = new ModelRenderer(this, 0, 0);
			sidePillars[i].addBox(8F, 0F, -2F, 2, 32, 2);
			sidePillars[i].setRotationPoint(0F, -7F, 0F);
			sidePillars[i].setTextureSize(32, 64);
			sidePillars[i].mirror = true;
			setRotation(sidePillars[i], 0F, (float) (i * Math.PI / 2), 0F);
		}
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		CenterPillar.render(f5);
		
		for (ModelRenderer model : sidePillars) {
			model.render(f5);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		float rot = f2 / 4;
		
		for (int i = 0; i < sidePillars.length; i++) {
			sidePillars[i].rotateAngleY = (float) (i * Math.PI / 2) + rot;
		}
	}

}