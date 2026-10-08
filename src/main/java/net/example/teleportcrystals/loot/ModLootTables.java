package net.example.teleportcrystals.loot;

import net.example.teleportcrystals.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

/**
 * Adds spacial shards to the real vanilla End City treasure loot table
 * (minecraft:chests/end_city_treasure - the same table both End City
 * room chests and End Ship chests use), via Fabric's LootTableEvents.MODIFY
 * rather than replacing the table outright. This keeps every vanilla
 * drop (elytra, enchanted gear, etc.) and anything another datapack or
 * mod adds - we're only ever adding one extra pool on top, never
 * discarding what's already there.
 *
 * The 25% chance here is a guess, not something specified - change the
 * value passed to randomChance(...) to tune it (0.0-1.0).
 */
public final class ModLootTables {
    private ModLootTables() {}

    private static final Identifier END_CITY_TREASURE = Identifier.fromNamespaceAndPath("minecraft", "chests/end_city_treasure");

    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key.identifier().equals(END_CITY_TREASURE) && source.isBuiltin()) {
                LootPool.Builder pool = LootPool.lootPool()
                        .add(LootItem.lootTableItem(ModItems.SPACIAL_SHARD))
                        .when(LootItemRandomChanceCondition.randomChance(0.25F));
                // No setRolls(): a LootPool builder already defaults to exactly 1 roll.
                tableBuilder.pool(pool.build());
            }
        });
    }
}
