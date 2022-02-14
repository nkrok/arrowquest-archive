package com.cm8check.arrowquest.tileentity;

import java.util.List;
import java.util.Random;

import com.cm8check.arrowquest.ArrowQuest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;

public class TileEntityFireChest extends TileEntityChest{
	private int explodeTimer = -1;
	private Random rand = ArrowQuest.RAND;
	
	@Override
	public void update(){
		if (worldObj.isRemote){
			int xx = pos.getX();
			int yy = pos.getY();
			int zz = pos.getZ();
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, xx+rand.nextFloat(), yy+0.5F, zz+rand.nextFloat(), 0.0D, 0.0D, 0.0D, new int[0]);
			worldObj.spawnParticle(EnumParticleTypes.FLAME, xx+rand.nextFloat(), yy+0.5F, zz+rand.nextFloat(), 0.0D, 0.1D, 0.0D, new int[0]);
		}
		else {
			if (this.explodeTimer > 0){
				this.explodeTimer--;
			}else if (this.explodeTimer == 0){
				this.explodeTimer = -1;
				worldObj.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3.0F, true);
				worldObj.setBlockToAir(pos);
				worldObj.removeTileEntity(pos);
				
				AxisAlignedBB aabb = AxisAlignedBB.fromBounds(pos.getX() - 3, pos.getY() - 3, pos.getZ() - 3, pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3);
				List<EntityPlayer> list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
				
				for (EntityPlayer player : list) {
					player.attackEntityFrom(new DamageSource("magic"), 400.0F);
				}
			}
		}
		
		super.update();
	}
	
	@Override
	public void openInventory(EntityPlayer player){
		if (this.explodeTimer == -1) {
			this.explodeTimer = 50;
			worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "game.tnt.primed", 1.0F, 1.0F);
		}
		
		super.openInventory(player);
	}
}