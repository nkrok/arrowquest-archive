package com.cm8check.arrowquest.inventory.container;

import com.cm8check.arrowquest.tileentity.TileEntityAirlockController;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerAirlockController extends Container {
	private final TileEntityAirlockController tile;
	
	private int airlockOpen;
	private int enabled;

	public ContainerAirlockController(InventoryPlayer playerInventory, TileEntityAirlockController tile) {
		this.tile = tile;

		int i;
		int i1;

		// player inventory
		for (i = 0; i < 3; i++) {
			for (i1 = 0; i1 < 9; i1++) {
				this.addSlotToContainer(new Slot(playerInventory, i1 + i * 9 + 9, 8 + i1 * 18, 84 + i * 18));
			}
		}
		// player hotbar
		for (i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tile.isUseableByPlayer(player);
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting listener){
        super.addCraftingToCrafters(listener);
        listener.func_175173_a(this, this.tile);
    }
	
	@Override
	public void detectAndSendChanges(){
        super.detectAndSendChanges();
        
        for (int i = 0; i < this.crafters.size(); ++i){
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.airlockOpen != this.tile.getField(0)){
                icrafting.sendProgressBarUpdate(this, 0, this.tile.getField(0));
            }
            
            if (this.enabled != this.tile.getField(1)){
                icrafting.sendProgressBarUpdate(this, 0, this.tile.getField(1));
            }
        }
        
        this.airlockOpen = this.tile.getField(0);
        this.enabled = this.tile.getField(1);
	}
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data){
        this.tile.setField(id, data);
    }

	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityplayer, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < 27) {
				if (!this.mergeItemStack(itemstack1, 27, 36, false)) {
					return null;
				}
			} else {
				if (!this.mergeItemStack(itemstack1, 0, 27, false)) {
					return null;
				}
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(entityplayer, itemstack1);
		}

		return itemstack;
	}
}