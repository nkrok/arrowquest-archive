package com.cm8check.arrowquest.entity;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class FireballStormController {
	private double stormX;
	private double stormZ;
	private double explodeY;
	private int fireballsRemaining;
	private int range;
	
	private World world;
	private Random rand = new Random();
	
	private EntityLivingBase shootingEntity;
	
	public FireballStormController(World world, double stormX, double stormY, double stormZ, int fireballCount, EntityLivingBase shootingEntity) {
		this.world = world;
		
		this.stormX = stormX;
		this.explodeY = stormY;
		this.stormZ = stormZ;
		this.fireballsRemaining = fireballCount;
		this.range = 6;
		this.shootingEntity = shootingEntity;
	}
	
	public EntitySpellFireball getNextFireball() {
		this.fireballsRemaining--;
		EntitySpellFireball fireball = new EntitySpellFireball(world, stormX - range + rand.nextInt(range*2), explodeY + 12, stormZ - range + rand.nextInt(range*2), 0.0D, -0.5D, 0.0D);
		fireball.shootingEntity = shootingEntity;
		fireball.setAsStormFireball(this);
		return fireball;
	}
	
	public int getFireballsRemaining() {
		return this.fireballsRemaining;
	}
	
	public double getExplodeY() {
		return this.explodeY;
	}
}