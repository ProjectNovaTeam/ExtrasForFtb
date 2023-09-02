package com.github.org.projectnova.extrasforftb;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.events.PlayerPermissionChange;
import net.luckperms.api.LuckPerms;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author X_Niter
 */
@Mod(ExtrasForFTB.MOD_ID)
public class ExtrasForFTB {

    public static final String MOD_ID = "extrasforftb";
    public static final Logger LOGGER = LogManager.getLogger("ExtrasForFTB");

    public static boolean ranksMod;

    public static boolean luckpermsMod;

    public static boolean essentialsMod;

    public static boolean teamsMod;

    public static boolean chunksMod;

    private LuckPerms luckPerms;

    public ExtrasForFTB() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        LOGGER.info("/================================/");
        LOGGER.info("|  EXTRA                         |");
        LOGGER.info("|  FOR                           |");
        LOGGER.info("|  FTB                           |");
        LOGGER.info("/================================/");


        ranksMod = ModList.get().isLoaded("ftbranks");
        if (ranksMod) {
            LOGGER.info("Found mod FTB-Ranks, applicable integrations will function if they are enabled.");
        } else {
            if (MainConfig.TRIVIAL_WARNINGS.isEnabled()) {
                LOGGER.warn("FTB-Ranks not present, integration will not take place, if this is intentional, you may ignore this!");
            }
        }


        luckpermsMod = ModList.get().isLoaded("luckperms");
        if (luckpermsMod) {
            LOGGER.info("Found mod LuckPerms, applicable integrations will function if they are enabled.");
            new PlayerPermissionChange(this, this.luckPerms).register();
        } else {
            if (MainConfig.TRIVIAL_WARNINGS.isEnabled()) {
                LOGGER.warn("LuckPerms not present, integration will not take place, if this is intentional, you may ignore this!");
            }
        }

        essentialsMod = ModList.get().isLoaded("ftbessentials");
        if (essentialsMod) {
            LOGGER.info("Found mod FTB-Essentials, applicable integrations will function if they are enabled.");
        } else {
            if (MainConfig.TRIVIAL_WARNINGS.isEnabled()) {
                LOGGER.warn("LuckPerms not present, integration will not take place, if this is intentional, you may ignore this!");
            }
        }

        teamsMod = ModList.get().isLoaded("ftbteams");
        if (teamsMod) {
            LOGGER.info("Found mod FTB-Teams, applicable integrations will function if they are enabled.");
        } else {
            if (MainConfig.TRIVIAL_WARNINGS.isEnabled()) {
                LOGGER.warn("FTB-Teams not present, integration will not take place, if this is intentional, you may ignore this!");
            }
        }

        chunksMod = ModList.get().isLoaded("ftbchunks");
        if (chunksMod) {
            LOGGER.info("Found mod FTB-Chunks, applicable integrations will function if they are enabled.");
        } else {
            if (MainConfig.TRIVIAL_WARNINGS.isEnabled()) {
                LOGGER.warn("FTB-Chunks not present, integration will not take place, if this is intentional, you may ignore this!");
            }
        }

    }
}
