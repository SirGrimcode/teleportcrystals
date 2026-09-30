package net.example.teleportcrystals.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Reads and writes the bound teleport location on an item stack, and
 * drives which of the 4 baked textures shows via the item's custom model
 * data "strings" list (see assets/teleportcrystals/items/teleport_*.json,
 * which uses the current minecraft:select item-model system).
 */
public final class TeleportData {
    private static final String KEY_X = "TpX";
    private static final String KEY_Y = "TpY";
    private static final String KEY_Z = "TpZ";
    private static final String KEY_DIM = "TpDim";

    private TeleportData() {}

    public static void save(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(KEY_X, pos.getX());
        nbt.putInt(KEY_Y, pos.getY());
        nbt.putInt(KEY_Z, pos.getZ());
        nbt.putString(KEY_DIM, dimension.identifier().toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

        stack.set(DataComponents.CUSTOM_MODEL_DATA,
                new CustomModelData(List.of(), List.of(), List.of(modelStateFor(dimension)), List.of()));
    }

    private static String modelStateFor(ResourceKey<Level> dimension) {
        if (dimension.equals(Level.OVERWORLD)) {
            return "overworld";
        } else if (dimension.equals(Level.NETHER)) {
            return "nether";
        } else if (dimension.equals(Level.END)) {
            return "end";
        }
        return "clear";
    }

    public static void clear(ItemStack stack) {
        stack.remove(DataComponents.CUSTOM_DATA);
        stack.remove(DataComponents.CUSTOM_MODEL_DATA);
    }

    public static boolean hasLocation(ItemStack stack) {
        return read(stack) != null;
    }

    /** @return the saved location, or null if this stack has none bound. */
    public static Saved read(ItemStack stack) {
        CustomData component = stack.get(DataComponents.CUSTOM_DATA);
        if (component == null) {
            return null;
        }

        CompoundTag nbt = component.copyTag();
        if (!nbt.contains(KEY_X) || !nbt.contains(KEY_DIM)) {
            return null;
        }

        // Since 1.21.5, CompoundTag's no-arg-fallback getters (getInt(key),
        // getString(key), etc) return Optional<T>. The fallback-taking
        // overload is a differently-named method - getIntOr(key, fallback) -
        // not a getInt(key, fallback) overload.
        BlockPos pos = new BlockPos(
                nbt.getIntOr(KEY_X, 0),
                nbt.getIntOr(KEY_Y, 0),
                nbt.getIntOr(KEY_Z, 0));

        Identifier dimId = Identifier.tryParse(nbt.getStringOr(KEY_DIM, ""));
        if (dimId == null) {
            return null;
        }

        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, dimId);
        return new Saved(pos, dimension);
    }

    public record Saved(BlockPos pos, ResourceKey<Level> dimension) {}
}
