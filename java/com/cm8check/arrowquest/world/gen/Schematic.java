package com.cm8check.arrowquest.world.gen;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.biome.BiomeGenBase;

public class Schematic {
	public final String name;
	public final NBTTagList tileEntities;
	public final short width;
	public final short height;
	public final short length;
	public final byte[] blocks;
	public final byte[] data;

	// generation info
	public final int genWeight;
	public final int minDistanceFromSpawn;
	public final int baseY;
	public final boolean resetAir;

	public Schematic(String name, NBTTagList tileEntities, short width, short height, short length, byte[] blocks, byte[] data,
			int weight, int minDistFromSpawn, int baseY, boolean resetAir) {
		this.name = name;
		this.tileEntities = tileEntities;
		this.width = width;
		this.height = height;
		this.length = length;
		this.blocks = blocks;
		this.data = data;
		
		this.genWeight = weight;
		this.minDistanceFromSpawn = minDistFromSpawn;
		this.baseY = baseY;
		this.resetAir = resetAir;
	}
}