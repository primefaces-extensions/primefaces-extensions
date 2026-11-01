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
package org.primefaces.extensions.showcase.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class FileContentMarkerUtilTest {

    private static InputStream stream(final String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void xhtmlKeepsOnlyExampleRegion() {
        final String xhtml = "<ui:composition>\nbefore\n<!-- EXAMPLE-SOURCE-START -->\n<div>inside</div>\n"
                    + "<!-- EXAMPLE-SOURCE-END -->\nafter\n</ui:composition>";
        final FileContent fc = FileContentMarkerUtil.readFileContent("/ui/x.xhtml", stream(xhtml), false);
        assertEquals("<div>inside</div>", fc.getValue().trim());
        assertEquals("x.xhtml", fc.getTitle());
    }

    @Test
    void xhtmlStripsExcludeBlocks() {
        final String xhtml = "<!-- EXAMPLE-SOURCE-START -->\n<div>keep</div>\n<!-- EXCLUDE-SOURCE-START -->\n"
                    + "<div>secret</div>\n<!-- EXCLUDE-SOURCE-END -->\n<span>tail</span>\n<!-- EXAMPLE-SOURCE-END -->";
        final FileContent fc = FileContentMarkerUtil.readFileContent("/ui/x.xhtml", stream(xhtml), false);
        assertTrue(fc.getValue().contains("keep"));
        assertTrue(fc.getValue().contains("tail"));
        assertFalse(fc.getValue().contains("secret"));
    }

    @Test
    void xhtmlHidesMarkerLinesInsideImplementation() {
        final String xhtml = "<ui:define name=\"implementation\">\n        <!-- EXAMPLE-SOURCE-START -->\n"
                    + "        <div class=\"card\">\n            <!-- EXAMPLE: counter/basicUsage -->\n            <h5>Basic</h5>\n"
                    + "        </div>\n        <!-- EXAMPLE-SOURCE-END -->\n</ui:define>";
        final FileContent fc = FileContentMarkerUtil.readFileContent("/ui/x.xhtml", stream(xhtml), false);
        assertFalse(fc.getValue().contains("EXAMPLE-SOURCE-START"), fc.getValue());
        assertFalse(fc.getValue().contains("EXAMPLE:"), fc.getValue());
        assertTrue(fc.getValue().startsWith("<div class=\"card\">"), fc.getValue());
        assertTrue(fc.getValue().contains("<h5>Basic</h5>"), fc.getValue());
    }

    @Test
    void javaStartsAtPackage() throws IOException {
        Path p = Paths.get("src/main/java/org/primefaces/extensions/showcase/controller/CounterController.java");
        if (!Files.exists(p)) {
            p = Paths.get("showcase").resolve(p);
        }
        try (InputStream is = Files.newInputStream(p)) {
            final FileContent fc = FileContentMarkerUtil.readFileContent(p.toString().replace("\\", "/"), is, false);
            assertTrue(fc.getValue().startsWith("package org.primefaces.extensions.showcase"), fc.getValue());
            assertEquals("java", fc.getType());
        }
    }
}
