package com.cm8check.arrowquest.item;

import com.cm8check.arrowquest.entity.ArrowQuestEntityHelper;
import com.cm8check.arrowquest.lib.ModLib;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class ItemThiefsDagger extends ItemSword{
	private float attackDamage;
	
	public ItemThiefsDagger() {
		super(Item.ToolMaterial.IRON);
		this.attackDamage = 6.0F;
		this.setMaxDamage(564);
		this.setUnlocalizedName(ModLib.itemThiefsDaggerName);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (!ArrowQuestEntityHelper.isEntityBoss(target.getClass()) && target.getHeldItem() != null && itemRand.nextInt(10) == 0) {
			attacker.entityDropItem(target.getHeldItem(), 1.0F).setPickupDelay(1);
			target.setCurrentItemOrArmor(0, null);
		}
		
		return super.hitEntity(stack, target, attacker);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn){
		return itemStackIn;
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