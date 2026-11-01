/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.showcase;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Test helpers for locating showcase webapp files.
 */
public final class ShowcaseFiles {

    private ShowcaseFiles() {
    }

    /** Resolves showcase/src/main/webapp whether tests run from the repo root or the module. */
    public static Path webappRoot() {
        final Path cwd = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        final Path inModule = cwd.resolve("src/main/webapp");
        if (Files.isDirectory(inModule)) {
            return inModule;
        }
        return cwd.resolve("showcase/src/main/webapp");
    }

    /** All .xhtml files under webapp/ui, empty if the directory does not exist. */
    public static List<Path> uiPages() {
        final Path ui = webappRoot().resolve("ui");
        if (!Files.isDirectory(ui)) {
            return Collections.emptyList();
        }
        try (Stream<Path> s = Files.walk(ui)) {
            return s.filter(p -> Files.isRegularFile(p) && p.getFileName().toString().endsWith(".xhtml"))
                        .sorted().collect(Collectors.toList());
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String read(final Path p) {
        try {
            return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
