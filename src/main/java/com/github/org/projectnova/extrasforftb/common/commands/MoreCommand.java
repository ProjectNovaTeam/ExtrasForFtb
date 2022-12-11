package com.github.org.projectnova.extrasforftb.common.commands;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Objects;

public class MoreCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("more")
                .requires(MainConfig.MORE.enabledAndOp())
                .requires(source -> source.hasPermission(2))
                .executes(context -> more(context.getSource().getPlayerOrException()))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> more(EntityArgument.getPlayer(context, "player")))
                )
        );
    }

    public static int more(ServerPlayer player) {
        ItemStack heldItem = player.getHandSlots().iterator().next();
        if (heldItem == null || heldItem.getItem() == Items.AIR) {
            TextComponent msg = new TextComponent("");
            msg.append(new TextComponent("Item in hand is not valid.").setStyle(Style.EMPTY
                    .applyFormat(ChatFormatting.RED)
            ));
            player.displayClientMessage(msg, false);
        }

        int oldStackSize = Objects.requireNonNull(heldItem).getCount();
        heldItem.setCount(heldItem.getMaxStackSize());

        TextComponent successMessage = new TextComponent("Set stack count ");
        successMessage.append(String.valueOf(oldStackSize)).withStyle(ChatFormatting.YELLOW);
        successMessage.append(" \u27A1 ");
        successMessage.append(String.valueOf(heldItem.getCount())).withStyle(ChatFormatting.GREEN);
        player.displayClientMessage(successMessage, false);

        return heldItem.getMaxStackSize();
    }
}
