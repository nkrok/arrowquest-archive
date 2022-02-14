package com.cm8check.arrowquest.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;

public class TileEntityFinalDestinationFinalCheckpoint extends TileEntity implements IInventory {
	private int destination;
	
	public void setDestination(int destination) {
		this.destination = destination;
	}
	
	public void cycleDestination() {
		this.destination++;
		
		if (this.destination >= 1) {
			this.destination = 0;
		}
		
		this.markDirty();
	}
	
	public int getDestination() {
		return this.destination;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setByte("destination", (byte) this.destination);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.destination = compound.getByte("destination");
	}

	@Override
	public String getName() {
		return "Teleporter";
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
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}

	@Override
	public int getField(int id) {
		if (id == 0) {
			return this.destination;
		}
		
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		if (id == 0) {
			this.destination = value;
		}
	}

	@Override
	public int getFieldCount() {
		return 1;
	}

	@Override
	public void clear() {}
}