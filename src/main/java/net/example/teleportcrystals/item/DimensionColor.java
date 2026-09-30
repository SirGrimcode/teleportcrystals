package net.example.teleportcrystals.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/** Maps a bound dimension to a plain display name for chat messages. */
public final class DimensionColor {
    private DimensionColor() {}

    public static String displayNameFor(ResourceKey<Level> dimension) {
        if (dimension.equals(Level.OVERWORLD)) {
            return "Overworld";
        } else if (dimension.equals(Level.NETHER)) {
            return "Nether";
        } else if (dimension.equals(Level.END)) {
            return "End";
        }
        return dimension.identifier().toString();
    }
}
