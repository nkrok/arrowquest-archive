package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.network.packet.PacketSpawnParticles;
import com.google.common.base.Predicate;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBlazeBoss extends EntityAQBoss implements IBossDisplayData{
	private int spellCooldown;
	private int spawnCooldown;
	private Random rand;
	private boolean canSpawnEnemies = false;
	private int atk = 0;
	private int nextFireballIndex = 0;
	
	private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
	
	public EntityBlazeBoss(World world){
		super(world);
		this.setSize(2.0F, 6.0F);
		this.experienceValue = 100;
		this.isImmuneToFire = true;
		
		this.rand = ArrowQuest.RAND;
		
		this.spellCooldown = 60 + rand.nextInt(60);
		this.spawnCooldown = 10;
		
		this.tasks.addTask(1, new AIFireballAttack());
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1000.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(15.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		if (!this.onGround && this.motionY < 0.0D){
            this.motionY *= 0.6D;
        }

        if (worldObj.isRemote){
        	BossStatus.setBossStatus(this, true);
        	
            if (this.rand.nextInt(24) == 0 && !this.isSilent()){
                this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int i = 0; i < 2; ++i){
                this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
            }
            
            ClientSoundHelper.bossMusicCheck("arrowquest:musicNuadha", this);
        }
		
		super.onLivingUpdate();
		
		if (!worldObj.isRemote && this.getAttackTarget() != null){
			if (spawnCooldown > 0){
				spawnCooldown -= 1;
			}else if (canSpawnEnemies){
				spawnCooldown = 200 + rand.nextInt(100);
				canSpawnEnemies = false;
				
				EntityLiving entity;
				
				entity = new EntityBlaze(worldObj);
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
				entity.setPosition(this.posX, this.posY, this.posZ);
				worldObj.spawnEntityInWorld(entity);
				
				entity = new EntityNetherOrc(worldObj);
				
				ItemStack sword = new ItemStack(Items.diamond_sword);
				sword.addEnchantment(Enchantment.fireAspect, 2);
				entity.setCurrentItemOrArmor(0, sword);
				
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
				
				entity.setPosition(this.posX, this.posY, this.posZ);
				worldObj.spawnEntityInWorld(entity);
			}
			
			if (spellCooldown > 0){
				spellCooldown -= 1;
			}else{
				if (atk == 0){
					boolean flag = false;
					
					AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 10, posY - 1, posZ - 10, posX + 10, posY + 2, posZ + 10);
					List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
					if (list != null && !list.isEmpty()){
						Iterator iterator = list.iterator();
			            while (iterator.hasNext()){
			            	EntityPlayer entity = (EntityPlayer) iterator.next();
			            	
			            	entity.attackEntityFrom(new EntityDamageSource("magic", this).setMagicDamage(), 30.0F);
			            	if (!entity.isImmuneToFire()){
			            		entity.setFire(5);
			            	}
			            	
			            	entity.addVelocity(0.0D, 1.0D, 0.0D);
			            	((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity));
			            	
			            	flag = true;
			            	canSpawnEnemies = true;
			            }
					}
					
					if (flag){
						ArrowQuest.packetPipeline.sendToAllAround(new PacketSpawnParticles(3, Math.round((float) posX), Math.round((float) posY), Math.round((float) posZ)), new TargetPoint(dimension, posX, posY, posZ, 32));
						worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 2.0F, 0.95F+(0.05F*rand.nextFloat()));
						worldObj.playSoundEffect(posX, posY, posZ, "mob.ghast.fireball", 2.0F, 0.95F+(0.05F*rand.nextFloat()));
					}
					
					spellCooldown = 40 + rand.nextInt(60);
					atk = 1;
				}else if (atk == 1){
					spellCooldown = 0;
					
					int amt = 32;
					float f = (360/amt);
					worldObj.playSoundEffect(posX, posY, posZ, "mob.ghast.fireball", 3.0F, 0.95F+(0.05F*rand.nextFloat()));
					
					double d1 = Math.sin(Math.toRadians(f*nextFireballIndex));
					double d2 = -0.5D;
					double d3 = Math.cos(Math.toRadians(f*nextFireballIndex));
					double dd = (double) MathHelper.sqrt_double(d1 * d1 + d2 * d2 + d3 * d3);
	                d1 = d1/dd * 0.5D;
	                d2 = d2/dd * 0.5D;
	                d3 = d3/dd * 0.5D;
	                
                    EntitySpellFireball fireball = new EntitySpellFireball(worldObj, posX, posY + (double) (height / 2.0F) + 0.5D, posZ, d1, d2, d3);
                    fireball.shootingEntity = this;
                    fireball.damage = 25.0F;
                    worldObj.spawnEntityInWorld(fireball);
                    
                    nextFireballIndex++;
                    if (nextFireballIndex >= amt){
                    	spellCooldown = 40;
                    	nextFireballIndex = 0;
                    	atk = 2;
                    }
				}else if (atk == 2){
					EntitySpellLightning entity = new EntitySpellLightning(worldObj, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, this);
					worldObj.addWeatherEffect(entity);
					
					atk = rand.nextInt(2);
					spellCooldown = 40;
				}
			}
		}
	}
	
	@Override
	protected void onDeathUpdate(){
        this.deathTime++;
        
        if (worldObj.isRemote && this.deathTime == 1){
        	ClientSoundHelper.stopMusic();
        }
        
        if (!this.isDead){
        	if (this.deathTime % 3 == 0){
        		worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 6.0F, 0.95F+(0.05F*rand.nextFloat()));
        		worldObj.spawnParticle(EnumParticleTypes.CLOUD, (posX-2)+(rand.nextFloat()*4), (posY-2)+(rand.nextFloat()*4), (posZ-2)+(rand.nextFloat()*4), 0.0D, 0.0D, 0.0D, new int[0]);
        		worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, (posX-1)+(rand.nextFloat()*2), (posY-1)+(rand.nextFloat()*2), (posZ-1)+(rand.nextFloat()*2), 0.0D, 0.0D, 0.0D, new int[0]);
        	}
        	
	        if (this.deathTime >= 100){
	        	this.setDead();
	        	worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBossExplode", 6.0F, 1.0F);
	        	
	            int i;
	
	            if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")){
	                i = this.getExperiencePoints(this.attackingPlayer);
	                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
	                while (i > 0)
	                {
	                    int j = EntityXPOrb.getXPSplit(i);
	                    i -= j;
	                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
	                }
	            }
	
	            for (i = 0; i < 20; i++){
	                double d2 = this.rand.nextGaussian() * 0.02D;
	                double d0 = this.rand.nextGaussian() * 0.02D;
	                double d1 = this.rand.nextGaussian() * 0.02D;
	                this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
	            }
	        }
        }
    }
	
	@Override
	public void setDead(){
		super.setDead();
		
		if (!worldObj.isRemote){
			ArrowQuestEntityHelper.distributeBossXP(worldObj, posX, posY, posZ, 25000, this);
		}
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(2);
		if (stack != null){
			this.entityDropItem(stack, 0.0F);
		}
	}
	
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){
		
	}
	
	@Override
	protected boolean canDespawn(){
		return false;
	}
	
	protected void entityInit(){
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }
	
    protected String getLivingSound(){
        return "mob.blaze.breathe";
    }
    
    protected String getHurtSound(){
        return "mob.blaze.hit";
    }
    
    protected String getDeathSound(){
        return "mob.blaze.hit";
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float p_70070_1_){
        return 15728880;
    }
    
    public float getBrightness(float p_70013_1_){
        return 15.0F;
    }

    protected void updateAITasks(){
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

        if (entitylivingbase != null && entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset)
        {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    public void fall(float distance, float damageMultiplier){}
    
    public boolean isBurning(){
        return this.func_70845_n();
    }

    public boolean func_70845_n(){
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void func_70844_e(boolean p_70844_1_){
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

    class AIFireballAttack extends EntityAIBase{
        private EntityBlazeBoss hostEntity = EntityBlazeBoss.this;
        private int field_179467_b;
        private int field_179468_c;

        public AIFireballAttack()
        {
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = this.hostEntity.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.field_179467_b = 0;
        }

        /**
         * Resets the task
         */
        public void resetTask()
        {
            this.hostEntity.func_70844_e(false);
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            --this.field_179468_c;
            EntityLivingBase entitylivingbase = this.hostEntity.getAttackTarget();
            double d0 = this.hostEntity.getDistanceSqToEntity(entitylivingbase);

            if (d0 < 4.0D)
            {
                if (this.field_179468_c <= 0)
                {
                    this.field_179468_c = 20;
                    this.hostEntity.attackEntityAsMob(entitylivingbase);
                }

                this.hostEntity.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            else if (d0 < 256.0D)
            {
                double d1 = entitylivingbase.posX - this.hostEntity.posX;
                double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (this.hostEntity.posY + (double)(this.hostEntity.height / 2.0F));
                double d3 = entitylivingbase.posZ - this.hostEntity.posZ;
                double dd = (double) MathHelper.sqrt_double(d1 * d1 + d2 * d2 + d3 * d3);
                d1 = d1/dd * 0.5D;
                d2 = d2/dd * 0.5D;
                d3 = d3/dd * 0.5D;

                if (this.field_179468_c <= 0)
                {
                    ++this.field_179467_b;

                    if (this.field_179467_b == 1)
                    {
                        this.field_179468_c = 20;
                        this.hostEntity.func_70844_e(true);
                    }
                    else if (this.field_179467_b <= 6)
                    {
                        this.field_179468_c = 3;
                    }
                    else
                    {
                        this.field_179468_c = 80;
                        this.field_179467_b = 0;
                        this.hostEntity.func_70844_e(false);
                        
                        ArrowQuest.packetPipeline.sendToAllAround(new PacketSpawnParticles(2, Math.round((float) hostEntity.posX), Math.round((float) hostEntity.posY),Math.round((float) hostEntity.posZ)), new TargetPoint(hostEntity.dimension, hostEntity.posX, hostEntity.posY, hostEntity.posZ, 64));
                        hostEntity.worldObj.playSoundEffect(hostEntity.posX, hostEntity.posY, hostEntity.posZ, "random.fizz", 1.0F, 0.95F+(0.05F*rand.nextFloat()));
                    }

                    if (this.field_179467_b > 1){
                        this.hostEntity.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, new BlockPos((int)this.hostEntity.posX, (int)this.hostEntity.posY, (int)this.hostEntity.posZ), 0);
                        
                        EntitySpellFireball fireball = new EntitySpellFireball(hostEntity.worldObj, hostEntity.posX, hostEntity.posY + (double)(this.hostEntity.height / 2.0F) + 0.5D, hostEntity.posZ, d1*0.5D, d2*0.5D, d3*0.5D);
                        fireball.shootingEntity = hostEntity;
                        fireball.damage = 20.0F;
                        this.hostEntity.worldObj.spawnEntityInWorld(fireball);
                    }
                }

                this.hostEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            }
            else
            {
                this.hostEntity.getNavigator().clearPathEntity();
                this.hostEntity.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }

            super.updateTask();
        }
    }
}