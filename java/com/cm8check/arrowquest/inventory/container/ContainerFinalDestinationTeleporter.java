package com.cm8check.arrowquest.inventory.container;

import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.tileentity.TileEntityFinalDestinationTeleporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFinalDestinationTeleporter extends Container{
	private final IInventory tile;
	
	public ContainerFinalDestinationTeleporter(InventoryPlayer playerInventory, TileEntityFinalDestinationTeleporter inventory){
		this.tile = inventory;
		
		//inventory
		this.addSlotToContainer(new SlotFDTeleporter(inventory, 0, 80, 34));
		
		int i;
		int i1;
		
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
			
			if (index == 0){
				if (!this.mergeItemStack(itemstack1, 1, 37, true)){
					return null;
				}
			}else{
				if (!this.mergeItemStack(itemstack1, 0, 1, false)){
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

class SlotFDTeleporter extends Slot{
	public SlotFDTeleporter(IInventory inventoryIn, int index, int xPosition, int yPosition){
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack){
		return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
	}
}