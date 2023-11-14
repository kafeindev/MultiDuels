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

package dev.kafein.multiduels.common.menu.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class RegisteredClickAction {
    private final String key;
    private final @Nullable String value;

    public RegisteredClickAction(String key, @Nullable String value) {
        this.key = key;
        this.value = value;
    }

    public static RegisteredClickAction of(@NotNull String keyValue) {
        String key = keyValue.substring(1, keyValue.indexOf("]")).toUpperCase(Locale.ENGLISH);
        String value = keyValue.substring(keyValue.indexOf("]") + 2);
        return new RegisteredClickAction(key, value);
    }

    public static RegisteredClickAction of(String key, @Nullable String value) {
        return new RegisteredClickAction(key, value);
    }

    public String getKey() {
        return this.key;
    }

    public @Nullable String getValue() {
        return this.value;
    }
}
