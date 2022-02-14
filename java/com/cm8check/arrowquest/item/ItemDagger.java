package com.cm8check.arrowquest.item;

import com.cm8check.arrowquest.entity.EntityThrownDagger;
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
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemDagger extends ItemSword{
	private float attackDamage;
	private ToolMaterial material;
	public static ToolMaterial tempThrownDaggerMaterial = ToolMaterial.WOOD;
	
	public ItemDagger(ToolMaterial material, String name){
		super(material);
		this.material = material;
		this.attackDamage = 2.0F + material.getDamageVsEntity();
		this.setUnlocalizedName(name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
		if (!world.isRemote){
			Vec3 look = player.getLook(1.0F);
			EntityThrownDagger entity = new EntityThrownDagger(world, player.posX, player.posY + player.getEyeHeight(), player.posZ, look.xCoord, look.yCoord, look.zCoord, player);
			entity.meta = itemstack.getMetadata()+1;
			entity.setMaterial(this.material);
			world.spawnEntityInWorld(entity);
			
			world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndDaggerThrow", 1, 1);
		}else{
			tempThrownDaggerMaterial = this.material;
		}
		
		itemstack.stackSize--;
		return itemstack;
	}
	
	@Override
	public Multimap getItemAttributeModifiers(){
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double)this.attackDamage, 0));
		return multimap;
	}
}