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

import dev.kafein.multiduels.common.components.InventoryComponent;
import dev.kafein.multiduels.common.components.PlayerComponent;

import java.util.UUID;

public final class Viewer {
    private final PlayerComponent player;
    private final InventoryComponent.View view;
    private final String target;
    private int page;
    private boolean closed;

    public Viewer(PlayerComponent player, InventoryComponent.View view, String target, int page) {
        this.player = player;
        this.view = view;
        this.target = target;
        this.page = page;
    }

    public static Viewer create(PlayerComponent player, InventoryComponent.View view, String target) {
        return create(player, view, target, 0);
    }

    public static Viewer create(PlayerComponent player, InventoryComponent.View view, String target, int page) {
        return new Viewer(player, view, target, page);
    }

    public PlayerComponent getPlayer() {
        return this.player;
    }

    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    public InventoryComponent.View getView() {
        return this.view;
    }

    public String getTarget() {
        return this.target;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
