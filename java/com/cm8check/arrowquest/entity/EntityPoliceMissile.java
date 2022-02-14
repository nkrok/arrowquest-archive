package com.cm8check.arrowquest.entity;

import java.util.List;

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
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPoliceMissile extends Entity{
	private double accelerationX;
	private double accelerationY;
	private double accelerationZ;
	private EntityLivingBase shootingEntity;
	private int life;
	public float damage = 60.0F;
	
	public EntityPoliceMissile(World world){
		super(world);
		this.setSize(3.0F, 3.0F);
		this.life = 40;
	}
	
	public EntityPoliceMissile(World world, double x, double y, double z, double xDir, double yDir, double zDir, EntityLivingBase attacker){
		this(world);
		
		double d0 = Math.sqrt(xDir*xDir + zDir*zDir);
		this.setLocationAndAngles(x, y, z, (float) Math.toDegrees(Math.atan2(zDir, xDir)) + 90.0F, (float) Math.toDegrees(Math.atan2(yDir, d0)));
		
		double dd = (double) MathHelper.sqrt_double(xDir * xDir + yDir * yDir + zDir * zDir);
		this.accelerationX = xDir/dd * 0.25D;
        this.accelerationY = yDir/dd * 0.25D;
        this.accelerationZ = zDir/dd * 0.25D;
        
        this.shootingEntity = attacker;
	}
	
	@Override
	public void onUpdate(){
		if (!this.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.isBlockLoaded(new BlockPos(this)))){
            this.setDead();
        }else{
            super.onUpdate();
            
            if (!this.worldObj.isRemote){
    			life--;
    			if (life <= 0){
    				this.setDead();
    			}
    		}
            
            Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
            vec3 = new Vec3(this.posX, this.posY, this.posZ);
            vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null){
                vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (int i = 0; i < list.size(); ++i){
                Entity entity1 = (Entity)list.get(i);

                if (entity1.canBeCollidedWith() && !entity1.isEntityEqual(this.shootingEntity) && entity1.getClass() != EntityPolice.class){
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null){
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D){
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null){
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null){
                this.onImpact(movingobjectposition);
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f2 = 0.95F;

        this.motionX += this.accelerationX;
        this.motionY += this.accelerationY;
        this.motionZ += this.accelerationZ;
        this.motionX *= (double) f2;
        this.motionY *= (double) f2;
        this.motionZ *= (double) f2;
        this.setPosition(this.posX, this.posY, this.posZ);
	}
	
	protected void onImpact(MovingObjectPosition movingObject){
        if (!this.worldObj.isRemote){
            boolean flag;

            if (movingObject.entityHit != null){
                flag = movingObject.entityHit.attackEntityFrom(new EntityDamageSource("mob", this.shootingEntity).setProjectile(), this.damage);
                movingObject.entityHit.addVelocity(this.motionX, 0.5D, this.motionZ);
            }
            
            this.setDead();
        }
    }
	
	@Override
	public void setDead(){
		this.worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 3.0F, false);
		
		if (this.worldObj.isRemote) {
			ArrowQuestEntityHelper.spawnBigExplosionEffect(this.worldObj, this.posX, this.posY + 1.0D, this.posZ);
		}
		
		super.setDead();
	}
	
	@Override
	protected void entityInit(){
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompound){
		if (tagCompound.hasKey("motion", 9)){
            NBTTagList nbttaglist = tagCompound.getTagList("motion", 6);
            this.motionX = nbttaglist.getDouble(0);
            this.motionY = nbttaglist.getDouble(1);
            this.motionZ = nbttaglist.getDouble(2);
        }else{
            this.setDead();
        }
        
        if (tagCompound.hasKey("direction", 9)){
            NBTTagList nbttaglist = tagCompound.getTagList("direction", 6);
            this.accelerationX = nbttaglist.getDouble(0);
            this.accelerationY = nbttaglist.getDouble(1);
            this.accelerationZ = nbttaglist.getDouble(2);
        }else{
            this.setDead();
        }
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound){
        tagCompound.setTag("motion", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
        tagCompound.setTag("direction", this.newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
	}
}