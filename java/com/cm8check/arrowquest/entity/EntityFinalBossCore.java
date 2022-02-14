package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.item.ModItems;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFinalBossCore extends EntityLiving{
	public EntityFinalBoss boss;
	private int checkForBossDelay;
	private int spawnerDelay;
	private int spawnsDone;
	
	public EntityFinalBossCore(World world){
		super(world);
		this.setSize(3.0F, 3.0F);
		this.experienceValue = 500;
		this.isImmuneToFire = true;
		this.setRotation(0.0F, 0.0F);
		
		if (!world.isRemote){
			this.checkForBossDelay = 20;
			this.spawnerDelay = 40;
			this.spawnsDone = 0;
		}
	}
	
	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
    }
	
	@Override
	public void onLivingUpdate(){
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.rotationYaw = 0.0F;
		this.rotationPitch = 0.0F;
		
		super.onLivingUpdate();
		
		if (!worldObj.isRemote){
			if (this.boss == null){
				if (this.checkForBossDelay > 0){
					this.checkForBossDelay--;
				}else if (this.checkForBossDelay == 0){
					this.checkForBossDelay = -1;
					this.getBoss();
				}
			}
			
			if (this.spawnerDelay > 0 && this.spawnsDone < 6){
				this.spawnerDelay--;
			}else{
				this.spawnerDelay = 40;
				
				EntityPlayer player = worldObj.getClosestPlayer(getPositionVector().xCoord, getPositionVector().yCoord, getPositionVector().zCoord, 16.0D);
				if (player != null && !player.capabilities.isCreativeMode){
					this.spawnerDelay = 1000 + rand.nextInt(300);
					this.spawnsDone++;
					
					EntityLivingBase entity;
					
					if (rand.nextInt(3) > 0){
						entity = new EntityPhantasm(worldObj);
						entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemDemonBlade));
						entity.setPosition(getPositionVector().xCoord, getPositionVector().yCoord, getPositionVector().zCoord);
						worldObj.spawnEntityInWorld(entity);
					}
					
					for (int i = 0; i < 2 + rand.nextInt(3); i++){
						entity = new EntityRobotSpider(worldObj);
						entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
						entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
						entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
						entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
						entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
						entity.setPosition((getPositionVector().xCoord-2) + rand.nextInt(5), getPositionVector().yCoord, (getPositionVector().zCoord-2) + rand.nextInt(5));
						worldObj.spawnEntityInWorld(entity);
					}
					
					if (rand.nextInt(2) == 0){
						for (int i = 0; i < 2 + rand.nextInt(2); i++){
							entity = new EntityObsidianWarrior(worldObj);
							entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemExecutionerAxe));
							entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemObsidianBoots));
							entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemObsidianLeggings));
							entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemObsidianChestplate));
							entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemObsidianHelmet));
							entity.setPosition((getPositionVector().xCoord-2) + rand.nextInt(5), getPositionVector().yCoord, (getPositionVector().zCoord-2) + rand.nextInt(5));
							worldObj.spawnEntityInWorld(entity);
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra){
		for (int i = 0; i < 10; i++){
			ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(1);
			if (stack != null){
				this.entityDropItem(stack, 0.0F);
			}
		}
	}
	
	@Override
	protected boolean canDespawn(){
		return false;
	}
	
	@Override
	protected void despawnEntity(){}
	
	@Override
	protected String getHurtSound(){
        return "mob.irongolem.hit";
    }
	
	@Override
	protected String getDeathSound(){
        return "mob.irongolem.death";
    }
	
	@Override
	public void setDead(){
		super.setDead();
		if (!this.worldObj.isRemote){
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 1.25F, true);
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 4.0F, false);
			this.worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndMegafreeze", 5.0F, 1.0F);
			if (this.boss != null){
				float hp = this.boss.getHealth()-360;
				this.boss.setHealth(hp);
				this.boss.spawnEnemies();
				
				if (hp < 250){
					
				}
			}
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound){
		super.writeEntityToNBT(compound);
		compound.setByte("SpawnsDone", (byte) this.spawnsDone);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound){
		super.readEntityFromNBT(compound);
		this.spawnsDone = compound.getByte("SpawnsDone");
	}
	
	private void getBoss(){
		System.out.println("Core boss check");
		
		boolean flag = false;
		List list = worldObj.getEntities(EntityFinalBoss.class, new Predicate(){
			@Override
			public boolean apply(Object input){
				return true;
			}
		});
		if (list != null && !list.isEmpty()){
			Iterator iterator = list.iterator();
            while (iterator.hasNext()){
            	EntityFinalBoss entity = (EntityFinalBoss) iterator.next();
            	this.boss = entity;
            	flag = true;
            	break;
            }
		}
		
		if (!flag){
			System.out.println("WARNING: BOSS CORE COULD NOT FIND BOSS ENTITY");
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_){
		return 15728880;
	}
	
	@Override
	public float getBrightness(float p_70013_1_){
		return 15.0F;
	}
}