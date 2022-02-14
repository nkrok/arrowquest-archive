package com.cm8check.arrowquest.item;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.entity.EntityDoomGuardian;
import com.cm8check.arrowquest.entity.EntitySpellFireball;
import com.cm8check.arrowquest.entity.EntitySpellLightning;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketOneshotAnimation;
import com.cm8check.arrowquest.network.packet.PacketSetCooldown;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;
import com.cm8check.arrowquest.network.packet.PacketSpawnParticles;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ItemWand extends Item{
	private Random rand = ArrowQuest.RAND;
	private int[] cooldown = new int[601];
	public static int localCooldown = -1;
	public static int localBaseCooldown = -1;
	
	protected ItemWand(String name, int maxDamage){
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setMaxDamage(maxDamage);
		this.setUnlocalizedName(name);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected){
		if (isSelected){
			if (!world.isRemote){
				if (cooldown[stack.getMetadata()] > 0){
					cooldown[stack.getMetadata()]--;
				}
				
				//jump scrolls passive negate fall damage
				if (stack.hasTagCompound()){
					NBTTagCompound nbt = stack.getTagCompound();
					int spell = nbt.getByte("ActiveSpell");
					if (spell >= 4 && spell <= 6){
						entity.fallDistance = 0;
					}
				}
			}else if (entity == Minecraft.getMinecraft().thePlayer){
				if (localCooldown > 0){
					localCooldown--;
				}
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
		if (!world.isRemote){
			if (player.isSneaking()){
				player.openGui(ArrowQuest.instance, ModLib.guiWandID, world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			}else{
				int damage = itemstack.getItemDamage();
				if (damage < itemstack.getMaxDamage() && itemstack.hasTagCompound()){
					NBTTagCompound nbt = itemstack.getTagCompound();
					int spell = nbt.getByte("ActiveSpell");
					
					if (cooldown[damage] <= 0 && spell > 0){
						cooldown[damage+1] = castSpell(spell, world, player);
						
						if (cooldown[damage+1] > -1){
							ArrowQuest.packetPipeline.sendTo(new PacketSetCooldown(cooldown[damage+1]), (EntityPlayerMP) player);
							
							itemstack.setItemDamage(damage+1);
						}else{
							ArrowQuest.packetPipeline.sendTo(new PacketSetCooldown(0), (EntityPlayerMP) player);
							
							cooldown[damage+1] = 0;
						}
					}
				}
			}
		}
		
		return itemstack;
	}
	
	public void cycleSpell(ItemStack itemstack){
		if (itemstack.hasTagCompound()){
			NBTTagCompound nbt = itemstack.getTagCompound();
			NBTTagList items = nbt.getTagList("Items", 10);
			
			if (!items.hasNoTags()){
				int slot = nbt.getByte("ActiveSlot")+1;
				if (slot >= items.tagCount()){
					slot = 0;
				}
				
				NBTTagCompound compound = items.getCompoundTagAt(slot);
				int meta = compound.getShort("Damage");
				nbt.setByte("ActiveSpell", (byte) meta);
				nbt.setByte("ActiveSlot", (byte) slot);
			}
		}
	}
	
	public int castSpell(int spell, World world, EntityPlayer player){
		if (spell == 1){ //fireball
			Vec3 look = player.getLook(1.0F);
			EntitySpellFireball fireball = new EntitySpellFireball(world, player.getPositionVector().xCoord, player.getPositionVector().yCoord+player.getEyeHeight(), player.getPositionVector().zCoord, look.xCoord, look.yCoord, look.zCoord);
			fireball.shootingEntity = player;
			world.spawnEntityInWorld(fireball);
			world.playSoundEffect(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), "mob.ghast.fireball", 1, 1);
			
			return 6;
		}else if (spell == 2){ //puddle
			ItemBucket bucket = (ItemBucket) Items.water_bucket;
			
			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

	        if (movingobjectposition != null){
	        	if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
	                BlockPos blockpos = movingobjectposition.getBlockPos();

	                if (world.isBlockModifiable(player, blockpos)){
	                	BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);
	                	
	                	if (bucket.tryPlaceContainedLiquid(world, blockpos1)){
	                        return 10;
	                    }
	                }
	        	}
	        }
		}else if (spell == 3){ //quake
			int x = player.getPosition().getX();
			int y = player.getPosition().getY();
			int z = player.getPosition().getZ();
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - 6, y - 1, z - 6, x + 6, y + 1, z + 6);
			List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
	            while (iterator.hasNext()){
	            	EntityLiving entity = (EntityLiving) iterator.next();
	            	int dx = entity.getPosition().getX() - x;
	            	int dz = entity.getPosition().getZ() - z;
	            	
	            	double pushX = (double) dx;
	            	double pushY = 0.5D;
	            	double pushZ = (double) dz;
	            	double d3 = (double) MathHelper.sqrt_double(pushX * pushX + pushY * pushY + pushZ * pushZ);
	            	pushX = pushX / d3 * 1.8D;
	            	pushZ = pushZ / d3 * 1.8D;
	            	entity.addVelocity(pushX, 0.5D, pushZ);
	            }
			}
			
			world.playSoundEffect(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), "arrowquest:sndQuake", 1, 1);
			
			return 20;
		}else if (spell == 4){ //jump I
			Vec3 look = player.getLook(1.0F);
			player.addVelocity(look.xCoord*1.5, look.yCoord*1.5, look.zCoord*1.5);
			((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(player));
			
			world.playSoundEffect(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), "arrowquest:sndAirJump", 1, 1);
			
			return 40;
		}else if (spell == 5){ //jump II
			Vec3 look = player.getLook(1.0F);
			player.addVelocity(look.xCoord*2, look.yCoord*2, look.zCoord*2);
			((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(player));
			
			world.playSoundEffect(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), "arrowquest:sndAirJump", 1, 1);
			
			return 40;
		}else if (spell == 6){ //jump III
			Vec3 look = player.getLook(1.0F);
			player.addVelocity(look.xCoord*2.5, look.yCoord*2.5, look.zCoord*2.5);
			((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(player));
			
			world.playSoundEffect(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), "arrowquest:sndAirJump", 1, 1);
			
			return 40;
		}else if (spell == 7){ //self heal I
			player.heal(1);
			((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S29PacketSoundEffect("arrowquest:sndHeal", (double) player.getPosition().getX(), (double) player.getPosition().getY(), (double) player.getPosition().getZ(), 1, 1));
			ArrowQuest.packetPipeline.sendToAllAround(new PacketOneshotAnimation(AnimationHelper.heal2.id, player.posX, player.posY - 0.3, player.posZ, 2.0F), new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 16));
			return 20;
		}else if (spell == 8){ //self heal II
			player.heal(2);
			((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S29PacketSoundEffect("arrowquest:sndHeal", (double) player.getPosition().getX(), (double) player.getPosition().getY(), (double) player.getPosition().getZ(), 1, 1));
			ArrowQuest.packetPipeline.sendToAllAround(new PacketOneshotAnimation(AnimationHelper.heal2.id, player.posX, player.posY - 0.3, player.posZ, 2.0F), new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 16));
			return 20;
		}else if (spell == 9){ //self heal III
			player.heal(3);
			((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S29PacketSoundEffect("arrowquest:sndHeal", (double) player.getPosition().getX(), (double) player.getPosition().getY(), (double) player.getPosition().getZ(), 1, 1));
			ArrowQuest.packetPipeline.sendToAllAround(new PacketOneshotAnimation(AnimationHelper.heal2.id, player.posX, player.posY - 0.3, player.posZ, 2.0F), new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 16));
			return 20;
		}else if (spell > 9 && spell < 13){ //AoE Heal
			int x = player.getPosition().getX();
			int y = player.getPosition().getY();
			int z = player.getPosition().getZ();
			
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - 7, y - 1, z - 7, x + 7, y + 4, z + 7);
			List list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
	            while (iterator.hasNext()){
	            	EntityPlayer entity = (EntityPlayer) iterator.next();
	            	entity.heal(spell-9);
	            	
	            	ArrowQuest.packetPipeline.sendToAllAround(new PacketOneshotAnimation(AnimationHelper.heal2.id, entity.posX, entity.posY - 0.3, entity.posZ, 2.0F), new TargetPoint(player.dimension, entity.posX, entity.posY, entity.posZ, 16));
	            }
			}
			
			world.playSoundEffect(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), "arrowquest:sndHeal", 1, 1);
			
			return 30;
		}else if (spell == 13){ //lightning
			Vec3 lightningPos = null;
			
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
				lightningPos = pointedEntity.getPositionVector();
			}else{
				if (spot != null && spot.typeOfHit == MovingObjectType.BLOCK){
					lightningPos = spot.hitVec;
				}
			}
			
			if (lightningPos != null){
				EntitySpellLightning entity = new EntitySpellLightning(world, lightningPos.xCoord, lightningPos.yCoord, lightningPos.zCoord, player);
				world.addWeatherEffect(entity);
				
				return 35;
			}
		}else if (spell == 14){ //lava puddle
			ItemBucket bucket = (ItemBucket) Items.lava_bucket;
			
			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

	        if (movingobjectposition != null){
	        	if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
	                BlockPos blockpos = movingobjectposition.getBlockPos();

	                if (world.isBlockModifiable(player, blockpos)){
	                	BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);
	                	
	                	if (bucket.tryPlaceContainedLiquid(world, blockpos1)){
	                        return 10;
	                    }
	                }
	        	}
	        }
		}else if (spell == 15){ //megafreeze
			int width = 4;
			int height = 1;
			
			int x = player.getPosition().getX();
			int y = player.getPosition().getY();
			int z = player.getPosition().getZ();
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - width, y - height, z - width, x + width, y + height+1, z + width);
			List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
	            while (iterator.hasNext()){
	            	EntityLiving entity = (EntityLiving) iterator.next();
	            	
	            	entity.attackEntityFrom(new EntityDamageSource("magic", player).setMagicDamage(), 19.0F);
	            }
			}
			
			ArrowQuest.packetPipeline.sendToAllAround(new PacketSpawnParticles(0, Math.round((float) player.posX), Math.round((float) player.posY), Math.round((float) player.posZ)), new TargetPoint(player.dimension, x, y, z, 16));
			
			world.playSoundEffect(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), "arrowquest:sndMegafreeze", 1, 1);
			
			return 35;
		}else if (spell == 16){ //fireproof aura
			int x = player.getPosition().getX();
			int y = player.getPosition().getY();
			int z = player.getPosition().getZ();
			
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - 7, y - 1, z - 7, x + 7, y + 4, z + 7);
			List list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
	            while (iterator.hasNext()){
	            	EntityPlayer entity = (EntityPlayer) iterator.next();
	            	
	            	entity.addPotionEffect(new PotionEffect(12, 200, 0, true, true));
	            	entity.extinguish();
	            }
			}
			
			return 20;
		}else if (spell == 17){ //temporary block
			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

	        if (movingobjectposition != null){
	        	if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
	                BlockPos blockpos = movingobjectposition.getBlockPos();

	                if (world.isBlockModifiable(player, blockpos)){
	                	BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);
	                	Material material = world.getBlockState(blockpos1).getBlock().getMaterial();
	                	int x = blockpos1.getX();
	                	int y = blockpos1.getY();
	                	int z = blockpos1.getZ();
	                	boolean flag = false;
	                	
	                	AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x, y, z, x + 1, y + 1, z + 1);
	        			List list = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
	        			if (list != null && !list.isEmpty()){
	        				flag = true;
	        			}

	                    if (!flag && (world.isAirBlock(blockpos1) || !material.isSolid())){
	                    	world.setBlockState(blockpos1, ModBlocks.blockTemporaryBlock.getDefaultState());
	                    	world.playSoundEffect(player.posX, player.posY, player.posZ, "dig.stone", 1, 1);
	                    	return 0;
	                    }
	                }
	        	}
	        }
		}else if (spell == 18){ //earthen blitz
			int x = player.getPosition().getX();
			int y = player.getPosition().getY();
			int z = player.getPosition().getZ();
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - 5, y - 1, z - 5, x + 5, y + 1, z + 5);
			List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
	            while (iterator.hasNext()){
	            	EntityLiving entity = (EntityLiving) iterator.next();
	            	entity.addVelocity(0.0D, 2.3D, 0.0D);
	            }
			}
			
			world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndSuperQuake", 1, 1);
			
			return 35;
		}else if (spell == 19){ //fire beam
			double dist = 14;
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
				if (entity.deathTime <= 0 && !(entity instanceof EntityDoomGuardian)){
	            	entity.attackEntityFrom(new EntityDamageSource("magic", player), 8.0F);
	            	
	            	ArrowQuest.packetPipeline.sendToAllAround(new PacketSetMobAnimation(3, player.getEntityId(), entity.getEntityId()), new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 48));
	            	world.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndFirePunch", 1, 0.95F+0.05F*rand.nextFloat());
	            	return 14;
				}
			}
		}else if (spell == 20){ //lightning strike
			int x = player.getPosition().getX();
			int y = player.getPosition().getY();
			int z = player.getPosition().getZ();
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - 10, y - 3, z - 10, x + 10, y + 3, z + 10);
			List list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			if (list != null && !list.isEmpty()){
				Iterator iterator = list.iterator();
	            while (iterator.hasNext()){
	            	EntityLiving entity = (EntityLiving) iterator.next();
	            	
	            	EntitySpellLightning lightning = new EntitySpellLightning(world, entity.getPositionVector().xCoord, entity.getPositionVector().yCoord, entity.getPositionVector().zCoord, player);
					lightning.damage = 25.0F;
	            	world.addWeatherEffect(lightning);
	            }
	            
	            return 35;
			}
		}else if (spell == 21){ //explo-fireball
			Vec3 look = player.getLook(1.0F);
			EntitySpellFireball fireball = new EntitySpellFireball(world, player.getPositionVector().xCoord, player.getPositionVector().yCoord+player.getEyeHeight(), player.getPositionVector().zCoord, look.xCoord, look.yCoord, look.zCoord);
			fireball.shootingEntity = player;
			fireball.damage = 10.0F;
			fireball.explodeRand = 1;
			world.spawnEntityInWorld(fireball);
			world.playSoundEffect(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), "mob.ghast.fireball", 1, 1);
			
			return 16;
		}else if (spell == 22){ //explo-tri-fireball
			for (int i = -1; i < 2; i++){
				Vec3 look = player.getLook(1.0F).rotateYaw(0.3F * i);
				EntitySpellFireball fireball = new EntitySpellFireball(world, player.getPositionVector().xCoord, player.getPositionVector().yCoord+player.getEyeHeight(), player.getPositionVector().zCoord, look.xCoord, look.yCoord, look.zCoord);
				fireball.shootingEntity = player;
				fireball.damage = 14.0F;
				fireball.explodeRand = 1;
				world.spawnEntityInWorld(fireball);
			}
			
			world.playSoundEffect(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), "mob.ghast.fireball", 1, 1);
			
			return 10;
		}
		
		return -1;
	}
}