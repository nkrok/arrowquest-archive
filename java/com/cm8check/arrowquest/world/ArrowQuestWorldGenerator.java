package com.cm8check.arrowquest.world;

import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.world.gen.Schematic;
import com.cm8check.arrowquest.world.gen.WorldGenSchematics;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class ArrowQuestWorldGenerator implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (!ArrowQuest.DEV_MODE && chunkX % 7 == 0 && chunkZ % 7 == 0) {
			int id = world.provider.getDimensionId();

			switch (id) {
			case -1:
				generateNether(world, random, chunkX * 16, chunkZ * 16);
				break;
			case 0:
				generateSurface(world, random, chunkX * 16, chunkZ * 16);
				break;
			case 1:
				generateEnd(world, random, chunkX * 16, chunkZ * 16);
				break;
			}
		}
	}

	private void generateNether(World world, Random rand, int x, int z) {
		BlockPos spawnPos = world.getSpawnPoint();
		Schematic schem = WorldGenSchematics.getWeightedRandomSchematic(
				world.getBiomeGenForCoords(new BlockPos(x, 30, z)).biomeID, rand, x, z, spawnPos.getX(),
				spawnPos.getZ());

		if (schem != null) {
			BlockPos horizon = null;

			for (int i = 15; i < 110; i++) {
				BlockPos pos = new BlockPos(x, i, z);
				if (world.getBlockState(pos).getBlock() == Blocks.air) {
					horizon = pos;
					break;
				}
			}

			if (horizon == null) {
				return;
			}
			
			if (schem.baseY > -5) {
				outerloop:
				while (true) {
					if (horizon.getY() < 1) {
						return;
					}
					
					for (int i = 0; i <= schem.width; i += schem.width) {
						for (int k = 0; k <= schem.length; k += schem.length) {
							if (world.getBlockState(horizon.add(i, -1, k)).getBlock() == Blocks.air) {
								horizon = horizon.add(0, -1, 0);
								continue outerloop;
							}
						}
					}
					
					break;
				}
			}
			
			System.out.println("Nether castle generated");
			
			WorldGenSchematics.generateSchematic(world, schem, x, horizon.getY() - schem.baseY, z, true, true, false);
		}
	}

	private void generateSurface(World world, Random rand, int x, int z) {
		// magic crystal ores
		this.addOreSpawn(ModBlocks.blockOreFire, world, rand, x, z, 16, 16, 2 + rand.nextInt(3), 13, 4, 25);
		this.addOreSpawn(ModBlocks.blockOreWater, world, rand, x, z, 16, 16, 2 + rand.nextInt(3), 13, 4, 25);
		this.addOreSpawn(ModBlocks.blockOreAir, world, rand, x, z, 16, 16, 2 + rand.nextInt(3), 13, 4, 25);
		this.addOreSpawn(ModBlocks.blockOreEarth, world, rand, x, z, 16, 16, 2 + rand.nextInt(3), 13, 4, 25);
		this.addOreSpawn(ModBlocks.blockOreLife, world, rand, x, z, 16, 16, 2 + rand.nextInt(3), 13, 4, 25);
		
		Schematic schem;
		
		if (ArrowQuestWorldData.policeBossDefeated && !ArrowQuestWorldData.generatedFinalPortal) {
			System.out.println("Final portal generated. x: " + x + ", z: " + z);
			schem = WorldGenSchematics.finalDestinationGate;
			ArrowQuestWorldData.generatedFinalPortal = true;
			ArrowQuestWorldData.instance.markDirty();
		}
		else {
			BlockPos spawnPos = world.getSpawnPoint();
			schem = WorldGenSchematics.getWeightedRandomSchematic(
					world.getBiomeGenForCoords(new BlockPos(x, 30, z)).biomeID, rand, x, z, spawnPos.getX(),
					spawnPos.getZ());
		}

		if (schem != null) {
			BlockPos horizon = world.getHorizon(new BlockPos(x, 0, z));
			
			if (schem.baseY > -5) {
				outerloop:
				while (true) {
					if (horizon.getY() < 1) {
						return;
					}
					
					for (int i = 0; i <= schem.width; i += schem.width) {
						for (int k = 0; k <= schem.length; k += schem.length) {
							if (world.getBlockState(horizon.add(i, -1, k)).getBlock() == Blocks.air) {
								horizon = horizon.add(0, -1, 0);
								continue outerloop;
							}
						}
					}
					
					break;
				}
			}

			WorldGenSchematics.generateSchematic(world, schem, x, horizon.getY() - schem.baseY, z, schem.resetAir, true, false);
		}
	}

	private void generateEnd(World world, Random rand, int x, int z) {

	}

	/**
	 * Adds an Ore Spawn to Minecraft
	 * 
	 * @param block          The Block to spawn
	 * @param world          The World to spawn in
	 * @param random         A Random object for retrieving random positions within
	 *                       the world to spawn the Block
	 * @param blockXPos      An int for passing the X-Coordinate for the Generation
	 *                       method
	 * @param blockZPos      An int for passing the Z-Coordinate for the Generation
	 *                       method
	 * @param maxX           An int for setting the maximum X-Coordinate values for
	 *                       spawning on the X-Axis on a Per-Chunk basis
	 * @param maxZ           An int for setting the maximum Z-Coordinate values for
	 *                       spawning on the Z-Axis on a Per-Chunk basis
	 * @param maxVeinSize    An int for setting the maximum size of a vein
	 * @param chancesToSpawn An int for the Number of chances available for the
	 *                       Block to spawn per-chunk
	 * @param minY           An int for the minimum Y-Coordinate height at which
	 *                       this block may spawn
	 * @param maxY           An int for the maximum Y-Coordinate height at which
	 *                       this block may spawn
	 **/
	private void addOreSpawn(Block block, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ,
			int maxVeinSize, int chancesToSpawn, int minY, int maxY) {
		int maxPossY = minY + (maxY - 1);
		assert maxY > minY : "addOreSpawn: The maximum Y must be greater than the Minimum Y";
		assert maxX > 0 && maxX <= 16 : "addOreSpawn: The Maximum X must be greater than 0 and less than 16";
		assert minY > 0 : "addOreSpawn: The Minimum Y must be greater than 0";
		assert maxY < 256 && maxY > 0 : "addOreSpawn: The Maximum Y must be less than 256 but greater than 0";
		assert maxZ > 0 && maxZ <= 16 : "addOreSpawn: The Maximum Z must be greater than 0 and less than 16";

		int diffBtwnMinMaxY = maxY - minY;
		for (int x = 0; x < chancesToSpawn; x++) {
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffBtwnMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			(new WorldGenMinable(block.getDefaultState(), maxVeinSize)).generate(world, random,
					new BlockPos(posX, posY, posZ));
		}
	}
}