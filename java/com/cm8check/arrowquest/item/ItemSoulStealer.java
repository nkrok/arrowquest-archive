package com.cm8check.arrowquest.item;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ItemSoulStealer extends ItemSword{
	public static final String[] skillNames = new String[] {
		"Wither Plague",
		"Sweeping Strike",
		"Mass Paralysis",
		"Underworld Wrath"
	};
	
	public static final int BASE_SOULS_NEEDED = 3;
	
	private float attackDamage;
	
	private EntityLiving[] followupAttackTargets;
	private int followupAttackTargetIndex;
	private int followupAttackTime;
	
	public ItemSoulStealer() {
		super(Item.ToolMaterial.IRON);
		this.attackDamage = 7.0F;
		this.setMaxDamage(816);
		this.setUnlocalizedName(ModLib.itemSoulStealerName);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
		
		/*
		if (entity.isSneaking() && stack.hasTagCompound()) {
			stack.getTagCompound().setInteger("Souls", stack.getTagCompound().getInteger("Souls") + 1);
		}
		*/
		
		if (!world.isRemote) {
			if (followupAttackTime > 0) {
				followupAttackTime--;
				
				if (followupAttackTime <= 0) {
					EntityLiving target = followupAttackTargets[followupAttackTargetIndex];
					
					if (!target.isDead) {
						target.attackEntityFrom(new EntityDamageSource("magic", entity), 55.0F);
						
						if (target.getHealth() <= 0) {
							stack.getTagCompound().setInteger("Souls", stack.getTagCompound().getInteger("Souls") + 1);
						}
		            	
		            	ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(3, entity.getEntityId(), target.getEntityId()), new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 48));
		            	world.playSoundEffect(entity.posX, entity.posY, entity.posZ, "arrowquest:sndStrongStrike", 1.0F, 0.95F+0.1F*itemRand.nextFloat());
					}
					
					followupAttackTargetIndex++;
					if (followupAttackTargetIndex < followupAttackTargets.length) {
						followupAttackTime = 4;
					}
					
					if (followupAttackTargetIndex % 3 == 0) {
						world.playSoundEffect(entity.posX, entity.posY, entity.posZ, "ambient.weather.thunder", 10000.0F, 0.9F);
					}
				}
			}
		}
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
		if (target.getHealth() == 0 && target instanceof EntityMob) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt == null) {
				nbt = new NBTTagCompound();
				stack.setTagCompound(nbt);
			}
			
			int amt = 1;
			
			if (target.getActivePotionEffect(Potion.wither) != null) {
				amt = 2;
			}
			
			if (target.getActivePotionEffect(Potion.moveSlowdown) != null) {
				amt *= 2;
				
				attacker.heal(2.0F);
				attacker.worldObj.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndQuickHeal", 1, 1.0F+0.05F*itemRand.nextFloat());
			}
			
			nbt.setInteger("Souls", nbt.getInteger("Souls") + amt);
		}
		
		if (stack.hasTagCompound()) {
			int level = stack.getTagCompound().getInteger("AttackPrimed");
			
			if (level > 0 && attacker.worldObj.getTotalWorldTime() >= stack.getTagCompound().getLong("PrimeActiveTime")) {
				useAttack(stack, level - 1, target, attacker);
				
				int soulsUsed = (int) (BASE_SOULS_NEEDED * Math.pow(2, level - 1));
				
				stack.getTagCompound().setInteger("Souls", stack.getTagCompound().getInteger("Souls") - soulsUsed);
				stack.getTagCompound().setInteger("AttackPrimed", 0);
			}
		}
		
		return super.hitEntity(stack, target, attacker);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote && stack.hasTagCompound()) {
			if (stack.getTagCompound().getInteger("AttackPrimed") == 0) {
				int souls = stack.getTagCompound().getInteger("Souls");
				
				if (souls >= BASE_SOULS_NEEDED) {
					int level = 0;
					
					for (int i = 1; i < skillNames.length; i++) {
						if (souls >= BASE_SOULS_NEEDED * Math.pow(2, i)) {
							level = i;
						}
						else {
							break;
						}
					}
					
					world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndCharged2", 1, 0.9F+0.1F*itemRand.nextFloat());
					stack.getTagCompound().setInteger("AttackPrimed", level + 1);
					stack.getTagCompound().setLong("PrimeActiveTime", world.getTotalWorldTime() + 20);
				}
			}
			else {
				stack.getTagCompound().setInteger("AttackPrimed", 0);
				world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndClone", 1, 0.93F+0.1F*itemRand.nextFloat());
			}
		}
		
		return stack;
	}
	
	private void useAttack(ItemStack stack, int level, EntityLivingBase target, EntityLivingBase attacker) {
		World world = target.worldObj;
		
		switch (level) {
		case 0: // Wither Plague
		{
			int width = 5;
			int height = 3;
			
			double x = target.posX;
			double y = target.posY;
			double z = target.posZ;
			
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - width, y - height, z - width, x + width, y + height+1, z + width);
			List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
				
	            while (iterator.hasNext()){
	            	EntityLiving entity = (EntityLiving) iterator.next();
	            	
	            	entity.addPotionEffect(new PotionEffect(20, 100, 2, false, true));
	            }
			}
			
			world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndWitherAoE", 1, 0.9F+0.1F*itemRand.nextFloat());
			
			break;
		}
		
		case 1: // Sweeping Strike
		{
			int width = 6;
			int height = 1;
			
			double x = target.posX;
			double y = target.posY;
			double z = target.posZ;
			
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - width, y - height, z - width, x + width, y + height+1, z + width);
			List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			
			int soulsGained = 0;
			
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
				
	            while (iterator.hasNext()){
	            	EntityLiving entity = (EntityLiving) iterator.next();
	            	
	            	entity.attackEntityFrom(new EntityDamageSource("generic", attacker), 40.0F);
	            	entity.addPotionEffect(new PotionEffect(19, 30, 2, true, false));
	            	
	            	if (entity.getHealth() <= 0) {
	            		soulsGained++;
	            	}
	            }
			}
			
			if (soulsGained > 0) {
				stack.getTagCompound().setInteger("Souls", stack.getTagCompound().getInteger("Souls") + soulsGained);
			}
			
			world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndSwordStrike", 1, 0.9F+0.1F*itemRand.nextFloat());
			
			break;
		}
		
		case 2: // Mass Paralysis
		{
			int width = 18;
			int height = 1;
			
			double x = target.posX;
			double y = target.posY;
			double z = target.posZ;
			
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - width, y - height, z - width, x + width, y + height+1, z + width);
			List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
				
	            while (iterator.hasNext()){
	            	EntityLiving entity = (EntityLiving) iterator.next();
	            	entity.addPotionEffect(new PotionEffect(2, 200, 6, false, true));
	            }
			}
			
			world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndMegafreezeReal", 1, 0.9F+0.1F*itemRand.nextFloat());
			
			break;
		}
		
		case 3: // Underworld Wrath
		{
			int width = 10;
			int height = 1;
			
			double x = target.posX;
			double y = target.posY;
			double z = target.posZ;
			
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - width, y - height, z - width, x + width, y + height+1, z + width);
			List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			
			if (list != null && !list.isEmpty()){
				followupAttackTargets = new EntityLiving[list.size()];
				int i = 0;
				Iterator iterator = list.iterator();
				
	            while (iterator.hasNext()){
	            	EntityLiving entity = (EntityLiving) iterator.next();
	            	entity.addVelocity(-0.2D + 0.4D*itemRand.nextDouble(), 1.4D, -0.2D + 0.4D*itemRand.nextDouble());
	            	
	            	followupAttackTargets[i] = entity;
	            	i++;
	            }
	            
	            followupAttackTargetIndex = 0;
	            followupAttackTime = 12;
			}
			
			world.playSoundEffect(target.posX, target.posY, target.posZ, "ambient.weather.thunder", 10000.0F, 0.9F);
			world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndEarthShatter", 1, 0.9F+0.1F*itemRand.nextFloat());
			
			break;
		}
		}
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