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

package dev.kafein.multiduels.bukkit.command;

import dev.kafein.multiduels.common.command.Command;
import dev.kafein.multiduels.common.command.CommandRegistrar;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class BukkitCommandRegistrar implements CommandRegistrar {
    private final String pluginName;
    private final CommandMap commandMap;

    public BukkitCommandRegistrar(final @NotNull Plugin plugin) {
        this.pluginName = plugin.getName().toLowerCase(Locale.ENGLISH);
        this.commandMap = getCommandMapFromServer();
    }

    @Override
    public void registerCommand(final @NotNull Command command) {
        BukkitCommand bukkitCommand = new BukkitCommandAdapter(command);
        this.commandMap.register(this.pluginName, bukkitCommand);
    }

    private CommandMap getCommandMapFromServer() {
        Server server = Bukkit.getServer();

        try {
            Method commandMapMethod = server.getClass().getDeclaredMethod("getCommandMap");
            commandMapMethod.setAccessible(true);

            return (CommandMap) commandMapMethod.invoke(server);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get command map from bukkit server.", e);
        }
    }
}
