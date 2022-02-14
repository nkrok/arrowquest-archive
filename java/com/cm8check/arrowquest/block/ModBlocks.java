package com.cm8check.arrowquest.block;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ModBlocks{
	public static Block blockSingleSpawner = new BlockSingleSpawner();
	public static Block blockDungeonChest = new BlockDungeonChest();
	public static Block blockBigCraftingTable = new BlockBigCraftingTable();
	public static Block blockOreFire = new BlockCrystalOre(1, ModLib.blockOreFireName);
	public static Block blockOreWater = new BlockCrystalOre(2, ModLib.blockOreWaterName);
	public static Block blockOreAir = new BlockCrystalOre(3, ModLib.blockOreAirName);
	public static Block blockOreEarth = new BlockCrystalOre(4, ModLib.blockOreEarthName);
	public static Block blockOreLife = new BlockCrystalOre(5, ModLib.blockOreLifeName);
	public static Block blockWandFurnace = new BlockWandFurnace();
	public static Block blockPoliceChest = new BlockPoliceChest();
	public static Block blockFireChest = new BlockFireChest();
	public static Block blockTemporaryBlock = new BlockTemporaryBlock();
	public static Block blockFinalDestinationTeleporter = new BlockFinalDestinationTeleporter();
	public static Block blockFinalDestinationCheckpoint = new BlockFinalDestinationCheckpoint();
	public static Block blockFinalDestinationReturn = new BlockFinalDestinationReturn();
	public static Block blockStructureController = new BlockStructureController();
	public static Block blockPoliceTeleporter = new BlockPoliceTeleporter();
	public static Block blockAirlockController = new BlockAirlockController();
	public static Block blockFinalDestinationFinalCheckpoint = new BlockFinalDestinationFinalCheckpoint();
	
	public static void init(){
		GameRegistry.registerBlock(blockSingleSpawner, ModLib.blockSingleSpawnerName);
		GameRegistry.registerBlock(blockDungeonChest, ModLib.blockDungeonChestName);
		GameRegistry.registerBlock(blockBigCraftingTable, ModLib.blockBigCraftingTableName);
		GameRegistry.registerBlock(blockOreFire, ModLib.blockOreFireName);
		GameRegistry.registerBlock(blockOreWater, ModLib.blockOreWaterName);
		GameRegistry.registerBlock(blockOreAir, ModLib.blockOreAirName);
		GameRegistry.registerBlock(blockOreEarth, ModLib.blockOreEarthName);
		GameRegistry.registerBlock(blockOreLife, ModLib.blockOreLifeName);
		GameRegistry.registerBlock(blockWandFurnace, ModLib.blockWandFurnaceName);
		GameRegistry.registerBlock(blockPoliceChest, ModLib.blockPoliceChestName);
		GameRegistry.registerBlock(blockFireChest, ModLib.blockFireChestName);
		GameRegistry.registerBlock(blockTemporaryBlock, ModLib.blockTemporaryBlockName);
		GameRegistry.registerBlock(blockFinalDestinationTeleporter, ModLib.blockFinalDestinationTeleporterName);
		GameRegistry.registerBlock(blockFinalDestinationCheckpoint, ModLib.blockFinalDestinationCheckpointName);
		GameRegistry.registerBlock(blockFinalDestinationReturn, ModLib.blockFinalDestinationReturnName);
		GameRegistry.registerBlock(blockStructureController, ModLib.blockStructureControllerName);
		GameRegistry.registerBlock(blockPoliceTeleporter, ModLib.blockPoliceTeleporterName);
		GameRegistry.registerBlock(blockAirlockController, ModLib.blockAirlockControllerName);
		GameRegistry.registerBlock(blockFinalDestinationFinalCheckpoint, ModLib.blockFinalDestinationFinalCheckpointName);
	}
}