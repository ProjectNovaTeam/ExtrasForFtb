package com.github.org.projectnova.extrasforftb.common.config;

import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;

/**
 * @author X_Niter
 */
public interface MainConfig {
    SNBTConfig CONFIG = SNBTConfig.create(ExtrasForFTB.MOD_ID).comment("Extras For FTB config file.");

    SNBTConfig GENERAL = CONFIG.getGroup("general").comment("General mod configs.");

    ToggleConfig LP_INTEGRATION = new ToggleConfig(GENERAL, "LuckPerms Integration", false)
            .comment("Enable LuckPerms Integration.");

    SNBTConfig ADMIN = CONFIG.getGroup("admin").comment("Admin commands for cheating and moderation.");

    ToggleConfig HOME_FOR = new ToggleConfig(ADMIN, "homefor")
            .comment("Allows admins to view and teleport to other users' homes.");

    ToggleConfig MORE = new ToggleConfig(ADMIN, "more")
            .comment("Allows admins to increase item stack in there hand.");

    ToggleConfig VANISH = new ToggleConfig(ADMIN, "vanish")
            .comment("Allows admins to go invisible.");
}
