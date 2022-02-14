package com.cm8check.arrowquest.block;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityPoliceTeleporter;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockPoliceTeleporter extends BlockContainer {
	protected BlockPoliceTeleporter() {
		super(Material.rock);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setUnlocalizedName(ModLib.blockPoliceTeleporterName);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (!player.isSneaking()) {
				player.openGui(ArrowQuest.instance, ModLib.guiPoliceTeleporterID, world, pos.getX(), pos.getY(),
						pos.getZ());
			}
		}

		return true;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPoliceTeleporter();
	}
}