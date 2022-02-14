package com.cm8check.arrowquest.entity;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.client.gui.GuiHudOverlay;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketGuiPopup;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;
import com.cm8check.arrowquest.tileentity.TileEntityAirlockController;
import com.cm8check.arrowquest.world.ArrowQuestWorldData;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityPoliceBoss extends EntityAQBoss implements IBossDisplayData, IRangedAttackMob {
	private EntityAIArrowAttack arrowAttack = new EntityAIArrowAttack(this, 0.3D, 30, 40, 25.0F);
	private EntityAIAttackOnCollide meleeAttack = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false);

	private int nextAtk;
	private int atkCooldown;
	private int additionalShots;
	private int additionalShotTimer;
	private boolean isMeleeEngaged;

	public int spawnX;
	public int spawnY;
	public int spawnZ;
	
	private boolean aggro;
	private boolean inAirlock;

	private boolean displayHealth;

	public EntityPoliceBoss(World world) {
		super(world);
		this.experienceValue = 500;

		this.atkCooldown = 80;
		this.nextAtk = 3;

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.arrowAttack);
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate) null));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(256.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(40.0D);
	}
	
	public void setInAirlock(boolean flag) {
		this.inAirlock = flag;
	}

	@Override
	public void onLivingUpdate() {
		if (worldObj.isRemote) {
			if (ClientSoundHelper.bossMusicCheck("arrowquest:musicBowser", this) && !this.displayHealth) {
				GuiHudOverlay.setIndefiniteGuiPopup("Find the Airlock!", "Find the Airlock!");
				this.displayHealth = true;
			}
			
			if (this.displayHealth) {
				BossStatus.setBossStatus(this, true);
			}
		}
		else {
			if (this.inAirlock) {
				this.motionX = 0;
				this.navigator.setSpeed(0);
				
				if (this.worldObj.getClosestPlayer(posX, posY, posZ, 128) == null) {
					this.setDead();
				}
			}
		}

		super.onLivingUpdate();

		if (!worldObj.isRemote && this.deathTime <= 0) {
			if (this.aggro && this.getAttackTarget() == null && !this.inAirlock) {
				EntityPlayer player = this.worldObj.getClosestPlayer(posX, posY, posZ, 256);
				if (player != null) {
					this.setPositionAndUpdate(player.posX, player.posY, player.posZ);
					this.worldObj.playSoundEffect(posX, posY, posZ, "mob.endermen.portal", 1.0F, 0.95F + rand.nextFloat()*0.1F);
					this.setAttackTarget(player);
				}
			}
			
			if (this.getAttackTarget() == null)
				return;
			
			if (!this.aggro) {
				this.aggro = true;
				
				TileEntity tile = this.worldObj.getTileEntity(new BlockPos(159, 94, 232));
				if (tile instanceof TileEntityAirlockController) {
					((TileEntityAirlockController) tile).setEnabled(true);
				}
			}
			
			if (atkCooldown > 0) {
				atkCooldown--;
			}
			else {
				int atk = this.nextAtk;
				
				if (this.inAirlock) {
					atk = rand.nextInt(3);
					this.atkCooldown = 8;
				}
				else {
					this.nextAtk = rand.nextInt(4);
					this.atkCooldown = 20;
				}
				
				if (atk == 0) {
					this.firePlasma(getAttackTarget());
					
					if (rand.nextInt(2) == 0) {
						this.nextAtk = 0;
						this.atkCooldown = 5;
					}
				}
				else if (atk == 1) {
					this.fireMissile(getAttackTarget());
					
					if (rand.nextInt(2) == 0) {
						this.nextAtk = 1;
						this.atkCooldown = 8;
					}
				}
				else if (atk == 2) {
					this.additionalShots = 4 + rand.nextInt(4);
					this.additionalShotTimer = 0;
				}
				else if (atk == 3 && !this.isMeleeEngaged) {
					this.engageMelee();
					this.nextAtk = 10;
					this.atkCooldown = 50;
					this.setAimCooldown(55);
				}
				else if (atk == 10 && this.isMeleeEngaged) {
					this.stopMelee();
					this.atkCooldown = 5;
				}
			}
			
			if (this.isBlocking) {
				this.navigator.setSpeed(0.3);
			}
			else {
				this.navigator.setSpeed(1.0);
			}

			if (additionalShots > 0) {
				if (additionalShotTimer > 0) {
					additionalShotTimer--;
				}
				else {
					additionalShots--;
					additionalShotTimer = 2;

					this.fireLaserAtTarget(getAttackTarget(), 40.0F);
				}
			}
		}
	}

	private EntityGenericProjectile fireLaser(double xDir, double yDir, double zDir, float damage) {
		double d4 = 0.025D;
		EntityGenericProjectile entity = new EntityGenericProjectile(EntityGenericProjectile.TYPE_POLICE_LASER,
				worldObj, posX, posY + getEyeHeight(), posZ, (xDir - d4 / 2) + rand.nextFloat() * d4,
				(yDir - d4 / 2) + rand.nextFloat() * d4, (zDir - d4 / 2) + rand.nextFloat() * d4, 1.8D, this);
		entity.damage = damage;
		worldObj.spawnEntityInWorld(entity);

		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndPoliceLaser", 4.0F,
				0.95F + (0.05F * rand.nextFloat()));

		return entity;
	}

	private EntityGenericProjectile fireLaserAtTarget(EntityLivingBase target, float damage) {
		double d1 = target.posX - posX;
		double d2 = (target.posY + target.getEyeHeight()) - (posY + getEyeHeight());
		double d3 = target.posZ - posZ;
		double d4 = 0.05D;
		EntityGenericProjectile entity = new EntityGenericProjectile(EntityGenericProjectile.TYPE_POLICE_LASER,
				worldObj, posX, posY + getEyeHeight(), posZ, (d1 - d4 / 2) + rand.nextFloat() * d4,
				(d2 - d4 / 2) + rand.nextFloat() * d4, (d3 - d4 / 2) + rand.nextFloat() * d4, 1.8D, this);
		entity.damage = damage;
		worldObj.spawnEntityInWorld(entity);

		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndPoliceLaser", 4.0F,
				0.95F + (0.05F * rand.nextFloat()));

		return entity;
	}
	
	private void fireMissile(EntityLivingBase target) {
		double d1 = target.posX - posX;
		double d2 = (target.posY+0.05D) - (posY+getEyeHeight());
		double d3 = target.posZ - posZ;
		
		double d4 = 0.08D;
		EntityPoliceMissile missile = new EntityPoliceMissile(worldObj, posX, posY + getEyeHeight(), posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4, this);
		worldObj.spawnEntityInWorld(missile);
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndRocket", 4.0F, 0.95F+(0.05F*rand.nextFloat()));
	}
	
	private void firePlasma(EntityLivingBase target) {
		double d1 = target.posX - posX;
		
		double d2 = (target.posY+target.getEyeHeight()) - (posY+getEyeHeight());
		if (d2 > 0) {
			d2 = 0;
		}
		
		double d3 = target.posZ - posZ;
		double d4 = 0.08D;
		
		EntityGenericProjectile entity = new EntityGenericProjectile(EntityGenericProjectile.TYPE_PLASMA_BALL, worldObj, posX, posY + getEyeHeight(), posZ, (d1-d4/2)+rand.nextFloat()*d4, (d2-d4/2)+rand.nextFloat()*d4, (d3-d4/2)+rand.nextFloat()*d4, 0.3D, this);
		entity.damage = 55.0F;
		
		worldObj.spawnEntityInWorld(entity);
		worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndPlasma", 4.0F, 0.93F + rand.nextFloat()*0.1F);
	}

	@Override
	protected void onDeathUpdate() {
		this.deathTime++;

		if (worldObj.isRemote && this.deathTime == 1) {
			ClientSoundHelper.stopMusic();
		}

		if (!this.isDead) {
			if (this.deathTime % (3 - Math.floor(this.deathTime / 51)) == 0) {
				worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 6.0F, 1.0F);
				worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (posX - 2) + (rand.nextFloat() * 4),
						(posY - 2) + (rand.nextFloat() * 4), (posZ - 2) + (rand.nextFloat() * 4), 0.0D, 0.0D, 0.0D,
						new int[0]);
				worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, (posX - 1) + (rand.nextFloat() * 2),
						(posY - 1) + (rand.nextFloat() * 2), (posZ - 1) + (rand.nextFloat() * 2), 0.0D, 0.0D, 0.0D,
						new int[0]);
			}

			if (this.deathTime >= 100) {
				this.setDead();
				worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndBossExplode", 6.0F, 1.0F);

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

	@Override
	protected String getHurtSound() {
		String str = "arrowquest:sndPoliceHurt" + (1 + rand.nextInt(3));
		return str;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		entity.worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndSabreHit", 1.0F,
				0.95F + (0.05F * rand.nextFloat()));
		return super.attackEntityAsMob(entity);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.canHarmInCreative())
			return super.attackEntityFrom(source, amount);
		
		if (!worldObj.isRemote && source.getEntity() instanceof EntityPlayer) {
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "arrowquest:sndSabreHit", 1.5F,
					0.95F + (0.05F * rand.nextFloat()));
			this.setBlockingWithCooldown(15);

			return false;
		}

		return false;
	}

	@Override
	public void setFire(int seconds) {
	}

	@Override
	public void setDead() {
		super.setDead();

		if (!worldObj.isRemote) {
			ArrowQuestEntityHelper.distributeBossXP(worldObj, posX, posY, posZ, 100000, this);

			ArrowQuest.packetPipeline.sendToDimension(new PacketGuiPopup(0), this.dimension);

			ArrowQuestWorldData.policeBossDefeated = true;
			if (ArrowQuestWorldData.instance != null) {
				ArrowQuestWorldData.instance.markDirty();
			}

			BlockPos pos = new BlockPos(this.spawnX, this.spawnY, this.spawnZ);
			worldObj.setBlockState(pos, ModBlocks.blockPoliceChest.getDefaultState());
			TileEntityChest tile = (TileEntityChest) worldObj.getTileEntity(pos);
			tile.setInventorySlotContents(13, new ItemStack(ModItems.itemFinalDestinationKey));
		}
		else {
			//GuiHudOverlay.clearIndefiniteGuiPopup();
			ClientSoundHelper.stopMusic();
		}
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int extra) {
		ItemStack stack = ArrowQuestEntityHelper.getEntityDrop(2);
		if (stack != null) {
			this.entityDropItem(stack, 0.0F);
		}
	}

	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {

	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	private void engageMelee() {
		this.tasks.removeTask(meleeAttack);
		this.tasks.removeTask(arrowAttack);
		this.tasks.addTask(2, meleeAttack);
		this.isMeleeEngaged = true;

		this.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemPoliceSabre));
	}

	private void stopMelee() {
		this.tasks.removeTask(meleeAttack);
		this.tasks.removeTask(arrowAttack);
		this.tasks.addTask(2, arrowAttack);
		this.isMeleeEngaged = false;

		this.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemPoliceGun));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("SpawnX", this.spawnX);
		tagCompound.setInteger("SpawnY", this.spawnY);
		tagCompound.setInteger("SpawnZ", this.spawnZ);
		tagCompound.setBoolean("Aggro", this.aggro);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		super.readEntityFromNBT(tagCompound);
		this.spawnX = tagCompound.getInteger("SpawnX");
		this.spawnY = tagCompound.getInteger("SpawnY");
		this.spawnZ = tagCompound.getInteger("SpawnZ");
		this.aggro = tagCompound.getBoolean("Aggro");
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {

	}
}