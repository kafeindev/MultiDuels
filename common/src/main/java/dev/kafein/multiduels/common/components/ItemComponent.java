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

import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemComponent {
    String getMaterial();

    int getAmount();

    String getName();

    List<String> getLore();

    Builder toBuilder();

    interface Builder {
        Builder amount(int amount);

        Builder name(@NotNull String name);

        Builder name(@NotNull String name, @NotNull Map<String, String> placeholders);

        Builder name(@NotNull ConfigurationNode node);

        Builder name(@NotNull ConfigurationNode node, @NotNull Map<String, String> placeholders);

        Builder lore(@NotNull String... lore);

        Builder lore(@NotNull Iterable<String> lore);

        Builder lore(@NotNull Iterable<String> lore, @NotNull Map<String, String> placeholders);

        Builder lore(@NotNull ConfigurationNode node);

        Builder lore(@NotNull ConfigurationNode node, @NotNull Map<String, String> placeholders);

        Builder enchantments(@NotNull Map<String, Integer> enchantments);

        Builder enchantments(@NotNull ConfigurationNode node);

        Builder enchantment(@NotNull String enchantment, int level);

        Builder flags(@NotNull Set<String> flags);

        Builder flags(@NotNull ConfigurationNode node);

        Builder flag(@NotNull String flag);

        Builder skullOwner(@NotNull String skullOwner);

        Builder skullOwner(@NotNull ConfigurationNode node);

        Builder headTexture(@NotNull String headTexture);

        Builder headTexture(@NotNull ConfigurationNode node);

        ItemComponent build();
    }

    interface Wrapper<T> {
        ItemComponent unwrap(@NotNull T wrapped);

        T wrap(@NotNull ItemComponent item);
    }

    interface Factory {
        ItemComponent create(@NotNull String material);

        ItemComponent create(@NotNull String material, int amount);

        ItemComponent create(@NotNull ConfigurationNode node);

        ItemComponent create(@NotNull ConfigurationNode node, @NotNull Map<String, String> placeholders);
    }
}
