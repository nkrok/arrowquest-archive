package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.entity.EntityHyperBlaze;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHyperBlaze extends RenderLiving{
    private static final ResourceLocation blazeTexture = new ResourceLocation("arrowquest", "textures/entity/entityHyperBlaze.png");

    public RenderHyperBlaze(){
        super(Minecraft.getMinecraft().getRenderManager(), new ModelBlaze(), 0.5F);
    }
    
    protected ResourceLocation getEntityTexture(EntityHyperBlaze entity){
        return blazeTexture;
    }
    
    protected ResourceLocation getEntityTexture(Entity entity){
        return this.getEntityTexture((EntityHyperBlaze) entity);
    }
    
    @Override
    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_){
    	
    }
}