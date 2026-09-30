package net.example.teleportcrystals.block;

import net.example.teleportcrystals.TeleportCrystalsMod;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public final class ModBlocks {
    private ModBlocks() {}

    // Hardness/resistance sit a bit above diamond ore (3.0F/3.0F) since
    // this is meant to be rarer/tougher-feeling; adjust to taste.
    public static final Block SPACIAL_ORE = register("spacial_ore",
            properties -> new Block(properties
                    .mapColor(net.minecraft.world.level.material.MapColor.COLOR_LIGHT_BLUE)
                    .soundType(SoundType.AMETHYST_CLUSTER)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 6.0F)));

    // Same id/property pattern as ModItems.register - the Block's own
    // registry key has to be set on its Properties *before* construction,
    // same reason as items (see ModItems for the fuller explanation).
    private static Block register(String path, Function<BlockBehaviour.Properties, Block> factory) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(TeleportCrystalsMod.MOD_ID, path));
        Block block = factory.apply(BlockBehaviour.Properties.of().setId(key));
        Block registered = Registry.register(BuiltInRegistries.BLOCK, key, block);
        registerBlockItem(path, registered);
        return registered;
    }

    private static void registerBlockItem(String path, Block block) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(TeleportCrystalsMod.MOD_ID, path));
        Item.Properties properties = new Item.Properties().setId(itemKey).useBlockDescriptionPrefix();
        Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, properties));
    }

    public static void init() {
        // Loot table (silk touch -> the block itself, otherwise shards
        // scaling with Fortune) lives at
        // data/teleportcrystals/loot_table/blocks/spacial_ore.json.
        // World generation is wired up separately in ModWorldGen.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(output -> {
            output.accept(SPACIAL_ORE);
        });
    }
}
