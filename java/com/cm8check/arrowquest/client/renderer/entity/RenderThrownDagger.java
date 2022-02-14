package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelThrownDagger;
import com.cm8check.arrowquest.entity.EntityThrownDagger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderThrownDagger extends Render{
	private static final ResourceLocation[] textures = new ResourceLocation[5];
	private ModelBase model;

    public RenderThrownDagger(){
        super(Minecraft.getMinecraft().getRenderManager());
        this.model = new ModelThrownDagger();
        
        textures[0] = new ResourceLocation("arrowquest", "textures/entity/entityThrownDaggerWood.png");
        textures[1] = new ResourceLocation("arrowquest", "textures/entity/entityThrownDaggerStone.png");
        textures[2] = new ResourceLocation("arrowquest", "textures/entity/entityThrownDaggerIron.png");
        textures[3] = new ResourceLocation("arrowquest", "textures/entity/entityThrownDaggerGold.png");
        textures[4] = new ResourceLocation("arrowquest", "textures/entity/entityThrownDaggerDiamond.png");
    }
    
    protected ResourceLocation getEntityTexture(EntityThrownDagger entity){
        return textures[entity.materialIndex];
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity){
        return this.getEntityTexture((EntityThrownDagger) entity);
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks){
    	EntityThrownDagger dagger = (EntityThrownDagger) entity;
    	GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(-dagger.rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(dagger.prevRotationPitch + ((dagger.rotationPitch-dagger.prevRotationPitch)*partialTicks), 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, -0.5F, 0.0F);
        float f2 = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        this.bindEntityTexture(dagger);
        this.model.render(dagger, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f2);
        GlStateManager.popMatrix();
    	
    	super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
    }
}