package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelWizard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWizardBoss extends RenderLiving{
    private static final ResourceLocation texture = new ResourceLocation("arrowquest", "textures/entity/entityWizardBoss.png");

    public RenderWizardBoss(){
        super(Minecraft.getMinecraft().getRenderManager(), new ModelWizard(0.125F), 1.0F);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity){
        return texture;
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks){
    	super.doRender(entity, x, y+1.5, z, p_76986_8_, partialTicks);
    }
}