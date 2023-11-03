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

import com.google.gson.JsonObject;
import dev.kafein.multiduels.common.utils.GsonProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;

import static com.google.common.base.Preconditions.checkNotNull;

final class RedisPublisher {
    private final RedisOperations pubSub;
    private final String channel;

    RedisPublisher(RedisOperations pubSub, String channel) {
        this.pubSub = pubSub;
        this.channel = channel;
    }

    void publish(@Nullable String key, @NotNull String message) {
        JsonObject json = GsonProvider.getGson().fromJson(message, JsonObject.class);
        checkNotNull(json, "Message is not a valid JSON object");

        if (key != null && json.get("key") == null) {
            json.addProperty("key", key);
        }

        try (Jedis jedis = this.pubSub.getJedisPool().getResource()) {
            jedis.publish(this.channel, json.toString());
        }
    }
}
