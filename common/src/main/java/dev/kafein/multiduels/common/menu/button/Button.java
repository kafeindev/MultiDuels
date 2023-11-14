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

import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.menu.action.RegisteredClickAction;
import dev.kafein.multiduels.common.menu.misc.ClickResult;
import dev.kafein.multiduels.common.menu.misc.contexts.ClickContext;
import dev.kafein.multiduels.common.menu.misc.contexts.OpenContext;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface Button {
    static Button fromNode(@NotNull ConfigurationNode node) {
        return DefaultButton.newBuilder()
                .node(node)
                .propertiesFromNode()
                .clickActionsFromNode()
                .build();
    }

    static @NotNull DefaultButton.Builder newBuilder() {
        return DefaultButton.newBuilder();
    }

    static @NotNull <T> PaginatedButton.Builder<T> newPaginatedBuilder() {
        return PaginatedButton.newBuilder();
    }

    Map<Integer, ItemComponent> createItems(@NotNull OpenContext context);

    ClickResult click(@NotNull ClickContext context);

    void setClickHandler(@NotNull ClickHandler clickHandler);

    Set<RegisteredClickAction> getClickActions();

    void putClickActions(@NotNull Set<RegisteredClickAction> actions);

    void putClickAction(@NotNull RegisteredClickAction action);

    void removeClickAction(@NotNull RegisteredClickAction action);

    void clearClickActions();

    ButtonProperties getProperties();

    @Nullable ConfigurationNode getNode();

    String getName();

    ButtonType getType();

    int[] getSlots();

    boolean hasSlot(int slot);

    @Nullable String getClickSound();
}
