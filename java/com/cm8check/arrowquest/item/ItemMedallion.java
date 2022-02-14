package com.cm8check.arrowquest.item;

import java.util.List;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemMedallion extends Item{
	public ItemMedallion(){
		this.setUnlocalizedName(ModLib.itemMedallionName);
		this.maxStackSize = 1;
		this.setMaxDamage(1200);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected){
		if (!world.isRemote){
			long time = world.getWorldInfo().getWorldTotalTime();
			if (time % 80 == 0 && entity instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer) entity;
				player.heal(1);
				
				stack.damageItem(1, player);
				
				if (stack.stackSize == 0) {
					player.inventory.setInventorySlotContents(slot, null);
				}
			}
		}
	}
}