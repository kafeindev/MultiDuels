/*
 * MIT License
 *
 * Copyright (c) 2023 Kafein
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.kafein.multiduels.common.command;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public final class CommandProperties {
    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final String permission;

    public CommandProperties(String name, String description, String usage) {
        this(name, Lists.newArrayList(), description, usage, null);
    }

    public CommandProperties(String name, String description, String usage, final @Nullable String permission) {
        this(name, Lists.newArrayList(), description, usage, permission);
    }

    public CommandProperties(String name, List<String> aliases, String description, String usage) {
        this(name, aliases, description, usage, null);
    }

    public CommandProperties(String name, List<String> aliases, String description, String usage, final @Nullable String permission) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usage;
    }

    public @Nullable String getPermission() {
        return this.permission;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CommandProperties)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        CommandProperties commandProperties = (CommandProperties) obj;
        return commandProperties.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.aliases, this.description, this.usage, this.permission);
    }

    @Override
    public String toString() {
        return "CommandProperties{" +
                "name='" + this.name + '\'' +
                ", aliases=" + this.aliases +
                ", description='" + this.description + '\'' +
                ", usage='" + this.usage + '\'' +
                ", permission='" + this.permission + '\'' +
                '}';
    }

    @NotNull
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private List<String> aliases;
        private String description;
        private String usage;
        private String permission;

        public Builder() {
            this.aliases = Lists.newArrayList();
        }

        public Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public Builder aliases(@NotNull List<String> aliases) {
            this.aliases = aliases;
            return this;
        }

        public Builder alias(@NotNull String alias) {
            this.aliases.add(alias);
            return this;
        }

        public Builder description(@NotNull String description) {
            this.description = description;
            return this;
        }

        public Builder usage(@NotNull String usage) {
            this.usage = usage;
            return this;
        }

        public Builder permission(@NotNull String permission) {
            this.permission = permission;
            return this;
        }

        public CommandProperties build() {
            checkNotNull(this.name, "name");
            checkNotNull(this.aliases, "aliases");
            checkNotNull(this.description, "description");
            checkNotNull(this.usage, "usage");

            return new CommandProperties(this.name, this.aliases, this.description, this.usage, this.permission);
        }
    }
}
