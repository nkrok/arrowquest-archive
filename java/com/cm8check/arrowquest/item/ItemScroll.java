package com.cm8check.arrowquest.item;

import java.util.List;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemScroll extends Item{
	public static String[] scrollTextureNames = new String[]{
		"arrowquest:"+ModLib.itemScrollFireName,
		"arrowquest:"+ModLib.itemScrollFireName, //fireball
		"arrowquest:"+ModLib.itemScrollWaterName, //puddle
		"arrowquest:"+ModLib.itemScrollEarthName, //quake
		"arrowquest:"+ModLib.itemScrollAirName, //jump I
		"arrowquest:"+ModLib.itemScrollAirName, //jump II
		"arrowquest:"+ModLib.itemScrollAirName, //jump III
		"arrowquest:"+ModLib.itemScrollLifeName, //self heal I
		"arrowquest:"+ModLib.itemScrollLifeName, //self heal II
		"arrowquest:"+ModLib.itemScrollLifeName, //self heal III
		"arrowquest:"+ModLib.itemScrollLifeName, //AoE heal I
		"arrowquest:"+ModLib.itemScrollLifeName, //AoE heal II
		"arrowquest:"+ModLib.itemScrollLifeName, //AoE heal III
		"arrowquest:"+ModLib.itemScrollAirName, //lightning
		"arrowquest:"+ModLib.itemScrollFireName, //lava puddle
		"arrowquest:"+ModLib.itemScrollWaterName, //megafreeze
		"arrowquest:"+ModLib.itemScrollFireName, //fireproof aura
		"arrowquest:"+ModLib.itemScrollEarthName, //temporary block
		"arrowquest:"+ModLib.itemScrollEarthName, //earthen blitz
		"arrowquest:"+ModLib.itemScrollFireName, //fire punch
		"arrowquest:"+ModLib.itemScrollAirName, //lightning strike
		"arrowquest:"+ModLib.itemScrollFireName, //explo-fireball
		"arrowquest:"+ModLib.itemScrollFireName //explo-tri-fireball
	};
	
	public static String[] scrollDescriptions = new String[]{
		"",
		"Shoot a basic fireball", //fireball
		"Spawn a water source", //puddle
		"Summon a force that knocks back nearby enemies", //quake
		"Leap in the direction you are looking", //jump I
		"Leap in the direction you are looking", //jump II
		"Leap in the direction you are looking", //jump III
		"Heal yourself for 0.5 hearts", //self heal I
		"Heal yourself for 1 heart", //self heal II
		"Heal yourself for 1.5 hearts", //self heal III
		"Heal nearby players for 0.5 hearts", //AoE heal I
		"Heal nearby players for 1 heart", //AoE heal II
		"Heal nearby players for 1.5 hearts", //AoE heal III
		"Summon lightning where you are pointing", //lightning
		"Spawn a lava source", //lava puddle
		"Summon an AoE burst of damage", //megafreeze
		"Give nearby players temporary Fire Resistance", //fireproof aura
		"Place a block that disappears after 6 seconds", //temporary block
		"Launch nearby enemies high into the air", //earthen blitz
		"Deal instant ranged damage to a mob", //fire punch
		"Strike all nearby enemies with lightning", //lightning strike
		"Shoot an exploding fireball", //explo-fireball
		"Shoot three exploding fireballs" //explo-tri-fireball
	};
	
	protected ItemScroll(){
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setUnlocalizedName(ModLib.itemScrollName);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		if (!world.isRemote && stack.getMetadata() > 0 && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
			if (!nbt.hasKey("AQFoundScroll" + stack.getMetadata())) {
				nbt.setBoolean("AQFoundScroll" + stack.getMetadata(), true);
				nbt.setByte(ModLib.nbtScrollsFound, (byte) (nbt.getByte(ModLib.nbtScrollsFound) + 1));
			}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return ModLib.itemScrollName + "_" + stack.getMetadata();
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		for (int i = 0; i < scrollTextureNames.length; ++i){
            list.add(new ItemStack(item, 1, i));
        }
	}
}