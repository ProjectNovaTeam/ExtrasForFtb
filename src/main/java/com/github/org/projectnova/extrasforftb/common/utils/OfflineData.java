package com.github.org.projectnova.extrasforftb.common.utils;

import dev.ftb.mods.ftbessentials.util.FTBEPlayerData;
import net.minecraftforge.common.UsernameCache;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class OfflineData {
    public static UUID getOfflineUUIDByName(String name) {
        return UsernameCache.getMap().entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(name))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public static FTBEPlayerData getOfflinePlayerData(UUID uuid) {
        if (uuid == null) return null;
        FTBEPlayerData data = FTBEPlayerData.MAP.get(uuid);
        if (data == null) {
            data = new FTBEPlayerData(uuid);
            data.load();
        }
        return data;
    }

    public static Set<String> getOfflineHomeSuggestions(String offlineName) {
        UUID uuid = getOfflineUUIDByName(offlineName);
        if (uuid == null) {
            return Collections.emptySet();
        }
        FTBEPlayerData data = getOfflinePlayerData(uuid);
        return data.homes.keySet();
    }
}
