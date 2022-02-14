package com.cm8check.arrowquest.tileentity;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.entity.EntityDoomGuardian;
import com.cm8check.arrowquest.entity.EntityFinalBoss;
import com.cm8check.arrowquest.entity.EntityFinalBossCore;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketDimensionMusicUpdate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class TileEntityFinalDestinationCheckpoint extends TileEntity implements IInventory{
	private ItemStack[] inventory = new ItemStack[5];
	
	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public String getName() {
		return "Checkpoint Teleporter";
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
		this.inventory[index] = stack;
		
		if (!this.worldObj.isRemote && this.worldObj.provider.getDimensionId() == ModLib.dimFinalBossID){
			int keyCount = 0;
			
			for (int i = 0; i < 5; i++){
				if (this.inventory[i] != null && this.inventory[i].getItem() == ModItems.itemKey){
					keyCount++;
				}
			}
			
			if (keyCount == 5){
				Object[] entities = this.worldObj.loadedEntityList.toArray();
				if (entities.length > 0){
					for (int i = 0; i < entities.length; i++){
						Entity entity = (Entity) entities[i];
						if (entity instanceof EntityMob){
							if (entity instanceof EntityDoomGuardian || entity instanceof EntityFinalBoss || entity instanceof EntityFinalBossCore){
								continue;
							}
							entity.setDead();
						}
					}
				}
				
				ArrowQuest.packetPipeline.sendToDimension(new PacketDimensionMusicUpdate(51, 0), ModLib.dimFinalBossID);
				
				List list = this.worldObj.playerEntities;
				if (list != null && !list.isEmpty()){
					Iterator iterator = list.iterator();
		            while (iterator.hasNext()){
		            	EntityPlayer player = (EntityPlayer) iterator.next();
		            	player.setPositionAndUpdate(57.5D, 104.0D, 15.5D);
		            	player.setSpawnChunk(new BlockPos(57, 104, 15), true, ModLib.dimFinalBossID);
		            	
		            	NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
		        		nbt.setByte(ModLib.nbtPlayerDimMusic, (byte) 51);
		            }
				}
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
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
		if (stack.getItem() == ModItems.itemKey){
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
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		
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
}