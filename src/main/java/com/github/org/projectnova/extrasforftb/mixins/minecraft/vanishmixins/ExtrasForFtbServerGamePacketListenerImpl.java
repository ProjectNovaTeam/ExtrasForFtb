package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins;

import com.github.org.projectnova.extrasforftb.api.EarlyServerPlayer;
import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.SoundSuppressionHelper;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerGamePacketListenerImpl.class)
public class ExtrasForFtbServerGamePacketListenerImpl {
    @Shadow
    public ServerPlayer player;
    @Shadow @Final
    private MinecraftServer server;

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        if (packet instanceof ClientboundPlayerInfoPacket infoPacket 
                && infoPacket.getAction() != ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER) {
            List<ClientboundPlayerInfoPacket.PlayerUpdate> filteredPacketEntries = infoPacket.getEntries()
                    .stream()
                    .filter(p -> !VanishHelper.isVanished(server.getPlayerList().getPlayer(p.getProfile().getId()), player))
                    .toList();

            if (filteredPacketEntries.isEmpty()) {
                callbackInfo.cancel();
            } 
            else if (!filteredPacketEntries.equals(infoPacket.getEntries())) {
                infoPacket.entries = filteredPacketEntries;
            }
        }
        else if (packet instanceof ClientboundTakeItemEntityPacket pickupPacket
                && VanishHelper.isVanished(player.level.getEntity(pickupPacket.getPlayerId()), player)) {
            callbackInfo.cancel();
        }
        else if (MainConfig.HIDE_FROM_WORLD.get()) {
            if (packet instanceof ClientboundSoundPacket soundPacket
                    && SoundSuppressionHelper.shouldSuppressSoundEventFor(SoundSuppressionHelper.getPlayerForPacket(soundPacket),
                    player.level, soundPacket.getX(), soundPacket.getY(), soundPacket.getZ(), player)) {
                callbackInfo.cancel();
            }

            else if (packet instanceof ClientboundSoundEntityPacket soundPacket
                    && SoundSuppressionHelper.shouldSuppressSoundEventFor(SoundSuppressionHelper.getPlayerForPacket(soundPacket),
                    player.level, player.level.getEntity(soundPacket.getId()), player)) {
                callbackInfo.cancel();
            }

            else if (packet instanceof ClientboundLevelEventPacket soundPacket
                    && SoundSuppressionHelper.shouldSuppressSoundEventFor(SoundSuppressionHelper.getPlayerForPacket(soundPacket),
                    player.level, Vec3.atCenterOf(soundPacket.getPos()), player)) {
                callbackInfo.cancel();
            }

            else if (packet instanceof ClientboundBlockEventPacket eventPacket
                    && SoundSuppressionHelper.shouldSuppressSoundEventFor(null, player.level, Vec3.atCenterOf(eventPacket.getPos()), player)) {
                callbackInfo.cancel();
            }

            else if (packet instanceof ClientboundLevelParticlesPacket particlesPacket
                    && SoundSuppressionHelper.shouldSuppressParticlesFor(null,
                    player.level, particlesPacket.getX(), particlesPacket.getY(), particlesPacket.getZ(), player)) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, GenericFutureListener<?> listener, CallbackInfo callbackInfo) {
        if (packet instanceof ClientboundChatPacket chatPacket && chatPacket.getMessage() instanceof TranslatableComponent component) {
            if (component.getKey().startsWith("multiplayer.player.joined") && VanishHelper.isVanished(EarlyServerPlayer.joiningPlayer, player)) {
                EarlyServerPlayer.joiningPlayer = null;
                callbackInfo.cancel();
            }
            else if (component.getKey().startsWith("multiplayer.player.left") || component.getKey().startsWith("death.") || component.getKey().startsWith("chat.type.advancement")) {
                if (component.getArgs()[0] instanceof Component playerName) {
                    for (ServerPlayer sender : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                        if (sender.getDisplayName().getString().equals(playerName.getString()) && VanishHelper.isVanished(sender, player))
                            callbackInfo.cancel();
                    }
                }
            }
        }
    }
}
