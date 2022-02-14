package com.cm8check.arrowquest.world.dimension;

import static net.minecraft.world.biome.BiomeGenBase.plains;

import com.google.common.collect.Lists;

import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;

public class WorldChunkManagerVoid extends WorldChunkManager{
	public WorldChunkManagerVoid(World worldIn){
        super(worldIn.getSeed(), worldIn.getWorldInfo().getTerrainType(), worldIn.getWorldInfo().getGeneratorOptions());
    }
	
	public static void init(){
		allowedBiomes = Lists.newArrayList(plains);
	}
}