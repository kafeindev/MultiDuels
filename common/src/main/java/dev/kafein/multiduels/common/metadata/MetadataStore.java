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

import java.util.*;
import java.util.function.Consumer;

public final class MetadataStore {
    private final Map<UUID, MetadataMap> playerMetadata;

    public MetadataStore() {
        this.playerMetadata = Maps.newHashMap();
    }

    public void put(@NotNull UUID uniqueId, @NotNull MetadataMap metadataMap) {
        this.playerMetadata.put(uniqueId, metadataMap);
    }

    public void put(@NotNull UUID uniqueId, @NotNull Metadata<?> metadata) {
        MetadataMap map = getOrDefault(uniqueId, new MetadataMap());
        map.put(metadata);

        put(uniqueId, map);
    }

    public Map<UUID, MetadataMap> getAll() {
        return this.playerMetadata;
    }

    public Map<UUID, MetadataMap> getByKey(@NotNull String key) {
        return this.playerMetadata.entrySet().stream()
                .filter(entry -> entry.getValue().contains(key))
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }

    public Map<UUID, MetadataMap> getByKey(@NotNull String... keys) {
        return this.playerMetadata.entrySet().stream()
                .filter(entry -> entry.getValue().contains(keys))
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }

    public Map<UUID, MetadataMap> getByKey(@NotNull Enum<?> key) {
        return getByKey(key.name());
    }

    public Map<UUID, MetadataMap> getByKey(@NotNull Enum<?>... keys) {
        return getByKey(Arrays.stream(keys).map(Enum::name).toArray(String[]::new));
    }

    public <T> Set<UUID> getByKeyAndValue(@NotNull String key, @NotNull T value) {
        return getByKey(key).entrySet().stream()
                .filter(entry -> entry.getValue().get(key).equals(value))
                .map(Map.Entry::getKey)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    public <T> Set<UUID> getByKeyAndValue(@NotNull Enum<?> key, @NotNull T value) {
        return getByKeyAndValue(key.name(), value);
    }

    public @Nullable MetadataMap get(@NotNull UUID uniqueId) {
        return this.playerMetadata.get(uniqueId);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(@NotNull UUID uniqueId, @NotNull String key) {
        if (!this.playerMetadata.containsKey(uniqueId)) {
            return null;
        }

        MetadataMap metadataMap = this.playerMetadata.get(uniqueId);
        if (!metadataMap.contains(key)) {
            return null;
        }

        return (T) metadataMap.get(key).getValue();
    }

    public <T> @Nullable T get(@NotNull UUID uniqueId, @NotNull Enum<?> key) {
        return get(uniqueId, key.name());
    }

    public MetadataMap getOrDefault(@NotNull UUID uniqueId, @NotNull MetadataMap defaultValue) {
        return this.playerMetadata.getOrDefault(uniqueId, defaultValue);
    }

    public boolean ifPresent(@NotNull UUID uniqueId, @NotNull Consumer<MetadataMap> consumer) {
        if (this.playerMetadata.containsKey(uniqueId)) {
            consumer.accept(this.playerMetadata.get(uniqueId));
            return true;
        }
        return false;
    }

    public void ifPresent(@NotNull UUID uniqueId, @NotNull String key,
                          @NotNull Consumer<MetadataMap> consumer) {
        if (!this.playerMetadata.containsKey(uniqueId)) {
            return;
        }

        MetadataMap metadataMap = this.playerMetadata.get(uniqueId);
        if (metadataMap.contains(key)) {
            consumer.accept(metadataMap);
        }
    }

    public void ifPresent(@NotNull UUID uniqueId, @NotNull Enum<?> key,
                          @NotNull Consumer<MetadataMap> consumer) {
        ifPresent(uniqueId, key.name(), consumer);
    }

    public boolean containsKey(@NotNull UUID uniqueId) {
        return this.playerMetadata.containsKey(uniqueId);
    }

    public boolean containsKey(@NotNull UUID uniqueId, @NotNull String key) {
        if (!this.playerMetadata.containsKey(uniqueId)) {
            return false;
        }

        MetadataMap metadataMap = this.playerMetadata.get(uniqueId);
        return metadataMap.contains(key);
    }

    public void remove(@NotNull UUID uniqueId) {
        this.playerMetadata.remove(uniqueId);
    }

    public void remove(@NotNull UUID uniqueId, @NotNull String key) {
        if (!this.playerMetadata.containsKey(uniqueId)) {
            return;
        }

        MetadataMap metadataMap = this.playerMetadata.get(uniqueId);
        metadataMap.remove(key);
        this.playerMetadata.put(uniqueId, metadataMap);
    }

    public void remove(@NotNull UUID uniqueId, @NotNull Enum<?> key) {
        remove(uniqueId, key.name());
    }

    public void clear() {
        this.playerMetadata.clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MetadataStore)) {
            return false;
        }

        MetadataStore metadataStore = (MetadataStore) obj;
        return this.playerMetadata.equals(metadataStore.playerMetadata);
    }

    @Override
    public int hashCode() {
        return this.playerMetadata.hashCode();
    }

    @Override
    public String toString() {
        return "MetadataStore{" +
                "playerMetadata=" + this.playerMetadata +
                '}';
    }
}
