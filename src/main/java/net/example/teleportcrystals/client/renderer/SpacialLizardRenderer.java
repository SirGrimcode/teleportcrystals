package net.example.teleportcrystals.client.renderer;

import net.example.teleportcrystals.TeleportCrystalsMod;
import net.example.teleportcrystals.client.ModModelLayers;
import net.example.teleportcrystals.client.model.SpacialLizardModel;
import net.example.teleportcrystals.client.state.SpacialLizardRenderState;
import net.example.teleportcrystals.entity.SpacialLizard;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class SpacialLizardRenderer extends MobRenderer<SpacialLizard, SpacialLizardRenderState, SpacialLizardModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(TeleportCrystalsMod.MOD_ID, "textures/entity/spacial_lizard.png");

    public SpacialLizardRenderer(EntityRendererProvider.Context context) {
        super(context, new SpacialLizardModel(context.bakeLayer(ModModelLayers.SPACIAL_LIZARD)), 0.8F);
    }

    @Override
    public SpacialLizardRenderState createRenderState() {
        return new SpacialLizardRenderState();
    }

    @Override
    public Identifier getTextureLocation(SpacialLizardRenderState state) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(SpacialLizard entity, SpacialLizardRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.diveAnimationState.copyFrom(entity.diveAnimationState);
        state.surfaceAnimationState.copyFrom(entity.surfaceAnimationState);
        state.lickAnimationState.copyFrom(entity.lickAnimationState);
        state.walkAnimationState.copyFrom(entity.walkAnimationState);
    }
}
