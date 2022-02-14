package com.cm8check.arrowquest;

import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.block.ModBlocks;
import com.cm8check.arrowquest.client.audio.ClientSoundHelper;
import com.cm8check.arrowquest.client.keybind.KeyBindings;
import com.cm8check.arrowquest.client.keybind.KeyInputHandler;
import com.cm8check.arrowquest.event.ArrowQuestEventTracker;
import com.cm8check.arrowquest.event.FMLArrowQuestEventTracker;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.network.PacketPipeline;
import com.cm8check.arrowquest.player.SkillTree;
import com.cm8check.arrowquest.player.XPRegistry;
import com.cm8check.arrowquest.proxy.ClientProxy;
import com.cm8check.arrowquest.proxy.CommonProxy;
import com.cm8check.arrowquest.world.ArrowQuestWorldGenerator;
import com.cm8check.arrowquest.world.gen.WorldGenSchematics;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "arrowquest", version = "1.22", name = "ArrowQuest")
public class ArrowQuest {
	@Instance("arrowquest")
	public static ArrowQuest instance;

	@SidedProxy(clientSide = "com.cm8check.arrowquest.proxy.ClientProxy", serverSide = "com.cm8check.arrowquest.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static final Random RAND = new Random();

	public static final PacketPipeline packetPipeline = new PacketPipeline();

	public static final boolean DEV_MODE = false;
	public static int savedMobType = 0;
	public static int savedMobLevel = 0;
	public static int savedChestType = 0;
	public static int savedChestGrade = 0;

	public static List<Byte> levelUpSkillList;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			FMLCommonHandler.instance().bus().register(new KeyInputHandler());
			KeyBindings.registerKeyBindings();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new ArrowQuestWorldGenerator(), 50);
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		ModBlocks.init();
		ModItems.init();
		proxy.registerTileEntities();
		proxy.registerEntities();
		proxy.registerRecipes();
		XPRegistry.registerXPValues();
		SkillTree.createSkillTree();

		packetPipeline.initalise();

		if (event.getSide() == Side.CLIENT) {
			ClientProxy.init();
			ClientSoundHelper.init();
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		packetPipeline.postInitialise();

		FMLCommonHandler.instance().bus().register(new FMLArrowQuestEventTracker());
		MinecraftForge.EVENT_BUS.register(new ArrowQuestEventTracker());

		ForgeChunkManager.setForcedChunkLoadingCallback(this, new LoadingCallback() {
			@Override
			public void ticketsLoaded(List<Ticket> tickets, World world) {}
		});

		proxy.registerDimensions();
		WorldGenSchematics.loadSchematics();
	}
}