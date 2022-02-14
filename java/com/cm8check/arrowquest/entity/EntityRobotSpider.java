package com.cm8check.arrowquest.entity;

import java.util.Random;

import com.cm8check.arrowquest.lib.ModLib;
import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityRobotSpider extends EntitySpider{
	protected int arrowTimer = -1;
	private int arrowCooldown;
	private boolean longRange = false;
	
	public EntityRobotSpider(World world){
		super(world);
		this.experienceValue = 10;
		this.isImmuneToFire = true;
		this.setSize(1.4F, 0.9F);
		
		this.tasks.taskEntries.clear();
		this.targetTasks.taskEntries.clear();
		
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(3, new EntityRobotSpider.AISpiderAttack(EntityPlayer.class));
        this.tasks.addTask(4, new EntityAIWander(this, 0.5D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
        
        this.arrowCooldown = 50 + rand.nextInt(30);
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(14.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
    }

	protected void entityInit() {
		super.entityInit();
	}

	public void onUpdate(){
		super.onUpdate();
		
		if (!longRange && this.recentlyHit > 0){
			this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
			longRange = true;
		}

		if (!this.worldObj.isRemote){
			if (getAttackTarget() != null){
				if (this.arrowTimer > 0){
					this.arrowTimer--;
				}else if (this.arrowTimer == 0){
					this.arrowTimer = -1;
					this.arrowCooldown = 20 + rand.nextInt(20);
					this.fireArrow(getAttackTarget());
				}
				
				if (this.arrowCooldown > 0){
					this.arrowCooldown--;
				}else if (this.arrowCooldown == 0){
					if (this.getDistanceToEntity(getAttackTarget()) <= 16){
						this.arrowTimer = 30;
						this.arrowCooldown = -1;
					}else{
						this.arrowCooldown = 20 + rand.nextInt(30);
					}
				}
			}
		}
	}
	
	private EntityArrow fireArrow(EntityLivingBase target){
		EntityArrow entity;
		int j = 2;
		if (rand.nextInt(j) == 0){
			entity = new EntityExplosiveArrow(worldObj, this, target, 1.5F, 4.0F);
		}else{
			entity = new EntityArrow(worldObj, this, target, 1.5F, 4.0F);
		}
		
		if (this.dimension == 0) {
			entity.setDamage(4.0D + (this.worldObj.getDifficulty().getDifficultyId() * 1.0D));
		}
		else {
			entity.setDamage(30.0D + (this.worldObj.getDifficulty().getDifficultyId() * 30.0D));
		}
		
		worldObj.spawnEntityInWorld(entity);
		worldObj.playSoundAtEntity(this, "arrowquest:sndCrossbow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 1.0F);
		
		return entity;
	}
	
	@Override
	public void setDead(){
		super.setDead();
		if (!this.worldObj.isRemote){
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 1.25F, true);
			this.worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndMegafreeze", 1, 1);
		}
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){
		
	}
	
	@Override
	protected Item getDropItem(){
		return null;
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_){
		
	}
	
	@Override
	protected String getLivingSound(){
		return null;
    }
	
	@Override
    protected String getHurtSound(){
		String str = "arrowquest:sndRobotSpiderSay"+(1+rand.nextInt(4));
        return str;
    }
    
	@Override
    protected String getDeathSound(){
        return "arrowquest:sndRobotSpiderDeath";
    }
	
	public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_){
		this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        return p_180482_2_;
	}

	class AISpiderAttack extends EntityAIAttackOnCollide{
		private EntityRobotSpider robotSpider;
		
		public AISpiderAttack(Class p_i45819_2_) {
			super(EntityRobotSpider.this, p_i45819_2_, 1.0D, true);
			this.robotSpider = EntityRobotSpider.this;
		}
		
		@Override
		protected double func_179512_a(EntityLivingBase p_179512_1_) {
			return (double) (4.0F + p_179512_1_.width);
		}
	}
}