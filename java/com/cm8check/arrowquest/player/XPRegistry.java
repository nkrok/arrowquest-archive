package com.cm8check.arrowquest.player;

import java.util.ArrayList;
import java.util.List;

import com.cm8check.arrowquest.lib.ModLib;

public class XPRegistry{
	private static ArrayList<Integer> xpValues = new ArrayList<Integer>();
	private static ArrayList<String> xpValueMobs = new ArrayList<String>();
	
	public static void registerXPValues(){
		register("arrowquest."+ModLib.entityPirateCaptainName, 15); //200
		
		register("arrowquest."+ModLib.entityCastleSoldierName, 200);
		register("arrowquest."+ModLib.entityCastleArcherName, 200);
		register("arrowquest."+ModLib.entityPoliceName, 1500);
		register("arrowquest."+ModLib.entityObsidianWarriorName, 6000);
		register("arrowquest."+ModLib.entityPhantasmName, 6000);
		register("arrowquest."+ModLib.entityBanishedSoulName, 6000);
		register("arrowquest."+ModLib.entityOrcName, 30);
		register("arrowquest."+ModLib.entityPirateName, 12);
		register("arrowquest."+ModLib.entityDwarfName, 30);
		register("arrowquest."+ModLib.entityElfName, 30);
		register("arrowquest."+ModLib.entityVampireName, 22);
		register("arrowquest."+ModLib.entityFurnaceMonsterName, 25);
		register("arrowquest."+ModLib.entityFlyingStingerName, 15);
		register("arrowquest."+ModLib.entityNetherOrcName, 300);
		register("arrowquest."+ModLib.entityRobotSpiderName, 30);
		register("arrowquest."+ModLib.entityWizardName, 12000);
		register("arrowquest."+ModLib.entityDoomGuardianName, 40000);
		register("arrowquest."+ModLib.entityHyperScorpionName, 8000);
		register("arrowquest."+ModLib.entityFinalBossCoreName, 80000);
		
		register("EnderDragon", 100000);
		register("WitherBoss", 1000);
		register("Creeper", 16);
		register("Skeleton", 13);
		register("Spider", 8);
		register("Zombie", 10);
		register("Slime", 2);
		register("Ghast", 40);
		register("PigZombie", 12);
		register("Enderman", 40);
		register("CaveSpider", 8);
		register("Silverfish", 2);
		register("Blaze", 10);
		register("LavaSlime", 3);
		register("Witch", 50);
		register("Endermite", 3);
		register("Guardian", 10);
	}
	
	public static int getEntityXPValue(String name){
		if (xpValueMobs.contains(name.toString())){
			return xpValues.get(xpValueMobs.indexOf(name));
		}
		return 1;
	}
	
	private static void register(String name, int xp){
		xpValues.add(xp);
		xpValueMobs.add(name);
	}
}