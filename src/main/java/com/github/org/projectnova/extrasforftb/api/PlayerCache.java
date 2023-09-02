package com.github.org.projectnova.extrasforftb.api;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.UsernameCache;

import java.util.ArrayList;
import java.util.List;

public interface PlayerCache {
    static Iterable<String> getAllPlayers(ServerPlayer player) {
        List<String> names = new ArrayList<>(UsernameCache.getMap().values());
        names.remove(player.getGameProfile().getName());
        return names;
    }
}
