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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityDwarfBoss extends EntityAQBoss implements IBossDisplayData{
	private int blockTimer;
	private boolean displayHealth;
	
	public EntityDwarfBoss(World world){
		super(world);
		
		this.rand = ArrowQuest.RAND;
		this.experienceValue = 500;
		this.isBlocking = false;
		this.blockTimer = 50 + rand.nextInt(50);
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (this.worldObj.isRemote){
            if (ClientSoundHelper.bossMusicCheck("arrowquest:musicDwarfBoss", this)){
            	this.displayHealth = true;
            }
            if (this.displayHealth){
            	BossStatus.setBossStatus(this, true);
            }
		}else{
			this.blockTimer--;
			if (this.blockTimer < 1){
				if (this.isBlocking){
					this.setBlocking(false);
					this.blockTimer = 80 + rand.nextInt(70);
				}else{
					this.setBlocking(true);
					this.blockTimer = 120 + rand.nextInt(30);
				}
			}
			
			if (this.isBlocking){
				this.getNavigator().setSpeed(0.7D);
			}
		}
	}
	
	@Override
	protected void onDeathUpdate(){
        this.deathTime++;
        
        if (this.worldObj.isRemote && this.deathTime == 1){
        	ClientSoundHelper.stopMusic();
        }

        if (this.deathTime == 20){
            int i;
            
            if (!this.worldObj.isRemote && this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")){
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
			ArrowQuestEntityHelper.distributeBossXP(this.worldObj, this.posX, this.posY, this.posZ, 4000, this);
		}
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		ItemStack drop = new ItemStack(ModItems.itemLongswordDiamond);
		drop.addEnchantment(Enchantment.knockback, 4);
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
		return "arrowquest:sndDwarfHit";
	}
	
	@Override
	protected String getDeathSound(){
        return "arrowquest:sndBossExplode";
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount){
		if (this.isBlocking){
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "arrowquest:sndBlock", 1.5F, 0.95F+(0.05F*rand.nextFloat()));
			return false;
		}
		
		return super.attackEntityFrom(source, amount);
	}
}