package com.cm8check.arrowquest.lib;

import java.util.UUID;

public class ModLib {
	// NBT key strings
	public static final String nbtPlayerXP = "AQPlayerXP";
	public static final String nbtPlayerLevelXP = "AQPlayerLevelXP";
	public static final String nbtPlayerLevel = "AQPlayerLevel";
	public static final String nbtPlayerATK = "AQPlayerATK";
	public static final String nbtPlayerDEF = "AQPlayerDEF";
	public static final String nbtPlayerMaxHP = "AQPlayerMaxHP";
	public static final String nbtPlayerMoveSpeed = "AQPlayerMoveSpeed";
	public static final String nbtPlayerJumpHeight = "AQPlayerJumpHeight";
	public static final String nbtPlayerMagicDMG = "AQPlayerMagicDMG";
	public static final String nbtPlayerFoodBonus = "AQPlayerFoodBonus";
	public static final String nbtPlayerSkills = "AQPlayerSkills";
	public static final String nbtPlayerLevelsToSpend = "AQPlayerLevelsToSpend";
	public static final String nbtPlayerRace = "AQPlayerRace";
	public static final String nbtMagicBackpackItems = "MagicBackpackItems";
	public static final String nbtPlayerCursedFeather = "CursedFeather";
	public static final String nbtPlayerDimMusic = "AQDimMusic";
	
	// NBT keys for adventure log
	public static final String nbtStructuresCleared = "AQStructuresCleared";
	public static final String nbtTier1StructuresCleared = "AQStructuresCleared1";
	public static final String nbtTier2StructuresCleared = "AQStructuresCleared2";
	public static final String nbtTier3StructuresCleared = "AQStructuresCleared3";
	public static final String nbtTier4StructuresCleared = "AQStructuresCleared4";
	public static final String nbtStructuresClearedList = "AQClearList";
	public static final String nbtArtifactWeaponsAcquired = "AQArtWeapons";
	public static final String nbtBossesKilled = "AQBossesKilled";
	public static final String nbtScrollsFound = "AQScrollsFound";
	public static final String nbtTPCTeleporterFound = "AQTPCTeleporterFound";
	public static final String nbtFinalDestReached = "AQFinalDestReached";
	public static final String nbtObjective = "AQObjective";
	public static final String nbtObjectiveProgress = "AQObjectiveProgress";
	
	public static final String nbtPlayerHasBeatenHumanBoss = "HumanBoss";
	public static final String nbtPlayerHasBeatenElfBoss = "ElfBoss";
	public static final String nbtPlayerHasBeatenOrcBoss = "OrcBoss";
	public static final String nbtPlayerHasBeatenDwarfBoss = "DwarfBoss";
	public static final String nbtPlayerHasBeatenWizardBoss = "WizardBoss";
	public static final String nbtPlayerHasBeatenVampireBoss = "VampireBoss";
	public static final String nbtPlayerHasBeatenBlazeBoss = "BlazeBoss";
	public static final String nbtPlayerHasBeatenPoliceBoss = "PoliceBoss";
	public static final String nbtPlayerHasBeatenFinalBoss = "FinalBoss";
	
	public static final String nbtPoliceBossDefeated = "PoliceBossDead";
	public static final String nbtFinalBossDefeated = "FinalBossDead";
	public static final String nbtActualFinalBossDefeated = "ActualFinalBossDead";

	// NBT ability key strings
	public static final String nbtPlayerAxeExtraDamage = "AQPlayerAxeExtraDamage";
	public static final String nbtPlayerPickaxeExtraDamage = "AQPlayerPickaxeExtraDamage";
	public static final String nbtPlayerShovelExtraDamage = "AQPlayerShovelExtraDamage";
	public static final String nbtPlayerBowExtraDamage = "AQPlayerBowExtraDamage";
	public static final String nbtPlayerBowExtraDamage2 = "AQPlayerBowExtraDamage2";
	public static final String nbtPlayerSwordXP = "AQPlayerSwordXP";
	public static final String nbtPlayerAxeXP = "AQPlayerAxeXP";
	public static final String nbtPlayerPickaxeXP = "AQPlayerPickaxeXP";
	public static final String nbtPlayerShovelXP = "AQPlayerShovelXP";
	public static final String nbtPlayerLessFireDamage = "AQPlayerLessFireDamage";
	public static final String nbtPlayerHasMagicBackpack = "AQPlayerHasMagicBackpack";

	public static final UUID playerSpeedUUID = UUID.fromString("7951a38c-1cf7-4b6c-ae6d-74bf2b788bf9");
	public static final UUID playerMaxHPUUID = UUID.fromString("d8c4956e-c4a0-493b-953f-082f7ba2baa8");

