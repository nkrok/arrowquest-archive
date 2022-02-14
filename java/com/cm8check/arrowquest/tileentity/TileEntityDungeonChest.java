package com.cm8check.arrowquest.tileentity;

import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.item.ItemScroll;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;

public class TileEntityDungeonChest extends TileEntity implements IUpdatePlayerListBox{
	private static final int chestTypeWeapons = 0;
	private static final int chestTypeFood = 1;
	private static final int chestTypePolice = 2;
	private static final int chestTypeFire = 3;
	private static final int chestTypeKey = 4;
	public int chestType;
	public int chestGrade;
	
	private static int keysSet = 0;
	
	private static final Item[] policeChestLoot = {
		ModItems.itemPoliceSabre, ModItems.itemPoliceBoots, ModItems.itemPoliceLeggings, ModItems.itemPoliceChestplate, ModItems.itemPoliceHelmet,
		ModItems.itemTrueforceBoots, ModItems.itemTrueforceLeggings, ModItems.itemTrueforceChestplate, ModItems.itemTrueforceHelmet, ModItems.itemTrueforceIngot,
		ModItems.itemMagicCrystal, ModItems.itemStaff3, Item.getItemFromBlock(Blocks.diamond_block), ModItems.itemMedallion, Items.enchanted_book,
		ModItems.itemDefenseMedallion, ModItems.itemFireMedallion, ModItems.itemExecutionerAxe, ModItems.itemFidelitySword, ModItems.itemSingingSword
	};
	private static final Item[] fireChestLoot = {
		ModItems.itemTrueforceBoots, ModItems.itemTrueforceLeggings, ModItems.itemTrueforceChestplate, ModItems.itemTrueforceHelmet,
		ModItems.itemMagicCrystal, ModItems.itemStaff3, ModItems.itemMedallion, Items.enchanted_book, ModItems.itemCursedFeather, ModItems.itemCursedFeather,
		ModItems.itemCursedFeather, ModItems.itemCursedFeather, ModItems.itemScroll, ModItems.itemScroll, ModItems.itemExplosiveCrossbow, ModItems.itemPoliceSabre,
		Items.arrow, Items.arrow, ModItems.itemTrueforceIngot, ModItems.itemDefenseMedallion, ModItems.itemEnergyBoots, ModItems.itemEnergyLeggings,
		ModItems.itemEnergyChestplate, ModItems.itemEnergyHelmet, ModItems.itemDemonBlade, ModItems.itemDemonTrident, ModItems.itemSceptreOfAsmodeus,
		ModItems.itemSceptreOfTorment
	};
	
