package com.cm8check.arrowquest.network.packet;

import java.util.Random;

import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class PacketSpawnParticles extends AbstractPacket{
	private int preset;
	private int x;
	private int y;
	private int z;
	
	public PacketSpawnParticles(){}
	public PacketSpawnParticles(int preset, int x, int y, int z){
		this.preset = preset;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeByte(preset);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		preset = buffer.readByte();
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		Random rand = new Random();
		
		World world = player.worldObj;
		
		switch(preset){
			case 0:
				int width = 3;
				int height = 1;
				
				world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
				for (int i = -height; i <= height; i++){
					for (int j = -width; j <= width; j++){
						for (int k = -width; k <= width; k++){
							world.spawnParticle(EnumParticleTypes.WATER_SPLASH, x+j, y+i, z+k, 0.0D, 0.0D, 0.0D, new int[0]);
							world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, x+j, y+i, z+k, 0.0D, 0.0D, 0.0D, new int[0]);
						}
					}
				}
			break;
			
			case 1:
				world.spawnParticle(EnumParticleTypes.HEART, x, y+2, z, rand.nextGaussian()-0.5, rand.nextGaussian()-0.5, rand.nextGaussian()-0.5, new int[0]);
			break;
			
			case 2:
				int amt = 32;
				float f = (180/amt);
				for (int i = 0; i < amt; i++){
					world.spawnParticle(EnumParticleTypes.FLAME, x, y+2, z, Math.sin(f*i), 0.0D, Math.cos(f*i), new int[0]);
				}
			break;
			
			case 3:
				int width1 = 10;
				int height1 = 1;
				
				world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
				for (int i = -height1; i <= height1; i++){
					for (int j = -width1; j <= width1; j++){
						for (int k = -width1; k <= width1; k++){
							world.spawnParticle(EnumParticleTypes.FLAME, x+j, y+i, z+k, 0.0D, 0.0D, 0.0D, new int[0]);
							world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x+j, y+i, z+k, 0.0D, 0.0D, 0.0D, new int[0]);
						}
					}
				}
			break;
			
			case 4:
			{
				for (int i = -4; i < 5; i += 2) {
					for (int k = -4; k < 5; k += 2) {
						AnimationHelper.spawnOneshotAnimation(world, AnimationHelper.darkSpirit, x + i, y - 0.7, z + k, 3.0F);
					}
				}
				
				break;
			}
			
			case 5:
			{
				for (int i = -3; i < 5; i += 2) {
					for (int k = -3; k < 5; k += 2) {
						AnimationHelper.spawnOneshotAnimation(world, AnimationHelper.darkSpirit, x + i, y - 0.5, z + k, 2.0F);
					}
				}
				
				break;
			}
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		
	}
}