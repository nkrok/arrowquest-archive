package com.cm8check.arrowquest.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemHammer extends ItemSword{
	private float attackDamage;
	private final ToolMaterial material;
	
	public ItemHammer(ToolMaterial material, String name){
		super(material);
		this.attackDamage = 5.0F + material.getDamageVsEntity();
		this.material = material;
		this.setUnlocalizedName(name);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
		float k = 0.25F + material.getDamageVsEntity()*0.25F;
		target.addVelocity((double)(-MathHelper.sin(attacker.rotationYaw * (float)Math.PI / 180.0F) * k), 0.1D, (double)(MathHelper.cos(attacker.rotationYaw * (float)Math.PI / 180.0F) * k));
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
}