package com.cm8check.arrowquest.entity;

import com.cm8check.arrowquest.client.renderer.entity.animation.Animation;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityOneshotAnimation extends Entity {
	private Animation animation;
	private float scale;
	
	public EntityOneshotAnimation(World world) {
		super(world);
	}
	
	public EntityOneshotAnimation(World world, Animation animation, float scale, double x, double y, double z) {
		super(world);
		this.animation = animation;
		this.scale = scale;
		this.setPosition(x, y, z);
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
	
	public float getScale() {
		return this.scale;
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		
		if (this.ticksExisted >= this.animation.frameSequence.length) {
			this.setDead();
		}
	}

	@Override
	protected void entityInit() {
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		
	}
}
