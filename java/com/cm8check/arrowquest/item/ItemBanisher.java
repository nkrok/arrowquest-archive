package com.cm8check.arrowquest.item;

import java.util.List;

import com.cm8check.arrowquest.entity.ArrowQuestEntityHelper;
import com.cm8check.arrowquest.entity.EntityBanishedSoul;
import com.cm8check.arrowquest.entity.EntityGenericProjectile;
import com.cm8check.arrowquest.lib.ModLib;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBanisher extends ItemSword{
	private float attackDamage;
	
	public ItemBanisher() {
		super(Item.ToolMaterial.EMERALD);
		this.attackDamage = 5.0F;
		this.setUnlocalizedName(ModLib.itemBanisherName);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote) {
			double dist = 8;
			Vec3 look = player.getLook(1.0F);
			Vec3 eyePos = new Vec3(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
			Vec3 ray = eyePos.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist);
			MovingObjectPosition spot = world.rayTraceBlocks(eyePos, ray, false, false, true);
			
			double d2 = dist;
			if (spot != null){
                d2 = spot.hitVec.distanceTo(eyePos);
            }
			
			EntityMob target = null;
            Vec3 vec33 = null;
            List list = world.getEntitiesWithinAABB(EntityMob.class, player.getEntityBoundingBox().addCoord(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist));
            
            for (int i = 0; i < list.size(); i++){
            	EntityMob entity1 = (EntityMob) list.get(i);

                if (entity1.canBeCollidedWith()){
                    float f2 = entity1.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f2, (double) f2, (double) f2);
                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(eyePos, ray);

                    if (axisalignedbb.isVecInside(eyePos)){
                        if (0.0D < d2 || d2 == 0.0D){
                            target = entity1;
                            vec33 = movingobjectposition == null ? eyePos : movingobjectposition.hitVec;
                            d2 = 0.0D;
                        }
                    }else if (movingobjectposition != null){
                        double d3 = eyePos.distanceTo(movingobjectposition.hitVec);

                        if (d3 < d2 || d2 == 0.0D){
                            if (entity1 == player.ridingEntity && !player.canRiderInteract()){
                                if (d2 == 0.0D){
                                    target = entity1;
                                    vec33 = movingobjectposition.hitVec;
                                }
                            }else{
                                target = entity1;
                                vec33 = movingobjectposition.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }
            }
            
			if (target != null && !(target instanceof EntityBanishedSoul) && !ArrowQuestEntityHelper.isEntityBoss(target.getClass()) && target.deathTime == 0) {
				player.addPotionEffect(new PotionEffect(2, 15, 3, true, false));
				//player.addPotionEffect(new PotionEffect(15, 25, 1, true, false));
				target.addPotionEffect(new PotionEffect(2, 10, 2, true, false));
				
				double d1 = player.posX - target.posX;
				d2 = (player.posY+1) - (target.posY+0.2);
				double d3 = player.posZ - target.posZ;
				
				EntityGenericProjectile soul = new EntityGenericProjectile(EntityGenericProjectile.TYPE_SOUL_STEAL, player.worldObj, target.posX, target.posY + 0.2D, target.posZ, d1, d2, d3, 0.3D, target);
				player.worldObj.spawnEntityInWorld(soul);
				
				if (target.attackEntityFrom(DamageSource.magic, target.getMaxHealth() / 7)) {
					player.worldObj.playSoundEffect(target.posX, target.posY, target.posZ, "arrowquest:sndSoulCapture", 1.0F, 0.93F + player.worldObj.rand.nextFloat() * 0.1F);
				}
				
				if (target.getHealth() <= 0) {
					NBTTagCompound nbt;
					
					if (!stack.hasTagCompound()) {
						nbt = new NBTTagCompound();
						stack.setTagCompound(nbt);
					}
					else {
						nbt = stack.getTagCompound();
					}
					
					nbt.setShort("Souls", (short) (nbt.getShort("Souls") + 1));
					
					EntityBanishedSoul entity = new EntityBanishedSoul(player.worldObj);
	        		entity.setPositionAndUpdate(target.posX, target.posY, target.posZ);
	        		player.worldObj.spawnEntityInWorld(entity);
	        		
	        		player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "ambient.weather.thunder", 10000.0F, 0.9F);
	        		
	        		if (player.worldObj.rand.nextInt(3) == 0) {
		        		player.attackEntityFrom(DamageSource.generic, 1.0F);
		        		if (player.getHealth() > 8.0F) {
		        			player.setHealth(8.0F);
		        		}
	        		}
				}
			}
		}
		
		return stack;
	}
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack) {
		Multimap multimap = HashMultimap.create();
		
		int bonus = 0;
		
		if (stack.hasTagCompound()) {
			bonus = stack.getTagCompound().getShort("Souls") * 3;
		}
		
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", this.attackDamage + bonus, 0));
		return multimap;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}
}