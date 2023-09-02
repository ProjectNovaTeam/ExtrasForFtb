package com.github.org.projectnova.extrasforftb.common.commands;

import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Objects;

public class MoreCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        ExtrasForFTB.LOGGER.info("Registering [/more] command");
        List<String> moreAliases = MainConfig.MORE_ALIASES.get();
        if (!moreAliases.isEmpty()) {
            moreAliases.forEach(mAlias -> {
                dispatcher.register(alias(mAlias));
                ExtrasForFTB.LOGGER.info("More command alias [/" + mAlias + "] registered.");
            });
        } else {
            dispatcher.register(alias("more"));
            ExtrasForFTB.LOGGER.info("More command alias [/more] registered. If you want more aliases, please add them in the config.");
        }
    }

    private static LiteralArgumentBuilder<CommandSourceStack> alias(String prefix) {
        return Commands.literal(prefix)
                .requires(MainConfig.MORE.enabledAndOp())
                .requires(source -> source.hasPermission(2))
                .executes(context -> more(context.getSource().getPlayerOrException()))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> more(EntityArgument.getPlayer(context, "player")))
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
        successMessage.append(String.valueOf(oldStackSize)).withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.RESET);
        successMessage.append(" \u27A1 ").withStyle(ChatFormatting.RESET);
        successMessage.append(String.valueOf(heldItem.getCount())).withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.RESET);
        player.displayClientMessage(successMessage, false);

        return heldItem.getMaxStackSize();
    }
}
