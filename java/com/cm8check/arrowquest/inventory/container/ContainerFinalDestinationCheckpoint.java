package com.cm8check.arrowquest.inventory.container;

import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.tileentity.TileEntityFinalDestinationCheckpoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFinalDestinationCheckpoint extends Container{
	private final IInventory tile;
	
	public ContainerFinalDestinationCheckpoint(InventoryPlayer playerInventory, TileEntityFinalDestinationCheckpoint inventory){
		this.tile = inventory;
		
		int i;
		int i1;
		
		//inventory
		for (i = 0; i < 5; i++){
			this.addSlotToContainer(new SlotFDCheckpoint(inventory, i, 44 + i * 18, 34));
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
	public boolean canInteractWith(EntityPlayer player){
		return this.tile.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityplayer, int index){
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index < 5){
				if (!this.mergeItemStack(itemstack1, 5, 41, true)){
					return null;
				}
			}else{
				if (!this.mergeItemStack(itemstack1, 0, 5, false)){
					return null;
				}
			}
			
			if (itemstack1.stackSize == 0){
				slot.putStack((ItemStack) null);
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

class SlotFDCheckpoint extends Slot{
	public SlotFDCheckpoint(IInventory inventoryIn, int index, int xPosition, int yPosition){
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack){
		return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
	}
}