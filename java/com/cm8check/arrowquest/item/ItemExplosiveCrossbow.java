package com.cm8check.arrowquest.item;

import com.cm8check.arrowquest.entity.EntityExplosiveArrow;
import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemExplosiveCrossbow extends ItemBow{
	private int[] cooldown = new int[769];
	
	public ItemExplosiveCrossbow(){
		this.setMaxDamage(768);
		this.setUnlocalizedName(ModLib.itemExplosiveCrossbowName);
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.maxStackSize = 1;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected){
		if (isSelected){
			if (!world.isRemote){
				if (cooldown[stack.getMetadata()] > 0){
					cooldown[stack.getMetadata()]--;
				}
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
		if (!world.isRemote && cooldown[itemstack.getMetadata()] <= 0){
			boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;

	        if (flag || player.inventory.hasItem(Items.arrow)){
	        	EntityArrow entityarrow = new EntityExplosiveArrow(world, player, 2.0F);
	        	entityarrow.setIsCritical(true);
	        	
	        	int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemstack);
	            entityarrow.setDamage(5.5D + (double) k);

	            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemstack);
	            if (l > 0){
	                entityarrow.setKnockbackStrength(l);
	            }

	            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemstack) > 0){
	                entityarrow.setFire(100);
	            }
	            
	            cooldown[itemstack.getMetadata()+1] = 10;
	            itemstack.damageItem(1, player);
	            world.playSoundAtEntity(player, "arrowquest:sndCrossbow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1.0F);

	            if (flag){
	                entityarrow.canBePickedUp = 2;
	            }else{
	                player.inventory.consumeInventoryItem(Items.arrow);
	            }
	            
	            world.spawnEntityInWorld(entityarrow);
	        }
		}
		
		return itemstack;
	}
}