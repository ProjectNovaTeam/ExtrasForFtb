package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins.world;

import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(SleepStatus.class)
public class ExtrasForFtbSleepStatus {
    //Fixes that vanished players are taken into account when calculating the amount of players needed for the night to be skipped

    /**
     * @author X_Niter
     * @reason Currently just vanilla/forge.
     * <P>When sleeping to skip night, we remove the vanished players from being counted for.</P>
     */
    // TODO: Integrate this feature for other well known 'sleeping skips night mods'
    // TODO: Potential Forge event to do this instead? I see future issues where other mods might be mixing into this as well and I'd hate to handle that one.
    @ModifyVariable(method = "update", at = @At(value = "HEAD"), argsOnly = true)
    public List<ServerPlayer> updatePlayers(List<ServerPlayer> original) {
        return VanishHelper.formatPlayerList(original, null);
    }
}
