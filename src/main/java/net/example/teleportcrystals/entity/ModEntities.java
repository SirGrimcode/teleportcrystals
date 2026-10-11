package net.example.teleportcrystals.entity;

import net.example.teleportcrystals.TeleportCrystalsMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
    private ModEntities() {}

    public static final ResourceKey<EntityType<?>> SPACIAL_LIZARD_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(TeleportCrystalsMod.MOD_ID, "spacial_lizard"));

    // Hitbox from the model's hitbox cube: about 1.6 wide, 0.7 tall.
    public static final EntityType<SpacialLizard> SPACIAL_LIZARD = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            SPACIAL_LIZARD_KEY,
            EntityType.Builder.<SpacialLizard>of(SpacialLizard::new, MobCategory.MONSTER)
                    .sized(1.6F, 0.7F)
                    .build(SPACIAL_LIZARD_KEY));

    public static void init() {
        FabricDefaultAttributeRegistry.register(SPACIAL_LIZARD, SpacialLizard.createLizardAttributes());
    }
}
