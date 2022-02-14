package com.cm8check.arrowquest.block;

import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.keybind.KeyBindings;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntitySingleSpawner;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSingleSpawner extends BlockContainer{
	private static boolean reverseEdit;
	
	protected BlockSingleSpawner(){
		super(Material.glass);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setUnlocalizedName(ModLib.blockSingleSpawnerName);
		if (ArrowQuest.DEV_MODE){
			this.setCreativeTab(CreativeTabs.tabMisc);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (ArrowQuest.DEV_MODE && world.isRemote){
			if (KeyBindings.cycleWandSpell.isKeyDown()){
				reverseEdit = true;
			}else{
				reverseEdit = false;
			}
		}else if (!world.isRemote){
			TileEntitySingleSpawner tile = (TileEntitySingleSpawner) world.getTileEntity(pos);
			if (tile != null && player.capabilities.isCreativeMode){
				if (player.isSneaking()){
					tile.mobLevel += 1;
					if (tile.mobLevel >= 3){
						tile.mobLevel = 0;
					}
					player.addChatMessage(new ChatComponentText("Mob: " + tile.mobNames[tile.mob] + ", MobLevel: " + tile.mobLevel));
					ArrowQuest.savedMobType = tile.mob;
					ArrowQuest.savedMobLevel = tile.mobLevel;
				}else{
					if (reverseEdit){
						tile.mob -= 1;
						if (tile.mob < 0){
							tile.mob = tile.mobNames.length-1;
						}
					}else{
						tile.mob += 1;
						if (tile.mob >= tile.mobNames.length){
							tile.mob = 0;
						}
					}
					player.addChatMessage(new ChatComponentText("Mob: " + tile.mobNames[tile.mob] + ", MobLevel: " + tile.mobLevel));
					ArrowQuest.savedMobType = tile.mob;
					ArrowQuest.savedMobLevel = tile.mobLevel;
				}
			}
		}
		
		return false;
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
	public int getRenderType(){
		return 0;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new TileEntitySingleSpawner();
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (ArrowQuest.DEV_MODE && !world.isRemote){
			TileEntitySingleSpawner tile = (TileEntitySingleSpawner) world.getTileEntity(pos);
			if (tile != null){
				tile.mob = ArrowQuest.savedMobType;
				tile.mobLevel = ArrowQuest.savedMobLevel;
				if (Minecraft.getMinecraft().thePlayer != null){
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Mob: " + tile.mobNames[tile.mob] + ", MobLevel: " + tile.mobLevel));
				}
			}
		}
	}
}