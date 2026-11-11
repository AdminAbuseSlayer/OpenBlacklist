package com.openblacklist;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = OpenBlacklist.MODID, name = "OpenBlacklist", version = "0.1.0",
        clientSideOnly = true, acceptedMinecraftVersions = "[1.12.2]")
public class OpenBlacklist {
    public static final String MODID = "openblacklist";
    private static final Logger LOGGER = LogManager.getLogger("OpenBlacklist");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            // Load (not initialise) the class so the transformer runs now and the log shows the result.
            Class.forName("com.mojang.patchy.BlockedServers", false, OpenBlacklist.class.getClassLoader());
        } catch (Throwable t) {
            LOGGER.warn("Could not load com.mojang.patchy.BlockedServers", t);
        }
        LOGGER.info("OpenBlacklist loaded. Patched methods: {}", BlacklistTransformer.PATCHED);
        if (BlacklistTransformer.PATCHED.isEmpty()) {
            LOGGER.warn("Nothing was patched. This build will NOT bypass the blocklist.");
        }
    }
}
