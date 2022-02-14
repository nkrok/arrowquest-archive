package com.cm8check.arrowquest.tileentity;

import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.entity.EntityPolice;
import com.cm8check.arrowquest.entity.EntityPoliceBoss;
import com.cm8check.arrowquest.item.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;

public class TileEntityAirlockController extends TileEntity implements IUpdatePlayerListBox, IInventory {
	private ForgeChunkManager.Ticket ticket;
	
	private boolean enabled;
	private boolean airlockOpen;
	
	public void setOpen(boolean open) {
		this.airlockOpen = open;
		
		if (open && !this.worldObj.isRemote) {
			for (int x = 147; x <= 167; x++)
			for (int y = 94; y <= 101; y++)
				this.worldObj.setBlockToAir(new BlockPos(x, y, 546));
		}
	}
	
	public boolean isOpen() {
		return this.airlockOpen;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean flag) {
		this.enabled = flag;
	}

	@Override
	public void update() {
		if (this.airlockOpen && !this.worldObj.isRemote) {
			List<EntityLivingBase> list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(146, 93, 232, 168, 101, 550));
			for (EntityLivingBase entity : list) {
				if (entity instanceof EntityPoliceBoss) {
					((EntityPoliceBoss) entity).setInAirlock(true);
				}
				else if (entity instanceof EntityPolice) {
					((EntityPolice) entity).setInAirlock(true);
				}
				
				if (entity.getHeldItem() != null && entity.getHeldItem().getItem() == ModItems.itemDebugTool)
					continue;
				
				double velocityY = 0;
				
				if (entity.posZ > 400) {
					velocityY = 0.07;
				}
				
				double velocityZ = 0.1 + (entity.posZ - 232) / 64;
				
				if (velocityZ > 4) {
					velocityZ = 4;
				}
				
				if (entity instanceof EntityPlayer) {
					velocityZ -= 0.1;
					if (velocityZ > 2) {
						velocityZ = 2;
					}
					
					entity.addVelocity(0, velocityY, velocityZ / 4);
					((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity));
				}
				else {
					entity.addVelocity(0, velocityY, velocityZ / 2);
				}
			}
		}
	}
	
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("open", this.airlockOpen);
		compound.setBoolean("enabled", this.enabled);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.airlockOpen = compound.getBoolean("open");
		this.enabled = compound.getBoolean("enabled");
	}
	
	@Override
	public void validate() {
		super.validate();
		if (!worldObj.isRemote && ticket == null) {
			ticket = ForgeChunkManager.requestTicket(ArrowQuest.instance, this.worldObj, ForgeChunkManager.Type.NORMAL);
			ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(this.getPos().getX() / 16, this.getPos().getZ() / 16));
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		ForgeChunkManager.releaseTicket(ticket);
	}
	
	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.airlockOpen ? 1 : 0;
			
		case 1:
			return this.enabled ? 1 : 0;
		}
		
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.airlockOpen = (value == 1);
			break;
			
		case 1:
			this.enabled = (value == 1);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public String getName() {
		return "Airlock Controller";
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
	public void clear() {}
}