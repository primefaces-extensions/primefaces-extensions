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
package org.primefaces.extensions.showcase.util.taglib;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TagLibProviderTest {

    private static TagLibProvider provider;

    @BeforeAll
    static void init() {
        provider = TagLibTestSupport.provider();
    }

    @Test
    void parsesPrimeFacesExtensionsTags() {
        final Map<String, TagInfo> tags = provider.getTags();
        for (final String name : List.of("sheet", "sheetcolumn", "monacoEditor", "markText", "layout", "layoutPane", "timePicker")) {
            assertTrue(tags.containsKey(name), "missing tag " + name);
        }
        assertTrue(tags.size() >= 60, "tags: " + tags.size());
    }

    @Test
    void sheetHasClientBehaviorEvents() {
        assertFalse(provider.getTags().get("sheet").getClientBehaviorsEvents().isEmpty());
    }

    @Test
    void timePickerHasValueProperty() {
        assertTrue(provider.getTags().get("timePicker").getProperties().stream().anyMatch(k -> "value".equals(k.getName())));
    }

    @Test
    void noFunctionsSoSectionIsHidden() {
        assertTrue(provider.getFunctionsSorted().isEmpty());
    }
}
