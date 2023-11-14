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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import dev.kafein.multiduels.common.components.InventoryComponent;
import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.components.PlayerComponent;
import dev.kafein.multiduels.common.menu.action.ClickAction;
import dev.kafein.multiduels.common.menu.action.ClickActionCollection;
import dev.kafein.multiduels.common.menu.button.Button;
import dev.kafein.multiduels.common.menu.button.ClickHandler;
import dev.kafein.multiduels.common.menu.misc.ClickResult;
import dev.kafein.multiduels.common.menu.misc.OpenCause;
import dev.kafein.multiduels.common.menu.misc.contexts.ClickContext;
import dev.kafein.multiduels.common.menu.misc.contexts.OpenContext;
import dev.kafein.multiduels.common.menu.view.Viewer;
import dev.kafein.multiduels.common.menu.view.ViewersHolder;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

abstract class AbstractMenu implements Menu {
    private final MenuProperties properties;
    private final InventoryComponent.Factory inventoryFactory;
    private final ItemComponent.Factory itemFactory;
    private final Set<Button> buttons;
    private final ClickActionCollection clickActions;
    private final ViewersHolder viewers;

    protected AbstractMenu(MenuProperties properties, InventoryComponent.Factory inventoryFactory, ItemComponent.Factory itemFactory) {
        this.properties = properties;
        this.inventoryFactory = inventoryFactory;
        this.itemFactory = itemFactory;
        this.buttons = properties.getNode() == null ? Sets.newHashSet() : loadButtonsFromNode();
        this.clickActions = new ClickActionCollection();
        this.viewers = ViewersHolder.create();
    }

    protected AbstractMenu(MenuProperties properties, InventoryComponent.Factory inventoryFactory, ItemComponent.Factory itemFactory, Set<Button> buttons) {
        this.properties = properties;
        this.inventoryFactory = inventoryFactory;
        this.itemFactory = itemFactory;
        this.buttons = buttons;
        this.clickActions = new ClickActionCollection();
        this.viewers = ViewersHolder.create();
    }

    @Override
    public Viewer open(@NotNull PlayerComponent player) {
        return open(player, 0);
    }

    @Override
    public Viewer open(@NotNull PlayerComponent player, int page) {
        return open(player, page, OpenCause.OPEN);
    }

    @Override
    public Viewer open(@NotNull PlayerComponent player, int page, @NotNull OpenCause cause) {
        OpenContext context = OpenContext.create(player, this, cause, page);

        InventoryComponent inventory = createInventory(context);
        InventoryComponent.View view = inventory.open(player);

        player.playSound(getOpenSound());

        Viewer viewer = Viewer.create(player, view, getName(), page);
        return this.viewers.addViewer(viewer);
    }

    InventoryComponent createInventory(@NotNull OpenContext context) {
        Map<String, String> placeholders = createTitlePlaceholders(context.getPlayer(), context.getPage());

        InventoryComponent inventory = this.inventoryFactory.create(getInventoryProperties());
        getButtons().forEach(button -> {
            Map<Integer, ItemComponent> itemStackMap = button.createItems(context);
            itemStackMap.forEach(inventory::setItem);
        });

        return inventory;
    }

    Map<String, String> createTitlePlaceholders(@NotNull PlayerComponent player, int page) {
        return ImmutableMap.of("%player%", player.getName(), "%page%", String.valueOf(page + 1));
    }

