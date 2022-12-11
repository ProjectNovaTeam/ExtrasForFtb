package com.github.org.projectnova.extrasforftb.common.config;


import com.github.org.projectnova.extrasforftb.ExtrasForFTB;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class EFFMixinConfig implements IMixinConfigPlugin {
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

        if (MainConfig.LP_INTEGRATION.isEnabled() && luckperms != null) {
            if (mixinClassName.equals("ftbessentials.config.ExtrasForFTBPermissionBasedIntValue")
                    && ftbessentials != null) {
                return true;
            }

            if (mixinClassName.equals("ftbchunks.ExtrasForFTBChunksWorldConfig")
                    && ftbchunks != null) {
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
