package com.cm8check.arrowquest.world.gen;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityPoliceTeleporter;
import com.cm8check.arrowquest.tileentity.TileEntitySingleSpawner;
import com.cm8check.arrowquest.tileentity.TileEntityStructureController;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraft.world.biome.BiomeGenOcean;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class WorldGenSchematics {
	public static final int STRUCTURE_SIZE_MEDIUM = 40000;
	public static final int STRUCTURE_SIZE_LARGE = 150000;
	
	private static int[] tierStructureCounts = new int[4];
	
	public static Schematic albatrossShip;
	public static Schematic asimovShip;
	public static Schematic heinleinShip;
	public static Schematic primroseShip;
	public static Schematic satellite;
	public static Schematic satelliteFinal;
	
	private static int satellitesGenerated;
	
	public static Schematic finalDestination;
	public static Schematic finalDestinationGate;

	private static int biomeCount;
	private static ArrayList<Schematic> schematicList;
	private static ArrayList<ArrayList<Schematic>> schematicsForBiomeID;
	private static ArrayList<Schematic> netherSchematics;

	public static Schematic getWeightedRandomSchematic(int biomeID, Random rand, int x, int z, int spawnX, int spawnZ) {
		if (biomeID >= schematicsForBiomeID.size()) {
			return null;
		}

		int distX = Math.abs(x - spawnX);
		int distZ = Math.abs(z - spawnZ);
		
		int maxMinWeight = 20000 - (distX + distZ)*4;
		if (maxMinWeight < 1000) {
			maxMinWeight = 1000;
		}
		
		//System.out.println(maxMinWeight);
		
		int minWeight = rand.nextInt(maxMinWeight);
		ArrayList<Schematic> choices = new ArrayList<Schematic>();
		
		ArrayList<Schematic> list;
		
		if (biomeID == BiomeGenBase.hell.biomeID) {
			list = netherSchematics;
		}
		else {
			list = schematicsForBiomeID.get(biomeID);
		}

		for (Schematic schem : list) {
			if (schem.genWeight * schem.genWeight > minWeight) {
				if (schem.minDistanceFromSpawn == 0 || distX > schem.minDistanceFromSpawn
						|| distZ > schem.minDistanceFromSpawn) {
					choices.add(schem);
				}
			}
		}

		if (choices.size() > 0) {
			return choices.get(rand.nextInt(choices.size()));
		}

		return null;
	}
	
	public static Schematic getSchematicByName(String name) {
		for (Schematic schem : schematicList) {
			if (schem.name.equals(name)) {
				return schem;
			}
		}
		
		return null;
	}
	
	public static int[] getTierStructureCounts() {
		return tierStructureCounts;
	}

	public static void loadSchematics() {
		schematicList = new ArrayList<Schematic>();
		schematicsForBiomeID = new ArrayList<ArrayList<Schematic>>();
		netherSchematics = new ArrayList<Schematic>();

		for (biomeCount = 0; biomeCount < BiomeGenBase.getBiomeGenArray().length; biomeCount++) {
			if (BiomeGenBase.getBiomeGenArray()[biomeCount] == null) {
				break;
			}

			schematicsForBiomeID.add(new ArrayList<Schematic>());
		}

		albatrossShip = load("albatrossShip", 0, 0);
		asimovShip = load("asimovShip", 0, 0);
		heinleinShip = load("heinleinShip", 0, 0);
		primroseShip = load("primroseShip", 0, 0);
		satellite = load("satellite", 0, 0);
		satelliteFinal = load("satelliteFinal", 0, 0);
		
		finalDestination = load("finalDestination", 0, 0);
		finalDestinationGate = load("finalDestinationGate", 0, 0);
		
		tierStructureCounts[3] = 2;

		load("pirateShipSmall", 5, 65, 0, BiomeGenBase.ocean);
		load("pirateShipBig", 8, 40, 100, BiomeGenBase.ocean);
		load("smallYacht", 2, 57, 0, BiomeGenBase.ocean);
		
		load("battleTowerBark", 100, 0, BiomeGenBase.forest, BiomeGenBase.forestHills, BiomeGenBase.birchForest,
				BiomeGenBase.birchForestHills, BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.taigaHills,
				BiomeGenBase.megaTaiga, BiomeGenBase.megaTaigaHills, BiomeGenBase.jungle, BiomeGenBase.savanna, BiomeGenBase.savannaPlateau);
		load("battleTowerSandstone", 100, 0, BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.mesa,
				BiomeGenBase.mesaPlateau, BiomeGenBase.mesaPlateau_F);
		load("battleTowerIce", 100, 0, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.frozenOcean,
				BiomeGenBase.coldTaiga, BiomeGenBase.coldTaigaHills, BiomeGenBase.coldBeach);
		load("battleTowerStone", 100, 0, BiomeGenBase.plains, BiomeGenBase.extremeHills, BiomeGenBase.extremeHillsEdge,
				BiomeGenBase.extremeHillsPlus, BiomeGenBase.stoneBeach);
		load("undergroundTower", 39, 100, 0);
		load("ruins1", 10, 100, 0);
		load("brokenHouse", 100, 0);
		load("smallMobStronghold", 27, 80, 0);
		load("scorpionPit", 12, 100, 0);
		load("robotSpiderTower", 47, 0);
		load("seleneTemple", 80, 0);
		load("colosseum", 50, 900, BiomeGenBase.desert, BiomeGenBase.mesa, BiomeGenBase.mesaPlateau,
				BiomeGenBase.mesaPlateau_F);
		load("desertTowerSmall", 100, 0, BiomeGenBase.desert, BiomeGenBase.desertHills);
		load("miscSmallTower", 80, 0);
		//load("miscTallTower", 37, 300);

		load("humanMegaCastle1", 30, 700, BiomeGenBase.plains);
		load("humanRoyalCastle", 30, 700);
		load("humanSmallCastle", 60, 100, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.frozenOcean,
				BiomeGenBase.frozenRiver, BiomeGenBase.coldTaiga, BiomeGenBase.coldTaigaHills, BiomeGenBase.coldBeach);
		load("skyCastle", -65, 35, 900);
		load("humanLendusCastle", 7, 30, 700);

		load("orcBase1", 30, 900, BiomeGenBase.desert, BiomeGenBase.mesa, BiomeGenBase.mesaPlateau,
				BiomeGenBase.mesaPlateau_F);
		load("netherBigCastle", 25, 0, BiomeGenBase.hell);
		load("sandstoneCastle", 70, 0, BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.mesa,
				BiomeGenBase.mesaPlateau, BiomeGenBase.mesaPlateau_F);
		load("orcSandstoneCastle2", 70, 0, BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.mesa,
				BiomeGenBase.mesaPlateau, BiomeGenBase.mesaPlateau_F);
		load("orcDesertTemple1", 70, 0, BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.mesa,
				BiomeGenBase.mesaPlateau, BiomeGenBase.mesaPlateau_F);
		load("orcDesertTemple2", 70, 0, BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.mesa,
				BiomeGenBase.mesaPlateau, BiomeGenBase.mesaPlateau_F);

		load("elfBattleTowerCastle", 50, 100, BiomeGenBase.forest, BiomeGenBase.forestHills, BiomeGenBase.birchForest,
				BiomeGenBase.birchForestHills, BiomeGenBase.taiga, BiomeGenBase.taigaHills, BiomeGenBase.megaTaiga, BiomeGenBase.megaTaigaHills, BiomeGenBase.jungle,
				BiomeGenBase.savanna, BiomeGenBase.savannaPlateau);
		load("elfCastle1", 30, 400, BiomeGenBase.forest, BiomeGenBase.birchForest, BiomeGenBase.taiga, BiomeGenBase.megaTaiga,
				BiomeGenBase.jungle, BiomeGenBase.savanna, BiomeGenBase.savannaPlateau);
		load("elfSmallCastle1", 80, 0, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.frozenOcean,
				BiomeGenBase.frozenRiver, BiomeGenBase.coldTaiga, BiomeGenBase.coldTaigaHills, BiomeGenBase.coldBeach);
		load("elfOakheartCastle", 50, 100);
		load("elfChateau", 5, 45, 900, BiomeGenBase.forest, BiomeGenBase.forestHills, BiomeGenBase.birchForest,
				BiomeGenBase.birchForestHills, BiomeGenBase.taiga, BiomeGenBase.taigaHills, BiomeGenBase.jungle,
				BiomeGenBase.savanna, BiomeGenBase.savannaPlateau);
		load("elfGiantCathedral", 45, 600);
		load("elfSmallHouse", 60, 100);
		load("elfSmallPagoda", 40, 100);

		load("dwarfStronghold1", 27, 45, 100, BiomeGenBase.plains, BiomeGenBase.savanna, BiomeGenBase.savannaPlateau);
		load("dwarfSnowCastle", 50, 150, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.frozenOcean,
				BiomeGenBase.frozenRiver, BiomeGenBase.coldTaiga, BiomeGenBase.coldTaigaHills, BiomeGenBase.coldBeach);
		load("dwarfCastle2", 50, 100);
		load("dwarfCastle3", 50, 100);
		load("dwarfMansion", 42, 900);

		//load("fmLargeFort", 30, 400);
		//load("giantCathedral", 27, 550);
		load("fmAirshipBig", -40, 30, 600);

		//load("vampireChateau", 13, 30, 200);
		load("vampireCastle1", 44, 150);
	}

	/**
	 * @param schem                Filename
	 * @param genWeight            Number from 1 to 100 representing the frequency
	 *                             (use 0 to prevent natural generation) of the
	 *                             structure generating
	 * @param minDistanceFromSpawn Minimum distance (in blocks) from the spawn point
	 *                             before the structure is allowed to generate
	 * @param biomes               The biomes that the structure should generate in
	 *                             (leave empty for all biomes)
	 * @return A new Schematic object
	 */
	private static Schematic load(String schem, int genWeight, int minDistanceFromSpawn, BiomeGenBase... biomes) {
		return load(schem, 0, genWeight, minDistanceFromSpawn, biomes);
	}

	/**
	 * @param schem                Filename
	 * @param baseY                Number of blocks to shift structure downward for
	 *                             generation
	 * @param genWeight            Number from 1 to 100 representing the frequency
	 *                             (use 0 to prevent natural generation) of the
	 *                             structure generating
	 * @param minDistanceFromSpawn Minimum distance (in blocks) from the spawn point
	 *                             before the structure is allowed to generate
	 * @param biomes               The biomes that the structure should generate in
	 *                             (leave empty for all biomes)
	 * @return A new Schematic object
	 */
	private static Schematic load(String schem, int baseY, int genWeight, int minDistanceFromSpawn,
			BiomeGenBase... biomes) {
		try {
			System.out.println("Loading schematic " + schem);
			InputStream is = WorldGenSchematics.class.getClassLoader()
					.getResourceAsStream("assets/arrowquest/schematics/" + schem + ".schematic");
			NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(is);
			short width = nbtdata.getShort("Width");
			short height = nbtdata.getShort("Height");
			short length = nbtdata.getShort("Length");

			byte[] blocks = nbtdata.getByteArray("Blocks");
			byte[] data = nbtdata.getByteArray("Data");

			System.out.println(schem + ": Schem size: " + width + " x " + height + " x " + length);
			NBTTagList tileentities = nbtdata.getTagList("TileEntities", 10);
			is.close();
			
			boolean resetAir = (biomes.length == 0 || biomes[0] != BiomeGenBase.ocean);
			Schematic newSchem = new Schematic(schem, tileentities, width, height, length, blocks, data, genWeight,
					minDistanceFromSpawn, baseY, resetAir);

			if (genWeight > 0) {
				if (biomes.length == 0) {
					// all biomes (excludes water, desert, and snow biomes)
					for (int i = 0; i < biomeCount; i++) {
						if (!(BiomeGenBase.getBiomeGenArray()[i] instanceof BiomeGenOcean) && !(BiomeGenBase.getBiomeGenArray()[i] instanceof BiomeGenDesert) && !BiomeGenBase.getBiomeGenArray()[i].isSnowyBiome()) {
							schematicsForBiomeID.get(i).add(newSchem);
						}
					}
				} else {
					// specific biomes
					for (BiomeGenBase b : biomes) {
						if (b == BiomeGenBase.hell) {
							netherSchematics.add(newSchem);
						}
						
						schematicsForBiomeID.get(b.biomeID).add(newSchem);
					}
				}
			}
			
			schematicList.add(newSchem);
			
			if (genWeight > 0) {
				int size = width * height * length;
				
				if (size > STRUCTURE_SIZE_LARGE) {
					tierStructureCounts[2]++;
				}
				else if (size > STRUCTURE_SIZE_MEDIUM) {
					tierStructureCounts[1]++;
				}
				else {
					tierStructureCounts[0]++;
				}
			}

			return newSchem;
		} catch (Exception e) {
			System.out.println("Loading schematic " + schem + " failed: " + e.toString());
			return null;
		}
	}

	public static void generateSchematic(World world, Schematic sh, int x, int y, int z, boolean resetAir, boolean createController, boolean fast) {
		if (y < 2 || y + sh.height > 255) {
			return;
		}
		
		long startTime = System.currentTimeMillis();
		
		int spawnerCount = 0;
		int dim = world.provider.getDimensionId();

		List<BlockPos> laterBlocksPos = new ArrayList<BlockPos>();
		List<Byte> laterBlocksID = new ArrayList<Byte>();
		List<Byte> laterBlocksMeta = new ArrayList<Byte>();

		Chunk currentChunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);

		int i = 0;
		for (int sy = y; sy < y + sh.height; sy++)
			for (int sz = z; sz < z + sh.length; sz++)
				for (int sx = x; sx < x + sh.width; sx++) {
					Block b;
					int id;
					if (sh.blocks[i] < 0) {
						id = 256 + sh.blocks[i];
					} else {
						id = sh.blocks[i];
					}

					if (id == 198) { // -58
						b = ModBlocks.blockSingleSpawner;
						spawnerCount++;
					} else if (id == 199) { // -57
						b = ModBlocks.blockDungeonChest;
					} else if (id == 207) {
						b = ModBlocks.blockPoliceChest;
					} else if (id == 210) {
						b = ModBlocks.blockFinalDestinationTeleporter;
					} else if (id == 212 || id == 213) {
						b = ModBlocks.blockFinalDestinationReturn;
					} else if (id == 211) {
						b = ModBlocks.blockFinalDestinationCheckpoint;
					} else if (id == 214) {
						if (dim == ModLib.dimFinalBossID)
							b = ModBlocks.blockFinalDestinationCheckpoint;
						else
							b = ModBlocks.blockPoliceTeleporter;
					} else if (id == 215) {
						b = ModBlocks.blockAirlockController;
					} else if (id == 216) {
						b = ModBlocks.blockFinalDestinationFinalCheckpoint;
					} else {
						b = Block.getBlockById(id);
					}

					if (sx >> 4 != currentChunk.xPosition || sz >> 4 != currentChunk.zPosition) {
						currentChunk = world.getChunkFromChunkCoords(sx >> 4, sz >> 4);
					}

					ExtendedBlockStorage sect = currentChunk.getBlockStorageArray()[sy >> 4];
					if (sect == null) {
						sect = new ExtendedBlockStorage((sy >> 4) * 16, true);
						currentChunk.getBlockStorageArray()[sy >> 4] = sect;
					}

					int xx = sx & 15;
					int yy = sy & 15;
					int zz = sz & 15;

					BlockPos pos = new BlockPos(sx, sy, sz);
					
					world.removeTileEntity(pos);
					
					if (fast) {
						if (resetAir) {
							sect.set(xx, yy, zz, Blocks.air.getDefaultState());
						}
						
						sect.setExtSkylightValue(xx, yy, zz, 0);
						world.markBlockForUpdate(pos);
					}
					else {
						if (resetAir && b == Blocks.air) {
							world.setBlockToAir(pos);
						}
					}

					if (b == Blocks.torch || b == Blocks.ladder || b == Blocks.redstone_torch || b == Blocks.vine
							|| b == Blocks.bed || b instanceof BlockDoor) {
						laterBlocksPos.add(pos);
						laterBlocksID.add((byte) id);
						laterBlocksMeta.add(sh.data[i]);
					} else if (b != Blocks.air) {
						if (fast) {
							sect.set(xx, yy, zz, b.getStateFromMeta(sh.data[i]));
						}
						else {
							world.setBlockState(pos, b.getStateFromMeta(sh.data[i]), 2);
						}
					}

					i++;
				}

		for (int j = 0; j < laterBlocksID.size(); j++) {
			Block b = Block.getBlockById(laterBlocksID.get(j));
			BlockPos pos = laterBlocksPos.get(j);
			
			if (fast) {
				if (pos.getX() >> 4 != currentChunk.xPosition || pos.getZ() >> 4 != currentChunk.zPosition) {
					currentChunk = world.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
				}

				ExtendedBlockStorage sect = currentChunk.getBlockStorageArray()[pos.getY() >> 4];
				if (sect == null) {
					sect = new ExtendedBlockStorage((pos.getY() >> 4) * 16, true);
					currentChunk.getBlockStorageArray()[pos.getY() >> 4] = sect;
				}
				
				sect.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, b.getStateFromMeta(laterBlocksMeta.get(j)));
				world.markBlockForUpdate(pos);
			}
			else {
				world.setBlockState(pos, b.getStateFromMeta(laterBlocksMeta.get(j)), 2);
			}
		}
		
		if (spawnerCount == 0) {
			createController = false;
		}
		
		BlockPos controllerPos = new BlockPos(x + sh.width/2, y - 1, z + sh.length/2);
		
		if (createController) {
			world.setBlockState(controllerPos, ModBlocks.blockStructureController.getDefaultState());
			
			TileEntityStructureController tile = (TileEntityStructureController) world.getTileEntity(controllerPos);
			tile.init(spawnerCount, sh.name);
		}

		if (sh.tileEntities != null) {
			for (int i1 = 0; i1 < sh.tileEntities.tagCount(); i1++) {
				NBTTagCompound nbt = sh.tileEntities.getCompoundTagAt(i1);
				BlockPos newPos = new BlockPos(nbt.getInteger("x") + x, nbt.getInteger("y") + y,
						nbt.getInteger("z") + z);
				TileEntity tile = world.getTileEntity(newPos);

				if (tile != null) {
					tile.readFromNBT(nbt);
					world.setTileEntity(newPos, tile);
					
					if (createController && tile instanceof TileEntitySingleSpawner) {
						((TileEntitySingleSpawner) tile).setControllerLocation(controllerPos);
					}
					else if (sh == satellite && tile instanceof TileEntityPoliceTeleporter) {
						TileEntityPoliceTeleporter porter = (TileEntityPoliceTeleporter) tile;
						
						switch (satellitesGenerated) {
						case 0:
							porter.setDestination(15);
							break;
							
						case 1:
							porter.setDestination(14);
							break;
							
						case 2:
							porter.setDestination(13);
							break;
							
						case 3:
							porter.setDestination(12);
							break;
							
						case 4:
							porter.setDestination(10);
							break;
						}
						
						satellitesGenerated++;
						if (satellitesGenerated >= 5) {
							satellitesGenerated = 0;
						}
					}
				}
			}
		}
		
		if (fast) {
			for (int sz = z; sz < z + sh.length; sz += 16)
				for (int sx = x; sx < x + sh.width; sx += 16) {
					Chunk chunk = world.getChunkFromChunkCoords(sx >> 4, sz >> 4);
					chunk.generateSkylightMap();
					// chunk.func_150809_p();
				}
		}

		//System.out.println("Time: " + Long.toString(System.currentTimeMillis() - startTime));
	}
	
	public static void generatePoliceDimension(World world) {
		generateSchematic(world, primroseShip, 0, 16, 0, false, false, true);
		generateSchematic(world, heinleinShip, 120, 32, 0, false, false, true);
		generateSchematic(world, albatrossShip, 107, 84, 617, false, false, true);
		generateSchematic(world, asimovShip, -90, 72, 300, false, false, true);
		
		generateSchematic(world, satellite, 0, 110, 398, false, false, true); // spawn
		generateSchematic(world, satellite, 52, 111, 462, false, false, true);
		generateSchematic(world, satellite, 110, 16, 622, false, false, true);
		generateSchematic(world, satellite, 74, 98, 87, false, false, true);
		generateSchematic(world, satellite, -117, 80, 357, false, false, true);
		generateSchematic(world, satelliteFinal, 195, 100, 573, false, false, true);
	}
	
	public static void generateFinalDestination(World world) {
		generateSchematic(world, finalDestination, 0, 64, 0, false, false, true);
	}
	
	public static void logClearStructure(EntityPlayer player, String schematicName, String categoryTag) {
		NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
    	NBTTagList clearList;
    	boolean unique = true;
    	
    	if (nbt.hasKey(ModLib.nbtStructuresClearedList)) {
        	clearList = nbt.getTagList(ModLib.nbtStructuresClearedList, 10);
        	
        	for (int i = 0; i < clearList.tagCount(); i++) {
        		String str = clearList.getCompoundTagAt(i).getString("name");
        		if (str.equals(schematicName)) {
        			unique = false;
        			break;
        		}
        	}
    	}
    	else {
    		clearList = new NBTTagList();
    		nbt.setTag(ModLib.nbtStructuresClearedList, clearList);
    	}
    	
    	if (unique) {
        	NBTTagCompound newTag = new NBTTagCompound();
        	newTag.setString("name", schematicName);
        	clearList.appendTag(newTag);
        	
        	nbt.setByte(categoryTag, (byte) (nbt.getByte(categoryTag) + 1));
    	}
    	
    	nbt.setShort(ModLib.nbtStructuresCleared, (short) (nbt.getShort(ModLib.nbtStructuresCleared) + 1));
	}
}