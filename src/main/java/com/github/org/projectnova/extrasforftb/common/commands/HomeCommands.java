package com.github.org.projectnova.extrasforftb.common.commands;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.OfflineData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbessentials.util.FTBEPlayerData;
import dev.ftb.mods.ftbessentials.util.TeleportPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.UsernameCache;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("homefor")
                .requires(MainConfig.HOME_FOR.enabledAndOp())
                .then(Commands.argument("player", StringArgumentType.string())
                        .requires(source -> source.hasPermission(2))
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(getAllPlayerNameSuggestion(context.getSource().getPlayerOrException()), builder))
                        .executes(context -> listHomesFor(context.getSource(), StringArgumentType.getString(context, "player")))
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .requires(source -> source.hasPermission(2))
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(OfflineData.getOfflineHomeSuggestions(StringArgumentType.getString(context, "player")), builder))
                                .executes(context -> homeFor(context.getSource().getPlayerOrException(), StringArgumentType.getString(context, "player"), StringArgumentType.getString(context, "name")))
                        )
                )
        );

        dispatcher.register(Commands.literal("listhomesfor")
                .requires(MainConfig.HOME_FOR.enabledAndOp())
                .then(Commands.argument("player", StringArgumentType.string())
                        .requires(source -> source.hasPermission(2))
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(getAllPlayerNameSuggestion(context.getSource().getPlayerOrException()), builder))
                        .executes(context -> listHomesFor(context.getSource(), StringArgumentType.getString(context, "player")))
                )
        );
    }

    private static Iterable<String> getAllPlayerNameSuggestion(ServerPlayer player) {
        List<String> names = new ArrayList<>(UsernameCache.getMap().values());
        names.remove(player.getGameProfile().getName());
        return names;
    }

    private static int listHomesFor(CommandSourceStack source, String name) throws CommandSyntaxException {
        UUID uuid = OfflineData.getOfflineUUIDByName(name);
        if (uuid == null) {
            source.getPlayerOrException().displayClientMessage(new TextComponent("No player found with name " + name + " !"), false);
            return 0;
        }
        return dev.ftb.mods.ftbessentials.command.HomeCommands.listhomes(source, source.getPlayerOrException().getGameProfile());
    }

    private static int homeFor(ServerPlayer player, String offlineName, String homeName) {
        UUID uuid = OfflineData.getOfflineUUIDByName(offlineName);
        if (uuid == null) {
            player.displayClientMessage(new TextComponent("No player with name " + offlineName + " found!"), false);
            return 0;
        }
        FTBEPlayerData data = OfflineData.getOfflinePlayerData(uuid);
        TeleportPos pos = data.homes.get(homeName.toLowerCase());

        if (pos == null) {
            player.displayClientMessage(new TextComponent("Home not found!"), false);
            return 0;
        }

        return data.homeTeleporter.teleport(player, p -> pos).runCommand(player);
    }
}
