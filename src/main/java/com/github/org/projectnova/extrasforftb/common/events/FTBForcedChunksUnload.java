package com.github.org.projectnova.extrasforftb.common.events;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import net.minecraft.server.level.ServerLevel;

import java.util.Timer;
import java.util.TimerTask;

public class FTBForcedChunksUnload {
    Timer chunkcleaner;

    public void unloadForcedChunksSchedule(ServerLevel world) {
        if (MainConfig.FORCED_CHUNKS_UNLOAD_SCHEDULE.isEnabled()) {
            chunkcleaner = new Timer(true);
            chunkcleaner.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    /*PlayerActivityHistory history = new PlayerActivityHistory(world);
                    LOGGER.info("Unloading chunks for players that have not been online in: " + SERVER_CONFIG.getExpireTimeInDays() + " Days");
                    LOGGER.info(history.getPlayersInChunkClearTime().size() + " Player(s) affected ");
                    for (String player : history.getPlayersInChunkClearTime()) {
                        LOGGER.info("Unloading " + player + "'s Chunks");
                        world.getServer().getCommands().performPrefixedCommand(world.getServer().createCommandSourceStack(),
                                "ftbchunks unload_all " + player);
                    }*/
                }
            }, 5, 60 * 60 * 1000);
        }

    }
}
