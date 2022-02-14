package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.tileentity.TileEntityPoliceTeleporter;
import com.cm8check.arrowquest.world.dimension.TeleporterPoliceDimension;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;

public class PacketPoliceTeleporterUse extends AbstractPacket {
	private int destination;
	private int teleporterX;
	private int teleporterY;
	private int teleporterZ;

	public PacketPoliceTeleporterUse() {}

	public PacketPoliceTeleporterUse(int destination, BlockPos teleporterPos) {
		this.destination = destination;
		this.teleporterX = teleporterPos.getX();
		this.teleporterY = teleporterPos.getY();
		this.teleporterZ = teleporterPos.getZ();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeByte(this.destination);
		buffer.writeInt(this.teleporterX);
		buffer.writeInt(this.teleporterY);
		buffer.writeInt(this.teleporterZ);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.destination = buffer.readByte();
		this.teleporterX = buffer.readInt();
		this.teleporterY = buffer.readInt();
		this.teleporterZ = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		if (ArrowQuest.DEV_MODE) {
			TileEntity tile = player.worldObj.getTileEntity(new BlockPos(teleporterX, teleporterY, teleporterZ));
			if (tile instanceof TileEntityPoliceTeleporter) {
				((TileEntityPoliceTeleporter) tile).cycleDestination();
			}
		}
		else {
			System.out.println("Destination: " + this.destination);
			
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			
			if (this.destination == 0) {
				playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, 0, new TeleporterPoliceDimension(playerMP.mcServer.worldServerForDimension(0)));
			}
			else {
				Vec3i pos = TileEntityPoliceTeleporter.destinationCoordinates[this.destination];
				player.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
				player.setSpawnChunk(new BlockPos(pos), true, ModLib.dimPoliceBaseID);
			}
			
			playerMP.playerNetServerHandler.sendPacket(new S29PacketSoundEffect("arrowquest:sndTeleport", player.posX, player.posY, player.posZ, 1.0F, 1.0F));
		}
	}
}