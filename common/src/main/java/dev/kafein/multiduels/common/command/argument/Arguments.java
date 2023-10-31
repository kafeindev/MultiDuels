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

package dev.kafein.multiduels.common.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class Arguments {
    private final Set<Argument> arguments;

    public Arguments(final @NotNull Set<Argument> arguments) {
        this.arguments = arguments;
    }

    public @NotNull Set<Argument> getArguments() {
        return this.arguments;
    }

    public @Nullable Argument get(final int index) {
        return this.arguments.stream()
                .filter(argument -> argument.getIndex() == index)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Arguments)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Arguments other = (Arguments) obj;
        return this.arguments.equals(other.arguments);
    }

    @Override
    public int hashCode() {
        return this.arguments.hashCode();
    }

    @Override
    public String toString() {
        return "Arguments{" +
                "arguments=" + this.arguments +
                '}';
    }
}
