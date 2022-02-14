package com.cm8check.arrowquest.entity;

import java.util.List;

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
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBanishedSoul extends EntityRaceBase {
	private int succSoundCooldown;
	
	public EntityBanishedSoul(World world){
		super(world);
		this.experienceValue = 75;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(150.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(0.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (!this.worldObj.isRemote && this.ticksExisted > 10) {
			if (this.succSoundCooldown > 0) {
				this.succSoundCooldown--;
			}
			
			double range = 4.0D;
			AxisAlignedBB aabb = new AxisAlignedBB(posX - range, posY - 1, posZ - range, posX + range, posY + 1, posZ + range);
			List<EntityPlayer> list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			
			for (EntityPlayer player : list) {
				if (player.capabilities.isCreativeMode || player.deathTime > 0 || player.isDead)
					continue;
				
				double d1 = player.posX - posX;
	            double d2 = player.posY - posY + 0.25D;
	            double d3 = player.posZ - posZ;
	            double dd = (double) MathHelper.sqrt_double(d1 * d1 + d2 * d2 + d3 * d3);
	            dd *= dd * dd;
	            d1 = d1/dd * 0.05D;
	            d2 = d2/dd * 0.05D;
	            d3 = d3/dd * 0.05D;
	             
	            player.addVelocity(-d1, -d2, -d3);
	            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(player));
	            
	            if (this.getDistanceToEntity(player) < 1.0F && player.hurtTime <= 0) {
	            	player.addPotionEffect(new PotionEffect(15, 25, 1, true, false));
	            	
	            	float newHP = player.getHealth() - 0.5F;
	            	player.setHealth(newHP);
	            	player.hurtTime = 1;
	            	
	            	if (newHP <= 0) {
	            		this.worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 10000.0F, 0.9F);
	            		
	            		/*
	            		EntityBanishedSoul soul = new EntityBanishedSoul(worldObj);
	            		soul.setPositionAndUpdate(player.posX, player.posY, player.posZ);
	            		this.worldObj.spawnEntityInWorld(soul);
	            		*/
	            	}
	            	
	            	if (this.succSoundCooldown <= 0) {
	            		this.succSoundCooldown = 60;
	            		this.worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndSoulSucc", 2.0F, 0.93F + rand.nextFloat()*0.1F);
	            	}
	            }
			}
		}
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entity) {
		return false;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.canHarmInCreative())
			return super.attackEntityFrom(source, amount);
		
		return false;
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){
		
	}
	
	@Override
	protected String getLivingSound() {
		return "arrowquest:sndBanishedSoul"+(1+rand.nextInt(4));
	}
	
	@Override
	protected String getHurtSound(){
		return null;
	}
	
	@Override
	protected String getDeathSound(){
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return 15.0F;
	}
}