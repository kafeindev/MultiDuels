package dev.kafein.multiduels.common.command;

import dev.kafein.multiduels.common.command.argument.Arguments;
import dev.kafein.multiduels.common.command.argument.Completion;
import dev.kafein.multiduels.common.command.misc.Sender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Command {
    @NotNull String getName();

    @NotNull String getDescription();

    @NotNull String getUsage();

    @NotNull Set<Completion> getCompletions();

    void execute(final @NotNull Sender sender, final @NotNull Arguments arguments);
}
