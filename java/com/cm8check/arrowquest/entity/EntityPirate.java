package com.cm8check.arrowquest.entity;

import com.google.common.base.Predicate;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class EntityPirate extends EntityRaceBase{
	public EntityPirate(World world){
		super(world);
		this.experienceValue = 10;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate)null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    }
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){
		
	}
	
	@Override
	protected String getHurtSound(){
		return "arrowquest:sndPirateHurt"+(1+rand.nextInt(2));
	}
	
	@Override
	protected String getDeathSound(){
		return "arrowquest:sndPirateDeath"+(1+rand.nextInt(2));
	}
}