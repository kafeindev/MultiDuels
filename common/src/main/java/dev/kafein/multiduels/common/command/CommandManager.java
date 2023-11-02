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

import dev.kafein.multiduels.common.command.completion.TabCompletion;
import dev.kafein.multiduels.common.command.completion.TabCompletionMap;
import dev.kafein.multiduels.common.command.context.CommandContext;
import dev.kafein.multiduels.common.command.context.CommandContextMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CommandManager {
    private final CommandRegistrar commandRegistrar;
    private final CommandContextMap commandContextMap;
    private final TabCompletionMap tabCompletionMap;

    public CommandManager(final @NotNull CommandRegistrar commandRegistrar) {
        this(commandRegistrar, new CommandContextMap(), new TabCompletionMap());
    }

    public CommandManager(final @NotNull CommandRegistrar commandRegistrar, final @NotNull CommandContextMap commandContextMap, final @NotNull TabCompletionMap tabCompletionMap) {
        this.commandRegistrar = commandRegistrar;
        this.commandContextMap = commandContextMap;
        this.tabCompletionMap = tabCompletionMap;
    }

    public @NotNull CommandRegistrar getCommandRegistrar() {
        return this.commandRegistrar;
    }

    public void registerCommand(final @NotNull Command command) {
        this.commandRegistrar.registerCommand(command);
    }

    public @NotNull CommandContextMap getCommandContextMap() {
        return this.commandContextMap;
    }

    public <T> @Nullable CommandContext<T> getCommandContext(final @NotNull Class<T> clazz) {
        return this.commandContextMap.getCommandContext(clazz);
    }

    public <T> void registerCommandContext(final @NotNull Class<T> clazz, final @NotNull CommandContext<T> commandContext) {
        this.commandContextMap.register(clazz, commandContext);
    }

    public @NotNull TabCompletionMap getTabCompletionMap() {
        return this.tabCompletionMap;
    }

    public @Nullable TabCompletion getTabCompletion(final @NotNull String key) {
        return this.tabCompletionMap.getTabCompletion(key);
    }

    public void registerTabCompletion(final @NotNull String key, final @NotNull TabCompletion tabCompletion) {
        this.tabCompletionMap.register(key, tabCompletion);
    }
}
