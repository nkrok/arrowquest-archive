package com.cm8check.arrowquest.proxy;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.client.gui.GuiAQLog;
import com.cm8check.arrowquest.client.gui.GuiAirlockController;
import com.cm8check.arrowquest.client.gui.GuiBigCraftingTable;
import com.cm8check.arrowquest.client.gui.GuiFinalDestinationCheckpoint;
import com.cm8check.arrowquest.client.gui.GuiFinalDestinationFinalCheckpoint;
import com.cm8check.arrowquest.client.gui.GuiFinalDestinationReturn;
import com.cm8check.arrowquest.client.gui.GuiFinalDestinationTeleporter;
import com.cm8check.arrowquest.client.gui.GuiLevelUp;
import com.cm8check.arrowquest.client.gui.GuiMagicBackpack;
import com.cm8check.arrowquest.client.gui.GuiPickRace;
import com.cm8check.arrowquest.client.gui.GuiPoliceTeleporter;
import com.cm8check.arrowquest.client.gui.GuiWand;
import com.cm8check.arrowquest.client.gui.GuiWandFurnace;
import com.cm8check.arrowquest.entity.EntityBanishedSoul;
import com.cm8check.arrowquest.entity.EntityBlazeBoss;
import com.cm8check.arrowquest.entity.EntityCastleArcher;
import com.cm8check.arrowquest.entity.EntityCastleSoldier;
import com.cm8check.arrowquest.entity.EntityCastleSoldierKing;
import com.cm8check.arrowquest.entity.EntityDoomBullet;
import com.cm8check.arrowquest.entity.EntityDoomGuardian;
import com.cm8check.arrowquest.entity.EntityDwarf;
import com.cm8check.arrowquest.entity.EntityDwarfBoss;
import com.cm8check.arrowquest.entity.EntityElectricBall;
import com.cm8check.arrowquest.entity.EntityElf;
import com.cm8check.arrowquest.entity.EntityElfBoss;
import com.cm8check.arrowquest.entity.EntityExplosiveArrow;
import com.cm8check.arrowquest.entity.EntityFinalBoss;
import com.cm8check.arrowquest.entity.EntityFinalBossCore;
import com.cm8check.arrowquest.entity.EntityFlamePillar;
import com.cm8check.arrowquest.entity.EntityFlamePillarPurple;
import com.cm8check.arrowquest.entity.EntityFlyingStinger;
import com.cm8check.arrowquest.entity.EntityFurnaceMonster;
import com.cm8check.arrowquest.entity.EntityGenericProjectile;
import com.cm8check.arrowquest.entity.EntityHyperBlaze;
import com.cm8check.arrowquest.entity.EntityHyperScorpion;
import com.cm8check.arrowquest.entity.EntityNetherOrc;
import com.cm8check.arrowquest.entity.EntityObsidianWarrior;
import com.cm8check.arrowquest.entity.EntityOneshotAnimation;
import com.cm8check.arrowquest.entity.EntityOrc;
import com.cm8check.arrowquest.entity.EntityOrcBoss;
import com.cm8check.arrowquest.entity.EntityPhantasm;
import com.cm8check.arrowquest.entity.EntityPirate;
import com.cm8check.arrowquest.entity.EntityPirateCaptain;
import com.cm8check.arrowquest.entity.EntityPolice;
import com.cm8check.arrowquest.entity.EntityPoliceBoss;
import com.cm8check.arrowquest.entity.EntityPoliceMissile;
import com.cm8check.arrowquest.entity.EntityPoliceSpawner;
import com.cm8check.arrowquest.entity.EntityRobotSpider;
import com.cm8check.arrowquest.entity.EntitySpellFireball;
import com.cm8check.arrowquest.entity.EntityThrownDagger;
import com.cm8check.arrowquest.entity.EntityVampire;
import com.cm8check.arrowquest.entity.EntityVampireBoss;
import com.cm8check.arrowquest.entity.EntityWizard;
import com.cm8check.arrowquest.entity.EntityWizardBoss;
import com.cm8check.arrowquest.inventory.InventoryMagicBackpack;
import com.cm8check.arrowquest.inventory.InventoryWand;
import com.cm8check.arrowquest.inventory.container.ContainerAirlockController;
import com.cm8check.arrowquest.inventory.container.ContainerBigCraftingTable;
import com.cm8check.arrowquest.inventory.container.ContainerFinalDestinationCheckpoint;
import com.cm8check.arrowquest.inventory.container.ContainerFinalDestinationFinalCheckpoint;
import com.cm8check.arrowquest.inventory.container.ContainerFinalDestinationReturn;
import com.cm8check.arrowquest.inventory.container.ContainerFinalDestinationTeleporter;
import com.cm8check.arrowquest.inventory.container.ContainerMagicBackpack;
import com.cm8check.arrowquest.inventory.container.ContainerPoliceTeleporter;
import com.cm8check.arrowquest.inventory.container.ContainerWand;
import com.cm8check.arrowquest.inventory.container.ContainerWandFurnace;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityAirlockController;
import com.cm8check.arrowquest.tileentity.TileEntityDungeonChest;
import com.cm8check.arrowquest.tileentity.TileEntityFinalDestinationCheckpoint;
import com.cm8check.arrowquest.tileentity.TileEntityFinalDestinationFinalCheckpoint;
import com.cm8check.arrowquest.tileentity.TileEntityFinalDestinationReturn;
import com.cm8check.arrowquest.tileentity.TileEntityFinalDestinationTeleporter;
import com.cm8check.arrowquest.tileentity.TileEntityFireChest;
import com.cm8check.arrowquest.tileentity.TileEntityPoliceChest;
import com.cm8check.arrowquest.tileentity.TileEntityPoliceTeleporter;
import com.cm8check.arrowquest.tileentity.TileEntitySingleSpawner;
import com.cm8check.arrowquest.tileentity.TileEntityStructureController;
import com.cm8check.arrowquest.tileentity.TileEntityWandFurnace;
import com.cm8check.arrowquest.world.dimension.WorldProviderFinalDestination;
import com.cm8check.arrowquest.world.dimension.WorldProviderPoliceDimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {
	private static int modEntities;

	public void registerDimensions() {
		// Police HQ
		DimensionManager.registerProviderType(ModLib.dimPoliceBaseID, WorldProviderPoliceDimension.class, false);
		DimensionManager.registerDimension(ModLib.dimPoliceBaseID, ModLib.dimPoliceBaseID);

		// Final Destination
		DimensionManager.registerProviderType(ModLib.dimFinalBossID, WorldProviderFinalDestination.class, false);
		DimensionManager.registerDimension(ModLib.dimFinalBossID, ModLib.dimFinalBossID);
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntitySingleSpawner.class, ModLib.tileEntitySingleSpawnerName);
		GameRegistry.registerTileEntity(TileEntityDungeonChest.class, ModLib.tileEntityDungeonChestName);
		GameRegistry.registerTileEntity(TileEntityWandFurnace.class, ModLib.tileEntityWandFurnaceName);
		GameRegistry.registerTileEntity(TileEntityPoliceChest.class, ModLib.tileEntityPoliceChestName);
		GameRegistry.registerTileEntity(TileEntityFireChest.class, ModLib.tileEntityFireChestName);
		GameRegistry.registerTileEntity(TileEntityFinalDestinationTeleporter.class,
				ModLib.tileEntityFinalDestinationTeleporterName);
		GameRegistry.registerTileEntity(TileEntityFinalDestinationCheckpoint.class,
				ModLib.tileEntityFinalDestinationCheckpointName);
		GameRegistry.registerTileEntity(TileEntityFinalDestinationFinalCheckpoint.class,
				ModLib.tileEntityFinalDestinationFinalCheckpointName);
		GameRegistry.registerTileEntity(TileEntityFinalDestinationReturn.class,
				ModLib.tileEntityFinalDestinationReturnName);
		GameRegistry.registerTileEntity(TileEntityStructureController.class, ModLib.blockStructureControllerName);
		GameRegistry.registerTileEntity(TileEntityPoliceTeleporter.class, ModLib.blockPoliceTeleporterName);
		GameRegistry.registerTileEntity(TileEntityAirlockController.class, ModLib.blockAirlockControllerName);
	}

	public void registerEntities() {
		modEntities = 0;

		// mobs
		registerEntity(EntityCastleSoldier.class, ModLib.entityCastleSoldierName, false);
		registerEntity(EntityCastleArcher.class, ModLib.entityCastleArcherName, false);
		registerEntity(EntityCastleSoldierKing.class, ModLib.entityCastleSoldierKingName, false);
		registerEntity(EntityPirate.class, ModLib.entityPirateName, false);
		registerEntity(EntityPirateCaptain.class, ModLib.entityPirateCaptainName, false);
		registerEntity(EntityOrc.class, ModLib.entityOrcName, false);
		registerEntity(EntityDwarf.class, ModLib.entityDwarfName, false);
		registerEntity(EntityElf.class, ModLib.entityElfName, false);
		registerEntity(EntityNetherOrc.class, ModLib.entityNetherOrcName, false);
		registerEntity(EntityBlazeBoss.class, ModLib.entityBlazeBossName, false);
		registerEntity(EntityFlyingStinger.class, ModLib.entityFlyingStingerName, false);
		registerEntity(EntityHyperScorpion.class, ModLib.entityHyperScorpionName, false);
		registerEntity(EntityPolice.class, ModLib.entityPoliceName, false);
		registerEntity(EntityPoliceBoss.class, ModLib.entityPoliceBossName, false);
		registerEntity(EntityRobotSpider.class, ModLib.entityRobotSpiderName, false);
		registerEntity(EntityDoomGuardian.class, ModLib.entityDoomGuardianName, false);
		registerEntity(EntityObsidianWarrior.class, ModLib.entityObsidianWarriorName, false);
		registerEntity(EntityFinalBoss.class, ModLib.entityFinalBossName, false);
		registerEntity(EntityFinalBossCore.class, ModLib.entityFinalBossCoreName, false);
		registerEntity(EntityHyperBlaze.class, ModLib.entityHyperBlazeName, false);
		registerEntity(EntityElfBoss.class, ModLib.entityElfBossName, false);
		registerEntity(EntityDwarfBoss.class, ModLib.entityDwarfBossName, false);
		registerEntity(EntityOrcBoss.class, ModLib.entityOrcBossName, false);
		registerEntity(EntityWizard.class, ModLib.entityWizardName, false);
		registerEntity(EntityWizardBoss.class, ModLib.entityWizardBossName, false);
		registerEntity(EntityVampire.class, ModLib.entityVampireName, false);
		registerEntity(EntityFurnaceMonster.class, ModLib.entityFurnaceMonsterName, false);
		registerEntity(EntityVampireBoss.class, ModLib.entityVampireBossName, false);
		registerEntity(EntityPhantasm.class, ModLib.entityPhantasmName, false);
		registerEntity(EntityBanishedSoul.class, ModLib.entityBanishedSoulName, false);

		// projectiles
		registerEntity(EntitySpellFireball.class, ModLib.entitySpellFireballName, true);
		registerEntity(EntityThrownDagger.class, ModLib.entityThrownDaggerName, true);
		registerEntity(EntityGenericProjectile.class, ModLib.entityPoliceLaserName, true);
		registerEntity(EntityExplosiveArrow.class, ModLib.entityExplosiveArrowName, true);
		registerEntity(EntityDoomBullet.class, ModLib.entityDoomBulletName, true);
		registerEntity(EntityElectricBall.class, ModLib.entityElectricBallName, true);
		registerEntity(EntityPoliceMissile.class, ModLib.entityPoliceMissileName, true);

		registerEntity(EntityPoliceSpawner.class, ModLib.entityPoliceSpawnerName, false);
		registerEntity(EntityFlamePillar.class, ModLib.entityFlamePillarName, false);
		registerEntity(EntityFlamePillarPurple.class, ModLib.entityFlamePillarName + "Purple", false);

		registerEntity(EntityOneshotAnimation.class, ModLib.entityOneshotAnimationName, false);

		// natural spawns
		for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++) {
			if (BiomeGenBase.getBiomeGenArray()[i] != null) {
				EntityRegistry.addSpawn(EntityFlyingStinger.class, 50, 1, 6, EnumCreatureType.MONSTER,
						BiomeGenBase.getBiomeGenArray()[i]);
			}
		}
	}

	private void registerEntity(Class<? extends Entity> entityClass, String name, boolean velocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, name, modEntities, ArrowQuest.instance, 80, 3, velocityUpdates);
		modEntities += 1;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == ModLib.guiWandID) {
			ItemStack itemstack = player.inventory.getStackInSlot(player.inventory.currentItem);
			if (itemstack != null) {
				return new ContainerWand(player.inventory, new InventoryWand(player, itemstack));
			}
		} else if (ID == ModLib.guiBigCraftingTableID) {
			return new ContainerBigCraftingTable(player.inventory);
		} else if (ID == ModLib.guiWandFurnaceID) {
			TileEntityWandFurnace tile = (TileEntityWandFurnace) world.getTileEntity(new BlockPos(x, y, z));
			return new ContainerWandFurnace(player.inventory, tile);
		} else if (ID == ModLib.guiMagicBackpackID) {
			return new ContainerMagicBackpack(player.inventory, new InventoryMagicBackpack(player));
		} else if (ID == ModLib.guiFinalDestinationTeleporterID) {
			TileEntityFinalDestinationTeleporter tile = (TileEntityFinalDestinationTeleporter) world
					.getTileEntity(new BlockPos(x, y, z));
			return new ContainerFinalDestinationTeleporter(player.inventory, tile);
		} else if (ID == ModLib.guiFinalDestinationCheckpointID) {
			TileEntityFinalDestinationCheckpoint tile = (TileEntityFinalDestinationCheckpoint) world
					.getTileEntity(new BlockPos(x, y, z));
			return new ContainerFinalDestinationCheckpoint(player.inventory, tile);
		} else if (ID == ModLib.guiFinalDestinationReturnID) {
			TileEntityFinalDestinationReturn tile = (TileEntityFinalDestinationReturn) world
					.getTileEntity(new BlockPos(x, y, z));
			return new ContainerFinalDestinationReturn(player.inventory, tile);
		} else if (ID == ModLib.guiPoliceTeleporterID) {
			TileEntityPoliceTeleporter tile = (TileEntityPoliceTeleporter) world.getTileEntity(new BlockPos(x, y, z));
			return new ContainerPoliceTeleporter(player.inventory, tile);
		} else if (ID == ModLib.guiAirlockControllerID) {
			TileEntityAirlockController tile = (TileEntityAirlockController) world.getTileEntity(new BlockPos(x, y, z));
			return new ContainerAirlockController(player.inventory, tile);
		} else if (ID == ModLib.guiFinalDestinationFinalCheckpointID) {
			TileEntityFinalDestinationFinalCheckpoint tile = (TileEntityFinalDestinationFinalCheckpoint) world.getTileEntity(new BlockPos(x, y, z));
			return new ContainerFinalDestinationFinalCheckpoint(player.inventory, tile);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == ModLib.guiLevelUpID) {
			GuiLevelUp gui = new GuiLevelUp();
			gui.setList(ArrowQuest.levelUpSkillList);
			return gui;
		} else if (ID == ModLib.guiAQLogID) {
			GuiAQLog gui = new GuiAQLog();
			return gui;
		} else if (ID == ModLib.guiWandID) {
			ItemStack itemstack = player.inventory.getStackInSlot(player.inventory.currentItem);
			if (itemstack != null) {
				return new GuiWand(player.inventory, new InventoryWand(player, itemstack));
			}
		} else if (ID == ModLib.guiBigCraftingTableID) {
			return new GuiBigCraftingTable(player.inventory);
		} else if (ID == ModLib.guiWandFurnaceID) {
			TileEntityWandFurnace tile = (TileEntityWandFurnace) world.getTileEntity(new BlockPos(x, y, z));
			return new GuiWandFurnace(player.inventory, tile);
		} else if (ID == ModLib.guiSelectRaceID) {
			return new GuiPickRace();
		} else if (ID == ModLib.guiMagicBackpackID) {
			return new GuiMagicBackpack(player.inventory, new InventoryMagicBackpack(player));
		} else if (ID == ModLib.guiFinalDestinationTeleporterID) {
			TileEntityFinalDestinationTeleporter tile = (TileEntityFinalDestinationTeleporter) world
					.getTileEntity(new BlockPos(x, y, z));
			return new GuiFinalDestinationTeleporter(player.inventory, tile);
		} else if (ID == ModLib.guiFinalDestinationCheckpointID) {
			TileEntityFinalDestinationCheckpoint tile = (TileEntityFinalDestinationCheckpoint) world
					.getTileEntity(new BlockPos(x, y, z));
			return new GuiFinalDestinationCheckpoint(player.inventory, tile);
		} else if (ID == ModLib.guiFinalDestinationReturnID) {
			TileEntityFinalDestinationReturn tile = (TileEntityFinalDestinationReturn) world
					.getTileEntity(new BlockPos(x, y, z));
			return new GuiFinalDestinationReturn(player.inventory, tile);
		} else if (ID == ModLib.guiPoliceTeleporterID) {
			TileEntityPoliceTeleporter tile = (TileEntityPoliceTeleporter) world.getTileEntity(new BlockPos(x, y, z));
			return new GuiPoliceTeleporter(player.inventory, tile);
		} else if (ID == ModLib.guiAirlockControllerID) {
			TileEntityAirlockController tile = (TileEntityAirlockController) world.getTileEntity(new BlockPos(x, y, z));
			return new GuiAirlockController(player.inventory, tile);
		} else if (ID == ModLib.guiFinalDestinationFinalCheckpointID) {
			TileEntityFinalDestinationFinalCheckpoint tile = (TileEntityFinalDestinationFinalCheckpoint) world.getTileEntity(new BlockPos(x, y, z));
			return new GuiFinalDestinationFinalCheckpoint(player.inventory, tile);
		}

		return null;
	}

	public void registerRecipes() {
		// basic wand
		GameRegistry.addRecipe(new ItemStack(ModItems.itemWand), "C", "S", "G", 'C', ModItems.itemMagicCrystal, 'S',
				Items.stick, 'G', Items.gold_ingot);

		// tier 2 crafting table
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockBigCraftingTable), "CC", "CC", 'C', Blocks.crafting_table);

		// blank scroll
		GameRegistry.addRecipe(new ItemStack(ModItems.itemScroll), "P", "P", "P", 'P', Items.paper);

		// scroll cloning - WAY OVERPOWERED LOL
		// CraftingManager.getInstance().getRecipeList().add(new RecipeScrollCloning());

		// wand furnace
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockWandFurnace), "GCG", "GFG", "GGG", 'G', Items.gold_ingot,
				'C', ModItems.itemMagicCrystal, 'F', Blocks.furnace);

		// full magic crystal
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.itemMagicCrystal, 1, 0),
				new ItemStack(ModItems.itemMagicCrystal, 1, 1), new ItemStack(ModItems.itemMagicCrystal, 1, 2),
				new ItemStack(ModItems.itemMagicCrystal, 1, 3), new ItemStack(ModItems.itemMagicCrystal, 1, 4),
				new ItemStack(ModItems.itemMagicCrystal, 1, 5));

		// basic scroll crafts
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.itemScroll, 1, 1),
				new ItemStack(ModItems.itemMagicCrystal, 1, 1), new ItemStack(ModItems.itemScroll, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.itemScroll, 1, 2),
				new ItemStack(ModItems.itemMagicCrystal, 1, 2), new ItemStack(ModItems.itemScroll, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.itemScroll, 1, 3),
				new ItemStack(ModItems.itemMagicCrystal, 1, 4), new ItemStack(ModItems.itemScroll, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.itemScroll, 1, 4),
				new ItemStack(ModItems.itemMagicCrystal, 1, 3), new ItemStack(ModItems.itemScroll, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.itemScroll, 1, 7),
				new ItemStack(ModItems.itemMagicCrystal, 1, 5), new ItemStack(ModItems.itemScroll, 1, 0));

		// daggers
		GameRegistry.addRecipe(new ItemStack(ModItems.itemDaggerWood), "M", "S", 'M', Blocks.planks, 'S', Items.stick);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemDaggerStone), "M", "S", 'M', Blocks.cobblestone, 'S',
				Items.stick);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemDaggerIron), "M", "S", 'M', Items.iron_ingot, 'S',
				Items.stick);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemDaggerGold), "M", "S", 'M', Items.gold_ingot, 'S',
				Items.stick);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemDaggerDiamond), "M", "S", 'M', Items.diamond, 'S',
				Items.stick);

		// trueforce armor
		GameRegistry.addRecipe(new ItemStack(ModItems.itemTrueforceHelmet), "MMM", "M M", 'M',
				ModItems.itemTrueforceIngot);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemTrueforceChestplate), "M M", "MMM", "MMM", 'M',
				ModItems.itemTrueforceIngot);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemTrueforceLeggings), "MMM", "M M", "M M", 'M',
				ModItems.itemTrueforceIngot);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemTrueforceBoots), "M M", "M M", 'M',
				ModItems.itemTrueforceIngot);

		// heavy-duty armor
		GameRegistry.addRecipe(new ItemStack(ModItems.itemHeavyDutyHelmet), "MMM", "M M", 'M',
				ModItems.itemHeavyDutyIngot);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemHeavyDutyChestplate), "M M", "MMM", "MMM", 'M',
				ModItems.itemHeavyDutyIngot);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemHeavyDutyLeggings), "MMM", "M M", "M M", 'M',
				ModItems.itemHeavyDutyIngot);
		GameRegistry.addRecipe(new ItemStack(ModItems.itemHeavyDutyBoots), "M M", "M M", 'M',
				ModItems.itemHeavyDutyIngot);
	}
}