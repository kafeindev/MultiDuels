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

package dev.kafein.multiduels.common.configuration;

import com.google.common.reflect.TypeToken;
import dev.kafein.multiduels.common.configuration.serializers.RedisCredentialsSerializer;
import dev.kafein.multiduels.common.manager.AbstractManager;
import dev.kafein.multiduels.common.redis.RedisCredentials;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;

public final class ConfigManager extends AbstractManager<String, Config> {
    private final Path dataFolder;
    private final KeyInjector keyInjector;
    private final ConfigurationOptions options;

    public ConfigManager(Path dataFolder) {
        this.dataFolder = dataFolder;
        this.keyInjector = new KeyInjector();
        this.options = ConfigurationOptions.defaults()
                .withSerializers(collection -> {
                    collection.register(TypeToken.of(RedisCredentials.class), RedisCredentialsSerializer.INSTANCE);
                });
    }

    public Config register(@NotNull String name, @NotNull Config config) {
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull Path path) {
        Config config = ConfigBuilder.builder(path)
                .options(this.options)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull Path path, @NotNull InputStream stream) {
        Config config = ConfigBuilder.builder(path)
                .options(this.options)
                .resource(stream)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull Path path, @NotNull Class<?> clazz, @NotNull String resource) {
        Config config = ConfigBuilder.builder(path)
                .options(this.options)
                .resource(clazz, resource)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(this.options)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull InputStream stream, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(this.options)
                .resource(stream)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull Class<?> clazz, @NotNull String resource, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(this.options)
                .resource(clazz, resource)
                .build();
        return put(name, config);
    }

    public void injectKeys(@NotNull Class<?> clazz, @NotNull Config config) {
        this.keyInjector.inject(clazz, config);
    }

    public void injectKeys(@NotNull Class<?> clazz, @NotNull String name) {
        this.keyInjector.inject(clazz, get(name));
    }

    public void injectKeys(@NotNull Class<?> clazz, @NotNull ConfigurationNode node) {
        this.keyInjector.inject(clazz, node);
    }

    public void injectKeys(@NotNull Field field, @NotNull Config config) {
        this.keyInjector.inject(field, config);
    }

    public void injectKeys(@NotNull Field field, @NotNull ConfigurationNode node) {
        this.keyInjector.inject(field, node);
    }

    public Path getDataFolder() {
        return this.dataFolder;
    }

    public ConfigurationOptions getOptions() {
        return this.options;
    }
}
