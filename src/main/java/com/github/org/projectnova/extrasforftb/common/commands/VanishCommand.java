package com.github.org.projectnova.extrasforftb.common.commands;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.MixinAccessor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.common.TickEvent;
import dev.ftb.mods.ftbessentials.util.FTBEPlayerData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class VanishCommand {
    public static final Map<ServerPlayer, ServerEntity> trackers = new HashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vanish")
                .requires(MainConfig.VANISH.enabledAndOp())
                .executes(context -> vanish(context, context.getSource().getPlayerOrException()))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> vanish(context, EntityArgument.getPlayer(context, "player")))
                )
        );
    }

    private static int vanish(CommandContext<CommandSourceStack> context, ServerPlayer serverPlayer) throws CommandSyntaxException {

        if (serverPlayer == null) {
            serverPlayer = context.getSource().getPlayerOrException();
        }

        String RUVanished = "";


        Object asd = serverPlayer;
        if (!((MixinAccessor) (Object) asd).vanished()) {
            vanish(serverPlayer, true);
            RUVanished = "now";
        } else {
            unvanish(serverPlayer);
            RUVanished = "no longer";
        }

        serverPlayer.displayClientMessage(new TextComponent("You are " + RUVanished + " vanished."), false);

        return 1;
    }

    public static void vanish(ServerPlayer player, boolean sendMsg) {
        ((MixinAccessor) (Object) player).setVanished(true);
        Objects.requireNonNull(player.getServer()).getPlayerList().broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, player));
        ((ServerChunkCache) player.getCommandSenderWorld().getChunkSource()).removeEntity(player);

        /*
        if (player.getServer().isDedicatedServer() && sendMsg && MoreGameRules.get().checkBooleanWithPerm(player.getCommandSenderWorld().getGameRules(), MoreGameRules.get().doJoinMessageRule(), player))
            Compat.get().broadcast(player.getServer().getPlayerList(), new Tuple<>(1, new ResourceLocation("system")), translatableText("multiplayer.player.left", player.getDisplayName())
                    .withStyle(Style.EMPTY.applyFormat(ChatFormatting.YELLOW)).build());
         */
    }

    public static void unvanish(ServerPlayer player) {
        if (!((MixinAccessor) (Object) player).vanished()) {
            return;
        }

        ((MixinAccessor) (Object) player).setVanished(false);
        Objects.requireNonNull(player.getServer()).getPlayerList().broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, player));
        trackers.remove(player);
        ((ServerChunkCache) player.getCommandSenderWorld().getChunkSource()).addEntity(player);

        /*
        if (player.getServer().isDedicatedServer() && MoreGameRules.get().checkBooleanWithPerm(player.getCommandSenderWorld().getGameRules(), MoreGameRules.get().doJoinMessageRule(), player))
            Compat.get().broadcast(player.getServer().getPlayerList(), new Tuple<>(1, new ResourceLocation("system")), translatableText("multiplayer.player.joined",
                    player.getDisplayName())
                    .withStyle(Style.EMPTY.applyFormat(ChatFormatting.YELLOW)).build());

         */
    }
}
