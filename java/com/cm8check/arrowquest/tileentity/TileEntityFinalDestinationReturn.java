package com.cm8check.arrowquest.tileentity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.entity.ArrowQuestEntityHelper;
import com.cm8check.arrowquest.item.ModItems;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.network.packet.PacketDimensionMusicUpdate;
import com.cm8check.arrowquest.world.dimension.TeleporterFinalDestination;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class TileEntityFinalDestinationReturn extends TileEntity implements IInventory, IUpdatePlayerListBox{
	private ItemStack inventory;
	
	public static boolean finalBossDefeated = false;
	private int dimCollapseTimer = 100;
	private int spawnTimer = 10;
	private int boomTimer = 60;
	private boolean sirenTriggered = false;
	private boolean firstSpawn = false;
	private int policeSpawnCount = 0;
	
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getName() {
		return "Return Teleporter";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.inventory != null)
        {
            ItemStack itemstack;

            if (this.inventory.stackSize <= count)
            {
                itemstack = this.inventory;
                this.inventory = null;
                return itemstack;
            }
            else
            {
                itemstack = this.inventory.splitStack(count);

                if (this.inventory.stackSize == 0)
                {
                    this.inventory = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index){
		ItemStack itemstack = this.inventory;
        this.inventory = null;
        return itemstack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		this.inventory = stack;
		
		if (!this.worldObj.isRemote && this.worldObj.provider.getDimensionId() == ModLib.dimFinalBossID && this.inventory != null && this.inventory.getItem() == ModItems.itemTheArrow){
			boolean given = false;
			Object[] entities = this.worldObj.playerEntities.toArray();
			if (entities.length > 0){
				for (int j = 0; j < entities.length; j++){
	            	EntityPlayer player = (EntityPlayer) entities[j];
	            	
	            	NBTTagCompound nbt = player.getEntityData().getCompoundTag(player.PERSISTED_NBT_TAG);
	            	if (nbt.getBoolean(ModLib.nbtPlayerCursedFeather)){
	            		nbt.setBoolean(ModLib.nbtPlayerCursedFeather, false);
	            	}
	            	for (int i = 0; i < 36; i++){
    					if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == ModItems.itemCursedFeather){
    						player.inventory.setInventorySlotContents(i, null);
    					}
    					if (!given && player.inventory.getStackInSlot(i) == null){
    						player.inventory.setInventorySlotContents(i, new ItemStack(ModItems.itemTheArrow));
    						given = true;
    					}
    				}
	            	
	            	EntityPlayerMP playerMP = (EntityPlayerMP) player;
	    			playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, 0, new TeleporterFinalDestination(playerMP.mcServer.worldServerForDimension(0)));
	            }
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player){}

	@Override
	public void closeInventory(EntityPlayer player){}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack.getItem() == ModItems.itemTheArrow){
			return true;
		}
		
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		
		compound.setBoolean("FinalBossDefeated", finalBossDefeated);
		compound.setByte("PoliceSpawnCount", (byte) this.policeSpawnCount);
		
		NBTTagCompound nbt = new NBTTagCompound();
		if (this.inventory != null){
			this.inventory.writeToNBT(nbt);
		}
		compound.setTag("Item", nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		
		boolean flag = compound.getBoolean("FinalBossDefeated");
		if (flag){
			finalBossDefeated = true;
			this.dimCollapseTimer = -1;
			this.spawnTimer = 80 + ArrowQuest.RAND.nextInt(40);
		}
		
		this.policeSpawnCount = compound.getByte("PoliceSpawnCount");
		
		NBTTagCompound nbt = compound.getCompoundTag("Item");
		if (nbt.hasKey("Count")){
			this.inventory = ItemStack.loadItemStackFromNBT(nbt);
		}
	}

	@Override
	public void update(){
		if (!this.worldObj.isRemote && finalBossDefeated){
			if (this.dimCollapseTimer > 0){
				if (!this.sirenTriggered){
					this.sirenTriggered = true;
					this.firstSpawn = true;
					this.worldObj.playSoundEffect(this.pos.getX(), this.pos.getY(), this.pos.getZ(), "arrowquest:sndSiren", 10000.0F, 1.0F);
				}
				
				this.dimCollapseTimer--;
				if (this.dimCollapseTimer < 2){
					//ArrowQuest.packetPipeline.sendToDimension(new PacketDimensionMusicUpdate(52, 0), ModLib.dimFinalBossID);
					this.dimCollapseTimer = 0;
					
					this.spawnRandomLava();
					this.spawnRandomLava();
				}
			}else{
				if (this.spawnTimer > 0){
					this.spawnTimer--;
					if (this.spawnTimer < 1){
						this.spawnTimer = 60 + ArrowQuest.RAND.nextInt(90);
						
						boolean spawnedPolice = false;
						
						Iterator iterator = this.worldObj.playerEntities.iterator();
						while (iterator.hasNext()){
							EntityPlayer player = (EntityPlayer) iterator.next();
							if (this.policeSpawnCount < 8 && (this.firstSpawn || ArrowQuest.RAND.nextInt(5) == 0)){
								this.policeSpawnCount++;
								ArrowQuestEntityHelper.spawnElitePoliceSquad(this.worldObj, player);
								if (!spawnedPolice){
									spawnedPolice = true;
									this.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndPoliceSpawn1", 5.0F, 0.95F+(0.05F*ArrowQuest.RAND.nextFloat()));
								}
							}else{
								this.worldObj.createExplosion(null, player.posX, player.posY, player.posZ, 2.0F, true);
			            		this.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "arrowquest:sndMegafreeze", 2.0F, 2.0F);
							}
						}
						
						this.spawnRandomLava();
						this.firstSpawn = false;
					}
				}
				
				if (this.boomTimer > 0){
					this.boomTimer--;
					if (this.boomTimer < 1){
						Random rand = ArrowQuest.RAND;
						this.boomTimer = 6 + rand.nextInt(15);
						
						this.worldObj.playSoundEffect(this.pos.getX() + 13 + rand.nextInt(59), this.pos.getY() - 6, (this.pos.getZ() - 7) + rand.nextInt(45), "arrowquest:sndMegafreeze", 4.0F, 0.8F+rand.nextFloat());
						this.worldObj.playSoundEffect(this.pos.getX() + 13 + rand.nextInt(59), this.pos.getY() - 6, (this.pos.getZ() - 7) + rand.nextInt(45), "random.explode", 4.0F, 0.9F+(0.1F*rand.nextFloat()));
					}
				}
			}
		}
	}
	
	private void spawnRandomLava(){
		int i, xx, yy, zz;
		Random rand = ArrowQuest.RAND;
		for (i = 0; i < 5; i++){
			xx = this.pos.getX() + 13 + rand.nextInt(59);
			yy = this.pos.getY() + 6;
			zz = (this.pos.getZ() - 7) + rand.nextInt(45);
			this.worldObj.setBlockState(new BlockPos(xx, yy, zz), Blocks.flowing_lava.getDefaultState());
		}
	}
	
	public static void startDimensionCollapse(){
		finalBossDefeated = true;
	}
}