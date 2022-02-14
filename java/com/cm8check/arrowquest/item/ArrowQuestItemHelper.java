package com.cm8check.arrowquest.item;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ArrowQuestItemHelper {
	protected static void checkArtifactItemAcquired(Item item, World world, Entity entity) {
		if (!world.isRemote && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
			if (!nbt.hasKey("AQFound_" + item.getUnlocalizedName())) {
				nbt.setBoolean("AQFound_" + item.getUnlocalizedName(), true);
				nbt.setByte(ModLib.nbtArtifactWeaponsAcquired, (byte) (nbt.getByte(ModLib.nbtArtifactWeaponsAcquired) + 1));
			}
		}
	}
}
