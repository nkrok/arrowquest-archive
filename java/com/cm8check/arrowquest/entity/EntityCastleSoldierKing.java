package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.network.packet.PacketSpawnParticles;
import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityCastleSoldierKing extends EntityAQBoss implements IBossDisplayData, IRangedAttackMob{
	private EntityAIArrowAttack rangedAttack = new EntityAIArrowAttack(this, 1.0D, 30, 40, 25.0F);
	private EntityAIAttackOnCollide meleeAttack = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false);
	
	private int atk;
	private int spellCooldown;
	private int quakeCooldown;
	private int teleportCooldown;
	private int additionalFireballs;
	private int additionalFireballTimer;
	private Random rand;
	private boolean isMeleeEngaged;
	private BlockPos teleportPos;
	private boolean readyToTeleport;
	
	private boolean displayHealth;
	
	public EntityCastleSoldierKing(World world){
		super(world);
		this.experienceValue = 500;
		
		this.rand = ArrowQuest.RAND;
		
		this.atk = rand.nextInt(3);
		this.spellCooldown = 40 + rand.nextInt(30);
		this.quakeCooldown = 40 + rand.nextInt(40);
		this.teleportCooldown = 60 + rand.nextInt(40);
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.rangedAttack);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(90.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(25.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		if (worldObj.isRemote){
            if (ClientSoundHelper.bossMusicCheck("arrowquest:musicPromisedPain", this)){
            	this.displayHealth = true;
            }
            if (this.displayHealth){
            	BossStatus.setBossStatus(this, true);
            }
		}
		
		super.onLivingUpdate();
		
		if (!worldObj.isRemote && this.getAttackTarget() != null){
			if (spellCooldown > 0){
				spellCooldown -= 1;
			}else{
				spellCooldown = 25 + rand.nextInt(40);
				
				if (this.atk == 0){
					EntitySpellLightning entity = new EntitySpellLightning(worldObj, (getAttackTarget().posX-1.0D)+(2.0F*this.rand.nextFloat()), getAttackTarget().posY, (getAttackTarget().posZ-1.0D)+(2.0F*this.rand.nextFloat()), this);
					worldObj.addWeatherEffect(entity);
					this.atk = rand.nextInt(3)+1;
				}else if (this.atk == 1){
					boolean flag = false;
					
					AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 5, posY - 1, posZ - 5, posX + 5, posY + 2, posZ + 5);
					List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
					if (list != null && !list.isEmpty()){
						Iterator iterator = list.iterator();
			            while (iterator.hasNext()){
			            	EntityPlayer entity = (EntityPlayer) iterator.next();
			            	entity.attackEntityFrom(new EntityDamageSource("magic", this).setMagicDamage(), 25.0F);
			            	flag = true;
			            }
					}
					
					if (flag){
						ArrowQuest.packetPipeline.sendToAllAround(new PacketSpawnParticles(0, Math.round((float) posX), Math.round((float) posY), Math.round((float) posZ)), new TargetPoint(dimension, posX, posY, posZ, 64));
						worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBurst2", 2.0F, 0.95F+(0.05F*rand.nextFloat()));
					}
					
					this.atk = rand.nextInt(4);
				}else if (this.atk == 2){
					additionalFireballs = 4+rand.nextInt(4);
					additionalFireballTimer = 0;
					
					this.atk = rand.nextInt(4);
				}else if (this.atk == 3){
					if (!this.isMeleeEngaged){
						this.teleportPos = this.getPosition();
						this.engageMelee();
					}
					this.atk = 4;
					this.spellCooldown = 80 + rand.nextInt(40);
				}else if (this.atk == 4){
					if (this.isMeleeEngaged){
						this.stopMelee();
					}
					this.atk = rand.nextInt(3);
					this.spellCooldown = 30+rand.nextInt(15);
				}
			}
			
			if (additionalFireballs > 0){
				if (additionalFireballTimer > 0){
					additionalFireballTimer--;
				}else{
					additionalFireballs--;
					additionalFireballTimer = 2;
					
					double d1 = getAttackTarget().posX - this.posX;
	                double d2 = getAttackTarget().getEntityBoundingBox().minY + (double) (getAttackTarget().height / 2.0F) - (this.posY + (double) (this.height / 2.0F));
	                double d3 = getAttackTarget().posZ - this.posZ;
	                double dd = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
	                d1 = d1/dd * 0.6D;
	                d2 = d2/dd * 0.6D;
	                d3 = d3/dd * 0.6D;
	                double d4 = 0.08D;
                    
                    EntitySpellFireball fireball = new EntitySpellFireball(worldObj, posX, posY + (double) (this.height / 2.0F) + 0.5D, posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4);
                    fireball.shootingEntity = this;
                    fireball.damage = 20.0F;
                    this.worldObj.spawnEntityInWorld(fireball);
                    
                    this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.ghast.fireball", 2.5F, 0.95F+(0.05F*rand.nextFloat()));
				}
			}
			
			if (quakeCooldown > 0){
				quakeCooldown -= 1;
			}else{
				quakeCooldown = 60 + rand.nextInt(40);
				
				boolean flag = false;
				
				AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 3, posY - 1, posZ - 3, posX + 3, posY + 1, posZ + 3);
				List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
				if (list != null && !list.isEmpty()){
					Iterator iterator = list.iterator();
		            while (iterator.hasNext()){
		            	EntityPlayer entity = (EntityPlayer) iterator.next();
		            	double dx = entity.posX - posX;
		            	double dz = entity.posZ - posZ;
		            	
		            	double pushX = dx;
		            	double pushY = 0.5D;
		            	double pushZ = dz;
		            	double d3 = (double) MathHelper.sqrt_double(pushX * pushX + pushY * pushY + pushZ * pushZ);
		            	pushX = pushX / d3 * 2.5D;
		            	pushZ = pushZ / d3 * 2.5D;
		            	entity.addVelocity(pushX, 0.5D, pushZ);
		            	
		            	((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity));
		            	
		            	flag = true;
		            }
				}
				
				if (flag){
					worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndQuake", 3.0F, 0.95F+(0.05F*rand.nextFloat()));
				}
			}
			
			if (this.teleportCooldown > 0){
				this.teleportCooldown--;
				if (this.teleportCooldown < 1){
					this.readyToTeleport = true;
				}
			}
		}
	}
	
	@Override
	protected void onDeathUpdate(){
        this.deathTime++;
        
        if (worldObj.isRemote && this.deathTime == 1){
        	ClientSoundHelper.stopMusic();
        }

        if (this.deathTime == 20){
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

            for (i = 0; i < 20; ++i){
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
            }
        }
    }
	
	@Override
	public void setFire(int seconds){}
	
	@Override
	public void setDead(){
		super.setDead();
		
		if (!worldObj.isRemote){
			ArrowQuestEntityHelper.distributeBossXP(worldObj, posX, posY, posZ, 10000, this);
		}
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		this.entityDropItem(new ItemStack(ModItems.itemKingSword), 0.0F);
		
		ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(2);
		if (stack != null){
			this.entityDropItem(stack, 0.0F);
		}
	}
	
	public void fall(float distance, float damageMultiplier){}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){}
	
	@Override
	protected boolean canDespawn(){
		return false;
	}
	
	@Override
	protected String getDeathSound(){
        return "arrowquest:sndBossExplode";
    }
	
	private void engageMelee(){
		this.tasks.removeTask(meleeAttack);
		this.tasks.removeTask(rangedAttack);
		this.tasks.addTask(2, meleeAttack);
		this.isMeleeEngaged = true;
	}
	
	private void stopMelee(){
		this.tasks.removeTask(meleeAttack);
		this.tasks.removeTask(rangedAttack);
		this.tasks.addTask(2, rangedAttack);
		this.isMeleeEngaged = false;
	}
	
	private void teleport(){
		if (!this.worldObj.isRemote){
			this.readyToTeleport = false;
			this.teleportCooldown = 120 + rand.nextInt(40);
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.endermen.portal", 2.0F, 0.95F+(0.05F*rand.nextFloat()));
			this.setPositionAndUpdate(this.teleportPos.getX(), this.teleportPos.getY(), this.teleportPos.getZ());
		}
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_){
		
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount){
		if (this.readyToTeleport && this.teleportPos != null){
			this.teleport();
			return false;
		}
		
		return super.attackEntityFrom(source, amount);
	}
}