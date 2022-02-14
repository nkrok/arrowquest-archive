package com.cm8check.arrowquest.block;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockBigCraftingTable extends Block{
	protected BlockBigCraftingTable(){
		super(Material.wood);
		this.setUnlocalizedName(ModLib.blockBigCraftingTableName);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setHardness(3.0F);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (!world.isRemote){
			if (!player.isSneaking()){
				player.openGui(ArrowQuest.instance, ModLib.guiBigCraftingTableID, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		
		return true;
	}
}