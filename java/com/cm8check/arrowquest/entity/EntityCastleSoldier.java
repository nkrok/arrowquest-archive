package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.item.ItemWand;
import com.cm8check.arrowquest.network.packet.PacketOneshotAnimation;
import com.cm8check.arrowquest.network.packet.PacketSpawnParticles;
import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityCastleSoldier extends EntityRaceBase implements IRangedAttackMob{
	private int spellCooldown;
	private boolean hasWand = false;
	private Random rand;
	private int atk = 0;
	private boolean longRange = false;
	
	private EntityAIAttackOnCollide meleeAttack = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false);
	private EntityAIArrowAttack arrowAttack = new EntityAIArrowAttack(this, 1.0D, 30, 40, 20.0F);
	
	public EntityCastleSoldier(World world){
		super(world);
		this.experienceValue = 20;
		this.rand = ArrowQuest.RAND;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, meleeAttack);
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(13.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (!longRange && this.recentlyHit > 0){
			this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
			longRange = true;
		}
		
		if (!worldObj.isRemote && hasWand && this.getAttackTarget() != null){
			if (spellCooldown > 0){
				spellCooldown -= 1;
			}else{
				if (atk > 0){
					AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 7, posY - 1, posZ - 7, posX + 7, posY + 4, posZ + 7);
					List list = worldObj.getEntitiesWithinAABB(EntityLiving.class, aabb);
					
					if (list != null && !list.isEmpty()){
						Iterator iterator = list.iterator();
			            while (iterator.hasNext()) {
			            	EntityLiving entity = (EntityLiving) iterator.next();
			            	entity.heal(5);
			            	
			            	ArrowQuest.packetPipeline.sendToAllAround(new PacketOneshotAnimation(AnimationHelper.heal1.id, entity.posX, entity.posY - 0.3, entity.posZ, 2.0F), new TargetPoint(dimension, posX, posY, posZ, 16));
			            }
					}
					
					worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndHeal", 1, 1);
					
					spellCooldown = 20 + rand.nextInt(20);
					atk = rand.nextInt(2);
				}else{
					boolean flag = false;
					
					AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 5, posY - 1, posZ - 5, posX + 5, posY + 2, posZ + 5);
					List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
					if (list != null && !list.isEmpty()){
						Iterator iterator = list.iterator();
			            while (iterator.hasNext()){
			            	EntityPlayer entity = (EntityPlayer) iterator.next();
			            	entity.attackEntityFrom(new EntityDamageSource("magic", this).setMagicDamage(), 20.0F);
			            	flag = true;
			            }
					}
					
					if (flag){
						ArrowQuest.packetPipeline.sendToAllAround(new PacketSpawnParticles(0, Math.round((float) posX), Math.round((float) posY), Math.round((float) posZ)), new TargetPoint(dimension, posX, posY, posZ, 16));
						worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBurst2", 1, 1);
						spellCooldown = 20 + rand.nextInt(30);
						atk = rand.nextInt(2);
					}else{
						spellCooldown = 5;
						atk = 0;
					}
				}
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
	public void fall(float distance, float damageMultiplier){
		if (this.getEquipmentInSlot(1) == null || this.getEquipmentInSlot(1).getItem() != Items.golden_boots){
			super.fall(distance, damageMultiplier);
		}
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
			this.tasks.removeTask(meleeAttack);
			this.tasks.removeTask(arrowAttack);
			ItemStack itemstack = this.getHeldItem();
			if (itemstack != null && itemstack.getItem() instanceof ItemWand){
				this.tasks.addTask(2, arrowAttack);
				this.hasWand = true;
				this.spellCooldown = 20 + rand.nextInt(20);
			}else{
				this.hasWand = false;
				this.tasks.addTask(2, meleeAttack);
			}
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_){
		
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