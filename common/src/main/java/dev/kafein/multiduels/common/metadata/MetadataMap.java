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

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public final class MetadataMap {
    private final Map<String, Metadata<?>> metadataMap;

    public MetadataMap() {
        this.metadataMap = Maps.newHashMap();
    }

    public <T> void put(@NotNull Metadata<T> metadata) {
        this.metadataMap.put(metadata.getKey(), metadata);
    }

    public <T> void putIfAbsent(@NotNull Metadata<T> metadata) {
        this.metadataMap.putIfAbsent(metadata.getKey(), metadata);
    }

    public Map<String, Metadata<?>> getAll() {
        return this.metadataMap;
    }

    public <T> @Nullable Metadata<T> get(@NotNull String key) {
        return (Metadata<T>) find(key).orElse(null);
    }

    public <T> @Nullable Metadata<T> get(@NotNull Enum<?> key) {
        return get(key.name());
    }

    public <T> @Nullable Metadata<T> get(@NotNull String key, @NotNull Class<T> type) {
        return (Metadata<T>) find(key)
                .filter(metadata -> metadata.getValue().getClass().equals(type))
                .orElse(null);
    }

    public <T> @Nullable Metadata<T> get(@NotNull Enum<?> key, @NotNull Class<T> type) {
        return get(key.name(), type);
    }

    public <T> Map<String, Metadata<T>> get(@NotNull Predicate<String> predicate, @NotNull Class<T> type) {
        return this.metadataMap.entrySet().stream()
                .filter(entry -> predicate.test(entry.getKey()) && entry.getValue().getValue().getClass().equals(type))
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), (Metadata<T>) entry.getValue()), HashMap::putAll);
    }

    public <T> Metadata<T> getOrDefault(@NotNull String key, @NotNull Metadata<T> defaultValue) {
        return (Metadata<T>) this.metadataMap.getOrDefault(key, defaultValue);
    }

    public <T> Metadata<T> getOrDefault(@NotNull Enum<?> key, @NotNull Metadata<T> defaultValue) {
        return getOrDefault(key.name(), defaultValue);
    }

    public <T> Optional<Metadata<T>> find(@NotNull String key) {
        return Optional.ofNullable((Metadata<T>) this.metadataMap.get(key));
    }

    public <T> Optional<Metadata<T>> find(@NotNull Enum<?> key) {
        return find(key.name());
    }

    public <T> boolean ifPresent(@NotNull String key, @NotNull Consumer<? super T> consumer) {
        return find(key).map(metadata -> {
            consumer.accept(((Metadata<T>) metadata).getValue());
            return true;
        }).orElse(false);
    }

    public <T> boolean ifPresent(@NotNull Enum<?> key, @NotNull Consumer<? super T> consumer) {
        return ifPresent(key.name(), consumer);
    }

    public boolean contains(@NotNull String key) {
        return this.metadataMap.containsKey(key);
    }

    public boolean contains(@NotNull String... keys) {
        for (String key : keys) {
            if (!contains(key)) return false;
        }
        return true;
    }

    public boolean contains(@NotNull Enum<?> key) {
        return contains(key.name());
    }

    public boolean contains(@NotNull Enum<?>... keys) {
        for (Enum<?> key : keys) {
            if (!contains(key)) return false;
        }
        return true;
    }

    public void remove(@NotNull String key) {
        this.metadataMap.remove(key);
    }

    public void remove(@NotNull Enum<?> key) {
        remove(key.name());
    }

    public <T> void remove(@NotNull Metadata<T> metadata) {
        this.metadataMap.remove(metadata.getKey());
    }

    public void clear() {
        this.metadataMap.clear();
    }

    public int size() {
        return this.metadataMap.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MetadataMap)) return false;

        MetadataMap metadataMap = (MetadataMap) obj;
        return this.metadataMap.equals(metadataMap.metadataMap);
    }

    @Override
    public int hashCode() {
        return this.metadataMap.hashCode();
    }

    @Override
    public String toString() {
        return "MetadataMap{" +
                "metadataMap=" + this.metadataMap +
                '}';
    }
}
