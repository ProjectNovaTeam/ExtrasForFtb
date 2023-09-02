package com.github.org.projectnova.extrasforftb.common.events;

import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import dev.ftb.mods.ftbranks.impl.FTBRanksAPIImpl;
import dev.ftb.mods.ftbranks.impl.RankManagerImpl;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeMutateEvent;

/**
 * Attempt to listen to LP event for any permission changes.
 * This should fire for any permission changes and result in forcing FTBRanks to update.
 *
 * This update is needed for the LP Integration to make sure that any cached permissions on FTB side is up-to-date.
 * @author X_Niter
 */
public class PlayerPermissionChange {
    private final ExtrasForFTB extrasForFTB;

    private final LuckPerms luckPerms;

    public PlayerPermissionChange(ExtrasForFTB mod,LuckPerms luckPerms) {
        this.extrasForFTB = mod;
        this.luckPerms = luckPerms;
    }

    public void register() {
        EventBus eventBus = this.luckPerms.getEventBus();
        eventBus.subscribe(this.extrasForFTB, NodeMutateEvent.class, this::onNodeMutated);
    }

    private void onNodeMutated(NodeMutateEvent event) {
        RankManagerImpl ftbRanksManager = FTBRanksAPIImpl.manager;
        try {
            if (ftbRanksManager != null) {
                // Reload FTBRanks which results in FTB's apps being in sync with LuckPerms permission changes.

                ftbRanksManager.reload();

            }
        } catch (Exception err) {
            ExtrasForFTB.LOGGER.error(err);
        }
    }
}
