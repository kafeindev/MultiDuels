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

package dev.kafein.multiduels.bukkit;

import dev.kafein.multiduels.bukkit.command.BukkitCommandRegistrar;
import dev.kafein.multiduels.bukkit.components.BukkitInventoryComponent;
import dev.kafein.multiduels.bukkit.components.BukkitItemComponent;
import dev.kafein.multiduels.bukkit.components.BukkitPlayerComponent;
import dev.kafein.multiduels.bukkit.components.BukkitServerComponent;
import dev.kafein.multiduels.common.AbstractMultiDuels;
import dev.kafein.multiduels.common.MultiDuels;
import dev.kafein.multiduels.common.command.CommandRegistrar;
import dev.kafein.multiduels.common.components.InventoryComponent;
import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.components.PlayerComponent;
import dev.kafein.multiduels.common.components.ServerComponent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class BukkitMultiDuels extends AbstractMultiDuels {
    private static final Wrapper<Plugin> WRAPPER = new WrapperImpl();

    private final Plugin plugin;

    public BukkitMultiDuels(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected @NotNull CommandRegistrar getCommandRegistrar() {
        return new BukkitCommandRegistrar(this.plugin);
    }

    @Override
    public ServerComponent.Factory getServerComponentFactory() {
        return BukkitServerComponent.getFactory();
    }

    @Override
    public PlayerComponent.Wrapper<?> getPlayerComponentWrapper() {
        return BukkitPlayerComponent.getWrapper();
    }

    @Override
    public ItemComponent.Factory getItemComponentFactory() {
        return BukkitItemComponent.getFactory();
    }

    @Override
    public ItemComponent.Wrapper<?> getItemComponentWrapper() {
        return BukkitItemComponent.getWrapper();
    }

    @Override
    public InventoryComponent.Factory getInventoryComponentFactory() {
        return BukkitInventoryComponent.getFactory();
    }

    private Plugin getHandle() {
        return this.plugin;
    }

    public static Wrapper<Plugin> getWrapper() {
        return WRAPPER;
    }

    private static final class WrapperImpl implements Wrapper<Plugin> {
        private WrapperImpl() {}

        @Override
        public Plugin wrap(MultiDuels plugin) {
            return ((BukkitMultiDuels) plugin).getHandle();
        }
    }
}
