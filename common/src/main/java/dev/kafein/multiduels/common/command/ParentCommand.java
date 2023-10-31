package dev.kafein.multiduels.common.command;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ParentCommand extends Command {
    @NotNull List<String> getChildNames();
}
