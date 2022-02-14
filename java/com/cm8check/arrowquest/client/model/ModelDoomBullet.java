package com.cm8check.arrowquest.client.model;

import com.cm8check.arrowquest.entity.EntityDoomBullet;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDoomBullet extends ModelBase{
	private final float size = 0.0825F;
	private ModelRenderer[] magicBits = new ModelRenderer[12];
	
	public ModelDoomBullet(){
		textureWidth = 128;
		textureHeight = 64;
		
		for (int i = 0; i < this.magicBits.length; i++){
            this.magicBits[i] = new ModelRenderer(this, 3, 20);
            this.magicBits[i].addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
        }
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, size);
		setRotationAngles(f, f1, f2, f3, f4, size, entity);
		
		for (int i = 0; i < this.magicBits.length; i++){
            this.magicBits[i].render(size*2);
        }
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity){
		EntityDoomBullet bullet = (EntityDoomBullet) entity;
		float f10 = bullet.spreadFloat;
		
		float f6 = f2 * (float) Math.PI * -0.1F;
		for (int i = 0; i < this.magicBits.length/2; i++){
			this.magicBits[i].rotationPointX = MathHelper.cos(f6) * f10;
			this.magicBits[i].rotationPointZ = MathHelper.sin(f6) * f10;
			f6++;
		}
		
		f6 = f2 * (float) Math.PI * -0.1F;
		for (int i = this.magicBits.length/2; i < this.magicBits.length; i++){
			this.magicBits[i].rotationPointX = MathHelper.cos(f6) * f10;
			this.magicBits[i].rotationPointY = MathHelper.sin(f6) * f10;
			f6++;
		}
	}
}