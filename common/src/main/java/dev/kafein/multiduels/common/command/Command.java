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

import dev.kafein.multiduels.common.command.completion.RegisteredTabCompletion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Command {
    @NotNull String getName();

    @NotNull List<String> getAliases();

    boolean isAlias(final @NotNull String alias);

    @NotNull String getDescription();

    @NotNull String getUsage();

    @Nullable String getPermission();

    @NotNull List<Command> getSubCommands();

    @NotNull Command findSubCommand(final @NotNull String sub);

    @NotNull Command findSubCommand(final @NotNull String... subs);

    int findSubCommandIndex(final @NotNull String... subs);

    @NotNull List<RegisteredTabCompletion> getTabCompletions();

    void execute(final @NotNull CommandSender sender, final @NotNull String[] args);
}
