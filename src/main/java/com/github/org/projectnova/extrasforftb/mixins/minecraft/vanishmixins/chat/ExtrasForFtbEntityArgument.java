package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins.chat;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;
import java.util.List;

@Mixin(EntityArgument.class)
public abstract class ExtrasForFtbEntityArgument {
    //Prevent players that are not allowed to see vanished players from targeting them through their name or a selector (1/4)
    @ModifyVariable(method = "getEntities", at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z", shift = At.Shift.BEFORE))
    private static Collection<? extends Entity> modifyEntityList(Collection<? extends Entity> originalList, CommandContext<CommandSourceStack> context) {
        if (MainConfig.HIDDEN_FROM_COMMANDS.get() && context.getSource().getEntity() != null) //only filter commands from players, not command blocks/console/datapacks
            originalList = VanishHelper.formatEntityList(originalList.stream().toList(), context.getSource().getEntity());

        return originalList;
    }

    //Prevent players that are not allowed to see vanished players from targeting them through their name or a selector (2/4)
    @ModifyVariable(method = "getPlayers", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", shift = At.Shift.BEFORE))
    private static List<ServerPlayer> modifyPlayerList(List<ServerPlayer> originalList, CommandContext<CommandSourceStack> context) {
        if (MainConfig.HIDDEN_FROM_COMMANDS.get() && context.getSource().getEntity() != null) //only filter commands from players, not command blocks/console/datapacks
            originalList = VanishHelper.formatPlayerList(originalList, context.getSource().getEntity());

        return originalList;
    }
}
