package com.cm8check.arrowquest.proxy;

import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.client.gui.GuiHudOverlay;
import com.cm8check.arrowquest.client.renderer.entity.RenderBlazeBoss;
import com.cm8check.arrowquest.client.renderer.entity.RenderDoomBullet;
import com.cm8check.arrowquest.client.renderer.entity.RenderDoomGuardian;
import com.cm8check.arrowquest.client.renderer.entity.RenderElectricOrb;
import com.cm8check.arrowquest.client.renderer.entity.RenderFinalBoss;
import com.cm8check.arrowquest.client.renderer.entity.RenderFinalBossCore;
import com.cm8check.arrowquest.client.renderer.entity.RenderFireChest;
import com.cm8check.arrowquest.client.renderer.entity.RenderFlamePillar;
import com.cm8check.arrowquest.client.renderer.entity.RenderFlyingStinger;
import com.cm8check.arrowquest.client.renderer.entity.RenderHuman;
import com.cm8check.arrowquest.client.renderer.entity.RenderHyperBlaze;
import com.cm8check.arrowquest.client.renderer.entity.RenderOneshotAnimation;
import com.cm8check.arrowquest.client.renderer.entity.RenderPoliceChest;
import com.cm8check.arrowquest.client.renderer.entity.RenderGenericProjectile;
import com.cm8check.arrowquest.client.renderer.entity.RenderPoliceMissile;
import com.cm8check.arrowquest.client.renderer.entity.RenderRobotSpider;
import com.cm8check.arrowquest.client.renderer.entity.RenderThrownDagger;
import com.cm8check.arrowquest.client.renderer.entity.RenderWizard;
import com.cm8check.arrowquest.client.renderer.entity.RenderWizardBoss;
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
import com.cm8check.arrowquest.entity.EntityGenericProjectile;
import com.cm8check.arrowquest.entity.EntityPoliceMissile;
import com.cm8check.arrowquest.entity.EntityPoliceSpawner;
import com.cm8check.arrowquest.entity.EntityRobotSpider;
import com.cm8check.arrowquest.entity.EntitySpellFireball;
import com.cm8check.arrowquest.entity.EntityThrownDagger;
import com.cm8check.arrowquest.entity.EntityVampire;
import com.cm8check.arrowquest.entity.EntityVampireBoss;
import com.cm8check.arrowquest.entity.EntityWizard;
import com.cm8check.arrowquest.entity.EntityWizardBoss;
import com.cm8check.arrowquest.item.ItemAQKey;
import com.cm8check.arrowquest.item.ItemScroll;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityFireChest;
import com.cm8check.arrowquest.tileentity.TileEntityPoliceChest;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy{
	private static ItemModelMesher modelMesher;
	
	public static void init(){
		MinecraftForge.EVENT_BUS.register(new GuiHudOverlay(Minecraft.getMinecraft()));
		
		registerBlockAndItemTextures();
		registerRenderHandlers();
	}
	
	private static void registerBlockAndItemTextures(){
		modelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		//blocks
		setBlockTexture(ModBlocks.blockSingleSpawner, ModLib.blockSingleSpawnerName);
		setBlockTexture(ModBlocks.blockDungeonChest, ModLib.blockDungeonChestName);
		setBlockTexture(ModBlocks.blockBigCraftingTable, ModLib.blockBigCraftingTableName);
		setBlockTexture(ModBlocks.blockOreFire, ModLib.blockOreFireName);
		setBlockTexture(ModBlocks.blockOreWater, ModLib.blockOreWaterName);
		setBlockTexture(ModBlocks.blockOreAir, ModLib.blockOreAirName);
		setBlockTexture(ModBlocks.blockOreEarth, ModLib.blockOreEarthName);
		setBlockTexture(ModBlocks.blockOreLife, ModLib.blockOreLifeName);
		setBlockTexture(ModBlocks.blockWandFurnace, ModLib.blockWandFurnaceName);
		setBlockTexture(ModBlocks.blockPoliceChest, ModLib.blockPoliceChestName);
		setBlockTexture(ModBlocks.blockFireChest, ModLib.blockFireChestName);
		setBlockTexture(ModBlocks.blockTemporaryBlock, ModLib.blockTemporaryBlockName);
		setBlockTexture(ModBlocks.blockFinalDestinationTeleporter, ModLib.blockFinalDestinationTeleporterName);
		setBlockTexture(ModBlocks.blockFinalDestinationFinalCheckpoint, ModLib.blockFinalDestinationFinalCheckpointName);
		setBlockTexture(ModBlocks.blockFinalDestinationCheckpoint, ModLib.blockFinalDestinationCheckpointName);
		setBlockTexture(ModBlocks.blockFinalDestinationReturn, ModLib.blockFinalDestinationReturnName);
		setBlockTexture(ModBlocks.blockStructureController, ModLib.blockStructureControllerName);
		setBlockTexture(ModBlocks.blockPoliceTeleporter, ModLib.blockPoliceTeleporterName);
		setBlockTexture(ModBlocks.blockAirlockController, ModLib.blockAirlockControllerName);
		
		//items
		setItemTexture(ModItems.itemWand, ModLib.itemWandName);
		setItemTexture(ModItems.itemStaff1, ModLib.itemStaffName+"1");
		setItemTexture(ModItems.itemStaff2, ModLib.itemStaffName+"2");
		setItemTexture(ModItems.itemStaff3, ModLib.itemStaffName+"3");
		setItemTexture(ModItems.itemKingSword, ModLib.itemKingSwordName);
		setItemTexture(ModItems.itemEnderSword, ModLib.itemEnderSwordName);
		setItemTexture(ModItems.itemMedallion, ModLib.itemMedallionName);
		setItemTexture(ModItems.itemFireMedallion, ModLib.itemFireMedallionName);
		setItemTexture(ModItems.itemDefenseMedallion, ModLib.itemDefenseMedallionName);
		setItemTexture(ModItems.itemLootMedallion, ModLib.itemLootMedallionName);
		setItemTexture(ModItems.itemPoliceSabre, ModLib.itemPoliceSabreName);
		setItemTexture(ModItems.itemPoliceGun, ModLib.itemPoliceGunName);
		setItemTexture(ModItems.itemTrueforceIngot, ModLib.itemTrueforceIngotName);
		setItemTexture(ModItems.itemHeavyDutyIngot, ModLib.itemHeavyDutyIngotName);
		setItemTexture(ModItems.itemPoliceTeleporter, ModLib.itemPoliceTeleporterName);
		setItemTexture(ModItems.itemExplosiveCrossbow, ModLib.itemExplosiveCrossbowName);
		setItemTexture(ModItems.itemFinalDestinationKey, ModLib.itemFinalDestinationKeyName);
		setItemTexture(ModItems.itemTheArrow, ModLib.itemTheArrowName);
		setItemTexture(ModItems.itemCursedFeather, ModLib.itemCursedFeatherName);
		setItemTexture(ModItems.itemDebugTool, ModLib.itemDebugToolName);
		
		setItemTexture(ModItems.itemLongbow, ModLib.itemLongbowName);
		ModelBakery.addVariantName(ModItems.itemLongbow, new String[]{"arrowquest:itemLongbow", "arrowquest:itemLongbowPulling0", "arrowquest:itemLongbowPulling1", "arrowquest:itemLongbowPulling2"});
		
		setItemTexture(ModItems.itemMagicCrystal, 0, ModLib.itemMagicCrystalName);
		setItemTexture(ModItems.itemMagicCrystal, 1, ModLib.itemCrystalFireName);
		setItemTexture(ModItems.itemMagicCrystal, 2, ModLib.itemCrystalWaterName);
		setItemTexture(ModItems.itemMagicCrystal, 3, ModLib.itemCrystalAirName);
		setItemTexture(ModItems.itemMagicCrystal, 4, ModLib.itemCrystalEarthName);
		setItemTexture(ModItems.itemMagicCrystal, 5, ModLib.itemCrystalLifeName);
		ModelBakery.addVariantName(ModItems.itemMagicCrystal, new String[]{
			"arrowquest:"+ModLib.itemMagicCrystalName,
			"arrowquest:"+ModLib.itemCrystalFireName,
			"arrowquest:"+ModLib.itemCrystalWaterName,
			"arrowquest:"+ModLib.itemCrystalAirName,
			"arrowquest:"+ModLib.itemCrystalEarthName,
			"arrowquest:"+ModLib.itemCrystalLifeName
		});
		
		setItemTexture(ModItems.itemBattleAxeWood, ModLib.itemBattleAxeName+"Wood");
		setItemTexture(ModItems.itemBattleAxeStone, ModLib.itemBattleAxeName+"Stone");
		setItemTexture(ModItems.itemBattleAxeIron, ModLib.itemBattleAxeName+"Iron");
		setItemTexture(ModItems.itemBattleAxeGold, ModLib.itemBattleAxeName+"Gold");
		setItemTexture(ModItems.itemBattleAxeDiamond, ModLib.itemBattleAxeName+"Diamond");
		
		setItemTexture(ModItems.itemLongswordWood, ModLib.itemLongswordName+"Wood");
		setItemTexture(ModItems.itemLongswordStone, ModLib.itemLongswordName+"Stone");
		setItemTexture(ModItems.itemLongswordIron, ModLib.itemLongswordName+"Iron");
		setItemTexture(ModItems.itemLongswordGold, ModLib.itemLongswordName+"Gold");
		setItemTexture(ModItems.itemLongswordDiamond, ModLib.itemLongswordName+"Diamond");
		
		setItemTexture(ModItems.itemHammerWood, ModLib.itemHammerName+"Wood");
		setItemTexture(ModItems.itemHammerStone, ModLib.itemHammerName+"Stone");
		setItemTexture(ModItems.itemHammerIron, ModLib.itemHammerName+"Iron");
		setItemTexture(ModItems.itemHammerGold, ModLib.itemHammerName+"Gold");
		setItemTexture(ModItems.itemHammerDiamond, ModLib.itemHammerName+"Diamond");
		
		setItemTexture(ModItems.itemDaggerWood, ModLib.itemDaggerName+"Wood");
		setItemTexture(ModItems.itemDaggerStone, ModLib.itemDaggerName+"Stone");
		setItemTexture(ModItems.itemDaggerIron, ModLib.itemDaggerName+"Iron");
		setItemTexture(ModItems.itemDaggerGold, ModLib.itemDaggerName+"Gold");
		setItemTexture(ModItems.itemDaggerDiamond, ModLib.itemDaggerName+"Diamond");
		
		setItemTexture(ModItems.itemPoliceHelmet, ModLib.itemPoliceArmorPrefix+"Helmet");
		setItemTexture(ModItems.itemPoliceChestplate, ModLib.itemPoliceArmorPrefix+"Chestplate");
		setItemTexture(ModItems.itemPoliceLeggings, ModLib.itemPoliceArmorPrefix+"Leggings");
		setItemTexture(ModItems.itemPoliceBoots, ModLib.itemPoliceArmorPrefix+"Boots");
		
		setItemTexture(ModItems.itemTrueforceHelmet, ModLib.itemTrueforceArmorPrefix+"Helmet");
		setItemTexture(ModItems.itemTrueforceChestplate, ModLib.itemTrueforceArmorPrefix+"Chestplate");
		setItemTexture(ModItems.itemTrueforceLeggings, ModLib.itemTrueforceArmorPrefix+"Leggings");
		setItemTexture(ModItems.itemTrueforceBoots, ModLib.itemTrueforceArmorPrefix+"Boots");
		
		setItemTexture(ModItems.itemHeavyDutyHelmet, ModLib.itemHeavyDutyArmorPrefix+"Helmet");
		setItemTexture(ModItems.itemHeavyDutyChestplate, ModLib.itemHeavyDutyArmorPrefix+"Chestplate");
		setItemTexture(ModItems.itemHeavyDutyLeggings, ModLib.itemHeavyDutyArmorPrefix+"Leggings");
		setItemTexture(ModItems.itemHeavyDutyBoots, ModLib.itemHeavyDutyArmorPrefix+"Boots");
		
		setItemTexture(ModItems.itemEnergyHelmet, ModLib.itemEnergyArmorPrefix+"Helmet");
		setItemTexture(ModItems.itemEnergyChestplate, ModLib.itemEnergyArmorPrefix+"Chestplate");
		setItemTexture(ModItems.itemEnergyLeggings, ModLib.itemEnergyArmorPrefix+"Leggings");
		setItemTexture(ModItems.itemEnergyBoots, ModLib.itemEnergyArmorPrefix+"Boots");
		
		setItemTexture(ModItems.itemObsidianHelmet, ModLib.itemPoliceArmorPrefix+"Helmet");
		setItemTexture(ModItems.itemObsidianChestplate, ModLib.itemPoliceArmorPrefix+"Chestplate");
		setItemTexture(ModItems.itemObsidianLeggings, ModLib.itemPoliceArmorPrefix+"Leggings");
		setItemTexture(ModItems.itemObsidianBoots, ModLib.itemPoliceArmorPrefix+"Boots");
		
		setItemTexture(ModItems.itemBroadAxe, ModLib.itemBroadAxeName);
		setItemTexture(ModItems.itemDoubleSword, ModLib.itemDoubleSwordName);
		setItemTexture(ModItems.itemExecutionerAxe, ModLib.itemExecutionerAxeName);
		setItemTexture(ModItems.itemFalchion, ModLib.itemFalchionName);
		setItemTexture(ModItems.itemClub, ModLib.itemClubName);
		setItemTexture(ModItems.itemSpikedClub, ModLib.itemSpikedClubName);
		setItemTexture(ModItems.itemGlaive, ModLib.itemGlaiveName);
		setItemTexture(ModItems.itemGreatsword, ModLib.itemGreatswordName);
		setItemTexture(ModItems.itemFidelitySword, ModLib.itemFidelitySwordName);
		setItemTexture(ModItems.itemSabre, ModLib.itemSabreName);
		setItemTexture(ModItems.itemScimitar, ModLib.itemScimitarName);
		
		setItemTexture(ModItems.itemSingingSword, ModLib.itemSingingSwordName);
		setItemTexture(ModItems.itemSoulStealer, ModLib.itemSoulStealerName);
		setItemTexture(ModItems.itemVampiresTooth, ModLib.itemVampiresToothName);
		setItemTexture(ModItems.itemMysteryMace, ModLib.itemMysteryMaceName);
		setItemTexture(ModItems.itemThiefsDagger, ModLib.itemThiefsDaggerName);
		setItemTexture(ModItems.itemEvilBlade, ModLib.itemEvilBladeName);
		setItemTexture(ModItems.itemAxeOfCorruption, ModLib.itemAxeOfCorruptionName);
		setItemTexture(ModItems.itemXPSword, ModLib.itemXPSwordName);
		setItemTexture(ModItems.itemBanisher, ModLib.itemBanisherName);
		
		setItemTexture(ModItems.itemSceptreOfAsmodeus, 0, ModLib.itemSceptreOfAsmodeusName);
		setItemTexture(ModItems.itemSceptreOfAsmodeus, 1, ModLib.itemSceptreOfAsmodeusName + "Charged");
		ModelBakery.addVariantName(ModItems.itemSceptreOfAsmodeus, new String[] {
				"arrowquest:"+ModLib.itemSceptreOfAsmodeusName,
				"arrowquest:"+ModLib.itemSceptreOfAsmodeusName + "Charged"
		});
		
		setItemTexture(ModItems.itemSceptreOfTorment, 0, ModLib.itemSceptreOfTormentName + "Charged");
		setItemTexture(ModItems.itemSceptreOfTorment, 1, ModLib.itemSceptreOfTormentName);
		ModelBakery.addVariantName(ModItems.itemSceptreOfTorment, new String[] {
				"arrowquest:"+ModLib.itemSceptreOfTormentName + "Charged",
				"arrowquest:"+ModLib.itemSceptreOfTormentName
		});
		
		setItemTexture(ModItems.itemDemonBlade, ModLib.itemDemonBladeName);
		setItemTexture(ModItems.itemDemonTrident, ModLib.itemDemonTridentName);
		
		//scrolls
		for (int i = 0; i < ItemScroll.scrollTextureNames.length; i++){
			setItemTexture(ModItems.itemScroll, i, ItemScroll.scrollTextureNames[i].replaceFirst("arrowquest:", ""));
		}
		ModelBakery.addVariantName(ModItems.itemScroll, ItemScroll.scrollTextureNames);
		
		//keys
		for (int i = 0; i < ItemAQKey.names.length; i++){
			setItemTexture(ModItems.itemKey, i, ItemAQKey.names[i].replaceFirst("arrowquest:", ""));
		}
		ModelBakery.addVariantName(ModItems.itemKey, ItemAQKey.names);
	}
	
	private static void registerRenderHandlers(){
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		
		//mobs
		RenderingRegistry.registerEntityRenderingHandler(EntityCastleSoldier.class, new RenderHuman(RenderHuman.humanTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityCastleArcher.class, new RenderHuman(RenderHuman.humanTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityCastleSoldierKing.class, new RenderHuman(RenderHuman.castleSoldierBossTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityPirate.class, new RenderHuman(RenderHuman.pirateTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityPirateCaptain.class, new RenderHuman(RenderHuman.pirateCaptainTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityOrc.class, new RenderHuman(RenderHuman.orcTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityDwarf.class, new RenderHuman(RenderHuman.dwarfTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityElf.class, new RenderHuman(RenderHuman.elfTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityNetherOrc.class, new RenderHuman(RenderHuman.orcTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityPolice.class, new RenderHuman(RenderHuman.policeTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityPoliceBoss.class, new RenderHuman(RenderHuman.policeBossTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityObsidianWarrior.class, new RenderHuman(RenderHuman.obsidianWarriorTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityElfBoss.class, new RenderHuman(RenderHuman.elfTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityDwarfBoss.class, new RenderHuman(RenderHuman.dwarfTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityOrcBoss.class, new RenderHuman(RenderHuman.orcTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityVampire.class, new RenderHuman(RenderHuman.vampireTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityVampireBoss.class, new RenderHuman(RenderHuman.vampireTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityFurnaceMonster.class, new RenderHuman(RenderHuman.furnaceMonsterTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityPhantasm.class, new RenderHuman(RenderHuman.phantasmTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityBanishedSoul.class, new RenderHuman(RenderHuman.banishedSoulTex));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBoss.class, new RenderBlazeBoss());
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingStinger.class, new RenderFlyingStinger());
		RenderingRegistry.registerEntityRenderingHandler(EntityHyperScorpion.class, new RenderFlyingStinger());
		RenderingRegistry.registerEntityRenderingHandler(EntityRobotSpider.class, new RenderRobotSpider());
		RenderingRegistry.registerEntityRenderingHandler(EntityDoomGuardian.class, new RenderDoomGuardian());
		RenderingRegistry.registerEntityRenderingHandler(EntityFinalBoss.class, new RenderFinalBoss());
		RenderingRegistry.registerEntityRenderingHandler(EntityFinalBossCore.class, new RenderFinalBossCore());
		RenderingRegistry.registerEntityRenderingHandler(EntityHyperBlaze.class, new RenderHyperBlaze());
		RenderingRegistry.registerEntityRenderingHandler(EntityWizard.class, new RenderWizard());
		RenderingRegistry.registerEntityRenderingHandler(EntityWizardBoss.class, new RenderWizardBoss());
		
		//projectiles
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellFireball.class, new RenderFireball(renderManager, 0.1F));
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownDagger.class, new RenderThrownDagger());
		RenderingRegistry.registerEntityRenderingHandler(EntityGenericProjectile.class, new RenderGenericProjectile());
		RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveArrow.class, new RenderArrow(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDoomBullet.class, new RenderDoomBullet());
		RenderingRegistry.registerEntityRenderingHandler(EntityElectricBall.class, new RenderElectricOrb());
		RenderingRegistry.registerEntityRenderingHandler(EntityPoliceMissile.class, new RenderPoliceMissile());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityPoliceSpawner.class, new RenderGenericProjectile());
		RenderingRegistry.registerEntityRenderingHandler(EntityFlamePillar.class, new RenderFlamePillar());
		RenderingRegistry.registerEntityRenderingHandler(EntityFlamePillarPurple.class, new RenderFlamePillar());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityOneshotAnimation.class, new RenderOneshotAnimation());
		
		//tile entities
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPoliceChest.class, new RenderPoliceChest());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFireChest.class, new RenderFireChest());
	}
	
	private static void setBlockTexture(Block block, String name){
		modelMesher.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation("arrowquest:" + name, "inventory"));
	}
	
	private static void setItemTexture(Item item, String name){
		modelMesher.register(item, 0, new ModelResourceLocation("arrowquest:" + name, "inventory"));
	}
	
	private static void setItemTexture(Item item, int meta, String name){
		modelMesher.register(item, meta, new ModelResourceLocation("arrowquest:" + name, "inventory"));
	}
}