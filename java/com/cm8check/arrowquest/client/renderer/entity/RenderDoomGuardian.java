package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelDoomGuardian;
import com.cm8check.arrowquest.entity.EntityDoomGuardian;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDoomGuardian extends RenderLiving{
    private static final ResourceLocation texture = new ResourceLocation("arrowquest", "textures/entity/entityDoomGuardian.png");

    public RenderDoomGuardian(){
        super(Minecraft.getMinecraft().getRenderManager(), new ModelDoomGuardian(), 1.0F);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity){
        return texture;
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks){
    	EntityDoomGuardian guardian = (EntityDoomGuardian) entity;
    	super.doRender(entity, x, y+0.7+Math.sin(guardian.floatLevel)*0.2, z, p_76986_8_, partialTicks);
    }
}