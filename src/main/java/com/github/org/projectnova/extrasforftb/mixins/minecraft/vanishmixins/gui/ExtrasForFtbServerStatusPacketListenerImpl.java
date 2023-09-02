package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins.gui;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerStatusPacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(ServerStatusPacketListenerImpl.class)
public abstract class ExtrasForFtbServerStatusPacketListenerImpl {
    @Shadow
    @Final
    private MinecraftServer server;

    /**
     * @author X_Niter
     * @reason stop server from sending the names of vanished players to the Multiplayer screen
     */
    @Redirect(method = "handleStatusRequest", at = @At(value = "NEW", target = "net/minecraft/network/protocol/status/ClientboundStatusResponsePacket"))
    public ClientboundStatusResponsePacket constructSServerInfoPacket(ServerStatus response) {
        if (MainConfig.MODIFY_PLAYER_LIST.get()) {
            PlayerList list = server.getPlayerList();
            GameProfile[] players = Objects.requireNonNull(response.getPlayers()).getSample();
            assert players != null;
            //this helper is needed to evaluate the right size for the actual array
            GameProfile[] newPlayersHelper = new GameProfile[players.length];
            GameProfile[] newPlayers;
            int visiblePlayersCount = 0;

            for (GameProfile profile : players) {
                if (!VanishHelper.isVanished(list.getPlayer(profile.getId()))) {
                    newPlayersHelper[visiblePlayersCount] = profile;
                    visiblePlayersCount++;
                }
            }

            newPlayers = new GameProfile[visiblePlayersCount];

            for (int i = 0; i < newPlayersHelper.length; i++) {
                if (newPlayersHelper[i] != null)
                    newPlayers[i] = newPlayersHelper[i];
            }

            response.getPlayers().setSample(newPlayers);
        }

        return new ClientboundStatusResponsePacket(response);
    }
}
