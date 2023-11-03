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

package dev.kafein.multiduels.common.utils.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public final class Fields {
    private Fields() {}

    public static void set(@NotNull Field field, @NotNull Object value) {
        set(field, null, value);
    }

    public static void set(@NotNull Field field, @Nullable Object instance, @NotNull Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field", e);
        }
    }

    public static void set(@NotNull Field field, @NotNull Object instance, @NotNull Object value, @NotNull String subFieldName) {
        try {
            Field subField = field.getType().getDeclaredField(subFieldName);
            set(subField, instance, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to set field", e);
        }
    }
}
