package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.entity.EntityRobotSpider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRobotSpider extends RenderSpider{
	private static final ResourceLocation texture = new ResourceLocation("arrowquest", "textures/entity/entityRobotSpider.png");

    public RenderRobotSpider(){
        super(Minecraft.getMinecraft().getRenderManager());
    }
    
    protected ResourceLocation getEntityTexture(EntityRobotSpider entity){
        return texture;
    }

    protected float getDeathMaxRotation(EntityLivingBase p_77037_1_){
        return this.getDeathMaxRotation((EntityRobotSpider) p_77037_1_);
    }
    
    protected ResourceLocation getEntityTexture(Entity entity){
        return this.getEntityTexture((EntityRobotSpider) entity);
    }
}