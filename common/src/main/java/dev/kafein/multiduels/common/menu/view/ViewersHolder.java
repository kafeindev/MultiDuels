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

package dev.kafein.multiduels.common.menu.view;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public final class ViewersHolder {
    private final Map<UUID, Viewer> viewers;

    public ViewersHolder() {
        this.viewers = Maps.newHashMap();
    }

    public ViewersHolder(Map<UUID, Viewer> viewers) {
        this.viewers = viewers;
    }

    public static ViewersHolder create() {
        return new ViewersHolder();
    }

    public static ViewersHolder create(Map<UUID, Viewer> viewers) {
        return new ViewersHolder(viewers);
    }

    public Map<UUID, Viewer> getViewers() {
        return this.viewers;
    }

    public Map<UUID, Viewer> getViewersSafe() {
        return Maps.newHashMap(this.viewers);
    }

    public @Nullable Viewer getViewer(@NotNull UUID uuid) {
        return this.viewers.get(uuid);
    }

    public Viewer addViewer(@NotNull Viewer viewer) {
        this.viewers.put(viewer.getUniqueId(), viewer);
        return viewer;
    }

    public @Nullable Viewer removeViewer(@NotNull UUID uuid) {
        return this.viewers.remove(uuid);
    }

    public @Nullable Viewer removeViewer(@NotNull Viewer viewer) {
        return this.viewers.remove(viewer.getUniqueId());
    }

    public void clear() {
        this.viewers.clear();
    }

    public boolean containsViewer(@NotNull UUID uuid) {
        return this.viewers.containsKey(uuid);
    }

    public boolean containsViewer(@NotNull Viewer viewer) {
        return this.viewers.containsKey(viewer.getUniqueId());
    }

    public boolean isEmpty() {
        return this.viewers.isEmpty();
    }

    public int size() {
        return this.viewers.size();
    }

    public Map<UUID, Viewer> toMap() {
        return Maps.newHashMap(this.viewers);
    }
}
