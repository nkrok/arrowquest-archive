package com.cm8check.arrowquest.block;

import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityFinalDestinationCheckpoint;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFinalDestinationCheckpoint extends BlockContainer{
	protected BlockFinalDestinationCheckpoint(){
		super(Material.rock);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setUnlocalizedName(ModLib.blockFinalDestinationCheckpointName);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFinalDestinationCheckpoint();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (!world.isRemote){
			if (!player.isSneaking()){
				player.openGui(ArrowQuest.instance, ModLib.guiFinalDestinationCheckpointID, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof TileEntityFinalDestinationCheckpoint){
            InventoryHelper.dropInventoryItems(world, pos, (TileEntityFinalDestinationCheckpoint) tileentity);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }
	
	@Override
	public int getRenderType(){
        return 3;
    }
}