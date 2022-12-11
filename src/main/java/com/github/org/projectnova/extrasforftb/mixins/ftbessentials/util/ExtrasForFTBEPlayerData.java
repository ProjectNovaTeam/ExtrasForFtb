package com.github.org.projectnova.extrasforftb.mixins.ftbessentials.util;



import com.github.org.projectnova.extrasforftb.common.utils.MixinAccessor;
import dev.ftb.mods.ftbessentials.util.FTBEPlayerData;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({FTBEPlayerData.class})
public class ExtrasForFTBEPlayerData implements MixinAccessor {

    public boolean vanished;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void ExtrasForFTB_FTBEPlayerData(CallbackInfo ci) {
        vanished = false;
    }

    @Inject(method = "write", at = @At("RETURN"), remap = false)
    private void ExtrasForFTB_SNBTCompoundTag_Write(CallbackInfo ci) {
        SNBTCompoundTag json = new SNBTCompoundTag();
        json.putBoolean("vanished", vanished);
    }

    @Inject(method = "read", at = @At("RETURN"), remap = false)
    private void ExtrasForFTB_Read(CompoundTag tag) {
        vanished = tag.getBoolean("vanished");
    }

    @Override
    public boolean vanished() {
        return vanished;
    }

    @Override
    public void setVanished(boolean vanished) {
        this.vanished = vanished;
        ((FTBEPlayerData) (Object) this).save();
    }
}
