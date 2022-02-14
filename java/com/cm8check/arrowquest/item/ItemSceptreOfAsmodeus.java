package com.cm8check.arrowquest.item;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.entity.ArrowQuestEntityHelper;
import com.cm8check.arrowquest.entity.EntityDoomGuardian;
import com.cm8check.arrowquest.entity.EntitySpellLightning;
import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSceptreOfAsmodeus extends Item{
	private EntityLiving[] followupAttackTargets;
	private long followupAttackTime;
	
	public ItemSceptreOfAsmodeus() {
		super();
		this.setUnlocalizedName(ModLib.itemSceptreOfAsmodeusName);
		this.setCreativeTab(CreativeTabs.tabCombat);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
		
		if (!world.isRemote) {
			if (stack.getMetadata() == 0) {
				if (stack.hasTagCompound()) {
					if (world.getTotalWorldTime() >= stack.getTagCompound().getLong("NextChargeTime")) {
						stack.setItemDamage(1);
						world.playSoundEffect(entity.posX, entity.posY, entity.posZ, "arrowquest:sndBlessingReceived", 1, 0.9F+0.1F*itemRand.nextFloat());
					}
				}
				else {
					stack.setTagCompound(new NBTTagCompound());
				}
			}
			
			if (followupAttackTime > 0 && world.getTotalWorldTime() >= followupAttackTime) {
				followupAttackTime = 0;
				
				for (int i = 0; i < followupAttackTargets.length; i++) {
					EntityLiving target = followupAttackTargets[i];
					
					if (!target.isDead && !(entity instanceof EntityDoomGuardian)) {
						EntitySpellLightning lightning = new EntitySpellLightning(world, target.posX, target.posY, target.posZ, (EntityLivingBase) entity);
						
						if (ArrowQuestEntityHelper.isEntityBoss(target.getClass())) {
							lightning.damage = 30.0F;
						}
						else {
							lightning.damage = 58.0F;
						}
						
		            	world.addWeatherEffect(lightning);
					}
				}
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		if (stack.getMetadata() == 1) {
			if (!world.isRemote) {
				int width = 8;
				int height = 3;
				
				double x = player.posX;
				double y = player.posY;
				double z = player.posZ;
				
				AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - width, y - height, z - width, x + width, y + height+1, z + width);
				List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
				if (list != null && !list.isEmpty()){
					followupAttackTargets = new EntityLiving[list.size()];
					int i = 0;
					Iterator iterator = list.iterator();
					
		            while (iterator.hasNext()) {
		            	EntityLiving entity = (EntityLiving) iterator.next();
		            	
		            	double dx = entity.posX - x;
		            	double dz = entity.posZ - z;
		            	
		            	double pushX = (double) dx;
		            	double pushY = 1.0D;
		            	double pushZ = (double) dz;
		            	double d3 = (double) MathHelper.sqrt_double(pushX * pushX + pushY * pushY + pushZ * pushZ);
		            	pushX = pushX / d3 * 2.5D;
		            	pushZ = pushZ / d3 * 2.5D;
		            	entity.addVelocity(pushX, 0.5D, pushZ);
		            	
		            	followupAttackTargets[i] = entity;
		            	i++;
		            }
		            
		            followupAttackTime = world.getTotalWorldTime() + 15;
		            stack.setItemDamage(0);
					stack.getTagCompound().setLong("NextChargeTime", world.getTotalWorldTime() + 2000 + itemRand.nextInt(500));
					
					world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndSkillMove1", 1, 0.9F+0.1F*itemRand.nextFloat());
				}
			}
		}
		
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata() == 1;
	}
}