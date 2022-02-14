package com.cm8check.arrowquest.item;

import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPoliceSabre extends ItemSword{
	private float attackDamage;
	private static Random rand = ArrowQuest.RAND;
	
	public ItemPoliceSabre(){
		super(ToolMaterial.EMERALD);
		this.setMaxDamage(0);
		this.attackDamage = 12.0F;
		this.setUnlocalizedName(ModLib.itemPoliceSabreName);
	}
	
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack){
		if (!entityLiving.worldObj.isRemote){
			entityLiving.worldObj.playSoundEffect(entityLiving.posX, entityLiving.posY, entityLiving.posZ, "arrowquest:sndSabreSwing", 1, 0.95F+(0.05F*rand.nextFloat()));
		}
		return false;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
		attacker.worldObj.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndSabreHit", 1, 1);
        return true;
    }
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn){
        return true;
    }
	
	@Override
	public Multimap getItemAttributeModifiers(){
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double)this.attackDamage, 0));
		return multimap;
	}
}