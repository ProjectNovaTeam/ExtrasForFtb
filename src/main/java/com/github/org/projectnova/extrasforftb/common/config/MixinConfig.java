package com.github.org.projectnova.extrasforftb.common.config;


import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinConfig implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {
        ExtrasForFTB.LOGGER.info("Initializing Enabled Mixins, hold on tight, we're entering dangerous territory here!");
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        ModFileInfo luckperms = LoadingModList.get().getModFileById("luckperms");
        ModFileInfo ftbessentials = LoadingModList.get().getModFileById("ftbessentials");
        ModFileInfo ftbchunks = LoadingModList.get().getModFileById("ftbchunks");
        ModFileInfo ftbranks = LoadingModList.get().getModFileById("ftbranks");

        if (MainConfig.LP_INTEGRATION.isEnabled() && luckperms != null && ftbranks != null) {
            if (mixinClassName.equals("ftbessentials.config.ExtrasForFTBPermissionBasedIntValue")
                    && ftbessentials != null) {
                return true;
            }

            if (mixinClassName.equals("ftbchunks.ExtrasForFTBChunksWorldConfig")
                    && ftbchunks != null) {
                return true;
            }
        } else if (MainConfig.LP_INTEGRATION.isEnabled() && (luckperms == null || ftbranks == null)) {
            if (luckperms == null) {
                ExtrasForFTB.LOGGER.fatal("Luckperms is not present but integration is enabled, please install Luckperms for this to work, or disable the integration.");
            }
            if (ftbranks == null) {
                ExtrasForFTB.LOGGER.fatal("FTB-Ranks is not present but Luckperms integration is enabled, please install FTB-Ranks for this to work, or disable the Luckperms integration.");
            }
            return false;
        }

        if (MainConfig.VANISH.isEnabled()) {
            if (mixinClassName.equals("minecraft.vanishmixins.ExtrasForFtbServerPlayer")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.ExtrasForFtbServerGamePacketListenerImpl")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.ExtrasForFtbPlayerList")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.world.ExtrasForFtbTrackedEntity")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.world.ExtrasForFtbSleepStatus")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.world.ExtrasForFtbPlayer")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.world.ExtrasForFtbLivingEntity")) {
                return true;
            }
            //if (mixinClassName.equals("minecraft.vanishmixins.sound.ExtrasForFtbEntity")) {
            //    return true;
            //}
            if (mixinClassName.equals("minecraft.vanishmixins.gui.ExtrasForFtbServerStatusPacketListenerImpl")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.gui.ExtrasForFtbServerStatus")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.chat.ExtrasForFtbListPlayersCommand")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.chat.ExtrasForFtbEntitySelector")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.chat.ExtrasForFtbEntityArgument")) {
                return true;
            }
            if (mixinClassName.equals("minecraft.vanishmixins.chat.ExtrasForFtbCombatTracker")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}
