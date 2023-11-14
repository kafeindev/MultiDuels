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
import dev.kafein.multiduels.common.components.LocationComponent;
import dev.kafein.multiduels.common.components.PlayerComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public final class BukkitPlayerComponent implements PlayerComponent {
    private final UUID uniqueId;
    private final String name;

    public BukkitPlayerComponent(Player player) {
        this(player.getUniqueId(), player.getName());
    }

    public BukkitPlayerComponent(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isOnline() {
        return this.getHandle() != null;
    }

    @Override
    public LocationComponent getLocation() {
        return null;
    }

    @Override
    public void teleport(@NotNull LocationComponent location) {

    }

    @Override
    public InventoryComponent.View openInventory(@NotNull InventoryComponent inventory) {
        return inventory.open(this);
    }

    @Override
    public InventoryComponent.View openInventory(@NotNull InventoryComponent.Properties properties) {
        InventoryComponent inventory = BukkitInventoryComponent.getFactory().create(properties);
        return inventory.open(this);
    }

    @Override
    public void closeInventory() {

    }

    @Override
    public void performCommand(@NotNull String command) {

    }

    @Override
    public void playSound(@NotNull String sound) {

    }

    @Override
    public void playSound(@NotNull String sound, float volume, float pitch) {

    }

    @Override
    public void hidePlayer(@NotNull PlayerComponent player) {

    }

    @Override
    public void hidePlayer(@NotNull PlayerComponent... players) {

    }

    @Override
    public void hidePlayer(@NotNull Iterable<PlayerComponent> players) {

    }

    @Override
    public void showPlayer(@NotNull PlayerComponent player) {

    }

    @Override
    public void showPlayer(@NotNull PlayerComponent... players) {

    }

    @Override
    public void showPlayer(@NotNull Iterable<PlayerComponent> players) {

    }

    @Override
    public void hideAllPlayers() {

    }

    @Override
    public void showAllPlayers() {

    }

    @Override
    public void sendMessage(@NotNull Component message) {

    }

    @Override
    public void sendMessage(@NotNull Component... messages) {

    }

    @Override
    public void sendMessage(@NotNull String message) {

    }

    @Override
    public void sendMessage(@NotNull String message, @Nullable Map<String, String> placeholders) {

    }

    @Override
    public void sendMessage(@NotNull String... messages) {

    }

    @Override
    public void sendMessage(@NotNull Iterable<String> messages) {

    }

    @Override
    public void sendMessage(@NotNull Iterable<String> messages, @Nullable Map<String, String> placeholders) {

    }

    @Override
    public void sendActionBar(@NotNull Component message) {

    }

    @Override
    public void sendActionBar(@NotNull String message) {

    }

    @Override
    public void sendActionBar(@NotNull String message, @Nullable Map<String, String> placeholders) {

    }

    @Override
    public void sendTitle(@NotNull String title, @NotNull String subtitle) {

    }

    @Override
    public void sendTitle(@NotNull String title, @NotNull String subtitle, @Nullable Map<String, String> placeholders) {

    }

    @Override
    public void sendTitle(@NotNull String title, @NotNull String subtitle, Title.@Nullable Times times) {

    }

    @Override
    public void sendTitle(@NotNull String title, @NotNull String subtitle, Title.@Nullable Times times, @Nullable Map<String, String> placeholders) {

    }

    private @Nullable Player getHandle() {
        UUID uniqueId = this.getUniqueId();
        return Bukkit.getPlayer(uniqueId);
    }

    public static Wrapper<Player> getWrapper() {
        return new WrapperImpl();
    }

    private static final class WrapperImpl implements Wrapper<Player> {
        private WrapperImpl() {}

        @Override
        public PlayerComponent unwrap(@NotNull Player wrapped) {
            return new BukkitPlayerComponent(wrapped.getUniqueId(), wrapped.getName());
        }

        @Override
        public Player wrap(@NotNull PlayerComponent player) {
            return ((BukkitPlayerComponent) player).getHandle();
        }
    }
}
