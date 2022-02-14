package com.cm8check.arrowquest.inventory;

import com.cm8check.arrowquest.inventory.container.ContainerBigCraftingTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

public class InventoryBigCraftingTable implements IInventory{
	private ItemStack result;
	public boolean custom = false;
	
	@Override
	public String getName(){
		return "Result";
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public IChatComponent getDisplayName(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSizeInventory(){
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index){
		return result;
	}

	@Override
	public ItemStack decrStackSize(int index, int count){
		if (result != null){
			ItemStack stack = result;
			result = null;
			return stack;
		}
		
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		if (result != null){
			ItemStack stack = result;
			result = null;
			return stack;
		}
		
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		result = stack;
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
	}

	@Override
	public void markDirty(){
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player){
		
	}

	@Override
	public void closeInventory(EntityPlayer player){
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return false;
	}

	@Override
	public int getField(int id){
		return 0;
	}

	@Override
	public void setField(int id, int value){
		
	}

	@Override
	public int getFieldCount(){
		return 0;
	}

	@Override
	public void clear(){
		result = null;
	}
}