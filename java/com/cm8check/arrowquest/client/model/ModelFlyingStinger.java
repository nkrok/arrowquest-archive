package com.cm8check.arrowquest.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFlyingStinger extends ModelBase{
	ModelRenderer body;
	ModelRenderer rightwing;
	ModelRenderer rightwing2;
	ModelRenderer leftwing;
	ModelRenderer leftwing2;
	ModelRenderer head;
	ModelRenderer back1;
	ModelRenderer back2;
	ModelRenderer back3;
	ModelRenderer back4;
	ModelRenderer back5;
	ModelRenderer back6;
	ModelRenderer back7;
	ModelRenderer leftwing3;
    ModelRenderer leftwing4;
    ModelRenderer rightwing3;
    ModelRenderer rightwing4;
    ModelRenderer tailTip;

	private final float size = 0.0625F;

	public ModelFlyingStinger(){
		textureWidth = 64;
		textureHeight = 32;

		body = new ModelRenderer(this, 26, 14);
		body.addBox(0F, 0.25F, 0F, 3, 2, 16);
		body.setRotationPoint(-1F, 19F, -8F);
		body.setTextureSize(64, 32);
		body.mirror = true;
		setRotation(body, 0F, 0F, 0F);
		rightwing = new ModelRenderer(this, 13, 6);
		rightwing.addBox(0F, 0.25F, 0F, 1, 1, 5);
		rightwing.setRotationPoint(0F, 19F, -3F);
		rightwing.setTextureSize(64, 32);
		rightwing.mirror = true;
		setRotation(rightwing, -0.3490659F, -1.570796F, 0F);
		rightwing2 = new ModelRenderer(this, 13, 13);
		rightwing2.addBox(0F, 0.25F, 0F, 1, 1, 5);
		rightwing2.setRotationPoint(0F, 19F, -5F);
		rightwing2.setTextureSize(64, 32);
		rightwing2.mirror = true;
		setRotation(rightwing2, -0.3490659F, -1.570796F, 0F);
		leftwing = new ModelRenderer(this, 0, 6);
		leftwing.addBox(0F, 0.25F, 0F, 1, 1, 5);
		leftwing.setRotationPoint(1F, 19F, -2F);
		leftwing.setTextureSize(64, 32);
		leftwing.mirror = true;
		setRotation(leftwing, -0.3490659F, 1.570796F, 0F);
		leftwing2 = new ModelRenderer(this, 0, 13);
		leftwing2.addBox(0F, 0.25F, 0F, 1, 1, 5);
		leftwing2.setRotationPoint(1F, 19F, -4F);
		leftwing2.setTextureSize(64, 32);
		leftwing2.mirror = true;
		setRotation(leftwing2, -0.3490659F, 1.570796F, 0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(0F, 0.25F, 0F, 1, 1, 1);
		head.setRotationPoint(0F, 19F, -9F);
		head.setTextureSize(64, 32);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		back1 = new ModelRenderer(this, 5, 0);
		back1.addBox(0F, 0.25F, 0F, 1, 3, 1);
		back1.setRotationPoint(0F, 16F, 7F);
		back1.setTextureSize(64, 32);
		back1.mirror = true;
		setRotation(back1, 0F, 0F, 0F);
		back2 = new ModelRenderer(this, 0, 0);
		back2.addBox(0F, 0.25F, 0F, 1, 1, 1);
		back2.setRotationPoint(0F, 18F, 5F);
		back2.setTextureSize(64, 32);
		back2.mirror = true;
		setRotation(back2, 0F, 0F, 0F);
		back3 = new ModelRenderer(this, 0, 0);
		back3.addBox(0F, 0.25F, 0F, 1, 1, 1);
		back3.setRotationPoint(0F, 18F, 3F);
		back3.setTextureSize(64, 32);
		back3.mirror = true;
		setRotation(back3, 0F, 0F, 0F);
		back4 = new ModelRenderer(this, 0, 0);
		back4.addBox(0F, 0.25F, 0F, 1, 1, 1);
		back4.setRotationPoint(0F, 18F, 1F);
		back4.setTextureSize(64, 32);
		back4.mirror = true;
		setRotation(back4, 0F, 0F, 0F);
		back5 = new ModelRenderer(this, 0, 0);
		back5.addBox(0F, 0.25F, 0F, 1, 1, 1);
		back5.setRotationPoint(0F, 18F, -1F);
		back5.setTextureSize(64, 32);
		back5.mirror = true;
		setRotation(back5, 0F, 0F, 0F);
		back6 = new ModelRenderer(this, 0, 0);
		back6.addBox(0F, 0.25F, 0F, 1, 1, 1);
		back6.setRotationPoint(0F, 18F, -3F);
		back6.setTextureSize(64, 32);
		back6.mirror = true;
		setRotation(back6, 0F, 0F, 0F);
		back7 = new ModelRenderer(this, 0, 0);
		back7.addBox(0F, 0.25F, 0F, 1, 1, 1);
		back7.setRotationPoint(0F, 18F, -5F);
		back7.setTextureSize(64, 32);
		back7.mirror = true;
		setRotation(back7, 0F, 0F, 0F);
		leftwing3 = new ModelRenderer(this, 0, 13);
		leftwing3.addBox(0F, 0.25F, 0F, 1, 1, 5);
		leftwing3.setRotationPoint(1F, 19F, 5F);
		leftwing3.setTextureSize(64, 32);
		leftwing3.mirror = true;
		setRotation(leftwing3, -0.3490659F, 1.570796F, 0F);
		leftwing4 = new ModelRenderer(this, 0, 6);
		leftwing4.addBox(0F, 0.25F, 0F, 1, 1, 5);
		leftwing4.setRotationPoint(1F, 19F, 7F);
		leftwing4.setTextureSize(64, 32);
		leftwing4.mirror = true;
		setRotation(leftwing4, -0.3490659F, 1.570796F, 0F);
		rightwing3 = new ModelRenderer(this, 13, 13);
		rightwing3.addBox(0F, 0.25F, 0F, 1, 1, 5);
		rightwing3.setRotationPoint(0F, 19F, 4F);
		rightwing3.setTextureSize(64, 32);
		rightwing3.mirror = true;
		setRotation(rightwing3, -0.3490659F, -1.570796F, 0F);
		rightwing4 = new ModelRenderer(this, 13, 6);
		rightwing4.addBox(0F, 0.25F, 0F, 1, 1, 5);
		rightwing4.setRotationPoint(0F, 19F, 6F);
		rightwing4.setTextureSize(64, 32);
		rightwing4.mirror = true;
		setRotation(rightwing4, -0.3490659F, -1.570796F, 0F);
		tailTip = new ModelRenderer(this, 0, 0);
		tailTip.addBox(0F, -1.75F, 6F, 1, 1, 1);
		tailTip.setRotationPoint(0F, 18F, 0F);
		tailTip.setTextureSize(64, 32);
		tailTip.mirror = true;
		setRotation(tailTip, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, size);
		setRotationAngles(f, f1, f2, f3, f4, size, entity);
		body.render(size);
		rightwing.render(size);
		rightwing2.render(size);
		leftwing.render(size);
		leftwing2.render(size);
		head.render(size);
		back1.render(size);
		back2.render(size);
		back3.render(size);
		back4.render(size);
		back5.render(size);
		back6.render(size);
		back7.render(size);
		leftwing3.render(size);
	    leftwing4.render(size);
	    rightwing3.render(size);
	    rightwing4.render(size);
	    tailTip.render(size);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity){
		float f9 = -(MathHelper.cos(f * 0.6662F * 2.0F + 0.0F) * 0.4F) * f1 * 0.1F;
        float f10 = -(MathHelper.cos(f * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * f1 * 0.1F;
        float f11 = -(MathHelper.cos(f * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * f1 * 0.1F;
        float f12 = -(MathHelper.cos(f * 0.6662F * 2.0F + ((float)Math.PI * 3F / 2F)) * 0.4F) * f1 * 0.1F;
        
        this.rightwing.rotateAngleY += f9;
        this.rightwing2.rotateAngleY += f10;
        this.rightwing3.rotateAngleY += f11;
        this.rightwing4.rotateAngleY += f12;
        this.leftwing.rotateAngleY += -f9;
        this.leftwing2.rotateAngleY += -f10;
        this.leftwing3.rotateAngleY += -f11;
        this.leftwing4.rotateAngleY += -f12;
	}
}