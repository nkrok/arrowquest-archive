package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelFlyingStinger;
import com.cm8check.arrowquest.entity.EntityFlyingStinger;
import com.cm8check.arrowquest.entity.EntityHyperScorpion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFlyingStinger extends RenderLiving{
    private static final ResourceLocation texture = new ResourceLocation("arrowquest", "textures/entity/entityFlyingStinger.png");
    private static final ResourceLocation hyperTexture = new ResourceLocation("arrowquest", "textures/entity/entityHyperScorpion.png");

    public RenderFlyingStinger(){
        super(Minecraft.getMinecraft().getRenderManager(), new ModelFlyingStinger(), 1.0F);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity){
    	if (entity instanceof EntityHyperScorpion){
    		return hyperTexture;
    	}
    	
        return texture;
    }
}