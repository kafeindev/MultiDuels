/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Kafein
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

package dev.kafein.multiduels.common.manager;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractManager<K, V> implements Manager<K, V> {
    protected final Map<K, V> map;

    protected AbstractManager() {
        this.map = Maps.newConcurrentMap();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public Map<K, V> getMap() {
        return this.map;
    }

    @Override
    public Collection<K> getKeys() {
        return this.map.keySet();
    }

    @Override
    public Collection<V> getValues() {
        return this.map.values();
    }

    @Override
    public Optional<V> find(@NotNull K key) {
        return Optional.ofNullable(this.map.get(key));
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        return find(key).orElse(null);
    }

    @Override
    public void putAll(@NotNull Map<K, V> map) {
        this.map.putAll(map);
    }

    @Override
    public V put(@NotNull K key, @NotNull V value) {
        this.map.put(key, value);

        return value;
    }

    @Override
    public void remove(@NotNull K key) {
        this.map.remove(key);
    }

    @Override
    public boolean removeIf(@NotNull Predicate<? super V> filter) {
        return this.map.values().removeIf(filter);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public boolean contains(@NotNull K key) {
        return this.map.containsKey(key);
    }

    @Override
    public int size() {
        return this.map.size();
    }
}
