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

package dev.kafein.multiduels.common.configuration;

import com.google.common.reflect.TypeToken;
import dev.kafein.multiduels.common.utils.reflect.Fields;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

final class KeyInjector {
    KeyInjector() {}

    void inject(@NotNull Class<?> clazz, @NotNull Config config) {
        inject(clazz, config.getNode());
    }

    void inject(@NotNull Class<?> clazz, @NotNull ConfigurationNode node) {
        for (Field field : clazz.getDeclaredFields()) {
            inject(field, node);
        }
    }

    void inject(@NotNull Field field, @NotNull Config config) {
        inject(field, config.getNode());
    }

    void inject(@NotNull Field field, @NotNull ConfigurationNode node) {
        if (!field.getType().isAssignableFrom(ConfigKey.class)) {
            return;
        }

        try {
            ConfigKey<?> key = (ConfigKey<?>) field.get(null);
            Class<?> type = key.getValue().getClass();

            ConfigurationNode child = node.getNode(Arrays.asList(key.getPath()));
            Object value = Collection.class.isAssignableFrom(type) ? child.getValue() : child.getValue(TypeToken.of(type));

            Fields.set(field, key, value, "value");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject config key: " + field.getName(), e);
        } catch (ObjectMappingException e) {
            throw new RuntimeException("Failed to deserialize config key: " + field.getName(), e);
        }
    }
}
