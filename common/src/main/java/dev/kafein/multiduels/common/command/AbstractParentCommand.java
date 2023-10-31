package dev.kafein.multiduels.common.command;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractParentCommand extends AbstractCommand
        implements ParentCommand {
    protected AbstractParentCommand(final @NotNull String name, final @NotNull String description, final @NotNull String usage) {
        super(name, description, usage);
    }
}
