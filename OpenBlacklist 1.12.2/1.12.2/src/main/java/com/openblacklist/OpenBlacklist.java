
package com.openblacklist;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = OpenBlacklist.MODID,
    name = OpenBlacklist.NAME,
    version = OpenBlacklist.VERSION,
    acceptedMinecraftVersions = "[1.12.2]",
    useMetadata = true
)
public class OpenBlacklist {

    public static final String MODID = "openblacklist";
    public static final String NAME = "OpenBlacklist";
    public static final String VERSION = "0.1.0";

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        logger.info("{} {} pre-initializing", NAME, VERSION);
        logger.info("Minecraft target: 1.12.2");
        logger.info("Java runtime: {} ({})",
            System.getProperty("java.version"),
            System.getProperty("java.vm.name"));
        logger.info("Operating system: {} {} ({})",
            System.getProperty("os.name"),
            System.getProperty("os.version"),
            System.getProperty("os.arch"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        logger.info("{} {} initialized successfully.", NAME, VERSION);
        logger.info("Connection diagnostics enabled.");
    }

    @SubscribeEvent
    public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (logger == null) return;

        logger.info("[Connection] Connected to remote address: {}",
            event.getManager().getRemoteAddress());
        logger.info("[Connection] Channel: {}",
            event.getManager().channel().getClass().getName());
        logger.info("[Connection] Channel active: {}",
            event.getManager().channel().isActive());
    }

    @SubscribeEvent
    public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (logger == null) return;

        logger.info("[Connection] Disconnected from remote address: {}",
            event.getManager().getRemoteAddress());
    }
}
