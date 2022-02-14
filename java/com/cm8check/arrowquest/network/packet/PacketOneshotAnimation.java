package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PacketOneshotAnimation extends AbstractPacket {
	private int id;
	private float x;
	private float y;
	private float z;
	private float scale;

	public PacketOneshotAnimation() {

	}

	public PacketOneshotAnimation(int id, double x, double y, double z, float scale) {
		this.id = id;
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
		this.scale = scale;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeByte(id);
		buffer.writeFloat(x);
		buffer.writeFloat(y);
		buffer.writeFloat(z);
		buffer.writeFloat(scale);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		id = buffer.readByte();
		x = buffer.readFloat();
		y = buffer.readFloat();
		z = buffer.readFloat();
		scale = buffer.readFloat();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		AnimationHelper.spawnOneshotAnimation(player.worldObj, AnimationHelper.getAnimationByID(id), x,
				y, z, scale);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {

	}
}