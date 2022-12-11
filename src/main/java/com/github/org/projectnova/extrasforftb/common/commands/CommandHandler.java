package com.github.org.projectnova.extrasforftb.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class CommandHandler {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        HomeCommands.register(dispatcher);
        MoreCommand.register(dispatcher);
        VanishCommand.register(dispatcher);
    }
}
