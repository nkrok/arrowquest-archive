package com.cm8check.arrowquest.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3i;

public class TileEntityPoliceTeleporter extends TileEntity implements IInventory {
	public static final String[] destinationNames = {
		"Overworld",
		"TF-H101 Upper Level",
		"TF-H101 Aircraft Hangar",
		"TF-H101 Lower Garage",
		
		"TF-S101",
		"TF-S102",
		"TF-S103", // final?
		"TF-S104",
		"TF-S105",
		"TF-S106",
		
		"TF-P101 L5 Aircraft Hangar",
		"TF-P101 Level 5",
		"TF-P101 L4 Garage",
		"TF-P101 L3 Aircraft Hangar",
		"TF-P101 L1 Garage",
		"TF-P101 Lower Level"
	};
	
	public static final Vec3i[] destinationCoordinates = {
		null,
		new Vec3i(156, 107, 488),
		new Vec3i(171, 96, 474),
		new Vec3i(177, 82, 539),
		new Vec3i(66, 114, 476),
		new Vec3i(124, 19, 636),
		new Vec3i(209, 103, 587),
		new Vec3i(14, 113, 412),
		new Vec3i(-103, 83, 371),
		new Vec3i(88, 101, 101),
		new Vec3i(34, 84, 117),
		new Vec3i(51, 80, 604),
		new Vec3i(43, 65, 606),
		new Vec3i(65, 56, 264),
		new Vec3i(62, 41, 50),
		new Vec3i(36, 25, 482)
	};
	
	private int destination;
	
	public void setDestination(int destination) {
		this.destination = destination;
		this.markDirty();
	}
	
	public void cycleDestination() {
		this.destination++;
		
		if (this.destination >= destinationNames.length) {
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