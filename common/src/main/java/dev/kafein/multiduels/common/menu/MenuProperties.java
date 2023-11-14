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

import dev.kafein.multiduels.common.components.InventoryComponent;
import dev.kafein.multiduels.common.configuration.Config;
import dev.kafein.multiduels.common.configuration.ConfigBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class MenuProperties {
    private final @Nullable ConfigurationNode node;

    private final String name;
    private final InventoryComponent.Properties inventoryProperties;

    //private long cooldown;

    private String openSound;
    private String closeSound;

    public MenuProperties(@Nullable ConfigurationNode node, String name, InventoryComponent.Properties inventoryProperties, String openSound, String closeSound) {
        this.node = node;
        this.name = name;
        this.inventoryProperties = inventoryProperties;
        this.openSound = openSound;
        this.closeSound = closeSound;
    }

    public static MenuProperties fromConfig(@NotNull String name, @NotNull Config config) {
        return fromNode(name, config.getNode().getNode("menu"));
    }

    public static MenuProperties fromConfig(@NotNull String name, @NotNull Class<?> clazz, @NotNull String resource, @NotNull String... path) {
        Config config = ConfigBuilder.builder(path)
                .resource(clazz, resource)
                .build();
        return fromNode(name, config.getNode().getNode("menu"));
    }

    public static MenuProperties fromNode(@NotNull String name, @NotNull ConfigurationNode node) {
        return new Builder()
                .node(node)
                .name(name)
                .title(node.getNode("title").getString("NONE"))
                .size(node.getNode("size").getInt(9))
                .openSound(node.getNode("open-sound").getString())
                .closeSound(node.getNode("close-sound").getString())
                .build();
    }

    public @Nullable ConfigurationNode getNode() {
        return this.node;
    }

    public String getName() {
        return this.name;
    }

    public InventoryComponent.Properties getInventoryProperties() {
        return this.inventoryProperties;
    }

    public String getTitle() {
        return this.inventoryProperties.getTitle();
    }

    public int getSize() {
        return this.inventoryProperties.getSize();
    }

    public @Nullable String getOpenSound() {
        return this.openSound;
    }

    public void setOpenSound(@Nullable String openSound) {
        this.openSound = openSound;
    }

    public @Nullable String getCloseSound() {
        return this.closeSound;
    }

    public void setCloseSound(@Nullable String closeSound) {
        this.closeSound = closeSound;
    }

    Builder toBuilder() {
        return new Builder()
                .node(this.node)
                .name(this.name)
                .title(this.inventoryProperties.getTitle())
                .size(this.inventoryProperties.getSize())
                .openSound(this.openSound)
                .closeSound(this.closeSound);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private ConfigurationNode node;

        private String name;
        private String title;
        private int size;

        private String openSound;
        private String closeSound;

        private Builder() {
        }

        public ConfigurationNode node() {
            return checkNotNull(this.node, "node");
        }

        public Builder node(@Nullable ConfigurationNode node) {
            this.node = node;
            return this;
        }

        public @Nullable String name() {
            return this.name;
        }

        public Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public @Nullable String title() {
            return this.title;
        }

        public Builder title(@NotNull String title) {
            this.title = title;
            return this;
        }

        public int size() {
            return this.size;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public @Nullable String openSound() {
            return this.openSound;
        }

        public Builder openSound(@Nullable String openSound) {
            this.openSound = openSound;
            return this;
        }

        public @Nullable String closeSound() {
            return this.closeSound;
        }

        public Builder closeSound(@Nullable String closeSound) {
            this.closeSound = closeSound;
            return this;
        }

        public MenuProperties build() {
            checkNotNull(this.name, "name");
            checkNotNull(this.title, "title");
            checkArgument(this.size > 0, "size must be positive");

            InventoryComponent.Properties inventoryProperties = InventoryComponent.Properties.create(this.title, this.size);
            return new MenuProperties(this.node, this.name, inventoryProperties, this.openSound, this.closeSound);
        }
    }
}
