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

public class ItemCustomIngot extends Item{
	public ItemCustomIngot(String name){
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
}