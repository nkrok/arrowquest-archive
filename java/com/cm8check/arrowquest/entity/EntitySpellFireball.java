package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySpellFireball extends EntityFireball{
	private int life;
	public float damage = 6.0F;
	public int explodeRand = 0;
	public boolean useLava;
	
	private boolean pierceBlocks;
	private boolean spawnedNextFireball;
	private FireballStormController controller;
	
	public EntitySpellFireball(World world){
		super(world);
		this.setSize(0.4F, 0.4F);
		this.life = 25;
	}
	
	public EntitySpellFireball(World world, double x, double y, double z, double accelX, double accelY, double accelZ){
		super(world);
        this.setSize(5.0F, 5.0F);
        this.life = 50;
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
        this.accelerationX = accelX * 0.3D;
        this.accelerationY = accelY * 0.3D;
        this.accelerationZ = accelZ * 0.3D;
	}
	
	public void onUpdate(){
		super.onUpdate();
		if (!this.worldObj.isRemote){
			life--;
			if (life <= 0){
				this.setDead();
			}
			
			if (this.pierceBlocks) {
				if (controller.getFireballsRemaining() > 0 && this.life <= 56 && !spawnedNextFireball) {
					spawnedNextFireball = true;
					worldObj.spawnEntityInWorld(controller.getNextFireball());
				}
				
				if (this.posY <= controller.getExplodeY()) {
					this.pierceBlocks = false;
					
					AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 2.5D, posY - 1, posZ - 2.5D, posX + 2.5D, posY + 1, posZ + 2.5D);
					List list = worldObj.getEntitiesWithinAABB(EntityLiving.class, aabb);
					
					if (list != null && !list.isEmpty()){
						Iterator iterator = list.iterator();
						
			            while (iterator.hasNext()){
			            	EntityLiving entity = (EntityLiving) iterator.next(); 
			            	entity.attackEntityFrom(new EntityDamageSource("magic", this.shootingEntity).setMagicDamage().setProjectile(), damage);
			            }
					}
				}
			}
		}
	}

	@Override
	protected void onImpact(MovingObjectPosition movingObject){
        if (!this.pierceBlocks && !this.worldObj.isRemote) {
            boolean flag;

            if (movingObject.entityHit != null){
                flag = movingObject.entityHit.attackEntityFrom(new EntityDamageSource("magic", this.shootingEntity).setMagicDamage().setProjectile(), damage);

                if (flag){
                    this.func_174815_a(this.shootingEntity, movingObject.entityHit);

                    if (explodeRand == 0 && !movingObject.entityHit.isImmuneToFire()){
                        movingObject.entityHit.setFire(5);
                    }
                }
            }
            
            if (explodeRand > 0 && rand.nextInt(explodeRand) == 0){
            	this.worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 1.5F, false);
            	if (useLava && rand.nextInt(explodeRand+4) == 0){
            		this.worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 1.5F, true);
            		this.worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndMegafreeze", 1.0F, 0.95F+(0.05F*rand.nextFloat()));
            		
            		BlockPos pos = getPosition().up(2);
            		if (this.worldObj.getBlockState(pos).getBlock() == Blocks.air){
            			this.worldObj.setBlockState(pos, Blocks.flowing_lava.getDefaultState());
            		}
            	}
            }

            this.setDead();
        }
    }
	
	@Override
    public boolean canBeCollidedWith(){
        return false;
    }

	@Override
    public boolean attackEntityFrom(DamageSource source, float amount){
        return false;
    }
	
	public void setAsStormFireball(FireballStormController controller) {
		this.pierceBlocks = true;
		this.life = 60;
		this.controller = controller;
		this.spawnedNextFireball = false;
		this.explodeRand = 1;
		this.damage = 20.0F;
	}
}