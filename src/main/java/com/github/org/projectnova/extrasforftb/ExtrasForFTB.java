package com.github.org.projectnova.extrasforftb;

import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftbranks.FTBRanks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

/**
 * @author X_Niter
 */
@Mod(ExtrasForFTB.MOD_ID)
public class ExtrasForFTB {

    public static final String MOD_ID = "extrasforftb";
    public static final Logger LOGGER = LogManager.getLogger("ExtrasForFTB");

    public static boolean ranksMod;

    public static boolean luckpermsMod;

    public ExtrasForFTB() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));


        ranksMod = ModList.get().isLoaded("ftbranks");
        if (ranksMod) {
            LOGGER.info("FTB-Ranks found, now integrating with it.");
        }

        luckpermsMod = ModList.get().isLoaded("luckperms");
        if (luckpermsMod) {
            LOGGER.info("LuckPerms found, now integrating with it.");
        }

    }
}
