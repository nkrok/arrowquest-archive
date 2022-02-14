package com.cm8check.arrowquest.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.item.ModItems;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class BigCraftingManager{
    private static final BigCraftingManager instance = new BigCraftingManager();
    private List recipes = new ArrayList();
    
    public static final BigCraftingManager getInstance(){
        return instance;
    }

    private BigCraftingManager(){
    	//staffs
    	this.addRecipe(new ItemStack(ModItems.itemStaff1), new Object[]{
    		"  MW",
    		" MWM",
    		" MM ",
    		"M   ",
    		'M', Items.gold_ingot, 'W', ModItems.itemWand
    	});
    	this.addRecipe(new ItemStack(ModItems.itemStaff2), new Object[]{
    		"  MW",
    		" MWM",
    		" MM ",
    		"M   ",
    		'M', Items.diamond, 'W', ModItems.itemWand
    	});
    	this.addRecipe(new ItemStack(ModItems.itemStaff3), new Object[]{
    		"  MW",
    		" MWM",
    		" MM ",
    		"M   ",
    		'M', Items.emerald, 'W', ModItems.itemWand
    	});
    	
    	//longbow
    	this.addRecipe(new ItemStack(ModItems.itemLongbow), new Object[]{
    		"MS ",
    		"M S",
    		"M S",
    		"MS ",
    		'S', Items.stick, 'M', Items.string
    	});
    	
    	//battle axes
    	this.addRecipe(new ItemStack(ModItems.itemBattleAxeWood), new Object[]{
    		"M M",
    		"MMM",
    		"MSM",
    		" S ",
    		'S', Items.stick, 'M', Blocks.planks
    	});
    	this.addRecipe(new ItemStack(ModItems.itemBattleAxeStone), new Object[]{
    		"M M",
    		"MMM",
    		"MSM",
    		" S ",
    		'S', Items.stick, 'M', Blocks.cobblestone
    	});
    	this.addRecipe(new ItemStack(ModItems.itemBattleAxeIron), new Object[]{
    		"M M",
    		"MMM",
    		"MSM",
    		" S ",
    		'S', Items.stick, 'M', Items.iron_ingot
    	});
    	this.addRecipe(new ItemStack(ModItems.itemBattleAxeGold), new Object[]{
    		"M M",
    		"MMM",
    		"MSM",
    		" S ",
    		'S', Items.stick, 'M', Items.gold_ingot
    	});
    	this.addRecipe(new ItemStack(ModItems.itemBattleAxeDiamond), new Object[]{
    		"M M",
    		"MMM",
    		"MSM",
    		" S ",
    		'S', Items.stick, 'M', Items.diamond
    	});
    	
    	//longswords
    	this.addRecipe(new ItemStack(ModItems.itemLongswordWood), new Object[]{
    		"M",
    		"M",
    		"M",
    		"S",
    		'S', Items.stick, 'M', Blocks.planks
    	});
    	this.addRecipe(new ItemStack(ModItems.itemLongswordStone), new Object[]{
    		"M",
    		"M",
    		"M",
    		"S",
    		'S', Items.stick, 'M', Blocks.cobblestone
    	});
    	this.addRecipe(new ItemStack(ModItems.itemLongswordIron), new Object[]{
    		"M",
    		"M",
    		"M",
    		"S",
    		'S', Items.stick, 'M', Items.iron_ingot
    	});
    	this.addRecipe(new ItemStack(ModItems.itemLongswordGold), new Object[]{
    		"M",
    		"M",
    		"M",
    		"S",
    		'S', Items.stick, 'M', Items.gold_ingot
    	});
    	this.addRecipe(new ItemStack(ModItems.itemLongswordDiamond), new Object[]{
    		"M",
    		"M",
    		"M",
    		"S",
    		'S', Items.stick, 'M', Items.diamond
    	});
    	
    	//hammers
    	this.addRecipe(new ItemStack(ModItems.itemHammerWood), new Object[]{
    		"MMM",
    		"MMM",
    		" S ",
    		" S ",
    		'S', Items.stick, 'M', Blocks.planks
    	});
    	this.addRecipe(new ItemStack(ModItems.itemHammerStone), new Object[]{
    		"MMM",
    		"MMM",
    		" S ",
    		" S ",
    		'S', Items.stick, 'M', Blocks.cobblestone
    	});
    	this.addRecipe(new ItemStack(ModItems.itemHammerIron), new Object[]{
    		"MMM",
    		"MMM",
    		" S ",
    		" S ",
    		'S', Items.stick, 'M', Items.iron_ingot
    	});
    	this.addRecipe(new ItemStack(ModItems.itemHammerGold), new Object[]{
    		"MMM",
    		"MMM",
    		" S ",
    		" S ",
    		'S', Items.stick, 'M', Items.gold_ingot
    	});
    	this.addRecipe(new ItemStack(ModItems.itemHammerDiamond), new Object[]{
    		"MMM",
    		"MMM",
    		" S ",
    		" S ",
    		'S', Items.stick, 'M', Items.diamond
    	});
    	
    	Collections.sort(this.recipes, new Comparator(){
            public int compare(IRecipe p_compare_1_, IRecipe p_compare_2_){
                return p_compare_1_ instanceof BigCraftingShapelessRecipes && p_compare_2_ instanceof BigCraftingShapedRecipes ? 1 : (p_compare_2_ instanceof BigCraftingShapelessRecipes && p_compare_1_ instanceof BigCraftingShapedRecipes ? -1 : (p_compare_2_.getRecipeSize() < p_compare_1_.getRecipeSize() ? -1 : (p_compare_2_.getRecipeSize() > p_compare_1_.getRecipeSize() ? 1 : 0)));
            }
            public int compare(Object p_compare_1_, Object p_compare_2_){
                return this.compare((IRecipe)p_compare_1_, (IRecipe)p_compare_2_);
            }
        });
    }

    public BigCraftingShapedRecipes addRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj){
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (par2ArrayOfObj[i] instanceof String[])
        {
            String[] astring = (String[])((String[])par2ArrayOfObj[i++]);

            for (int l = 0; l < astring.length; ++l)
            {
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }
        else
        {
            while (par2ArrayOfObj[i] instanceof String)
            {
                String s2 = (String)par2ArrayOfObj[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = Maps.newHashMap(); i < par2ArrayOfObj.length; i += 2)
        {
            Character character = (Character)par2ArrayOfObj[i];
            ItemStack itemstack1 = null;

            if (par2ArrayOfObj[i + 1] instanceof Item)
            {
                itemstack1 = new ItemStack((Item)par2ArrayOfObj[i + 1]);
            }
            else if (par2ArrayOfObj[i + 1] instanceof Block)
            {
                itemstack1 = new ItemStack((Block)par2ArrayOfObj[i + 1], 1, 32767);
            }
            else if (par2ArrayOfObj[i + 1] instanceof ItemStack)
            {
                itemstack1 = (ItemStack)par2ArrayOfObj[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1)
        {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0)))
            {
                aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
            }
            else
            {
                aitemstack[i1] = null;
            }
        }

        BigCraftingShapedRecipes shapedrecipes = new BigCraftingShapedRecipes(j, k, aitemstack, par1ItemStack);
        this.recipes.add(shapedrecipes);
        return shapedrecipes;
    }

    public void addShapelessRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj)
    {
        ArrayList arraylist = new ArrayList();
        Object[] aobject = par2ArrayOfObj;
        int i = par2ArrayOfObj.length;

        for (int j = 0; j < i; ++j)
        {
            Object object1 = aobject[j];

            if (object1 instanceof ItemStack)
            {
                arraylist.add(((ItemStack)object1).copy());
            }
            else if (object1 instanceof Item)
            {
                arraylist.add(new ItemStack((Item)object1));
            }
            else
            {
                if (!(object1 instanceof Block))
                {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                arraylist.add(new ItemStack((Block)object1));
            }
        }

        this.recipes.add(new BigCraftingShapelessRecipes(par1ItemStack, arraylist));
    }

    public ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World worldIn){
        Iterator iterator = this.recipes.iterator();
        IRecipe irecipe;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            irecipe = (IRecipe)iterator.next();
        }
        while (!irecipe.matches(p_82787_1_, worldIn));

        return irecipe.getCraftingResult(p_82787_1_);
    }
    
    public ItemStack[] func_180303_b(InventoryCrafting p_180303_1_, World worldIn){
        Iterator iterator = this.recipes.iterator();

        while (iterator.hasNext())
        {
            IRecipe irecipe = (IRecipe)iterator.next();

            if (irecipe.matches(p_180303_1_, worldIn))
            {
                return irecipe.getRemainingItems(p_180303_1_);
            }
        }

        ItemStack[] aitemstack = new ItemStack[p_180303_1_.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i)
        {
            aitemstack[i] = p_180303_1_.getStackInSlot(i);
        }

        return aitemstack;
    }

    public List getRecipeList(){
        return this.recipes;
    }
}