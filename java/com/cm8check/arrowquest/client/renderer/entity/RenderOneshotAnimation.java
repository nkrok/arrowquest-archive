package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelElectricOrb;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.entity.EntityOneshotAnimation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderOneshotAnimation extends Render {
    public RenderOneshotAnimation(){
        super(Minecraft.getMinecraft().getRenderManager());
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
    	if (!entity.isDead) {
	    	EntityOneshotAnimation entityAnimation = (EntityOneshotAnimation) entity;
	    	
	    	if (entityAnimation.getAnimation().isGroundAnimation) {
	    		AnimationHelper.renderGroundAnimation(entityAnimation.getAnimation(), this, entity, x, y, z, entityAnimation.getScale());
	    	}
	    	else {
	    		float zTranslate = 0.0F;
	    		if (entityAnimation.getAnimation() == AnimationHelper.heal1 || entityAnimation.getAnimation() == AnimationHelper.heal2) {
	    			zTranslate = -0.3F;
	    		}
	    		
	    		AnimationHelper.renderAnimation(entityAnimation.getAnimation(), this, this.renderManager, entity, x, y, z, entityAnimation.getScale(), zTranslate);
	    	}
    	}
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}
}