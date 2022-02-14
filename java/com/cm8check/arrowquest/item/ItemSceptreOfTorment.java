package com.cm8check.arrowquest.item;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.entity.ArrowQuestEntityHelper;
import com.cm8check.arrowquest.entity.EntityDoomGuardian;
import com.cm8check.arrowquest.entity.EntitySpellLightning;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketOneshotAnimation;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSceptreOfTorment extends Item{
	private EntityLiving[] followupAttackTargets;
	private long followupAttackTime;
	
	public static final int MAX_CHARGE = 300;
	
	public ItemSceptreOfTorment() {
		super();
		this.setUnlocalizedName(ModLib.itemSceptreOfTormentName);
		this.setCreativeTab(CreativeTabs.tabCombat);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
		
		if (!world.isRemote) {
			if (followupAttackTime > 0 && world.getTotalWorldTime() >= followupAttackTime) {
				followupAttackTime = 0;
				
				for (int i = 0; i < followupAttackTargets.length; i++) {
					EntityLiving target = followupAttackTargets[i];
					
					if (!target.isDead && !(entity instanceof EntityDoomGuardian)) {
						EntitySpellLightning lightning = new EntitySpellLightning(world, target.posX, target.posY, target.posZ, (EntityLivingBase) entity);
						lightning.damage = 0.0F;
		            	world.addWeatherEffect(lightning);
						
						float damage;
						
						if (ArrowQuestEntityHelper.isEntityBoss(target.getClass())) {
							damage = 40.0F;
						}
						else {
							damage = 150.0F;
						}
						
						target.attackEntityFrom(new DamageSource("mob").setDamageAllowedInCreativeMode(), damage);
						target.addVelocity(0.0D, -3.0D, 0.0D);
		            	
						world.playSoundEffect(entity.posX, entity.posY, entity.posZ, "arrowquest:sndExplosionBig2", 0.3F, 0.93F + world.rand.nextFloat() * 0.1F);
		            	ArrowQuest.packetPipeline.sendToDimension(new PacketOneshotAnimation(AnimationHelper.bigExplosion2.id, target.posX, target.posY - 2.0D, target.posZ, 5.0F), target.dimension);
					}
				}
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		if (stack.getMetadata() > 0)
			return stack;
		
		if (!world.isRemote) {
			double dist = 50;
			Vec3 look = player.getLook(1.0F);
			Vec3 eyePos = new Vec3(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
			Vec3 ray = eyePos.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist);
			MovingObjectPosition spot = world.rayTraceBlocks(eyePos, ray, false, false, true);
			
			double d2 = dist;
			if (spot != null){
                d2 = spot.hitVec.distanceTo(eyePos);
            }
			
			Entity pointedEntity = null;
            Vec3 vec33 = null;
            List list = world.getEntitiesWithinAABB(EntityMob.class, player.getEntityBoundingBox().addCoord(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist));
            
            for (int i = 0; i < list.size(); i++){
                Entity entity1 = (Entity) list.get(i);

                if (entity1.canBeCollidedWith()){
                    float f2 = entity1.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f2, (double) f2, (double) f2);
                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(eyePos, ray);

                    if (axisalignedbb.isVecInside(eyePos)){
                        if (0.0D < d2 || d2 == 0.0D){
                            pointedEntity = entity1;
                            vec33 = movingobjectposition == null ? eyePos : movingobjectposition.hitVec;
                            d2 = 0.0D;
                        }
                    }else if (movingobjectposition != null){
                        double d3 = eyePos.distanceTo(movingobjectposition.hitVec);

                        if (d3 < d2 || d2 == 0.0D){
                            if (entity1 == player.ridingEntity && !player.canRiderInteract()){
                                if (d2 == 0.0D){
                                    pointedEntity = entity1;
                                    vec33 = movingobjectposition.hitVec;
                                }
                            }else{
                                pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }
            }
            
			if (pointedEntity != null) {
				int width = 1;
				int height = 3;
				
				double x = pointedEntity.posX;
				double y = pointedEntity.posY;
				double z = pointedEntity.posZ;
				
				AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - width, y - height, z - width, x + width, y + height+1, z + width);
				list = world.getEntitiesWithinAABB(EntityMob.class, aabb);
				if (list != null && !list.isEmpty()) {
					followupAttackTargets = new EntityLiving[list.size()];
					int i = 0;
					Iterator iterator = list.iterator();
					
		            while (iterator.hasNext()) {
		            	EntityLiving entity = (EntityLiving) iterator.next();
		            	
		            	double dx = entity.posX - player.posX;
		            	double dz = entity.posZ - player.posZ;
		            	
		            	double pushX = (double) dx;
		            	double pushY = 1.5D;
		            	double pushZ = (double) dz;
		            	double d3 = (double) MathHelper.sqrt_double(pushX * pushX + pushY * pushY + pushZ * pushZ);
		            	pushX = pushX / d3 * 4.5D;
		            	pushZ = pushZ / d3 * 4.5D;
		            	entity.addVelocity(pushX, pushY, pushZ);
		            	
		            	followupAttackTargets[i] = entity;
		            	i++;
		            }
		            
		            followupAttackTime = world.getTotalWorldTime() + 17;
		            
		            if (!stack.hasTagCompound()) {
		            	stack.setTagCompound(new NBTTagCompound());
		            }
		            
		            stack.getTagCompound().setInteger("ChargeNeeded", 250);
		            stack.setItemDamage(1);
		            
		            world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndSuperSceptre", 1.0F, 1.0F);
				}
			}
		}
		
		return stack;
	}
}