	private static final Item[] swords = {
		Items.stone_sword, Items.iron_sword, Items.diamond_sword
	};
	private static final Item[] longswords = {
		ModItems.itemLongswordStone, ModItems.itemLongswordIron, ModItems.itemLongswordDiamond
	};
	private static final Item[] hammers = {
		ModItems.itemHammerStone, ModItems.itemHammerIron, ModItems.itemHammerDiamond
	};
	private static final Item[] battleAxes = {
		ModItems.itemBattleAxeStone, ModItems.itemBattleAxeIron, ModItems.itemBattleAxeDiamond
	};
	private static final Item[] tools = {
		Items.iron_pickaxe, Items.golden_pickaxe, Items.diamond_pickaxe
	};
	private static final Item[] armors = {
		Items.chainmail_boots, Items.chainmail_leggings, Items.chainmail_chestplate, Items.chainmail_helmet,
		Items.iron_boots, Items.iron_leggings, Items.iron_chestplate, Items.iron_helmet,
		Items.diamond_boots, Items.diamond_leggings, Items.diamond_chestplate, Items.diamond_helmet
	};
	private static final Item[] foods = {
		Items.apple, Items.cooked_beef, Items.cooked_porkchop, Items.cooked_chicken, Items.beef, Items.porkchop, Items.chicken,
		Items.bread, Items.potato, Items.baked_potato, Items.melon
	};
	private static final Item[] misc = {
		Items.arrow, Items.arrow, Item.getItemFromBlock(Blocks.planks), Item.getItemFromBlock(Blocks.stone), Item.getItemFromBlock(Blocks.torch),
		Items.coal, Item.getItemFromBlock(Blocks.log), ModItems.itemDaggerStone, ModItems.itemDaggerIron, ModItems.itemClub
	};
	private static final Item[] tier1specialLoot = {
		ModItems.itemWand, ModItems.itemEnderSword, Item.getItemFromBlock(Blocks.enchanting_table), ModItems.itemMagicCrystal, Item.getItemFromBlock(Blocks.iron_block),
		ModItems.itemTrueforceIngot, ModItems.itemHeavyDutyIngot, ModItems.itemHeavyDutyIngot, ModItems.itemGlaive, ModItems.itemFalchion, ModItems.itemSabre,
		ModItems.itemSoulStealer, ModItems.itemVampiresTooth, ModItems.itemThiefsDagger
	};
	private static final Item[] tier2specialLoot = {
		ModItems.itemMagicCrystal, ModItems.itemMagicCrystal, ModItems.itemStaff1, Items.diamond, Items.emerald, Items.blaze_rod,
		ModItems.itemTrueforceIngot, ModItems.itemTrueforceIngot, ModItems.itemHeavyDutyIngot, ModItems.itemHeavyDutyIngot,
		ModItems.itemSpikedClub, ModItems.itemBroadAxe, ModItems.itemDoubleSword, ModItems.itemScimitar, ModItems.itemFidelitySword,
		ModItems.itemGreatsword, ModItems.itemSingingSword, ModItems.itemMysteryMace, ModItems.itemEvilBlade, ModItems.itemXPSword,
		ModItems.itemLootMedallion
	};
	private static final Item[] tier3specialLoot = {
		ModItems.itemMagicCrystal, ModItems.itemMagicCrystal, ModItems.itemMagicCrystal, ModItems.itemStaff2, Item.getItemFromBlock(Blocks.diamond_block),
		Item.getItemFromBlock(Blocks.emerald_block), ModItems.itemMedallion, ModItems.itemFireMedallion, ModItems.itemDefenseMedallion,
		ModItems.itemExecutionerAxe, ModItems.itemDemonTrident, ModItems.itemSceptreOfAsmodeus, ModItems.itemSceptreOfAsmodeus,
		ModItems.itemSingingSword, ModItems.itemEvilBlade, ModItems.itemEvilBlade, ModItems.itemXPSword
	};
	private static final int[] tier1scrolls = {
		5, 10, 14, 16, 17
	};
	private static final int[] tier2scrolls = {
		8, 11, 15
	};
	private static final int[] tier3scrolls = {
		6, 9, 12, 13, 18
	};
	private static final int[] tier4scrolls = {
		19, 20, 21
	};
	private static final int[] tier5scrolls = {
		22
	};
	
	public boolean isUseableByPlayer(EntityPlayer player){
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		chestType = compound.getByte("chestType");
		chestGrade = compound.getByte("chestGrade");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		compound.setByte("chestType", (byte) chestType);
		compound.setByte("chestGrade", (byte) chestGrade);
	}
	
	@Override
	public void update(){
		if (!ArrowQuest.DEV_MODE && this.hasWorldObj() && worldObj.getDifficulty() != EnumDifficulty.PEACEFUL){
			spawnChest();
		}
	}
	
	public void spawnChest(){
		worldObj.setBlockToAir(pos);
		worldObj.removeTileEntity(pos);
		if (chestType == chestTypeKey){
			worldObj.setBlockState(pos, ModBlocks.blockPoliceChest.getDefaultState());
		}else if (chestType == chestTypeFire){
			worldObj.setBlockState(pos, ModBlocks.blockFireChest.getDefaultState());
		}else if (worldObj.provider.getDimensionId() == ModLib.dimPoliceBaseID){
			worldObj.setBlockState(pos, ModBlocks.blockPoliceChest.getDefaultState());
		}else{
			worldObj.setBlockState(pos, Blocks.chest.getDefaultState());
		}
		TileEntityChest tile = (TileEntityChest) worldObj.getTileEntity(pos);
		setChestContents(tile, chestType, chestGrade);
	}
	
