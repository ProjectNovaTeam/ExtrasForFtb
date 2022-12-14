package com.github.org.projectnova.extrasforftb.common.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Yann151924
 */
public class LuckPermsIntegration {

    public static int getMaxClaimedChunks(ServerPlayer player, int def) {
        return Math.max(getMetaData(player.getUUID(), "ftbchunks.max_claimed").map(Integer::parseInt).orElse(def), 0);
    }

    public static int getMaxForceLoadedChunks(ServerPlayer player, int def) {
        return Math.max(getMetaData(player.getUUID(), "ftbchunks.max_force_loaded").map(Integer::parseInt).orElse(def), 0);
    }

    public static boolean getChunkLoadOffline(ServerPlayer player, boolean def) {
        return getMetaData(player.getUUID(), "ftbchunks.chunk_load_offline").map(Boolean::parseBoolean).orElse(def);
    }

    public static boolean getNoWilderness(ServerPlayer player, boolean def) {
        return getMetaData(player.getUUID(), "ftbchunks.no_wilderness").map(Boolean::parseBoolean).orElse(def);
    }

    public static int getInt(ServerPlayer player, int def, String node){
        return Math.max(getMetaData(player.getUUID(), node).map(Integer::parseInt).orElse(def), 0);
    }

    private static Optional<String> getMetaData(UUID uuid, String meta){
        LuckPerms luckperms = LuckPermsProvider.get();
        Optional<String> metaValue = Optional.empty();
        try {
            User user = luckperms.getUserManager().getUser(uuid);
            if(user != null){
                Optional<QueryOptions> context = luckperms.getContextManager().getQueryOptions(user);
                if(context.isPresent()){
                    metaValue = Optional.ofNullable(user.getCachedData().getMetaData(context.get()).getMetaValue(meta));
                }
            }
        } catch (IllegalStateException e){
            System.err.println("Error on fetching user with luckperms");
            System.err.println(e.getMessage());
        }
        return metaValue;
    }
}
