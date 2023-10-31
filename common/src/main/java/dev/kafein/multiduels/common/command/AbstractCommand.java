package dev.kafein.multiduels.common.command;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand implements Command {
    private final String name;
    private final String description;
    private final String usage;

    protected AbstractCommand(final @NotNull String name, final @NotNull String description, final @NotNull String usage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull String getDescription() {
        return this.description;
    }

    @Override
    public @NotNull String getUsage() {
        return this.usage;
    }
}
