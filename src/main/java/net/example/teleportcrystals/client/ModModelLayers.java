package net.example.teleportcrystals.client;

import net.example.teleportcrystals.TeleportCrystalsMod;
import net.example.teleportcrystals.client.model.SpacialLizardModel;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public final class ModModelLayers {
    private ModModelLayers() {}

    public static final ModelLayerLocation SPACIAL_LIZARD = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(TeleportCrystalsMod.MOD_ID, "spacial_lizard"), "main");

    public static void register() {
        ModelLayerRegistry.registerModelLayer(SPACIAL_LIZARD, SpacialLizardModel::getTexturedModelData);
    }
}
