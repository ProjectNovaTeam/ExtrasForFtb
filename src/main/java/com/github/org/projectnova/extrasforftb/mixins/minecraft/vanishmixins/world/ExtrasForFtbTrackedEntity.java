package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins.world;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.TrackedEntity.class)
public abstract class ExtrasForFtbTrackedEntity {
    @Shadow
    @Final
    Entity entity;

    //Prevent tracking of vanished players for other players, which prevents vanished players from being rendered for anyone but themselves and permitted players.
    @Inject(method = "updatePlayer", at = @At("HEAD"), cancellable = true)
    private void onUpdatePlayer(ServerPlayer otherPlayer, CallbackInfo info) {
        if (MainConfig.HIDE_FROM_WORLD.get()) {
            if (entity instanceof Player player && VanishHelper.isVanished(player, otherPlayer))
                info.cancel();
        }
    }
}
