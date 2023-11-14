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

import dev.kafein.multiduels.common.utils.file.FileCreator;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ConfigBuilder {
    private final Path path;

    private ConfigType type = ConfigType.YAML;
    private ConfigurationOptions options = ConfigurationOptions.defaults();
    private InputStream resource;

    public ConfigBuilder(Path path) {
        this.path = path;
    }

    public ConfigBuilder(String... path) {
        this.path = Paths.get(String.join("/", path));
    }

    public ConfigBuilder(Path dataFolder, String... path) {
        this.path = dataFolder.resolve(String.join("/", path));
    }

    public ConfigBuilder(File file) {
        this.path = file.toPath();
    }

    public static ConfigBuilder builder(@NotNull Path path) {
        return new ConfigBuilder(path);
    }

    public static ConfigBuilder builder(@NotNull String... path) {
        return new ConfigBuilder(path);
    }

    public static ConfigBuilder builder(@NotNull Path dataFolder, @NotNull String... path) {
        return new ConfigBuilder(dataFolder, path);
    }

    public static ConfigBuilder builder(@NotNull File file) {
        return new ConfigBuilder(file);
    }

    public Path path() {
        return this.path;
    }

    public ConfigType type() {
        return this.type;
    }

    public ConfigBuilder type(ConfigType type) {
        this.type = type;
        return this;
    }

    public ConfigurationOptions options() {
        return this.options;
    }

    public ConfigBuilder options(@NotNull ConfigurationOptions options) {
        this.options = options;
        return this;
    }

    public InputStream resource() {
        return this.resource;
    }

    public ConfigBuilder resource(@NotNull Class<?> clazz, @NotNull String resource) {
        this.resource = clazz.getResourceAsStream(resource);
        return this;
    }

    public ConfigBuilder resource(@NotNull InputStream inputStream) {
        this.resource = inputStream;
        return this;
    }

    public @UnknownNullability Config build() {
        FileCreator fileCreator = FileCreator.of(this.path);
        try {
            if (this.resource != null) {
                fileCreator.createAndInject(this.resource);
            } else {
                fileCreator.create();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create file", e);
        }

        try {
            ConfigurationLoader<?> nodeLoader = createLoader();
            return new Config(this.type, nodeLoader.load(), this.path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config", e);
        }
    }

    private ConfigurationLoader<?> createLoader() {
        switch (this.type) {
            case JSON:
                return GsonConfigurationLoader.builder()
                        .setPath(this.path)
                        .setDefaultOptions(this.options)
                        .build();
            case YAML:
                return YAMLConfigurationLoader.builder()
                        .setPath(this.path)
                        .setDefaultOptions(this.options)
                        .build();
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
