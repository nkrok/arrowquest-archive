package com.cm8check.arrowquest.item;

import java.util.List;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemAQKey extends Item{
	public static String[] names = new String[]{
		"arrowquest:"+ModLib.itemKeyRedName,
		"arrowquest:"+ModLib.itemKeyYellowName,
		"arrowquest:"+ModLib.itemKeyBlueName,
		"arrowquest:"+ModLib.itemKeyGreenName,
		"arrowquest:"+ModLib.itemKeyPurpleName
	};
	
	protected ItemAQKey(){
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setUnlocalizedName(ModLib.itemKeyName);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setMaxStackSize(1);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return "item." + ModLib.itemKeyName + "_" + stack.getMetadata();
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		for (int i = 0; i < names.length; ++i){
            list.add(new ItemStack(item, 1, i));
        }
	}
}