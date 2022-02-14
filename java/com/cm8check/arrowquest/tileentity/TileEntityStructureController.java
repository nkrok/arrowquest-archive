package com.cm8check.arrowquest.tileentity;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.lib.ModLib;
import com.cm8check.arrowquest.world.gen.Schematic;
import com.cm8check.arrowquest.world.gen.WorldGenSchematics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;

public class TileEntityStructureController extends TileEntity implements IUpdatePlayerListBox {
	private int spawnersLeft;
	private Entity[] finalEntities;
	private boolean hasBoss;
	private String schematicName;
	
	private int clearDelay;
	
	private static final int SPAWNER_CLEAR_THRESHOLD = 4;
	
	public void init(int spawnerCount, String schematicName) {
		this.spawnersLeft = spawnerCount;
		this.schematicName = schematicName;
	}
	
	@Override
	public void update() {
		if (this.clearDelay > 0) {
			this.clearDelay--;
			
			if (this.clearDelay <= 0) {
				this.doClear();
			}
		}
		else {
			if (!this.hasBoss && this.finalEntities != null) {
				boolean allDead = true;
				
				for (int i = 0; i < this.finalEntities.length; i++) {
					if (this.finalEntities[i] != null) {
						if (this.finalEntities[i].isDead) {
							this.finalEntities[i] = null;
						}
						else {
							allDead = false;
						}
					}
				}
				
				if (allDead) {
					this.structureCleared();
				}
			}
		}
	}

	public void structureCleared() {
		System.out.println("Clear");
		this.clearDelay = 30;
	}
	
	public void doClear() {
		if (this.schematicName != null && !this.schematicName.equals("")) {
			Schematic schematic = WorldGenSchematics.getSchematicByName(this.schematicName);
			int width = schematic.width;
			int height = schematic.height;
			int length = schematic.length;
			
			int size = width * length * height;
			
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(pos.getX() - width, pos.getY() - 10, pos.getZ() - length, pos.getX() + width, pos.getY() + height + 10, pos.getZ() + length);
			List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			
			if (list != null && !list.isEmpty()) {
				Iterator iterator = list.iterator();
				
	            while (iterator.hasNext()) {
	            	EntityPlayer player = (EntityPlayer) iterator.next();
	            	player.addChatMessage(new ChatComponentText("Structure cleared!"));
	            	
	            	String snd, tag;
	            	if (size > WorldGenSchematics.STRUCTURE_SIZE_LARGE) {
	            		snd = "sndClearL";
	            		tag = ModLib.nbtTier3StructuresCleared;
	            	}
	            	else if (size > WorldGenSchematics.STRUCTURE_SIZE_MEDIUM) {
	            		snd = "sndClearM";
	            		tag = ModLib.nbtTier2StructuresCleared;
	            	}
	            	else {
	            		snd = "sndClearS";
	            		tag = ModLib.nbtTier1StructuresCleared;
	            	}
	            	
	            	WorldGenSchematics.logClearStructure(player, schematicName, tag);
	            	
	            	((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S29PacketSoundEffect("arrowquest:" + snd,
	            			player.posX, player.posY, player.posZ, 10.0F, 0.95F + (0.05F * ArrowQuest.RAND.nextFloat())));
	            }
			}
		}
		
		worldObj.setBlockToAir(pos);
	}
	
	protected void spawnerSpawned(Entity entity) {
		if (!this.hasBoss) {
			if (this.spawnersLeft <= SPAWNER_CLEAR_THRESHOLD && this.spawnersLeft > 0) {
				if (this.finalEntities == null) {
					this.finalEntities = new Entity[SPAWNER_CLEAR_THRESHOLD];
				}
				
				this.finalEntities[SPAWNER_CLEAR_THRESHOLD - this.spawnersLeft] = entity;
			}
			
			this.spawnersLeft--;
			
			if (this.spawnersLeft < 0) {
				this.spawnersLeft = 0;
			}
		}
	}
	
	protected void setHasBoss(boolean hasBoss) {
		this.hasBoss = hasBoss;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.spawnersLeft = compound.getShort("SpawnersLeft");
		this.hasBoss = compound.getBoolean("HasBoss");
		this.schematicName = compound.getString("SchematicName");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setShort("SpawnersLeft", (short) this.spawnersLeft); 
		compound.setBoolean("HasBoss", this.hasBoss);
		compound.setString("SchematicName", this.schematicName);
	}
}