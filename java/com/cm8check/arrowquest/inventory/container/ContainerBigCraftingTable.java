package com.cm8check.arrowquest.inventory.container;

import com.cm8check.arrowquest.crafting.BigCraftingManager;
import com.cm8check.arrowquest.inventory.InventoryBigCraftingTable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerBigCraftingTable extends Container{
	private InventoryCrafting craftMatrix = new InventoryCrafting(this, 4, 4);
	private InventoryBigCraftingTable craftResult = new InventoryBigCraftingTable();
	private World worldObj;
	
	public ContainerBigCraftingTable(InventoryPlayer playerInventory){
		this.worldObj = playerInventory.player.worldObj;
		
		int i;
		int i1;
		
		//crafting output
		this.addSlotToContainer(new SlotBigCraftingResult(playerInventory.player, craftMatrix, craftResult, 0, 124, 49));
		//crafting inventory
		for (i = 0; i < 4; i++){
			for (i1 = 0; i1 < 4; i1++){
				this.addSlotToContainer(new Slot(craftMatrix, i1 + i * 4, 12 + i1 * 18, 21 + i * 18));
			}
		}
		
		//player inventory
		for (i = 0; i < 3; i++){
			for (i1 = 0; i1 < 9; i1++){
				this.addSlotToContainer(new Slot(playerInventory, i1 + i * 9 + 9, 8 + i1 * 18, 106 + i * 18));
			}
		}
		//player hotbar
		for (i = 0; i < 9; i++){
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 164));
		}
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory){
		ItemStack stack = BigCraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj);
		craftResult.custom = true;
		if (stack == null){
			stack = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj);
			craftResult.custom = false;
		}
        this.craftResult.setInventorySlotContents(0, stack);
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        super.onContainerClosed(player);

        if (!this.worldObj.isRemote){
            for (int i = 0; i < 16; ++i){
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

                if (itemstack != null){
                	player.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn){
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index){
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0){ //result
                if (!this.mergeItemStack(itemstack1, 17, 53, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }else if (index >= 17 && index < 44){ //main player inventory
                if (!this.mergeItemStack(itemstack1, 44, 53, false))
                {
                    return null;
                }
            }else if (index >= 44 && index < 53){ //player hotbar
                if (!this.mergeItemStack(itemstack1, 17, 44, false))
                {
                    return null;
                }
            }else if (!this.mergeItemStack(itemstack1, 17, 53, false)){
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot){
        return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
    }
}

class SlotBigCraftingResult extends SlotCrafting{
	private final InventoryCrafting craftMatrix;
	private final EntityPlayer thePlayer;
	private final InventoryBigCraftingTable craftingResult;
	
	public SlotBigCraftingResult(EntityPlayer player, InventoryCrafting craftingInventory, IInventory craftingResult, int slotIndex, int xPosition, int yPosition){
		super(player, craftingInventory, craftingResult, slotIndex, xPosition, yPosition);
		this.thePlayer = player;
		this.craftMatrix = craftingInventory;
		this.craftingResult = (InventoryBigCraftingTable) craftingResult;
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack){
        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, craftMatrix);
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(playerIn);
        ItemStack[] aitemstack;
        if (craftingResult.custom){
        	aitemstack = BigCraftingManager.getInstance().func_180303_b(this.craftMatrix, playerIn.worldObj);
        }else{
        	aitemstack = CraftingManager.getInstance().func_180303_b(this.craftMatrix, playerIn.worldObj);
        }
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

        for (int i = 0; i < aitemstack.length; ++i)
        {
            ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);
            ItemStack itemstack2 = aitemstack[i];

            if (itemstack1 != null)
            {
                this.craftMatrix.decrStackSize(i, 1);
            }

            if (itemstack2 != null)
            {
                if (this.craftMatrix.getStackInSlot(i) == null)
                {
                    this.craftMatrix.setInventorySlotContents(i, itemstack2);
                }
                else if (!this.thePlayer.inventory.addItemStackToInventory(itemstack2))
                {
                    this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
                }
            }
        }
    }
}