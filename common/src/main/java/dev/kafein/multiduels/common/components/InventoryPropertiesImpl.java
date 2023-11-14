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

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final class InventoryPropertiesImpl implements InventoryComponent.Properties {
    private final String title;
    private final @Nullable String type;
    private final int size;

    InventoryPropertiesImpl(String title, int size) {
        this(title, null, size);
    }

    InventoryPropertiesImpl(String title, @Nullable String type, int size) {
        this.title = title;
        this.type = type;
        this.size = size;
    }

    static InventoryPropertiesImpl create(String title, String type) {
        return new InventoryPropertiesImpl(title, type, -1);
    }

    static InventoryPropertiesImpl create(String title, int size) {
        return new InventoryPropertiesImpl(title, size);
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public @Nullable String getType() {
        return this.type;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InventoryPropertiesImpl)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        InventoryPropertiesImpl other = (InventoryPropertiesImpl) obj;
        return this.title.equals(other.title) && this.size == other.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.title, this.size);
    }

    @Override
    public String toString() {
        return "InventoryPropertiesImpl{" +
                "title='" + this.title + '\'' +
                ", type='" + this.type + '\'' +
                ", size=" + this.size +
                '}';
    }
}
