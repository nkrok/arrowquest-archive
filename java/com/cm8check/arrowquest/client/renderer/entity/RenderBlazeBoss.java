package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelBlazeBoss;
import com.cm8check.arrowquest.entity.EntityBlazeBoss;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlazeBoss extends RenderLiving{
    private static final ResourceLocation blazeTexture = new ResourceLocation("arrowquest", "textures/entity/entityBlazeBoss.png");

    public RenderBlazeBoss(){
        super(Minecraft.getMinecraft().getRenderManager(), new ModelBlazeBoss(), 1.0F);
    }
    
    protected ResourceLocation getEntityTexture(EntityBlazeBoss entity){
        return blazeTexture;
    }
    
    protected ResourceLocation getEntityTexture(Entity entity){
        return this.getEntityTexture((EntityBlazeBoss) entity);
    }
    
    @Override
    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_){
    	
    }
}