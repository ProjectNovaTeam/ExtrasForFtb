package com.github.org.projectnova.extrasforftb.common.utils;

import com.github.org.projectnova.extrasforftb.api.PlayerVanishEvent;
import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VanishHelper {
    private static final Set<ServerPlayer> vanishedPlayers = new HashSet<>();
    private static final Set<String> vanishingQueue = new HashSet<>();

    public static List<? extends Entity> formatEntityList(List<? extends Entity> rawList, Entity forPlayer) {
        return rawList.stream().filter(entity -> !(entity instanceof Player player) || !isVanished(player, forPlayer)).collect(Collectors.toList());
    }

    public static List<ServerPlayer> formatPlayerList(List<ServerPlayer> rawList, Entity forPlayer) {
        // TODO: Config option to allow controlling if vanished players are, or are not included for this list.
        // HERE IS WHY:
        // This list is used inside of a mixin that handles the 'sleep to skip night feature'
        // We want to allow admin/owners to choose weather or not they do or do not want vanished players
        // to be included when 'sleep requirements for skipping night' is calculated.

        // It very well may be, that this list does not need that, and instead we actually handle it at the mixin.
        // We would handle it at the mixin by just returning the default MC code with no change.

        return rawList.stream().filter(player -> !isVanished(player, forPlayer)).collect(Collectors.toList());
    }

    public static void toggleVanish(ServerPlayer player) {
        boolean vanishes = !VanishHelper.isVanished(player);

        if (vanishes) {
            //player.sendMessage(new TextComponent("Be Vanished Mate"), Util.NIL_UUID);
            player.displayClientMessage(new TextComponent("Be Vanished Mate"), false);
        }

        VanishHelper.sendJoinOrLeaveMessageToPlayers(player.getLevel().getServer().getPlayerList().getPlayers(), player, vanishes, false);
        VanishHelper.updateVanishedStatus(player, vanishes);
        VanishHelper.sendJoinOrLeaveMessageToPlayers(player.getLevel().getServer().getPlayerList().getPlayers(), player, vanishes, true); //We always need to send fake join/leave messages when the player is in an unvanished state, thus we try twice and return early (within that method) if the player is vanished

        VanishHelper.sendPacketsOnVanish(player, player.getLevel(), vanishes);
    }

    public static void sendPacketsOnVanish(ServerPlayer currentPlayer, ServerLevel world, boolean vanishes) {
        List<ServerPlayer> serverPlayerList = world.getServer().getPlayerList().getPlayers();
        ServerChunkCache chunkSource = currentPlayer.getLevel().getChunkSource();

        for (ServerPlayer player : serverPlayerList) {
            if (!player.equals(currentPlayer)) { //prevent packet from being sent to the executor of the command
                if (!canSeeVanishedPlayers(player)) {
                    player.connection.send(new ClientboundPlayerInfoPacket(vanishes
                            ? ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER : ClientboundPlayerInfoPacket.Action.ADD_PLAYER, currentPlayer));
                }

                if (isVanished(player)) {
                    //update the vanishing player's tab list in case the vanishing player can (not) see other vanished players now
                    currentPlayer.connection.send(new ClientboundPlayerInfoPacket(canSeeVanishedPlayers(currentPlayer, vanishes)
                            ? ClientboundPlayerInfoPacket.Action.ADD_PLAYER : ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, player));
                }

                if (MainConfig.VANISH.isEnabled() && MainConfig.HIDE_FROM_WORLD.get()) {
                    if (vanishes && !canSeeVanishedPlayers(player)) {
                        //remove the vanishing player for the other players that cannot see vanished players
                        player.connection.send(new ClientboundRemoveEntitiesPacket(currentPlayer.getId()));
                    } else if (isVanished(player) && !canSeeVanishedPlayers(currentPlayer, vanishes)) {
                        //if the vanishing players cannot see vanished players now, remove them for the vanishing player
                        currentPlayer.connection.send(new ClientboundRemoveEntitiesPacket(player.getId()));
                    }
                }
            }
        }

        //We can safely send the tracking update for the vanishing or un-vanishing player to everyone,
        // the more strict and player-aware filter gets applied in MixinChunkMapTrackedEntity.
        // But we don't need to do that ourselves if the player has not been added yet (for example before it has fully joined the server)
        if (chunkSource.chunkMap.entityMap.containsKey(currentPlayer.getId())) {

            //we don't want an error in our log because the entity to be tracked is already on that list
            chunkSource.chunkMap.entityMap.remove(currentPlayer.getId());
            chunkSource.addEntity(currentPlayer);
        }

        currentPlayer.connection.send(new ClientboundSetActionBarTextPacket(VanishHelper.getVanishedStatusText(currentPlayer, vanishes)));
    }

    public static void sendJoinOrLeaveMessageToPlayers(List<ServerPlayer> playerList, ServerPlayer sender, boolean leaveMessage, boolean beforeStatusChange) {
        if (MainConfig.FAKE_CONNECTION_MESSAGE.get() && leaveMessage != beforeStatusChange
                && sender.server.getPlayerList().getPlayers().contains(sender)) {

            //Only send fake messages if the player has actually fully joined the server before this method is invoked

            Component message = new TranslatableComponent(leaveMessage ? "multiplayer.player.left"
                    : "multiplayer.player.joined", sender.getDisplayName()).withStyle(ChatFormatting.YELLOW);

            for (ServerPlayer receiver : playerList) {
                receiver.sendMessage(message, sender.getUUID());
            }
        }
    }

    public static void updateVanishedStatus(ServerPlayer player, boolean vanished) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag deathPersistentData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);

        deathPersistentData.putBoolean("Vanished", vanished);

        //Because the deathPersistentData could have been created newly by getCompound if it didn't exist before
        persistentData.put(Player.PERSISTED_NBT_TAG, deathPersistentData);

        updateVanishedPlayerList(player, vanished);
        MinecraftForge.EVENT_BUS.post(new PlayerVanishEvent(player, vanished));
        player.refreshTabListName();
    }

    public static void updateVanishedPlayerList(ServerPlayer player, boolean vanished) {
        if (vanished)
            vanishedPlayers.add(player);
        else
            vanishedPlayers.remove(player);

        SoundSuppressionHelper.updateVanishedPlayerMap(player, vanished);
    }

    public static TranslatableComponent getVanishedStatusText(ServerPlayer player, boolean isVanished) {
        return new TranslatableComponent(isVanished ? "%s is currently vanished." : "%s is currently not vanished.", player.getDisplayName());
    }

    public static boolean addToQueue(String playerName) {
        return vanishingQueue.add(playerName);
    }

    public static boolean removeFromQueue(String playerName) {
        return vanishingQueue.remove(playerName);
    }

    public static boolean canSeeVanishedPlayers(Entity entity) {
        return canSeeVanishedPlayers(entity, isVanished(entity));
    }

    public static boolean canSeeVanishedPlayers(Entity entity, boolean isVanished) {
        if (entity instanceof Player player) {
            return MainConfig.VANISHED_SEE_VANISHED.get() && isVanished;
        }

        return false;
    }

    public static boolean isVanished(Entity player) {
        return isVanished(player, null);
    }

    public static boolean isVanished(Entity player, Entity forPlayer) {
        if (player instanceof Player && !player.level.isClientSide) {
            boolean isVanished = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).getBoolean("Vanished");

            if (forPlayer != null) {
                //No player should ever be vanished for themselves
                if (player.equals(forPlayer)) {
                    return false;
                }

                return isVanished && !canSeeVanishedPlayers(forPlayer);
            }

            return isVanished;
        }

        return false;
    }
}
