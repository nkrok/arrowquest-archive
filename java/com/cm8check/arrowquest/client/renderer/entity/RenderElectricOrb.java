package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelElectricOrb;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderElectricOrb extends Render{
	private static final ResourceLocation texture = new ResourceLocation("arrowquest", "textures/entity/entityElectricOrb.png");
	private ModelBase model;

    public RenderElectricOrb(){
        super(Minecraft.getMinecraft().getRenderManager());
        this.model = new ModelElectricOrb();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity){
        return texture;
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks){
    	GlStateManager.pushMatrix();
    	GlStateManager.translate((float) x, (float) y - 2.3F, (float) z);
        GlStateManager.scale(2, 2, 2);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);
        this.model.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        
        AnimationHelper.renderAnimation(AnimationHelper.electricOrb, this, this.renderManager, entity, x, y - 0.6, z, 2.0F, -0.3F);
    }
}