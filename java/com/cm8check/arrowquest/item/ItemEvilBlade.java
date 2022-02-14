package com.cm8check.arrowquest.item;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.entity.ArrowQuestEntityHelper;
import com.cm8check.arrowquest.entity.EntityFlamePillar;
import com.cm8check.arrowquest.entity.FireballStormController;
import com.cm8check.arrowquest.lib.ModLib;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEvilBlade extends ItemSword{
	private float attackDamage;
	private int type;
	
	public static final int[] CHARGE_LEVELS = {
		8,
		16,
		32,
		64,
		128
	};
	
	public static final String[][] SKILL_NAMES = {
		{
			"Super Slice",
			"Eruption",
			"Wrath of Flames",
			"Underworld Uprising",
			"Axe of Corruption"
		},
		{
			"Super Slice",
			"Eruption",
			"Wrath of Flames",
			"Underworld Uprising",
			"Kill Everything"
		}
	};
	
	public ItemEvilBlade(int type) {
		super(Item.ToolMaterial.EMERALD);
		
		if (type == 0) {
			this.attackDamage = 7.0F;
			this.setMaxDamage(931);
			this.setUnlocalizedName(ModLib.itemEvilBladeName);
		}
		else {
			this.attackDamage = 10.0F;
			this.setMaxDamage(1237);
			this.setUnlocalizedName(ModLib.itemAxeOfCorruptionName);
		}
		
		this.type = type;
	}
	
	/*
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		if (isSelected && entity.isSneaking() && stack.hasTagCompound()) {
			int newCharge = stack.getTagCompound().getInteger("Charge") + 1;
			stack.getTagCompound().setInteger("Charge", newCharge);
		}
	}
	*/
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
	}
	
	private void useAttack(int atk, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		World world = attacker.worldObj;
		
		switch (atk) {
		case 0: // Super Slice
		{
			if (!ArrowQuestEntityHelper.isEntityBoss(target.getClass())) {
				target.setHealth(0.0F);
				
				for (int i = 0; i < 3; i++) {
					EntityLivingBase clone = (EntityLivingBase) EntityList.createEntityByName(EntityList.getEntityString(target), world);
					clone.copyDataFromOld(target);
					clone.setHealth(0.0F);
					world.spawnEntityInWorld(clone);
				}
			}
			else {
				target.attackEntityFrom(new EntityDamageSource("generic", attacker), 35.0F);
			}
			
			world.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndSkillMove1", 1, 0.9F+0.1F*itemRand.nextFloat());
			world.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndSwordStrike", 1, 0.9F+0.1F*itemRand.nextFloat());
			
			break;
		}
		
		case 1: // Eruption
		{
			EntityFlamePillar pillar = new EntityFlamePillar(world, target.posX, target.posY, target.posZ, attacker, 0);
			world.spawnEntityInWorld(pillar);
			
			world.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndFlamePillar", 6.0F, 0.92F+0.08F*itemRand.nextFloat());
			
			break;
		}
		
		case 2: // Wrath of Flames
		{
			EntityFlamePillar pillar = new EntityFlamePillar(world, target.posX, target.posY, target.posZ, attacker, 5);
			world.spawnEntityInWorld(pillar);
			
			world.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndFlamePillar", 6.0F, 0.92F+0.08F*itemRand.nextFloat());
			
			break;
		}
		
		case 3: // Summon the Underworld
		{
			int width = 8;
			int height = 2;
			
			// create netherrack
			for (int k1 = -width; k1 <= width; k1++) {
	            for (int l1 = -height; l1 <= height; l1++) {
	                for (int i2 = -width; i2 <= width; i2++) {
	                    BlockPos pos = target.getPosition().add(k1, l1, i2);
	                    if (world.getTileEntity(pos) == null) {
	                    	Block block = world.getBlockState(pos).getBlock();
	                    	if (block != Blocks.air && block.getBlockHardness(world, pos) > -1.0F) {
	                    		world.setBlockState(pos, Blocks.netherrack.getDefaultState());
	                    	}
	                    }
	                }
	            }
	        }
			
			FireballStormController controller = new FireballStormController(world, target.posX, target.posY, target.posZ, 45, attacker);
			world.spawnEntityInWorld(controller.getNextFireball());
			
			EntityFlamePillar pillar = new EntityFlamePillar(world, target.posX, target.posY, target.posZ, attacker, 1);
			world.spawnEntityInWorld(pillar);
			
			width = 10;
			height = 3;
			
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(target.posX - width, target.posY - height, target.posZ - width, target.posX + width, target.posY + height, target.posZ + width);
			List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			
			if (list != null) {
				for (int i = 0; i < 2; i++) {
					if (!list.isEmpty()) {
						EntityLiving entity = (EntityLiving) list.remove(itemRand.nextInt(list.size()));
						
						pillar = new EntityFlamePillar(world, entity.posX, entity.posY, entity.posZ, attacker, 1);
						world.spawnEntityInWorld(pillar);
					}
					else {
						break;
					}
				}
			}
			
			world.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndEvilSummon", 6.0F, 0.92F+0.08F*itemRand.nextFloat());
			
			break;
		}
		
		case 4: // Axe of Corruption/Kill Everything
		{
			if (type == 0) {
				stack.setItem(ModItems.itemAxeOfCorruption);
				stack.setItemDamage(0);
				
				world.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndClone", 1.0F, 0.92F+0.08F*itemRand.nextFloat());
			}
			else {
				int width = 25;
				int height = 5;
				
				double x = target.posX;
				double y = target.posY;
				double z = target.posZ;
				
				AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - width, y - height, z - width, x + width, y + height+1, z + width);
				List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
				
				if (list != null && !list.isEmpty()){
					Iterator iterator = list.iterator();
					
		            while (iterator.hasNext()){
		            	EntityLiving entity = (EntityLiving) iterator.next();
		            	
		            	if (!ArrowQuestEntityHelper.isEntityBoss(entity.getClass())) {
		            		entity.setHealth(0.0F);
		            	}
		            }
				}
				
				world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndEvilSummon", 6.0F, 0.9F+0.1F*itemRand.nextFloat());
				world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndDeathCry", 6.0F, 0.9F+0.07F*itemRand.nextFloat());
				world.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndStrongStrike", 0.8F, 0.9F+0.1F*itemRand.nextFloat());
			}
			
			break;
		}
		}
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		int primed = stack.getTagCompound().getInteger("AttackPrimed");
		
		if (primed > 0) {
			if (attacker.worldObj.getTotalWorldTime() >= stack.getTagCompound().getLong("PrimeActiveTime")) {
				int chargeUsed = CHARGE_LEVELS[primed - 1];
				if (type == 1) {
					chargeUsed /= 2;
				}
				
				stack.getTagCompound().setInteger("Charge", stack.getTagCompound().getInteger("Charge") - chargeUsed);
				stack.getTagCompound().setInteger("AttackPrimed", 0);
				
				useAttack(primed - 1, stack, target, attacker);
			}
		}
		else if (target instanceof EntityMob) {
			int newCharge = stack.getTagCompound().getInteger("Charge") + 1;
			stack.getTagCompound().setInteger("Charge", newCharge);
			
			for (int c : CHARGE_LEVELS) {
				if (newCharge == c) {
					attacker.worldObj.playSoundEffect(attacker.posX, attacker.posY, attacker.posZ, "arrowquest:sndCharged", 1, 0.9F+0.1F*itemRand.nextFloat());
				}
			}
		}
		
		return super.hitEntity(stack, target, attacker);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote && stack.hasTagCompound()) {
			if (stack.getTagCompound().getInteger("AttackPrimed") == 0) {
				int charge = stack.getTagCompound().getInteger("Charge");
				int level = 0;
				
				for (int c : CHARGE_LEVELS) {
					if (charge >= c) {
						level++;
					}
					else {
						break;
					}
				}
				
				if (level > 0) {
					world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndEvilBladeCharge", 1, 0.93F+0.1F*itemRand.nextFloat());
					stack.getTagCompound().setInteger("AttackPrimed", level);
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
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().getInteger("AttackPrimed") > 0;
	}
	
	@Override
	public Multimap getItemAttributeModifiers(){
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double)this.attackDamage, 0));
		return multimap;
	}
	
	public int getWeaponType() {
		return type;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}
}