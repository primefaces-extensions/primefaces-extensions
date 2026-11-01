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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Guards that the legacy pe:layout showcase shell stays removed.
 */
class LegacyRemovedTest {

    private static List<Path> files(final Path root) throws IOException {
        try (Stream<Path> s = Files.walk(root)) {
            return s.filter(Files::isRegularFile).collect(Collectors.toList());
        }
    }

    @Test
    void noLegacyDirs() {
        final Path root = ShowcaseFiles.webappRoot();
        for (final String d : new String[] {"templates", "views", "sections"}) {
            Assertions.assertFalse(Files.exists(root.resolve(d)), "Legacy directory still exists: " + d);
        }
    }

    @Test
    void noPeLayoutShell() throws IOException {
        final Path webInf = ShowcaseFiles.webappRoot().resolve("WEB-INF");
        for (final Path p : files(webInf)) {
            final String name = p.getFileName().toString();
            if (name.endsWith(".xhtml") || name.endsWith(".xml")) {
                Assertions.assertFalse(ShowcaseFiles.read(p).contains("pe:layout"), "pe:layout used in " + p);
            }
        }
    }

    @Test
    void noLegacyCss() throws IOException {
        final Path root = ShowcaseFiles.webappRoot();
        for (final Path p : files(root)) {
            final String name = p.getFileName().toString();
            if (!(name.endsWith(".xhtml") || name.endsWith(".xml") || name.endsWith(".css") || name.endsWith(".js"))) {
                continue;
            }
            final String c = ShowcaseFiles.read(p);
            for (final String needle : new String[] {"syntaxhighlighter", "themeSwitcher.css", "masterLayout"}) {
                Assertions.assertFalse(c.contains(needle), needle + " referenced in " + p);
            }
        }
    }

    @Test
    void noLegacyResources() {
        final Path root = ShowcaseFiles.webappRoot();
        for (final String f : new String[] {"resources/syntaxhighlighter", "resources/js/showcase.js", "resources/css/global.css",
                    "resources/css/layout.css", "resources/css/usecases.css", "resources/css/themeSwitcher.css"}) {
            Assertions.assertFalse(Files.exists(root.resolve(f)), "Legacy resource still exists: " + f);
        }
    }
}
