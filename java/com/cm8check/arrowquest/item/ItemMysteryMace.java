package com.cm8check.arrowquest.item;

import java.util.Random;

import com.cm8check.arrowquest.entity.ArrowQuestEntityHelper;
import com.cm8check.arrowquest.entity.EntitySpellLightning;
import com.cm8check.arrowquest.lib.ModLib;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemMysteryMace extends ItemSword{
	private float attackDamage;
	
	enum MysteryAttack {
		CowTransform,
		SquidTransform,
		Lightning,
		Explode,
		UpwardLaunch,
		SetFire,
		Paralyze,
		Invisibility,
		Clone,
		GiveMace,
		StealWeapon,
		DropAllItems
	}
	
	public ItemMysteryMace() {
		super(Item.ToolMaterial.EMERALD);
		this.attackDamage = 7.0F;
		this.setMaxDamage(280);
		this.setUnlocalizedName(ModLib.itemMysteryMaceName);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (!target.worldObj.isRemote && !ArrowQuestEntityHelper.isEntityBoss(target.getClass()) && itemRand.nextInt(3) > 0) {
			useAttack(target, attacker);
		}
		
		return super.hitEntity(stack, target, attacker);
	}
	
	private void useAttack(EntityLivingBase target, EntityLivingBase attacker) {
		MysteryAttack atk = null;
		while (atk == null) {
			atk = randomEnum(itemRand, MysteryAttack.class);
			
			if ((atk == MysteryAttack.CowTransform || atk == MysteryAttack.SquidTransform) && target.getHealth() > 8.0F) {
				atk = null;
				continue;
			}
			
			if (atk == MysteryAttack.Explode && target.getHealth() > 8.0F) {
				atk = null;
				continue;
			}
			
			if (atk == MysteryAttack.Lightning && itemRand.nextInt(4) > 0) {
				atk = null;
				continue;
			}
			
			if (atk == MysteryAttack.DropAllItems && (itemRand.nextInt(16) > 0 || !(attacker instanceof EntityPlayer))) {
				atk = null;
				continue;
			}
			
			if (atk == MysteryAttack.Clone && target.getHealth() == 0) {
				atk = null;
				continue;
			}
		}
		
		World world = attacker.worldObj;
		
		switch (atk) {
		case CowTransform:
		{
			EntityCow cow = new EntityCow(world);
			cow.setPosition(target.posX, target.posY, target.posZ);
			world.spawnEntityInWorld(cow);
			
			//target.setHealth(1.0F);
			world.removeEntity(target);
			
			break;
		}
		
		case SquidTransform:
		{
			EntitySquid squid = new EntitySquid(world);
			squid.setPosition(target.posX, target.posY, target.posZ);
			world.spawnEntityInWorld(squid);
			
			world.removeEntity(target);
			
			break;
		}
		
		case Lightning:
		{
			EntitySpellLightning lightning = new EntitySpellLightning(world, target.posX, target.posY, target.posZ, attacker);
			lightning.damage = 16.0F;
			world.addWeatherEffect(lightning);
			
			break;
		}
		
		case Explode:
		{
			world.createExplosion(attacker, target.posX, target.posY + 1, target.posZ, 2.0F, false);
    		world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndMegafreeze", 1.0F, 0.95F+(0.05F*itemRand.nextFloat()));
			
			//target.setHealth(1.0F);
			world.removeEntity(target);
			
			break;
		}
		
		case UpwardLaunch:
		{
			target.addVelocity(-0.2D + 0.4D*itemRand.nextDouble(), 2.0D, -0.2D + 0.4D*itemRand.nextDouble());
			world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndSuperQuake", 1.0F, 0.92F+(0.08F*itemRand.nextFloat()));
			
			break;
		}
		
		case SetFire:
		{
			target.setFire(7);
			break;
		}
		
		case Paralyze:
		{
			target.addPotionEffect(new PotionEffect(2, 100, 6, false, true));
			world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndParalyze", 1.0F, 0.92F+(0.08F*itemRand.nextFloat()));
			
			break;
		}
		
		case Invisibility:
		{
			target.addPotionEffect(new PotionEffect(14, 120, 0, false, true));
			break;
		}
		
		case Clone:
		{
			for (int i = 0; i < 2; i++) {
				EntityLivingBase clone = (EntityLivingBase) EntityList.createEntityByName(EntityList.getEntityString(target), world);
				clone.copyDataFromOld(target);
				clone.setHealth(target.getHealth());
				world.spawnEntityInWorld(clone);
			}
			
			world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndClone", 1.0F, 0.92F+(0.08F*itemRand.nextFloat()));
			
			break;
		}
		
		case GiveMace:
		{
			target.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemMysteryMace));
			break;
		}
		
		case StealWeapon:
		{
			if (target.getHeldItem() != null) {
				attacker.entityDropItem(target.getHeldItem(), 1.0F).setPickupDelay(1);
				target.setCurrentItemOrArmor(0, null);
			}
			
			break;
		}
		
		case DropAllItems:
		{
			EntityPlayer player = (EntityPlayer) attacker;
			
			for (int i = 0; i < player.inventory.mainInventory.length; i++) {
	            if (player.inventory.mainInventory[i] != null) {
	                player.dropItem(player.inventory.mainInventory[i], true, false);
	                player.inventory.mainInventory[i] = null;
	            }
	        }
			
			break;
		}
		}
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
	
	private static <T extends Enum<?>> T randomEnum(Random random, Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}
}