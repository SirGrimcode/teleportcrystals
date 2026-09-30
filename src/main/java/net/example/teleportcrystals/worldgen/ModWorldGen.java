package net.example.teleportcrystals.worldgen;

import net.example.teleportcrystals.TeleportCrystalsMod;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Hooks the Spacial Ore placed feature (see
 * data/teleportcrystals/worldgen/placed_feature/spacial_ore.json) into the
 * three "outer island" End biomes only - deliberately NOT the central main
 * island ("minecraft:the_end") and NOT "minecraft:small_end_islands" (the
 * scattered tiny islands out in the void), since the ask was specifically
 * for the outer islands and nothing floating loose in the void.
 *
 * Using Fabric's BiomeModifications API (fabric-biome-api-v1, already a
 * dependency via fabric-api) rather than overriding the vanilla biome JSON
 * files directly - that would require re-specifying an entire biome
 * definition just to add one feature, and would silently stop merging with
 * any other mod/datapack that also touches these biomes.
 */
public final class ModWorldGen {
    private ModWorldGen() {}

    private static final ResourceKey<PlacedFeature> SPACIAL_ORE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath(TeleportCrystalsMod.MOD_ID, "spacial_ore"));

    public static void init() {
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.END_BARRENS),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                SPACIAL_ORE_PLACED_KEY);
    }
}
