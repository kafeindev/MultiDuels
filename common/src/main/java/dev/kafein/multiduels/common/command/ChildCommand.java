package dev.kafein.multiduels.common.command;

import org.jetbrains.annotations.NotNull;

public interface ChildCommand extends Command {
    @NotNull String getParentName();
}
