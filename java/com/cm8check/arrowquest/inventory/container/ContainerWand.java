package com.cm8check.arrowquest.inventory.container;

import com.cm8check.arrowquest.inventory.InventoryWand;
import com.cm8check.arrowquest.item.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWand extends Container{
	private InventoryWand inventory;
	private int size;
	
	public ContainerWand(InventoryPlayer playerInventory, InventoryWand inventory){
		this.inventory = inventory;
		size = inventory.size;
		
		int i;
		int i1;
		
		//wand inventory
		for (i = 0; i < size/2; i++){
			for (i1 = 0; i1 < 2; i1++){
				this.addSlotToContainer(new SlotWand(inventory, i1 + i * 2, 72 + i1 * 18, 17 + i * 18));
			}
		}
		
		//player inventory
		for (i = 0; i < 3; i++){
			for (i1 = 0; i1 < 9; i1++){
				this.addSlotToContainer(new Slot(playerInventory, i1 + i * 9 + 9, 8 + i1 * 18, 100 + i * 18));
			}
		}
		//player hotbar
		for (i = 0; i < 9; i++){
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 158));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn){
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player){
		super.onContainerClosed(player);
		if (!player.worldObj.isRemote){
			this.inventory.close();
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityplayer, int index){
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index < size){
				if (!this.mergeItemStack(itemstack1, size, 36+size, true)){
					return null;
				}
			}else{
				if (!this.mergeItemStack(itemstack1, 0, size, false)){
					return null;
				}
			}
			
			if (itemstack1.stackSize == 0){
				slot.putStack((ItemStack)null);
			}else{
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack.stackSize){
				return null;
			}
			slot.onPickupFromSlot(entityplayer, itemstack1);
		}
		
		return itemstack;
	}
}

class SlotWand extends Slot{
	private int index;
	
	public SlotWand(IInventory inventory, int index, int xPosition, int yPosition){
		super(inventory, index, xPosition, yPosition);
		this.index = index;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack){
		return this.inventory.isItemValidForSlot(this.index, stack);
	}
}