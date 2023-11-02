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

package dev.kafein.multiduels.common.command.context;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class CommandContextMap {
    private final Map<Class<?>, CommandContext<?>> commandContexts;

    public CommandContextMap() {
        this(Maps.newHashMap());
    }

    public CommandContextMap(final @NotNull Map<Class<?>, CommandContext<?>> commandContexts) {
        this.commandContexts = commandContexts;
    }

    public @NotNull Map<Class<?>, CommandContext<?>> getCommandContexts() {
        return this.commandContexts;
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable CommandContext<T> getCommandContext(final @NotNull Class<T> clazz) {
        return (CommandContext<T>) this.commandContexts.get(clazz);
    }

    public <T> void register(final @NotNull Class<T> clazz, final @NotNull CommandContext<T> commandContext) {
        this.commandContexts.put(clazz, commandContext);
    }

    public <T> void unregister(final @NotNull Class<T> clazz) {
        this.commandContexts.remove(clazz);
    }
}
