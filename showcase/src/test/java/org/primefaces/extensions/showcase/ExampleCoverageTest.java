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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExampleCoverageTest {

    private static final Pattern MARKER = Pattern.compile("<!--\\s*EXAMPLE:\\s*(\\S+)\\s*-->");

    private static Set<String> inventory() throws IOException {
        final Set<String> lines = new LinkedHashSet<>();
        try (InputStream in = ExampleCoverageTest.class.getResourceAsStream("/example-inventory.txt")) {
            Assertions.assertNotNull(in, "example-inventory.txt missing");
            for (final String line : new String(in.readAllBytes(), StandardCharsets.UTF_8).split("\r?\n")) {
                final String t = line.trim();
                if (!t.isEmpty() && !t.startsWith("#")) {
                    lines.add(t);
                }
            }
        }
        return lines;
    }

    private static boolean legacyExists(final String entry) {
        final String dir = entry.substring(0, entry.indexOf('/'));
        final String useCase = entry.substring(entry.indexOf('/') + 1);
        final Path sections = ShowcaseFiles.webappRoot().resolve("sections").resolve(dir);
        if ("fileDownload/basic".equals(entry)) {
            return Files.exists(ShowcaseFiles.webappRoot().resolve("views/fileDownload.xhtml"));
        }
        return Files.exists(sections.resolve("example-" + useCase + ".xhtml"))
                    || (("fluidgrid".equals(dir) || "keynote".equals(dir)) && Files.exists(sections.resolve(useCase + ".xhtml")));
    }

    private static List<String> markersIn(final Path page) {
        final List<String> found = new ArrayList<>();
        final Matcher m = MARKER.matcher(ShowcaseFiles.read(page));
        while (m.find()) {
            found.add(m.group(1));
        }
        return found;
    }

    @Test
    void everyLegacyExampleStillExists() throws IOException {
        final Set<String> covered = new LinkedHashSet<>();
        for (final Path page : ShowcaseFiles.uiPages()) {
            covered.addAll(markersIn(page));
        }
        final List<String> missing = new ArrayList<>();
        for (final String entry : inventory()) {
            if (!legacyExists(entry) && !covered.contains(entry)) {
                missing.add(entry);
            }
        }
        Assertions.assertTrue(missing.isEmpty(), "Examples neither in legacy files nor in ui pages: " + missing);
    }

    @Test
    void markersReferToInventory() throws IOException {
        final Set<String> inventory = inventory();
        final Map<String, Path> seen = new HashMap<>();
        final List<String> problems = new ArrayList<>();
        for (final Path page : ShowcaseFiles.uiPages()) {
            for (final String marker : markersIn(page)) {
                if (!inventory.contains(marker)) {
                    problems.add("unknown marker '" + marker + "' in " + page);
                }
                final Path prev = seen.put(marker, page);
                if (prev != null) {
                    problems.add("duplicate marker '" + marker + "' in " + prev + " and " + page);
                }
            }
        }
        Assertions.assertTrue(problems.isEmpty(), "Marker problems: " + problems);
    }
}
