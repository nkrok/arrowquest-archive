package com.cm8check.arrowquest.item;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCustomArmor extends ItemArmor{
	public static final ArmorMaterial policeArmor = EnumHelper.addArmorMaterial("itemPoliceArmor", "arrowquest:itemPoliceArmor", 66, new int[]{3, 8, 6, 3}, 30);
	public static final ArmorMaterial trueforceArmor = EnumHelper.addArmorMaterial("itemTrueforceArmor", "arrowquest:itemTrueforceArmor", 164, new int[]{3, 9, 7, 3}, 15);
	public static final ArmorMaterial obsidianArmor = EnumHelper.addArmorMaterial("itemObsidianArmor", "arrowquest:itemObsidianArmor", 33, new int[]{3, 8, 6, 3}, 10);
	public static final ArmorMaterial heavyDutyArmor = EnumHelper.addArmorMaterial("itemHeavyDutyArmor", "arrowquest:itemHeavyDutyArmor", 99, new int[]{3, 9, 6, 3}, 15);
	public static final ArmorMaterial energyArmor = EnumHelper.addArmorMaterial("itemEnergyArmor", "arrowquest:itemEnergyArmor", 0, new int[]{4, 9, 7, 3}, 15);
	
	public ItemCustomArmor(ArmorMaterial material, String name, int renderIndex, int armorType){
		super(material, renderIndex, armorType);
		this.setUnlocalizedName(name);
	}
}