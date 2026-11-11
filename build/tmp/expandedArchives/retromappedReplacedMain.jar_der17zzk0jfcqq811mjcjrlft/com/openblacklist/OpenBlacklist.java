package com.openblacklist;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
    modid = OpenBlacklist.MODID,
    name = OpenBlacklist.NAME,
    version = OpenBlacklist.VERSION,
    acceptedMinecraftVersions = "[1.8.9]"
)
public class OpenBlacklist {

    public static final String MODID = "openblacklist";
    public static final String NAME = "OpenBlacklist";
    public static final String VERSION = "0.1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("[OpenBlacklist] Loaded successfully.");
    }
}