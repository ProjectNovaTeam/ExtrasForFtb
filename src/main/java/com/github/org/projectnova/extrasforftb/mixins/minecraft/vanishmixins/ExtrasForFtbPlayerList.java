package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins;

import com.github.org.projectnova.extrasforftb.api.EarlyServerPlayer;
import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.SoundSuppressionHelper;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerList.class)
public class ExtrasForFtbPlayerList {

    /**
     * @author X_Niter
     * @reason Fires the vanish method on players in the vanish queue.
     * <p>Also is our helper method to get the joining player into the {@link ServerPlayer} class</p>
     */
    // TODO: Remove the queue system for vanishing, no real usage for such a thing.
    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    public void onSendJoinMessage(Connection networkManager, ServerPlayer player, CallbackInfo ci) {
        if (VanishHelper.removeFromQueue(player.getGameProfile().getName()) && !VanishHelper.isVanished(player)) {
            VanishHelper.toggleVanish(player);
        }

        EarlyServerPlayer.joiningPlayer = player;
    }

    /**
     * @author X_Niter
     * @reason Stores the player that is exempted from broadcasting a given sound packet so the information can be used later for sound suppression.
     */
    @Inject(method = "broadcast", at = @At("HEAD"))
    public void onBroadcast(Player except, double x, double y, double z, double radius, ResourceKey<Level> dimension, Packet<?> packet, CallbackInfo callbackInfo) {
        if (MainConfig.HIDE_FROM_WORLD.get() && except != null
                && (packet instanceof ClientboundSoundPacket || packet instanceof ClientboundSoundEntityPacket || packet instanceof ClientboundLevelEventPacket)) {
            SoundSuppressionHelper.putSoundPacket(packet, except);
        }
    }
}
