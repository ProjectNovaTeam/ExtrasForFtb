package com.github.org.projectnova.extrasforftb;

import com.github.org.projectnova.extrasforftb.common.commands.CommandHandler;
import com.github.org.projectnova.extrasforftb.common.commands.VanishCommand;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.platform.Platform;
import dev.ftb.mods.ftbessentials.config.FTBEConfig;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.nio.file.Path;

/**
 * @author X_Niter
 */
@Mod.EventBusSubscriber(modid = ExtrasForFTB.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventBus {
    public static final LevelResource CONFIG_FILE = new LevelResource("serverconfig/extrasforftb.snbt");

    @SubscribeEvent
    public static void serverAboutToStart(ServerAboutToStartEvent event) {
        Path configFilePath = event.getServer().getWorldPath(CONFIG_FILE);
        Path defaultConfigFilePath = Platform.getConfigFolder().resolve("../defaultconfigs/extrasforftb-server.snbt");

        FTBEConfig.CONFIG.load(configFilePath, defaultConfigFilePath, () -> new String[]{
                "Default config file that will be copied to world's serverconfig/ftbextras.snbt location",
                "Copy values you wish to override in here",
                "Example:",
                "",
                "{",
                "	misc: {",
                "		enderchest: {",
                "			enabled: false",
                "		}",
                "	}",
                "}",
        });
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandHandler.registerCommands(event.getDispatcher());
    }

    @SubscribeEvent
    public void initCommonSetup(FMLCommonSetupEvent event) {
        while (VanishCommand.trackers.size() > 0) {
            TickEvent.SERVER_PRE.register(server -> VanishCommand.trackers.values().forEach(ServerEntity::sendChanges));
        }
    }
}
