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

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.kafein.multiduels.common.redis.codec.Decoder;
import dev.kafein.multiduels.common.redis.codec.DecoderCollection;
import dev.kafein.multiduels.common.redis.message.Message;
import dev.kafein.multiduels.common.redis.message.MessageListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class RedisClient {
    private final RedisCredentials credentials;
    private final RedisCache cache;
    private final RedisOperations operations;
    private final DecoderCollection decoders;
    private final ExecutorService executorService;

    private JedisPool jedisPool;
    private boolean closed;

    public RedisClient(RedisCredentials credentials) {
        this.credentials = credentials;
        this.cache = new RedisCache(this);
        this.operations = new RedisOperations(this);
        this.decoders = new DecoderCollection();
        this.executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat("multiduels-redis-%d")
                .setDaemon(true)
                .build());
    }

    public RedisCredentials getCredentials() {
        return this.credentials;
    }

    public void connect() {
        this.jedisPool = RedisConnector.connect(this.credentials);
    }

    public void disconnect() {
        this.closed = true;
        this.operations.unsubscribeAll();
        this.jedisPool.close();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public RedisCache getCache() {
        return this.cache;
    }

    public RedisOperations getOperations() {
        return this.operations;
    }

    public <T> void publish(@NotNull String channel, @NotNull Message message) {
        this.operations.publish(channel, message);
    }

    public <T> void publish(@NotNull String channel, @NotNull T message) {
        this.operations.publish(channel, null, message);
    }

    public <T> void publish(@NotNull String channel, @Nullable String key, @NotNull T message) {
        this.operations.publish(channel, key, message);
    }

    public <T> void publish(@NotNull String channel, @NotNull String message) {
        this.operations.publish(channel, null, message);
    }

    public void publish(@NotNull String channel, @Nullable String key, @NotNull String message) {
        this.operations.publish(channel, key, message);
    }

    public void subscribe(@NotNull String channel) {
        this.operations.subscribe(this.executorService, channel);
    }

    public void subscribe(@NotNull ExecutorService executorService, @NotNull String channel) {
        this.operations.subscribe(executorService, channel);
    }

    public void subscribe(@NotNull String channel, @NotNull Map<String, MessageListener> listeners) {
        this.operations.subscribe(this.executorService, channel, listeners);
    }

    public void subscribe(@NotNull ExecutorService executorService, @NotNull String channel, @NotNull Map<String, MessageListener> listeners) {
        this.operations.subscribe(executorService, channel, listeners);
    }

    public void unsubscribe(@NotNull String channel) {
        this.operations.unsubscribe(channel);
    }

    public boolean isSubscribed(@NotNull String channel) {
        return this.operations.isSubscribed(channel);
    }

    public void registerListener(@NotNull String channel, @NotNull String key, @NotNull MessageListener listener) {
        this.operations.registerListener(channel, key, listener);
    }

    public void unregisterListener(@NotNull String channel, @NotNull String key) {
        this.operations.unregisterListener(channel, key);
    }

    public DecoderCollection getDecoders() {
        return this.decoders;
    }

    public <T> void registerDecoder(@NotNull Class<T> type, @NotNull Decoder<T> decoder) {
        this.decoders.registerDecoder(type, decoder);
    }

    public <T> void unregisterDecoder(@NotNull Class<?> type) {
        this.decoders.unregisterDecoder(type);
    }

    public <T> Decoder<T> getDecoder(@NotNull Class<?> type) {
        return this.decoders.getDecoder(type);
    }
}
