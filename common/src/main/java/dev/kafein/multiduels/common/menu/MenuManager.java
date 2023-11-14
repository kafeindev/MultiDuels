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

package dev.kafein.multiduels.common.menu;

import dev.kafein.multiduels.common.components.PlayerComponent;
import dev.kafein.multiduels.common.manager.AbstractManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public final class MenuManager extends AbstractManager<String, Menu> {
    public void stopViewing() {
        getValues().forEach(Menu::stopViewing);
    }

    public void stopViewing(@NotNull String name) {
        find(name).ifPresent(Menu::stopViewing);
    }

    public void stopViewing(@NotNull Menu menu) {
        menu.stopViewing();
    }

    public void stopViewing(@NotNull UUID uniqueId) {
        findByViewer(uniqueId).ifPresent(menu -> menu.close(uniqueId));
    }

    public void stopViewing(@NotNull PlayerComponent player) {
        findByViewer(player.getUniqueId()).ifPresent(menu -> menu.close(player));
    }

    public Optional<Menu> find(@NotNull Class<? extends Menu> menuClass) {
        return this.getValues().stream()
                .filter(menuClass::isInstance)
                .findFirst();
    }

    public Optional<Menu> findByViewer(@NotNull UUID uniqueId) {
        return this.getValues().stream()
                .filter(menu -> menu.isViewing(uniqueId))
                .findFirst();
    }

    public void register(@NotNull Menu menu) {
        put(menu.getName(), menu);
    }

    public void register(@NotNull Iterable<Menu> menus) {
        menus.forEach(this::register);
    }
}
