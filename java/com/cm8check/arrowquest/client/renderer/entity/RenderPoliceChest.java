package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.block.BlockPoliceChest;
import com.cm8check.arrowquest.tileentity.TileEntityPoliceChest;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPoliceChest extends TileEntitySpecialRenderer{
    private static final ResourceLocation textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
    private static final ResourceLocation textureNormal = new ResourceLocation("arrowquest", "textures/entity/tileEntityPoliceChest.png");
    private ModelChest simpleChest = new ModelChest();
    private ModelChest largeChest = new ModelLargeChest();

    public void func_180538_a(TileEntityPoliceChest p_180538_1_, double p_180538_2_, double p_180538_4_, double p_180538_6_, float p_180538_8_, int p_180538_9_)
    {
        int j;

        if (!p_180538_1_.hasWorldObj())
        {
            j = 0;
        }
        else
        {
            Block block = p_180538_1_.getBlockType();
            j = p_180538_1_.getBlockMetadata();

            if (block instanceof BlockPoliceChest && j == 0)
            {
                ((BlockPoliceChest) block).checkForSurroundingChests(p_180538_1_.getWorld(), p_180538_1_.getPos(), p_180538_1_.getWorld().getBlockState(p_180538_1_.getPos()));
                j = p_180538_1_.getBlockMetadata();
            }

            p_180538_1_.checkForAdjacentChests();
        }

        if (p_180538_1_.adjacentChestZNeg == null && p_180538_1_.adjacentChestXNeg == null)
        {
            ModelChest modelchest;

            if (p_180538_1_.adjacentChestXPos == null && p_180538_1_.adjacentChestZPos == null)
            {
                modelchest = this.simpleChest;

                if (p_180538_9_ >= 0)
                {
                    this.bindTexture(DESTROY_STAGES[p_180538_9_]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(5888);
                }
                else
                {
                    this.bindTexture(textureNormal);
                }
            }
            else
            {
                modelchest = this.largeChest;

                if (p_180538_9_ >= 0)
                {
                    this.bindTexture(DESTROY_STAGES[p_180538_9_]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(5888);
                }
                else
                {
                    this.bindTexture(textureNormalDouble);
                }
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();

            if (p_180538_9_ < 0)
            {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GlStateManager.translate((float)p_180538_2_, (float)p_180538_4_ + 1.0F, (float)p_180538_6_ + 1.0F);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            short short1 = 0;

            if (j == 2)
            {
                short1 = 180;
            }

            if (j == 3)
            {
                short1 = 0;
            }

            if (j == 4)
            {
                short1 = 90;
            }

            if (j == 5)
            {
                short1 = -90;
            }

            if (j == 2 && p_180538_1_.adjacentChestXPos != null)
            {
                GlStateManager.translate(1.0F, 0.0F, 0.0F);
            }

            if (j == 5 && p_180538_1_.adjacentChestZPos != null)
            {
                GlStateManager.translate(0.0F, 0.0F, -1.0F);
            }

            GlStateManager.rotate((float)short1, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            float f1 = p_180538_1_.prevLidAngle + (p_180538_1_.lidAngle - p_180538_1_.prevLidAngle) * p_180538_8_;
            float f2;

            if (p_180538_1_.adjacentChestZNeg != null)
            {
                f2 = p_180538_1_.adjacentChestZNeg.prevLidAngle + (p_180538_1_.adjacentChestZNeg.lidAngle - p_180538_1_.adjacentChestZNeg.prevLidAngle) * p_180538_8_;

                if (f2 > f1)
                {
                    f1 = f2;
                }
            }

            if (p_180538_1_.adjacentChestXNeg != null)
            {
                f2 = p_180538_1_.adjacentChestXNeg.prevLidAngle + (p_180538_1_.adjacentChestXNeg.lidAngle - p_180538_1_.adjacentChestXNeg.prevLidAngle) * p_180538_8_;

                if (f2 > f1)
                {
                    f1 = f2;
                }
            }

            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;
            modelchest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
            modelchest.renderAll();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (p_180538_9_ >= 0)
            {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }

    public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_)
    {
        this.func_180538_a((TileEntityPoliceChest) p_180535_1_, posX, posZ, p_180535_6_, p_180535_8_, p_180535_9_);
    }
}