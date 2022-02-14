package com.cm8check.arrowquest.entity;

import java.util.Iterator;
import java.util.List;

import com.cm8check.arrowquest.ArrowQuest;
import com.cm8check.arrowquest.network.packet.PacketSetMobAnimation;
import com.cm8check.arrowquest.network.packet.PacketSpawnParticles;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFlamePillar extends Entity {
	private EntityLivingBase shootingEntity;
	private int life;
	private int wrath;
	private int type;
	
	public final int TOTAL_LIFE = 80;

	public EntityFlamePillar(World world) {
		super(world);
		this.setSize(2.0F, 2.0F);
		this.life = TOTAL_LIFE;
	}

	public EntityFlamePillar(World world, double x, double y, double z, EntityLivingBase attacker, int wrathCount) {
		this(world);

		this.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
		this.shootingEntity = attacker;
		this.wrath = wrathCount;

		//world.createExplosion(attacker, posX, posY + 1, posZ, 2.0F, false);

		if (!world.isRemote && world.isAreaLoaded(new BlockPos(this), 10)) {
			ArrowQuest.packetPipeline.sendToAllAround(new PacketSpawnParticles(0, (int) x, (int) y + 1, (int) z), new TargetPoint(attacker.dimension, x, y, z, 32));
			world.playSoundEffect(posX, posY, posZ, "random.explode", 6.0F, 0.95F+(0.05F*rand.nextFloat()));
			
			BlockPos blockpos = new BlockPos(this);

			if (world.getBlockState(blockpos).getBlock().getMaterial() == Material.air
					&& Blocks.fire.canPlaceBlockAt(world, blockpos)) {
				world.setBlockState(blockpos, Blocks.fire.getDefaultState());
			}

			for (int i = 0; i < 4; i++) {
				BlockPos blockpos1 = blockpos.add(this.rand.nextInt(3) - 1, 0, this.rand.nextInt(3) - 1);

				if (world.getBlockState(blockpos1).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(world, blockpos1)) {
					world.setBlockState(blockpos1, Blocks.fire.getDefaultState());
				}
			}
		}
	}

	@Override
	public void onUpdate() {
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		
		this.life--;
		if (this.life <= 0) {
			this.setDead();
		}
		
		if (!worldObj.isRemote) {
			if (this.life == 50) {
				worldObj.playSoundEffect(posX, posY, posZ, "arrowquest:sndFlamePillar", 1, 0.92F + 0.08F * rand.nextFloat());
			}
			
			float width = 2.5F;
			float height = 4.0F;
	
			AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - width, posY, posZ - width, posX + width, posY + height, posZ + width);
			List list;
			
			if (type == 1) {
				list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			}
			else {
				list = worldObj.getEntitiesWithinAABB(EntityLiving.class, aabb);
			}
	
			if (list != null && !list.isEmpty()) {
				Iterator iterator = list.iterator();
	
				while (iterator.hasNext()) {
					EntityLivingBase entity = (EntityLivingBase) iterator.next();
					
					if (type == 1) {
						entity.attackEntityFrom(new EntityDamageSource("magic", shootingEntity), 20.0F * this.worldObj.getDifficulty().getDifficultyId());
					}
					else {
						entity.addPotionEffect(new PotionEffect(2, 15, 4, true, false));
						entity.attackEntityFrom(new EntityDamageSource("magic", shootingEntity), 20.0F);
					}
					
					entity.setFire(7);
				}
			}
			
			if (wrath > 0 && life % 10 == 0) {
				width = 8.0F;
				height = 3.0F;
				
				aabb = AxisAlignedBB.fromBounds(posX - width, posY - height, posZ - width, posX + width, posY + height, posZ + width);
				
				if (type == 1) {
					list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
				}
				else {
					list = worldObj.getEntitiesWithinAABB(EntityLiving.class, aabb);
				}
		
				if (list != null && !list.isEmpty()) {
					Entity entity = (Entity) list.get(rand.nextInt(list.size()));
					
					if (type == 1) {
						EntityFlamePillarPurple pillar = new EntityFlamePillarPurple(worldObj, entity.posX, entity.posY, entity.posZ, shootingEntity, 0);
						worldObj.spawnEntityInWorld(pillar);
					}
					else {
						EntityFlamePillar pillar = new EntityFlamePillar(worldObj, entity.posX, entity.posY, entity.posZ, shootingEntity, 0);
						worldObj.spawnEntityInWorld(pillar);
					}
					
					worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "arrowquest:sndFlamePillar", 6.0F, 0.92F+0.08F*rand.nextFloat());
				}
				
				wrath--;
			}
		}
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompound) {
		this.life = tagCompound.getShort("lifeRemaining");
		this.wrath = tagCompound.getByte("wrathCount");
		this.type = tagCompound.getByte("pillarType");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		tagCompound.setShort("lifeRemaining", (short) this.life);
		tagCompound.setByte("wrathCount", (byte) this.wrath);
		tagCompound.setByte("pillarType", (byte) this.type);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}
	
	public int getLife() {
		return life;
	}
	
	protected void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
}