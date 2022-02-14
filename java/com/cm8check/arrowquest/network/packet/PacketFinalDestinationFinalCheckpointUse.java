package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.tileentity.TileEntityFinalDestinationFinalCheckpoint;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public class PacketFinalDestinationFinalCheckpointUse extends AbstractPacket {
	private int destination;
	private int teleporterX;
	private int teleporterY;
	private int teleporterZ;

	public PacketFinalDestinationFinalCheckpointUse() {}

	public PacketFinalDestinationFinalCheckpointUse(int destination, BlockPos teleporterPos) {
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
			if (tile instanceof TileEntityFinalDestinationFinalCheckpoint) {
				((TileEntityFinalDestinationFinalCheckpoint) tile).cycleDestination();
				System.out.println("Destination: " + ((TileEntityFinalDestinationFinalCheckpoint) tile).getDestination());
			}
		}
		else {
			System.out.println("Destination: " + this.destination);
		}
	}
}