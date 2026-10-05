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
package org.primefaces.extensions.integrationtests.suneditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.integrationtests.AbstractPrimeExtensionsPageTest;
import org.primefaces.selenium.AbstractPrimePage;
import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.component.SunEditor;

public class SunEditor002Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("SunEditor: custom toolbar only renders configured buttons")
    void customToolbar(Page page) {
        SunEditor editor = page.toolbar;

        assertTrue(editor.hasToolbarButton("bold"));
        assertTrue(editor.hasToolbarButton("underline"));
        assertFalse(editor.hasToolbarButton("italic"));
        assertFalse(editor.hasToolbarButton("strike"));
        assertNoJavascriptErrors();
    }

    @Test
    @Order(2)
    @DisplayName("SunEditor: readonly editor is not editable")
    void readonly(Page page) {
        SunEditor editor = page.readonly;

        assertEquals("Read only", editor.getText());
        assertTrue(editor.isReadOnly());
    }

    @Test
    @Order(3)
    @DisplayName("SunEditor: disabled editor is disabled and not editable")
    void disabled(Page page) {
        SunEditor editor = page.disabled;

        assertEquals("Disabled", editor.getText());
        assertTrue(editor.isDisabled());
        assertTrue(editor.isReadOnly());
        assertNotNull(editor.getRoot().getAttribute("disabled"));
    }

    @Test
    @Order(4)
    @DisplayName("SunEditor: script tags in the value are sanitized")
    void sanitized(Page page) {
        SunEditor editor = page.unsafe;

        assertEquals("Safe", editor.getText());
        assertFalse(editor.getContent().toLowerCase().contains("<script"), editor.getContent());
        assertNull(PrimeSelenium.executeScript("return window.sunEditorXss;"));
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(id = "form:toolbar")
        SunEditor toolbar;

        @FindBy(id = "form:readonly")
        SunEditor readonly;

        @FindBy(id = "form:disabled")
        SunEditor disabled;

        @FindBy(id = "form:unsafe")
        SunEditor unsafe;

        @Override
        public String getLocation() {
            return "suneditor/sunEditor002.xhtml";
        }
    }
}
