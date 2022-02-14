package com.cm8check.arrowquest.entity;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityPolice extends EntityRaceBase implements IRangedAttackMob{
	private EntityAIAttackOnCollide meleeAttack = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false);
	private EntityAIArrowAttack arrowAttack = new EntityAIArrowAttack(this, 1.0D, 40, 70, 20.0F);
	
	private boolean longRange = false;
	private boolean shouldFireMissile = false;
	private int additionalShots;
	private int additionalShotTimer;
	private int additionalShotTimerBase = 4;
	
	private int specialAttackTimer;
	
	private boolean meleeSpecial = false;
	public boolean elite = false;
	private int blockingTimer;
	private int blockingHoldTimer;
	
	private boolean isWizzerd;
	private int wizzerdNextAtk;
	public int wizzerdLiftTime;
	
	private boolean inAirlock;
	
	public EntityPolice(World world){
		super(world);
		this.experienceValue = 60;
		
		this.aimTimeRemainBase = 80;
		this.blockingTimer = 20;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(9.0D);
    }
	
	public void setInAirlock(boolean flag) {
		this.inAirlock = flag;
	}
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (!longRange && this.recentlyHit > 0){
			this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
			longRange = true;
		}
		
		if (!this.worldObj.isRemote){
			if (this.getAttackTarget() != null){
				if (this.isWizzerd) {
					if (this.wizzerdLiftTime > 0 && !this.inAirlock) {
						this.wizzerdLiftTime--;
						if (this.wizzerdLiftTime <= 0) {
							this.bossRaiseArms = false;
							ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(1, this.getEntityId(), 0), new TargetPoint(dimension, posX, posY, posZ, 48));
						}
						
						getAttackTarget().motionX = 0;
						
						if (this.wizzerdLiftTime > 40)
							getAttackTarget().motionY = 0.2D;
						else
							getAttackTarget().motionY = 0;
						
						getAttackTarget().motionZ = 0;
						getAttackTarget().isAirBorne = true;
						
						((EntityPlayerMP) getAttackTarget()).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(getAttackTarget()));
					}
					
					if (this.specialAttackTimer > 0) {
						this.specialAttackTimer--;
					}
					
					if (this.specialAttackTimer <= 0) {
						//this.specialAttackTimer = 80 + rand.nextInt(60);
						this.specialAttackTimer = 40;
						
						int atk = wizzerdNextAtk;
						wizzerdNextAtk = rand.nextInt(4);
						
						if (atk == 0) {
							EntityLivingBase entity = getAttackTarget();
							entity.attackEntityFrom(new EntityDamageSource("mob", this), 15.0F + (13.0F * this.worldObj.getDifficulty().getDifficultyId()));
			            	worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBeam", 4.0F, 0.95F+0.05F*rand.nextFloat());
			                
			                ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(7, this.getEntityId(), entity.getEntityId()), new TargetPoint(dimension, posX, posY, posZ, 48));
						}
						else if (atk == 1) {
							this.fireBlueMagic(getAttackTarget());
							this.specialAttackTimer = 15;
						}
						else if (atk == 2) {
							this.fireLittlePlasma(getAttackTarget());
							wizzerdNextAtk = 9;
							additionalShots = 2;
							specialAttackTimer = 4;
						}
						else if (atk == 3) {
							this.wizzerdLiftTime = 60;
							this.specialAttackTimer = 20;
							this.bossRaiseArms = true;
							ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(1, this.getEntityId(), 1), new TargetPoint(dimension, posX, posY, posZ, 48));
						}
						else if (atk == 9) {
							this.fireLittlePlasma(getAttackTarget());
							
							additionalShots--;
							if (additionalShots > 0) {
								wizzerdNextAtk = 9;
								specialAttackTimer = 4;
							}
						}
					}
				}
				else {
					if (additionalShots > 0){
						if (additionalShotTimer > 0){
							additionalShotTimer--;
						}else{
							additionalShots--;
							additionalShotTimer = additionalShotTimerBase;
							
							this.fireLaser(getAttackTarget());
						}
					}
				}
			}
			
			if (this.meleeSpecial){
				if (this.getAttackTarget() != null) {
					if (this.specialAttackTimer > 0) {
						this.specialAttackTimer--;
					}
					
					if (this.specialAttackTimer <= 0) {
						this.specialAttackTimer = 100 + rand.nextInt(80);
						
						this.firePlasma(getAttackTarget());
						this.swingItem();
						
						this.blockingTimer = 30;
						this.setBlockingWithCooldown(45);
					}
				}
				
				if (this.blockingTimer > 0){
					this.blockingTimer--;
					if (this.blockingTimer < 1){
						this.blockingHoldTimer = 20 + rand.nextInt(20);
					}
				}else if (this.blockingHoldTimer > 0){
					this.blockingHoldTimer--;
					if (this.blockingHoldTimer < 1){
						this.blockingTimer = 30 + rand.nextInt(30);
					}
				}
				
				if (this.isBlocking){
					this.navigator.setSpeed(0.5);
				}else{
					this.navigator.setSpeed(1.0);
				}
			}
		}
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		if (this.dimension != ModLib.dimPoliceBaseID && rand.nextInt(25) == 0){
			this.entityDropItem(new ItemStack(ModItems.itemPoliceTeleporter), 0.0F);
		}
		
		ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(1);
		
		if (stack != null){
			this.entityDropItem(stack, 0.0F);
		}
	}
	
	@Override
	protected String getHurtSound(){
		String str = "arrowquest:sndPoliceHurt"+(1+rand.nextInt(3));
        return str;
    }
	
	@Override
	protected String getDeathSound(){
		String str = "arrowquest:sndPoliceDeath"+(1+rand.nextInt(3));
        return str;
    }
	
	@Override
	public boolean attackEntityAsMob(Entity entity){
		entity.worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndSabreHit", 1.0F, 0.95F+(0.05F*rand.nextFloat()));
		return super.attackEntityAsMob(entity);
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){
		
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float accuracy){
		if (this.isWizzerd) {
			return;
		}
		
		this.additionalShots = 1+rand.nextInt(3);
		if (this.elite) {
			if (this.dimension == ModLib.dimFinalBossID) {
				this.additionalShots += 3;
			}
			
			if (rand.nextInt(2) == 0){
				this.shouldFireMissile = true;
			}
		}
		this.additionalShotTimer = this.additionalShotTimerBase;
		
		this.fireLaser(target);
	}
	
	private void fireLaser(EntityLivingBase target){
		double d1 = target.posX - posX;
		double d2 = (target.posY+target.getEyeHeight()) - (posY+getEyeHeight());
		double d3 = target.posZ - posZ;
		double d4 = 0.08D;
		EntityGenericProjectile entity = new EntityGenericProjectile(EntityGenericProjectile.TYPE_POLICE_LASER, worldObj, posX, posY + getEyeHeight(), posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4, 1.8D, this);
		
		if (this.elite && this.dimension == ModLib.dimFinalBossID){
			entity.damage = 20.0F + (15.0F * this.worldObj.getDifficulty().getDifficultyId());
		}
		
		worldObj.spawnEntityInWorld(entity);
		
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndPoliceLaser", 4.0F, 0.95F+(0.05F*rand.nextFloat()));
		
		if (this.additionalShots == 0){
			if (this.shouldFireMissile){
				this.shouldFireMissile = false;
				this.fireMissile(target);
			}
			
			this.setAimCooldown(25);
		}
	}
	
	private void fireMissile(EntityLivingBase target) {
		double d1 = target.posX - posX;
		double d2 = (target.posY+0.05D) - (posY+getEyeHeight());
		double d3 = target.posZ - posZ;
		
		if (this.dimension == ModLib.dimFinalBossID && rand.nextInt(3) == 0) {
			for (int i = 0; i < 4; i++) {
				double d4 = 4.0D;
				EntityPoliceMissile missile = new EntityPoliceMissile(worldObj, posX, posY + getEyeHeight(), posZ, (d1-d4/2)+rand.nextFloat()*d4, d2, (d3-d4/2)+rand.nextFloat()*d4, this);
				worldObj.spawnEntityInWorld(missile);
			}
			
			worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndRocket", 4.0F, 0.95F+(0.05F*rand.nextFloat()));
		}
		else {
			double d4 = 0.08D;
			EntityPoliceMissile missile = new EntityPoliceMissile(worldObj, posX, posY + getEyeHeight(), posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4, this);
			worldObj.spawnEntityInWorld(missile);
			worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndRocket", 4.0F, 0.95F+(0.05F*rand.nextFloat()));
		}
	}
	
	private void firePlasma(EntityLivingBase target) {
		double d1 = target.posX - posX;
		
		double d2 = (target.posY+target.getEyeHeight()) - (posY+getEyeHeight());
		if (d2 > 0) {
			d2 = 0;
		}
		
		double d3 = target.posZ - posZ;
		double d4 = 0.08D;
		
		EntityGenericProjectile entity = new EntityGenericProjectile(EntityGenericProjectile.TYPE_PLASMA_BALL, worldObj, posX, posY + getEyeHeight(), posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4, 0.1D, this);
		entity.damage = 40.0F;
		
		worldObj.spawnEntityInWorld(entity);
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndPlasma", 4.0F, 0.93F + rand.nextFloat()*0.1F);
	}
	
	private void fireBlueMagic(EntityLivingBase target) {
		double d1 = target.posX - posX;
		double d2 = (target.posY+target.getEyeHeight()) - (posY+getEyeHeight());
		double d3 = target.posZ - posZ;
		double d4 = 0.08D;
		
		EntityGenericProjectile entity = new EntityGenericProjectile(EntityGenericProjectile.TYPE_BLUE_PROJECTILE, worldObj, posX, posY + getEyeHeight() - 0.5D, posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4, 0.7D, this);
		entity.damage = 40.0F;
		
		worldObj.spawnEntityInWorld(entity);
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBlueProjectile", 4.0F, 0.93F + rand.nextFloat()*0.1F);
	}
	
	private void fireLittlePlasma(EntityLivingBase target) {
		for (int i = 0; i < 3; i++) {
			double d1 = target.posX - posX;
			double d2 = (target.posY+target.getEyeHeight()) - (posY+getEyeHeight());
			double d3 = target.posZ - posZ;
			double d4 = 4.0D;
			
			EntityGenericProjectile entity = new EntityGenericProjectile(EntityGenericProjectile.TYPE_LITTLE_PLASMA_BALL, worldObj, posX, posY + getEyeHeight(), posZ, (d1-d4/2)+rand.nextFloat()*d4, d2, (d3-d4/2)+rand.nextFloat()*d4, 0.02D, this);
			entity.damage = 40.0F;
			
			worldObj.spawnEntityInWorld(entity);
		}
		
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndLittlePlasma", 4.0F, 0.93F + rand.nextFloat()*0.1F);
	}
	
	@Override
	public void setCurrentItemOrArmor(int slot, ItemStack itemstack){
		super.setCurrentItemOrArmor(slot, itemstack);
		if (slot == 0){
			this.setAttackTask();
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound){
		super.writeEntityToNBT(tagCompound);
		tagCompound.setBoolean("IsElite", elite);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound){
		super.readEntityFromNBT(tagCompound);
		this.elite = tagCompound.getBoolean("IsElite");
		this.setAttackTask();
	}
	
	private void setAttackTask(){
		if (!this.worldObj.isRemote){
			if (this.elite){
				if (this.dimension == ModLib.dimFinalBossID) {
					this.additionalShotTimerBase = 2;
					this.isImmuneToFire = true;
					this.experienceValue = 120;
					this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(75.0D);
				}
				
				this.specialAttackTimer = 80 + rand.nextInt(40);
				this.blockingTimer = 20 + rand.nextInt(20);
			}
			
			this.tasks.removeTask(arrowAttack);
			this.tasks.removeTask(meleeAttack);
			ItemStack itemstack = this.getHeldItem();
			
			if (itemstack == null) {
				this.isWizzerd = true;
				this.tasks.addTask(2, arrowAttack);
			}
			else if (itemstack.getItem() == ModItems.itemPoliceGun){
				this.tasks.addTask(2, arrowAttack);
			}
			else {
				this.tasks.addTask(2, meleeAttack);
				if (this.elite){
					this.meleeSpecial = true;
				}
			}
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount){
		if (!source.canHarmInCreative() && !source.isFireDamage() && this.meleeSpecial && this.blockingTimer < 1){
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "arrowquest:sndSabreHit", 1.5F, 0.95F+(0.05F*rand.nextFloat()));
			this.blockingHoldTimer = 18;
			this.setBlockingWithCooldown(18);
			
			return false;
		}
		
		return super.attackEntityFrom(source, amount);
	}
}