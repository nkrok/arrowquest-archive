package com.cm8check.arrowquest.entity;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityRaceBase extends EntityMob{
	public int aimCooldown;
	public int aimTimeRemaining;
	public int aimTimeRemainBase;
	public boolean bossRaiseArms;
	public boolean isBlocking;
	public int blockCooldown;
	
	public EntityRaceBase(World world){
		super(world);
		this.aimCooldown = -1;
		this.aimTimeRemainBase = 40;
		this.blockCooldown = -1;
	}
	
	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		
		if (this.aimCooldown > 0){
			this.aimCooldown--;
		}else if (this.aimCooldown == 0){
			this.aimTimeRemaining--;
			if (this.aimTimeRemaining <= 0){
				this.aimCooldown = -2;
			}
		}else if (this.aimCooldown == -2 && !worldObj.isRemote){
			if (this.getAttackTarget() != null){
				this.setAimCooldown(0);
			}
		}
		
		if (this.blockCooldown > 0){
			this.blockCooldown--;
			if (this.blockCooldown < 1){
				this.blockCooldown = -1;
				this.isBlocking = false;
			}
		}
	}
	
	protected void setDefaultAimCooldown(){
		this.setAimCooldown(15);
	}
	
	protected void setAimCooldown(int cooldown){
		this.aimCooldown = cooldown;
		this.aimTimeRemaining = this.aimTimeRemainBase;
		ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(0, this.getEntityId(), this.aimCooldown), new TargetPoint(dimension, posX, posY, posZ, 48));
	}
	
	protected void setBlocking(boolean blocking){
		this.isBlocking = blocking;
		int val;
		if (blocking){
			val = 1;
		}else{
			val = 0;
		}
		
		ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(4, this.getEntityId(), val), new TargetPoint(dimension, posX, posY, posZ, 48));
	}
	
	protected void setBlockingWithCooldown(int cooldown){
		this.isBlocking = true;
		this.blockCooldown = cooldown;
		ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(5, this.getEntityId(), cooldown), new TargetPoint(dimension, posX, posY, posZ, 48));
	}
	
	@Override
	protected boolean canDespawn(){
		return false;
	}
}