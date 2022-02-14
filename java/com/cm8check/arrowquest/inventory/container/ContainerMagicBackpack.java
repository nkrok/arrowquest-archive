package com.cm8check.arrowquest.inventory.container;

import com.cm8check.arrowquest.inventory.InventoryMagicBackpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMagicBackpack extends Container{
	private InventoryMagicBackpack inventory;
	
	public ContainerMagicBackpack(InventoryPlayer playerInventory, InventoryMagicBackpack inventory){
		this.inventory = inventory;
		
		int i;
		int i1;
		
		//backpack inventory
		for (i = 0; i < 3; i++){
			for (i1 = 0; i1 < 9; i1++){
				this.addSlotToContainer(new Slot(inventory, i1 + i * 9, 8 + i1 * 18, 18 + i * 18));
			}
		}
		
		//player inventory
		for (i = 0; i < 3; i++){
			for (i1 = 0; i1 < 9; i1++){
				this.addSlotToContainer(new Slot(playerInventory, i1 + i * 9 + 9, 8 + i1 * 18, 84 + i * 18));
			}
		}
		//player hotbar
		for (i = 0; i < 9; i++){
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player){
		super.onContainerClosed(player);
		if (!player.worldObj.isRemote){
			this.inventory.close();
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn){
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityplayer, int index){
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index < 27){
				if (!this.mergeItemStack(itemstack1, 27, 63, true)){
					return null;
				}
			}else{
				if (!this.mergeItemStack(itemstack1, 0, 27, false)){
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