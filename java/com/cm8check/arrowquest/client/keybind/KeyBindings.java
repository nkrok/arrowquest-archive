package com.cm8check.arrowquest.client.keybind;

import org.lwjgl.input.Keyboard;

import com.cm8check.arrowquest.ArrowQuest;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings{
	public static KeyBinding openLevelUpGUI;
	public static KeyBinding cycleWandSpell;
	public static KeyBinding openMagicBackpack;
	public static KeyBinding openAQLogGUI;
	
	public static void registerKeyBindings(){
		openLevelUpGUI = new KeyBinding("key.openLevelUpGUI", Keyboard.KEY_L, "key.categories.arrowquest");
		cycleWandSpell = new KeyBinding("key.cycleWandSpell", Keyboard.KEY_F, "key.categories.arrowquest");
		openMagicBackpack = new KeyBinding("key.openMagicBackpack", Keyboard.KEY_B, "key.categories.arrowquest");
		openAQLogGUI = new KeyBinding("key.openAQLogGUI", Keyboard.KEY_K, "key.categories.arrowquest");
		
		ClientRegistry.registerKeyBinding(openLevelUpGUI);
		ClientRegistry.registerKeyBinding(cycleWandSpell);
		ClientRegistry.registerKeyBinding(openMagicBackpack);
		ClientRegistry.registerKeyBinding(openAQLogGUI);
	}
}