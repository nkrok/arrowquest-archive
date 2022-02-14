package com.cm8check.arrowquest.item;

import com.cm8check.arrowquest.lib.ModLib;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemLongbow extends ItemBow{
	public ItemLongbow(){
		super();
		this.setMaxDamage(768);
		this.setUnlocalizedName(ModLib.itemLongbowName);
	}
	
	@Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining){
		int time = this.getMaxItemUseDuration(stack) - useRemaining;
        ModelResourceLocation modelresourcelocation = new ModelResourceLocation("arrowquest:"+ModLib.itemLongbowName, "inventory");

        if(stack.getItem() == this && player.getItemInUse() == stack){
            if (time >= 18){
                modelresourcelocation = new ModelResourceLocation("arrowquest:"+ModLib.itemLongbowName+"Pulling2", "inventory");
            }else if (time > 9){
                modelresourcelocation = new ModelResourceLocation("arrowquest:"+ModLib.itemLongbowName+"Pulling1", "inventory");
            }else if (time > 0){
                modelresourcelocation = new ModelResourceLocation("arrowquest:"+ModLib.itemLongbowName+"Pulling0", "inventory");
            }
        }
        
        return modelresourcelocation;
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft){
        int j = this.getMaxItemUseDuration(stack) - timeLeft;
        net.minecraftforge.event.entity.player.ArrowLooseEvent event = new net.minecraftforge.event.entity.player.ArrowLooseEvent(playerIn, stack, j);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;
        j = event.charge;

        boolean flag = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

        if (flag || playerIn.inventory.hasItem(Items.arrow)){
            float f = (float)j / 10.0F;

            if ((double)f < 0.1D){
                return;
            }

            if (f > 2.0F){
                f = 2.0F;
            }

            EntityArrow entityarrow = new EntityArrow(worldIn, playerIn, f);

            if (f == 2.0F){
                entityarrow.setIsCritical(true);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);

            entityarrow.setDamage(3.0D + (double)k + 0.5D);

            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

            if (l > 0)
            {
                entityarrow.setKnockbackStrength(l);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
            {
                entityarrow.setFire(100);
            }

            stack.damageItem(1, playerIn);
            worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (flag)
            {
                entityarrow.canBePickedUp = 2;
            }
            else
            {
                playerIn.inventory.consumeInventoryItem(Items.arrow);
            }

            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);

            if (!worldIn.isRemote)
            {
                worldIn.spawnEntityInWorld(entityarrow);
            }
        }
    }
}