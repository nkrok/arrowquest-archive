package com.cm8check.arrowquest.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDoomBullet extends Entity{
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;
	private EntityLivingBase shootingEntity;
	private int life;
	public float damage;
	
	public float spreadFloat;
	public float maxSpreadFloat;
	
	public EntityDoomBullet(World world){
		super(world);
		this.setSize(1.75F, 1.75F);
		this.life = 80;
		this.spreadFloat = 0.0F;
		this.maxSpreadFloat = 3.0F;
		this.damage = 30.0F;
	}
	
	public EntityDoomBullet(World world, double x, double y, double z, double xDir, double yDir, double zDir, EntityLivingBase attacker){
		this(world);
		
		double d0 = Math.sqrt(xDir*xDir + zDir*zDir);
		this.setLocationAndAngles(x, y, z, (float) Math.toDegrees(Math.atan2(zDir, xDir)) + 90.0F, (float) Math.toDegrees(Math.atan2(yDir, d0)));
		
		double dd = (double) MathHelper.sqrt_double(xDir * xDir + yDir * yDir + zDir * zDir);
		this.accelerationX = xDir/dd * 0.08D;
        this.accelerationY = yDir/dd * 0.08D;
        this.accelerationZ = zDir/dd * 0.08D;
        
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
            List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (int i = 0; i < list.size(); ++i){
                Entity entity1 = (Entity) list.get(i);

                if (entity1.canBeCollidedWith()){
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
        float f2 = 0.95F;

        this.motionX += this.accelerationX;
        this.motionY += this.accelerationY;
        this.motionZ += this.accelerationZ;
        this.motionX *= (double) f2;
        this.motionY *= (double) f2;
        this.motionZ *= (double) f2;
        this.worldObj.spawnParticle(EnumParticleTypes.REDSTONE, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        this.setPosition(this.posX, this.posY, this.posZ);
        
        if (this.spreadFloat < this.maxSpreadFloat){
        	this.spreadFloat += 0.2F;
        }
	}
	
	protected void onImpact(MovingObjectPosition movingObject){
        if (!this.worldObj.isRemote){
            boolean flag;

            if (movingObject.entityHit != null){
                flag = movingObject.entityHit.attackEntityFrom(new EntityDamageSource("mob", this.shootingEntity).setProjectile(), this.damage + (20.0F * this.worldObj.getDifficulty().getDifficultyId()));
            }
            
            this.setDead();
        }
    }
	
	@Override
	public void setDead(){
		if (!worldObj.isRemote){
			worldObj.createExplosion(this.shootingEntity, posX, posY, posZ, 2.0F, false);
			if (this.damage > 30.0F){
				for (int i = 0; i < 6; i++){
					EntityDoomBullet entity = new EntityDoomBullet(worldObj, posX, posY, posZ, -1D+rand.nextFloat()*2D, -1D+rand.nextFloat()*2D, -1D+rand.nextFloat()*2D, this.shootingEntity);
					worldObj.spawnEntityInWorld(entity);
				}
			}
		}
		super.setDead();
	}
	
	@Override
	protected void entityInit(){}

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
	
	@SideOnly(Side.CLIENT)
	@Override
    public int getBrightnessForRender(float p_70070_1_){
        return 15728880;
    }
    
	@Override
    public float getBrightness(float p_70013_1_){
        return 15.0F;
    }
}