	// tile entity names
	public static final String tileEntitySingleSpawnerName = "tileEntitySingleSpawner";
	public static final String tileEntityDungeonChestName = "tileEntityDungeonChest";
	public static final String tileEntityWandFurnaceName = "tileEntityWandFurnace";
	public static final String tileEntityPoliceChestName = "tileEntityPoliceChest";
	public static final String tileEntityFireChestName = "tileEntityFireChest";
	public static final String tileEntityFinalDestinationTeleporterName = "tileEntityFinalDestinationTeleporter";
	public static final String tileEntityFinalDestinationFinalCheckpointName = "tileEntityFinalDestinationFinalCheckpoint";
	public static final String tileEntityFinalDestinationCheckpointName = "tileEntityFinalDestinationCheckpoint";
	public static final String tileEntityFinalDestinationReturnName = "tileEntityFinalDestinationReturn";

	// entity names
	public static final String entityCastleSoldierName = "entityCastleSoldier";
	public static final String entityCastleArcherName = "entityCastleArcher";
	public static final String entityCastleSoldierKingName = "entityCastleSoldierKing";
	public static final String entityPirateName = "entityPirate";
	public static final String entityPirateCaptainName = "entityPirateCaptain";
	public static final String entityOrcName = "entityOrc";
	public static final String entityNetherOrcName = "entityNetherOrc";
	public static final String entityDwarfName = "entityDwarf";
	public static final String entityElfName = "entityElf";
	public static final String entityBlazeBossName = "entityBlazeBoss";
	public static final String entityFlyingStingerName = "entityFlyingStinger";
	public static final String entityHyperScorpionName = "entityHyperScorpion";
	public static final String entityPoliceName = "entityPolice";
	public static final String entityPoliceBossName = "entityPoliceBoss";
	public static final String entitySpellFireballName = "entitySpellFireball";
	public static final String entityThrownDaggerName = "entityThrownDagger";
	public static final String entityPoliceLaserName = "entityPoliceLaser";
	public static final String entityExplosiveArrowName = "entityExplosiveArrow";
	public static final String entityRobotSpiderName = "entityRobotSpider";
	public static final String entityDoomGuardianName = "entityDoomGuardian";
	public static final String entityObsidianWarriorName = "entityObsidianWarrior";
	public static final String entityDoomBulletName = "entityDoomBullet";
	public static final String entityFinalBossName = "entityFinalBoss";
	public static final String entityFinalBossCoreName = "entityFinalBossCore";
	public static final String entityPoliceSpawnerName = "entityPoliceSpawner";
	public static final String entityElectricBallName = "entityElectricBall";
	public static final String entityPoliceMissileName = "entityPoliceMissile";
	public static final String entityHyperBlazeName = "entityHyperBlaze";
	public static final String entityElfBossName = "entityElfBoss";
	public static final String entityDwarfBossName = "entityDwarfBoss";
	public static final String entityOrcBossName = "entityOrcBoss";
	public static final String entityWizardName = "entityWizard";
	public static final String entityWizardBossName = "entityWizardBoss";
	public static final String entityFlamePillarName = "entityFlamePillar";
	public static final String entityOneshotAnimationName = "entityOneshotAnimation";
	public static final String entityVampireName = "entityVampire";
	public static final String entityFurnaceMonsterName = "entityFurnaceMonster";
	public static final String entityPhantasmName = "entityPhantasm";
	public static final String entityVampireBossName = "entityVampireBoss";
	public static final String entityBanishedSoulName = "entityBanishedSoul";

	// block names
	public static final String blockSingleSpawnerName = "blockSingleSpawner";
	public static final String blockDungeonChestName = "blockDungeonChest";
	public static final String blockBigCraftingTableName = "blockBigCraftingTable";
	public static final String blockWandFurnaceName = "blockWandFurnace";
	public static final String blockOreFireName = "blockOreFire";
	public static final String blockOreWaterName = "blockOreWater";
	public static final String blockOreEarthName = "blockOreEarth";
	public static final String blockOreAirName = "blockOreAir";
	public static final String blockOreLifeName = "blockOreLife";
	public static final String blockPoliceChestName = "blockPoliceChest";
	public static final String blockFireChestName = "blockFireChest";
	public static final String blockTemporaryBlockName = "blockTemporaryBlock";
	public static final String blockFinalDestinationTeleporterName = "blockFinalDestinationTeleporter";
	public static final String blockFinalDestinationFinalCheckpointName = "blockFinalDestinationFinalCheckpoint";
	public static final String blockFinalDestinationCheckpointName = "blockFinalDestinationCheckpoint";
	public static final String blockFinalDestinationReturnName = "blockFinalDestinationReturn";
	public static final String blockStructureControllerName = "blockStructureController";
	public static final String blockPoliceTeleporterName = "blockPoliceTeleporter";
	public static final String blockAirlockControllerName = "blockAirlockController";

