package com.norfried.eldritch_takeover.client;

import com.norfried.eldritch_takeover.entity.living.EntityStraulokScavenger;
import com.norfried.eldritch_takeover.util.Reference;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderStraulokScavenger extends RenderLiving<EntityStraulokScavenger>
{
    private static final ResourceLocation STRAULOK_SCAVENGER_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entities/straulok/straulok_scavenger.png");

    public RenderStraulokScavenger(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSilverfish(), 0.35F);
        //addLayer(new LayerStraulokScavengerStalks(this));
    }

    protected void preRenderCallback(EntityStraulokScavenger entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(1.15F, 1.15F, 1.15F);
    }

    protected ResourceLocation getEntityTexture(EntityStraulokScavenger entity)
    {
        return STRAULOK_SCAVENGER_TEXTURES;
    }
}
