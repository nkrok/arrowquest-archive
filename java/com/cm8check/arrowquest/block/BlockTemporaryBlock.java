package com.cm8check.arrowquest.block;

import java.util.Random;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTemporaryBlock extends Block{
	protected BlockTemporaryBlock(){
		super(Material.glass);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setUnlocalizedName(ModLib.blockTemporaryBlockName);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public EnumWorldBlockLayer getBlockLayer(){
        return EnumWorldBlockLayer.CUTOUT;
    }
	
	@Override
	public boolean isFullCube(){
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (!world.isRemote){
			world.scheduleUpdate(pos, this, 120);
		}
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
		if (!world.isRemote){
			world.setBlockToAir(pos);
		}
	}
}