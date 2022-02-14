package com.cm8check.arrowquest.entity;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.item.ModItems;
import com.google.common.base.Predicate;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityElfBoss extends EntityAQBoss implements IBossDisplayData, IRangedAttackMob{
	private EntityAIArrowAttack rangedAttack = new EntityAIArrowAttack(this, 1.0D, 18, 18, 25.0F);
	private EntityAIAttackOnCollide meleeAttack = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false);
	
	private final ItemStack bowStack = new ItemStack(Items.bow);
	public final ItemStack swordStack = new ItemStack(ModItems.itemSabre);
	
	private boolean isMeleeEngaged = true;
	private int switchModeCooldown;
	
	private boolean displayHealth;
	
	public EntityElfBoss(World world){
		super(world);
		
		this.rand = ArrowQuest.RAND;
		this.experienceValue = 500;
		this.aimTimeRemainBase = 15;
		this.switchModeCooldown = 60;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (this.worldObj.isRemote){
            if (ClientSoundHelper.bossMusicCheck("arrowquest:musicSeize", this)){
            	this.displayHealth = true;
            }
            if (this.displayHealth){
            	BossStatus.setBossStatus(this, true);
            }
		}else{
			this.switchModeCooldown--;
			if (this.switchModeCooldown < 1){
				if (this.isMeleeEngaged){
					this.stopMelee();
					this.switchModeCooldown = 100 + rand.nextInt(50);
				}else{
					this.switchModeCooldown = 60 + rand.nextInt(40);
					this.engageMelee();
				}
			}
		}
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float accuracy){
		EntityArrow entityarrow = new EntityArrow(this.worldObj, this, target, 1.6F, 4);
		entityarrow.setDamage(3.5D + (double)((float) this.worldObj.getDifficulty().getDifficultyId() * 1.0F));

		this.playSound("random.bow", 1.5F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(entityarrow);
		
		this.setAimCooldown(0);
	}
	
	@Override
	protected void onDeathUpdate(){
        this.deathTime++;
        
        if (this.worldObj.isRemote && this.deathTime == 1){
        	ClientSoundHelper.stopMusic();
        }

        if (this.deathTime == 20){
            int i;
            
            if (!this.worldObj.isRemote && (this.recentlyHit > 0) && this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")){
                i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0){
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
                }
            }
            
            this.setDead();
            
            for (i = 0; i < 20; i++){
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
            }
        }
    }
	
	@Override
	public void setDead(){
		super.setDead();
		
		if (!this.worldObj.isRemote){
			ArrowQuestEntityHelper.distributeBossXP(this.worldObj, this.posX, this.posY, this.posZ, 5000, this);
		}
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		ItemStack drop = new ItemStack(Items.bow);
		drop.addEnchantment(Enchantment.power, 3);
		drop.addEnchantment(Enchantment.infinity, 1);
		this.entityDropItem(drop, 0.0F);
		
		ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(2);
		if (stack != null){
			this.entityDropItem(stack, 0.0F);
		}
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){}
	
	@Override
	protected boolean canDespawn(){
		return false;
	}
	
	@Override
	protected String getHurtSound(){
		return "arrowquest:sndElfHurt";
	}
	
	@Override
	protected String getDeathSound(){
        return "arrowquest:sndBossExplode";
    }
	
	private void engageMelee(){
		this.setCurrentItemOrArmor(0, this.swordStack);
		this.tasks.removeTask(meleeAttack);
		this.tasks.removeTask(rangedAttack);
		this.tasks.addTask(2, meleeAttack);
		this.isMeleeEngaged = true;
		this.setAimCooldown(5000);
	}
	
	private void stopMelee(){
		this.setCurrentItemOrArmor(0, this.bowStack);
		this.tasks.removeTask(meleeAttack);
		this.tasks.removeTask(rangedAttack);
		this.tasks.addTask(2, rangedAttack);
		this.isMeleeEngaged = false;
		this.setAimCooldown(0);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund){
		super.readEntityFromNBT(tagCompund);
		this.setAttackTask();
	}
	
	private void setAttackTask(){
		if (!this.worldObj.isRemote){
			this.tasks.removeTask(rangedAttack);
			this.tasks.removeTask(meleeAttack);
			ItemStack itemstack = this.getHeldItem();
			if (itemstack != null && itemstack.getItem() == Items.bow){
				this.tasks.addTask(2, rangedAttack);
				this.isMeleeEngaged = false;
			}else{
				this.tasks.addTask(2, meleeAttack);
				this.isMeleeEngaged = true;
			}
		}
	}
}