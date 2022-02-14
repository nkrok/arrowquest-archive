package com.cm8check.arrowquest.entity;

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
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPhantasm extends EntityRaceBase implements IRangedAttackMob{
	private EntityAIAttackOnCollide meleeAttack = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false);
	private EntityAIArrowAttack arrowAttack = new EntityAIArrowAttack(this, 1.0D, 40, 50, 20.0F);
	
	private boolean longRange = false;
	
	public EntityPhantasm(World world){
		super(world);
		this.experienceValue = 75;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(150.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(45.0D);
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
	public boolean attackEntityAsMob(Entity entity){
		if (entity instanceof EntityLivingBase && rand.nextInt(2) == 0) {
			EntityLivingBase target = (EntityLivingBase) entity;
			int effect = rand.nextInt(5);
			
			switch (effect) {
			case 0:
				target.addPotionEffect(new PotionEffect(19, 100, 1, false, true));
				break;
				
			case 1:
				target.addPotionEffect(new PotionEffect(2, 100, 1, false, true));
				break;
				
			case 2:
				target.addPotionEffect(new PotionEffect(4, 60, 0, false, true));
				break;
				
			case 3:
				target.addPotionEffect(new PotionEffect(15, 40, 0, false, true));
				break;
				
			case 4:
				target.addPotionEffect(new PotionEffect(18, 80, 1, false, true));
				break;
			}
		}
		
		return super.attackEntityAsMob(entity);
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
	public void attackEntityWithRangedAttack(EntityLivingBase target, float accuracy){
		EntityArrow entityarrow = new EntityArrow(this.worldObj, this, target, 1.6F, 4);
		entityarrow.setDamage(25.0D + (this.worldObj.getDifficulty().getDifficultyId() * 25.0D));

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
	
	@Override
	protected String getHurtSound(){
		return "arrowquest:sndPhantasmHurt";
	}
	
	@Override
	protected String getDeathSound(){
		return "arrowquest:sndPhantasmDeath"+(1+rand.nextInt(3));
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