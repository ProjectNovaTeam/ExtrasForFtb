package com.github.org.projectnova.extrasforftb.common.commands;

import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class VanishCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        ExtrasForFTB.LOGGER.info("Registering [/vanish] command");
        List<String> vanishAliases = MainConfig.VANISH_ALIASES.get();
        if (!vanishAliases.isEmpty()) {
            vanishAliases.forEach(vAlias -> {
                dispatcher.register(alias(vAlias));
                ExtrasForFTB.LOGGER.info("Vanish command alias [/" + vAlias + "] registered.");
            });
        } else {
            dispatcher.register(alias("vanish"));
            ExtrasForFTB.LOGGER.info("Vanish command alias [/vanish] registered. If you want more aliases, please add them in the config.");
        }
    }

    private static LiteralArgumentBuilder<CommandSourceStack> alias(String prefix) {
        return Commands.literal(prefix)
                .requires(MainConfig.VANISH.enabledAndOp())
                .executes(context -> vanish(context, context.getSource().getPlayerOrException()))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> vanish(context, EntityArgument.getPlayer(context, "player")))
                )
                .then(Commands.literal("list")
                        .executes(context -> listVanished(context, context.getSource().getPlayerOrException().getGameProfile().getName()))
                );
    }

    private static int vanish(CommandContext<CommandSourceStack> ctx, ServerPlayer serverPlayer) {

        VanishHelper.toggleVanish(serverPlayer);

        String RUVanished = "not";
        if (VanishHelper.isVanished(serverPlayer)) {
            RUVanished = "now";
        } else if (!VanishHelper.isVanished(serverPlayer)) {
            RUVanished = "no longer";
        }

        serverPlayer.displayClientMessage(new TextComponent("You are " + RUVanished + " vanished."), false);

        return 1;
    }

    private static int listVanished(CommandContext<CommandSourceStack> context, String playerName) {
        ServerPlayer serverPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(playerName);

        if (serverPlayer != null) {
            if (!VanishHelper.isVanished(serverPlayer))
                vanish(context, serverPlayer);
            else
                context.getSource().sendFailure(new TranslatableComponent("Could not add already vanished player " + "%s to the vanishing queue", playerName));

            return 1;
        }

        if (VanishHelper.removeFromQueue(playerName))
            context.getSource().sendSuccess(new TranslatableComponent("Removed " + "%s from the vanishing queue", playerName), true);
        else if (VanishHelper.addToQueue(playerName))
            context.getSource().sendSuccess(new TranslatableComponent("Added " + "%s to the vanishing queue", playerName), true);

        return 1;
    }
}
