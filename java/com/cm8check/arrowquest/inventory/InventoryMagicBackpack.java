package com.cm8check.arrowquest.inventory;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;

public class InventoryMagicBackpack implements IInventory{
	protected ItemStack[] inventory = new ItemStack[27];
	private EntityPlayer playerUsing;
	
	public InventoryMagicBackpack(EntityPlayer player){
		this.playerUsing = player;
		
		NBTTagCompound nbt = playerUsing.getEntityData().getCompoundTag(playerUsing.PERSISTED_NBT_TAG);
		readFromNBT(nbt);
	}
	
	public void readFromNBT(NBTTagCompound nbt){
		if (nbt.hasKey(ModLib.nbtMagicBackpackItems)){
			NBTTagList items = nbt.getTagList(ModLib.nbtMagicBackpackItems, 10);
			for (int i = 0; i < items.tagCount(); i++){
				NBTTagCompound compound = items.getCompoundTagAt(i);
				byte slot = compound.getByte("Slot");
				this.inventory[slot] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.inventory.length; i++){
			if (this.inventory[i] != null){
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(compound);
                items.appendTag(compound);
			}
		}
		nbt.setTag(ModLib.nbtMagicBackpackItems, items);
	}
	
	public void close(){
		NBTTagCompound nbt = playerUsing.getEntityData().getCompoundTag(playerUsing.PERSISTED_NBT_TAG);
		writeToNBT(nbt);
	}
	
	@Override
	public String getName(){
		return "Magic Backpack";
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public IChatComponent getDisplayName(){
		return null;
	}

	@Override
	public int getSizeInventory(){
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index){
		return inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count){
		ItemStack stack = getStackInSlot(index);
		
        if (stack != null){
            if (stack.stackSize <= count){
                setInventorySlotContents(index, null);
            }else{
            	stack = stack.splitStack(count);
                if (stack.stackSize == 0){
                    setInventorySlotContents(index, null);
                }
            }
        }
        
        return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index){
		ItemStack stack = inventory[index];
		inventory[index] = null;
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		inventory[index] = stack;
		
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
		return true;
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
		
	}
}