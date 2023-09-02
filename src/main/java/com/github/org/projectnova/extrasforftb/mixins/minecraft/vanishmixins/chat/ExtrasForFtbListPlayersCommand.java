package com.github.org.projectnova.extrasforftb.mixins.minecraft.vanishmixins.chat;

import com.github.org.projectnova.extrasforftb.common.config.MainConfig;
import com.github.org.projectnova.extrasforftb.common.utils.VanishHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ListPlayersCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ListPlayersCommand.class)
public abstract class ExtrasForFtbListPlayersCommand {

    /**
     * @author X_Niter
     * @reason Filter result of /list command when non-admins execute it
     */
    @Redirect(method = "format", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;getPlayers()Ljava/util/List;"))
    private static List<ServerPlayer> redirectGetPlayers(PlayerList playerList, CommandSourceStack source) {
        if (MainConfig.MODIFY_PLAYER_LIST.get()) {
            return VanishHelper.formatPlayerList(playerList.getPlayers(), source.getEntity());
        }

        return playerList.getPlayers();
    }
}
