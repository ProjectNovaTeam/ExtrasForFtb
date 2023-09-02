package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins.world;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class ExtrasForFtbPlayer extends LivingEntity {

    public ExtrasForFtbPlayer(Level world) {
        super(EntityType.PLAYER, world);
    }

    //Fixes that the night can be skipped in some instances when a vanished player is sleeping
    @Inject(method = "isSleepingLongEnough", at = @At("HEAD"), cancellable = true)
    private void onIsSleepingLongEnough(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (MainConfig.HIDE_FROM_WORLD.get() && VanishHelper.isVanished(this))
            callbackInfo.setReturnValue(false);
    }
}
