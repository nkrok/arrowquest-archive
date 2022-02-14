package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.network.packet.PacketOneshotAnimation;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;
import com.cm8check.arrowquest.network.packet.PacketSpawnParticles;
import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityWizard extends EntityRaceBase implements IRangedAttackMob{
	private boolean longRange = false;
	private boolean canBlock;
	private int cooldown;
	private int blockCooldown;
	private int megafreezeTimer;
	private int nextAtk;
	
	public int wizardBeamTime;
	public EntityLivingBase wizardBeamTarget;
	public boolean wizardBeamTargetInRange;
	
	public EntityWizard(World world){
		super(world);
		this.cooldown = 20 + this.rand.nextInt(10);
		this.nextAtk = rand.nextInt(6);
		this.canBlock = false;
		this.isImmuneToFire = true;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 100, 100, 20.0F));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(150.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (this.wizardBeamTime > 0 && this.deathTime == 0) {
			this.wizardBeamTime--;
			
			if (this.wizardBeamTarget != null) {
				if (this.getDistanceToEntity(this.wizardBeamTarget) < 16.0F) {
					this.wizardBeamTargetInRange = true;
					
					if (!this.worldObj.isRemote && this.canEntityBeSeen(this.wizardBeamTarget)) {
						//this.wizardBeamTarget.setHealth(this.wizardBeamTarget.getHealth() - 0.25F);
						//this.wizardBeamTarget.hurtTime = 1;
						
						this.wizardBeamTarget.attackEntityFrom(DamageSource.magic, 1.0F);
					}
				}
				else {
					this.wizardBeamTargetInRange = false;
				}
				
				if (this.wizardBeamTarget.deathTime > 0) {
					this.wizardBeamTarget = null;
				}
			}
		}
		
		if (!this.worldObj.isRemote){
			if (!this.longRange && this.recentlyHit > 0){
				this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
				this.longRange = true;
			}
			
			if (this.getAttackTarget() != null){
				this.cooldown--;
				if (this.cooldown < 1){
					int atk = this.nextAtk;
					this.nextAtk = rand.nextInt(6);
					
					//atk = 5;
					
					if (atk < 2){
						AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 10, posY - 1, posZ - 10, posX + 10, posY + 4, posZ + 10);
						List list = worldObj.getEntitiesWithinAABB(EntityLiving.class, aabb);
						
						if (list != null && !list.isEmpty()){
							Iterator iterator = list.iterator();
				            while (iterator.hasNext()){
				            	EntityLiving entity = (EntityLiving) iterator.next();
				            	if (!(entity instanceof EntityWizard)){
				            		entity.heal(30);
				            		ArrowQuest.packetPipeline.sendToAllAround(new PacketOneshotAnimation(AnimationHelper.heal1.id, entity.posX, entity.posY - 0.3, entity.posZ, 2.0F), new TargetPoint(dimension, posX, posY, posZ, 32));
				            	}
				            }
						}
						
						worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndHeal", 1, 1);
						this.cooldown = 20;
					}else if (atk == 2){
						boolean flag = false;
						
						AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 6, posY - 1, posZ - 6, posX + 6, posY + 1, posZ + 6);
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
							worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndQuake", 2.0F, 0.95F+(0.05F*rand.nextFloat()));
							this.cooldown = 10;
							this.nextAtk = 10;
						}else{
							this.cooldown = 3;
						}
					}else if (atk == 3){
						this.setPositionAndUpdate(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ);
						this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.endermen.portal", 2.0F, 0.95F+(0.1F*rand.nextFloat()));
						this.megafreezeTimer = 10 + rand.nextInt(6);
						this.cooldown = 30 + this.rand.nextInt(20);
					}
					else if (atk == 4) {
						this.fireGreenMagic(getAttackTarget());
						
						if (rand.nextInt(2) == 0) {
							this.nextAtk = 4;
							this.cooldown = 5;
						}
						else {
							this.cooldown = 20 + rand.nextInt(20);
						}
					}
					else if (atk == 5) {
						if (this.getDistanceToEntity(getAttackTarget()) < 16.0F) {
							this.wizardBeamTime = 115;
							this.wizardBeamTarget = getAttackTarget();
							
							this.cooldown = this.wizardBeamTime + 20 + rand.nextInt(20);
							ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(9, this.getEntityId(), getAttackTarget().getEntityId()), new TargetPoint(dimension, posX, posY, posZ, 48));
							this.worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndWizardBeam", 3.0F, 0.95F);
						}
						else {
							System.out.println("asdf");
							this.cooldown = 3;
						}
					}
					else if (atk == 10) {
						EntityLivingBase entity = getAttackTarget();
						entity.attackEntityFrom(new EntityDamageSource("mob", this), 50.0F + (20.0F * this.worldObj.getDifficulty().getDifficultyId()));
		            	worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBeam", 4.0F, 0.95F+0.05F*rand.nextFloat());
		                
		                ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(8, this.getEntityId(), entity.getEntityId()), new TargetPoint(dimension, posX, posY, posZ, 48));
		                
		                this.cooldown = 20 + rand.nextInt(20);
					}
				}
				
				this.blockCooldown--;
				if (this.blockCooldown < 1){
					if (this.canBlock){
						this.canBlock = false;
						this.blockCooldown = 80 + rand.nextInt(50);
					}else{
						this.canBlock = true;
						this.blockCooldown = 40 + rand.nextInt(20);
					}
				}
				
				if (this.megafreezeTimer > 0){
					this.megafreezeTimer--;
					if (this.megafreezeTimer < 1){
						AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 5, posY - 1, posZ - 5, posX + 5, posY + 1, posZ + 5);
            			List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
            			if (list != null && !list.isEmpty()){
            				Iterator iterator = list.iterator();
            	            while (iterator.hasNext()){
            	            	EntityPlayer entity = (EntityPlayer) iterator.next();
            	            	if (!entity.isBlocking()){
            	            		entity.attackEntityFrom(new EntityDamageSource("magic", this).setMagicDamage(), 60.0F + (30.0F * this.worldObj.getDifficulty().getDifficultyId()));
            	            	}
            	            }
            			}
            			
            			ArrowQuest.packetPipeline.sendToAllAround(new PacketSpawnParticles(0, Math.round((float) posX), Math.round((float) posY), Math.round((float) posZ)), new TargetPoint(dimension, posX, posY, posZ, 32));
						worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBurst1", 2.0F, 1.0F+(0.2F*rand.nextFloat()));
					}
				}
			}
		}
	}
	
	private void fireGreenMagic(EntityLivingBase target) {
		double d1 = target.posX - posX;
		double d2 = (target.posY+target.getEyeHeight()) - (posY+getEyeHeight());
		double d3 = target.posZ - posZ;
		double d4 = 0.08D;
		
		EntityGenericProjectile entity = new EntityGenericProjectile(EntityGenericProjectile.TYPE_GREEN_PROJECTILE, worldObj, posX, posY + getEyeHeight() - 0.5D, posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4, 0.7D, this);
		entity.damage = 100.0F;
		
		worldObj.spawnEntityInWorld(entity);
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBlueProjectile", 4.0F, 0.93F + rand.nextFloat()*0.1F);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount){
		if (!source.canHarmInCreative() && (this.canBlock || source.isProjectile())){
			this.setBlockingWithCooldown(15);
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.endermen.portal", 4.0F, 0.95F+0.05F*rand.nextFloat());
			return false;
		}
		
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(1);
		
		if (stack != null){
			this.entityDropItem(stack, 0.0F);
		}
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){}
	
	@Override
	protected String getDeathSound(){
        return "mob.wither.idle";
    }
	
	@Override
	protected String getHurtSound(){
        return "mob.wither.hurt";
    }
	
	@Override
	protected boolean canDespawn(){
		return false;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_){}
}