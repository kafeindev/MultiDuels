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

import com.google.common.collect.ImmutableList;
import dev.kafein.multiduels.common.command.completion.RegisteredTabCompletion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class AbstractCommand implements Command {
    private final CommandProperties properties;
    private final List<Command> subCommands;
    private final List<RegisteredTabCompletion> tabCompletions;

    public AbstractCommand(final @NotNull CommandProperties properties) {
        this(properties, ImmutableList.of());
    }

    public AbstractCommand(final @NotNull CommandProperties properties, final @NotNull List<Command> subCommands) {
        this(properties, subCommands, ImmutableList.of());
    }

    public AbstractCommand(final @NotNull CommandProperties properties, final @NotNull List<Command> subCommands, final @NotNull List<RegisteredTabCompletion> tabCompletions) {
        this.properties = properties;
        this.subCommands = subCommands;
        this.tabCompletions = tabCompletions;
    }

    @Override
    public @NotNull String getName() {
        return this.properties.getName();
    }

    @Override
    public @NotNull List<String> getAliases() {
        return this.properties.getAliases();
    }

    @Override
    public boolean isAlias(@NotNull String alias) {
        return this.properties.getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(alias));
    }

    @Override
    public @NotNull String getDescription() {
        return this.properties.getDescription();
    }

    @Override
    public @NotNull String getUsage() {
        return this.properties.getUsage();
    }

    @Override
    public @Nullable String getPermission() {
        return this.properties.getPermission();
    }

    @Override
    public @NotNull List<Command> getSubCommands() {
        return this.subCommands;
    }

    @Override
    public @NotNull Command findSubCommand(final @NotNull String sub) {
        return this.subCommands.stream()
                .filter(command -> command.isAlias(sub))
                .findFirst()
                .orElse(this);
    }

    @Override
    public @NotNull Command findSubCommand(final @NotNull String... subs) {
        checkArgument(subs.length > 0, "Sub commands cannot be empty.");

        if (subs.length == 1) {
            return findSubCommand(subs[0]);
        } else {
            String[] newSubs = Arrays.copyOfRange(subs, 1, subs.length);
            return findSubCommand(subs[0]).findSubCommand(newSubs);
        }
    }

    @Override
    public int findSubCommandIndex(final @NotNull String... subs) {
        checkArgument(subs.length > 0, "Sub commands cannot be empty.");

        int index = 0;
        Command command = this;
        while (index < subs.length) {
            if (command.isAlias(subs[index])) {
                command = command.findSubCommand(subs[index]);
                index++;
            } else {
                break;
            }
        }

        return index;
    }

    @Override
    public @NotNull List<RegisteredTabCompletion> getTabCompletions() {
        return this.tabCompletions;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Command)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Command command = (Command) obj;
        return command.getName().equalsIgnoreCase(this.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName(), this.getAliases(), this.getDescription(), this.getUsage(), this.getPermission());
    }

    @Override
    public String toString() {
        return "CommandProperties{" +
                "name='" + this.getName() + '\'' +
                ", aliases=" + this.getAliases() +
                ", description='" + this.getDescription() + '\'' +
                ", usage='" + this.getUsage() + '\'' +
                ", permission='" + this.getPermission() + '\'' +
                '}';
    }
}
