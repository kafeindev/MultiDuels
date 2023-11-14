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

import dev.kafein.multiduels.common.MultiDuels;
import dev.kafein.multiduels.common.components.ServerComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

public final class BukkitServerComponent implements ServerComponent {
    private static final Factory FACTORY = new FactoryImpl();

    private final Server server;

    public BukkitServerComponent() {
        this(Bukkit.getServer());
    }

    public BukkitServerComponent(Server server) {
        this.server = server;
    }

    @Override
    public void dispatchCommand(@NotNull String command) {
        this.server.dispatchCommand(this.server.getConsoleSender(), command);
    }

    public static Factory getFactory() {
        return FACTORY;
    }

    private static final class FactoryImpl implements Factory {
        @Override
        public ServerComponent create(@NotNull MultiDuels plugin) {
            return new BukkitServerComponent();
        }
    }
}
