package com.cm8check.arrowquest.entity;

import java.util.List;

import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.item.ItemDagger;
import com.cm8check.arrowquest.item.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGenericProjectile extends Entity {
	public static final int TYPE_POLICE_LASER = 0;
	public static final int TYPE_GREEN_PROJECTILE = 1;
	public static final int TYPE_BLUE_PROJECTILE = 2;
	public static final int TYPE_EXPLOSION_BIT = 3;
	public static final int TYPE_PLASMA_BALL = 4;
	public static final int TYPE_LITTLE_PLASMA_BALL = 5;
	public static final int TYPE_SOUL_STEAL = 6;
	
	private EntityLivingBase shootingEntity;
	private int life;
	public float damage = 20.0F;
	private boolean stopped;

	private int type;

	public EntityGenericProjectile(World world) {
		super(world);
		this.setSize(0.4F, 0.4F);
		this.life = 25;

		this.dataWatcher.addObject(5, Byte.valueOf((byte) 0));
	}

	public EntityGenericProjectile(int type, World world, double x, double y, double z, double xDir, double yDir,
			double zDir, double speed, EntityLivingBase attacker) {
		this(world);

		this.type = type;
		this.dataWatcher.updateObject(5, Byte.valueOf((byte) type));

		double d0 = Math.sqrt(xDir * xDir + zDir * zDir);
		this.setLocationAndAngles(x, y, z, (float) Math.toDegrees(Math.atan2(zDir, xDir)) + 90.0F,
				(float) Math.toDegrees(Math.atan2(yDir, d0)));

		double dd = (double) MathHelper.sqrt_double(xDir * xDir + yDir * yDir + zDir * zDir);
		this.motionX = xDir / dd * speed;
		this.motionY = yDir / dd * speed;
		this.motionZ = zDir / dd * speed;

		this.shootingEntity = attacker;
		initProjectile();
	}

	@Override
	public void onUpdate() {
		if (!this.worldObj.isRemote && !this.worldObj.isBlockLoaded(new BlockPos(this))) {
			this.setDead();
			return;
		}
		
		super.onUpdate();
		
		life--;
		
		if (!this.worldObj.isRemote || this.type == TYPE_EXPLOSION_BIT) {
			if (life <= 0) {
				this.setDead();
			}
		}

		Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
		Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
		vec3 = new Vec3(this.posX, this.posY, this.posZ);
		vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (movingobjectposition != null) {
			vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
					movingobjectposition.hitVec.zCoord);
		}

		Entity entity = null;

		if (this.type != TYPE_EXPLOSION_BIT) {
			if (!this.worldObj.isRemote) {
				List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()
						.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
				double d0 = 0.0D;
	
				for (int i = 0; i < list.size(); ++i) {
					Entity entity1 = (Entity) list.get(i);
	
					if (entity1.canBeCollidedWith() && !entity1.isEntityEqual(this.shootingEntity)
							&& entity1.getClass() != EntityPolice.class) {
						float f = 1.0F;
						AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f, (double) f,
								(double) f);
						MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
	
						if (movingobjectposition1 != null) {
							double d1 = vec3.distanceTo(movingobjectposition1.hitVec);
	
							if (d1 < d0 || d0 == 0.0D) {
								entity = entity1;
								d0 = d1;
							}
						}
					}
				}
			}
		}
		else {
			if (this.motionX != 0.0D || this.motionZ != 0.0D) {
				this.motionY -= 0.08D;
			}
		}

		if (entity != null) {
			movingobjectposition = new MovingObjectPosition(entity);
		}

		if (movingobjectposition != null) {
			this.onImpact(movingobjectposition);
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		
		if (this.type == TYPE_PLASMA_BALL && this.life < 30) {
			double d = 1.15D;
			this.motionX *= d;
			this.motionY *= d;
			this.motionZ *= d;
		}
		else if (this.type == TYPE_LITTLE_PLASMA_BALL && this.life < 30) {
			double d = 1.2D;
			this.motionX *= d;
			this.motionY *= d;
			this.motionZ *= d;
		}
		
		this.setPosition(this.posX, this.posY, this.posZ);

		if (worldObj.isRemote) {
			if (this.type == 0) {
				this.type = this.dataWatcher.getWatchableObjectByte(5);
				
				if (this.type > 0) {
					this.initProjectile();
				}
			}
			
			if (this.type == TYPE_EXPLOSION_BIT && (this.motionX != 0.0D || this.motionZ != 0.0D)) {
				AnimationHelper.spawnOneshotAnimation(worldObj, AnimationHelper.fireLaserComponent, posX, posY - 0.5, posZ, 0.75F);
			}
			else if (this.type == TYPE_PLASMA_BALL) {
				AnimationHelper.spawnOneshotAnimation(worldObj, AnimationHelper.policeLaserEffect, posX - 0.5 + rand.nextFloat()*1, posY - 0.5 + rand.nextFloat()*1, posZ - 0.5 + rand.nextFloat()*1, 2.0F);
			}
		}
	}

	protected void onImpact(MovingObjectPosition movingObject) {
		if (this.type == TYPE_EXPLOSION_BIT) {
			this.motionX = this.motionY = this.motionZ = 0.0D;
		}
		
		if (!this.worldObj.isRemote) {
			if (movingObject.entityHit != null && this.type != TYPE_SOUL_STEAL) {
				if (this.type == 0) {
					movingObject.entityHit.attackEntityFrom(
							new DamageSource("mob").setProjectile(), this.damage);
				}
				else {
					movingObject.entityHit.attackEntityFrom(
							new EntityDamageSource("mob", this.shootingEntity).setProjectile(), this.damage);
				}
			}

			this.setDead();
		}
	}
	
	@Override
	public void setDead() {
		super.setDead();
		
		if (this.type == TYPE_PLASMA_BALL) {
			if (worldObj.isRemote) {
				ArrowQuestEntityHelper.spawnBigExplosionEffect(worldObj, posX, posY, posZ);
			}
			else {
				this.worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 4.0F, false);
			}
		}
		
		if (worldObj.isRemote && this.type == TYPE_EXPLOSION_BIT) {
			AnimationHelper.spawnOneshotAnimation(worldObj, AnimationHelper.fireLaserComponent, posX, posY - 0.5, posZ, 0.75F);
		}
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompound) {
		if (tagCompound.hasKey("motion", 9)) {
			NBTTagList nbttaglist = tagCompound.getTagList("motion", 6);
			this.motionX = nbttaglist.getDouble(0);
			this.motionY = nbttaglist.getDouble(1);
			this.motionZ = nbttaglist.getDouble(2);
		} else {
			this.setDead();
		}

		this.type = tagCompound.getByte("pType");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		tagCompound.setTag("motion", this.newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
		tagCompound.setByte("pType", (byte) this.type);
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

	public int getType() {
		return this.type;
	}

	private void initProjectile() {
		switch (this.type) {
		case TYPE_GREEN_PROJECTILE: {
			this.setSize(3.0F, 3.0F);
			this.life = 160;
			break;
		}

		case TYPE_BLUE_PROJECTILE: {
			this.setSize(3.0F, 3.0F);
			this.life = 160;
			break;
		}

		case TYPE_EXPLOSION_BIT: {
			this.life = 120;
			break;
		}
		
		case TYPE_PLASMA_BALL: {
			this.setSize(4.0F, 4.0F);
			this.life = 40;
			this.damage = 40.0F;
			break;
		}
		
		case TYPE_LITTLE_PLASMA_BALL: {
			this.setSize(0.4F, 0.4F);
			this.life = 50;
			this.damage = 40.0F;
			break;
		}
		
		case TYPE_SOUL_STEAL:
			this.damage = 0.0F;
			this.life = 60;
		}
		
		if (worldObj.isRemote) {
			this.setSize(0.4F, 0.4F);
		}
	}
}