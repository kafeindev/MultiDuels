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

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public final class ConfigKey<T> {
    private final String[] path;
    private final String key;
    private final T value;

    public ConfigKey(String[] path, String key, T defaultValue) {
        this.path = path;
        this.key = key;
        this.value = defaultValue;
    }

    @NotNull
    public static <T> ConfigKey<T> of(@NotNull T defaultValue, @NotNull String... path) {
        return new ConfigKey<>(path, path[path.length - 1], defaultValue);
    }

    @NotNull
    public static <T> ConfigKey<T> of(@NotNull T defaultValue, @NotNull String[] path, @NotNull String key) {
        return new ConfigKey<>(path, key, defaultValue);
    }

    public String[] getPath() {
        return this.path;
    }

    public String getKey() {
        return this.key;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConfigKey)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        ConfigKey<?> configKey = (ConfigKey<?>) obj;
        return Arrays.equals(this.path, configKey.path)
                && this.key.equals(configKey.key);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.path) ^ Objects.hash(this.key);
    }

    @Override
    public String toString() {
        return "ConfigKey{" +
                "path=" + Arrays.toString(this.path) +
                ", key='" + this.key + '\'' +
                '}';
    }
}
