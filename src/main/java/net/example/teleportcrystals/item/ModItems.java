package net.example.teleportcrystals.item;

import net.example.teleportcrystals.TeleportCrystalsMod;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public final class ModItems {
    private ModItems() {}

    // Unbound copies can stack like ender pearls; once bound, differing
    // component data (the saved location) keeps them from stacking with
    // differently-bound copies automatically.
    public static final Item TELEPORT_STONE = register("teleport_stone",
            properties -> new TeleportStoneItem(properties.stacksTo(16)));

    // Tool-like: one at a time, has durability, and can take Unbreaking /
    // Mending (see the enchantable/durability item tag).
    public static final Item TELEPORT_CRYSTAL = register("teleport_crystal",
            properties -> new TeleportWandItem(properties.stacksTo(1).durability(24)));

    // Dropped by mining teleportcrystals:spacial_ore (see ModBlocks and
    // the spacial_ore loot table); replaces amethyst shards in the
    // Netherite (teleport_crystal) recipe.
    public static final Item SPACIAL_SHARD = register("spacial_shard",
            properties -> new Item(properties.stacksTo(64)));

    // An item's Properties must have its own registry id set via .setId(...)
    // *before* the item is constructed - the item's constructor reads it
    // immediately (for its description id) and throws a NullPointerException
    // ("Item id not set") if it's missing. So the key has to exist first,
    // and the item is built from a factory rather than passed in ready-made.
    private static Item register(String path, Function<Item.Properties, Item> factory) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(TeleportCrystalsMod.MOD_ID, path));
        Item item = factory.apply(new Item.Properties().setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void init() {
        // Registers the field initializers above and adds both items to
        // the creative "Tools & Utilities" tab.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            output.accept(TELEPORT_STONE);
            output.accept(TELEPORT_CRYSTAL);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(SPACIAL_SHARD);
        });
    }
}
