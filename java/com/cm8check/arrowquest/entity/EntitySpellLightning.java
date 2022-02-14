package com.cm8check.arrowquest.entity;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntitySpellLightning extends EntityLightningBolt{
    private int lightningState;
    public long boltVertex;
    private int boltLivingTime;
    private EntityLivingBase attacker;
    public float damage = 11.0F;

    public EntitySpellLightning(World worldIn, double posX, double posY, double posZ, EntityLivingBase attacker){
        super(worldIn, posX, posY, posZ);
        this.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
        this.attacker = attacker;
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
    }
    
    @Override
    public void onUpdate(){
    	this.onEntityUpdate();

        if (this.lightningState == 2)
        {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.lightningState;

        if (this.lightningState < 0)
        {
            if (this.boltLivingTime == 0)
            {
                this.setDead();
            }
            else if (this.lightningState < -this.rand.nextInt(10))
            {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
                BlockPos blockpos = new BlockPos(this);

                if (!this.worldObj.isRemote && this.worldObj.getGameRules().getGameRuleBooleanValue("doFireTick") && this.worldObj.isAreaLoaded(blockpos, 10) && this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(this.worldObj, blockpos))
                {
                    this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
                }
            }
        }

        if (this.lightningState == 0)
        {
            if (this.worldObj.isRemote)
            {
                this.worldObj.setLastLightningBolt(2);
            }
            else
            {
                double d0 = 2.0D;
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 6.0D + d0, this.posZ + d0));

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = (Entity)list.get(i);
                    
                    if (entity != attacker){
                    	entity.attackEntityFrom(new EntityDamageSourceIndirect("lightningBolt", this, attacker).setMagicDamage(), this.damage);
                        entity.setFire(8);
                    }
                }
            }
        }
    }
}