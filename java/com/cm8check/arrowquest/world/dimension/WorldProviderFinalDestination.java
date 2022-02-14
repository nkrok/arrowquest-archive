package com.cm8check.arrowquest.world.dimension;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderFinalDestination extends WorldProvider{
	@Override
	public IChunkProvider createChunkGenerator(){
		return new ChunkProviderFinalDestination(this.worldObj);
	}
	
	@Override
	public void registerWorldChunkManager(){
		WorldChunkManagerVoid.init();
		this.worldChunkMgr = new WorldChunkManagerVoid(worldObj);
		this.dimensionId = ModLib.dimFinalBossID;
	}
	
	@Override
	public String getDimensionName(){
		return "The Final Destination";
	}

	@Override
	public String getInternalNameSuffix(){
		return "_finalboss";
	}
	
	@Override
	public BlockPos getRandomizedSpawnPoint(){
		return this.worldObj.getSpawnPoint();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getWelcomeMessage(){
		return "The Final Destination";
	}
	
	@Override
	public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        return 0.5F;
    }
}