package dev.kafein.multiduels.common.command;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractChildCommand extends AbstractCommand
        implements ChildCommand {
    protected AbstractChildCommand(final @NotNull String name, final @NotNull String description, final @NotNull String usage) {
        super(name, description, usage);
    }
}
