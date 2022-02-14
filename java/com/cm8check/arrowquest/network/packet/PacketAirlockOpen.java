package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.tileentity.TileEntityAirlockController;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public class PacketAirlockOpen extends AbstractPacket {
	private BlockPos airlockPos;
	
	public PacketAirlockOpen() {}
	
	public PacketAirlockOpen(BlockPos airlockPos) {
		this.airlockPos = airlockPos;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(airlockPos.getX());
		buffer.writeInt(airlockPos.getY());
		buffer.writeInt(airlockPos.getZ());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		this.airlockPos = new BlockPos(x, y, z);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		TileEntity tile = player.worldObj.getTileEntity(airlockPos);
		
		if (tile instanceof TileEntityAirlockController) {
			TileEntityAirlockController airlock = (TileEntityAirlockController) tile;
			
			if (airlock.isEnabled()) {
				airlock.setOpen(true);
				player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndAirlockOpen", 6.0F, 1.0F);
			}
		}
	}
}