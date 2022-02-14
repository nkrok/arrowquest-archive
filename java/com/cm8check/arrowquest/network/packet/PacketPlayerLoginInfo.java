package com.cm8check.arrowquest.network.packet;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketPlayerLoginInfo extends AbstractPacket{
	private int xp;
	private int levelXP;
	private int level;
	private int levelsToSpend;
	private int atk;
	private int def;
	private int speed;
	private int jump;
	private int magicDMG;
	private boolean selectRace;
	private int dimID;
	
	public PacketPlayerLoginInfo(){}
	public PacketPlayerLoginInfo(int xp, int levelXP, int level, int levelsToSpend, int atk, int def, int speed, int jump, int magicDMG, boolean selectRace, int dim){
		this.xp = xp;
		this.levelXP = levelXP;
		this.level = level;
		this.levelsToSpend = levelsToSpend;
		this.atk = atk;
		this.def = def;
		this.speed = speed;
		this.jump = jump;
		this.magicDMG = magicDMG;
		this.selectRace = selectRace;
		this.dimID = dim;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeInt(xp);
		buffer.writeInt(levelXP);
		buffer.writeShort(level);
		buffer.writeByte(levelsToSpend);
		buffer.writeByte(atk);
		buffer.writeByte(def);
		buffer.writeByte(speed);
		buffer.writeByte(jump);
		buffer.writeByte(magicDMG);
		buffer.writeBoolean(selectRace);
		buffer.writeByte(dimID);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		xp = buffer.readInt();
		levelXP = buffer.readInt();
		level = buffer.readShort();
		levelsToSpend = buffer.readByte();
		atk = buffer.readByte();
		def = buffer.readByte();
		speed = buffer.readByte();
		jump = buffer.readByte();
		magicDMG = buffer.readByte();
		selectRace = buffer.readBoolean();
		dimID = buffer.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		ArrowQuestPlayer.localPlayerXP = xp;
		ArrowQuestPlayer.localPlayerLevelXP = levelXP;
		ArrowQuestPlayer.localPlayerLevel = level;
		ArrowQuestPlayer.localPlayerLevelsToSpend = levelsToSpend;
		ArrowQuestPlayer.localPlayerATK = atk;
		ArrowQuestPlayer.localPlayerDEF = def;
		ArrowQuestPlayer.localPlayerSpeed = speed;
		ArrowQuestPlayer.localPlayerJumpHeight = jump;
		ArrowQuestPlayer.localPlayerMagicDMG = magicDMG;
		ArrowQuestPlayer.selectRace = selectRace;
		
		if (dimID >= ModLib.dimPoliceBaseID){
			ClientSoundHelper.playCustomMusicByID(dimID);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		
	}
}