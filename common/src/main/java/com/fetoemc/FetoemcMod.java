package com.fetoemc;

import dev.architectury.platform.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FetoemcMod {
    public static final String MOD_ID = "fetoemc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public FetoemcMod() {
        LOGGER.info("Fetoemc Mod initialized on " + (Platform.isFabric() ? "Fabric" : "Forge"));
    }
}