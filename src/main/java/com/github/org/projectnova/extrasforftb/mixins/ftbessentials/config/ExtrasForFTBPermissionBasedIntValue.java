package com.github.org.projectnova.extrasforftb.mixins.ftbessentials.config;

import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.LuckPermsIntegration;
import dev.ftb.mods.ftbessentials.FTBEssentials;
import dev.ftb.mods.ftbessentials.FTBRanksIntegration;
import dev.ftb.mods.ftbessentials.config.PermissionBasedIntValue;
import dev.ftb.mods.ftblibrary.snbt.config.IntValue;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author X_Niter
 * Temp Mixin for Luckperms Integration,
 * This class is temperary and will be removed the moment FTB Essentials makes this live.
 */
@Mixin({PermissionBasedIntValue.class})
public class ExtrasForFTBPermissionBasedIntValue {

    @Shadow
    @Final
    public IntValue value;

    @Shadow
    @Final
    public String permission;


    @Overwrite(remap = false)
    public int get(ServerPlayer player) {
        if (MainConfig.LP_INTEGRATION.isEnabled() && ExtrasForFTB.luckpermsMod) {
            return LuckPermsIntegration.getInt(player, value.get(), permission);
        } else {
            if (FTBEssentials.ranksMod) {
                return FTBRanksIntegration.getInt(player, value.get(), permission);
            }
        }

        return value.get();
    }
}
