package com.cm8check.arrowquest.entity;

import com.google.common.base.Predicate;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityNetherOrc extends EntityRaceBase implements IRangedAttackMob{
	private EntityAIAttackOnCollide meleeAttack = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false);
	private EntityAIArrowAttack arrowAttack = new EntityAIArrowAttack(this, 1.0D, 40, 50, 20.0F);
	
	private boolean longRange = false;
	
	public EntityNetherOrc(World world){
		super(world);
		this.experienceValue = 40;
		this.isImmuneToFire = true;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(18.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(15.0D);
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
	protected void onDeathUpdate(){
        ++this.deathTime;

        if (this.deathTime == 20)
        {
            int i;

            if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
            {
                i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
                }
            }

            this.setDead();

            for (i = 0; i < 20; ++i)
            {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
            }
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
	protected String getHurtSound(){
        return "arrowquest:sndOrcHit";
    }
	
	@Override
	protected String getDeathSound(){
        return "arrowquest:sndOrcDeath";
    }
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float accuracy){
		EntityArrow entityarrow = new EntityArrow(this.worldObj, this, target, 1.6F, 4);
		entityarrow.setDamage(7.0D + ((double) this.worldObj.getDifficulty().getDifficultyId() * 4.0D));
		
		ItemStack stack = this.getHeldItem();
		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0){
            entityarrow.setFire(100);
        }

		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(entityarrow);
		
		this.setDefaultAimCooldown();
	}
	
	@Override
	public void setCurrentItemOrArmor(int slot, ItemStack itemstack){
		super.setCurrentItemOrArmor(slot, itemstack);
		if (slot == 0){
			this.setAttackTask();
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund){
		super.readEntityFromNBT(tagCompund);
		this.setAttackTask();
	}
	
	private void setAttackTask(){
		if (!this.worldObj.isRemote){
			this.tasks.removeTask(arrowAttack);
			this.tasks.removeTask(meleeAttack);
			ItemStack itemstack = this.getHeldItem();
			if (itemstack != null && itemstack.getItem() == Items.bow){
				this.tasks.addTask(2, arrowAttack);
			}else{
				this.tasks.addTask(2, meleeAttack);
			}
		}
	}
}