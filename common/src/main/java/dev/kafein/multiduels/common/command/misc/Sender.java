package dev.kafein.multiduels.common.command.misc;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class Sender {
    private final String name;
    private final UUID uniqueId;

    public Sender(final @NotNull String name, final @NotNull UUID uniqueId) {
        this.name = name;
        this.uniqueId = uniqueId;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Sender)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Sender sender = (Sender) obj;
        return this.name.equals(sender.name) && this.uniqueId.equals(sender.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.uniqueId);
    }

    @Override
    public String toString() {
        return "Sender{" +
                "name='" + this.name + '\'' +
                ", uniqueId=" + this.uniqueId +
                '}';
    }
}
