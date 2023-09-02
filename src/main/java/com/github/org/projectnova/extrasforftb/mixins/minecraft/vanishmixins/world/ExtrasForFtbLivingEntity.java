package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins.world;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ExtrasForFtbLivingEntity extends Entity {

    private ExtrasForFtbLivingEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    //Prevent particles from being created when a vanished player falls
    @Redirect(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    public <T extends ParticleOptions> int redirectSendParticles(ServerLevel serverWorld, T type, double posX, double posY, double posZ, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        if (!MainConfig.HIDE_FROM_WORLD.get() || !VanishHelper.isVanished(this))
            serverWorld.sendParticles(type, posX, posY, posZ, particleCount, xOffset, yOffset, zOffset, speed);

        return 0;
    }

    //Prevent entities like passive mobs or pufferfish from detecting vanished players
    @Inject(method = "canBeSeenByAnyone", at = @At("HEAD"), cancellable = true)
    public void onCanBeSeen(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (MainConfig.HIDE_FROM_WORLD.get() && VanishHelper.isVanished(this))
            callbackInfo.setReturnValue(false);
    }
}
