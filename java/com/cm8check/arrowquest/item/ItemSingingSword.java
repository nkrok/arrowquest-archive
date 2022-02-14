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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSingingSword extends ItemSword{
	private float attackDamage;
	
	public static final int MAX_CHARGE = 100;
	
	public ItemSingingSword() {
		super(Item.ToolMaterial.EMERALD);
		this.attackDamage = 8.0F;
		this.setMaxDamage(1126);
		this.setUnlocalizedName(ModLib.itemSingingSwordName);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
		if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("Charged")) {
			stack.getTagCompound().setBoolean("Charged", false);
			
			float dmg;
			
			if (ArrowQuestEntityHelper.isEntityBoss(target.getClass())) {
				dmg = 40.0F;
			}
			else {
				dmg = 130.0F;
			}
			
			target.attackEntityFrom(new EntityDamageSource("generic", attacker), dmg);
			target.worldObj.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndStrongStrike", 1, 0.95F+0.05F*itemRand.nextFloat());
		}
		
		return super.hitEntity(stack, target, attacker);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		
		if (!nbt.getBoolean("Charged")) {
			player.setItemInUse(stack, MAX_CHARGE);
		}
		
		return stack;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player) {
		if (stack.hasTagCompound()) {
			stack.getTagCompound().setBoolean("Charged", true);
		}
		
		if (!world.isRemote) {
			world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndCharged", 1, 0.9F+0.1F*itemRand.nextFloat());
		}
		
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().getBoolean("Charged");
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return MAX_CHARGE;
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