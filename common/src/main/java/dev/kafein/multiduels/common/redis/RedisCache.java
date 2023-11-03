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

package dev.kafein.multiduels.common.redis;

import dev.kafein.multiduels.common.redis.codec.Decoder;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

import java.time.Duration;

import static com.google.common.base.Preconditions.checkNotNull;

public final class RedisCache {
    private final RedisClient client;

    public RedisCache(RedisClient client) {
        this.client = client;
    }

    public boolean exists(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.exists(key);
        }
    }

    public String get(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.get(key);
        }
    }

    public <T> T getDecoded(@NotNull String key, @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        return getDecoded(key, decoder);
    }

    public <T> T getDecoded(@NotNull String key, @NotNull Decoder<T> decoder) {
        String value = get(key);
        if (value == null) {
            return null;
        }

        return decoder.decode(value);
    }

    public void set(@NotNull String key, @NotNull String value) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.set(key, value);
        }
    }

    public void set(@NotNull String key, @NotNull String value, @NotNull Duration duration) {
        set(key, value, duration.toMillis());
    }

    public void set(@NotNull String key, @NotNull String value, long duration) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.psetex(key, duration, value);
        }
    }

    public <T> void setDecoded(@NotNull String key, @NotNull T value, @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        setDecoded(key, value, decoder);
    }

    public <T> void setDecoded(@NotNull String key, @NotNull T value, @NotNull Decoder<T> decoder) {
        set(key, decoder.encode(value));
    }

    public <T> void setDecoded(@NotNull String key, @NotNull T value, @NotNull Decoder<T> decoder, @NotNull Duration duration) {
        setDecoded(key, value, decoder, duration.toMillis());
    }

    public <T> void setDecoded(@NotNull String key, @NotNull T value, @NotNull Decoder<T> decoder, long duration) {
        set(key, decoder.encode(value), duration);
    }

    public void expire(@NotNull String key, @NotNull Duration duration) {
        expire(key, duration.toMillis());
    }

    public void expire(@NotNull String key, long duration) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.pexpire(key, duration);
        }
    }

    public void delete(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.del(key);
        }
    }
}
