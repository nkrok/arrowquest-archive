package com.cm8check.arrowquest.block;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.keybind.KeyBindings;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityDungeonChest;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDungeonChest extends BlockContainer{
	private static boolean reverseEdit;
	
	private final String[] chestNames = {
		"Weapons", "Food", "PoliceChest", "ExplodingChest", "KeyChest"
	};
	
	protected BlockDungeonChest(){
		super(Material.wood);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setUnlocalizedName(ModLib.blockDungeonChestName);
		if (ArrowQuest.DEV_MODE){
			this.setCreativeTab(CreativeTabs.tabMisc);
		}
	}
	
	@SuppressWarnings("unused")
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (ArrowQuest.DEV_MODE && world.isRemote){
			if (KeyBindings.cycleWandSpell.isKeyDown()){
				reverseEdit = true;
			}else{
				reverseEdit = false;
			}
		}
		else if (!world.isRemote) {
			TileEntityDungeonChest tile = (TileEntityDungeonChest) world.getTileEntity(pos);
			if (tile != null && player.capabilities.isCreativeMode){
				if (player.isSneaking()){
					tile.chestGrade += 1;
					if (tile.chestGrade >= 3){
						tile.chestGrade = 0;
					}
					player.addChatMessage(new ChatComponentText("ChestType: " + chestNames[tile.chestType] + ", ChestGrade: " + tile.chestGrade));
					ArrowQuest.savedChestType = tile.chestType;
					ArrowQuest.savedChestGrade = tile.chestGrade;
				}else{
					if (reverseEdit) {
						tile.chestType -= 1;
						if (tile.chestType < 0) {
							tile.chestType = chestNames.length-1;
						}
					}
					else {
						tile.chestType += 1;
						if (tile.chestType >= chestNames.length){
							tile.chestType = 0;
						}
					}
					
					player.addChatMessage(new ChatComponentText("ChestType: " + chestNames[tile.chestType] + ", ChestGrade: " + tile.chestGrade));
					ArrowQuest.savedChestType = tile.chestType;
					ArrowQuest.savedChestGrade = tile.chestGrade;
				}
			}
		}
		return false;
	}
	
	@Override
	public int getRenderType(){
		return 3;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new TileEntityDungeonChest();
	}
	
	@SuppressWarnings("unused")
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (ArrowQuest.DEV_MODE && !world.isRemote){
			TileEntityDungeonChest tile = (TileEntityDungeonChest) world.getTileEntity(pos);
			if (tile != null){
				tile.chestType = ArrowQuest.savedChestType;
				tile.chestGrade = ArrowQuest.savedChestGrade;
				if (Minecraft.getMinecraft().thePlayer != null){
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("ChestType: " + chestNames[tile.chestType] + ", ChestGrade: " + tile.chestGrade));
				}
			}
		}
	}
}