package com.cm8check.arrowquest.tileentity;

import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.entity.EntityAQBoss;
import com.cm8check.arrowquest.entity.EntityBanishedSoul;
import com.cm8check.arrowquest.entity.EntityBlazeBoss;
import com.cm8check.arrowquest.entity.EntityCastleArcher;
import com.cm8check.arrowquest.entity.EntityCastleSoldier;
import com.cm8check.arrowquest.entity.EntityCastleSoldierKing;
import com.cm8check.arrowquest.entity.EntityDoomGuardian;
import com.cm8check.arrowquest.entity.EntityDwarf;
import com.cm8check.arrowquest.entity.EntityDwarfBoss;
import com.cm8check.arrowquest.entity.EntityElf;
import com.cm8check.arrowquest.entity.EntityElfBoss;
import com.cm8check.arrowquest.entity.EntityFinalBoss;
import com.cm8check.arrowquest.entity.EntityFinalBossCore;
import com.cm8check.arrowquest.entity.EntityFlyingStinger;
import com.cm8check.arrowquest.entity.EntityFurnaceMonster;
import com.cm8check.arrowquest.entity.EntityHyperBlaze;
import com.cm8check.arrowquest.entity.EntityHyperScorpion;
import com.cm8check.arrowquest.entity.EntityNetherOrc;
import com.cm8check.arrowquest.entity.EntityObsidianWarrior;
import com.cm8check.arrowquest.entity.EntityOrc;
import com.cm8check.arrowquest.entity.EntityOrcBoss;
import com.cm8check.arrowquest.entity.EntityPhantasm;
import com.cm8check.arrowquest.entity.EntityPirate;
import com.cm8check.arrowquest.entity.EntityPirateCaptain;
import com.cm8check.arrowquest.entity.EntityPolice;
import com.cm8check.arrowquest.entity.EntityPoliceBoss;
import com.cm8check.arrowquest.entity.EntityRobotSpider;
import com.cm8check.arrowquest.entity.EntityVampire;
import com.cm8check.arrowquest.entity.EntityVampireBoss;
import com.cm8check.arrowquest.entity.EntityWizard;
import com.cm8check.arrowquest.entity.EntityWizardBoss;
import com.cm8check.arrowquest.item.ModItems;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;

public class TileEntitySingleSpawner extends TileEntity implements IUpdatePlayerListBox {
	public int mob;
	public int mobLevel;
	private int updateDelay;
	private static Random random = new Random();
	
	private BlockPos structureControllerLocation;

	public final String[] mobNames = { "Zombie", "Spider", "Skeleton", "Creeper", "CastleSoldier", "CastleArcher",
			"Pirate", "CastleSoldierKing", "Orc", "OrcArcher", "PirateCaptain", "Dwarf", "DwarfArcher", "Elf",
			"ElfArcher", "EasyOrc", "GoldenHuman", "GoldenHumanArcher", "NetherOrc", "NetherOrcArcher", "BlazeBoss",
			"Police", "PoliceGunner", "PoliceBoss", "RobotSpider", "Scorpion", "DoomGuardian", "ObsidianWarrior",
			"OWArcher", "ElitePolice", "ElitePoliceGunner", "Wizard", "HyperBlaze", "FinalBoss", "ElfBoss", "DwarfBoss",
			"OrcBoss", "ColosseumBoss", "Vampire", "VampireArcher", "FurnaceMonster", "FurnaceMonsterArcher",
			"Phantasm", "PhantasmArcher", "VampireBoss", "BanishedSoul" };