	public static void setChestContents(TileEntityChest tile, int chestType, int chestGrade){
		Random rand = ArrowQuest.RAND;
		
		int numItems = 3 + rand.nextInt((chestGrade+1)*2);
		
		if (chestType == chestTypePolice){
			numItems = 4 + rand.nextInt(3);
		}else if (chestType == chestTypeFire){
			numItems += 5;
		}else if (chestType == chestTypeKey) {
			numItems += 10;
		}
		
		if (chestGrade == 2){
			numItems += 7;
		}
		
		for (int i = 0; i < numItems; i++){
			ItemStack itemstack = null;
			
			float itemDamageScale;
			
			/*
			if (chestGrade == 2 || chestType == chestTypePolice || chestType == chestTypeFire || chestType == chestTypeKey) {
				itemDamageScale = 0.6F + rand.nextFloat() * 0.35F;
				if (itemDamageScale > 0.9F)
					itemDamageScale = 1.0F;
			}
			else if (chestGrade == 1) {
				itemDamageScale = 0.35F + rand.nextFloat() * 0.45F;
			}
			else {
				itemDamageScale = 0.1F + rand.nextFloat() * 0.65F;
			}
			
			itemDamageScale = 1.0F - itemDamageScale;
			*/
			
			itemDamageScale = 0.0F;
			
			if (chestType == chestTypeWeapons){
				int type = 4;
				if (rand.nextInt(2+chestGrade) > 0){
					type = rand.nextInt(4);
				}else if (chestGrade == 2){
					type = 3;
				}
				
				if (chestGrade == 0 && type == 3)
					type = 4;
				
				if (type == 0){ // weapons
					int weapon = rand.nextInt(4);
					if (weapon == 3){
						itemstack = new ItemStack(Items.bow);
						for (int j = 0; j < rand.nextInt(chestGrade+1); j++){
							int ench = rand.nextInt(3);
							if (ench == 0){
								itemstack.addEnchantment(Enchantment.power, 1+rand.nextInt(Math.max(1, chestGrade)));
							}else if (ench == 1){
								itemstack.addEnchantment(Enchantment.punch, chestGrade);
							}else if (ench == 2){
								itemstack.addEnchantment(Enchantment.flame, 1);
							}
						}
						
						itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
					}else if (weapon < 3){
						Item item = null;
						
						if (rand.nextInt(5) == 0){
							item = longswords[rand.nextInt(chestGrade+1)];
							if (chestGrade == 2 && item == longswords[0]){
								item = longswords[2];
							}
						}else{
							if (rand.nextInt(5) == 0){
								item = hammers[rand.nextInt(chestGrade+1)];
								if (chestGrade == 2 && item == hammers[0]){
									item = hammers[2];
								}
							}else{
								if (rand.nextInt(5) == 0){
									item = battleAxes[rand.nextInt(chestGrade+1)];
									if (chestGrade == 2 && item == battleAxes[0]){
										item = battleAxes[2];
									}
								}else{
									item = swords[rand.nextInt(chestGrade+1)];
									if (chestGrade == 2 && item == swords[0]){
										item = swords[2];
									}
								}
							}
						}
						
						itemstack = new ItemStack(item);
						for (int j = 0; j < rand.nextInt(chestGrade+1); j++){
							int ench = rand.nextInt(4);
							if (ench == 0){
								int level = 1+rand.nextInt(Math.max(1, chestGrade));
								if (chestGrade == 2){
									level += 1+rand.nextInt(2);
								}
								itemstack.addEnchantment(Enchantment.sharpness, level);
							}else if (ench == 1){
								itemstack.addEnchantment(Enchantment.fireAspect, chestGrade);
							}else if (ench == 2){
								itemstack.addEnchantment(Enchantment.looting, 1+rand.nextInt(3));
							}
							else {
								itemstack.addEnchantment(Enchantment.unbreaking, chestGrade);
							}
						}
						
						itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
					}
				}else if (type == 1){ // armors
					itemstack = new ItemStack(armors[4*rand.nextInt(chestGrade+1) + rand.nextInt(4)]);
					for (int j = 0; j < rand.nextInt(chestGrade+1); j++){
						int ench = rand.nextInt(5);
						if (ench == 0){
							int level = 1+rand.nextInt(Math.max(1, chestGrade));
							if (chestGrade == 2){
								level += 1+rand.nextInt(2);
							}
							itemstack.addEnchantment(Enchantment.protection, level);
						}else if (ench == 1){
							itemstack.addEnchantment(Enchantment.unbreaking, chestGrade);
						}else if (ench == 2){
							itemstack.addEnchantment(Enchantment.projectileProtection, 1+rand.nextInt(Math.max(1, chestGrade)));
						}else if (ench == 3){
							itemstack.addEnchantment(Enchantment.fireProtection, 1+rand.nextInt(Math.max(1, chestGrade)));
						}else if (ench == 4){
							itemstack.addEnchantment(Enchantment.blastProtection, 1+rand.nextInt(Math.max(1, chestGrade)));
						}
					}
					
					itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
				}else if (type == 2){ // tools 
					itemstack = new ItemStack(tools[rand.nextInt(chestGrade+1)]);
					for (int j = 0; j < rand.nextInt(chestGrade+1); j++){
						int ench = rand.nextInt(2);
						if (ench == 0){
							itemstack.addEnchantment(Enchantment.efficiency, 1+rand.nextInt(Math.max(1, chestGrade)));
						}else if (ench == 1){
							itemstack.addEnchantment(Enchantment.unbreaking, chestGrade);
						}
					}
					
					itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
				}else if (type == 3){ // special loot
					int specialLootType = rand.nextInt(3);
					
					int specialLootTier = 0;
					if (rand.nextInt(3) == 0){
						specialLootTier = 1;
					}else if (rand.nextInt(5) == 0){
						specialLootTier = 2;
					}
					
					if (specialLootType == 0){ //scroll
						int scroll;
						if (specialLootTier == 1){
							scroll = tier2scrolls[rand.nextInt(tier2scrolls.length)];
						}else if (specialLootTier == 2){
							scroll = tier3scrolls[rand.nextInt(tier3scrolls.length)];
						}else{
							scroll = tier1scrolls[rand.nextInt(tier1scrolls.length)];
						}
						
						itemstack = new ItemStack(ModItems.itemScroll, 1, scroll);
					}else if (specialLootType == 1){ //random loot
						Item item;
						int meta = 0;
						int amount = 1;
						if (specialLootTier == 1){
							item = tier2specialLoot[rand.nextInt(tier2specialLoot.length)];
							if (item == ModItems.itemMagicCrystal){
								meta = rand.nextInt(5)+1;
							}
						}else if (specialLootTier == 2){
							item = tier3specialLoot[rand.nextInt(tier3specialLoot.length)];
						}else{
							item = tier1specialLoot[rand.nextInt(tier1specialLoot.length)];
						}
						
						if (item == ModItems.itemHeavyDutyIngot){
							amount = 2 + rand.nextInt(3);
						}else if (item == ModItems.itemTrueforceIngot){
							amount = 1 + rand.nextInt(2);
						}
						
						itemstack = new ItemStack(item, amount, meta);
						
						if (chestGrade > 0 && item instanceof ItemSword) {
							for (int j = 0; j < rand.nextInt(chestGrade+1); j++){
								int ench = rand.nextInt(3);
								if (ench == 0){
									int level = 1+rand.nextInt(Math.max(1, chestGrade));
									if (chestGrade == 2){
										level += 1+rand.nextInt(2);
									}
									itemstack.addEnchantment(Enchantment.sharpness, level);
								}else if (ench == 1){
									itemstack.addEnchantment(Enchantment.unbreaking, chestGrade);
								}else if (ench == 2){
									itemstack.addEnchantment(Enchantment.looting, 1+rand.nextInt(3));
								}
							}
						}
						
						if (item instanceof ItemSword || item instanceof ItemArmor) {
							itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
						}
					}else{ //enchanted book
						itemstack = new ItemStack(Items.enchanted_book);
						
						EnchantmentData enchanment;
						int ench = rand.nextInt(4);
						if (ench == 0){
							enchanment = new EnchantmentData(Enchantment.sharpness, 2+rand.nextInt(2 + chestGrade));
						}else if (ench == 1){
							enchanment = new EnchantmentData(Enchantment.protection, 1+rand.nextInt(2 + chestGrade));
						}else if (ench == 2){
							enchanment = new EnchantmentData(Enchantment.projectileProtection, 1+rand.nextInt(2 + chestGrade));
						}
						else {
							enchanment = new EnchantmentData(Enchantment.unbreaking, 1+rand.nextInt(1 + chestGrade));
						}
						
						Items.enchanted_book.addEnchantment(itemstack, enchanment);
					}
				}else{ // misc 
					Item item = misc[rand.nextInt(misc.length)];
					itemstack = new ItemStack(item, Math.min(2+rand.nextInt(7)+rand.nextInt(15*(chestGrade+1)), item.getItemStackLimit()));
					
					if (item instanceof ItemSword) {
						itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
					}
				}
			}else if (chestType == chestTypeFood){
				int food = rand.nextInt(foods.length);
				itemstack = new ItemStack(foods[food], 2 + rand.nextInt(2) + rand.nextInt(2*(chestGrade+1)));
			}else if (chestType == chestTypePolice){
				Item item = policeChestLoot[rand.nextInt(policeChestLoot.length)];
				
				if (item == Items.enchanted_book){
					itemstack = new ItemStack(Items.enchanted_book);
					
					EnchantmentData enchanment;
					int ench = rand.nextInt(4);
					if (ench == 0){
						enchanment = new EnchantmentData(Enchantment.sharpness, 5 + rand.nextInt(3));
					}else if (ench == 1){
						enchanment = new EnchantmentData(Enchantment.protection, 4);
					}else if (ench == 2){
						enchanment = new EnchantmentData(Enchantment.projectileProtection, 4);
					}
					else {
						enchanment = new EnchantmentData(Enchantment.unbreaking, 3);
					}
					
					Items.enchanted_book.addEnchantment(itemstack, enchanment);
				}else{
					int amount = 1;
					if (item == ModItems.itemTrueforceIngot){
						amount += rand.nextInt(5);
					}
					
					itemstack = new ItemStack(item, amount);
					
					if (item instanceof ItemSword) {
						for (int j = 0; j < rand.nextInt(3); j++){
							int ench = rand.nextInt(4);
							if (ench == 0){
								int level = 1+rand.nextInt(2);
								if (chestGrade == 2){
									level += 1+rand.nextInt(2);
								}
								itemstack.addEnchantment(Enchantment.sharpness, level);
							}else if (ench == 1){
								itemstack.addEnchantment(Enchantment.fireAspect, 1);
							}else if (ench == 2){
								itemstack.addEnchantment(Enchantment.looting, 1+rand.nextInt(3));
							}
							else {
								itemstack.addEnchantment(Enchantment.unbreaking, 1+rand.nextInt(3));
							}
						}
					}
					
					if (item instanceof ItemSword || item instanceof ItemArmor) {
						itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
					}
				}
			}else if (chestType == chestTypeFire){
				Item item = fireChestLoot[rand.nextInt(fireChestLoot.length)];
				
				if (item instanceof ItemSword){
					itemstack = new ItemStack(item);
					itemstack.addEnchantment(Enchantment.sharpness, 6+rand.nextInt(2));
					if (rand.nextInt(2) == 0){
						itemstack.addEnchantment(Enchantment.fireAspect, 2);
					}
					if (rand.nextInt(2) == 0){
						itemstack.addEnchantment(Enchantment.looting, 3);
					}
					
					itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
				}else if (item instanceof ItemArmor){
					itemstack = new ItemStack(item);
					if (rand.nextInt(2) == 0){
						itemstack.addEnchantment(Enchantment.protection, 4);
					}else if (rand.nextInt(2) == 0){
						itemstack.addEnchantment(Enchantment.projectileProtection, 4);
					}else if (rand.nextInt(2) == 0){
						itemstack.addEnchantment(Enchantment.blastProtection, 4);
					}
					if (rand.nextInt(2) == 0){
						itemstack.addEnchantment(Enchantment.unbreaking, 3);
					}
					
					itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
				}else if (item == Items.enchanted_book){
					itemstack = new ItemStack(Items.enchanted_book);
					
					EnchantmentData enchanment;
					int ench = rand.nextInt(3);
					if (ench == 0){
						enchanment = new EnchantmentData(Enchantment.sharpness, 7);
					}else if (ench == 1){
						enchanment = new EnchantmentData(Enchantment.protection, 5);
					}else{
						enchanment = new EnchantmentData(Enchantment.projectileProtection, 5);
					}
					
					Items.enchanted_book.addEnchantment(itemstack, enchanment);
				}else if (item == ModItems.itemScroll){
					int scroll = tier4scrolls[rand.nextInt(tier4scrolls.length)];
					itemstack = new ItemStack(ModItems.itemScroll, 1, scroll);
				}else{
					int amount = 1;
					if (item == ModItems.itemTrueforceIngot){
						amount = 2;
						amount += rand.nextInt(8);
					}else if (item == ModItems.itemMagicCrystal){
						amount += rand.nextInt(4);
					}else if (item == Items.arrow){
						amount = 64;
					}else if (item == Item.getItemFromBlock(Blocks.diamond_block)){
						amount += rand.nextInt(4);
					}
					
					itemstack = new ItemStack(item, amount);
				}
			}else if (chestType == chestTypeKey){
				if (i == 0){
					itemstack = new ItemStack(ModItems.itemKey, 1, keysSet);
					
					TileEntityPoliceChest tileEntityPoliceChest = (TileEntityPoliceChest) tile;
					tileEntityPoliceChest.setKeyNumber(keysSet);
					
					keysSet++;
					if (keysSet >= 5){
						keysSet = 0;
					}
				}else{
					int specialLootType = rand.nextInt(3);
					
					int specialLootTier = 0;
					if (rand.nextInt(3) == 0){
						specialLootTier = 1;
					}else if (rand.nextInt(4) == 0){
						specialLootTier = 2;
					}
					
					if (specialLootType == 0){ //scroll
						int scroll;
						if (specialLootTier == 1){
							scroll = tier2scrolls[rand.nextInt(tier2scrolls.length)];
						}else if (specialLootTier == 2){
							scroll = tier3scrolls[rand.nextInt(tier3scrolls.length)];
						}else{
							scroll = tier1scrolls[rand.nextInt(tier1scrolls.length)];
						}
						
						itemstack = new ItemStack(ModItems.itemScroll, 1, scroll);
					}else if (specialLootType == 1){ //random loot
						Item item;
						int meta = 0;
						if (specialLootTier == 1){
							item = tier2specialLoot[rand.nextInt(tier2specialLoot.length)];
							if (item == ModItems.itemMagicCrystal){
								meta = rand.nextInt(5)+1;
							}
						}else if (specialLootTier == 2){
							item = tier3specialLoot[rand.nextInt(tier3specialLoot.length)];
						}else{
							item = tier1specialLoot[rand.nextInt(tier1specialLoot.length)];
						}
						
						itemstack = new ItemStack(item, 1, meta);
						
						if (item instanceof ItemSword || item instanceof ItemArmor) {
							itemstack.setItemDamage(MathHelper.floor_float(itemstack.getMaxDamage() * itemDamageScale));
						}
					}else{ //enchanted book
						itemstack = new ItemStack(Items.enchanted_book);
						
						EnchantmentData enchanment;
						int ench = rand.nextInt(3);
						if (ench == 0){
							enchanment = new EnchantmentData(Enchantment.sharpness, 4+rand.nextInt(2));
						}else if (ench == 1){
							enchanment = new EnchantmentData(Enchantment.protection, 3+rand.nextInt(2));
						}else{
							enchanment = new EnchantmentData(Enchantment.projectileProtection, 3+rand.nextInt(2));
						}
						
						Items.enchanted_book.addEnchantment(itemstack, enchanment);
					}
				}
			}
			
			tile.setInventorySlotContents(i, itemstack);
		}
	}
}