package com.cm8check.arrowquest.item;

import java.util.List;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMagicCrystal extends Item{
	public ItemMagicCrystal(){
		this.setHasSubtypes(true);
		this.setUnlocalizedName(ModLib.itemMagicCrystalName);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return "item." + ModLib.itemMagicCrystalName + "_" + stack.getMetadata();
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		for (int i = 0; i < 6; ++i){
            list.add(new ItemStack(item, 1, i));
        }
	}
}