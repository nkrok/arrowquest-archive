package com.cm8check.arrowquest.item;

import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketGuiPopup;
import com.cm8check.arrowquest.tileentity.TileEntityFinalDestinationReturn;
import com.cm8check.arrowquest.world.ArrowQuestWorldData;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTheArrow extends Item{
	public ItemTheArrow(){
		this.setUnlocalizedName(ModLib.itemTheArrowName);
		this.setCreativeTab(CreativeTabs.tabCombat);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(ItemStack stack){
		return true;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
		if (!world.isRemote && world.provider.getDimensionId() == ModLib.dimFinalBossID && !TileEntityFinalDestinationReturn.finalBossDefeated){
			TileEntityFinalDestinationReturn.startDimensionCollapse();
		}
	}
}