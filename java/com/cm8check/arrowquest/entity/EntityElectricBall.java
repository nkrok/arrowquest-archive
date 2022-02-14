package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
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
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityElectricBall extends Entity{
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;
	private boolean stopped;
	private EntityLivingBase shootingEntity;
	private int life;
	private int shockCooldown;
	private int zapEffectTimer = 0;
	
	public EntityElectricBall(World world){
		super(world);
		this.setSize(1.75F, 1.75F);
		this.life = 200;
		this.zapEffectTimer = rand.nextInt(6);
	}
	
	public EntityElectricBall(World world, double x, double y, double z, double xDir, double yDir, double zDir, EntityLivingBase attacker){
		this(world);
		
		double d0 = Math.sqrt(xDir*xDir + zDir*zDir);
		this.setLocationAndAngles(x, y, z, (float) Math.toDegrees(Math.atan2(zDir, xDir)) + 90.0F, (float) Math.toDegrees(Math.atan2(yDir, d0)));
		
		double dd = (double) MathHelper.sqrt_double(xDir * xDir + yDir * yDir + zDir * zDir);
		this.accelerationX = xDir/dd * 0.1D;
        this.accelerationY = yDir/dd * 0.1D;
        this.accelerationZ = zDir/dd * 0.1D;
        
        this.shootingEntity = attacker;
	}
	
	@Override
	public void onUpdate(){
		if (!this.stopped){
			if (!this.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.isBlockLoaded(new BlockPos(this)))){
	            this.setDead();
	        }else{
	            super.onUpdate();
	            
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
	                this.accelerationX = 0.0D;
	                this.accelerationY = 0.0D;
	                this.accelerationZ = 0.0D;
	                this.motionX = 0.0D;
	                this.motionY = 0.0D;
	                this.motionZ = 0.0D;
	                this.stopped = true;
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
	        
	        this.accelerationX *= 0.93;
	        this.accelerationY *= 0.93;
	        this.accelerationZ *= 0.93;
	        
	        if (!worldObj.isRemote && Math.abs(this.accelerationX) < 0.005D && Math.abs(this.accelerationY) < 0.005D && Math.abs(this.accelerationZ) < 0.005D){
	        	this.accelerationX = 0.0D;
                this.accelerationY = 0.0D;
                this.accelerationZ = 0.0D;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
	        	this.stopped = true;
	        }
		}
        
        if (worldObj.isRemote){
        	/*
        	for (int i = 0; i < 8; i++){
        		this.worldObj.spawnParticle(EnumParticleTypes.REDSTONE, (posX-0.5)+rand.nextFloat(), (posY-0.5)+rand.nextFloat(), (posZ-0.5)+rand.nextFloat(), 0.0D, 0.0D, 0.0D, new int[0]);
        	}
        	
        	if (this.zapEffectTimer > 0){
        		this.zapEffectTimer--;
        	}
        	if (this.zapEffectTimer <= 0){
        		this.zapEffectTimer = 6;
        		
        		int count = 10;
        		double xto = posX + (-2D+rand.nextFloat()*4D);
        		double yto = posY + (-2D+rand.nextFloat()*4D);
        		double zto = posZ + (-2D+rand.nextFloat()*4D);
				for (int i = 0; i < count; i++){
					double xx = posX + ((xto - posX)/count)*i;
					double yy = posY + ((yto - posY)/count)*i;
					double zz = posZ + ((zto - posZ)/count)*i;
					worldObj.spawnParticle(EnumParticleTypes.FLAME, xx, yy, zz, 0.0D, 0.0D, 0.0D, new int[0]);
					worldObj.spawnParticle(EnumParticleTypes.REDSTONE, xx, yy, zz, 0.0D, 0.0D, 0.0D, new int[0]);
				}
        	}
        	*/
        }else{
			life--;
			if (life <= 0){
				this.setDead();
			}
			
			if (this.shockCooldown <= 0){
				int range = 9;
				
				AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range);
				List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
				if (list != null && !list.isEmpty()){
					Iterator iterator = list.iterator();
		            while (iterator.hasNext()){
		            	EntityPlayer entity = (EntityPlayer) iterator.next();
		            	
		            	entity.attackEntityFrom(new EntityDamageSource("mob", this.shootingEntity), 5.0F + (30.0F * this.worldObj.getDifficulty().getDifficultyId()));
		            	worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.wither.shoot", 1, 0.95F+0.05F*rand.nextFloat());
		            	worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndFirePunch", 1, 0.95F+0.05F*rand.nextFloat());
		            	
		            	ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(3, this.getEntityId(), entity.getEntityId()), new TargetPoint(dimension, posX, posY, posZ, 48));
		            	this.shockCooldown = 12;
		            }
				}
			}else{
				this.shockCooldown--;
			}
        }
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
        
        this.stopped = tagCompound.getBoolean("stopped");
        this.life = tagCompound.getShort("lifeRemaining");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound){
        tagCompound.setTag("motion", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
        tagCompound.setTag("direction", this.newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
        tagCompound.setBoolean("stopped", this.stopped);
        tagCompound.setShort("lifeRemaining", (short) this.life);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public int getBrightnessForRender(float p_70070_1_){
        return 15728880;
    }
    
	@Override
    public float getBrightness(float p_70013_1_){
        return 1.0F;
    }
}