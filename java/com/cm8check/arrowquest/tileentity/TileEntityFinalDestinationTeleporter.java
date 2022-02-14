package com.cm8check.arrowquest.tileentity;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.world.ArrowQuestWorldData;
import com.cm8check.arrowquest.world.dimension.TeleporterFinalDestination;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IChatComponent;

public class TileEntityFinalDestinationTeleporter extends TileEntity implements IInventory, IUpdatePlayerListBox{
	private ItemStack inventory;
	
	@Override
	public int getSizeInventory() {
		return 1;
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
	public ItemStack getStackInSlot(int index) {
		return inventory;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.inventory != null)
        {
            ItemStack itemstack;

            if (this.inventory.stackSize <= count)
            {
                itemstack = this.inventory;
                this.inventory = null;
                return itemstack;
            }
            else
            {
                itemstack = this.inventory.splitStack(count);

                if (this.inventory.stackSize == 0)
                {
                    this.inventory = null;
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
		ItemStack itemstack = this.inventory;
        this.inventory = null;
        return itemstack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		this.inventory = stack;
		
		if (!this.worldObj.isRemote && this.worldObj.provider.getDimensionId() != ModLib.dimFinalBossID && this.inventory.getItem() == ModItems.itemFinalDestinationKey){
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(this.pos.getX() - 16, this.pos.getY() - 16, this.pos.getZ() - 16, this.pos.getX() + 16, this.pos.getY() + 16, this.pos.getZ() + 16);
			List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
	            while (iterator.hasNext()){
	            	EntityPlayer player = (EntityPlayer) iterator.next();
	            	EntityPlayerMP playerMP = (EntityPlayerMP) player;
	    			playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, ModLib.dimFinalBossID, new TeleporterFinalDestination(playerMP.mcServer.worldServerForDimension(ModLib.dimFinalBossID)));
	            }
			}
		}
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
		if (stack.getItem() == ModItems.itemFinalDestinationKey){
			return true;
		}
		
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		
		NBTTagCompound nbt = new NBTTagCompound();
		if (this.inventory != null){
			this.inventory.writeToNBT(nbt);
		}
		compound.setTag("Item", nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		
		NBTTagCompound nbt = compound.getCompoundTag("Item");
		if (nbt.hasKey("Count")){
			this.inventory = ItemStack.loadItemStackFromNBT(nbt);
		}
	}
	
	@Override
	public void update(){
		if (ArrowQuestWorldData.finalBossDefeated && !ArrowQuest.DEV_MODE){
			this.worldObj.setBlockToAir(this.pos);
			this.worldObj.removeTileEntity(this.pos);
		}
	}
}