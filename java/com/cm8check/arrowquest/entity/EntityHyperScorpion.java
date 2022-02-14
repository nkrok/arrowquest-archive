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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityHyperScorpion extends EntityMob{
	private boolean longRange = false;
	private int blowUpTimer;
	private boolean blowingUp;
	
	public EntityHyperScorpion(World world){
		super(world);
		this.experienceValue = 100;
		this.isImmuneToFire = true;
		this.setSize(1.0F, 0.25F);
		
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (!longRange && this.recentlyHit > 0){
			this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
			longRange = true;
		}
		
		if (!worldObj.isRemote && !this.isDead && getAttackTarget() != null){
			if (!this.blowingUp && this.getDistanceToEntity(getAttackTarget()) < 2.0F){
				this.blowingUp = true;
				worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndHyperScorpion", 10.0F, 1.0F);
			}
			
			if (this.blowingUp){
				this.blowUpTimer++;
				if (this.blowUpTimer >= 20){
					worldObj.createExplosion(this, posX, posY, posZ, 15.0F, false);
					worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndMegafreeze", 1, 1);
					
					for (int i = 0; i < 6; i++){
						EntityFlyingStinger scorpion = new EntityFlyingStinger(worldObj);
						scorpion.setPosition(posX, posY, posZ);
						worldObj.spawnEntityInWorld(scorpion);
					}
					
					this.setDead();
				}
			}
		}else if (worldObj.isRemote){
			worldObj.spawnParticle(EnumParticleTypes.SPELL_INSTANT, posX, posY, posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}
	
	@Override
	public boolean attackEntityAsMob(Entity par1){
		return false;
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