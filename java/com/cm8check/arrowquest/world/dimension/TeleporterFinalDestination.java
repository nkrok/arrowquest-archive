package com.cm8check.arrowquest.world.dimension;

import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.world.gen.WorldGenSchematics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterFinalDestination extends Teleporter{
	private final WorldServer worldServerInstance;
	
	public TeleporterFinalDestination(WorldServer worldIn){
		super(worldIn);
		this.worldServerInstance = worldIn;
	}
	
	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw){
		if (this.worldServerInstance.provider.getDimensionId() != ModLib.dimFinalBossID){
			BlockPos spawn = this.worldServerInstance.getSpawnPoint();
			entityIn.setLocationAndAngles(spawn.getX(), spawn.getY(), spawn.getZ(), entityIn.rotationYaw, 0.0F);
		}else{
	        entityIn.setLocationAndAngles(26, 66, 69, entityIn.rotationYaw, 0.0F);
	        
	        if (entityIn instanceof EntityPlayer){
	        	EntityPlayer player = (EntityPlayer) entityIn;
	        	player.setSpawnChunk(new BlockPos(26, 66, 69), true, ModLib.dimFinalBossID);
	        }
	        
	        this.worldServerInstance.setSpawnPoint(new BlockPos(26, 66, 69));
		}
		
		entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;
	}
	
	@Override
	public boolean placeInExistingPortal(Entity entityIn, float p_180620_2_){
		return true;
	}
	
	@Override
	public boolean makePortal(Entity p_85188_1_){
		return true;
	}
}