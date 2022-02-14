package com.cm8check.arrowquest.item;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ModItems{
	public static Item itemWand = new ItemWand(ModLib.itemWandName, 150);
	public static Item itemStaff1 = new ItemWand(ModLib.itemStaffName+"1", 300);
	public static Item itemStaff2 = new ItemWand(ModLib.itemStaffName+"2", 450);
	public static Item itemStaff3 = new ItemWand(ModLib.itemStaffName+"3", 600);
	public static Item itemScroll = new ItemScroll();
	public static Item itemLongbow = new ItemLongbow();
	public static Item itemMagicCrystal = new ItemMagicCrystal();
	public static Item itemKingSword = new ItemKingSword();
	public static Item itemEnderSword = new ItemEnderSword();
	public static Item itemMedallion = new ItemMedallion();
	public static Item itemFireMedallion = new ItemMedallionFire();
	public static Item itemDefenseMedallion = new ItemMedallionDefense();
	public static Item itemLootMedallion = new ItemMedallionLoot();
	public static Item itemPoliceSabre = new ItemPoliceSabre();
	public static Item itemPoliceGun = new ItemPoliceGun();
	public static Item itemTrueforceIngot = new ItemCustomIngot(ModLib.itemTrueforceIngotName);
	public static Item itemHeavyDutyIngot = new ItemCustomIngot(ModLib.itemHeavyDutyIngotName);
	public static Item itemPoliceTeleporter = new ItemPoliceTeleporter();
	public static Item itemExplosiveCrossbow = new ItemExplosiveCrossbow();
	public static Item itemFinalDestinationKey = new ItemCustomIngot(ModLib.itemFinalDestinationKeyName);
	public static Item itemTheArrow = new ItemTheArrow().setMaxStackSize(1);
	public static Item itemCursedFeather = new ItemCursedFeather();
	public static Item itemKey = new ItemAQKey();
	public static Item itemDebugTool = new ItemDebugTool();
	
	public static Item itemBattleAxeWood = new ItemBattleAxe(ToolMaterial.WOOD, ModLib.itemBattleAxeName+"Wood");
	public static Item itemBattleAxeStone = new ItemBattleAxe(ToolMaterial.STONE, ModLib.itemBattleAxeName+"Stone");
	public static Item itemBattleAxeIron = new ItemBattleAxe(ToolMaterial.IRON, ModLib.itemBattleAxeName+"Iron");
	public static Item itemBattleAxeGold = new ItemBattleAxe(ToolMaterial.GOLD, ModLib.itemBattleAxeName+"Gold");
	public static Item itemBattleAxeDiamond = new ItemBattleAxe(ToolMaterial.EMERALD, ModLib.itemBattleAxeName+"Diamond");
	
	public static Item itemLongswordWood = new ItemLongsword(ToolMaterial.WOOD, ModLib.itemLongswordName+"Wood");
	public static Item itemLongswordStone = new ItemLongsword(ToolMaterial.STONE, ModLib.itemLongswordName+"Stone");
	public static Item itemLongswordIron = new ItemLongsword(ToolMaterial.IRON, ModLib.itemLongswordName+"Iron");
	public static Item itemLongswordGold = new ItemLongsword(ToolMaterial.GOLD, ModLib.itemLongswordName+"Gold");
	public static Item itemLongswordDiamond = new ItemLongsword(ToolMaterial.EMERALD, ModLib.itemLongswordName+"Diamond");
	
	public static Item itemHammerWood = new ItemHammer(ToolMaterial.WOOD, ModLib.itemHammerName+"Wood");
	public static Item itemHammerStone = new ItemHammer(ToolMaterial.STONE, ModLib.itemHammerName+"Stone");
	public static Item itemHammerIron = new ItemHammer(ToolMaterial.IRON, ModLib.itemHammerName+"Iron");
	public static Item itemHammerGold = new ItemHammer(ToolMaterial.GOLD, ModLib.itemHammerName+"Gold");
	public static Item itemHammerDiamond = new ItemHammer(ToolMaterial.EMERALD, ModLib.itemHammerName+"Diamond");
	
	public static Item itemDaggerWood = new ItemDagger(ToolMaterial.WOOD, ModLib.itemDaggerName+"Wood");
	public static Item itemDaggerStone = new ItemDagger(ToolMaterial.STONE, ModLib.itemDaggerName+"Stone");
	public static Item itemDaggerIron = new ItemDagger(ToolMaterial.IRON, ModLib.itemDaggerName+"Iron");
	public static Item itemDaggerGold = new ItemDagger(ToolMaterial.GOLD, ModLib.itemDaggerName+"Gold");
	public static Item itemDaggerDiamond = new ItemDagger(ToolMaterial.EMERALD, ModLib.itemDaggerName+"Diamond");
	
	public static Item itemPoliceHelmet = new ItemCustomArmor(ItemCustomArmor.policeArmor, ModLib.itemPoliceArmorPrefix+"Helmet", 1, 0);
	public static Item itemPoliceChestplate = new ItemCustomArmor(ItemCustomArmor.policeArmor, ModLib.itemPoliceArmorPrefix+"Chestplate", 1, 1);
	public static Item itemPoliceLeggings = new ItemCustomArmor(ItemCustomArmor.policeArmor, ModLib.itemPoliceArmorPrefix+"Leggings", 2, 2);
	public static Item itemPoliceBoots = new ItemCustomArmor(ItemCustomArmor.policeArmor, ModLib.itemPoliceArmorPrefix+"Boots", 1, 3);
	
	public static Item itemTrueforceHelmet = new ItemCustomArmor(ItemCustomArmor.trueforceArmor, ModLib.itemTrueforceArmorPrefix+"Helmet", 1, 0);
	public static Item itemTrueforceChestplate = new ItemCustomArmor(ItemCustomArmor.trueforceArmor, ModLib.itemTrueforceArmorPrefix+"Chestplate", 1, 1);
	public static Item itemTrueforceLeggings = new ItemCustomArmor(ItemCustomArmor.trueforceArmor, ModLib.itemTrueforceArmorPrefix+"Leggings", 2, 2);
	public static Item itemTrueforceBoots = new ItemCustomArmor(ItemCustomArmor.trueforceArmor, ModLib.itemTrueforceArmorPrefix+"Boots", 1, 3);
	
	public static Item itemHeavyDutyHelmet = new ItemCustomArmor(ItemCustomArmor.heavyDutyArmor, ModLib.itemHeavyDutyArmorPrefix+"Helmet", 1, 0);
	public static Item itemHeavyDutyChestplate = new ItemCustomArmor(ItemCustomArmor.heavyDutyArmor, ModLib.itemHeavyDutyArmorPrefix+"Chestplate", 1, 1);
	public static Item itemHeavyDutyLeggings = new ItemCustomArmor(ItemCustomArmor.heavyDutyArmor, ModLib.itemHeavyDutyArmorPrefix+"Leggings", 2, 2);
	public static Item itemHeavyDutyBoots = new ItemCustomArmor(ItemCustomArmor.heavyDutyArmor, ModLib.itemHeavyDutyArmorPrefix+"Boots", 1, 3);
	
	public static Item itemEnergyHelmet = new ItemCustomArmor(ItemCustomArmor.energyArmor, ModLib.itemEnergyArmorPrefix+"Helmet", 1, 0);
	public static Item itemEnergyChestplate = new ItemCustomArmor(ItemCustomArmor.energyArmor, ModLib.itemEnergyArmorPrefix+"Chestplate", 1, 1);
	public static Item itemEnergyLeggings = new ItemCustomArmor(ItemCustomArmor.energyArmor, ModLib.itemEnergyArmorPrefix+"Leggings", 2, 2);
	public static Item itemEnergyBoots = new ItemCustomArmor(ItemCustomArmor.energyArmor, ModLib.itemEnergyArmorPrefix+"Boots", 1, 3);
	
	public static Item itemObsidianHelmet = new ItemCustomArmor(ItemCustomArmor.obsidianArmor, ModLib.itemObsidianArmorPrefix+"Helmet", 1, 0).setCreativeTab(null);
	public static Item itemObsidianChestplate = new ItemCustomArmor(ItemCustomArmor.obsidianArmor, ModLib.itemObsidianArmorPrefix+"Chestplate", 1, 1).setCreativeTab(null);
	public static Item itemObsidianLeggings = new ItemCustomArmor(ItemCustomArmor.obsidianArmor, ModLib.itemObsidianArmorPrefix+"Leggings", 2, 2).setCreativeTab(null);
	public static Item itemObsidianBoots = new ItemCustomArmor(ItemCustomArmor.obsidianArmor, ModLib.itemObsidianArmorPrefix+"Boots", 1, 3).setCreativeTab(null);
	
	public static Item itemBroadAxe = new ItemSpecialWeapon(ModLib.itemBroadAxeName, 7.0F, false);
	public static Item itemDoubleSword = new ItemSpecialWeapon(ModLib.itemDoubleSwordName, 8.0F, true);
	public static Item itemExecutionerAxe = new ItemSpecialWeapon(ModLib.itemExecutionerAxeName, 12.0F, false);
	public static Item itemFalchion = new ItemSpecialWeapon(ModLib.itemFalchionName, 6.0F, false);
	public static Item itemClub = new ItemSpecialWeapon(ModLib.itemClubName, 4.0F, false);
	public static Item itemSpikedClub = new ItemSpecialWeapon(ModLib.itemSpikedClubName, 7.0F, false);
	public static Item itemGlaive = new ItemSpecialWeapon(ModLib.itemGlaiveName, 6.0F, false);
	public static Item itemGreatsword = new ItemSpecialWeapon(ModLib.itemGreatswordName, 9.0F, true);
	public static Item itemFidelitySword = new ItemSpecialWeapon(ModLib.itemFidelitySwordName, 8.0F, true);
	public static Item itemSabre = new ItemSpecialWeapon(ModLib.itemSabreName, 7.0F, true);
	public static Item itemScimitar = new ItemSpecialWeapon(ModLib.itemScimitarName, 8.0F, true);
	
	public static Item itemSingingSword = new ItemSingingSword();
	public static Item itemSceptreOfAsmodeus = new ItemSceptreOfAsmodeus();
	public static Item itemSceptreOfTorment = new ItemSceptreOfTorment();
	public static Item itemSoulStealer = new ItemSoulStealer();
	public static Item itemVampiresTooth = new ItemVampiresTooth();
	public static Item itemMysteryMace = new ItemMysteryMace();
	public static Item itemThiefsDagger = new ItemThiefsDagger();
	public static Item itemEvilBlade = new ItemEvilBlade(0);
	public static Item itemAxeOfCorruption = new ItemEvilBlade(1);
	public static Item itemXPSword = new ItemXPSword();
	public static Item itemBanisher = new ItemBanisher();
	
	public static Item itemDemonBlade = new ItemDemonWeapon(ModLib.itemDemonBladeName, 13.0F, true);
	public static Item itemDemonTrident = new ItemDemonWeapon(ModLib.itemDemonTridentName, 10.0F, false);
	
	public static void init(){
		GameRegistry.registerItem(itemWand, ModLib.itemWandName);
		GameRegistry.registerItem(itemStaff1, ModLib.itemStaffName+"1");
		GameRegistry.registerItem(itemStaff2, ModLib.itemStaffName+"2");
		GameRegistry.registerItem(itemStaff3, ModLib.itemStaffName+"3");
		GameRegistry.registerItem(itemScroll, ModLib.itemScrollFireName);
		GameRegistry.registerItem(itemLongbow, ModLib.itemLongbowName);
		GameRegistry.registerItem(itemMagicCrystal, ModLib.itemMagicCrystalName);
		GameRegistry.registerItem(itemKingSword, ModLib.itemKingSwordName);
		GameRegistry.registerItem(itemEnderSword, ModLib.itemEnderSwordName);
		GameRegistry.registerItem(itemMedallion, ModLib.itemMedallionName);
		GameRegistry.registerItem(itemFireMedallion, ModLib.itemFireMedallionName);
		GameRegistry.registerItem(itemDefenseMedallion, ModLib.itemDefenseMedallionName);
		GameRegistry.registerItem(itemLootMedallion, ModLib.itemLootMedallionName);
		GameRegistry.registerItem(itemPoliceSabre, ModLib.itemPoliceSabreName);
		GameRegistry.registerItem(itemPoliceGun, ModLib.itemPoliceGunName);
		GameRegistry.registerItem(itemTrueforceIngot, ModLib.itemTrueforceIngotName);
		GameRegistry.registerItem(itemHeavyDutyIngot, ModLib.itemHeavyDutyIngotName);
		GameRegistry.registerItem(itemPoliceTeleporter, ModLib.itemPoliceTeleporterName);
		GameRegistry.registerItem(itemExplosiveCrossbow, ModLib.itemExplosiveCrossbowName);
		GameRegistry.registerItem(itemFinalDestinationKey, ModLib.itemFinalDestinationKeyName);
		GameRegistry.registerItem(itemTheArrow, ModLib.itemTheArrowName);
		GameRegistry.registerItem(itemCursedFeather, ModLib.itemCursedFeatherName);
		GameRegistry.registerItem(itemKey, ModLib.itemKeyName);
		GameRegistry.registerItem(itemDebugTool, ModLib.itemDebugToolName);
		
		GameRegistry.registerItem(itemBattleAxeWood, ModLib.itemBattleAxeName+"Wood");
		GameRegistry.registerItem(itemBattleAxeStone, ModLib.itemBattleAxeName+"Stone");
		GameRegistry.registerItem(itemBattleAxeIron, ModLib.itemBattleAxeName+"Iron");
		GameRegistry.registerItem(itemBattleAxeGold, ModLib.itemBattleAxeName+"Gold");
		GameRegistry.registerItem(itemBattleAxeDiamond, ModLib.itemBattleAxeName+"Diamond");
		
		GameRegistry.registerItem(itemLongswordWood, ModLib.itemLongswordName+"Wood");
		GameRegistry.registerItem(itemLongswordStone, ModLib.itemLongswordName+"Stone");
		GameRegistry.registerItem(itemLongswordIron, ModLib.itemLongswordName+"Iron");
		GameRegistry.registerItem(itemLongswordGold, ModLib.itemLongswordName+"Gold");
		GameRegistry.registerItem(itemLongswordDiamond, ModLib.itemLongswordName+"Diamond");
		
		GameRegistry.registerItem(itemHammerWood, ModLib.itemHammerName+"Wood");
		GameRegistry.registerItem(itemHammerStone, ModLib.itemHammerName+"Stone");
		GameRegistry.registerItem(itemHammerIron, ModLib.itemHammerName+"Iron");
		GameRegistry.registerItem(itemHammerGold, ModLib.itemHammerName+"Gold");
		GameRegistry.registerItem(itemHammerDiamond, ModLib.itemHammerName+"Diamond");
		
		GameRegistry.registerItem(itemDaggerWood, ModLib.itemDaggerName+"Wood");
		GameRegistry.registerItem(itemDaggerStone, ModLib.itemDaggerName+"Stone");
		GameRegistry.registerItem(itemDaggerIron, ModLib.itemDaggerName+"Iron");
		GameRegistry.registerItem(itemDaggerGold, ModLib.itemDaggerName+"Gold");
		GameRegistry.registerItem(itemDaggerDiamond, ModLib.itemDaggerName+"Diamond");
		
		GameRegistry.registerItem(itemPoliceHelmet, ModLib.itemPoliceArmorPrefix+"Helmet");
		GameRegistry.registerItem(itemPoliceChestplate, ModLib.itemPoliceArmorPrefix+"Chestplate");
		GameRegistry.registerItem(itemPoliceLeggings, ModLib.itemPoliceArmorPrefix+"Leggings");
		GameRegistry.registerItem(itemPoliceBoots, ModLib.itemPoliceArmorPrefix+"Boots");
		
		GameRegistry.registerItem(itemTrueforceHelmet, ModLib.itemTrueforceArmorPrefix+"Helmet");
		GameRegistry.registerItem(itemTrueforceChestplate, ModLib.itemTrueforceArmorPrefix+"Chestplate");
		GameRegistry.registerItem(itemTrueforceLeggings, ModLib.itemTrueforceArmorPrefix+"Leggings");
		GameRegistry.registerItem(itemTrueforceBoots, ModLib.itemTrueforceArmorPrefix+"Boots");
		
		GameRegistry.registerItem(itemHeavyDutyHelmet, ModLib.itemHeavyDutyArmorPrefix+"Helmet");
		GameRegistry.registerItem(itemHeavyDutyChestplate, ModLib.itemHeavyDutyArmorPrefix+"Chestplate");
		GameRegistry.registerItem(itemHeavyDutyLeggings, ModLib.itemHeavyDutyArmorPrefix+"Leggings");
		GameRegistry.registerItem(itemHeavyDutyBoots, ModLib.itemHeavyDutyArmorPrefix+"Boots");
		
		GameRegistry.registerItem(itemEnergyHelmet, ModLib.itemEnergyArmorPrefix+"Helmet");
		GameRegistry.registerItem(itemEnergyChestplate, ModLib.itemEnergyArmorPrefix+"Chestplate");
		GameRegistry.registerItem(itemEnergyLeggings, ModLib.itemEnergyArmorPrefix+"Leggings");
		GameRegistry.registerItem(itemEnergyBoots, ModLib.itemEnergyArmorPrefix+"Boots");
		
		GameRegistry.registerItem(itemObsidianHelmet, ModLib.itemObsidianArmorPrefix+"Helmet");
		GameRegistry.registerItem(itemObsidianChestplate, ModLib.itemObsidianArmorPrefix+"Chestplate");
		GameRegistry.registerItem(itemObsidianLeggings, ModLib.itemObsidianArmorPrefix+"Leggings");
		GameRegistry.registerItem(itemObsidianBoots, ModLib.itemObsidianArmorPrefix+"Boots");
		
		GameRegistry.registerItem(itemBroadAxe, ModLib.itemBroadAxeName);
		GameRegistry.registerItem(itemDoubleSword, ModLib.itemDoubleSwordName);
		GameRegistry.registerItem(itemExecutionerAxe, ModLib.itemExecutionerAxeName);
		GameRegistry.registerItem(itemFalchion, ModLib.itemFalchionName);
		GameRegistry.registerItem(itemClub, ModLib.itemClubName);
		GameRegistry.registerItem(itemSpikedClub, ModLib.itemSpikedClubName);
		GameRegistry.registerItem(itemGlaive, ModLib.itemGlaiveName);
		GameRegistry.registerItem(itemGreatsword, ModLib.itemGreatswordName);
		GameRegistry.registerItem(itemFidelitySword, ModLib.itemFidelitySwordName);
		GameRegistry.registerItem(itemSabre, ModLib.itemSabreName);
		GameRegistry.registerItem(itemScimitar, ModLib.itemScimitarName);
		
		GameRegistry.registerItem(itemSingingSword, ModLib.itemSingingSwordName);
		GameRegistry.registerItem(itemSceptreOfAsmodeus, ModLib.itemSceptreOfAsmodeusName);
		GameRegistry.registerItem(itemSceptreOfTorment, ModLib.itemSceptreOfTormentName);
		GameRegistry.registerItem(itemSoulStealer, ModLib.itemSoulStealerName);
		GameRegistry.registerItem(itemVampiresTooth, ModLib.itemVampiresToothName);
		GameRegistry.registerItem(itemMysteryMace, ModLib.itemMysteryMaceName);
		GameRegistry.registerItem(itemThiefsDagger, ModLib.itemThiefsDaggerName);
		GameRegistry.registerItem(itemEvilBlade, ModLib.itemEvilBladeName);
		GameRegistry.registerItem(itemAxeOfCorruption, ModLib.itemAxeOfCorruptionName);
		GameRegistry.registerItem(itemXPSword, ModLib.itemXPSwordName);
		GameRegistry.registerItem(itemBanisher, ModLib.itemBanisherName);
		
		GameRegistry.registerItem(itemDemonBlade, ModLib.itemDemonBladeName);
		GameRegistry.registerItem(itemDemonTrident, ModLib.itemDemonTridentName);
		
		ItemCustomArmor.trueforceArmor.customCraftingMaterial = itemTrueforceIngot;
		ItemCustomArmor.heavyDutyArmor.customCraftingMaterial = itemHeavyDutyIngot;
	}
}