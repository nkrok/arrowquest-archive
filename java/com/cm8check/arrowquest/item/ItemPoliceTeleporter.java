package com.cm8check.arrowquest.item;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketGuiPopup;
import com.cm8check.arrowquest.world.dimension.TeleporterPoliceDimension;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemPoliceTeleporter extends Item{
	public ItemPoliceTeleporter(){
		this.setUnlocalizedName(ModLib.itemPoliceTeleporterName);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setMaxDamage(1);
		this.maxStackSize = 1;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		if (!world.isRemote && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
			
			if (nbt.getByte(ModLib.nbtObjective) == 2 && !nbt.hasKey(ModLib.nbtTPCTeleporterFound)) {
				nbt.setBoolean(ModLib.nbtTPCTeleporterFound, true);
				nbt.setByte(ModLib.nbtObjective, (byte) 3);
				
				ArrowQuest.packetPipeline.sendTo(new PacketGuiPopup(0), (EntityPlayerMP) player);
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer playerIn){
		if (!world.isRemote){
			/*
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(playerIn.posX - 16, playerIn.posY - 16, playerIn.posZ - 16, playerIn.posX + 16, playerIn.posY + 16, playerIn.posZ + 16);
			List list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
	            while (iterator.hasNext()){
	            	EntityPlayer player = (EntityPlayer) iterator.next();
	            	EntityPlayerMP playerMP = (EntityPlayerMP) player;
	    			if (player.dimension != ModLib.dimPoliceBaseID){
	    				playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, ModLib.dimPoliceBaseID, new TeleporterPoliceDimension(playerMP.mcServer.worldServerForDimension(ModLib.dimPoliceBaseID)));
	    			}else{
	    				playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, 0, new TeleporterPoliceDimension(playerMP.mcServer.worldServerForDimension(0)));
	    			}
	            }
			}
			*/
			
			EntityPlayerMP playerMP = (EntityPlayerMP) playerIn;
			
			if (playerMP.dimension != ModLib.dimPoliceBaseID) {
				playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, ModLib.dimPoliceBaseID, new TeleporterPoliceDimension(playerMP.mcServer.worldServerForDimension(ModLib.dimPoliceBaseID)));
			}
			else {
				playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, 0, new TeleporterPoliceDimension(playerMP.mcServer.worldServerForDimension(0)));
			}
			
			itemstack.damageItem(1, playerIn);
		}
		
		return itemstack;
	}
}