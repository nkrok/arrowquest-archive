package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;
import com.cm8check.arrowquest.network.packet.PacketSpawnParticles;
import com.cm8check.arrowquest.tileentity.TileEntityDungeonChest;
import com.google.common.base.Predicate;

public class EntityWizardBoss extends EntityAQBoss implements IBossDisplayData, IRangedAttackMob{
	private boolean displayHealth;
	private int atk;
	private int fireballsLeft;
	private int nextFireballTimer;
	private int teleportAttackTimer;
	
	private int resetArmsTimer;
	public int armState;
	
	public int spawnX;
	public int spawnY;
	public int spawnZ;
	
	public EntityWizardBoss(World world){
		super(world);
		this.setSize(1.2F, 3.6F);
		this.atk = this.rand.nextInt(4);
		this.armState = 0;
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 70, 64.0F));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(650.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    }
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float accuracy){
		if (this.atk == 0){
			this.nextFireballTimer = 0;
			this.fireballsLeft = 3 + rand.nextInt(4);
			
			this.setArmState(2, this.fireballsLeft*4);
		}else if (this.atk == 1){
			double d1 = target.posX - this.posX;
            double d2 = target.posY - (this.posY + (double) (this.height / 2.0F));
            double d3 = target.posZ - this.posZ;
            double dd = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            d1 = d1/dd * 0.6D;
            d2 = d2/dd * 0.6D;
            d3 = d3/dd * 0.6D;
            
            EntitySpellFireball fireball = new EntitySpellFireball(worldObj, posX, posY + (double) (this.height / 2.0F) + 0.5D, posZ, d1, d2, d3);
            fireball.shootingEntity = this;
            fireball.damage = 16.0F;
            fireball.explodeRand = 1;
            this.worldObj.spawnEntityInWorld(fireball);
            
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.ghast.fireball", 2.5F, 0.95F+(0.05F*rand.nextFloat()));
            this.setArmState(2, 10);
		}else if (this.atk == 2){
			double d1 = target.posX - this.posX;
            double d2 = target.posY - (this.posY + (double) (this.height / 2.0F));
            double d3 = target.posZ - this.posZ;
            double dd = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            d1 = d1/dd * 0.6D;
            d2 = d2/dd * 0.6D;
            d3 = d3/dd * 0.6D;
            Vec3 vec = new Vec3(d1, d2, d3);
            
            for (int i = -1; i < 2; i++){
            	Vec3 newVec = vec.rotateYaw(0.3F * i);
	            EntitySpellFireball fireball = new EntitySpellFireball(worldObj, posX, posY + (double) (this.height / 2.0F) + 0.5D, posZ, newVec.xCoord, newVec.yCoord, newVec.zCoord);
	            fireball.shootingEntity = this;
	            fireball.damage = 16.0F;
	            fireball.explodeRand = 1;
	            this.worldObj.spawnEntityInWorld(fireball);
            }
            
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.ghast.fireball", 2.5F, 0.95F+(0.05F*rand.nextFloat()));
            this.setArmState(3, 10);
		}else if (this.atk > 2){
			this.setPositionAndUpdate(target.posX, target.posY, target.posZ);
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.endermen.portal", 2.0F, 0.95F+(0.05F*rand.nextFloat()));
			this.teleportAttackTimer = 15 + rand.nextInt(3);
		}
		
		int lastatk = this.atk;
		this.atk = this.rand.nextInt(5);
		if (this.atk == lastatk){
			if (this.atk < 4){
				this.atk++;
			}else{
				this.atk = 0;
			}
		}
	}
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (this.worldObj.isRemote){
            if (ClientSoundHelper.bossMusicCheck("arrowquest:musicDwarfBoss", this)){
            	this.displayHealth = true;
            }
            if (this.displayHealth){
            	BossStatus.setBossStatus(this, true);
            }
		}else{
			if (this.resetArmsTimer > 0){
				this.resetArmsTimer--;
				if (this.resetArmsTimer < 1){
					this.armState = 0;
					ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(6, this.getEntityId(), 0), new TargetPoint(dimension, posX, posY, posZ, 128));
				}
			}
			
			if (getAttackTarget() != null){
				if (this.fireballsLeft > 0){
					this.nextFireballTimer--;
					if (this.nextFireballTimer < 1){
						this.nextFireballTimer = 4;
						this.fireballsLeft--;
						
						double d1 = getAttackTarget().posX - this.posX;
			            double d2 = getAttackTarget().getEntityBoundingBox().minY + (double) (getAttackTarget().height / 2.0F) - (this.posY + (double) (this.height / 2.0F));
			            double d3 = getAttackTarget().posZ - this.posZ;
			            double dd = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
			            d1 = d1/dd * 0.6D;
			            d2 = d2/dd * 0.6D;
			            d3 = d3/dd * 0.6D;
			            double d4 = 0.08D;
			            
			            EntitySpellFireball fireball = new EntitySpellFireball(worldObj, posX, posY + (double) (this.height / 2.0F) + 0.5D, posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4);
			            fireball.shootingEntity = this;
			            fireball.damage = 14.0F;
			            this.worldObj.spawnEntityInWorld(fireball);
			            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.ghast.fireball", 2.5F, 0.95F+(0.05F*rand.nextFloat()));
					}
				}
				
	            if (this.teleportAttackTimer > 0){
	            	this.teleportAttackTimer--;
	            	if (this.teleportAttackTimer < 1){
	            		this.teleportAttackTimer = -1;
	            		
	            		int a = rand.nextInt(3);
	            		if (a == 0){
	            			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 5, posY - 1, posZ - 5, posX + 5, posY + 1, posZ + 5);
	            			List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
	            			if (list != null && !list.isEmpty()){
	            				Iterator iterator = list.iterator();
	            	            while (iterator.hasNext()){
	            	            	EntityPlayer entity = (EntityPlayer) iterator.next();
	            	            	if (!entity.isBlocking()){
	            	            		entity.addVelocity(0.0D, 1.5D, 0.0D);
	            	            		((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity));
	            	            	}
	            	            }
	            			}
	            			
	            			worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndSuperQuake", 1.5F, 0.95F+(0.05F*rand.nextFloat()));
	            			
	            			this.setArmState(1, 15);
	            		}else if (a == 1){
	            			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 5, posY - 1, posZ - 5, posX + 5, posY + 1, posZ + 5);
	            			List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
	            			if (list != null && !list.isEmpty()){
	            				Iterator iterator = list.iterator();
	            	            while (iterator.hasNext()){
	            	            	EntityPlayer entity = (EntityPlayer) iterator.next();
	            	            	if (!entity.isBlocking()){
	            	            		entity.attackEntityFrom(new EntityDamageSource("magic", this).setMagicDamage(), 15.0F);
	            	            	}
	            	            }
	            			}
	            			
	            			ArrowQuest.packetPipeline.sendToAllAround(new PacketSpawnParticles(0, Math.round((float) posX), Math.round((float) posY), Math.round((float) posZ)), new TargetPoint(dimension, posX, posY, posZ, 32));
							worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBurst1", 2.0F, 1.5F+(0.05F*rand.nextFloat()));
	            		}else{
	            			if (getAttackTarget() instanceof EntityPlayer){
	            				EntityPlayer player = (EntityPlayer) getAttackTarget();
	            				if (!player.isBlocking()){
			            			EntitySpellLightning entity = new EntitySpellLightning(worldObj, (getAttackTarget().posX-1.0D)+(2.0F*this.rand.nextFloat()), getAttackTarget().posY, (getAttackTarget().posZ-1.0D)+(2.0F*this.rand.nextFloat()), this);
			            			entity.damage = 9.0F;
			    					worldObj.addWeatherEffect(entity);
	            				}
	            			}
	            		}
	            	}
	            }
			}
		}
	}
	
	private void setArmState(int state, int time){
		this.armState = state;
		this.resetArmsTimer = time;
		ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(6, this.getEntityId(), state), new TargetPoint(dimension, posX, posY, posZ, 128));
	}
	
	@Override
	public void setDead(){
		super.setDead();
		if (this.worldObj.isRemote){
			ClientSoundHelper.stopMusic();
		}else{
			ArrowQuestEntityHelper.distributeBossXP(worldObj, posX, posY, posZ, 5000, this);
			
			BlockPos pos = new BlockPos(this.spawnX+1, this.spawnY, this.spawnZ);
			worldObj.setBlockState(pos, Blocks.chest.getDefaultState());
			TileEntityChest tile = (TileEntityChest) worldObj.getTileEntity(pos);
			TileEntityDungeonChest.setChestContents(tile, 0, 2);
			
			pos = new BlockPos(this.spawnX-1, this.spawnY, this.spawnZ);
			worldObj.setBlockState(pos, Blocks.chest.getDefaultState());
			tile = (TileEntityChest) worldObj.getTileEntity(pos);
			TileEntityDungeonChest.setChestContents(tile, 0, 2);
			
			pos = new BlockPos(this.spawnX, this.spawnY, this.spawnZ+1);
			worldObj.setBlockState(pos, Blocks.chest.getDefaultState());
			tile = (TileEntityChest) worldObj.getTileEntity(pos);
			TileEntityDungeonChest.setChestContents(tile, 0, 2);
			
			pos = new BlockPos(this.spawnX, this.spawnY, this.spawnZ-1);
			worldObj.setBlockState(pos, Blocks.chest.getDefaultState());
			tile = (TileEntityChest) worldObj.getTileEntity(pos);
			TileEntityDungeonChest.setChestContents(tile, 0, 1);
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
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_){}
	
	@Override
	protected String getDeathSound(){
        return "mob.wither.death";
    }
	
	@Override
	protected String getHurtSound(){
        return "arrowquest:sndWizardHurt";
    }
	
	@Override
	protected boolean canDespawn(){
		return false;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound){
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("SpawnX", this.spawnX);
		tagCompound.setByte("SpawnY", (byte) this.spawnY);
		tagCompound.setInteger("SpawnZ", this.spawnZ);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound){
		super.readEntityFromNBT(tagCompound);
		this.spawnX = tagCompound.getInteger("SpawnX");
		this.spawnY = tagCompound.getByte("SpawnY");
		this.spawnZ = tagCompound.getInteger("SpawnZ");
	}
}