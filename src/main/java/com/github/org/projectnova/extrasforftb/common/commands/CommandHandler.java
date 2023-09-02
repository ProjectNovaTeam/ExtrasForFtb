package com.github.org.projectnova.extrasforftb.common.commands;

import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.data.Main;

public class CommandHandler {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        if (ExtrasForFTB.essentialsMod) {
            if (MainConfig.HOME_FOR.isEnabled()) {
                HomeCommands.registerHomeFor(dispatcher);
            }
            if (MainConfig.LIST_HOMES_FOR.isEnabled()) {
                HomeCommands.registerListHomesFor(dispatcher);
            }
        }

        if (MainConfig.MORE.isEnabled()) {
            MoreCommand.register(dispatcher);
        }

        if (MainConfig.VANISH.isEnabled()) {
            VanishCommand.register(dispatcher);
        }
    }
}
