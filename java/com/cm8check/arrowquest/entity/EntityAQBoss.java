package com.cm8check.arrowquest.entity;

import com.cm8check.arrowquest.tileentity.TileEntityStructureController;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntityAQBoss extends EntityRaceBase {
	private BlockPos structureControllerLocation;
	
	public EntityAQBoss(World world) {
		super(world);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);
		
		if (this.structureControllerLocation != null) {
			tagCompound.setIntArray("StructureControllerLocation", new int[] {
					structureControllerLocation.getX(),
					structureControllerLocation.getY(),
					structureControllerLocation.getZ()
			});
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		super.readEntityFromNBT(tagCompound);
		
		if (tagCompound.hasKey("StructureControllerLocation")) {
			int[] coords = tagCompound.getIntArray("StructureControllerLocation");
			this.structureControllerLocation = new BlockPos(coords[0], coords[1], coords[2]);
		}
	}
	
	@Override
	public void setDead() {
		super.setDead();
		
		if (this.structureControllerLocation != null) {
			TileEntity tile = this.worldObj.getTileEntity(this.structureControllerLocation);
			if (tile instanceof TileEntityStructureController) {
				((TileEntityStructureController) tile).structureCleared();
			}
		}
	}
	
	public void setStructureControllerLocation(BlockPos pos) {
		this.structureControllerLocation = pos;
	}
}