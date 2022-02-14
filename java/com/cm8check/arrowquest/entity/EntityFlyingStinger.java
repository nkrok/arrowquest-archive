package com.cm8check.arrowquest.entity;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityFlyingStinger extends EntityMob{
	private boolean longRange = false;
	
	public EntityFlyingStinger(World world){
		super(world);
		this.experienceValue = 10;
		this.setSize(1.0F, 0.25F);
		
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (!longRange && this.recentlyHit > 0){
			this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
			longRange = true;
		}
	}
	
	@Override
	public boolean attackEntityAsMob(Entity par1){
		if (par1 instanceof EntityLivingBase){
			EntityLivingBase entity = (EntityLivingBase) par1;
			entity.addPotionEffect(new PotionEffect(19, 200, 0, false, true));
			entity.addPotionEffect(new PotionEffect(17, 200, 0, false, true));
		}
		
		return super.attackEntityAsMob(par1);
	}
	
	@Override
	protected String getLivingSound(){
        return "mob.silverfish.say";
    }
	
	@Override
    protected String getHurtSound(){
        return "mob.silverfish.hit";
    }
    
    @Override
    protected String getDeathSound(){
        return "mob.silverfish.kill";
    }
	
	@Override
	public boolean canBePushed(){
		return false;
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){
		
	}
}