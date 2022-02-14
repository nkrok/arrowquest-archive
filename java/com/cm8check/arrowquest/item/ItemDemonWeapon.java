package com.cm8check.arrowquest.item;

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
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDemonWeapon extends ItemSword{
	private float attackDamage;
	private boolean canBlock;
	
	public ItemDemonWeapon(String name, float damage, boolean canBlock) {
		super(Item.ToolMaterial.IRON);
		this.attackDamage = damage;
		this.canBlock = canBlock;
		this.setUnlocalizedName(name);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
		if (!(attacker instanceof EntityPlayer))
			return super.hitEntity(stack, target, attacker);
		
		int effect = itemRand.nextInt(5);
		
		EntityLivingBase inflictedEntity = target;
		if (itemRand.nextInt(8) == 0) {
			inflictedEntity = attacker;
		}
		
		switch (effect) {
		case 0:
			inflictedEntity.addPotionEffect(new PotionEffect(19, 100, 1, false, true));
			break;
			
		case 1:
			inflictedEntity.addPotionEffect(new PotionEffect(2, 100, 1, false, true));
			break;
			
		case 2:
			inflictedEntity.addPotionEffect(new PotionEffect(4, 60, 0, false, true));
			break;
			
		case 3:
			inflictedEntity.addPotionEffect(new PotionEffect(15, 40, 0, false, true));
			break;
			
		case 4:
			inflictedEntity.addPotionEffect(new PotionEffect(18, 80, 1, false, true));
			break;
		}
		
		return super.hitEntity(stack, target, attacker);
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