package com.cm8check.arrowquest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

public class EntityExplosiveArrow extends EntityArrow{
	public EntityExplosiveArrow(World world){
		super(world);
	}
	
	public EntityExplosiveArrow(World worldIn, EntityLivingBase shooter, float p_i1756_3_){
		super(worldIn, shooter, p_i1756_3_);
	}
	
	public EntityExplosiveArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_){
		super(worldIn, shooter, p_i1755_3_, p_i1755_4_, p_i1755_5_);
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		
		if (this.arrowShake > 0 && !this.isDead){
			this.setDead();
		}
	}
	
	@Override
	public void setDead(){
		super.setDead();
		this.worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 3.0F, false);
	}
}