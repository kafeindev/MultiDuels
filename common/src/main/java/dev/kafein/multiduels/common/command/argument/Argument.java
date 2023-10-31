package dev.kafein.multiduels.common.command.argument;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Argument {
    private final int index;
    private final String value;

    public Argument(final int index, final @NotNull String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return this.index;
    }

    public @NotNull String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Argument)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Argument other = (Argument) obj;
        return this.index == other.index && this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.index, this.value);
    }

    @Override
    public String toString() {
        return "Argument{" +
                "index=" + this.index +
                ", value='" + this.value + '\'' +
                '}';
    }
}
