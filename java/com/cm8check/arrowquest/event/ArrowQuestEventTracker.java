package com.cm8check.arrowquest.event;

import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.entity.ArrowQuestEntityHelper;
import com.cm8check.arrowquest.entity.EntityPolice;
import com.cm8check.arrowquest.entity.EntityPoliceBoss;
import com.cm8check.arrowquest.item.ItemBattleAxe;
import com.cm8check.arrowquest.item.ItemDagger;
import com.cm8check.arrowquest.item.ItemDemonWeapon;
import com.cm8check.arrowquest.item.ItemLongsword;
import com.cm8check.arrowquest.item.ItemScroll;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketPlayerXP;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;
import com.cm8check.arrowquest.player.XPRegistry;
import com.cm8check.arrowquest.world.ArrowQuestWorldData;
import com.cm8check.arrowquest.world.gen.WorldGenSchematics;

import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ArrowQuestEventTracker {
	private Random rand = new Random();

	@SubscribeEvent
	public void onWorldLoaded(WorldEvent.Load event) {
		GameRules rules = event.world.getGameRules();
		if (!rules.hasRule("bedsSetSpawn")) {
			rules.addGameRule("bedsSetSpawn", "true", GameRules.ValueType.BOOLEAN_VALUE);
		}

		if (!event.world.isRemote) {
			int id = event.world.provider.getDimensionId();
			if (id == 0) {
				if (!ArrowQuestWorldData.loaded) {
					ArrowQuestWorldData.instance = ArrowQuestWorldData.forWorld(event.world);
				}
			} else if (id == ModLib.dimFinalBossID) {
				if (!ArrowQuest.DEV_MODE
						&& event.world.getBlockState(new BlockPos(30, 69, 72)).getBlock() == Blocks.air) {
					System.out.println(
							"Preparing start region for level " + ModLib.dimFinalBossID + " (this may take a while)");
					WorldGenSchematics.generateFinalDestination(event.world);
					System.out.println("Finished preparing start region for level " + ModLib.dimFinalBossID);
				}
			} else if (id == ModLib.dimPoliceBaseID) {
				if (!ArrowQuest.DEV_MODE
						&& event.world.getBlockState(new BlockPos(13, 46, 16)).getBlock() == Blocks.air) {
					System.out.println(
							"Preparing start region for level " + ModLib.dimPoliceBaseID + " (this may take a while)");
					WorldGenSchematics.generatePoliceDimension(event.world);
					System.out.println("Finished preparing start region for level " + ModLib.dimPoliceBaseID);
				}
			}
		}
	}

	@SubscribeEvent
	public void onWorldUnloaded(WorldEvent.Unload event) {
		if (!event.world.isRemote) {
			if (event.world.provider.getDimensionId() == 0) {
				ArrowQuestWorldData.reset();
			}
		}
	}

	@SubscribeEvent
	public void onEntityLivingKilled(LivingDeathEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);

			if (nbt.hasKey(ModLib.nbtMagicBackpackItems) && !player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
				NBTTagList items = nbt.getTagList(ModLib.nbtMagicBackpackItems, 10);
				for (int i = 0; i < items.tagCount(); i++) {
					NBTTagCompound compound = items.getCompoundTagAt(i);
					ItemStack stack = ItemStack.loadItemStackFromNBT(compound);

					EntityItem entityitem = new EntityItem(player.worldObj, player.posX, player.posY + 1.0D,
							player.posZ, stack);
					float f3 = 0.05F;
					entityitem.motionX = (float) this.rand.nextGaussian() * f3;
					entityitem.motionY = (float) this.rand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) this.rand.nextGaussian() * f3;
					player.worldObj.spawnEntityInWorld(entityitem);
				}

				nbt.removeTag(ModLib.nbtMagicBackpackItems);
			}
			
			// reset current level of xp
			nbt.setInteger(ModLib.nbtPlayerXP, 0);
			ArrowQuest.packetPipeline.sendTo(new PacketPlayerXP(0), (EntityPlayerMP) player);

			if (!player.worldObj.getGameRules().getGameRuleBooleanValue("bedsSetSpawn")) {
				player.setSpawnChunk(player.worldObj.getSpawnPoint(), true, 0);
			}
		} else if (event.source.getEntity() != null) {
			if (event.source.getEntity() instanceof EntityPlayer) {
				int xpGain = getXPGainValue(event.entityLiving);

				EntityPlayer player = (EntityPlayer) event.source.getEntity();
				
				if (player.getHeldItem() != null) {
					if (player.getHeldItem().getItem() == ModItems.itemAxeOfCorruption) {
						xpGain *= 5;
					}
					else if (player.getHeldItem().getItem() == ModItems.itemXPSword) {
						xpGain *= 2;
					}
				}

				ArrowQuestPlayer.increasePlayerXP(player, xpGain);
				
				for (int i = 0; i < 36; i++) {
					if (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].getItem() == ModItems.itemLootMedallion) {
						System.out.println("LOOT");
						ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(1);
						
						if (stack != null) {
							event.entityLiving.entityDropItem(stack, 0.0F);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		// System.out.println(event.ammount);
		if (event.entityLiving instanceof EntityPlayer) { // player took damage
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
			
			float amountOriginal = event.ammount;

			event.ammount *= 1 - (nbt.getByte(ModLib.nbtPlayerDEF) * 0.05);

			ItemStack stack;
			boolean defenseMedallion = false;
			
			for (int i = 0; i < 36; i++) {
				stack = player.inventory.getStackInSlot(i);
				if (stack != null) {
					if (stack.getItem() == ModItems.itemDefenseMedallion && !defenseMedallion) {
						event.ammount *= 0.75;
						defenseMedallion = true;
					}
					else if (stack.getItem() == ModItems.itemSceptreOfTorment) {
						if (stack.getMetadata() == 1) {
							NBTTagCompound sceptreNBT;
							
							if (!stack.hasTagCompound()) {
								sceptreNBT = new NBTTagCompound();
								stack.setTagCompound(sceptreNBT);
							}
							else {
								sceptreNBT = stack.getTagCompound();
							}
							
							int chargeNeeded = sceptreNBT.getInteger("ChargeNeeded");
							int newChargeNeeded = chargeNeeded - (int) amountOriginal;
							
							if (newChargeNeeded <= 0) {
								newChargeNeeded = 0;
								stack.setItemDamage(0);
								event.entityLiving.worldObj.playSoundEffect(event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, "arrowquest:sndBlessingReceived", 1, 0.9F+0.1F*event.entityLiving.worldObj.rand.nextFloat());
							}
							
							sceptreNBT.setInteger("ChargeNeeded", newChargeNeeded);
						}
					}
				}
			}

			if (event.source.isFireDamage() && nbt.getBoolean(ModLib.nbtPlayerLessFireDamage)) {
				event.ammount /= 2;
			}
		} else if (event.source.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);

			if (!event.source.isMagicDamage()) { // player dealt physical damage
				float before = event.ammount;

				event.ammount += nbt.getByte(ModLib.nbtPlayerATK);

				if (player.getHeldItem() != null) {
					Item heldItem = player.getHeldItem().getItem();
					if (heldItem.getClass() == ItemSword.class || heldItem.getClass() == ItemLongsword.class) {
						if (nbt.getBoolean(ModLib.nbtPlayerSwordXP) && rand.nextInt(2) == 0) {
							ArrowQuestPlayer.increasePlayerXP(player, 1);
						}
					} else if (heldItem.getClass() == ItemAxe.class || heldItem.getClass() == ItemBattleAxe.class) {
						if (nbt.getBoolean(ModLib.nbtPlayerAxeExtraDamage)) {
							event.ammount *= 1.5;
						}
						if (nbt.getBoolean(ModLib.nbtPlayerAxeXP) && rand.nextInt(2) == 0) {
							ArrowQuestPlayer.increasePlayerXP(player, 1);
						}
					} else if (heldItem.getClass() == ItemPickaxe.class) {
						if (nbt.getBoolean(ModLib.nbtPlayerPickaxeExtraDamage)) {
							event.ammount *= 1.5;
						}
						if (nbt.getBoolean(ModLib.nbtPlayerPickaxeXP) && rand.nextInt(2) == 0) {
							ArrowQuestPlayer.increasePlayerXP(player, 1);
						}
					} else if (heldItem.getClass() == ItemSpade.class) {
						if (nbt.getBoolean(ModLib.nbtPlayerShovelExtraDamage)) {
							event.ammount *= 1.5;
						}
						if (nbt.getBoolean(ModLib.nbtPlayerShovelXP) && rand.nextInt(2) == 0) {
							ArrowQuestPlayer.increasePlayerXP(player, 1);
						}
					} else if (heldItem instanceof ItemBow) {
						boolean p10 = nbt.getBoolean(ModLib.nbtPlayerBowExtraDamage);
						boolean p15 = nbt.getBoolean(ModLib.nbtPlayerBowExtraDamage2);
						
						if (p10 && p15) {
							event.ammount *= 1.25;
						}
						else if (p10) {
							event.ammount *= 1.10;
						}
						else if (p15) {
							event.ammount *= 1.15;
						}
					}
				}
			} else {
				event.ammount += nbt.getByte(ModLib.nbtPlayerMagicDMG);
			}

			if (nbt.getBoolean(ModLib.nbtPlayerHasBeatenBlazeBoss) && !ArrowQuestWorldData.policeBossDefeated
					&& rand.nextInt(100) == 0 && event.entityLiving.getClass() != EntityPolice.class
					&& event.entityLiving.getClass() != EntityPoliceBoss.class) {
				ArrowQuestEntityHelper.spawnPoliceSquad(event.entityLiving.worldObj, player);
			}

			if (nbt.getBoolean(ModLib.nbtPlayerCursedFeather)) {
				boolean hasFeather = false;
				for (int i = 0; i < 36; i++) {
					if (player.inventory.getStackInSlot(i) != null
							&& player.inventory.getStackInSlot(i).getItem() == ModItems.itemCursedFeather) {
						hasFeather = true;
						break;
					}
				}

				if (!hasFeather) {
					player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "ambient.weather.thunder", 10000.0F, 0.9F);
					player.setHealth(player.getHealth() / 4);
					
					int count = 1;
					for (int i = 0; i < 36; i++) {
						if (player.inventory.getStackInSlot(i) == null) {
							player.inventory.setInventorySlotContents(i, new ItemStack(ModItems.itemCursedFeather));
							count--;
							if (count <= 0) {
								break;
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerUseItem(PlayerUseItemEvent.Finish event) {
		if (!event.entityPlayer.worldObj.isRemote && event.item.getItem() instanceof ItemFood) {
			ItemFood food = (ItemFood) event.item.getItem();
			NBTTagCompound nbt = event.entityPlayer.getEntityData().getCompoundTag(event.entityPlayer.PERSISTED_NBT_TAG);
			
			float bonus = food.getHealAmount(event.item) * (nbt.getByte(ModLib.nbtPlayerFoodBonus) / 4.0F);
			
			if (bonus > 0) {
				int intBonus;
				
				if (bonus < 1.0F) {
					intBonus = MathHelper.ceiling_float_int(bonus);
				}
				else {
					intBonus = MathHelper.floor_float(bonus);
				}
				
				event.entityPlayer.getFoodStats().addStats(intBonus, food.getSaturationModifier(event.item));
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!event.world.isRemote && event.entity instanceof EntityPlayer) { // player joined world or respawned
			EntityPlayer player = (EntityPlayer) event.entity;
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);

			if (nbt.hasKey(ModLib.nbtPlayerSkills)) {
				int jump = nbt.getByte(ModLib.nbtPlayerJumpHeight);
				if (jump > 0) {
					player.addPotionEffect(new PotionEffect(8, 1000000, jump - 1, true, false));
				}

				int speed = nbt.getByte(ModLib.nbtPlayerMoveSpeed);
				if (speed > 0) {
					AttributeModifier modifier = new AttributeModifier(ModLib.playerSpeedUUID, "Speed", 0.2D * speed,
							2);
					IAttributeInstance attribute = player.getAttributeMap()
							.getAttributeInstance(SharedMonsterAttributes.movementSpeed);

					AttributeModifier oldModifier = attribute.getModifier(ModLib.playerSpeedUUID);
					if (oldModifier != null) {
						attribute.removeModifier(oldModifier);
					}

					attribute.applyModifier(modifier);
				}

				int hp = nbt.getByte(ModLib.nbtPlayerMaxHP);
				if (hp > 0) {
					player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").setBaseValue(20);

					AttributeModifier modifier = new AttributeModifier(ModLib.playerMaxHPUUID, "AQMaxHP", hp, 0);
					IAttributeInstance attribute = player.getAttributeMap()
							.getAttributeInstance(SharedMonsterAttributes.maxHealth);

					AttributeModifier oldModifier = attribute.getModifier(ModLib.playerMaxHPUUID);
					if (oldModifier != null) {
						attribute.removeModifier(oldModifier);
					}

					attribute.applyModifier(modifier);
				}
			}

			if (player.dimension == ModLib.dimPoliceBaseID) {
				player.addPotionEffect(new PotionEffect(16, 1000000, 0, true, false));
			}
		}
	}

	private int getXPGainValue(EntityLivingBase entity) {
		return XPRegistry.getEntityXPValue(EntityList.getEntityString(entity));
	}

	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent event) {
		if (event.entity.isUsingItem() && event.entity.getItemInUse().getItem() == ModItems.itemLongbow) {
			int time = event.entity.getItemInUseDuration();
			float fov = (float) time / 20.0F;

			if (fov > 1.0F) {
				fov = 1.0F;
			} else {
				fov *= fov;
			}

			event.newfov = event.fov * (1.0F - fov * 0.15F);
		}
	}

	@SubscribeEvent
	public void onItemToss(ItemTossEvent event) {
		if (event.entityItem.getEntityItem().getItem() == ModItems.itemCursedFeather) {
			for (int i = 0; i < 36; i++) {
				if (event.player.inventory.getStackInSlot(i) == null) {
					event.player.inventory.setInventorySlotContents(i, new ItemStack(ModItems.itemCursedFeather));
					event.setCanceled(true);
					break;
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerDropItems(PlayerDropsEvent event) {
		int perservedItems = 0;

		for (int i = 0; i < event.drops.size(); i++) {
			EntityItem entityItem = event.drops.get(i);

			if (entityItem.getEntityItem().getItem() == ModItems.itemFidelitySword) {
				event.drops.remove(i);
				event.entityPlayer.inventory.setInventorySlotContents(perservedItems, entityItem.getEntityItem());

				perservedItems++;
				i--;
			}
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		if (event.wasDeath) {
			event.entityPlayer.inventory.copyInventory(event.original.inventory);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void handleItemTooltipEvent(ItemTooltipEvent event) {
		ItemStack stack = event.itemStack;
		Item item = stack.getItem();

		if (item == ModItems.itemScroll) {
			event.toolTip.add(ItemScroll.scrollDescriptions[stack.getMetadata()]);
		} else if (item == ModItems.itemMedallion) {
			event.toolTip.add("Gradually regenerates health");
		} else if (item == ModItems.itemFireMedallion) {
			event.toolTip.add("Grants constant Fire Resistance");
		} else if (item == ModItems.itemDefenseMedallion) {
			event.toolTip.add("Reduces incoming damage by 25%");
			event.toolTip.add("(does not stack)");
		} else if (item == ModItems.itemLootMedallion) {
			event.toolTip.add("Receive better loot from enemies");
		} else if (item instanceof ItemDagger) {
			event.toolTip.add("Can be thrown");
		} else if (item == ModItems.itemEnderSword) {
			event.toolTip.add("Right-clicking on a mob within 16 blocks");
			event.toolTip.add("performs a teleport attack.");
		} else if (item == ModItems.itemPoliceTeleporter) {
			event.toolTip.add("Right click to use");
			if (stack.getMetadata() == 0) {
				event.toolTip.add("2 uses remaining");
			} else {
				event.toolTip.add("1 use remaining");
			}
			String dest = "TPC Headquarters";
			if (event.entityPlayer != null && event.entityPlayer.dimension == ModLib.dimPoliceBaseID) {
				dest = "Overworld";
			}
			event.toolTip.add("Destination: " + dest);
		} else if (item == ModItems.itemFinalDestinationKey) {
			event.toolTip.add("Appears to be a key of some sort.");
			event.toolTip.add("\u00A7oKeep exploring...");
		} else if (item == ModItems.itemFidelitySword) {
			event.toolTip.add("Remains in inventory after death");
		} else if (item == ModItems.itemSingingSword) {
			event.toolTip.add("Right-click to charge");
		} else if (item == ModItems.itemSceptreOfAsmodeus) {
			event.toolTip.add("- Charges periodically.");
			event.toolTip.add("- Right-click to use.");
		} else if (item == ModItems.itemSoulStealer) {
			event.toolTip.add("- Killing enemies gathers souls.");
			event.toolTip.add("- Right-click to activate ability.");
			event.toolTip.add("- The more souls improves the ability.");
		} else if (item == ModItems.itemVampiresTooth) {
			event.toolTip.add("Killing enemies heals you");
		} else if (item == ModItems.itemMysteryMace) {
			event.toolTip.add("Attacks cause random things to happen");
		} else if (item == ModItems.itemEvilBlade) {
			event.toolTip.add("- Charge up weapon by hitting enemies.");
			event.toolTip.add("- Right-click to activate ability.");
			event.toolTip.add("- The more charge improves the ability.");
		} else if (item == ModItems.itemAxeOfCorruption) {
			event.toolTip.add("- Kills grant 5x experience.");
			event.toolTip.add("- Charge up weapon by hitting enemies.");
			event.toolTip.add("- Right-click to activate ability.");
			event.toolTip.add("- The more charge improves the ability.");
		} else if (item instanceof ItemDemonWeapon) {
			event.toolTip.add("Attacking inflicts negative status effects,");
			event.toolTip.add("with a chance to inflict upon self instead.");
		} else if (item == Item.getItemFromBlock(ModBlocks.blockWandFurnace)) {
			event.toolTip.add("Uses magic crystals to recharge wands.");
			
			if (stack.hasTagCompound()) {
				event.toolTip.add("Charge Stored: " + stack.getTagCompound().getShort("ChargeLeft"));
			}
		} else if (item == ModItems.itemXPSword) {
			event.toolTip.add("Kills grant double experience.");
		} else if (item == ModItems.itemBanisher) {
			event.toolTip.add("- Hold right-click to banish an enemy.");
			event.toolTip.add("- Each soul banished increases damage by 3.");
			event.toolTip.add("- Banishing an enemy spawns a banished soul");
			event.toolTip.add("and may inflict self damage.");
		} else if (item == ModItems.itemSceptreOfTorment) {
			event.toolTip.add("- Taking damage charges the sceptre.");
			event.toolTip.add("- When fully charged, aim at an enemy");
			event.toolTip.add("and right-click to use.");
			
			if (stack.getMetadata() == 0) {
				event.toolTip.add("  [CHARGED]");
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlaySoundEvent(PlaySoundEvent event) {
		if (event.category == SoundCategory.MUSIC) {
			if (ClientSoundHelper.isCustomMusicPlaying()) {
				event.result = null;
			}
		}
	}
}