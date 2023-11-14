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

package dev.kafein.multiduels.common.menu.button;

import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ButtonProperties {
    private final @Nullable ConfigurationNode node;

    private final String name;
    private final int[] slots;

    private String clickSound;

    ButtonProperties(@Nullable ConfigurationNode node, String name, int[] slots, @Nullable String clickSound) {
        this.node = node;
        this.name = name;
        this.slots = slots;
        this.clickSound = clickSound;
    }

    public static ButtonProperties of(String name, int[] slots) {
        return new ButtonProperties(null, name, slots, null);
    }

    public static ButtonProperties of(String name, int[] slots, @Nullable String clickSound) {
        return new ButtonProperties(null, name, slots, clickSound);
    }

    public static ButtonProperties fromNode(@NotNull ConfigurationNode node) {
        return new Builder()
                .node(node)
                .name((String) node.getKey())
                .clickSound(node.getNode("click-sound").getString())
                .slots(node.getNode("slot").isEmpty() ? node.getNode("slots") : node.getNode("slot"))
                .build();
    }

    public @Nullable ConfigurationNode getNode() {
        return this.node;
    }

    public String getName() {
        return this.name;
    }

    public int[] getSlots() {
        return this.slots;
    }

    public @Nullable String getClickSound() {
        return this.clickSound;
    }

    public void setClickSound(@Nullable String clickSound) {
        this.clickSound = clickSound;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private ConfigurationNode node;

        private String name;
        private int[] slots;

        private String clickSound;

        Builder() {}

        public @Nullable ConfigurationNode node() {
            return this.node;
        }

        public Builder node(@NotNull ConfigurationNode node) {
            this.node = node;
            return this;
        }

        public Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public Builder slots(int @NotNull [] slots) {
            this.slots = slots;
            return this;
        }

        @NotNull Builder slots(@NotNull ConfigurationNode node) {
            Object slotsObject = node.getValue();
            if (slotsObject instanceof Integer) {
                this.slots = new int[]{(int) slotsObject};
            } else {
                this.slots = node.getList(obj -> Integer.parseInt(obj.toString())).stream().mapToInt(Integer::intValue).toArray();
            }

            return this;
        }

        public Builder clickSound(@NotNull String clickSound) {
            this.clickSound = clickSound;
            return this;
        }

        public Builder copy(@NotNull ButtonProperties properties) {
            this.node = properties.node;
            this.name = properties.name;
            this.slots = properties.slots;
            this.clickSound = properties.clickSound;
            return this;
        }

        public Builder copy(@NotNull Builder builder) {
            this.node = builder.node;
            this.name = builder.name;
            this.slots = builder.slots;
            this.clickSound = builder.clickSound;
            return this;
        }

        public ButtonProperties build() {
            checkNotNull(this.name, "name");
            return new ButtonProperties(this.node, this.name, this.slots, this.clickSound);
        }
    }
}
