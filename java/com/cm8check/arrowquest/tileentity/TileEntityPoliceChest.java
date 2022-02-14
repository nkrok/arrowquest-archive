package com.cm8check.arrowquest.tileentity;

import com.cm8check.arrowquest.item.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;

public class TileEntityPoliceChest extends TileEntityChest{
	private int keyNumber;
	
	public void setKeyNumber(int num) {
		keyNumber = num + 1;
	}
	
	@Override
	public void closeInventory(EntityPlayer player) {
		if (keyNumber > 0) {
			this.setInventorySlotContents(0, new ItemStack(ModItems.itemKey, 1, keyNumber - 1));
		}
		
		super.closeInventory(player);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		keyNumber = compound.getInteger("KeyNumber");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		compound.setInteger("KeyNumber", keyNumber);
	}
}