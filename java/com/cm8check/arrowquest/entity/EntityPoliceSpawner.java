package com.cm8check.arrowquest.entity;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.network.packet.PacketOneshotAnimation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPoliceSpawner extends Entity {
	private int remainingSpawns;
	public int spawnTimer;
	
	private double nextSpawnX;
	private double nextSpawnY;
	private double nextSpawnZ;
	
	public boolean isElite;
	public EntityPlayer targetPlayer;
	
	private static final String[] spawnSounds = {
		"arrowquest:sndPoliceAppear1",
		"arrowquest:sndPoliceAppear2",
		"arrowquest:sndPoliceAppear3"
	};

	public EntityPoliceSpawner(World world) {
		super(world);
		this.setSize(0.1F, 0.1F);

		this.remainingSpawns = 4 + rand.nextInt(2);
		this.spawnTimer = 75;
		this.isElite = false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isRemote && !this.isDead) {
			this.spawnTimer--;
			if (this.spawnTimer <= 0) {
				this.spawnPolice();
			}
			else if (this.spawnTimer == 7) {
				this.prepareSpawn();
			}
		}
	}
	
	private void prepareSpawn() {
		BlockPos entityPos;
		if (this.isElite && this.targetPlayer != null) {
			entityPos = this.targetPlayer.getPosition();
			this.posX = this.targetPlayer.posX;
			this.posY = this.targetPlayer.posY;
			this.posZ = this.targetPlayer.posZ;
		}
		else {
			entityPos = this.getPosition();
		}
		
		double spawnX = this.posX;
		double spawnY = this.posY + 1.5D;
		double spawnZ = this.posZ;

		boolean flag = false;
		int tries = 0;
		while (tries < 10) {
			int xx = (entityPos.getX() - 5) + rand.nextInt(11);
			int yy = entityPos.getY() + 1;
			int zz = (entityPos.getZ() - 5) + rand.nextInt(11);
			if (worldObj.getBlockState(new BlockPos(xx, yy, zz)).getBlock() == Blocks.air) {
				spawnX = xx + 0.5D;
				spawnY = yy + 0.5D;
				spawnZ = zz + 0.5D;
				tries = 10;
				flag = true;
				break;
			}

			tries++;
		}
		
		this.nextSpawnX = spawnX;
		this.nextSpawnY = spawnY;
		this.nextSpawnZ = spawnZ;
		
		ArrowQuest.packetPipeline.sendToAllAround(new PacketOneshotAnimation(AnimationHelper.policeSpawn.id, spawnX, spawnY - 1.0D, spawnZ, 4.0F), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 48));
		worldObj.playSoundEffect(spawnX, spawnY, spawnZ, spawnSounds[rand.nextInt(spawnSounds.length)], 2.0F, 0.9F + 0.1F * rand.nextFloat());
	}

	private void spawnPolice() {
		this.remainingSpawns--;
		if (this.remainingSpawns <= 0) {
			this.setDead();
		}
		
		this.spawnTimer = 8;

		EntityPolice entity = new EntityPolice(worldObj);
		entity.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
		entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemPoliceBoots));
		entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemPoliceLeggings));
		entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemPoliceChestplate));
		entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemPoliceHelmet));
		entity.elite = this.isElite;

		if (rand.nextInt(2) == 0) {
			entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemPoliceGun));
		} else {
			entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemPoliceSabre));
		}

		entity.setPosition(this.nextSpawnX, this.nextSpawnY, this.nextSpawnZ);
		worldObj.spawnEntityInWorld(entity);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {

	}
}