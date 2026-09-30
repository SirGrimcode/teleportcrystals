package net.example.teleportcrystals.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * More expensive teleport crystal: crafted from amethyst shards, a
 * netherite ingot, blaze rods and ender pearls. Has durability instead of
 * being consumed, and is enchantable with Unbreaking / Mending (see the
 * enchantable/durability item tag under data/minecraft/tags).
 */
public class TeleportWandItem extends TeleportCrystalItem {

    public TeleportWandItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void onUsed(ItemStack stack, ServerPlayer player) {
        if (player.getAbilities().instabuild) {
            return;
        }
        if (player.level() instanceof ServerLevel serverLevel) {
            stack.hurtAndBreak(1, serverLevel, player, item -> {
                // Called when the item breaks; vanilla tools just no-op
                // here beyond the built-in break sound/particle.
            });
        }
    }
}
