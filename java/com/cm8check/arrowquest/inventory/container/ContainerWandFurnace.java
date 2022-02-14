package com.cm8check.arrowquest.inventory.container;

import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.tileentity.TileEntityWandFurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerWandFurnace extends Container{
	private final IInventory tile;
	private int chargeStored;
	
	public ContainerWandFurnace(InventoryPlayer playerInventory, TileEntityWandFurnace inventory){
		this.tile = inventory;
		
		//furnace inventory
		this.addSlotToContainer(new Slot(inventory, 0, 80, 18));
		this.addSlotToContainer(new Slot(inventory, 1, 80, 49));
		
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
	public void addCraftingToCrafters(ICrafting listener){
        super.addCraftingToCrafters(listener);
        listener.func_175173_a(this, this.tile);
    }
	
	@Override
	public void detectAndSendChanges(){
        super.detectAndSendChanges();
        
        for (int i = 0; i < this.crafters.size(); ++i){
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.chargeStored != this.tile.getField(0)){
                icrafting.sendProgressBarUpdate(this, 0, this.tile.getField(0));
            }
        }
        
        this.chargeStored = this.tile.getField(0);
	}
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data){
        this.tile.setField(id, data);
    }
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityplayer, int index){
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index < 2){
				if (!this.mergeItemStack(itemstack1, 2, 38, true)){
					return null;
				}
			}else{
				if (itemstack1.getItem() == ModItems.itemMagicCrystal){
					if (!this.mergeItemStack(itemstack1, 1, 2, false)){
						return null;
					}
				}else if (itemstack1.getItem() == ModItems.itemWand || itemstack1.getItem() == ModItems.itemStaff1 || itemstack1.getItem() == ModItems.itemStaff2 || itemstack1.getItem() == ModItems.itemStaff3){
					if (!this.mergeItemStack(itemstack1, 0, 1, false)){
						return null;
					}
				}else{
					if (index >= 2 && index < 29){
						if (!this.mergeItemStack(itemstack1, 29, 38, false)){
							return null;
						}
					}else if (index >= 29 && index < 38){
						if (!this.mergeItemStack(itemstack1, 2, 29, false)){
							return null;
						}
					}
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