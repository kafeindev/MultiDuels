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

package dev.kafein.multiduels.common.redis.codec;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public final class DecoderCollection {
    private static final DecoderCollection DEFAULTS;

    static {
        DEFAULTS = DecoderCollection.newBuilder()
                .build();
    }

    private final Map<Class<?>, Decoder<?>> decoders;

    public DecoderCollection() {
        this(DecoderCollection.DEFAULTS);
    }

    public DecoderCollection(DecoderCollection collection) {
        this(collection.getDecoders());
    }

    public DecoderCollection(Map<Class<?>, Decoder<?>> decoders) {
        this.decoders = decoders;
    }

    public Map<Class<?>, Decoder<?>> getDecoders() {
        return this.decoders;
    }

    public Set<Class<?>> getKeys() {
        return this.decoders.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T> Decoder<T> getDecoder(@NotNull Class<?> type) {
        return (Decoder<T>) this.decoders.get(type);
    }

    public boolean isRegistered(@NotNull Class<?> type) {
        return this.decoders.containsKey(type);
    }

    public <T> void registerDecoder(@NotNull Class<T> type, @NotNull Decoder<T> decoder) {
        this.decoders.put(type, decoder);
    }

    public void unregisterDecoder(@NotNull Class<?> type) {
        this.decoders.remove(type);
    }

    @NotNull
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<Class<?>, Decoder<?>> decoders;

        private Builder() {
            this.decoders = Maps.newHashMap();
        }

        public Builder registerDecoder(@NotNull Class<?> type, @NotNull Decoder<?> decoder) {
            this.decoders.put(type, decoder);
            return this;
        }

        public Builder unregisterDecoder(@NotNull Class<?> type) {
            this.decoders.remove(type);
            return this;
        }

        public DecoderCollection build() {
            return new DecoderCollection(this.decoders);
        }
    }
}
