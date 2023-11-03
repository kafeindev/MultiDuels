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

package dev.kafein.multiduels.common.utils.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileCreator {
    private final Path path;

    public FileCreator(Path path) {
        this.path = path;
    }

    @NotNull
    public static FileCreator of(@NotNull Path path) {
        return new FileCreator(path);
    }

    @NotNull
    public static FileCreator of(@NotNull String... path) {
        return new FileCreator(Paths.get(String.join("/", path)));
    }

    @NotNull
    public static FileCreator of(@NotNull Path dataFolder, @NotNull String... path) {
        return new FileCreator(dataFolder.resolve(String.join("/", path)));
    }

    @NotNull
    public static FileCreator of(@NotNull File file) {
        return new FileCreator(file.toPath());
    }

    public boolean create() throws IOException {
        Files.createDirectories(this.path.getParent());

        if (!Files.exists(this.path)) {
            Files.createFile(this.path);
            return true;
        }

        return false;
    }

    public boolean createAndInject(@NotNull Class<?> clazz, @NotNull String resource) throws IOException {
        InputStream inputStream = clazz.getResourceAsStream(resource);
        if (inputStream == null) throw new IOException("Resource not found: " + resource);

        return createAndInject(inputStream);
    }

    public boolean createAndInject(@NotNull InputStream inputStream) throws IOException {
        Files.createDirectories(this.path.getParent());
        if (Files.exists(this.path)) return false;

        Files.copy(inputStream, this.path);
        return true;
    }
}
