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

package dev.kafein.multiduels.common.menu.action;

import com.google.common.collect.ImmutableMap;
import dev.kafein.multiduels.common.MultiDuels;
import dev.kafein.multiduels.common.components.PlayerComponent;
import dev.kafein.multiduels.common.components.ServerComponent;

import static com.google.common.base.Preconditions.checkNotNull;
import static dev.kafein.multiduels.common.utils.Strings.replace;

final class DefaultClickActions {
    static ClickAction CLOSE = (clickContext, registeredClickAction) -> clickContext.getPlayer().closeInventory();

    static ClickAction OPEN = (clickContext, registeredClickAction) -> {
        String menuName = registeredClickAction.getValue();
        checkNotNull(menuName, "Menu name cannot be null");

        MultiDuels plugin = clickContext.getPlugin();
        plugin.getMenuManager().find(menuName).ifPresent(menu -> menu.open(clickContext.getPlayer()));
    };

    static ClickAction COMMAND_CONSOLE = (clickContext, registeredClickAction) -> {
        String command = registeredClickAction.getValue();
        checkNotNull(command, "Command cannot be null");

        MultiDuels plugin = clickContext.getPlugin();

        ServerComponent server = plugin.getServerComponentFactory().create(plugin);
        server.dispatchCommand(replace(command, ImmutableMap.of(
                "%player%", clickContext.getPlayer().getName(),
                "%menu%", clickContext.getMenu().getName()
        )));
    };

    static ClickAction COMMAND_PLAYER = (clickContext, registeredClickAction) -> {
        String command = registeredClickAction.getValue();
        checkNotNull(command, "Command cannot be null");

        PlayerComponent player = clickContext.getPlayer();
        player.performCommand(replace(command, ImmutableMap.of(
                "%player%", player.getName(),
                "%menu%", clickContext.getMenu().getName()
        )));
    };

    private DefaultClickActions() {}
}
