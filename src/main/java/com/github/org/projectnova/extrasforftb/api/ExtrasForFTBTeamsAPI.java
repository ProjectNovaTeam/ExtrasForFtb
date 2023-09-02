package com.github.org.projectnova.extrasforftb.api;

import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import dev.ftb.mods.ftbteams.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.data.ClientTeamManager;
import dev.ftb.mods.ftbteams.data.Team;
import dev.ftb.mods.ftbteams.data.TeamBase;
import dev.ftb.mods.ftbteams.data.TeamRank;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class ExtrasForFTBTeamsAPI extends FTBTeamsAPI {

    private static final List<TeamRank> PARTY_RANKS = List.of(TeamRank.OWNER, TeamRank.OFFICER, TeamRank.MEMBER);

    public ExtrasForFTBTeamsAPI(){}

    public static Set<UUID> getFTBTeamMembers(ServerPlayer player) {
        return getPlayerTeam(player).getMembers();
    }

    public static Collection<Team> getFTBTeams() {
        return FTBTeamsAPI.getManager().getTeams();
    }


    /*
    long max = (Long)FTBChunksWorldConfig.MAX_IDLE_DAYS_BEFORE_UNCLAIM.get() * 86400000L;
        if (max != 0L) {
            ClaimedChunkManager manager = FTBChunksAPI.getManager();
            List<ClaimedChunk> expired = new ArrayList();
            Stream var10000 = FTBTeamsAPI.getManager().getTeams().stream();
            Objects.requireNonNull(manager);
            var10000.map(manager::getData).filter((data) -> {
                return now - data.getLastLoginTime() > max;
            }).forEach((data) -> {
                Collection<ClaimedChunk> chunks = data.getClaimedChunks();
                expired.addAll(chunks);
                FTBChunks.LOGGER.info("all chunk claims for team {} have expired due to team inactivity; unclaiming {} chunks", data, chunks.size());
            });
            if (!expired.isEmpty()) {
                CommandSourceStack sourceStack = server.createCommandSourceStack();
                Map<ResourceKey<Level>, List<SendChunkPacket.SingleChunk>> toSync = new HashMap();
                expired.forEach((c) -> {
                    unclaimChunk(now, c, toSync, sourceStack);
                });
                syncChunks(toSync, server, Util.NIL_UUID);
            }

        }
     */
}