	public TileEntitySingleSpawner() {
		this.updateDelay = 1 + random.nextInt(40);
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false
				: player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		mob = compound.getByte("mob");
		mobLevel = compound.getByte("mobLevel");
		
		if (compound.hasKey("controllerLocation")) {
			int[] controllerPos = compound.getIntArray("controllerLocation");
			this.structureControllerLocation = new BlockPos(controllerPos[0], controllerPos[1], controllerPos[2]);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setByte("mob", (byte) mob);
		compound.setByte("mobLevel", (byte) mobLevel);
		
		if (this.structureControllerLocation != null) {
			compound.setIntArray("controllerLocation", new int[] {
					this.structureControllerLocation.getX(),
					this.structureControllerLocation.getY(),
					this.structureControllerLocation.getZ()
			});
		}
	}

	@Override
	public void update() {
		if (!worldObj.isRemote && !ArrowQuest.DEV_MODE) {
			if (updateDelay > 0) {
				updateDelay -= 1;
			} else {
				updateDelay = 40;
				if (worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
					EntityPlayer player = worldObj.getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(),
							16.0D);
					if (player != null) {
						if (!player.capabilities.isCreativeMode) {
							spawnEntity(mob, mobLevel);
							worldObj.setBlockToAir(getPos());
							worldObj.removeTileEntity(getPos());
						}
					}
				}
			}
		}
	}
	
	public void setControllerLocation(BlockPos pos) {
		this.structureControllerLocation = pos;
	}

