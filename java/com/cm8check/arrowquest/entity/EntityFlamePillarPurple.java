package com.cm8check.arrowquest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityFlamePillarPurple extends EntityFlamePillar {
	public EntityFlamePillarPurple(World world) {
		super(world);
		this.setType(1);
	}
	
	public EntityFlamePillarPurple(World world, double x, double y, double z, EntityLivingBase attacker, int wrathCount) {
		super(world, x, y, z, attacker, wrathCount);
		this.setType(1);
	}
}