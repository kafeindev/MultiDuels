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

package dev.kafein.multiduels.common.metadata;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Metadata<T> {
    private final String key;
    private final T value;

    public Metadata(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public static <T> Metadata<T> create(String key, T value) {
        return new Metadata<>(key, value);
    }

    public static <T> Metadata<T> create(Enum<?> key, T value) {
        return new Metadata<>(key.name(), value);
    }

    public String getKey() {
        return this.key;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Metadata)) return false;

        Metadata<?> metadata = (Metadata<?>) obj;
        return metadata.getKey().equals(this.key)
                && metadata.getValue().equals(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "key='" + this.key + '\'' +
                ", value=" + this.value +
                '}';
    }
}
