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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import redis.clients.jedis.HostAndPort;

public final class RedisCredentials {
    private final HostAndPort hostAndPort;
    private final @Nullable String username;
    private final @Nullable String password;
    private final boolean useSsl;

    public RedisCredentials(HostAndPort hostAndPort, final @Nullable String username, final @Nullable String password, boolean useSsl) {
        this.hostAndPort = hostAndPort;
        this.username = username;
        this.password = password;
        this.useSsl = useSsl;
    }

    @NotNull
    public static RedisCredentials empty() {
        return new RedisCredentials(HostAndPort.from("localhost:6379"), null, null, false);
    }

    @NotNull
    public static RedisCredentials fromNode(@NotNull ConfigurationNode node) {
        return new RedisCredentials(
                HostAndPort.from(node.node("host").getString()),
                node.node("username").getString(),
                node.node("password").getString(),
                node.node("use-ssl").getBoolean()
        );
    }

    public HostAndPort getHostAndPort() {
        return this.hostAndPort;
    }

    public @Nullable String getUsername() {
        return this.username;
    }

    public @Nullable String getPassword() {
        return this.password;
    }

    public boolean useSsl() {
        return this.useSsl;
    }
}