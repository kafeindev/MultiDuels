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

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.kafein.multiduels.common.components.ItemComponent;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

import static dev.kafein.multiduels.common.utils.Strings.replace;

public final class BukkitItemComponent implements ItemComponent {
    private static final Factory FACTORY = new FactoryImpl();
    private static final Wrapper<ItemStack> WRAPPER = new WrapperImpl();

    private final ItemStack itemStack;

    public BukkitItemComponent(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public String getMaterial() {
        return this.itemStack.getType().name();
    }

    @Override
    public int getAmount() {
        return this.itemStack.getAmount();
    }

    @Override
    public String getName() {
        return this.itemStack.getItemMeta().getDisplayName();
    }

    @Override
    public List<String> getLore() {
        return this.itemStack.getItemMeta().getLore();
    }

    private ItemStack getHandle() {
        return this.itemStack;
    }

    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this.itemStack);
    }

    public static Factory getFactory() {
        return FACTORY;
    }

    public static Wrapper<ItemStack> getWrapper() {
        return WRAPPER;
    }

    public static Builder newBuilder(ItemStack itemStack) {
        return new BuilderImpl(itemStack);
    }

    private static final class BuilderImpl implements Builder {
        private final ItemStack itemStack;
        private final ItemMeta itemMeta;

        private BuilderImpl(ItemStack itemStack) {
            this.itemStack = itemStack;
            this.itemMeta = itemStack.getItemMeta();
        }

        @Override
        public Builder amount(int amount) {
            this.itemStack.setAmount(amount);
            return this;
        }

        @Override
        public Builder name(@NotNull String name) {
            this.itemMeta.setDisplayName(name);
            return this;
        }

        @Override
        public Builder name(@NotNull String name, @NotNull Map<String, String> placeholders) {
            String replaced = replace(name, placeholders);
            return name(replaced);
        }

        @Override
        public Builder name(@NotNull ConfigurationNode node) {
            if (node.getNode("name").isEmpty()) {
                return this;
            }

            return name(node.getNode("name").getString());
        }

        @Override
        public Builder name(@NotNull ConfigurationNode node, @NotNull Map<String, String> placeholders) {
            if (node.getNode("name").isEmpty()) {
                return this;
            }

            return name(node.getNode("name").getString(), placeholders);
        }

        @Override
        public Builder lore(@NotNull String... lore) {
            List<String> loreList = this.itemMeta.hasLore() ? this.itemMeta.getLore() : Lists.newArrayList();
            loreList.addAll(Lists.newArrayList(lore));

            this.itemMeta.setLore(loreList);
            return this;
        }

        @Override
        public Builder lore(@NotNull Iterable<String> lore) {
            List<String> loreList = Lists.newArrayList(lore);
            this.itemMeta.setLore(loreList);

            return this;
        }

        @Override
        public Builder lore(@NotNull Iterable<String> lore, @NotNull Map<String, String> placeholders) {
            List<String> loreList = Lists.newArrayList(lore);
            loreList.replaceAll(s -> replace(s, placeholders));

            return lore(loreList);
        }

        @Override
        public Builder lore(@NotNull ConfigurationNode node) {
            if (node.getNode("lore").isEmpty()) {
                return this;
            }

            List<String> loreList = node.getNode("lore").getList(Object::toString);
            return lore(loreList);
        }

        @Override
        public Builder lore(@NotNull ConfigurationNode node, @NotNull Map<String, String> placeholders) {
            if (node.getNode("lore").isEmpty()) {
                return this;
            }

            List<String> loreList = node.getNode("lore").getList(Object::toString);
            return lore(loreList, placeholders);
        }

        @Override
        public Builder enchantments(@NotNull Map<String, Integer> enchantments) {
            enchantments.forEach((enchantment, level) -> {
                XEnchantment xEnchantment = XEnchantment.matchXEnchantment(enchantment).orElse(XEnchantment.DAMAGE_ALL);
                this.itemMeta.addEnchant(xEnchantment.getEnchant(), level, true);
            });

            return this;
        }

        @Override
        public Builder enchantments(@NotNull ConfigurationNode node) {
            if (node.getNode("enchantments").isEmpty()) {
                return this;
            }

            Map<String, Integer> enchantmentsMap = node.getNode("enchantments").getChildrenMap().entrySet().stream()
                    .collect(HashMap::new, (map, entry) -> map.put(entry.getKey().toString(), entry.getValue().getInt()), HashMap::putAll);
            return enchantments(enchantmentsMap);
        }

        @Override
        public Builder enchantment(@NotNull String enchantment, int level) {
            XEnchantment xEnchantment = XEnchantment.matchXEnchantment(enchantment).orElse(XEnchantment.DAMAGE_ALL);
            this.itemMeta.addEnchant(xEnchantment.getEnchant(), level, true);

            return this;
        }

        @Override
        public Builder flags(@NotNull Set<String> flags) {
            flags.forEach(flag -> {
                ItemFlag itemFlag = ItemFlag.valueOf(flag);
                this.itemMeta.addItemFlags(itemFlag);
            });

            return this;
        }

        @Override
        public Builder flags(@NotNull ConfigurationNode node) {
            if (node.getNode("flags").isEmpty()) {
                return this;
            }

            List<String> flagList = node.getNode("flags").getList(Object::toString);
            return flags(ImmutableSet.copyOf(flagList));
        }

        @Override
        public Builder flag(@NotNull String flag) {
            ItemFlag itemFlag = ItemFlag.valueOf(flag);
            this.itemMeta.addItemFlags(itemFlag);

            return this;
        }

        @Override
        public Builder skullOwner(@NotNull String skullOwner) {
            SkullMeta skullMeta = (SkullMeta) this.itemMeta;
            skullMeta.setOwner(skullOwner);

            return this;
        }

        @Override
        public Builder skullOwner(@NotNull ConfigurationNode node) {
            if (node.getNode("skull-owner").isEmpty()) {
                return this;
            }

            return skullOwner(node.getNode("skull-owner").getString());
        }

        @Override
        public Builder headTexture(@NotNull String headTexture) {
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
            gameProfile.getProperties().put("textures", new Property("textures", headTexture));

            try {
                Field profileField = this.itemMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(this.itemMeta, gameProfile);

                return this;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to set head texture", e);
            }
        }

        @Override
        public Builder headTexture(@NotNull ConfigurationNode node) {
            if (node.getNode("head-texture").isEmpty()) {
                return this;
            }

            return headTexture(node.getNode("head-texture").getString());
        }

        @Override
        public ItemComponent build() {
            this.itemStack.setItemMeta(this.itemMeta);
            return new BukkitItemComponent(this.itemStack);
        }
    }

    private static final class WrapperImpl implements Wrapper<ItemStack> {
        private WrapperImpl() {
        }

        @Override
        public ItemComponent unwrap(@NotNull ItemStack item) {
            return new BukkitItemComponent(item);
        }

        @Override
        public ItemStack wrap(@NotNull ItemComponent item) {
            return ((BukkitItemComponent) item).getHandle();
        }
    }

    private static final class FactoryImpl implements Factory {
        private FactoryImpl() {
        }

        @Override
        public ItemComponent create(@NotNull String material) {
            return create(material, 1);
        }

        @Override
        public ItemComponent create(@NotNull String material, int amount) {
            XMaterial xMaterial = XMaterial.matchXMaterial(material).orElse(XMaterial.STONE);
            return new BuilderImpl(xMaterial.parseItem()).amount(amount).build();
        }

        @Override
        public ItemComponent create(@NotNull ConfigurationNode node) {
            XMaterial xMaterial = XMaterial.matchXMaterial(node.getNode("material").getString("STONE")).orElse(XMaterial.STONE);
            int amount = node.getNode("amount").getInt(1);

            return new BuilderImpl(xMaterial.parseItem())
                    .amount(amount)
                    .name(node)
                    .lore(node)
                    .enchantments(node)
                    .flags(node)
                    .skullOwner(node)
                    .headTexture(node)
                    .build();
        }

        @Override
        public ItemComponent create(@NotNull ConfigurationNode node, @NotNull Map<String, String> placeholders) {
            XMaterial xMaterial = XMaterial.matchXMaterial(node.getNode("material").getString("STONE")).orElse(XMaterial.STONE);
            int amount = node.getNode("amount").getInt(1);

            return new BuilderImpl(xMaterial.parseItem())
                    .amount(amount)
                    .name(node, placeholders)
                    .lore(node, placeholders)
                    .enchantments(node)
                    .flags(node)
                    .skullOwner(node)
                    .headTexture(node)
                    .build();
        }
    }
}