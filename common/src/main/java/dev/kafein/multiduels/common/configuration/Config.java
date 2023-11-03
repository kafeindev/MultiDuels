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

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;

import java.nio.file.Path;
import java.util.Objects;

public final class Config {
    private final ConfigType type;
    private final ConfigurationNode node;
    private final @Nullable Path path;

    public Config(ConfigType type, ConfigurationNode node, @Nullable Path path) {
        this.node = node;
        this.type = type;
        this.path = path;
    }

    public ConfigType getType() {
        return this.type;
    }

    public ConfigurationNode getNode() {
        return this.node;
    }

    public @Nullable Path getPath() {
        return this.path;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Config)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Config config = (Config) obj;
        return Objects.equals(this.type, config.type)
                && Objects.equals(this.node, config.node)
                && Objects.equals(this.path, config.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.node, this.path);
    }

    @Override
    public String toString() {
        return "Config{" +
                "type=" + this.type +
                ", node=" + this.node +
                ", path=" + this.path +
                '}';
    }
}
