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

package dev.kafein.multiduels.common.components;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface PlayerComponent {
    UUID getUniqueId();

    String getName();

    boolean isOnline();

    LocationComponent getLocation();

    void teleport(@NotNull LocationComponent location);

    InventoryComponent.View openInventory(@NotNull InventoryComponent inventory);

    InventoryComponent.View openInventory(@NotNull InventoryComponent.Properties properties);

    void closeInventory();

    void performCommand(@NotNull String command);

    void playSound(@NotNull String sound);

    void playSound(@NotNull String sound, float volume, float pitch);

    void hidePlayer(@NotNull PlayerComponent player);

    void hidePlayer(@NotNull PlayerComponent... players);

    void hidePlayer(@NotNull Iterable<PlayerComponent> players);

    void showPlayer(@NotNull PlayerComponent player);

    void showPlayer(@NotNull PlayerComponent... players);

    void showPlayer(@NotNull Iterable<PlayerComponent> players);

    void hideAllPlayers();

    void showAllPlayers();

    void sendMessage(@NotNull Component message);

    void sendMessage(@NotNull Component... messages);

    void sendMessage(@NotNull String message);

    void sendMessage(@NotNull String message, @Nullable Map<String, String> placeholders);

    void sendMessage(@NotNull String... messages);

    void sendMessage(@NotNull Iterable<String> messages);

    void sendMessage(@NotNull Iterable<String> messages, @Nullable Map<String, String> placeholders);

    void sendActionBar(@NotNull Component message);

    void sendActionBar(@NotNull String message);

    void sendActionBar(@NotNull String message, @Nullable Map<String, String> placeholders);

    void sendTitle(@NotNull String title, @NotNull String subtitle);

    void sendTitle(@NotNull String title, @NotNull String subtitle, @Nullable Map<String, String> placeholders);

    void sendTitle(@NotNull String title, @NotNull String subtitle, @Nullable Title.Times times);

    void sendTitle(@NotNull String title, @NotNull String subtitle, @Nullable Title.Times times, @Nullable Map<String, String> placeholders);

    interface Wrapper<T> {
        PlayerComponent unwrap(@NotNull T wrapped);

        T wrap(@NotNull PlayerComponent player);
    }
}
