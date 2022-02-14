package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.gui.GuiAQLog;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.world.ArrowQuestWorldData;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class PacketAQLogOpen extends AbstractPacket {
	public PacketAQLogOpen() {
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

	}

	@Override
	public void handleClientSide(EntityPlayer player) {

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
		GuiAQLog.LogInfo.structuresCleared = nbt.getShort(ModLib.nbtStructuresCleared);
		GuiAQLog.LogInfo.tier1StructuresCleared = nbt.getByte(ModLib.nbtTier1StructuresCleared);
		GuiAQLog.LogInfo.tier2StructuresCleared = nbt.getByte(ModLib.nbtTier2StructuresCleared);
		GuiAQLog.LogInfo.tier3StructuresCleared = nbt.getByte(ModLib.nbtTier3StructuresCleared);
		GuiAQLog.LogInfo.tier4StructuresCleared = nbt.getByte(ModLib.nbtTier4StructuresCleared);

		GuiAQLog.LogInfo.bosses = nbt.getByte(ModLib.nbtBossesKilled);
		GuiAQLog.LogInfo.artifactWeapons = nbt.getByte(ModLib.nbtArtifactWeaponsAcquired);
		GuiAQLog.LogInfo.scrolls = nbt.getByte(ModLib.nbtScrollsFound);

		GuiAQLog.LogInfo.objective = determineObjective(nbt);
		nbt.setByte(ModLib.nbtObjective, (byte) GuiAQLog.LogInfo.objective);
		GuiAQLog.LogInfo.objectiveProgress = nbt.getByte(ModLib.nbtObjectiveProgress);

		ArrowQuest.packetPipeline.sendTo(new PacketAQLogInfo(), (EntityPlayerMP) player);
	}

	private int determineObjective(NBTTagCompound nbt) {
		if ((!nbt.getBoolean(ModLib.nbtPlayerHasBeatenHumanBoss)
				|| !nbt.getBoolean(ModLib.nbtPlayerHasBeatenElfBoss)
				|| !nbt.getBoolean(ModLib.nbtPlayerHasBeatenDwarfBoss)
				|| !nbt.getBoolean(ModLib.nbtPlayerHasBeatenOrcBoss))
				&& !nbt.getBoolean(ModLib.nbtPlayerHasBeatenBlazeBoss)) {
			return 0;
		}
		
		if (!nbt.getBoolean(ModLib.nbtPlayerHasBeatenBlazeBoss)) {
			return 1;
		}
		
		if (!nbt.getBoolean(ModLib.nbtTPCTeleporterFound)) {
			return 2;
		}
		
		if (!ArrowQuestWorldData.policeBossDefeated) {
			return 3;
		}
		
		if (!nbt.getBoolean(ModLib.nbtFinalDestReached)) {
			return 4;
		}
		
		if (!ArrowQuestWorldData.finalBossDefeated) {
			return 5;
		}
		
		if (!ArrowQuestWorldData.actualFinalBossDefeated) {
			return 6;
		}

		return 7;
	}
}