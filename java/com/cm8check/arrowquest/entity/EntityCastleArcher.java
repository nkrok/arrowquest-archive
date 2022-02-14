package com.cm8check.arrowquest.entity;

import com.google.common.base.Predicate;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class EntityCastleArcher extends EntityRaceBase implements IRangedAttackMob{
	public EntityAIArrowAttack arrowAttack;
	private boolean longRange = false;
	
	public EntityCastleArcher(World world){
		super(world);
		this.experienceValue = 20;
		
		this.arrowAttack = new EntityAIArrowAttack(this, 1.0D, 40, 50, 25.0F);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, arrowAttack);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(25.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
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
	protected void dropFewItems(boolean p_70628_1_, int extra){
		ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(1);
		
		if (stack != null){
			this.entityDropItem(stack, 0.0F);
		}
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){
		
	}
	
	@Override
	public void fall(float distance, float damageMultiplier){
		if (this.getEquipmentInSlot(1).getItem() != Items.golden_boots){
			super.fall(distance, damageMultiplier);
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_){
		EntityArrow entityarrow = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6F, 2);
		entityarrow.setDamage(7.0D + (double)((float)this.worldObj.getDifficulty().getDifficultyId() * 4.0F));
		
		ItemStack stack = this.getHeldItem();
		int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
        if (k > 0){
            entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 0.5D);
        }
        int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
        if (l > 0){
            entityarrow.setKnockbackStrength(l);
        }

		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(entityarrow);
		
		this.setDefaultAimCooldown();
	}
	
	@Override
	protected String getHurtSound(){
		return "arrowquest:sndHumanHurt1";
	}
	
	@Override
	protected String getDeathSound(){
		return "arrowquest:sndHumanDeath"+(1+rand.nextInt(2));
	}
}