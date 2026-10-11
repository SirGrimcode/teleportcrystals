package net.example.teleportcrystals.client;

import net.example.teleportcrystals.client.renderer.SpacialLizardRenderer;
import net.example.teleportcrystals.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class TeleportCrystalsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModModelLayers.register();
        EntityRenderers.register(ModEntities.SPACIAL_LIZARD, SpacialLizardRenderer::new);
    }
}
