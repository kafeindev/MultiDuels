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

package dev.kafein.multiduels.bukkit.components;

import dev.kafein.multiduels.common.components.InventoryComponent;
import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.components.PlayerComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class BukkitInventoryComponent implements InventoryComponent {
    private static final Factory FACTORY = new FactoryImpl();

    private final Inventory inventory;

    private BukkitInventoryComponent(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public View open(@NotNull PlayerComponent player) {
        Player bukkitPlayer = BukkitPlayerComponent.getWrapper().wrap(player);
        if (bukkitPlayer == null) {
            throw new IllegalArgumentException("Player is not online");
        }

        InventoryView view = bukkitPlayer.openInventory(this.inventory);
        return new ViewImpl(view);
    }

    @Override
    public void close(@NotNull PlayerComponent player) {
        player.closeInventory();
    }

    @Override
    public void addItem(@NotNull ItemComponent item) {
        ItemStack wrapped = BukkitItemComponent.getWrapper().wrap(item);
        this.inventory.addItem(wrapped);
    }

    @Override
    public void setItem(int slot, @NotNull ItemComponent item) {
        ItemStack wrapped = BukkitItemComponent.getWrapper().wrap(item);
        this.inventory.setItem(slot, wrapped);
    }

    @Override
    public void removeItem(int slot) {
        this.inventory.setItem(slot, null);
    }

    public static Factory getFactory() {
        return FACTORY;
    }

    private static final class ViewImpl implements View {
        private final InventoryComponent top;
        private final InventoryComponent bottom;

        private ViewImpl(InventoryView view) {
            this.top = new BukkitInventoryComponent(view.getTopInventory());
            this.bottom = new BukkitInventoryComponent(view.getBottomInventory());
        }

        private ViewImpl(InventoryComponent top, InventoryComponent bottom) {
            this.top = top;
            this.bottom = bottom;
        }

        @Override
        public InventoryComponent getTop() {
            return this.top;
        }

        @Override
        public InventoryComponent getBottom() {
            return this.bottom;
        }
    }

    private static final class FactoryImpl implements Factory {
        @Override
        public InventoryComponent create(@NotNull Properties properties) {
            if (properties.getType() != null) {
                return create(properties.getTitle(), properties.getType());
            } else {
                return create(properties.getTitle(), properties.getSize());
            }
        }

        @Override
        public InventoryComponent create(@NotNull String title, int size) {
            Inventory inventory = Bukkit.createInventory(null, size, title);
            return new BukkitInventoryComponent(inventory);
        }

        @Override
        public InventoryComponent create(@NotNull String title, @NotNull String type) {
            Inventory inventory = Bukkit.createInventory(null, InventoryType.valueOf(type), title);
            return new BukkitInventoryComponent(inventory);
        }
    }
}
