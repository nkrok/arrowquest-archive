package com.cm8check.arrowquest.client.renderer.entity;

import com.cm8check.arrowquest.client.model.ModelWizard;
import com.cm8check.arrowquest.client.renderer.entity.animation.AnimationHelper;
import com.cm8check.arrowquest.entity.EntityWizard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWizard extends RenderLiving{
    private static final ResourceLocation texture = new ResourceLocation("arrowquest", "textures/entity/entityWizard.png");

    public RenderWizard(){
        super(Minecraft.getMinecraft().getRenderManager(), new ModelWizard(0.0625F), 1.0F);
        this.addLayer(new LayerHeldItem(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity){
        return texture;
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks){
    	super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
    	
    	EntityWizard wizard = (EntityWizard) entity;
		
		if (wizard.wizardBeamTime > 0 && wizard.wizardBeamTarget != null && wizard.deathTime == 0 && wizard.wizardBeamTargetInRange && wizard.canEntityBeSeen(wizard.wizardBeamTarget)) {
			double targetX = wizard.wizardBeamTarget.lastTickPosX + (wizard.wizardBeamTarget.posX - wizard.wizardBeamTarget.lastTickPosX) * (double)partialTicks;
			double targetY = wizard.wizardBeamTarget.lastTickPosY + (wizard.wizardBeamTarget.posY - wizard.wizardBeamTarget.lastTickPosY) * (double)partialTicks;
			double targetZ = wizard.wizardBeamTarget.lastTickPosZ + (wizard.wizardBeamTarget.posZ - wizard.wizardBeamTarget.lastTickPosZ) * (double)partialTicks;
			
			int count = 10;
			for (int i = 0; i < count; i++) {
				double xx = wizard.posX + ((targetX - wizard.posX) / count) * (i + partialTicks);
				double yy = wizard.posY + entity.getEyeHeight() - 0.2D
						+ (((targetY + 0.3D) - (wizard.posY + entity.getEyeHeight())) / count) * (i + partialTicks);
				double zz = wizard.posZ + ((targetZ - wizard.posZ) / count) * (i + partialTicks);
				
				AnimationHelper.renderAnimation(AnimationHelper.wizardBeamEffect, this, this.renderManager, entity, xx - this.renderManager.viewerPosX, yy - this.renderManager.viewerPosY, zz - this.renderManager.viewerPosZ);
			}
		}
    }
}