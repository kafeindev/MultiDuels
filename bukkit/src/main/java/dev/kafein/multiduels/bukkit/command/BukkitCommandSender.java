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

import dev.kafein.multiduels.common.command.CommandSender;
import dev.kafein.multiduels.common.components.PlayerComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final class BukkitCommandSender implements CommandSender {
    private final org.bukkit.command.CommandSender sender;

    BukkitCommandSender(@NotNull org.bukkit.command.CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public void sendMessage(@NotNull String message) {

    }

    @Override
    public void sendMessage(@NotNull String... messages) {

    }

    @Override
    public void sendMessage(@NotNull Iterable<String> messages) {

    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return false;
    }

    @Override
    public PlayerComponent asPlayer() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CommandSender)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        CommandSender other = (CommandSender) obj;
        return this.getName().equals(other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName());
    }

    @Override
    public String toString() {
        return "CommandSender{" +
                "name='" + this.getName() + '\'' +
                '}';
    }
}
