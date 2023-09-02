package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins.gui;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerStatus.Players.class)
public abstract class ExtrasForFtbServerStatus {
    @Shadow
    public int numPlayers;

    //update the onlinePlayerCount when setting the players, players should be already filtered by MixinServerStatusNetHandler; also makes use of an AT to un-final onlinePlayerCount
    @Inject(method = "setSample", at = @At("HEAD"))
    private void onSetSample(GameProfile[] players, CallbackInfo info) {
        if (MainConfig.MODIFY_PLAYER_LIST.get()) {
            this.numPlayers = players.length;
        }
    }
}
