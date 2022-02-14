package com.cm8check.arrowquest.item;

import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.client.gui.GuiHudOverlay;
import com.cm8check.arrowquest.entity.ArrowQuestEntityHelper;
import com.cm8check.arrowquest.entity.EntityDoomGuardian;
import com.cm8check.arrowquest.entity.EntityGenericProjectile;
import com.cm8check.arrowquest.entity.EntityObsidianWarrior;
import com.cm8check.arrowquest.entity.EntityPhantasm;
import com.cm8check.arrowquest.entity.EntityPolice;
import com.cm8check.arrowquest.entity.EntityPoliceBoss;
import com.cm8check.arrowquest.entity.EntityPoliceMissile;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityDungeonChest;
import com.cm8check.arrowquest.tileentity.TileEntityStructureController;
import com.cm8check.arrowquest.world.gen.WorldGenSchematics;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemDebugTool extends Item {
	protected ItemDebugTool() {
		this.setUnlocalizedName(ModLib.itemDebugToolName);
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		
		if (!world.isRemote) {
			//GuiHudOverlay.createGuiPopup("Test popup", "This is a test popup.");
			
			//ArrowQuestEntityHelper.spawnPoliceSquad(world, player);
		}
		

		return itemstack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			for (int i = 0; i < 3; i++) {
				BlockPos blockpos = pos.up().north(i*2);
				world.setBlockState(blockpos, ModBlocks.blockDungeonChest.getDefaultState());
				
				TileEntityDungeonChest tile = (TileEntityDungeonChest) world.getTileEntity(blockpos);
				tile.chestType = 3;
				tile.chestGrade = i;
			}
		}

		return false;
	}
}