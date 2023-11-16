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

import com.google.common.collect.Sets;
import dev.kafein.multiduels.common.components.InventoryComponent;
import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.menu.button.Button;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class MenuBuilder {
    private MenuProperties.Builder propertiesBuilder;
    private InventoryComponent.Factory inventoryFactory;
    private ItemComponent.Factory itemFactory;
    private Set<Button> buttons;

    MenuBuilder() {
        this.propertiesBuilder = MenuProperties.newBuilder();
        this.buttons = Sets.newHashSet();
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public MenuProperties.Builder properties() {
        return this.propertiesBuilder;
    }

    public MenuBuilder properties(@NotNull MenuProperties.Builder propertiesBuilder) {
        this.propertiesBuilder = propertiesBuilder;
        return this;
    }

    public MenuBuilder properties(@NotNull MenuProperties properties) {
        this.propertiesBuilder = properties.toBuilder();
        return this;
    }

    public MenuBuilder propertiesFromNode(@NotNull ConfigurationNode node) {
        checkNotNull(this.propertiesBuilder.name(), "Name must be set before loading properties from node");
        return properties(MenuProperties.fromNode(this.propertiesBuilder.name(), node));
    }

    public MenuBuilder node(@NotNull ConfigurationNode node) {
        this.propertiesBuilder.node(node);
        return this;
    }

    public MenuBuilder name(@NotNull String name) {
        this.propertiesBuilder.name(name);
        return this;
    }

    public MenuBuilder inventoryTitle(@NotNull String title) {
        this.propertiesBuilder.title(title);
        return this;
    }

    public MenuBuilder inventorySize(int size) {
        this.propertiesBuilder.size(size);
        return this;
    }

    public MenuBuilder openSound(@NotNull String openSound) {
        this.propertiesBuilder.openSound(openSound);
        return this;
    }

    public MenuBuilder closeSound(@NotNull String closeSound) {
        this.propertiesBuilder.closeSound(closeSound);
        return this;
    }

    public InventoryComponent.Factory inventoryFactory() {
        return this.inventoryFactory;
    }

    public MenuBuilder inventoryFactory(@NotNull InventoryComponent.Factory inventoryFactory) {
        this.inventoryFactory = inventoryFactory;
        return this;
    }

    public ItemComponent.Factory itemFactory() {
        return this.itemFactory;
    }

    public MenuBuilder itemFactory(@NotNull ItemComponent.Factory itemFactory) {
        this.itemFactory = itemFactory;
        return this;
    }

    public @Nullable Set<Button> buttons() {
        return this.buttons;
    }

    public MenuBuilder buttons(@NotNull Set<Button> buttons) {
        this.buttons = buttons;
        return this;
    }

    public MenuBuilder buttonsFromNode() {
        ConfigurationNode parent = checkNotNull(this.propertiesBuilder.node(), "node");
        return buttonsFromNode(parent.getNode("menu", "buttons"));
    }

    public MenuBuilder buttonsFromNode(@NotNull ConfigurationNode parent) {
        for (ConfigurationNode node : parent.getChildrenMap().values()) {
            Button button = Button.fromNode(node);
            this.buttons.add(button);
        }

        return this;
    }

    public MenuBuilder button(@NotNull Button button) {
        this.buttons.add(button);
        return this;
    }

    public Menu build() {
        MenuProperties properties = this.propertiesBuilder.build();
        checkNotNull(properties.getName(), "name");
        checkNotNull(properties.getTitle(), "title");
        checkNotNull(this.inventoryFactory, "inventoryFactory");
        checkNotNull(this.itemFactory, "itemFactory");

        return new DefaultMenu(properties, this.inventoryFactory, this.itemFactory, this.buttons);
    }
}
