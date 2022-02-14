package com.cm8check.arrowquest.item;


import com.cm8check.arrowquest.entity.EntityGenericProjectile;
import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemPoliceGun extends Item{
	protected ItemPoliceGun(){
		this.setUnlocalizedName(ModLib.itemPoliceGunName);
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabCombat);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
		if (!world.isRemote){
			Vec3 look = player.getLook(1.0F);
			EntityGenericProjectile entity = new EntityGenericProjectile(EntityGenericProjectile.TYPE_POLICE_LASER, world, player.posX, player.posY + player.getEyeHeight(), player.posZ, look.xCoord, look.yCoord, look.zCoord, 0.6D, player);
			world.spawnEntityInWorld(entity);
			
			world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndPoliceLaser", 1, 1);
		}
		
		return itemstack;
	}
}