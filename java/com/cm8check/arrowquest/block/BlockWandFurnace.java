package com.cm8check.arrowquest.block;

import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityWandFurnace;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWandFurnace extends BlockContainer{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	protected BlockWandFurnace(){
		super(Material.iron);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 2);
		this.setUnlocalizedName(ModLib.blockWandFurnaceName);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new TileEntityWandFurnace();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (!world.isRemote){
			if (!player.isSneaking()){
				player.openGui(ArrowQuest.instance, ModLib.guiWandFurnaceID, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		
		return true;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (!world.isRemote){
            Block block = world.getBlockState(pos.north()).getBlock();
            Block block1 = world.getBlockState(pos.south()).getBlock();
            Block block2 = world.getBlockState(pos.west()).getBlock();
            Block block3 = world.getBlockState(pos.east()).getBlock();
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            world.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
        TileEntity tileentity = world.getTileEntity(pos);
        
        if (tileentity instanceof TileEntityWandFurnace){
            InventoryHelper.dropInventoryItems(world, pos, (TileEntityWandFurnace) tileentity);
            world.updateComparatorOutputLevel(pos, this);
            
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setShort("ChargeLeft", (short) ((TileEntityWandFurnace) tileentity).chargeLeft);
            
            ItemStack stack = new ItemStack(this);
            stack.setTagCompound(nbt);
            spawnAsEntity(world, pos, stack);
        }
        
        super.breakBlock(world, pos, state);
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		
		if (stack.hasTagCompound()) {
			TileEntity tile = world.getTileEntity(pos);
			
			if (tile instanceof TileEntityWandFurnace) {
				((TileEntityWandFurnace) tile).chargeLeft = stack.getTagCompound().getShort("ChargeLeft");
			}
		}
	}
	
	@Override
	public int getRenderType(){
        return 3;
    }
	
	@SideOnly(Side.CLIENT)
    public IBlockState getStateForEntityRender(IBlockState state){
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta){
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

	@Override
    public int getMetaFromState(IBlockState state){
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }
    
    @Override
    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[] {FACING});
    }

    @SideOnly(Side.CLIENT)
    static final class SwitchEnumFacing{
        static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];
        static{
            try
            {
                FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}