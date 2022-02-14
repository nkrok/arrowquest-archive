package com.cm8check.arrowquest.client.model;

import com.cm8check.arrowquest.entity.EntityFinalBoss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFinalBoss extends ModelBase{
	ModelRenderer BodyMain;
	ModelRenderer Head1;
	ModelRenderer Head2;
	ModelRenderer Head3;
	ModelRenderer Head4;
	ModelRenderer HeadMiddle1;
	ModelRenderer HeadMiddle2;
	ModelRenderer HeadMiddle3;
	ModelRenderer HeadMiddle4;
	ModelRenderer HeadMiddle5;
	ModelRenderer Arm1;
	ModelRenderer Arm2;
	ModelRenderer Arm3;
	ModelRenderer Arm4;
	
	private ModelRenderer[] legs = new ModelRenderer[16];
	
	private final float size = 0.125F;

	public ModelFinalBoss(){
		textureWidth = 256;
		textureHeight = 128;

		BodyMain = new ModelRenderer(this, 0, 0);
		BodyMain.addBox(0F, 0F, 0F, 48, 55, 48);
		BodyMain.setRotationPoint(-24F, -31F, -24F);
		BodyMain.setTextureSize(256, 128);
		BodyMain.mirror = true;
		setRotation(BodyMain, 0F, 0F, 0F);
		Head1 = new ModelRenderer(this, 0, 105);
		Head1.addBox(0F, 0F, 0F, 7, 7, 7);
		Head1.setRotationPoint(17F, -38F, 17F);
		Head1.setTextureSize(256, 128);
		Head1.mirror = true;
		setRotation(Head1, 0F, 0F, 0F);
		Head2 = new ModelRenderer(this, 0, 105);
		Head2.addBox(0F, 0F, 0F, 7, 7, 7);
		Head2.setRotationPoint(-24F, -38F, 17F);
		Head2.setTextureSize(256, 128);
		Head2.mirror = true;
		setRotation(Head2, 0F, 0F, 0F);
		Head3 = new ModelRenderer(this, 0, 105);
		Head3.addBox(0F, 0F, 0F, 7, 7, 7);
		Head3.setRotationPoint(17F, -38F, -24F);
		Head3.setTextureSize(256, 128);
		Head3.mirror = true;
		setRotation(Head3, 0F, 0F, 0F);
		Head4 = new ModelRenderer(this, 0, 105);
		Head4.addBox(0F, 0F, 0F, 7, 7, 7);
		Head4.setRotationPoint(-24F, -38F, -24F);
		Head4.setTextureSize(256, 128);
		Head4.mirror = true;
		setRotation(Head4, 0F, 0F, 0F);
		HeadMiddle1 = new ModelRenderer(this, 29, 105);
		HeadMiddle1.addBox(0F, 0F, 0F, 9, 12, 9);
		HeadMiddle1.setRotationPoint(-4F, -48F, -4F);
		HeadMiddle1.setTextureSize(256, 128);
		HeadMiddle1.mirror = true;
		setRotation(HeadMiddle1, 0F, 0F, 0F);
		HeadMiddle2 = new ModelRenderer(this, 29, 105);
		HeadMiddle2.addBox(0F, 0F, 0F, 9, 12, 9);
		HeadMiddle2.setRotationPoint(7F, -43F, -4F);
		HeadMiddle2.setTextureSize(256, 128);
		HeadMiddle2.mirror = true;
		setRotation(HeadMiddle2, 0F, 0F, 0F);
		HeadMiddle3 = new ModelRenderer(this, 29, 105);
		HeadMiddle3.addBox(0F, 0F, 0F, 9, 12, 9);
		HeadMiddle3.setRotationPoint(-15F, -43F, -4F);
		HeadMiddle3.setTextureSize(256, 128);
		HeadMiddle3.mirror = true;
		setRotation(HeadMiddle3, 0F, 0F, 0F);
		HeadMiddle4 = new ModelRenderer(this, 29, 105);
		HeadMiddle4.addBox(0F, 0F, 0F, 9, 12, 9);
		HeadMiddle4.setRotationPoint(-4F, -43F, 7F);
		HeadMiddle4.setTextureSize(256, 128);
		HeadMiddle4.mirror = true;
		setRotation(HeadMiddle4, 0F, 0F, 0F);
		HeadMiddle5 = new ModelRenderer(this, 29, 105);
		HeadMiddle5.addBox(0F, 0F, 0F, 9, 12, 9);
		HeadMiddle5.setRotationPoint(-4F, -43F, -15F);
		HeadMiddle5.setTextureSize(256, 128);
		HeadMiddle5.mirror = true;
		setRotation(HeadMiddle5, 0F, 0F, 0F);
		Arm1 = new ModelRenderer(this, 193, 0);
		Arm1.addBox(0F, 0F, 0F, 10, 44, 6);
		Arm1.setRotationPoint(-4F, -3F, -30F);
		Arm1.setTextureSize(256, 128);
		Arm1.mirror = true;
		setRotation(Arm1, -0.0523599F, 0F, 0F);
		Arm2 = new ModelRenderer(this, 193, 0);
		Arm2.addBox(0F, 0F, 0F, 10, 44, 6);
		Arm2.setRotationPoint(-4F, -3F, 24F);
		Arm2.setTextureSize(256, 128);
		Arm2.mirror = true;
		setRotation(Arm2, 0.0523599F, 0F, 0F);
		Arm3 = new ModelRenderer(this, 193, 0);
		Arm3.addBox(0F, 0F, 0F, 10, 44, 6);
		Arm3.setRotationPoint(-30F, -3F, 5F);
		Arm3.setTextureSize(256, 128);
		Arm3.mirror = true;
		setRotation(Arm3, -0.0523599F, 1.570796F, 0F);
		Arm4 = new ModelRenderer(this, 193, 0);
		Arm4.addBox(0F, 0F, 0F, 10, 44, 6);
		Arm4.setRotationPoint(24F, -3F, 5F);
		Arm4.setTextureSize(256, 128);
		Arm4.mirror = true;
		setRotation(Arm4, 0.0523599F, 1.570796F, 0F);
		
		for (int i = 0; i < this.legs.length; i++){
			this.legs[i] = new ModelRenderer(this, 193, 0);
			this.legs[i].addBox(0F, 10F, 0F, 10, 44, 6);
			this.legs[i].setTextureSize(256, 128);
			this.legs[i].mirror = true;
			setRotation(this.legs[i], -0.174532925F, 0F, 0F);
        }
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, size);
		setRotationAngles(f, f1, f2, f3, f4, size, entity);
		BodyMain.render(size);
		Head1.render(size);
		Head2.render(size);
		Head3.render(size);
		Head4.render(size);
		HeadMiddle1.render(size);
		HeadMiddle2.render(size);
		HeadMiddle3.render(size);
		HeadMiddle4.render(size);
		HeadMiddle5.render(size);
		Arm1.render(size);
		Arm2.render(size);
		Arm3.render(size);
		Arm4.render(size);
		
		for (int i = 0; i < this.legs.length; i++){
            this.legs[i].render(size);
        }
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity){
		EntityFinalBoss boss = (EntityFinalBoss) entity;
		
		float f6 = f2 * (float) Math.PI * -0.025F;
		float f7 = 2.0F;
		
		for (int i = 0; i < this.legs.length/2; i++){
            this.legs[i].offsetX = MathHelper.cos(f6) * f7;
            this.legs[i].offsetZ = MathHelper.sin(f6) * f7;
            this.legs[i].rotateAngleY = MathHelper.cos(f6);
            f6++;
        }
		
		f6 = f2 * (float) Math.PI * boss.legSpeed;
		f7 = 4.0F;
		
		for (int i = this.legs.length/2; i < this.legs.length; i++){
            this.legs[i].offsetX = MathHelper.cos(f6) * f7;
            this.legs[i].offsetZ = MathHelper.sin(f6) * f7;
            this.legs[i].offsetY = 3.0F;
            this.legs[i].rotateAngleY = MathHelper.cos(f6);
            f6++;
        }
	}
}