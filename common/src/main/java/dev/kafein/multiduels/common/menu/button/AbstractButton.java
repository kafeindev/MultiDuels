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

import com.google.common.collect.Sets;
import dev.kafein.multiduels.common.components.ItemComponent;
import dev.kafein.multiduels.common.menu.action.RegisteredClickAction;
import dev.kafein.multiduels.common.menu.misc.ClickResult;
import dev.kafein.multiduels.common.menu.misc.contexts.ClickContext;
import dev.kafein.multiduels.common.menu.misc.contexts.OpenContext;
import dev.kafein.multiduels.common.utils.ArrayUtils;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

abstract class AbstractButton implements Button {
    private final ButtonProperties properties;
    private final ItemStackFactory itemStackFactory;
    private final Set<RegisteredClickAction> clickActions;
    private ClickHandler clickHandler;

    AbstractButton(ButtonProperties properties, ItemStackFactory itemStackFactory, ClickHandler clickHandler) {
        this(properties, itemStackFactory, clickHandler, Sets.newHashSet());
    }

    AbstractButton(ButtonProperties properties, ItemStackFactory itemStackFactory, ClickHandler clickHandler, Set<RegisteredClickAction>clickActions) {
        this.properties = properties;
        this.clickHandler = clickHandler;
        this.clickActions = clickActions;
        this.itemStackFactory = itemStackFactory;
    }

    @Override
    public Map<Integer, ItemComponent> createItems(@NotNull OpenContext context) {
        return this.itemStackFactory.createItemStacks(context, this);
    }

    @Override
    public ClickResult click(@NotNull ClickContext context) {
        return this.clickHandler.apply(context);
    }

    @Override
    public void setClickHandler(@NotNull ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public Set<RegisteredClickAction> getClickActions() {
        return this.clickActions;
    }

    @Override
    public void putClickActions(@NotNull Set<RegisteredClickAction> actions) {
        this.clickActions.addAll(actions);
    }

    @Override
    public void putClickAction(@NotNull RegisteredClickAction action) {
        this.clickActions.add(action);
    }

    @Override
    public void removeClickAction(@NotNull RegisteredClickAction action) {
        this.clickActions.remove(action);
    }

    @Override
    public void clearClickActions() {
        this.clickActions.clear();
    }

    @Override
    public ButtonProperties getProperties() {
        return this.properties;
    }

    @Override
    public @Nullable ConfigurationNode getNode() {
        return this.properties.getNode();
    }

    @Override
    public String getName() {
        return this.properties.getName();
    }

    @Override
    public int[] getSlots() {
        return this.properties.getSlots();
    }

    @Override
    public boolean hasSlot(int slot) {
        int[] slots = getSlots();
        return ArrayUtils.contains(slots, slot);
    }

    @Override
    public @Nullable String getClickSound() {
        return this.properties.getClickSound();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Button)) return false;

        Button button = (Button) obj;
        return getName().equals(button.getName());
    }

    abstract static class Builder<T, B extends Builder<T, B>> {
        protected ButtonProperties.Builder properties;
        protected Set<RegisteredClickAction> clickActions;
        protected ClickHandler clickHandler;

        protected Builder() {
            this.properties = ButtonProperties.newBuilder();
            this.clickActions = Sets.newHashSet();
            this.clickHandler = context -> ClickResult.CANCELLED;
        }

        @SuppressWarnings("unchecked")
        public B self() {
            return (B) this;
        }

        public ButtonProperties.Builder properties() {
            return this.properties;
        }

        public B properties(@NotNull ButtonProperties properties) {
            this.properties.copy(properties);
            return self();
        }

        public B propertiesFromNode() {
            checkNotNull(this.properties.node(), "Node is null");
            return propertiesFromNode(this.properties.node());
        }

        public B propertiesFromNode(@NotNull ConfigurationNode node) {
            this.properties.copy(ButtonProperties.fromNode(node));
            return self();
        }

        public B node(@NotNull ConfigurationNode node) {
            this.properties.node(node);
            return self();
        }

        public B name(@NotNull String name) {
            this.properties.name(name);
            return self();
        }

        public B slots(int... slots) {
            this.properties.slots(slots);
            return self();
        }

        public B clickSound(@NotNull String clickSound) {
            this.properties.clickSound(clickSound);
            return self();
        }

        public Set<RegisteredClickAction> clickActions() {
            return this.clickActions;
        }

        public B clickAction(@NotNull Iterable<RegisteredClickAction> actions) {
            this.clickActions.addAll(Sets.newHashSet(actions));
            return self();
        }

        public B clickAction(@NotNull RegisteredClickAction... action) {
            this.clickActions.addAll(Arrays.asList(action));
            return self();
        }

        public B clickActionsFromNode() {
            checkNotNull(this.properties.node(), "Node is null");
            return clickActionsFromNode(this.properties.node());
        }

        public B clickActionsFromNode(@NotNull ConfigurationNode node) {
            ConfigurationNode actionsNode = node.getNode("actions");
            if (!actionsNode.isEmpty()) {
                List<String> actions = actionsNode.getList(Object::toString);
                actions.forEach(keyValue -> {
                    RegisteredClickAction action = RegisteredClickAction.of(keyValue);
                    this.clickActions.add(action);
                });
            }

            return self();
        }

        public ClickHandler clickHandler() {
            return this.clickHandler;
        }

        public B whenClicked(@NotNull ClickHandler clickHandler) {
            this.clickHandler = clickHandler;
            return self();
        }

        public abstract @NotNull T build();
    }
}
