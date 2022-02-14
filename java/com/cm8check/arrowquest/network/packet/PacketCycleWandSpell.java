package com.cm8check.arrowquest.network.packet;

import java.util.ArrayList;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.item.ItemWand;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.player.ArrowQuestPlayer;
import com.cm8check.arrowquest.player.SkillTree;
import com.cm8check.arrowquest.player.SkillTreeEntry;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PacketCycleWandSpell extends AbstractPacket{
	public PacketCycleWandSpell(){}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		
	}

	@Override
	public void handleServerSide(EntityPlayer player){
		ItemStack stack = player.getHeldItem();
		if (stack != null && stack.getItem() instanceof ItemWand){
			ItemWand item = (ItemWand) stack.getItem();
			item.cycleSpell(stack);
		}
	}
}