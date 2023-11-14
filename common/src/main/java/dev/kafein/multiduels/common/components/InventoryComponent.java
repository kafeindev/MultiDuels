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

package dev.kafein.multiduels.common.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InventoryComponent {
    View open(@NotNull PlayerComponent player);

    void close(@NotNull PlayerComponent player);

    void addItem(@NotNull ItemComponent item);

    void setItem(int slot, @NotNull ItemComponent item);

    void removeItem(int slot);

    interface Properties {
        static Properties create(@NotNull String title, String type) {
            return InventoryPropertiesImpl.create(title, type);
        }

        static Properties create(@NotNull String title, int size) {
            return InventoryPropertiesImpl.create(title, size);
        }

        String getTitle();

        @Nullable String getType();

        int getSize();
    }

    interface View {
        InventoryComponent getTop();

        InventoryComponent getBottom();
    }

    interface Factory {
        InventoryComponent create(@NotNull Properties properties);

        InventoryComponent create(@NotNull String title, int size);

        InventoryComponent create(@NotNull String title, @NotNull String type);
    }
}
