package net.example.teleportcrystals.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Cheap, disposable teleport crystal: crafted from amethyst shards, an
 * iron ingot and ender pearls. Consumed (shrinks the stack by one) the
 * moment it's used to teleport - it does not have durability.
 */
public class TeleportStoneItem extends TeleportCrystalItem {

    public TeleportStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void onUsed(ItemStack stack, ServerPlayer player) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }
}
