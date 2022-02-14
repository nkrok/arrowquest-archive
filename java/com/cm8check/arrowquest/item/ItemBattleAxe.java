package com.cm8check.arrowquest.item;

import java.util.Set;

import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.world.dimension.TeleporterPoliceDimension;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemBattleAxe extends ItemSword{
	private float attackDamage;
	private float efficiencyOnProperMaterial;
	private static final Set effectiveBlocks = Sets.newHashSet(new Block[] {Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder});
	
	public ItemBattleAxe(ToolMaterial material, String name){
		super(material);
		this.attackDamage = 7.0F + material.getDamageVsEntity();
		this.efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
		this.setUnlocalizedName(name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
		return itemstack;
	}
	
	@Override
	public Multimap getItemAttributeModifiers(){
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double)this.attackDamage, 0));
		return multimap;
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, Block block){
		if (block.getMaterial() != Material.wood && block.getMaterial() != Material.plants && block.getMaterial() != Material.vine){
			return this.effectiveBlocks.contains(block) ? this.efficiencyOnProperMaterial : 1.0F;
		}else{
			return this.efficiencyOnProperMaterial;
		}
    }
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn){
        if (blockIn.getBlockHardness(worldIn, pos) != 0.0F){
            stack.damageItem(1, playerIn);
        }

        return true;
    }
	
	@Override
	public boolean canHarvestBlock(Block blockIn){
		return false;
	}
}