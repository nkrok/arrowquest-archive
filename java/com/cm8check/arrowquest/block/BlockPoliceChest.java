package com.cm8check.arrowquest.block;

import java.util.Random;

import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityPoliceChest;

import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPoliceChest extends BlockChest{
	protected BlockPoliceChest(){
		super(0);
		this.setUnlocalizedName(ModLib.blockPoliceChestName);
		this.setHardness(2.5F);
		this.setStepSound(soundTypeWood);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta){
        return new TileEntityPoliceChest();
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(Blocks.chest);
    }
	
	@SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos){
        return Item.getItemFromBlock(Blocks.chest);
    }
}