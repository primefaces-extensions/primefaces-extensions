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
package org.primefaces.extensions.component.documentviewer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * DocumentViewer component tests.
 */
public class DocumentViewerTest {

    private static final String PDF_URL = "/invoicePdfServlet?uid=1234";

    /**
     * GitHub #2769: a literal <code>url</code> must be an alias for <code>value</code>, otherwise the renderer (which reads {@code getValue()}) produces an
     * empty document source.
     */
    @Test
    void urlIsAliasForValue() {
        final DocumentViewer viewer = new DocumentViewer();
        viewer.setUrl(PDF_URL);

        assertEquals(PDF_URL, viewer.getValue());
        assertEquals(PDF_URL, viewer.getUrl());
    }

    @Test
    void valueIsAliasForUrl() {
        final DocumentViewer viewer = new DocumentViewer();
        viewer.setValue(PDF_URL);

        assertEquals(PDF_URL, viewer.getUrl());
        assertEquals(PDF_URL, viewer.getValue());
    }

    @Test
    void urlDefaultsToNull() {
        final DocumentViewer viewer = new DocumentViewer();

        assertNull(viewer.getUrl());
        assertNull(viewer.getValue());
    }
}
