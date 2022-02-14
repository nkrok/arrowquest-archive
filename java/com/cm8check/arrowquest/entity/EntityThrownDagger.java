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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityThrownDagger extends Entity{
	public ToolMaterial material;
	public Item item;
	public int materialIndex;
	public int meta;
	
	private double accelerationX;
	private double accelerationY;
	private double accelerationZ;
	private EntityLivingBase shootingEntity;
	
	public EntityThrownDagger(World world){
		super(world);
		this.setSize(0.75F, 0.75F);
		
		if (world.isRemote){
			this.setMaterial(ItemDagger.tempThrownDaggerMaterial);
		}
	}
	
	public EntityThrownDagger(World world, double x, double y, double z, double xDir, double yDir, double zDir, EntityLivingBase attacker){
		this(world);
		this.setLocationAndAngles(x, y, z, attacker.rotationYaw, this.rotationPitch);
		this.accelerationX = xDir * 0.4D;
        this.accelerationY = yDir * 0.4D;
        this.accelerationZ = zDir * 0.4D;
        this.shootingEntity = attacker;
	}
	
	@Override
	public void onUpdate(){
		if (!this.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.isBlockLoaded(new BlockPos(this)))){
            this.setDead();
        }else{
            super.onUpdate();
            
            this.rotationPitch += 36;
            this.accelerationY -= 0.015F;
            
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

                if (entity1.canBeCollidedWith() && (!entity1.isEntityEqual(this.shootingEntity))){
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

        if (this.isInWater()){
            for (int j = 0; j < 4; ++j){
                float f3 = 0.25F;
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f3, this.posY - this.motionY * (double)f3, this.posZ - this.motionZ * (double)f3, this.motionX, this.motionY, this.motionZ, new int[0]);
            }

            f2 = 0.8F;
        }

        this.motionX += this.accelerationX;
        this.motionY += this.accelerationY;
        this.motionZ += this.accelerationZ;
        this.motionX *= (double)f2;
        this.motionY *= (double)f2;
        this.motionZ *= (double)f2;
        this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        this.setPosition(this.posX, this.posY, this.posZ);
	}
	
	protected void onImpact(MovingObjectPosition movingObject){
        if (!this.worldObj.isRemote){
            boolean flag;

            if (movingObject.entityHit != null){
                flag = movingObject.entityHit.attackEntityFrom(new EntityDamageSource("player", this.shootingEntity).setProjectile(), 6.0F + this.material.getDamageVsEntity());

                if (flag){
                    this.func_174815_a(this.shootingEntity, movingObject.entityHit);
                }
            }
            
            this.setDead();
        }
    }
	
	@Override
	public void setDead(){
		super.setDead();
		
		if (!worldObj.isRemote && this.meta < this.item.getMaxDamage()){
			EntityItem entityitem = new EntityItem(worldObj, this.posX, this.posY, this.posZ, new ItemStack(this.item, 1, this.meta));
			float f3 = 0.05F;
	        entityitem.motionX = (float)this.rand.nextGaussian() * f3;
	        entityitem.motionY = (float)this.rand.nextGaussian() * f3 + 0.2F;
	        entityitem.motionZ = (float)this.rand.nextGaussian() * f3;
	        worldObj.spawnEntityInWorld(entityitem);
		}
	}
	
	public void setMaterial(ToolMaterial mat){
		this.material = mat;
		
		switch(mat){
		case WOOD:
			this.materialIndex = 0;
			this.item = ModItems.itemDaggerWood;
		break;
		case STONE:
			this.materialIndex = 1;
			this.item = ModItems.itemDaggerStone;
		break;
		case IRON:
			this.materialIndex = 2;
			this.item = ModItems.itemDaggerIron;
		break;
		case GOLD:
			this.materialIndex = 3;
			this.item = ModItems.itemDaggerGold;
		break;
		case EMERALD:
			this.materialIndex = 4;
			this.item = ModItems.itemDaggerDiamond;
		break;
		
		default:
			this.materialIndex = 0;
			this.item = ModItems.itemDaggerWood;
		break;
		}
	}
	
	public void setMaterialByIndex(int index){
		this.materialIndex = index;
		
		switch(index){
		case 0:
			this.material = ToolMaterial.WOOD;
			this.item = ModItems.itemDaggerWood;
		break;
		case 1:
			this.material = ToolMaterial.STONE;
			this.item = ModItems.itemDaggerStone;
		break;
		case 2:
			this.material = ToolMaterial.IRON;
			this.item = ModItems.itemDaggerIron;
		break;
		case 3:
			this.material = ToolMaterial.GOLD;
			this.item = ModItems.itemDaggerGold;
		break;
		case 4:
			this.material = ToolMaterial.EMERALD;
			this.item = ModItems.itemDaggerDiamond;
		break;
		
		default:
			this.material = ToolMaterial.WOOD;
			this.item = ModItems.itemDaggerWood;
		break;
		}
	}
	
	@Override
	protected void entityInit(){
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompound){
		this.meta = tagCompound.getShort("meta");
		this.setMaterialByIndex(tagCompound.getByte("material"));

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
		tagCompound.setShort("meta", (short) this.meta);
		tagCompound.setByte("material", (byte) this.materialIndex);
        tagCompound.setTag("motion", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
        tagCompound.setTag("direction", this.newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
	}
}