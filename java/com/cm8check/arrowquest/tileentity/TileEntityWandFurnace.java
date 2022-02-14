package com.cm8check.arrowquest.tileentity;

import com.cm8check.arrowquest.item.ItemWand;
import com.cm8check.arrowquest.item.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

public class TileEntityWandFurnace extends TileEntity implements IUpdatePlayerListBox, ISidedInventory{
	private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {1};
    private static final int[] slotsSides = new int[] {0};
	
	private ItemStack[] inventory = new ItemStack[2];
	public int chargeLeft;
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.inventory[index] != null)
        {
            ItemStack itemstack;

            if (this.inventory[index].stackSize <= count)
            {
                itemstack = this.inventory[index];
                this.inventory[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.inventory[index].splitStack(count);

                if (this.inventory[index].stackSize == 0)
                {
                    this.inventory[index] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index){
		ItemStack itemstack = this.inventory[index];
        this.inventory[index] = null;
        return itemstack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		inventory[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player){}

	@Override
	public void closeInventory(EntityPlayer player){}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 1 && stack.getItem() != ModItems.itemMagicCrystal){
			return false;
		}
		return true;
	}

	@Override
	public int getField(int id){
		switch (id){
            case 0:
                return this.chargeLeft;
            default:
                return 0;
        }
	}

	@Override
	public void setField(int id, int value){
		switch (id){
            case 0:
                this.chargeLeft = value;
            break;
        }
	}

	@Override
	public int getFieldCount(){
		return 1;
	}

	@Override
	public void clear(){
		for (int i = 0; i < this.inventory.length; ++i){
            this.inventory[i] = null;
        }
	}

	@Override
	public String getName() {
		return "Caster Furnace";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1){
			return false;
		}
		return true;
	}

	@Override
	public void update(){
		if (!worldObj.isRemote){
			boolean dirty = false;
			
			if (inventory[1] != null && inventory[1].getItem() == ModItems.itemMagicCrystal && this.chargeLeft <= 5940){
				boolean flag = false;
				
				int meta = inventory[1].getMetadata();
				if (meta == 0 && this.chargeLeft <= 5700){
					this.chargeLeft += 300;
					flag = true;
				}else if (meta > 0 && this.chargeLeft <= 5940){
					this.chargeLeft += 60;
					flag = true;
				}
				
				if (flag){
					this.decrStackSize(1, 1);
					dirty = true;
				}
			}
			
			if (inventory[0] != null && this.chargeLeft > 0 && inventory[0].getItem() instanceof ItemWand){
				int damage = inventory[0].getMetadata();
				if (damage > 0){
					int chargeUsed = Math.min(4, damage);
					if (this.chargeLeft >= chargeUsed){
						inventory[0].setItemDamage(damage - chargeUsed);
						if (inventory[0].getItem() == ModItems.itemWand){
							chargeUsed *= 2;
						}
						this.chargeLeft -= chargeUsed;
						if (this.chargeLeft < 0){
							this.chargeLeft = 0;
						}
						dirty = true;
					}
				}
			}
			
			if (dirty){
				this.markDirty();
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		this.chargeLeft = compound.getShort("ChargeLeft");
		
		NBTTagList taglist = compound.getTagList("Items", 10);
		this.inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < taglist.tagCount(); i++){
            NBTTagCompound nbt = taglist.getCompoundTagAt(i);
            byte slot = nbt.getByte("Slot");

            if (slot >= 0 && slot < this.inventory.length){
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(nbt);
            }
        }
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		compound.setShort("ChargeLeft", (short) this.chargeLeft);
		
		NBTTagList taglist = new NBTTagList();
        for (int i = 0; i < this.inventory.length; i++){
            if (this.inventory[i] != null){
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbt);
                taglist.appendTag(nbt);
            }
        }
        compound.setTag("Items", taglist);
	}
}