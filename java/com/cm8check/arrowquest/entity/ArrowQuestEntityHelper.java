package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketGuiPopup;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;
import com.cm8check.arrowquest.world.ArrowQuestWorldData;
import com.cm8check.arrowquest.world.gen.WorldGenSchematics;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ArrowQuestEntityHelper{
	private static final Class[] bossEntities = {
		EntityBlazeBoss.class,
		EntityCastleSoldierKing.class,
		EntityDoomGuardian.class,
		EntityDwarfBoss.class,
		EntityElfBoss.class,
		EntityFinalBoss.class,
		EntityFinalBossCore.class,
		EntityOrcBoss.class,
		EntityPoliceBoss.class,
		EntityWizardBoss.class,
		EntityVampireBoss.class,
		EntityPirateCaptain.class,
		EntityWizard.class,
		EntityPlayer.class
	};
	
	private static final Item[] tier1specialLoot = {
		ModItems.itemMagicCrystal, ModItems.itemScroll, ModItems.itemWand, ModItems.itemLongbow,
		Item.getItemFromBlock(Blocks.iron_block), Items.diamond, Items.emerald, Items.ender_pearl, Items.gold_ingot,
		ModItems.itemLongswordIron, ModItems.itemHammerIron, ModItems.itemBattleAxeIron, ModItems.itemSabre, ModItems.itemBroadAxe,
		ModItems.itemSpikedClub, ModItems.itemThiefsDagger, ModItems.itemVampiresTooth, ModItems.itemGlaive
	};
	private static final Item[] tier2specialLoot = {
		ModItems.itemMagicCrystal, ModItems.itemStaff1, ModItems.itemTrueforceIngot, Item.getItemFromBlock(Blocks.diamond_block),
		Item.getItemFromBlock(Blocks.emerald_block), Items.ender_eye, ModItems.itemLongswordDiamond, ModItems.itemHammerDiamond, ModItems.itemBattleAxeDiamond,
		ModItems.itemHeavyDutyBoots, ModItems.itemHeavyDutyChestplate, ModItems.itemHeavyDutyHelmet, ModItems.itemHeavyDutyLeggings,
		ModItems.itemDoubleSword, ModItems.itemGreatsword, ModItems.itemScimitar, ModItems.itemExecutionerAxe, ModItems.itemXPSword,
		ModItems.itemMysteryMace
	};
	private static final Item[] tier3specialLoot = {
		ModItems.itemMedallion, ModItems.itemFireMedallion, ModItems.itemTrueforceBoots, ModItems.itemTrueforceChestplate, ModItems.itemTrueforceHelmet,
		ModItems.itemTrueforceLeggings, ModItems.itemDefenseMedallion, ModItems.itemStaff2, ModItems.itemEvilBlade
	};
	
	public static ItemStack getEntityDrop(int level){
		Random rand = ArrowQuest.RAND;
		
		if (level >= 2){
			if (rand.nextInt(5) == 0){
				int type = rand.nextInt(tier3specialLoot.length);
				return new ItemStack(tier3specialLoot[type]);
			}
		}
		
		if (level >= 1){
			if (rand.nextInt(15) == 0){
				int type = rand.nextInt(tier2specialLoot.length);
				return new ItemStack(tier2specialLoot[type]);
			}
		}
		
		if (level >= 0){
			if (rand.nextInt(10) == 0){
				int type = rand.nextInt(tier1specialLoot.length);
				if (type == 0){
					return new ItemStack(ModItems.itemMagicCrystal, 1, 1+rand.nextInt(5));
				}else{
					return new ItemStack(tier1specialLoot[type]);
				}
			}
		}
		
		return null;
	}
	
	public static void distributeBossXP(World world, double posX, double posY, double posZ, int amount, EntityLiving host){
		List list;
		
		if (host.getClass() == EntityPoliceBoss.class || host.getClass() == EntityFinalBoss.class) {
			list = world.playerEntities;
		}
		else {
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - 64, posY - 64, posZ - 64, posX + 64, posY + 64, posZ + 64);
			list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
		}
		
		if (list != null && !list.isEmpty()){
			Iterator iterator = list.iterator();
            while (iterator.hasNext()){
            	EntityPlayer player = (EntityPlayer) iterator.next();
            	ArrowQuestPlayer.increasePlayerXP(player, amount);
            	
            	String tag = null;
            	int objectiveFlag = -1;
    			
    			if (host.getClass() == EntityCastleSoldierKing.class) {
    				tag = ModLib.nbtPlayerHasBeatenHumanBoss;
    				objectiveFlag = 0;
    			}
    			else if (host.getClass() == EntityElfBoss.class) {
    				tag = ModLib.nbtPlayerHasBeatenElfBoss;
    				objectiveFlag = 0;
    			}
    			else if (host.getClass() == EntityOrcBoss.class) {
    				tag = ModLib.nbtPlayerHasBeatenOrcBoss;
    				objectiveFlag = 0;
    			}
    			else if (host.getClass() == EntityDwarfBoss.class) {
    				tag = ModLib.nbtPlayerHasBeatenDwarfBoss;
    				objectiveFlag = 0;
    			}
    			else if (host.getClass() == EntityWizard.class) {
    				tag = ModLib.nbtPlayerHasBeatenWizardBoss;
    			}
    			else if (host.getClass() == EntityVampireBoss.class) {
    				tag = ModLib.nbtPlayerHasBeatenVampireBoss;
    			}
    			else if (host.getClass() == EntityBlazeBoss.class) {
    				tag = ModLib.nbtPlayerHasBeatenBlazeBoss;
    				objectiveFlag = 1;
    			}
    			else if (host.getClass() == EntityPoliceBoss.class) {
    				tag = ModLib.nbtPlayerHasBeatenPoliceBoss;
    				WorldGenSchematics.logClearStructure(player, "heinleinShip", ModLib.nbtTier4StructuresCleared);
    			}
    			else if (host.getClass() == EntityFinalBoss.class) {
    				tag = ModLib.nbtPlayerHasBeatenFinalBoss;
    				WorldGenSchematics.logClearStructure(player, "finalDestination", ModLib.nbtTier4StructuresCleared);
    			}
            	
            	if (tag != null) {
            		NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
            		if (!nbt.hasKey(tag)) {
            			nbt.setBoolean(tag, true);
            			nbt.setByte(ModLib.nbtBossesKilled, (byte) (nbt.getByte(ModLib.nbtBossesKilled) + 1));
            			
            			if (objectiveFlag == 0) {
            				int objectiveProgress = nbt.getByte(ModLib.nbtObjectiveProgress) + 1;
            				if (objectiveProgress == 4) {
            					nbt.setByte(ModLib.nbtObjective, (byte) 1);
            					ArrowQuest.packetPipeline.sendTo(new PacketGuiPopup(0), (EntityPlayerMP) player);
            				}
            				
            				nbt.setByte(ModLib.nbtObjectiveProgress, (byte) objectiveProgress);
            			}
            			else if (objectiveFlag > 0) {
            				if (nbt.getByte(ModLib.nbtObjective) == objectiveFlag) {
            					ArrowQuest.packetPipeline.sendTo(new PacketGuiPopup(0), (EntityPlayerMP) player);
            					nbt.setByte(ModLib.nbtObjective, (byte) (objectiveFlag + 1));
            				}
            			}
            		}
            	}
            }
		}
	}
	
	public static void spawnPoliceSquad(World world, EntityPlayer player){
		EntityPoliceSpawner squad = new EntityPoliceSpawner(world);
		squad.setPosition(player.posX, player.posY, player.posZ);
		squad.targetPlayer = player;
		world.spawnEntityInWorld(squad);
		world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndPoliceSpawn1", 6.0F, 0.95F+(0.05F*ArrowQuest.RAND.nextFloat()));
	}
	
	public static void spawnElitePoliceSquad(World world, EntityPlayer player){
		EntityPoliceSpawner squad = new EntityPoliceSpawner(world);
		squad.setPosition(player.posX, player.posY, player.posZ);
		squad.isElite = true;
		squad.targetPlayer = player;
		squad.spawnTimer = 50;
		world.spawnEntityInWorld(squad);
	}
	
	public static boolean isEntityBoss(Class clazz) {
		for (Class c : bossEntities) {
			if (c == clazz) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Should only be called client-side
	 */
	public static void spawnBigExplosionEffect(World world, double x, double y, double z) {
		int k = 1 + world.rand.nextInt(3);
		world.playSound(x, y, z, "arrowquest:sndExplosionBig" + k, 3.0F, 0.93F+(0.1F*ArrowQuest.RAND.nextFloat()), false);
		AnimationHelper.spawnOneshotAnimation(world, AnimationHelper.bigExplosion, x, y - 1.4D, z, 5.0F);
		
		for (int i = 0; i < 10; i++) {
			EntityGenericProjectile proj = new EntityGenericProjectile(EntityGenericProjectile.TYPE_EXPLOSION_BIT, world, x, y, z,
					-0.5D + ArrowQuest.RAND.nextDouble(), ArrowQuest.RAND.nextDouble(), -0.5D + ArrowQuest.RAND.nextDouble(),
					0.5D, null);
			
			world.spawnEntityInWorld(proj);
		}
	}
}