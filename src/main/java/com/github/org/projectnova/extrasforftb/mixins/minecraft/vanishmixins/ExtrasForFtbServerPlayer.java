package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins;

import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ExtrasForFtbServerPlayer extends Player {
    public ExtrasForFtbServerPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "isSpectator", at = @At("HEAD"), cancellable = true)
    public void onIsSpectator(CallbackInfoReturnable<Boolean> callback) {
        if (VanishHelper.isVanished(this)) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            String className = stackTrace[3].getClassName();

            try {
                Class<?> clazz = Class.forName(className);

                if (!clazz.getPackage().getName().startsWith("net.minecraft.")) {
                    callback.setReturnValue(true);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
