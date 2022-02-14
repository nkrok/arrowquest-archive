package com.cm8check.arrowquest.world;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class ArrowQuestWorldData extends WorldSavedData{
	private final static String key = "arrowquest";
	public static boolean loaded = false;
	
	//fields
	public static boolean generatedFinalPortal = false;
	public static boolean policeBossDefeated = false;
	public static boolean finalBossDefeated = false;
	public static boolean actualFinalBossDefeated = false;
	
	public static ArrowQuestWorldData instance;
	
	public ArrowQuestWorldData(String name){
		super(name);
	}
	
	public static ArrowQuestWorldData forWorld(World world){
		loaded = true;
		
		MapStorage storage = world.getPerWorldStorage();
		ArrowQuestWorldData result = (ArrowQuestWorldData) storage.loadData(ArrowQuestWorldData.class, key);
		if (result == null){
			result = new ArrowQuestWorldData(key);
			storage.setData(key, result);
		}
		return result;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		generatedFinalPortal = nbt.getBoolean("GeneratedFinalPortal");
		policeBossDefeated = nbt.getBoolean(ModLib.nbtPoliceBossDefeated);
		finalBossDefeated = nbt.getBoolean(ModLib.nbtFinalBossDefeated);
		actualFinalBossDefeated = nbt.getBoolean(ModLib.nbtActualFinalBossDefeated);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		nbt.setBoolean("GeneratedFinalPortal", generatedFinalPortal);
		nbt.setBoolean(ModLib.nbtPoliceBossDefeated, policeBossDefeated);
		nbt.setBoolean(ModLib.nbtFinalBossDefeated, finalBossDefeated);
		nbt.setBoolean(ModLib.nbtActualFinalBossDefeated, actualFinalBossDefeated);
	}
	
	public static void reset() {
		loaded = false;
		
		generatedFinalPortal = false;
		policeBossDefeated = false;
		finalBossDefeated = false;
		actualFinalBossDefeated = false;
	}
}