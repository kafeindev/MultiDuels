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
import dev.kafein.multiduels.common.redis.codec.Decoder;
import dev.kafein.multiduels.common.redis.message.Message;
import dev.kafein.multiduels.common.redis.message.MessageListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

public final class RedisOperations {
    private final RedisClient client;
    private final Map<String, RedisSubscription> subscriptions;

    public RedisOperations(RedisClient client) {
        this(client, Maps.newConcurrentMap());
    }

    public RedisOperations(RedisClient client, Map<String, RedisSubscription> subscriptions) {
        this.client = client;
        this.subscriptions = subscriptions;
    }

    public <T> void publish(@NotNull String channel, @NotNull Message message) {
        publish(channel, message.getKey(), message.getPayload());
    }

    public <T> void publish(@NotNull String channel, @NotNull T message) {
        publish(channel, null, message);
    }

    public <T> void publish(@NotNull String channel, @Nullable String key, @NotNull T message) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot publish message to channel " + channel + " as it is not subscribed to");

        Decoder<T> decoder = this.client.getDecoder(message.getClass());
        checkNotNull(decoder, "Decoder for " + message.getClass().getName() + " is not registered");

        publish(channel, key, decoder.encode(message));
    }

    public <T> void publish(@NotNull String channel,
                            @NotNull String message) {
        publish(channel, null, message);
    }

    public void publish(@NotNull String channel, @Nullable String key, @NotNull String message) {
        RedisPublisher publisher = new RedisPublisher(this, channel);
        publisher.publish(key, message);
    }

    public Map<String, RedisSubscription> getSubscriptions() {
        return this.subscriptions;
    }

    public boolean isSubscribed(@NotNull String channel) {
        return this.subscriptions.containsKey(channel);
    }

    public void subscribe(@NotNull ExecutorService executor, @NotNull String channel) {
        RedisSubscription subscription = new RedisSubscription(this, channel);
        CompletableFuture.runAsync(subscription, executor);

        this.subscriptions.put(channel, subscription);
    }

    public void subscribe(@NotNull ExecutorService executor, @NotNull String channel, @NotNull MessageListener mainListener) {
        RedisSubscription subscription = new RedisSubscription(this, channel, mainListener);
        CompletableFuture.runAsync(subscription, executor);

        this.subscriptions.put(channel, subscription);
    }

    public void subscribe(@NotNull ExecutorService executor, @NotNull String channel, @NotNull Map<String, MessageListener> listeners) {
        RedisSubscription subscription = new RedisSubscription(this, channel, listeners);
        CompletableFuture.runAsync(subscription, executor);

        this.subscriptions.put(channel, subscription);
    }

    public void subscribe(@NotNull ExecutorService executor, @NotNull String channel, @NotNull MessageListener mainListener, @NotNull Map<String, MessageListener> listeners) {
        RedisSubscription subscription = new RedisSubscription(this, channel, mainListener, listeners);
        CompletableFuture.runAsync(subscription, executor);

        this.subscriptions.put(channel, subscription);
    }

    public void unsubscribe(@NotNull String channel) {
        this.subscriptions.remove(channel).unsubscribe();
    }

    public void unsubscribeAll() {
        this.subscriptions.values().forEach(RedisSubscription::unsubscribe);
        this.subscriptions.clear();
    }

    public void registerListener(@NotNull String channel, @NotNull MessageListener listener) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot register listener for channel " + channel + " as it is not subscribed to");

        subscription.setMainListener(listener);
    }

    public void registerListener(@NotNull String channel, @NotNull String key, @NotNull MessageListener listener) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot register listener for channel " + channel + " as it is not subscribed to");

        subscription.registerListener(key, listener);
    }

    public void unregisterListener(@NotNull String channel) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot unregister listener for channel " + channel + " as it is not subscribed to");

        subscription.setMainListener(null);
    }

    public void unregisterListener(@NotNull String channel, @NotNull String key) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot unregister listener for channel " + channel + " as it is not subscribed to");

        subscription.unregisterListener(key);
    }

    @NotNull RedisClient getClient() {
        return this.client;
    }

    @NotNull JedisPool getJedisPool() {
        return this.client.getJedisPool();
    }
}
