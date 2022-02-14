package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelFlamePillar;
import com.cm8check.arrowquest.entity.EntityFlamePillar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFlamePillar extends Render{
	private static final ResourceLocation texture = new ResourceLocation("arrowquest", "textures/entity/entityFlamePillar.png");
	private static final ResourceLocation texturePurple = new ResourceLocation("arrowquest", "textures/entity/entityFlamePillarPurple.png");
	private static final ResourceLocation texPurpleFire = new ResourceLocation("arrowquest", "textures/animations/purpleFire.png");
	
	private ModelBase model;

    public RenderFlamePillar(){
        super(Minecraft.getMinecraft().getRenderManager());
        this.model = new ModelFlamePillar();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity){
    	EntityFlamePillar pillar = (EntityFlamePillar) entity;
    	
    	if (pillar.getType() == 1) {
    		return texturePurple;
    	}
    	
        return texture;
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks){
    	EntityFlamePillar pillar = (EntityFlamePillar) entity;
    	
    	int ticksAlive = pillar.TOTAL_LIFE - pillar.getLife();
    	if (ticksAlive < 5) {
    		y -= 6 - ((ticksAlive + partialTicks)/5)*6;
    	}
    	
    	GlStateManager.pushMatrix();
    	GlStateManager.translate((float) x, (float) y + 0.6F, (float) z);
        GlStateManager.scale(2, 4, 2);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);
        this.model.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        
        if (pillar.getType() == 1) {
	        renderPurpleFire(entity, x, y, z, partialTicks, entity.width * 1.4F, entity.width * 1.4F);
	        renderPurpleFire(entity, x, y + 3.0F, z, partialTicks, entity.width, entity.width * 1.2F);
        }
        else {
        	renderFire(entity, x, y, z, partialTicks, entity.width * 1.4F, entity.width * 1.4F);
        	renderFire(entity, x, y + 3.0F, z, partialTicks, entity.width, entity.width * 1.2F);
        }
    }
    
    private void renderFire(Entity entity, double x, double y, double z, float partialTicks, float xScale, float yScale) {
        GlStateManager.disableLighting();
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.scale(xScale, yScale, xScale);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f2 = 0.5F;
        float f3 = 0.0F;
        float f4 = entity.height / xScale;
        float f5 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, -0.3F + (float)((int)f4) * 0.02F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f6 = 0.0F;
        int i = 0;
        worldrenderer.startDrawingQuads();

        while (f4 > 0.0F)
        {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f7 = textureatlassprite2.getMinU();
            float f8 = textureatlassprite2.getMinV();
            float f9 = textureatlassprite2.getMaxU();
            float f10 = textureatlassprite2.getMaxV();

            if (i / 2 % 2 == 0)
            {
                float f11 = f9;
                f9 = f7;
                f7 = f11;
            }

            worldrenderer.addVertexWithUV((double)(f2 - f3), (double)(0.0F - f5), (double)f6, (double)f9, (double)f10);
            worldrenderer.addVertexWithUV((double)(-f2 - f3), (double)(0.0F - f5), (double)f6, (double)f7, (double)f10);
            worldrenderer.addVertexWithUV((double)(-f2 - f3), (double)(1.4F - f5), (double)f6, (double)f7, (double)f8);
            worldrenderer.addVertexWithUV((double)(f2 - f3), (double)(1.4F - f5), (double)f6, (double)f9, (double)f8);
            f4 -= 0.45F;
            f5 -= 0.45F;
            f2 *= 0.9F;
            f6 += 0.03F;
            ++i;
        }

        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
    
    private void renderPurpleFire(Entity entity, double x, double y, double z, float partialTicks, float xScale, float yScale) {
        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.scale(xScale, yScale, xScale);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f2 = 0.5F;
        float f3 = 0.0F;
        float f4 = entity.height / xScale;
        float f5 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, -0.3F + (float)((int)f4) * 0.02F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f6 = 0.0F;
        int i = 0;
        worldrenderer.startDrawingQuads();

        while (f4 > 0.0F)
        {
            this.bindTexture(texPurpleFire);
            
            float f7 = 0.5F;
            float f9 = 1.0F;
            
            if (i % 2 == 0) {
            	f7 = 0.0F;
            	f9 = 0.5F;
            }

            int frame = entity.ticksExisted % 32;
            float f8 = 0.03125F * frame;
            float f10 = 0.03125F * (frame + 1);

            if (i / 2 % 2 == 0)
            {
                float f11 = f9;
                f9 = f7;
                f7 = f11;
            }

            worldrenderer.addVertexWithUV((double)(f2 - f3), (double)(0.0F - f5), (double)f6, (double)f9, (double)f10);
            worldrenderer.addVertexWithUV((double)(-f2 - f3), (double)(0.0F - f5), (double)f6, (double)f7, (double)f10);
            worldrenderer.addVertexWithUV((double)(-f2 - f3), (double)(1.4F - f5), (double)f6, (double)f7, (double)f8);
            worldrenderer.addVertexWithUV((double)(f2 - f3), (double)(1.4F - f5), (double)f6, (double)f9, (double)f8);
            f4 -= 0.45F;
            f5 -= 0.45F;
            f2 *= 0.9F;
            f6 += 0.03F;
            ++i;
        }

        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
}