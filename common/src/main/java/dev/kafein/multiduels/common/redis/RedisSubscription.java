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

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import dev.kafein.multiduels.common.redis.message.MessageListener;
import dev.kafein.multiduels.common.utils.GsonProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class RedisSubscription extends JedisPubSub implements Runnable {
    private final RedisOperations operations;
    private final String channel;
    private final Map<String, MessageListener> listeners;

    private MessageListener mainListener;

    RedisSubscription(RedisOperations operations, String channel) {
        this(operations, channel, Maps.newConcurrentMap());
    }

    RedisSubscription(RedisOperations operations, String channel, MessageListener mainListener) {
        this(operations, channel, mainListener, Maps.newConcurrentMap());
    }

    RedisSubscription(RedisOperations operations, String channel, Map<String, MessageListener> listeners) {
        this.operations = operations;
        this.channel = channel;
        this.listeners = listeners;
    }

    RedisSubscription(RedisOperations operations, String channel, MessageListener mainListener, Map<String, MessageListener> listeners) {
        this.operations = operations;
        this.channel = channel;
        this.listeners = listeners;
        this.mainListener = mainListener;
    }

    @Override
    public void run() {
        while (!this.operations.getClient().isClosed() && !this.operations.getJedisPool().isClosed() && !Thread.interrupted()) {
            try (Jedis jedis = this.operations.getJedisPool().getResource()) {
                jedis.subscribe(this, this.channel);
            } catch (JedisConnectionException e) {
                if (this.operations.getClient().isClosed()) {
                    return;
                }

                try {
                    unsubscribe();
                } catch (JedisConnectionException ignored) {
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals(this.channel)) {
            return;
        }

        JsonObject parsed = GsonProvider.getGson().fromJson(message, JsonObject.class);

        if (parsed.has("key")) {
            String key = parsed.get("key").getAsString();
            MessageListener listener = getListener(key);
            checkNotNull(listener, "Listener for key " + key + " is not registered");

            listener.onMessage(this.operations.getClient(), parsed);
        } else {
            MessageListener listener = getMainListener();
            checkNotNull(listener, "Main listener is not registered");

            listener.onMessage(this.operations.getClient(), parsed);
        }
    }

    public @Nullable MessageListener getMainListener() {
        return this.mainListener;
    }

    public void setMainListener(@Nullable MessageListener mainListener) {
        this.mainListener = mainListener;
    }

    public MessageListener getListener(@NotNull String key) {
        return this.listeners.get(key);
    }

    public boolean isRegistered(@NotNull String key) {
        return this.listeners.containsKey(key);
    }

    public void registerListener(@NotNull String key, @NotNull MessageListener listener) {
        this.listeners.put(key, listener);
    }

    public void unregisterListener(@NotNull String key) {
        this.listeners.remove(key);
    }
}
