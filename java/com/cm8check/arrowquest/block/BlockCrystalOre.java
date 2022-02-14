package com.cm8check.arrowquest.block;

import java.util.Random;

import com.cm8check.arrowquest.item.ItemMagicCrystal;
import com.cm8check.arrowquest.item.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockCrystalOre extends Block{
	private final int type;
	
	protected BlockCrystalOre(int type, String name){
		super(Material.rock);
		this.type = type;
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 2);
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune){
		return ModItems.itemMagicCrystal;
	}
	
	@Override
	public int damageDropped(IBlockState state){
		return this.type;
	}
	
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random){
		if (fortune > 0){
			int j = random.nextInt(fortune + 2) - 1;
            if (j < 0){
                j = 0;
            }
            
            return j+1;
		}
		
		return 1;
	}
}