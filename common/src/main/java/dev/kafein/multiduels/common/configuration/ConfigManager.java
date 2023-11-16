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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.reflect.TypeToken;
import dev.kafein.multiduels.common.configuration.serializers.RedisCredentialsSerializer;
import dev.kafein.multiduels.common.redis.RedisCredentials;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public final class ConfigManager {
    public static final ConfigurationOptions DEFAULT_OPTIONS = ConfigurationOptions.defaults()
            .withSerializers(collection -> {
                collection.register(TypeToken.of(RedisCredentials.class), RedisCredentialsSerializer.INSTANCE);
            });

    private final Cache<String, Config> configs;
    private final Path dataFolder;
    private final KeyInjector keyInjector;

    public ConfigManager(Path dataFolder) {
        this.configs = CacheBuilder.newBuilder()
                .build();
        this.dataFolder = dataFolder;
        this.keyInjector = new KeyInjector();
    }

    public Cache<String, Config> getConfigs() {
        return this.configs;
    }

    public @Nullable Config get(@NotNull String name) {
        return this.configs.getIfPresent(name);
    }

    public Optional<Config> find(@NotNull String name) {
        return Optional.ofNullable(this.configs.getIfPresent(name));
    }

    public Optional<Config> find(@NotNull Path path) {
        return this.configs.asMap().values().stream()
                .filter(config -> Objects.nonNull(config.getPath()))
                .filter(config -> config.getPath().equals(path))
                .findFirst();
    }

    public Config register(@NotNull String name, @NotNull Config config) {
        this.configs.put(name, config);
        return config;
    }

    public Config register(@NotNull String name, @NotNull Path path) {
        Config config = ConfigBuilder.builder(path)
                .options(DEFAULT_OPTIONS)
                .build();
        return register(name, config);
    }

    public Config register(@NotNull String name, @NotNull Path path, @NotNull InputStream stream) {
        Config config = ConfigBuilder.builder(path)
                .options(DEFAULT_OPTIONS)
                .resource(stream)
                .build();
        return register(name, config);
    }

    public Config register(@NotNull String name, @NotNull Path path, @NotNull Class<?> clazz, @NotNull String resource) {
        Config config = ConfigBuilder.builder(path)
                .options(DEFAULT_OPTIONS)
                .resource(clazz, resource)
                .build();
        return register(name, config);
    }

    public Config register(@NotNull String name, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(DEFAULT_OPTIONS)
                .build();
        return register(name, config);
    }

    public Config register(@NotNull String name, @NotNull InputStream stream, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(DEFAULT_OPTIONS)
                .resource(stream)
                .build();
        return register(name, config);
    }

    public Config register(@NotNull String name, @NotNull Class<?> clazz, @NotNull String resource, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(DEFAULT_OPTIONS)
                .resource(clazz, resource)
                .build();
        return register(name, config);
    }

    public void injectKeys(@NotNull Class<?> clazz, @NotNull Config config) {
        this.keyInjector.inject(clazz, config);
    }

    public void injectKeys(@NotNull Class<?> clazz, @NotNull String name) {
        Config config = this.configs.getIfPresent(name);
        if (config != null) {
            injectKeys(clazz, config);
        }
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
        return DEFAULT_OPTIONS;
    }
}
