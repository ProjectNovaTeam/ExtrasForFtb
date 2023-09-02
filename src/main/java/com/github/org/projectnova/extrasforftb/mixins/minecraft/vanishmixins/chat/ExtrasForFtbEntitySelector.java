package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins.chat;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(EntitySelector.class)
public class ExtrasForFtbEntitySelector {
    //Prevent players that are not allowed to see vanished players from targeting them through their name or a selector (3/4)

    @ModifyVariable(method = "findSingleEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", shift = At.Shift.BEFORE))
    private List<? extends Entity> modifyEntityList(List<? extends Entity> originalList, CommandSourceStack source) {
        //only filter commands from players, not command blocks/console/datapacks
        if (MainConfig.HIDDEN_FROM_COMMANDS.get() && source.getEntity() != null) {
            originalList = VanishHelper.formatEntityList(originalList, source.getEntity());
        }

        return originalList;
    }

    //Prevent players that are not allowed to see vanished players from targeting them through their name or a selector (4/4)
    @ModifyVariable(method = "findSinglePlayer", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", shift = At.Shift.BEFORE))
    private List<ServerPlayer> modifyPlayerList(List<ServerPlayer> originalList, CommandSourceStack source) {
        //only filter commands from players, not command blocks/console/datapacks
        if (MainConfig.HIDDEN_FROM_COMMANDS.get() && source.getEntity() != null) {
            originalList = VanishHelper.formatPlayerList(originalList, source.getEntity());
        }

        return originalList;
    }
}