	// item names
	public static final String itemWandName = "itemWand";
	public static final String itemStaffName = "itemStaff";
	public static final String itemScrollName = "itemScroll";
	public static final String itemScrollFireName = "itemScrollFire";
	public static final String itemScrollWaterName = "itemScrollWater";
	public static final String itemScrollAirName = "itemScrollAir";
	public static final String itemScrollEarthName = "itemScrollEarth";
	public static final String itemScrollLifeName = "itemScrollLife";
	public static final String itemBattleAxeName = "itemBattleAxe";
	public static final String itemDaggerName = "itemDagger";
	public static final String itemHammerName = "itemHammer";
	public static final String itemLongbowName = "itemLongbow";
	public static final String itemLongswordName = "itemLongsword";
	public static final String itemMagicCrystalName = "itemMagicCrystal";
	public static final String itemCrystalFireName = "itemCrystalFire";
	public static final String itemCrystalWaterName = "itemCrystalWater";
	public static final String itemCrystalEarthName = "itemCrystalEarth";
	public static final String itemCrystalAirName = "itemCrystalAir";
	public static final String itemCrystalLifeName = "itemCrystalLife";
	public static final String itemKingSwordName = "itemKingSword";
	public static final String itemEnderSwordName = "itemEnderSword";
	public static final String itemMedallionName = "itemMedallion";
	public static final String itemFireMedallionName = "itemFireMedallion";
	public static final String itemDefenseMedallionName = "itemDefenseMedallion";
	public static final String itemLootMedallionName = "itemLootMedallion";
	public static final String itemPoliceSabreName = "itemPoliceSabre";
	public static final String itemPoliceArmorPrefix = "itemPolice";
	public static final String itemTrueforceIngotName = "itemTrueforceIngot";
	public static final String itemTrueforceArmorPrefix = "itemTrueforce";
	public static final String itemHeavyDutyArmorPrefix = "itemHeavyDuty";
	public static final String itemHeavyDutyIngotName = "itemHeavyDutyIngot";
	public static final String itemEnergyArmorPrefix = "itemEnergy";
	public static final String itemPoliceGunName = "itemPoliceGun";
	public static final String itemPoliceTeleporterName = "itemPoliceTeleporter";
	public static final String itemExplosiveCrossbowName = "itemExplosiveCrossbow";
	public static final String itemFinalDestinationKeyName = "itemFinalDestinationKey";
	public static final String itemTheArrowName = "itemTheArrow";
	public static final String itemCursedFeatherName = "itemCursedFeather";
	public static final String itemObsidianArmorPrefix = "itemObsidian";
	public static final String itemKeyName = "itemAQKey";
	public static final String itemKeyRedName = "itemAQKeyRed";
	public static final String itemKeyBlueName = "itemAQKeyBlue";
	public static final String itemKeyGreenName = "itemAQKeyGreen";
	public static final String itemKeyYellowName = "itemAQKeyYellow";
	public static final String itemKeyPurpleName = "itemAQKeyPurple";
	public static final String itemDebugToolName = "itemDebugTool";

	public static final String itemBroadAxeName = "itemBroadAxe";
	public static final String itemDoubleSwordName = "itemDoubleSword";
	public static final String itemExecutionerAxeName = "itemExecutionerAxe";
	public static final String itemFalchionName = "itemFalchion";
	public static final String itemClubName = "itemClub";
	public static final String itemSpikedClubName = "itemSpikedClub";
	public static final String itemGlaiveName = "itemGlaive";
	public static final String itemGreatswordName = "itemGreatsword";
	public static final String itemFidelitySwordName = "itemFidelitySword";
	public static final String itemSabreName = "itemSabre";
	public static final String itemScimitarName = "itemScimitar";

	public static final String itemSingingSwordName = "itemSingingSword";
	public static final String itemSceptreOfAsmodeusName = "itemSceptreOfAsmodeus";
	public static final String itemSceptreOfTormentName = "itemSceptreOfTorment";
	public static final String itemSoulStealerName = "itemSoulStealer";
	public static final String itemVampiresToothName = "itemVampiresTooth";
	public static final String itemMysteryMaceName = "itemMysteryMace";
	public static final String itemThiefsDaggerName = "itemThiefsDagger";
	public static final String itemEvilBladeName = "itemEvilBlade";
	public static final String itemAxeOfCorruptionName = "itemAxeOfCorruption";
	public static final String itemXPSwordName = "itemXPSword";
	public static final String itemBanisherName = "itemBanisher";

	public static final String itemDemonBladeName = "itemDemonBlade";
	public static final String itemDemonTridentName = "itemDemonTrident";

	// gui IDs
	public static final int guiLevelUpID = 0;
	public static final int guiWandID = 1;
	public static final int guiBigCraftingTableID = 2;
	public static final int guiWandFurnaceID = 3;
	public static final int guiSelectRaceID = 4;
	public static final int guiMagicBackpackID = 5;
	public static final int guiFinalDestinationTeleporterID = 6;
	public static final int guiFinalDestinationCheckpointID = 7;
	public static final int guiFinalDestinationReturnID = 8;
	public static final int guiAQLogID = 9;
	public static final int guiPoliceTeleporterID = 10;
	public static final int guiAirlockControllerID = 11;
	public static final int guiFinalDestinationFinalCheckpointID = 12;

	// dimension IDs
	public static final int dimPoliceBaseID = 48;
	public static final int dimFinalBossID = 49;
}