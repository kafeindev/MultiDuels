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

import dev.kafein.multiduels.common.MultiDuels;
import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.components.PlayerComponent;
import dev.kafein.multiduels.common.menu.Menu;
import dev.kafein.multiduels.common.menu.button.Button;
import dev.kafein.multiduels.common.menu.misc.ClickType;

public final class ClickContext {
    private final MultiDuels plugin;

    private final PlayerComponent player;
    private final Menu menu;
    private final Button button;
    private final ItemComponent item;
    private final ItemComponent cursor;
    private final ClickType clickType;
    private final int slot;

    public ClickContext(MultiDuels plugin, Menu menu, PlayerComponent player, Button button, ItemComponent item, ItemComponent cursor, ClickType clickType, int slot) {
        this.plugin = plugin;
        this.menu = menu;
        this.player = player;
        this.button = button;
        this.item = item;
        this.cursor = cursor;
        this.clickType = clickType;
        this.slot = slot;
    }

    public static ClickContext create(MultiDuels plugin, Menu menu, PlayerComponent player, Button button, ItemComponent item, ItemComponent cursor, ClickType clickType, int slot) {
        return new ClickContext(plugin, menu, player, button, item, cursor, clickType, slot);
    }

    public MultiDuels getPlugin() {
        return this.plugin;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public PlayerComponent getPlayer() {
        return this.player;
    }


    public Button getButton() {
        return this.button;
    }

    public ItemComponent getItem() {
        return this.item;
    }

    public ItemComponent getCursor() {
        return this.cursor;
    }

    public ClickType getClickType() {
        return this.clickType;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isLeftClick() {
        return this.clickType == ClickType.LEFT;
    }

    public boolean isRightClick() {
        return this.clickType == ClickType.RIGHT;
    }

    public boolean isShiftClick() {
        return this.clickType == ClickType.SHIFT_LEFT || this.clickType == ClickType.SHIFT_RIGHT;
    }
}
