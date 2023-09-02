package com.github.org.projectnova.extrasforftb.mixins.ftbchunks;

import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.LuckPermsIntegration;
import dev.ftb.mods.ftbchunks.FTBChunks;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import dev.ftb.mods.ftbchunks.FTBRanksIntegration;
import dev.ftb.mods.ftbchunks.data.FTBChunksTeamData;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static dev.ftb.mods.ftbchunks.FTBChunksWorldConfig.*;

/**
 * @author X_Niter, LatvianModder, Yann151924
 * Temp Mixin for Luckperms Integration,
 * This class is temperary and will be removed the moment FTB Chunks makes this live.
 */
@Mixin({FTBChunksWorldConfig.class})
public interface ExtrasForFTBChunksWorldConfig {

    // TODO: FTBRanksAPIImpl.manager.reload();
    // TODO; Need to fire an FTB update when LP makes any permission changes to make sure FTB's mods is updated with LP changes.

    @Overwrite(remap = false)
    static int getMaxClaimedChunks(FTBChunksTeamData playerData, ServerPlayer player) {
        if (MainConfig.LP_INTEGRATION.isEnabled() && ExtrasForFTB.luckpermsMod && player != null) {
            return LuckPermsIntegration.getMaxClaimedChunks(player, MAX_CLAIMED_CHUNKS.get()) + playerData.getExtraClaimChunks();
        } else if (FTBChunks.ranksMod && player != null) {
            return FTBRanksIntegration.getMaxClaimedChunks(player, MAX_CLAIMED_CHUNKS.get()) + playerData.getExtraClaimChunks();
        }

        return MAX_CLAIMED_CHUNKS.get() + playerData.getExtraClaimChunks();
    }

    @Overwrite(remap = false)
    static int getMaxForceLoadedChunks(FTBChunksTeamData playerData, ServerPlayer player) {
        if (MainConfig.LP_INTEGRATION.isEnabled() && ExtrasForFTB.luckpermsMod && player != null) {
            return LuckPermsIntegration.getMaxForceLoadedChunks(player, MAX_FORCE_LOADED_CHUNKS.get()) + playerData.getExtraForceLoadChunks();
        } else if (FTBChunks.ranksMod && player != null) {
            return FTBRanksIntegration.getMaxForceLoadedChunks(player, MAX_FORCE_LOADED_CHUNKS.get()) + playerData.getExtraForceLoadChunks();
        }

        return MAX_FORCE_LOADED_CHUNKS.get() + playerData.getExtraForceLoadChunks();
    }

    @Overwrite(remap = false)
    static boolean canPlayerOfflineForceload(ServerPlayer player) {
        if (MainConfig.LP_INTEGRATION.isEnabled() && ExtrasForFTB.luckpermsMod && player != null) {
            return LuckPermsIntegration.getChunkLoadOffline(player, false);
        } else if (FTBChunks.ranksMod && player != null) {
            return FTBRanksIntegration.getChunkLoadOffline(player, false);
        }

        return CHUNK_LOAD_OFFLINE.get();
    }

    @Overwrite(remap = false)
    static boolean noWilderness(ServerPlayer player) {
        if (MainConfig.LP_INTEGRATION.isEnabled() && ExtrasForFTB.luckpermsMod && player != null) {
            return LuckPermsIntegration.getNoWilderness(player, NO_WILDERNESS.get());
        } else if (FTBChunks.ranksMod && player != null) {
            return FTBRanksIntegration.getNoWilderness(player, NO_WILDERNESS.get());
        }

        return NO_WILDERNESS.get();
    }
}
