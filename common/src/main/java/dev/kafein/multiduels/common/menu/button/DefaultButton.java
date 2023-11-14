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

import com.google.common.collect.Maps;
import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.menu.action.RegisteredClickAction;
import dev.kafein.multiduels.common.menu.misc.contexts.OpenContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

final class DefaultButton extends AbstractButton {
    DefaultButton(ButtonProperties properties, ItemStackFactory itemStackFactory, ClickHandler clickHandler) {
        super(properties, itemStackFactory, clickHandler);
    }

    DefaultButton(ButtonProperties properties, ItemStackFactory itemStackFactory, ClickHandler clickHandler, Set<RegisteredClickAction> clickActions) {
        super(properties, itemStackFactory, clickHandler, clickActions);
    }

    public static DefaultButton.Builder newBuilder() {
        return new Builder();
    }

    @Override
    public ButtonType getType() {
        return ButtonType.DEFAULT;
    }

    public static final class Builder extends AbstractButton.Builder<DefaultButton, Builder> {
        private final DefaultItemStackFactoryBuilder itemStackFactoryBuilder;

        Builder() {
            super();
            this.itemStackFactoryBuilder = new DefaultItemStackFactoryBuilder();
        }

        public DefaultButton.Builder itemFactory(@NotNull Function<OpenContext, ItemComponent> factory) {
            this.itemStackFactoryBuilder.itemFactory(factory);
            return this;
        }

        public DefaultButton.Builder itemModifier(@NotNull BiConsumer<OpenContext, ItemComponent> modifier) {
            this.itemStackFactoryBuilder.itemModifier(modifier);
            return this;
        }

        public DefaultButton.Builder itemPlaceholders(@NotNull Function<OpenContext, Map<String, String>> placeholders) {
            this.itemStackFactoryBuilder.placeholders(placeholders);
            return this;
        }

        public DefaultButton.Builder itemPlaceholders(@NotNull Map<String, String> placeholders) {
            return itemPlaceholders(context -> placeholders);
        }

        @Override
        public DefaultButton build() {
            return new DefaultButton(properties().build(), this.itemStackFactoryBuilder.build(), clickHandler(), clickActions());
        }
    }

    static final class DefaultItemStackFactoryBuilder extends ItemStackFactory.Builder<DefaultItemStackFactoryBuilder> {
        private Function<OpenContext, ItemComponent> itemFactory;
        private BiConsumer<OpenContext, ItemComponent> itemModifier;

        DefaultItemStackFactoryBuilder() {
            super();
        }

        public @Nullable Function<OpenContext, ItemComponent> itemFactory() {
            return this.itemFactory;
        }

        public DefaultItemStackFactoryBuilder itemFactory(@NotNull Function<OpenContext, ItemComponent> factory) {
            this.itemFactory = factory;
            return this;
        }

        public @Nullable BiConsumer<OpenContext, ItemComponent> itemModifier() {
            return this.itemModifier;
        }

        public DefaultItemStackFactoryBuilder itemModifier(@NotNull BiConsumer<OpenContext, ItemComponent> modifier) {
            this.itemModifier = modifier;
            return this;
        }

        @Override
        ItemStackFactory build() {
            return (context, button) -> {
                checkArgument(this.itemFactory != null || button.getNode() != null, "Either itemFactory or node must be set");

                Map<String, String> placeholders = this.placeholders.apply(context);

                ItemComponent itemComponent = this.itemFactory != null
                        ? this.itemFactory.apply(context)
                        : context.getMenu().getItemFactory().create(button.getNode(), placeholders);
                if (this.itemModifier != null) {
                    this.itemModifier.accept(context, itemComponent);
                }

                Map<Integer, ItemComponent> itemStacks = Maps.newHashMap();
                for (int slot : button.getSlots()) {
                    itemStacks.put(slot, itemComponent);
                }

                return itemStacks;
            };
        }
    }
}