    @Override
    public @Nullable Viewer nextPage(@NotNull UUID uniqueId) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return nextPage(viewer);
    }

    @Override
    public Viewer nextPage(@NotNull PlayerComponent player) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return nextPage(viewer);
    }

    @Override
    public Viewer nextPage(@NotNull Viewer viewer) {
        open(viewer.getPlayer(), viewer.getPage() + 1, OpenCause.CHANGE_PAGE);
        return viewer;
    }

    @Override
    public @Nullable Viewer previousPage(@NotNull UUID uniqueId) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return previousPage(viewer);
    }

    @Override
    public Viewer previousPage(@NotNull PlayerComponent player) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return previousPage(viewer);
    }

    @Override
    public Viewer previousPage(@NotNull Viewer viewer) {
        open(viewer.getPlayer(), viewer.getPage() - 1, OpenCause.CHANGE_PAGE);
        return viewer;
    }

    @Override
    public @Nullable Viewer refresh(@NotNull UUID uniqueId) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return refresh(viewer);
    }

    @Override
    public Viewer refresh(@NotNull PlayerComponent player) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return refresh(viewer);
    }

    @Override
    public Viewer refresh(@NotNull Viewer viewer) {
        OpenContext context = OpenContext.create(viewer.getPlayer(), this, OpenCause.REFRESH, viewer.getPage());

        InventoryComponent inventory = viewer.getView().getTop();
        getButtons().forEach(button -> {
            Map<Integer, ItemComponent> itemStackMap = button.createItems(context);
            itemStackMap.forEach(inventory::setItem);
        });

        return viewer;
    }

    @Override
    public @Nullable Viewer refreshButton(@NotNull UUID uniqueId, int slot) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return refreshButton(viewer, slot);
    }

    @Override
    public @Nullable Viewer refreshButton(@NotNull UUID uniqueId, @NotNull String buttonName) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return refreshButton(viewer, buttonName);
    }

    @Override
    public Viewer refreshButton(@NotNull PlayerComponent player, int slot) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return refreshButton(viewer, slot);
    }

    @Override
    public Viewer refreshButton(@NotNull PlayerComponent player, @NotNull String buttonName) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return refreshButton(viewer, buttonName);
    }

    @Override
    public Viewer refreshButton(@NotNull Viewer viewer, int slot) {
        InventoryComponent inventory = viewer.getView().getTop();
        findButton(slot).ifPresent(button -> {
            OpenContext context = OpenContext.create(viewer.getPlayer(), this, OpenCause.REFRESH, viewer.getPage());

            Map<Integer, ItemComponent> itemStackMap = button.createItems(context);
            itemStackMap.forEach(inventory::setItem);
        });

        return viewer;
    }

    @Override
    public Viewer refreshButton(@NotNull Viewer viewer, @NotNull String buttonName) {
        InventoryComponent inventory = viewer.getView().getTop();
        findButton(buttonName).ifPresent(button -> {
            OpenContext context = OpenContext.create(viewer.getPlayer(), this, OpenCause.REFRESH, viewer.getPage());

            Map<Integer, ItemComponent> itemStackMap = button.createItems(context);
            itemStackMap.forEach(inventory::setItem);
        });

        return viewer;
    }

    @Override
    public @Nullable Viewer close(@NotNull UUID uniqueId) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return close(viewer);
    }

    @Override
    public @Nullable Viewer close(@NotNull PlayerComponent player) {
        return close(player.getUniqueId());
    }

    @Override
    public Viewer close(@NotNull Viewer viewer) {
        if (viewer.isClosed()) {
            return viewer;
        }
        viewer.setClosed(true);

        PlayerComponent player = viewer.getPlayer();
        player.closeInventory();
        player.playSound(getCloseSound());

        return this.viewers.removeViewer(player.getUniqueId());
    }

    @Override
    public ClickResult click(@NotNull ClickContext context) {
        Button button = context.getButton();
        if (button == null) {
            return ClickResult.CANCELLED;
        }

        PlayerComponent player = context.getPlayer();
        player.playSound(button.getClickSound());

        button.getClickActions().forEach(registeredClickAction -> {
            ClickAction clickAction = this.clickActions.findAction(registeredClickAction).orElse(null);
            if (clickAction == null) {
                return;
            }

            clickAction.accept(context, registeredClickAction);
        });

        return button.click(context);
    }

    @Override
    public ClickResult clickEmptySlot(@NotNull ClickContext context) {
        return ClickResult.CANCELLED;
    }

    @Override
    public ClickResult clickBottomInventory(@NotNull ClickContext context) {
        return ClickResult.CANCELLED;
    }

    @Override
    public ClickActionCollection getClickActions() {
        return this.clickActions;
    }

    @Override
    public void registerClickAction(@NotNull String key, @NotNull ClickAction action) {
        this.clickActions.registerAction(key, action);
    }

    @Override
    public void unregisterClickAction(@NotNull String key) {
        this.clickActions.unregisterAction(key);
    }

    @Override
    public ViewersHolder getViewers() {
        return this.viewers;
    }

    @Override
    public boolean isViewing(@NotNull PlayerComponent player) {
        return isViewing(player.getUniqueId());
    }

    @Override
    public boolean isViewing(@NotNull UUID uniqueId) {
        return this.viewers.containsViewer(uniqueId);
    }

    @Override
    public void stopViewing() {
        Map<UUID, Viewer> viewersSafe = this.viewers.getViewersSafe();
        viewersSafe.values().forEach(this::close);
    }

    @Override
    public MenuProperties getProperties() {
        return this.properties;
    }

    @Override
    public @Nullable ConfigurationNode getNode() {
        return this.properties.getNode();
    }

    @Override
    public String getName() {
        return this.properties.getName();
    }

    @Override
    public InventoryComponent.Factory getInventoryFactory() {
        return this.inventoryFactory;
    }

    @Override
    public InventoryComponent.Properties getInventoryProperties() {
        return this.properties.getInventoryProperties();
    }

    @Override
    public String getTitle() {
        return this.properties.getTitle();
    }

    @Override
    public int getSize() {
        return this.properties.getSize();
    }

    @Override
    public ItemComponent.Factory getItemFactory() {
        return this.itemFactory;
    }

    @Override
    public @Nullable Set<Button> loadButtonsFromNode() {
        if (getNode() == null) {
            return null;
        }

        ConfigurationNode node = getNode().getNode("buttons");
        return loadButtonsFromNode(node);
    }

    @Override
    public @Nullable Set<Button> loadButtonsFromNode(@NotNull ConfigurationNode node) {
        Set<Button> nodeButtons = new HashSet<>();

        for (ConfigurationNode child : node.getChildrenMap().values()) {
            Button button = Button.fromNode(child);
            nodeButtons.add(button);
        }

        return nodeButtons;
    }

    @Override
    public Set<Button> getButtons() {
        return this.buttons;
    }

    @Override
    public Optional<Button> findButton(@NotNull String name) {
        return this.buttons.stream()
                .filter(button -> button.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<Button> findButton(int slot) {
        return this.buttons.stream()
                .filter(button -> button.hasSlot(slot))
                .findFirst();
    }

    @Override
    public void putButtons(@NotNull Button... buttons) {
        for (Button button : buttons) {
            putButton(button);
        }
    }

    @Override
    public void putButtons(@NotNull Iterable<Button> buttons) {
        for (Button button : buttons) {
            putButton(button);
        }
    }

    @Override
    public void putButton(@NotNull Button button) {
        this.buttons.add(button);
    }

    @Override
    public void removeButtons(@NotNull Button... buttons) {
        for (Button button : buttons) {
            removeButton(button);
        }
    }

    @Override
    public void removeButtons(@NotNull Iterable<Button> buttons) {
        for (Button button : buttons) {
            removeButton(button);
        }
    }

    @Override
    public void removeButton(@NotNull Button button) {
        this.buttons.remove(button);
    }

    @Override
    public void removeButton(@NotNull String name) {
        findButton(name).ifPresent(this::removeButton);
    }

    @Override
    public @Nullable Button registerClickHandler(@NotNull String buttonName, @NotNull ClickHandler handler) {
        Optional<Button> optionalButton = findButton(buttonName);
        return optionalButton.map(button -> registerClickHandler(button, handler)).orElse(null);
    }

    @Override
    public @Nullable Button registerClickHandler(@NotNull Button button, @NotNull ClickHandler handler) {
        button.setClickHandler(handler);
        return button;
    }

    @Override
    public @Nullable String getOpenSound() {
        return this.properties.getOpenSound();
    }

    @Override
    public @Nullable String getCloseSound() {
        return this.properties.getCloseSound();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Menu)) return false;
        if (obj == this) return true;

        Menu menu = (Menu) obj;
        return menu.getName().equals(getName());
    }
}
