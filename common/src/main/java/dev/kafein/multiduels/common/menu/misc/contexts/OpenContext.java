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

package dev.kafein.multiduels.common.menu.misc.contexts;

import dev.kafein.multiduels.common.components.PlayerComponent;
import dev.kafein.multiduels.common.menu.Menu;
import dev.kafein.multiduels.common.menu.misc.OpenCause;

public final class OpenContext {
    private final PlayerComponent player;
    private final Menu menu;
    private final OpenCause cause;
    private final int page;

    public OpenContext(PlayerComponent player, Menu menu, OpenCause cause, int page) {
        this.player = player;
        this.menu = menu;
        this.cause = cause;
        this.page = page;
    }

    public static OpenContext create(PlayerComponent player, Menu menu, OpenCause cause, int page) {
        return new OpenContext(player, menu, cause, page);
    }

    public PlayerComponent getPlayer() {
        return this.player;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public OpenCause getCause() {
        return this.cause;
    }

    public int getPage() {
        return this.page;
    }
}
