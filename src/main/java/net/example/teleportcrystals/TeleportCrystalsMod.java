package net.example.teleportcrystals;

import net.example.teleportcrystals.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeleportCrystalsMod implements ModInitializer {
    public static final String MOD_ID = "teleportcrystals";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.init();
        LOGGER.info("Teleport Crystals initialized");
    }
}
