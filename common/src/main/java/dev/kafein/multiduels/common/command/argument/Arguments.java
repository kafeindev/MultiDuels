package dev.kafein.multiduels.common.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class Arguments {
    private final Set<Argument> arguments;

    public Arguments(final @NotNull Set<Argument> arguments) {
        this.arguments = arguments;
    }

    public @NotNull Set<Argument> getArguments() {
        return this.arguments;
    }

    public @Nullable Argument get(final int index) {
        return this.arguments.stream()
                .filter(argument -> argument.getIndex() == index)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Arguments)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Arguments other = (Arguments) obj;
        return this.arguments.equals(other.arguments);
    }

    @Override
    public int hashCode() {
        return this.arguments.hashCode();
    }

    @Override
    public String toString() {
        return "Arguments{" +
                "arguments=" + this.arguments +
                '}';
    }
}
