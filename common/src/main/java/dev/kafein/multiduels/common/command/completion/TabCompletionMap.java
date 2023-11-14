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

package dev.kafein.multiduels.common.command.completion;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class TabCompletionMap {
    private final Map<String, TabCompletion> tabCompletions;

    public TabCompletionMap() {
        this(Maps.newHashMap());
    }

    public TabCompletionMap(Map<String, TabCompletion> tabCompletions) {
        this.tabCompletions = tabCompletions;
    }

    public Map<String, TabCompletion> getTabCompletions() {
        return this.tabCompletions;
    }

    public @Nullable TabCompletion getTabCompletion(@NotNull String key) {
        return this.tabCompletions.get(key);
    }

    public void register(@NotNull String key, @NotNull TabCompletion tabCompletion) {
        this.tabCompletions.put(key, tabCompletion);
    }

    public void unregister(@NotNull String key) {
        this.tabCompletions.remove(key);
    }
}
