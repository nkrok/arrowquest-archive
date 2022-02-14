package com.cm8check.arrowquest.item;

import java.util.List;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemCursedFeather extends Item{
	public ItemCursedFeather(){
		this.setUnlocalizedName(ModLib.itemCursedFeatherName);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.maxStackSize = 1;
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack itemstack, EntityPlayer player){
		return false;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
		if (!world.isRemote){
			EntityPlayer player = (EntityPlayer) entity;
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
			
			if (!nbt.getBoolean(ModLib.nbtPlayerCursedFeather)){
				world.playSoundEffect(player.posX, player.posY, player.posZ, "ambient.weather.thunder", 10000.0F, 0.9F);
				player.setHealth(player.getHealth() / 2);
				nbt.setBoolean(ModLib.nbtPlayerCursedFeather, true);
			}
			
			//player.addExhaustion(0.05F);
		}
	}
}