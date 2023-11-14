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
import dev.kafein.multiduels.common.utils.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

final class PaginatedButton extends AbstractButton implements Button {
    PaginatedButton(ButtonProperties properties, ItemStackFactory itemStackFactory, ClickHandler clickHandler) {
        super(properties, itemStackFactory, clickHandler);
    }

    PaginatedButton(ButtonProperties properties, ItemStackFactory itemStackFactory, ClickHandler clickHandler, Set<RegisteredClickAction> clickActions) {
        super(properties, itemStackFactory, clickHandler, clickActions);
    }

    public static <T> PaginatedButton.Builder<T> newBuilder() {
        return new Builder<>();
    }

    @Override
    public ButtonType getType() {
        return ButtonType.PAGINATED;
    }

    public static final class Builder<T> extends AbstractButton.Builder<PaginatedButton, Builder<T>> {
        private final @NotNull PaginatedItemStackFactoryBuilder<T> itemStackFactoryBuilder;

        Builder() {
            super();
            this.itemStackFactoryBuilder = new PaginatedItemStackFactoryBuilder<>();
        }

        public PaginatedButton.Builder<T> entries(@NotNull Function<OpenContext, List<T>> entries) {
            this.itemStackFactoryBuilder.entries(entries);
            return this;
        }

        public PaginatedButton.Builder<T> itemFactory(@NotNull BiFunction<OpenContext, T, ItemComponent> itemFactory) {
            this.itemStackFactoryBuilder.itemFactory(itemFactory);
            return this;
        }

        public PaginatedButton.Builder<T> itemModifier(@NotNull TriConsumer<OpenContext, ItemComponent, T> modifier) {
            this.itemStackFactoryBuilder.modifier(modifier);
            return this;
        }

        public PaginatedButton.Builder<T> placeholdersPerEntry(@NotNull BiFunction<OpenContext, T, Map<String, String>> placeholdersPerEntry) {
            this.itemStackFactoryBuilder.placeholdersPerEntry(placeholdersPerEntry);
            return this;
        }

        public PaginatedButton.Builder<T> itemPlaceholders(@NotNull Function<OpenContext, Map<String, String>> placeholders) {
            this.itemStackFactoryBuilder.placeholders(placeholders);
            return this;
        }

        public PaginatedButton.Builder<T> itemPlaceholders(@NotNull Map<String, String> placeholders) {
            return itemPlaceholders(context -> placeholders);
        }


        @Override
        public PaginatedButton build() {
            return new PaginatedButton(properties().build(), this.itemStackFactoryBuilder.build(), clickHandler(), clickActions());
        }
    }

    static final class PaginatedItemStackFactoryBuilder<T> extends ItemStackFactory.Builder<PaginatedItemStackFactoryBuilder<T>> {
        private Function<OpenContext, List<T>> entries;
        private BiFunction<OpenContext, T, Map<String, String>> placeholdersPerEntry;
        private BiFunction<OpenContext, T, ItemComponent> itemFactory;
        private TriConsumer<OpenContext, ItemComponent, T> itemModifier;

        private PaginatedItemStackFactoryBuilder() {
            super();
        }

        public Function<OpenContext, List<T>> entries() {
            return this.entries;
        }

        public PaginatedItemStackFactoryBuilder<T> entries(@NotNull Function<OpenContext, List<T>> entries) {
            this.entries = entries;
            return this;
        }

        public BiFunction<OpenContext, T, Map<String, String>> placeholdersPerEntry() {
            return this.placeholdersPerEntry;
        }

        public PaginatedItemStackFactoryBuilder<T> placeholdersPerEntry(@NotNull BiFunction<OpenContext, T, Map<String, String>> placeholdersPerEntry) {
            this.placeholdersPerEntry = placeholdersPerEntry;
            return this;
        }

        public BiFunction<OpenContext, T, ItemComponent> itemFactory() {
            return this.itemFactory;
        }

        public PaginatedItemStackFactoryBuilder<T> itemFactory(@NotNull BiFunction<OpenContext, T, ItemComponent> itemFactory) {
            this.itemFactory = itemFactory;
            return this;
        }

        public TriConsumer<OpenContext, ItemComponent, T> modifier() {
            return this.itemModifier;
        }

        public PaginatedItemStackFactoryBuilder<T> modifier(@NotNull TriConsumer<OpenContext, ItemComponent, T> modifier) {
            this.itemModifier = modifier;
            return this;
        }

        @Override
        ItemStackFactory build() {
            return (context, button) -> {
                checkNotNull(this.entries, "entries");
                checkArgument(this.itemFactory != null || button.getNode() != null, "Either itemFactory or node must be set");

                List<T> entries = this.entries.apply(context);
                int slotCount = button.getSlots().length;

                int minIndex = context.getPage() * slotCount;
                if (minIndex >= entries.size()) {
                    return Maps.newHashMap();
                }

                Map<Integer, ItemComponent> itemStacks = Maps.newHashMap();
                for (int i = 0; i < slotCount; i++) {
                    int position = minIndex + i;
                    if (position >= entries.size()) {
                        break;
                    }

                    T entry = entries.get(position);
                    Map<String, String> placeholders = this.placeholdersPerEntry != null
                            ? this.placeholdersPerEntry.apply(context, entry)
                            : this.placeholders.apply(context);

                    ItemComponent itemComponent = this.itemFactory != null
                            ? this.itemFactory.apply(context, entry)
                            : context.getMenu().getItemFactory().create(button.getNode(), placeholders);
                    if (this.itemModifier != null) {
                        this.itemModifier.accept(context, itemComponent, entry);
                    }

                    itemStacks.put(button.getSlots()[i], itemComponent);
                }

                return itemStacks;
            };
        }
    }
}