	private void spawnEntity(int mob, int mobLevel) {
		EntityLiving entity;

		switch (mob) {
		case 0: // Zombie
			entity = new EntityZombie(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.leather_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.leather_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			}

			for (int i = 0; i < 4; i++) {
				entity.setEquipmentDropChance(i, 0.0F);
			}
			break;
		case 1: // Spider
			entity = new EntitySpider(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.leather_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.leather_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			}

			for (int i = 0; i < 4; i++) {
				entity.setEquipmentDropChance(i, 0.0F);
			}
			break;
		case 2: // Skeleton
			entity = new EntitySkeleton(worldObj);

			entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.leather_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.leather_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			}

			for (int i = 0; i < 4; i++) {
				entity.setEquipmentDropChance(i, 0.0F);
			}
			break;
		case 3: // Creeper
			entity = new EntityCreeper(worldObj);

			if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			}
			break;
		case 4: // CastleSoldier
			entity = new EntityCastleSoldier(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				if (random.nextInt(5) == 0) {
					entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemStaff1));
				} else {
					entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemDoubleSword));
				}
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}
			break;
		case 5: // CastleArcher
			entity = new EntityCastleArcher(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}

			entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
			break;
		case 6: // Pirate
			entity = new EntityPirate(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
			}
			break;
		case 7: // CastleSoldierKing
			entity = new EntityCastleSoldierKing(worldObj);

			entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemKingSword));
			entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
			entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
			entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			break;
		case 8: // Orc
			entity = new EntityOrc(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}
			break;
		case 9: // OrcArcher
			entity = new EntityOrc(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}
			break;
		case 10: // PirateCaptain
			entity = new EntityPirateCaptain(worldObj);

			entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemScimitar));
			entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
			entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
			break;
		case 11: // Dwarf
			entity = new EntityDwarf(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}
			break;
		case 12: // DwarfArcher
			entity = new EntityDwarf(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}
			break;
		case 13: // Elf
			entity = new EntityElf(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}
			break;
		case 14: // ElfArcher
			entity = new EntityElf(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}
			break;
		case 15: // EasyOrc
			entity = new EntityOrc(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.leather_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.leather_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			}

			if (random.nextInt(5) == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
			}
			break;
		case 16: // GoldenHuman
			entity = new EntityCastleSoldier(worldObj);

			if (mobLevel == 0) {
				ItemStack sword = new ItemStack(Items.golden_sword);
				sword.addEnchantment(Enchantment.knockback, 5);
				sword.addEnchantment(Enchantment.sharpness, 4);
				entity.setCurrentItemOrArmor(0, sword);

				entity.setCurrentItemOrArmor(1, new ItemStack(Items.golden_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.golden_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.golden_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.golden_helmet));
			} else if (mobLevel == 1) {
				ItemStack sword = new ItemStack(Items.golden_sword);
				sword.addEnchantment(Enchantment.knockback, 6);
				sword.addEnchantment(Enchantment.sharpness, 5);
				entity.setCurrentItemOrArmor(0, sword);

				entity.setCurrentItemOrArmor(1, new ItemStack(Items.golden_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.golden_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.golden_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.golden_helmet));
			} else if (mobLevel == 2) {
				if (random.nextInt(5) == 0) {
					entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemStaff3));
				} else {
					ItemStack sword = new ItemStack(Items.golden_sword);
					sword.addEnchantment(Enchantment.knockback, 7);
					sword.addEnchantment(Enchantment.sharpness, 6);
					entity.setCurrentItemOrArmor(0, sword);
				}
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.golden_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.golden_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.golden_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.golden_helmet));
			}
			break;
		case 17: // GoldenHumanArcher
			entity = new EntityCastleArcher(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.golden_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.golden_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.golden_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.golden_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.golden_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.golden_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.golden_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.golden_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.golden_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.golden_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.golden_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.golden_helmet));
			}

			ItemStack bow = new ItemStack(Items.bow);
			bow.addEnchantment(Enchantment.punch, 6);
			bow.addEnchantment(Enchantment.power, 4);
			entity.setCurrentItemOrArmor(0, bow);
			break;
		case 18: // NetherOrc
			entity = new EntityNetherOrc(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemGlaive));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				ItemStack sword = new ItemStack(ModItems.itemBroadAxe);
				sword.addEnchantment(Enchantment.fireAspect, 1);
				entity.setCurrentItemOrArmor(0, sword);
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}
			break;
		case 19: // NetherOrcArcher
			entity = new EntityNetherOrc(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}

			ItemStack stack = new ItemStack(Items.bow);
			stack.addEnchantment(Enchantment.flame, 1);
			entity.setCurrentItemOrArmor(0, stack);
			break;
		case 20: // BlazeBoss
			entity = new EntityBlazeBoss(worldObj);
			break;
		case 21: // Police
			entity = new EntityPolice(worldObj);
			
			entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemPoliceBoots));
			entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemPoliceLeggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemPoliceChestplate));
			entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemPoliceHelmet));
			
			if (random.nextInt(4) == 0) {
				((EntityPolice) entity).elite = true;
			}
			
			if (random.nextInt(7) == 0) {
				entity.setCurrentItemOrArmor(0, null);
			}
			else {
				entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemPoliceSabre));
			}
			
			break;
		case 22: // PoliceGunner
			entity = new EntityPolice(worldObj);
			
			entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemPoliceBoots));
			entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemPoliceLeggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemPoliceChestplate));
			entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemPoliceHelmet));
			
			if (random.nextInt(4) == 0) {
				((EntityPolice) entity).elite = true;
			}
			
			if (random.nextInt(7) == 0) {
				entity.setCurrentItemOrArmor(0, null);
			}
			else {
				entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemPoliceGun));
			}
			
			break;
		case 23: // PoliceBoss
			entity = new EntityPoliceBoss(worldObj);

			entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemPoliceSabre));
			entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemPoliceBoots));
			entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemPoliceLeggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemPoliceChestplate));
			entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemPoliceHelmet));

			EntityPoliceBoss entityPoliceBoss = (EntityPoliceBoss) entity;
			entityPoliceBoss.spawnX = getPos().getX();
			entityPoliceBoss.spawnY = getPos().getY();
			entityPoliceBoss.spawnZ = getPos().getZ();
			break;
		case 24: // RobotSpider
			entity = new EntityRobotSpider(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
				this.spawnEntity(24, 0);
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
				this.spawnEntity(24, 0);
			}

			if (random.nextInt(20) == 0) {
				EntityHyperScorpion scorpion = new EntityHyperScorpion(worldObj);
				scorpion.setPosition(getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D);
				worldObj.spawnEntityInWorld(scorpion);
			}

			if (random.nextInt(16) == 0) {
				this.spawnEntity(31, 0);
			}
			break;
		case 25: // Scorpion
			entity = new EntityFlyingStinger(worldObj);
			break;
		case 26: // DoomGuardian
			entity = new EntityDoomGuardian(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			}
			break;
		case 27: // ObsidianWarrior
			if (random.nextInt(2) == 0) {
				entity = null;
				this.spawnEntity(42, mobLevel);
			}
			else {
				entity = new EntityObsidianWarrior(worldObj);
	
				if (mobLevel == 0) {
					entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemBroadAxe));
					entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemObsidianBoots));
					entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemObsidianLeggings));
					entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemObsidianChestplate));
					entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemObsidianHelmet));
				} else if (mobLevel == 1) {
					entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemGreatsword));
					entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemObsidianBoots));
					entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemObsidianLeggings));
					entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemObsidianChestplate));
					entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemObsidianHelmet));
				} else if (mobLevel == 2) {
					entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemExecutionerAxe));
					entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemObsidianBoots));
					entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemObsidianLeggings));
					entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemObsidianChestplate));
					entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemObsidianHelmet));
				}
			}

			if (random.nextInt(2) == 0) {
				this.spawnEntity(24, 0);
			}

			if (random.nextInt(25) == 0) {
				this.spawnEntity(32, 0);
			}
			break;
		case 28: // OWArcher
			if (random.nextInt(2) == 0) {
				entity = null;
				this.spawnEntity(43, mobLevel);
			}
			else {
				entity = new EntityObsidianWarrior(worldObj);
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
	
				if (mobLevel == 0) {
					entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemObsidianBoots));
					entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemObsidianLeggings));
					entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemObsidianChestplate));
					entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemObsidianHelmet));
				} else if (mobLevel == 1) {
					entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemObsidianBoots));
					entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemObsidianLeggings));
					entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemObsidianChestplate));
					entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemObsidianHelmet));
				} else if (mobLevel == 2) {
					entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemObsidianBoots));
					entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemObsidianLeggings));
					entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemObsidianChestplate));
					entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemObsidianHelmet));
				}
			}

			if (random.nextInt(2) == 0) {
				this.spawnEntity(24, 0);
			}

			if (random.nextInt(25) == 0) {
				this.spawnEntity(32, 0);
			}
			break;
		case 29: // ElitePolice
			entity = new EntityPolice(worldObj);

			EntityPolice police = (EntityPolice) entity;
			police.elite = true;
			
			entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemPoliceBoots));
			entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemPoliceLeggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemPoliceChestplate));
			entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemPoliceHelmet));
			
			if (random.nextInt(7) == 0) {
				entity.setCurrentItemOrArmor(0, null);
			}
			else {
				entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemPoliceSabre));
			}

			if (random.nextInt(16) == 0) {
				this.spawnEntity(32, 0);
			}

			if (random.nextInt(4) == 0) {
				this.spawnEntity(31, 0);
			}
			break;
		case 30: // ElitePoliceGunner
			entity = new EntityPolice(worldObj);

			EntityPolice police2 = (EntityPolice) entity;
			police2.elite = true;
			
			entity.setCurrentItemOrArmor(1, new ItemStack(ModItems.itemPoliceBoots));
			entity.setCurrentItemOrArmor(2, new ItemStack(ModItems.itemPoliceLeggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(ModItems.itemPoliceChestplate));
			entity.setCurrentItemOrArmor(4, new ItemStack(ModItems.itemPoliceHelmet));
			
			if (random.nextInt(7) == 0) {
				entity.setCurrentItemOrArmor(0, null);
			}
			else {
				entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemPoliceGun));
			}

			if (random.nextInt(16) == 0) {
				this.spawnEntity(32, 0);
			}

			if (random.nextInt(4) == 0) {
				this.spawnEntity(31, 0);
			}
			break;
		case 31: // Wizard
			entity = new EntityWizard(worldObj);
			entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemStaff3));
			break;
		case 32: // HyperBlaze
			entity = new EntityHyperBlaze(worldObj);
			break;
		case 33: // FinalBoss
			entity = null;

			int xx = this.pos.getX();
			int yy = this.pos.getY();
			int zz = this.pos.getZ();

			// FinalBoss
			EntityFinalBoss boss = new EntityFinalBoss(worldObj);
			boss.setPosition(xx + 95.5D, yy - 30.0D, zz + 3.5D);
			boss.lootChestPos = new BlockPos(xx + 124, yy - 33, zz + 3);
			boss.maxHeight = yy - 14;
			worldObj.spawnEntityInWorld(boss);

			// Cores
			EntityFinalBossCore core = new EntityFinalBossCore(worldObj);
			core.setPosition(xx + 108.5D, yy - 12.0D, zz + 30.0D);
			core.boss = boss;
			worldObj.spawnEntityInWorld(core);

			core = new EntityFinalBossCore(worldObj);
			core.setPosition(xx + 88.5D, yy - 6.0D, zz + 30.0D);
			core.boss = boss;
			worldObj.spawnEntityInWorld(core);

			core = new EntityFinalBossCore(worldObj);
			core.setPosition(xx + 119.5D, yy - 37.0D, zz + 3.5D);
			core.boss = boss;
			worldObj.spawnEntityInWorld(core);

			core = new EntityFinalBossCore(worldObj);
			core.setPosition(xx + 90.5D, yy - 19.0D, zz - 22.5D);
			core.boss = boss;
			worldObj.spawnEntityInWorld(core);

			core = new EntityFinalBossCore(worldObj);
			core.setPosition(xx + 118.5D, yy - 19.0D, zz - 16.5D);
			core.boss = boss;
			worldObj.spawnEntityInWorld(core);
			break;
		case 34: // ElfBoss
			EntityElfBoss elfBoss = new EntityElfBoss(worldObj);
			elfBoss.setCurrentItemOrArmor(0, elfBoss.swordStack);
			elfBoss.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
			elfBoss.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
			elfBoss.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
			elfBoss.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			entity = elfBoss;
			break;
		case 35: // DwarfBoss
			entity = new EntityDwarfBoss(worldObj);
			ItemStack sword = new ItemStack(ModItems.itemLongswordDiamond);
			sword.addEnchantment(Enchantment.knockback, 5);
			entity.setCurrentItemOrArmor(0, sword);
			entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
			entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
			entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			break;
		case 36: // OrcBoss
			entity = new EntityOrcBoss(worldObj);
			entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemBattleAxeDiamond));
			entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
			entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
			entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
			break;
		case 37: // ColloseumBoss
			int i = ArrowQuest.RAND.nextInt(2);
			i = 0;

			if (i == 0) {
				entity = new EntityWizardBoss(worldObj);
				EntityWizardBoss wizard = (EntityWizardBoss) entity;
				wizard.spawnX = getPos().getX();
				wizard.spawnY = getPos().getY();
				wizard.spawnZ = getPos().getZ();
			} else {
				entity = new EntityWizardBoss(worldObj);
			}
			break;

		case 38: // Vampire
			entity = new EntityVampire(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemGlaive));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
			}

			break;

		case 39: // VampireArcher
			entity = new EntityVampire(worldObj);

			entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
			}

			break;

		case 40: // FurnaceMonster
			entity = new EntityFurnaceMonster(worldObj);

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemFalchion));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
			}

			break;

		case 41: // FurnaceMonsterArcher
			entity = new EntityFurnaceMonster(worldObj);

			entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));

			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_chestplate));
			} else if (mobLevel == 1) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (mobLevel == 2) {
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
			}

			break;
		
		case 42: // Phantasm
			entity = new EntityPhantasm(worldObj);
			
			if (mobLevel == 0) {
				entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemExecutionerAxe));
			}
			else {
				entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemDemonBlade));
			}
			
			break;
			
		case 43: // PhantasmArcher
			entity = new EntityPhantasm(worldObj);
			
			entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
			
			break;

		case 44: // VampireBoss
			entity = new EntityVampireBoss(worldObj);

			entity.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemVampiresTooth));
			entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
			entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
			entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));

			break;
			
		case 45: // BanishedSoul
			entity = new EntityBanishedSoul(worldObj);
			break;

		default:
			entity = new EntityZombie(worldObj);
			break;
		}
		
		if (entity == null)
			return;

		entity.setPosition(getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D);
		worldObj.spawnEntityInWorld(entity);
		
		if (this.structureControllerLocation != null) {
			TileEntity tile = worldObj.getTileEntity(structureControllerLocation);
			if (tile instanceof TileEntityStructureController) {
				TileEntityStructureController controller = (TileEntityStructureController) tile;
				
				if (entity instanceof EntityAQBoss) {
					controller.setHasBoss(true);
					((EntityAQBoss) entity).setStructureControllerLocation(structureControllerLocation);
				}
				else {
					controller.spawnerSpawned(entity);
				}
			}
		}
	}
}