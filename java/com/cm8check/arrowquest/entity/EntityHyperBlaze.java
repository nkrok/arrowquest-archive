package com.cm8check.arrowquest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHyperBlaze extends EntityMob{
	private float heightOffset = 0.5F;
	private int heightOffsetUpdateTime;

	public EntityHyperBlaze(World worldIn){
		super(worldIn);
		this.isImmuneToFire = true;
		this.experienceValue = 75;
		this.tasks.addTask(4, new EntityHyperBlaze.AIFireballAttack());
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(150.0D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(50.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(32.0D);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.blaze.breathe";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.blaze.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.blaze.death";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_)
	{
		return 15728880;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_)
	{
		return 1.0F;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		if (!this.onGround && this.motionY < 0.0D)
		{
			this.motionY *= 0.6D;
		}

		if (this.worldObj.isRemote)
		{
			if (this.rand.nextInt(24) == 0 && !this.isSilent())
			{
				this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
			}

			for (int i = 0; i < 2; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}

		super.onLivingUpdate();
	}

	@Override
	protected void updateAITasks()
	{
		if (this.isWet())
		{
			this.attackEntityFrom(DamageSource.drown, 1.0F);
		}

		--this.heightOffsetUpdateTime;

		if (this.heightOffsetUpdateTime <= 0)
		{
			this.heightOffsetUpdateTime = 100;
			this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
		}

		EntityLivingBase entitylivingbase = this.getAttackTarget();

		if (entitylivingbase != null && entitylivingbase.posY + entitylivingbase.getEyeHeight() > this.posY + this.getEyeHeight() + this.heightOffset)
		{
			this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
			this.isAirBorne = true;
		}

		super.updateAITasks();
	}

	@Override
	public void fall(float distance, float damageMultiplier) {}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
	 */
	@Override
	public boolean isBurning()
	{
		return this.func_70845_n();
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_){
		ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(0);
		if (stack != null){
			this.entityDropItem(stack, 0.0F);
		}
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){
		
	}

	public boolean func_70845_n()
	{
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void func_70844_e(boolean p_70844_1_)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if (p_70844_1_)
		{
			b0 = (byte)(b0 | 1);
		}
		else
		{
			b0 &= -2;
		}

		this.dataWatcher.updateObject(16, Byte.valueOf(b0));
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	@Override
	protected boolean isValidLightLevel()
	{
		return true;
	}

	class AIFireballAttack extends EntityAIBase
	{
		private EntityHyperBlaze field_179469_a = EntityHyperBlaze.this;
		private int field_179467_b;
		private int field_179468_c;

		public AIFireballAttack()
		{
			this.setMutexBits(3);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute()
		{
			EntityLivingBase entitylivingbase = this.field_179469_a.getAttackTarget();
			return entitylivingbase != null && entitylivingbase.isEntityAlive();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void startExecuting()
		{
			this.field_179467_b = 0;
		}

		/**
		 * Resets the task
		 */
		@Override
		public void resetTask()
		{
			this.field_179469_a.func_70844_e(false);
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask()
		{
			--this.field_179468_c;
			EntityLivingBase entitylivingbase = this.field_179469_a.getAttackTarget();
			double d0 = this.field_179469_a.getDistanceSqToEntity(entitylivingbase);

			if (d0 < 4.0D)
			{
				if (this.field_179468_c <= 0)
				{
					this.field_179468_c = 20;
					this.field_179469_a.attackEntityAsMob(entitylivingbase);
				}

				this.field_179469_a.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
			}
			else if (d0 < 256.0D)
			{
				double d1 = entitylivingbase.posX - this.field_179469_a.posX;
				double d2 = (entitylivingbase.posY+0.5D) - (this.field_179469_a.posY + this.field_179469_a.height / 2.0F);
				double d3 = entitylivingbase.posZ - this.field_179469_a.posZ;
				double d4 = 0.08D;
				double dd = (double) MathHelper.sqrt_double(d1 * d1 + d2 * d2 + d3 * d3);
                d1 = d1/dd * 0.5D;
                d2 = d2/dd * 0.5D;
                d3 = d3/dd * 0.5D;

				if (this.field_179468_c <= 0)
				{
					++this.field_179467_b;

					if (this.field_179467_b == 1)
					{
						this.field_179468_c = 40;
						this.field_179469_a.func_70844_e(true);
					}
					else if (this.field_179467_b <= 8)
					{
						this.field_179468_c = 3;
					}
					else
					{
						this.field_179468_c = 40;
						this.field_179467_b = 0;
						this.field_179469_a.func_70844_e(false);
					}

					if (this.field_179467_b > 1)
					{
						float f = MathHelper.sqrt_float(MathHelper.sqrt_double(d0)) * 0.5F;
						this.field_179469_a.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, new BlockPos((int)this.field_179469_a.posX, (int)this.field_179469_a.posY, (int)this.field_179469_a.posZ), 0);
						
						EntitySpellFireball fireball = new EntitySpellFireball(field_179469_a.worldObj, field_179469_a.posX, field_179469_a.posY + (double)(this.field_179469_a.height / 2.0F) + 0.5D, field_179469_a.posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4);
                        fireball.shootingEntity = field_179469_a;
                        fireball.damage = 40.0F + (30.0F * field_179469_a.worldObj.getDifficulty().getDifficultyId());
                        fireball.explodeRand = 3;
                        fireball.useLava = true;
						this.field_179469_a.worldObj.spawnEntityInWorld(fireball);
					}
				}

				this.field_179469_a.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
			}
			else
			{
				this.field_179469_a.getNavigator().clearPathEntity();
				this.field_179469_a.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
			}

			super.updateTask();
		}
	}
}