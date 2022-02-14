package com.cm8check.arrowquest.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketDimensionMusicUpdate;
import com.cm8check.arrowquest.network.packet.PacketOneshotAnimation;
import com.cm8check.arrowquest.network.packet.PacketSpawnParticles;
import com.cm8check.arrowquest.tileentity.TileEntityDungeonChest;
import com.cm8check.arrowquest.world.ArrowQuestWorldData;
import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFinalBoss extends EntityMob implements IBossDisplayData {
	public boolean isInvincible;
	public float floatLevel;
	public float legSpeed;
	public float legSpeedTo;
	
	private int lightningTimer;
	private int attackTimer;
	private int nextAttack;
	private int doomBulletIndex;
	private int fireballIndex;
	private boolean throwWarn;
	
	private ArrayList<FollowupAttack> followupAttacks;
	
	private final int TOTAL_ATTACKS = 8;

	private boolean displayHealth;

	public BlockPos lootChestPos;
	public int maxHeight = 40;
	
	private static final String[] roarSounds = {
		"arrowquest:sndFinalBossRoar1",
		"arrowquest:sndFinalBossRoar2",
		"arrowquest:sndFinalBossRoar3",
		"arrowquest:sndFinalBossRoar4"
	};

	public EntityFinalBoss(World world) {
		super(world);
		this.setSize(6.0F, 20.0F);
		this.experienceValue = 10000;
		this.isImmuneToFire = true;

		this.isInvincible = true;
		this.legSpeed = -0.025F;
		this.attackTimer = 100 + rand.nextInt(40);
		this.nextAttack = rand.nextInt(4);
		
		this.followupAttacks = new ArrayList<FollowupAttack>();

		this.targetTasks.addTask(1,
				new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(2000.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(256.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(30.0D);
	}

	@Override
	public void onLivingUpdate() {
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;

		if (worldObj.isRemote) {
			if (ClientSoundHelper.bossMusicCheck("arrowquest:musicAmaterasu", this)) {
				this.displayHealth = true;
			}
			if (this.displayHealth) {
				BossStatus.setBossStatus(this, true);
			}
		} else {
			if (this.posY < this.maxHeight) {
				this.motionY = 0.1D;
			}
			
			for (int i = 0; i < this.followupAttacks.size(); i++) {
				FollowupAttack attack = this.followupAttacks.get(i);
				attack.timeLeft--;
				
				if (attack.timeLeft <= 0) {
					switch (attack.atk) {
					case 5: // darkness attack
					{
						for (int k = 0; k < 8; k++) {
							double ang = Math.PI * k / 4.0;
							double xx = attack.x + Math.sin(ang)*2;
							double zz = attack.z + Math.cos(ang)*2;
							ArrowQuest.packetPipeline.sendToDimension(new PacketOneshotAnimation(AnimationHelper.electricStrike.id, xx, attack.y, zz, 2.0F), this.dimension);
						}
						
						int width = 2;
				
						AxisAlignedBB aabb = AxisAlignedBB.fromBounds(attack.x - width, attack.y, attack.z - width, attack.x + width, attack.y + 0.5, attack.z + width);
						List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
				
						if (list != null && !list.isEmpty()) {
							Iterator iterator = list.iterator();
				
							while (iterator.hasNext()) {
								EntityPlayer entity = (EntityPlayer) iterator.next();
								entity.attackEntityFrom(new EntityDamageSource("magic", this), 40.0F + (40.0F * this.worldObj.getDifficulty().getDifficultyId()));
							}
						}
						
						worldObj.playSoundEffect(attack.x, attack.y, attack.z, "arrowquest:sndStrongStrike", 2.0F, 0.9F+0.1F*rand.nextFloat());
						break;
					}
					
					case 7:
					{
						if (attack.val > 0) {
							int preset = 4;
							if (attack.val % 2 == 0) {
								preset = 5;
							}
							
							ArrowQuest.packetPipeline.sendToDimension(new PacketSpawnParticles(preset, (int) attack.x, (int) attack.y, (int) attack.z), this.dimension);
							
							attack.val--;
							attack.timeLeft = 11;
							this.followupAttacks.add(attack);
						}
						else {
							int width = 4;
							
							AxisAlignedBB aabb = AxisAlignedBB.fromBounds(attack.x - width, attack.y, attack.z - width, attack.x + width, attack.y + 1.5, attack.z + width);
							List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
					
							if (list != null && !list.isEmpty()) {
								Iterator iterator = list.iterator();
					
								while (iterator.hasNext()) {
									EntityPlayer entity = (EntityPlayer) iterator.next();
									entity.attackEntityFrom(new EntityDamageSource("magic", this), 80.0F + (80.0F * this.worldObj.getDifficulty().getDifficultyId()));
									entity.addPotionEffect(new PotionEffect(15, 160, 0, false, true));
									entity.addPotionEffect(new PotionEffect(2, 100, 5, true, false));
								}
							}
							
							worldObj.playSoundEffect(attack.x, attack.y, attack.z, "arrowquest:sndEvilSummon", 2.0F, 0.9F+0.1F*rand.nextFloat());
						}
						
						break;
					}
					}
					
					this.followupAttacks.remove(i);
					i--;
				}
			}

			if (getAttackTarget() != null) {
				if (this.attackTimer > 0) {
					this.attackTimer -= 1;
				}

				if (this.attackTimer <= 0) {
					//this.nextAttack = 7;
					
					if (this.nextAttack != 2 && this.nextAttack != 4 && rand.nextInt(5) == 0) {
						worldObj.playSoundEffect(posX, posY, posZ, roarSounds[rand.nextInt(roarSounds.length)], 1000.0F, 0.9F+0.1F*rand.nextFloat());
					}
					
					if (this.nextAttack == 0) { // big doom bullet
						this.attackTimer = 50 + rand.nextInt(30);

						if (this.getHealth() < 250) {
							this.attackTimer /= 2;
						}

						while (this.nextAttack == 0) {
							this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
						}

						this.fireBigDoomBullet(getAttackTarget());
					} else if (this.nextAttack == 1) { // electric orbs
						this.attackTimer = 80 + rand.nextInt(20);
						
						if (this.getHealth() < 250) {
							this.attackTimer /= 2;
						}
						
						while (this.nextAttack == 1) {
							this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
						}

						this.fireElectricOrbs(getAttackTarget());
					} else if (this.nextAttack == 2) { // rapid fire doom bullets
						this.fireDoomBullet(getAttackTarget());

						this.doomBulletIndex++;
						if (this.doomBulletIndex >= 10) {
							this.doomBulletIndex = 0;
							
							while (this.nextAttack == 2) {
								this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
							}
							
							this.attackTimer = 50 + rand.nextInt(30);
							
							if (this.getHealth() < 250) {
								this.attackTimer /= 2;
							}
						} else {
							this.attackTimer = 8;
						}
					} else if (this.nextAttack == 3) { // throw
						/*if (this.throwWarn) {
							this.throwWarn = false;
							
							while (this.nextAttack == 3) {
								this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
							}
							
							this.attackTimer = 60;
							this.lightningTimer = 16;
							
							if (this.getHealth() < 250) {
								this.lightningTimer /= 2;
							}

							List list = worldObj.playerEntities;
							if (list != null && !list.isEmpty()) {
								Iterator iterator = list.iterator();
								while (iterator.hasNext()) {
									EntityPlayer entity = (EntityPlayer) iterator.next();
									if (!entity.isBlocking()) {
										entity.addVelocity(-3.0D + (6.0D * rand.nextFloat()), 2.5D,
												-3.0D + (6.0D * rand.nextFloat()));
										((EntityPlayerMP) entity).playerNetServerHandler
												.sendPacket(new S12PacketEntityVelocity(entity));
										((EntityPlayerMP) entity).playerNetServerHandler
												.sendPacket(new S29PacketSoundEffect("arrowquest:sndSuperQuake",
														entity.posX, entity.posY, entity.posZ, 1.0F,
														0.95F + (0.05F * rand.nextFloat())));
									}
								}
							}
						} else {
							this.throwWarn = true;
							this.attackTimer = 45;

							List list = worldObj.getEntities(EntityPlayer.class, new Predicate() {
								@Override
								public boolean apply(Object input) {
									return true;
								}
							});
							if (list != null && !list.isEmpty()) {
								Iterator iterator = list.iterator();
								while (iterator.hasNext()) {
									EntityPlayer entity = (EntityPlayer) iterator.next();
									((EntityPlayerMP) entity).playerNetServerHandler
											.sendPacket(new S29PacketSoundEffect("arrowquest:sndBell", entity.posX,
													entity.posY, entity.posZ, 1.0F, 1.0F));
								}
							}
						}*/
						
						while (this.nextAttack == 3) {
							this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
						}
						
						this.attackTimer = 3;
					} else if (this.nextAttack == 4) { // fireballs
						this.fireFireball(getAttackTarget());

						this.fireballIndex++;
						if (this.fireballIndex >= 14) {
							this.fireballIndex = 0;
							
							while (this.nextAttack == 4) {
								this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
							}
							
							this.attackTimer = 50 + rand.nextInt(30);
							if (this.getHealth() < 250) {
								this.attackTimer /= 2;
							}
						} else {
							this.attackTimer = 8;
						}
					} else if (this.nextAttack == 5) { // darkness attack
						while (this.nextAttack == 5) {
							this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
						}
						
						this.attackTimer = 50 + rand.nextInt(50);
						
						if (this.getHealth() < 250) {
							this.attackTimer /= 2;
						}
						
						List list = worldObj.getEntities(EntityPlayer.class, new Predicate() {
							@Override
							public boolean apply(Object input) {
								return true;
							}
						});
						
						if (list != null && !list.isEmpty()) {
							Iterator iterator = list.iterator();
							while (iterator.hasNext()) {
								EntityPlayer entity = (EntityPlayer) iterator.next();
								
								this.followupAttacks.add(new FollowupAttack(5, 15, entity.posX, entity.posY, entity.posZ));
								ArrowQuest.packetPipeline.sendToDimension(new PacketOneshotAnimation(AnimationHelper.darknessAttack1.id, entity.posX, entity.posY, entity.posZ, 6.0F), this.dimension);
							}
						}
					} else if (this.nextAttack == 6) { // plasma eruption
						while (this.nextAttack == 6) {
							this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
						}
						
						this.attackTimer = 30 + rand.nextInt(30);
						
						if (this.getHealth() < 250) {
							this.attackTimer /= 2;
						}
						
						List list = worldObj.getEntities(EntityPlayer.class, new Predicate() {
							@Override
							public boolean apply(Object input) {
								return true;
							}
						});
						
						if (list != null && !list.isEmpty()) {
							Iterator iterator = list.iterator();
							while (iterator.hasNext()) {
								EntityPlayer entity = (EntityPlayer) iterator.next();
								
								EntityFlamePillarPurple pillar = new EntityFlamePillarPurple(worldObj, entity.posX, entity.posY, entity.posZ, this, 1);
								worldObj.spawnEntityInWorld(pillar);
								
								worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "arrowquest:sndFlamePillar", 6.0F, 0.92F+0.08F*rand.nextFloat());
							}
						}
					} else if (this.nextAttack == 7) { // dark spirits
						while (this.nextAttack == 7) {
							this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
						}
						
						this.attackTimer = 60 + rand.nextInt(50);
						
						if (this.getHealth() < 250) {
							this.attackTimer /= 2;
						}
						
						List list = worldObj.getEntities(EntityPlayer.class, new Predicate() {
							@Override
							public boolean apply(Object input) {
								return true;
							}
						});
						
						if (list != null && !list.isEmpty()) {
							Iterator iterator = list.iterator();
							while (iterator.hasNext()) {
								EntityPlayer entity = (EntityPlayer) iterator.next();
								
								((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(new S29PacketSoundEffect("arrowquest:sndDarkSpirits", entity.posX,
										entity.posY, entity.posZ, 10.0F, 0.93F+0.08F*rand.nextFloat()));
								
								int num = 4;
								if (this.getHealth() < 250) {
									num = 3;
								}
								
								this.followupAttacks.add(new FollowupAttack(7, 1, entity.posX, entity.posY, entity.posZ, num));
							}
						}
					}
				}
				
				if (this.lightningTimer > 0) {
					this.lightningTimer--;
					
					if (this.lightningTimer <= 0) {
						while (this.nextAttack == 3) {
							this.nextAttack = rand.nextInt(TOTAL_ATTACKS);
						}
						
						this.attackTimer = 30 + rand.nextInt(30);
						
						if (this.getHealth() < 250) {
							this.attackTimer /= 2;
						}

						List list = worldObj.getEntities(EntityPlayer.class, new Predicate() {
							@Override
							public boolean apply(Object input) {
								return true;
							}
						});
						
						if (list != null && !list.isEmpty()) {
							Iterator iterator = list.iterator();
							while (iterator.hasNext()) {
								EntityPlayer entity = (EntityPlayer) iterator.next();
								if (!entity.isBlocking()) {
									EntitySpellLightning lightning = new EntitySpellLightning(worldObj, entity.posX,
											entity.posY, entity.posZ, this);
									lightning.damage = 10.0F
											+ (13.0F * this.worldObj.getDifficulty().getDifficultyId());
									worldObj.addWeatherEffect(lightning);
									entity.addPotionEffect(new PotionEffect(15, 160, 0, false, true));
								}
							}
						}
					}
				}
			}
		}

		super.onLivingUpdate();

		this.floatLevel += 0.0523599F;
		if (this.floatLevel >= 6.28319F) {
			this.floatLevel = 0;
		}
	}
	
	private void removeAllEnemies() {
		List list = worldObj.getEntities(EntityMob.class, new Predicate() {
			@Override
			public boolean apply(Object input) {
				return true;
			}
		});

		if (list != null && !list.isEmpty()) {
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityMob entity = (EntityMob) iterator.next();
				
				if (entity == this)
					continue;
				
				entity.setDead();
			}
		}
	}

	@Override
	protected void onDeathUpdate() {
		this.deathTime++;

		if (this.deathTime == 1) {
			worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndDeathCry", 10000.0F, 1.0F);
			this.removeAllEnemies();
		}

		if (!this.isDead) {
			if (this.deathTime % (10 - Math.floor(this.deathTime / 24)) == 0) {
				worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 1000.0F,
						0.95F + (0.05F * rand.nextFloat()));
				worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (posX - 2) + (rand.nextFloat() * 4),
						(posY - 2) + (rand.nextFloat() * 4) + 7, (posZ - 2) + (rand.nextFloat() * 4), 0.0D, 0.0D, 0.0D,
						new int[0]);
				worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, (posX - 1) + (rand.nextFloat() * 2),
						(posY - 1) + (rand.nextFloat() * 2) + 7, (posZ - 1) + (rand.nextFloat() * 2), 0.0D, 0.0D, 0.0D,
						new int[0]);
			}

			if (this.deathTime >= 200) {
				this.setDead();

				worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndFinalBossExplode", 10000.0F, 1.0F);

				if (!this.worldObj.isRemote) {
					if (this.lootChestPos == null) {
						this.lootChestPos = this.getPosition();
					}

					this.worldObj.setBlockState(this.lootChestPos, ModBlocks.blockPoliceChest.getDefaultState());
					TileEntityChest tile = (TileEntityChest) worldObj.getTileEntity(this.lootChestPos);
					tile.setInventorySlotContents(13, new ItemStack(ModItems.itemTheArrow));

					/*
					BlockPos pos = this.lootChestPos.north();
					this.worldObj.setBlockState(pos, Blocks.chest.getDefaultState());
					tile = (TileEntityChest) worldObj.getTileEntity(pos);
					TileEntityDungeonChest.setChestContents(tile, 3, 2);

					pos = this.lootChestPos.south();
					this.worldObj.setBlockState(pos, Blocks.chest.getDefaultState());
					tile = (TileEntityChest) worldObj.getTileEntity(pos);
					TileEntityDungeonChest.setChestContents(tile, 3, 2);

					pos = this.lootChestPos.up();
					this.worldObj.setBlockState(pos, Blocks.chest.getDefaultState());
					tile = (TileEntityChest) worldObj.getTileEntity(pos);
					TileEntityDungeonChest.setChestContents(tile, 3, 2);
					*/

					ArrowQuest.packetPipeline.sendToDimension(new PacketDimensionMusicUpdate(0, 0),
							ModLib.dimFinalBossID);
				}

				int i;

				if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.func_146066_aG()
						&& this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
					i = this.getExperiencePoints(this.attackingPlayer);
					i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
					while (i > 0) {
						int j = EntityXPOrb.getXPSplit(i);
						i -= j;
						this.worldObj
								.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
					}
				}

				for (i = 0; i < 20; i++) {
					double d2 = this.rand.nextGaussian() * 0.02D;
					double d0 = this.rand.nextGaussian() * 0.02D;
					double d1 = this.rand.nextGaussian() * 0.02D;
					this.worldObj.spawnParticle(EnumParticleTypes.CLOUD,
							this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
							this.posY + (double) (this.rand.nextFloat() * this.height),
							this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2,
							d0, d1, new int[0]);
				}
			}
		}
	}

	private void fireBigDoomBullet(EntityLivingBase target) {
		double d1 = target.posX - posX;
		double d2 = (target.posY + target.getEyeHeight()) - (posY + getEyeHeight());
		double d3 = target.posZ - posZ;
		double d4 = 6.0D;
		for (int i = 0; i < 3; i++) {
			EntityDoomBullet entity = new EntityDoomBullet(worldObj, posX, posY + getEyeHeight(), posZ,
					(d1 - d4 / 2) + rand.nextFloat() * d4, (d2 - d4 / 2) + rand.nextFloat() * d4,
					(d3 - d4 / 2) + rand.nextFloat() * d4, this);
			entity.accelerationX *= 0.2;
			entity.accelerationY *= 0.2;
			entity.accelerationZ *= 0.2;
			entity.damage = 60.0F;
			worldObj.spawnEntityInWorld(entity);
		}
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndGuardianShoot", 10.0F,
				0.95F + 0.05F * rand.nextFloat());
	}

	private void fireDoomBullet(EntityLivingBase target) {
		double d1 = target.posX - posX;
		double d2 = (target.posY + 1) - (posY + getEyeHeight());
		double d3 = target.posZ - posZ;
		double d4 = 0.5D;
		EntityDoomBullet entity = new EntityDoomBullet(worldObj, posX, posY + getEyeHeight(), posZ,
				(d1 - d4 / 2) + rand.nextFloat() * d4, (d2 - d4 / 2) + rand.nextFloat() * d4,
				(d3 - d4 / 2) + rand.nextFloat() * d4, this);
		worldObj.spawnEntityInWorld(entity);
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndPoliceLaser", 10.0F,
				0.95F + 0.05F * rand.nextFloat());
	}

	private void fireElectricOrbs(EntityLivingBase target) {
		double d1 = target.posX - posX;
		double d2 = (target.posY + target.getEyeHeight()) - (posY + getEyeHeight());
		double d3 = target.posZ - posZ;
		double d4 = 25.0D;
		for (int i = 0; i < 4; i++) {
			EntityElectricBall entity = new EntityElectricBall(worldObj, posX, posY + getEyeHeight(), posZ,
					(d1 - d4 / 2) + rand.nextFloat() * d4, (d2 - d4 / 2) + rand.nextFloat() * d4,
					(d3 - d4 / 2) + rand.nextFloat() * d4, this);
			worldObj.spawnEntityInWorld(entity);
		}
	}

	private void fireFireball(EntityLivingBase target) {
		double d1 = target.posX - posX;
		double d2 = (target.posY + 1) - (posY + getEyeHeight());
		double d3 = target.posZ - posZ;
		double d4 = 5.0D;
		d1 = (d1 - d4 / 2) + rand.nextFloat() * d4;
		d2 = (d2 - d4 / 2) + rand.nextFloat() * d4;
		d3 = (d3 - d4 / 2) + rand.nextFloat() * d4;
		double dd = (double) MathHelper.sqrt_double(d1 * d1 + d2 * d2 + d3 * d3) * 4.0;
		d1 = d1 / dd;
		d2 = d2 / dd;
		d3 = d3 / dd;

		EntitySpellFireball fireball = new EntitySpellFireball(worldObj, posX, posY + getEyeHeight(), posZ, d1, d2, d3);
		fireball.shootingEntity = this;
		fireball.damage = 20.0F + (35.0F * this.worldObj.getDifficulty().getDifficultyId());
		worldObj.spawnEntityInWorld(fireball);
		worldObj.playSoundEffect(posX, posY, posZ, "mob.ghast.fireball", 20.0F, 0.95F+0.05F*rand.nextFloat());
	}

	public void spawnEnemies() {
		if (getAttackTarget() != null) {
			BlockPos entityPos = getAttackTarget().getPosition();

			for (int i = 0; i < 1; i++) {
				double spawnX = getAttackTarget().posX;
				double spawnY = getAttackTarget().posY + 1.5D;
				double spawnZ = getAttackTarget().posZ;

				boolean flag = false;
				int tries = 0;
				while (tries < 10) {
					int xx = (entityPos.getX() - 3) + rand.nextInt(7);
					int yy = entityPos.getY() + 1;
					int zz = (entityPos.getZ() - 3) + rand.nextInt(7);
					if (worldObj.getBlockState(new BlockPos(xx, yy, zz)).getBlock() == Blocks.air) {
						spawnX = xx + 0.5D;
						spawnY = yy + 0.5D;
						spawnZ = zz + 0.5D;
						tries = 10;
						flag = true;
						break;
					}

					tries++;
				}

				EntityLivingBase entity = new EntityDoomGuardian(worldObj);
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
				entity.setPosition(spawnX, spawnY, spawnZ);
				worldObj.spawnEntityInWorld(entity);
			}
		}
	}

	@Override
	public void setDead() {
		super.setDead();
		if (!this.worldObj.isRemote) {
			ArrowQuestEntityHelper.distributeBossXP(worldObj, posX, posY, posZ, 2000000, this);
			
			ArrowQuestWorldData.finalBossDefeated = true;
			if (ArrowQuestWorldData.instance != null) {
				ArrowQuestWorldData.instance.markDirty();
			}
		}
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra) {
		for (int i = 0; i < 15; i++) {
			ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(1);
			if (stack != null) {
				this.entityDropItem(stack, 0.0F);
			}
		}

		for (int i = 0; i < 15; i++) {
			ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(2);
			if (stack != null) {
				this.entityDropItem(stack, 0.0F);
			}
		}
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected void despawnEntity() {
	}

	@Override
	protected String getHurtSound() {
		return "mob.wither.hurt";
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isInvincible && this.getHealth() > 250 && !ArrowQuest.DEV_MODE && !source.canHarmInCreative()) {
			return false;
		}

		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);

		if (this.lootChestPos == null) {
			this.lootChestPos = this.getPosition();
		}

		tagCompound.setInteger("LootX", this.lootChestPos.getX());
		tagCompound.setInteger("LootY", this.lootChestPos.getY());
		tagCompound.setInteger("LootZ", this.lootChestPos.getZ());
		tagCompound.setInteger("MaxHeight", this.maxHeight);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		super.readEntityFromNBT(tagCompound);
		int lx = tagCompound.getInteger("LootX");
		int ly = tagCompound.getInteger("LootY");
		int lz = tagCompound.getInteger("LootZ");
		this.lootChestPos = new BlockPos(lx, ly, lz);

		this.maxHeight = tagCompound.getInteger("MaxHeight");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return 15.0F;
	}
}

class FollowupAttack {
	int atk;
	int timeLeft;
	int val;
	
	double x;
	double y;
	double z;
	
	protected FollowupAttack(int atk, int time, double x, double y, double z, int val) {
		this.atk = atk;
		this.timeLeft = time;
		this.x = x;
		this.y = y;
		this.z = z;
		this.val = val;
	}
	
	protected FollowupAttack(int atk, int time, double x, double y, double z) {
		this(atk, time, x, y, z, 0);
	}
}