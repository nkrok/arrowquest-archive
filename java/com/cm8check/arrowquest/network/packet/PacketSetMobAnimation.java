package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.entity.EntityDoomGuardian;
import com.cm8check.arrowquest.entity.EntityRaceBase;
import com.cm8check.arrowquest.entity.EntityWizard;
import com.cm8check.arrowquest.entity.EntityWizardBoss;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;

public class PacketSetMobAnimation extends AbstractPacket {
	private int cmd;
	private int entityID;
	private int val;

	public PacketSetMobAnimation() {
	}

	public PacketSetMobAnimation(int cmd, int entityID, int val) {
		this.cmd = cmd;
		this.entityID = entityID;
		this.val = val;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeByte(cmd);
		buffer.writeInt(entityID);
		buffer.writeInt(val);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		cmd = buffer.readByte();
		entityID = buffer.readInt();
		val = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		Entity entity = player.worldObj.getEntityByID(entityID);

		if (entity == null) {
			return;
		}

		switch (cmd) {
		case 0:
			if (entity instanceof EntityRaceBase) {
				EntityRaceBase living = (EntityRaceBase) entity;
				living.aimCooldown = val;
				living.aimTimeRemaining = living.aimTimeRemainBase;
			}

			break;

		case 1:
			if (entity instanceof EntityRaceBase) {
				EntityRaceBase living = (EntityRaceBase) entity;
				living.bossRaiseArms = (val == 1);
			}

			break;

		case 2:
			if (entity instanceof EntityDoomGuardian) {
				EntityDoomGuardian guardian = (EntityDoomGuardian) entity;
				guardian.deathAuraTimer = 120;
			}

			break;

		case 3: {
			Entity target = player.worldObj.getEntityByID(val);

			if (target == null) {
				return;
			}

			int count = 10;
			for (int i = 0; i < count; i++) {
				double xx = entity.posX + ((target.posX - entity.posX) / count) * i;
				double yy = entity.posY + entity.getEyeHeight()
						+ (((target.posY + 1.0D) - (entity.posY + entity.getEyeHeight())) / count) * i;
				double zz = entity.posZ + ((target.posZ - entity.posZ) / count) * i;

				AnimationHelper.spawnOneshotAnimation(entity.worldObj, AnimationHelper.fireLaserComponent, xx, yy - 0.2,
						zz, 1.0F);

				// entity.worldObj.spawnParticle(EnumParticleTypes.FLAME, xx, yy, zz,
				// -0.1+0.2*ArrowQuest.rand.nextFloat(), -0.1+0.2*ArrowQuest.rand.nextFloat(),
				// -0.1+0.2*ArrowQuest.rand.nextFloat(), new int[0]);
				// entity.worldObj.spawnParticle(EnumParticleTypes.REDSTONE, xx, yy, zz, 0.0D,
				// 0.0D, 0.0D, new int[0]);
			}

			break;
		}

		case 4:
			if (entity instanceof EntityRaceBase) {
				EntityRaceBase living = (EntityRaceBase) entity;
				living.isBlocking = (val == 1);
			}

			break;

		case 5:
			if (entity instanceof EntityRaceBase) {
				EntityRaceBase living = (EntityRaceBase) entity;
				living.isBlocking = true;
				living.blockCooldown = val;
			}

			break;

		case 6:
			if (entity instanceof EntityWizardBoss) {
				EntityWizardBoss wizard = (EntityWizardBoss) entity;
				wizard.armState = val;
			}

			break;

		case 7: {
			Entity target = player.worldObj.getEntityByID(val);

			if (target == null) {
				return;
			}

			int count = 10;
			for (int i = 0; i < count; i++) {
				double xx = entity.posX + ((target.posX - entity.posX) / count) * i;
				double yy = entity.posY + entity.getEyeHeight()
						+ (((target.posY + 1.0D) - (entity.posY + entity.getEyeHeight())) / count) * i;
				double zz = entity.posZ + ((target.posZ - entity.posZ) / count) * i;

				AnimationHelper.spawnOneshotAnimation(entity.worldObj, AnimationHelper.policeBeam, xx, yy, zz, 0.5F);
			}

			break;
		}

		case 8: {
			Entity target = player.worldObj.getEntityByID(val);

			if (target == null) {
				return;
			}

			int count = 10;
			for (int i = 0; i < count; i++) {
				double xx = entity.posX + ((target.posX - entity.posX) / count) * i;
				double yy = entity.posY + entity.getEyeHeight()
						+ (((target.posY + 1.0D) - (entity.posY + entity.getEyeHeight())) / count) * i;
				double zz = entity.posZ + ((target.posZ - entity.posZ) / count) * i;

				AnimationHelper.spawnOneshotAnimation(entity.worldObj, AnimationHelper.greenMagicBeam, xx, yy - 0.4, zz,
						1.5F);
			}

			break;
		}

		case 9:
			if (entity instanceof EntityWizard) {
				EntityWizard wizard = (EntityWizard) entity;
				wizard.wizardBeamTime = 115;
				
				Entity target = player.worldObj.getEntityByID(val);
				if (target instanceof EntityLivingBase) {
					wizard.wizardBeamTarget = (EntityLivingBase) target;
				}
			}

			break;
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {

	}
}