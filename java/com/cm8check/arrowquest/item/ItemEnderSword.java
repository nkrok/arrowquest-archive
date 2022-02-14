package com.cm8check.arrowquest.item;

import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnderSword extends ItemSword{
	private float attackDamage;
	
	public ItemEnderSword(){
		super(ToolMaterial.EMERALD);
		this.attackDamage = 8.0F;
		this.setUnlocalizedName(ModLib.itemEnderSwordName);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		ArrowQuestItemHelper.checkArtifactItemAcquired(this, world, entity);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			double dist = 16;
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
            List list = world.getEntitiesWithinAABB(EntityLiving.class, player.getEntityBoundingBox().addCoord(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist));
            
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
            
			if (pointedEntity instanceof EntityLiving){
				EntityLiving entity = (EntityLiving) pointedEntity;
            	entity.attackEntityFrom(new EntityDamageSource("magic", player), 4.0F);
				
				Vec3 pos = pointedEntity.getPositionVector();
				player.setPositionAndUpdate(pos.xCoord, pos.yCoord, pos.zCoord);
				
				world.playSoundEffect(pos.xCoord, pos.yCoord, pos.zCoord, "mob.endermen.portal", 1, 1);
				stack.damageItem(2, player);
			}
		}
		
		return stack;
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