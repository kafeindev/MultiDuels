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

import dev.kafein.multiduels.common.components.InventoryComponent;
import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.components.PlayerComponent;
import dev.kafein.multiduels.common.configuration.Config;
import dev.kafein.multiduels.common.configuration.ConfigBuilder;
import dev.kafein.multiduels.common.menu.action.ClickAction;
import dev.kafein.multiduels.common.menu.action.ClickActionCollection;
import dev.kafein.multiduels.common.menu.button.Button;
import dev.kafein.multiduels.common.menu.button.ClickHandler;
import dev.kafein.multiduels.common.menu.misc.ClickResult;
import dev.kafein.multiduels.common.menu.misc.OpenCause;
import dev.kafein.multiduels.common.menu.misc.contexts.ClickContext;
import dev.kafein.multiduels.common.menu.view.Viewer;
import dev.kafein.multiduels.common.menu.view.ViewersHolder;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Menu {
    static Menu fromConfig(String name, Config config) {
        return fromNode(name, config.getNode());
    }

    static Menu fromConfig(String name, Class<?> clazz, String resource, String... path) {
        Config config = ConfigBuilder.builder(path)
                .resource(clazz, resource)
                .build();
        return fromConfig(name, config);
    }

    static Menu fromNode(String name, ConfigurationNode node) {
        return new MenuBuilder()
                .name(name)
                .node(node)
                .propertiesFromNode(node)
                .buttonsFromNode(node)
                .build();
    }

    static MenuBuilder newBuilder() {
        return new MenuBuilder();
    }

    Viewer open(@NotNull PlayerComponent player);

    Viewer open(@NotNull PlayerComponent player, int page);

    Viewer open(@NotNull PlayerComponent player, int page, @NotNull OpenCause cause);

    @Nullable Viewer nextPage(@NotNull UUID uniqueId);

    Viewer nextPage(@NotNull PlayerComponent player);

    Viewer nextPage(@NotNull Viewer viewer);

    @Nullable Viewer previousPage(@NotNull UUID uniqueId);

    Viewer previousPage(@NotNull PlayerComponent player);

    Viewer previousPage(@NotNull Viewer viewer);

    @Nullable Viewer refresh(@NotNull UUID uniqueId);

    Viewer refresh(@NotNull PlayerComponent player);

    Viewer refresh(@NotNull Viewer viewer);

    @Nullable Viewer refreshButton(@NotNull UUID uniqueId, int slot);

    @Nullable Viewer refreshButton(@NotNull UUID uniqueId, @NotNull String buttonName);

    @Nullable Viewer refreshButton(@NotNull PlayerComponent player, int slot);

    @Nullable Viewer refreshButton(@NotNull PlayerComponent player, @NotNull String buttonName);

    Viewer refreshButton(@NotNull Viewer viewer, int slot);

    Viewer refreshButton(@NotNull Viewer viewer, @NotNull String buttonName);

    @Nullable Viewer close(@NotNull UUID uniqueId);

    @Nullable Viewer close(@NotNull PlayerComponent player);

    Viewer close(@NotNull Viewer viewer);

    ClickResult click(@NotNull ClickContext context);

    ClickResult clickEmptySlot(@NotNull ClickContext context);

    ClickResult clickBottomInventory(@NotNull ClickContext context);

    ClickActionCollection getClickActions();

    void registerClickAction(@NotNull String key, @NotNull ClickAction action);

    void unregisterClickAction(@NotNull String key);

    ViewersHolder getViewers();

    boolean isViewing(@NotNull PlayerComponent player);

    boolean isViewing(@NotNull UUID uniqueId);

    void stopViewing();

    MenuProperties getProperties();

    @Nullable ConfigurationNode getNode();

    String getName();

    InventoryComponent.Factory getInventoryFactory();

    InventoryComponent.Properties getInventoryProperties();

    String getTitle();

    int getSize();

    ItemComponent.Factory getItemFactory();

    @Nullable Set<Button> loadButtonsFromNode();

    @Nullable Set<Button> loadButtonsFromNode(@NotNull ConfigurationNode node);

    Set<Button> getButtons();

    Optional<Button> findButton(@NotNull String name);

    Optional<Button> findButton(int slot);

    void putButtons(@NotNull Button... buttons);

    void putButtons(@NotNull Iterable<Button> buttons);

    void putButton(@NotNull Button button);

    void removeButtons(@NotNull Button... buttons);

    void removeButtons(@NotNull Iterable<Button> buttons);

    void removeButton(@NotNull Button button);

    void removeButton(@NotNull String name);

    @Nullable Button registerClickHandler(@NotNull String buttonName, @NotNull ClickHandler handler);

    @Nullable Button registerClickHandler(@NotNull Button button, @NotNull ClickHandler handler);

    @Nullable String getOpenSound();

    @Nullable String getCloseSound();
}
