package com.cm8check.arrowquest.world.dimension;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderPoliceDimension extends WorldProvider{
	@Override
	public IChunkProvider createChunkGenerator(){
		return new ChunkProviderPoliceHQ(this.worldObj);
	}
	
	@Override
	public void registerWorldChunkManager(){
		WorldChunkManagerVoid.init();
		this.worldChunkMgr = new WorldChunkManagerVoid(worldObj);
		this.dimensionId = ModLib.dimPoliceBaseID;
	}
	
	@Override
	public String getDimensionName(){
		return "Trueforce Headquarters";
	}

	@Override
	public String getInternalNameSuffix(){
		return "_tpchq";
	}
	
	@Override
	public BlockPos getRandomizedSpawnPoint(){
		return this.worldObj.getSpawnPoint();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getWelcomeMessage(){
		return "Trueforce HQ";
	}
	
	@Override
	public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        return 0.5F;
    }
}