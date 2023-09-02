package com.github.org.projectnova.extrasforftb.common.config;

import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import dev.ftb.mods.ftbessentials.config.ToggleableConfig;
import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.IntValue;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftblibrary.snbt.config.StringListValue;

import java.util.Arrays;

/**
 * @author X_Niter
 */
public interface MainConfig {

    // Sense this is an addon to FTB's mods, and I like there config handler, we're using there config handler!
    SNBTConfig CONFIG = SNBTConfig.create(ExtrasForFTB.MOD_ID.toUpperCase())
            .comment("Extras For FTB config file.");


    // General section for the configs
    SNBTConfig GENERAL = CONFIG.getGroup("general")
            .comment("General config options, these options are known to be common to it's own function or affect the whole mods functionality");

    ToggleableConfig LP_INTEGRATION = new ToggleableConfig(GENERAL, "LuckPerms Integration", false)
            .comment("Enable LuckPerms Integration.");

    ToggleableConfig TRIVIAL_WARNINGS = new ToggleableConfig(GENERAL, "Trivial Warnings", true)
            .comment("Enable Trivial Warnings in console, these warnings are informational but not important, such as reporting in console of disabled features or integrations.");

    ToggleableConfig FORCED_CHUNKS_UNLOAD_SCHEDULE = new ToggleableConfig(GENERAL, "Forced Chunks Unload Schedule", false)
            .comment("Enable removing players forced chunks if they have not joined the server in a set amount of time.");

    IntValue FORCED_CHUNKS_EXPIRE_TIME = FORCED_CHUNKS_UNLOAD_SCHEDULE.config.getInt("Forced Chunks Expire Time", 28)
            .comment("Time in days the player has not joined the server in which there Forced Chunks will be cleared.");

    // All features that pertain to admins/mods and generally should not be for normal players
    SNBTConfig ADMIN = CONFIG.getGroup("Admin").comment("Admin commands for cheating and moderation.");

    ToggleableConfig HOME_FOR = new ToggleableConfig(ADMIN, "homefor", true)
            .comment("Allows admins to view and teleport to other users homes.");

    StringListValue HOME_FOR_ALIASES = HOME_FOR.config.getStringList("home_for_aliases", Arrays.asList("home", "homefor", "hf"))
            .comment("Modify the Homefor command aliases at your needs, if this is empty, the command defaults to [/homefor]");

    ToggleableConfig LIST_HOMES_FOR = new ToggleableConfig(ADMIN, "list_homes_for", true)
            .comment("Allows admins to view a list of other players homes.");

    StringListValue LIST_HOMES_FOR_ALIASES = HOME_FOR.config.getStringList("list_homes_for_aliases", Arrays.asList("home", "listhomesfor", "lhf"))
            .comment("Modify the ListHomesFor command aliases at your needs, if this is empty, the command defaults to [/listhomesfor]");

    ToggleableConfig MORE = new ToggleableConfig(ADMIN, "more", true)
            .comment("Allows admins to increase item stack in there hand.");

    StringListValue MORE_ALIASES = MORE.config.getStringList("more_aliases", Arrays.asList("more", "m"))
            .comment("Modify the More command aliases at your needs, if this is empty, the command defaults to [/more]");

    ToggleableConfig VANISH = new ToggleableConfig(ADMIN, "vanish", true)
            .comment("Allows admins to go invisible.");

    StringListValue VANISH_ALIASES = VANISH.config.getStringList("vanish_aliases", Arrays.asList("vanish", "v"))
            .comment("Modify the Vanish command aliases at your needs, if this is empty, the command defaults to [/vanish]");

    BooleanValue HIDE_FROM_WORLD = VANISH.config.getBoolean("world_hidden", true)
            .comment("Hide vanished players physical world properties such as sounds, animations, and particles.");

    BooleanValue FAKE_CONNECTION_MESSAGE = VANISH.config.getBoolean("connection_message", true)
            .comment("Send fake join & leave message when players toggle vanish on or off.");

    BooleanValue CANCEL_INDIRECT_SOUNDEVENTS = VANISH.config.getBoolean("indirect_sounds", true)
            .comment("Cancel indirect sounds of vanished player such as hitting a mob.");

    BooleanValue CANCEL_INDIRECT_PARTICLES = VANISH.config.getBoolean("indirect_particles", true)
            .comment("Cancel indirect particles of vanished player such as eating.");

    BooleanValue VANISHED_SEE_VANISHED = VANISH.config.getBoolean("vanished_Sees_vanished", true)
            .comment("Can vanished players see other vanished players.");

    BooleanValue MODIFY_PLAYER_LIST = VANISH.config.getBoolean("modify_player_list", true)
            .comment("Modify the player list to exclude vanished players.");

    BooleanValue HIDDEN_FROM_COMMANDS = VANISH.config.getBoolean("hidden_from_commands", true)
            .comment("Remove vanished players from being executed in commands, such as not showing in autocompletion.");

    ToggleableConfig EXTRA_FTBTEAMS_COMMANDS = new ToggleableConfig(ADMIN, "extra_ftbteams_commands", true)
            .comment("Enable the extra FTB-Teams commands.")
            .comment("Commands are: \n</{command alias} list> (Will list all teams), </{command alias} list members {playername}> (Will list all members in a players team)");

    StringListValue EXTRA_FTBTEAMS_COMMANDS_ALIASES = VANISH.config.getStringList("extra_ftbteams_commands_aliases", Arrays.asList("ftbteams", "teams"))
            .comment("Modify the extra FTB-Teams commands aliases at your needs, if this is empty, the command defaults to [/ftbteams]");


}
