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

package dev.kafein.multiduels.common.redis.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Message {
    private final String channel;
    private final @Nullable String key;
    private final String payload;

    public Message(String channel, @Nullable String key, String payload) {
        this.channel = channel;
        this.key = key;
        this.payload = payload;
    }

    @NotNull
    public static Message create(@NotNull String channel, @NotNull String payload) {
        return new Message(channel, null, payload);
    }

    @NotNull
    public static Message create(@NotNull String channel, @Nullable String key, @NotNull String payload) {
        return new Message(channel, key, payload);
    }

    public String getChannel() {
        return this.channel;
    }

    public @Nullable String getKey() {
        return this.key;
    }

    public String getPayload() {
        return this.payload;
    }
}
