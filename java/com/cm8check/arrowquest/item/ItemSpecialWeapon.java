package com.cm8check.arrowquest.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class ItemSpecialWeapon extends ItemSword{
	private float attackDamage;
	private boolean canBlock;
	
	public ItemSpecialWeapon(String name, float damage, boolean canBlock) {
		super(Item.ToolMaterial.IRON);
		this.attackDamage = damage;
		this.canBlock = canBlock;
		this.setUnlocalizedName(name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn){
		if (!canBlock) {
			return itemStackIn;
		}
		
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
	
	@Override
	public Multimap getItemAttributeModifiers(){
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double)this.attackDamage, 0));
		return multimap;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}
}