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

package dev.kafein.multiduels.common.menu.button;

import com.google.common.collect.Maps;
import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.menu.misc.contexts.OpenContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

@FunctionalInterface
interface ItemStackFactory {
    Map<Integer, ItemComponent> createItemStacks(@NotNull OpenContext context, @NotNull Button button);

    abstract class Builder<T extends Builder<T>> {
        protected Function<OpenContext, Map<String, String>> placeholders;

        protected Builder() {
            placeholders = context -> Maps.newHashMap();
        }

        @SuppressWarnings("unchecked")
        private T self() {
            return (T) this;
        }

        public @Nullable Function<OpenContext, Map<String, String>> placeholders() {
            return this.placeholders;
        }

        public T placeholders(@NotNull Function<OpenContext, Map<String, String>> placeholders) {
            this.placeholders = placeholders;
            return self();
        }

        public T placeholders(@NotNull Map<String, String> placeholders) {
            return placeholders(context -> placeholders);
        }

        abstract ItemStackFactory build();
    }
}
