package com.cm8check.arrowquest.inventory;

import com.cm8check.arrowquest.item.ItemWand;
import com.cm8check.arrowquest.item.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;

public class InventoryWand implements IInventory{
	protected ItemStack[] inventory = new ItemStack[8];
	private ItemStack itemstack;
	public int size;
	private String name;
	private EntityPlayer playerUsing;
	
	public InventoryWand(EntityPlayer player, ItemStack stack){
		this.playerUsing = player;
		this.itemstack = stack;
		name = "Staff";
		if (stack.getItem() == ModItems.itemWand){
			size = 2;
			name = "Wand";
		}else if (stack.getItem() == ModItems.itemStaff1){
			size = 4;
		}else if (stack.getItem() == ModItems.itemStaff2){
			size = 6;
		}else if (stack.getItem() == ModItems.itemStaff3){
			size = 8;
		}
		
		NBTTagCompound nbt = this.itemstack.getTagCompound();
		if (nbt == null){
			nbt = new NBTTagCompound();
			this.itemstack.setTagCompound(nbt);
		}
		nbt.setBoolean("WandOpened", true);
		nbt.setByte("ActiveSpell", (byte) 0);
		nbt.setByte("ActiveSlot", (byte) -1);
		readFromNBT(nbt);
	}
	
	public void readFromNBT(NBTTagCompound nbt){
		NBTTagList items = nbt.getTagList("Items", 10);
		for (int i = 0; i < items.tagCount(); i++){
			NBTTagCompound compound = items.getCompoundTagAt(i);
			byte slot = compound.getByte("Slot");
			this.inventory[slot] = ItemStack.loadItemStackFromNBT(compound);
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.inventory.length; i++){
			if (this.inventory[i] != null){
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(compound);
                items.appendTag(compound);
			}
		}
		nbt.setTag("Items", items);
	}
	
	public void close(){
		Item item = itemstack.getItem();
		this.itemstack = null;
		for (int i = 0; i < 36; i++){
			ItemStack stack = playerUsing.inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() == item && stack.hasTagCompound() && stack.getTagCompound().getBoolean("WandOpened")){
				this.itemstack = stack;
				break;
			}
		}
		
		if (this.itemstack != null){
			NBTTagCompound nbt = this.itemstack.getTagCompound();
			writeToNBT(nbt);
			nbt.setBoolean("WandOpened", false);
			
			ItemWand itemWand = (ItemWand) item;
			itemWand.cycleSpell(this.itemstack);
		}else{
			System.out.println("Error finding an opened wand to save to: New wand contents may have been lost!");
		}
	}
	
	@Override
	public String getName(){
		return this.name;
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
        if (stack != null)
        {
            if (stack.stackSize <= count)
            {
                setInventorySlotContents(index, null);
            }
            else
            {
            	stack = stack.splitStack(count);
                if (stack.stackSize == 0)
                {
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
		if (index < size && stack.getItem() == ModItems.itemScroll && stack.getMetadata() > 0){
			return true;
		}
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
		
	}
	
}