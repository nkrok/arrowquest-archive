package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;
import com.cm8check.arrowquest.network.packet.PacketSpawnParticles;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDoomGuardian extends EntityMob implements IRangedAttackMob{
	private EntityAIArrowAttack arrowAttack = new EntityAIArrowAttack(this, 1.0D, 40, 60, 16.0F);
	private boolean longRange = false;
	
	private int particleTimer = 0;
	private int laserTimer = -1;
	private int superBulletTimer = -1;
	private int deathAuraUseTimer;
	public int deathAuraTimer = -1;
	public float magicBitLevel;
	public float floatLevel;
	
	public EntityDoomGuardian(World world){
		super(world);
		this.setSize(0.7F, 2.8F);
		this.experienceValue = 90;
		this.isImmuneToFire = true;
		
		this.deathAuraUseTimer = 80 + rand.nextInt(40);
		this.magicBitLevel = 9.0F;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.arrowAttack);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(18.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(30.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		if (!this.onGround && this.motionY < 0.0D){
            this.motionY *= 0.6D;
        }
		
		super.onLivingUpdate();
		
		if (!longRange && this.recentlyHit > 0){
			this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
			longRange = true;
		}
		
		if (!worldObj.isRemote && getAttackTarget() != null){
			if (this.superBulletTimer > 0){
				this.superBulletTimer--;
			}else if (this.superBulletTimer == 0){
				this.superBulletTimer = -1;
				this.fireDoomBullet(getAttackTarget());
			}
			
			if (this.laserTimer > 0){
				this.laserTimer--;
			}else if (this.laserTimer == 0){
				this.laserTimer = -1;
				
				EntityLivingBase entity = getAttackTarget();
				entity.attackEntityFrom(new EntityDamageSource("mob", this), 15.0F + (30.0F * this.worldObj.getDifficulty().getDifficultyId()));
            	worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.wither.shoot", 1, 0.95F+0.05F*rand.nextFloat());
            	worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndFirePunch", 1, 0.95F+0.05F*rand.nextFloat());
            	
            	double d1 = entity.posX - posX;
				double d2 = 0.25D;
				double d3 = entity.posZ - posZ;
				double dd = (double) MathHelper.sqrt_double(d1 * d1 + d2 * d2 + d3 * d3);
                d1 = d1/dd * 0.5D;
                d2 = d2/dd * 0.5D;
                d3 = d3/dd * 0.5D;
                
                entity.addVelocity(d1, d2, d3);
                if (entity instanceof EntityPlayer){
                	((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity));
                }
                
                ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(3, this.getEntityId(), entity.getEntityId()), new TargetPoint(dimension, posX, posY, posZ, 48));
			}
			
			if (this.deathAuraUseTimer > 0){
				this.deathAuraUseTimer--;
			}else if (this.deathAuraUseTimer == 0){
				this.deathAuraUseTimer = -1;
				this.deathAuraTimer = 160;
				ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(2, this.getEntityId(), 1), new TargetPoint(dimension, posX, posY, posZ, 48));
			}
		}
		
		if (this.deathAuraTimer > 0){
			this.deathAuraTimer--;
			
			if (this.magicBitLevel > 0.0F){
				this.magicBitLevel -= 0.3F;
			}
			
			if (!this.worldObj.isRemote){
				AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 3, posY, posZ - 3, posX + 3, posY + 3.5, posZ + 3);
				List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
				if (list != null && !list.isEmpty()){
					Iterator iterator = list.iterator();
		            while (iterator.hasNext()){
		            	EntityPlayer entity = (EntityPlayer) iterator.next();
		            	entity.attackEntityFrom(new EntityDamageSource("mob", this).setMagicDamage(), 25.0F);
		            }
				}
			}
		}else if (this.deathAuraTimer == 0){
			this.deathAuraTimer = -1;
			this.deathAuraUseTimer = 100 + rand.nextInt(60);
		}else if (this.deathAuraTimer == -1){
			if (this.magicBitLevel < 9.0F){
				this.magicBitLevel += 0.3F;
			}
		}
		
		this.floatLevel += 0.0523599F;
		if (this.floatLevel >= 6.28319F){
			this.floatLevel = 0;
		}
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		for (int i = 0; i < 15; i++){
			ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(1);
			if (stack != null){
				this.entityDropItem(stack, 0.0F);
			}
		}
	}
	
	private void fireDoomBullet(EntityLivingBase target){
		double d1 = target.posX - posX;
		double d2 = (target.posY+target.getEyeHeight()) - (posY+getEyeHeight());
		double d3 = target.posZ - posZ;
		double d4 = 3.0D;
		for (int i = 0; i < 5; i++){
			EntityDoomBullet entity = new EntityDoomBullet(worldObj, posX, posY + getEyeHeight(), posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4, this);
			worldObj.spawnEntityInWorld(entity);
		}
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndGuardianShoot", 10.0F, 0.95F+0.05F*rand.nextFloat());
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){}
	
	@Override
	protected String getDeathSound(){
        return "arrowquest:sndGuardianDeath";
    }
	
	@Override
	protected String getHurtSound(){
        return "arrowquest:sndGuardianHurt";
    }
	
	@Override
	public void fall(float distance, float damageMultiplier){}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_){
		this.superBulletTimer = 6;
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndGuardianCharge", 1, 0.95F+0.05F*rand.nextFloat());
		
		if (this.laserTimer == -1){
			this.laserTimer = 30 + rand.nextInt(30);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_){
		return 15728880;
	}
	
	@Override
	public float getBrightness(float p_70013_1_){
		return 15.0F;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount){
		if (this.deathAuraTimer > 0){
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.endermen.portal", 4.0F, 0.95F+0.05F*rand.nextFloat());
			return false;
		}
		
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	protected boolean canDespawn(){
		return false;
	}
}