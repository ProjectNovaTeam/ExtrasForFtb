package com.github.org.projectnova.extrasforftb.common.config;

import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Predicate;

public class ToggleConfig implements Predicate<CommandSourceStack> {
    public final String name;
    public final SNBTConfig config;
    public final BooleanValue enabled;

    public ToggleConfig(SNBTConfig parent, String name) {
        this(parent, name, true);
    }

    public ToggleConfig(SNBTConfig parent, String name, boolean def) {
        this.name = name;
        config = parent.getGroup(name);
        enabled = config.getBoolean("enabled", def);
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public ToggleConfig comment(String... comment) {
        config.comment(comment);
        return this;
    }

    @Override
    public boolean test(CommandSourceStack stack) {
        return isEnabled();
    }

    public Predicate<CommandSourceStack> enabledAndOp() {
        return stack -> test(stack) && stack.hasPermission(2);
    }
}
