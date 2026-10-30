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
package org.primefaces.extensions.integrationtests.layout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.integrationtests.AbstractPrimeExtensionsPageTest;
import org.primefaces.selenium.AbstractPrimePage;
import org.primefaces.selenium.PrimeSelenium;

public class Layout001Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("Layout: the resizer is a PrimeFaces draggable")
    void resizerIsDraggable(Page page) {
        assertTrue(page.resizer.getAttribute("class").contains("p-draggable"), page.resizer.getAttribute("class"));
        assertEquals(true, PrimeSelenium.executeScript("return !!PrimeFaces.interactions.instance($('.ui-layout-resizer-west'), 'draggable');"));
        assertNoJavascriptErrors();
    }

    @Test
    @Order(2)
    @DisplayName("Layout: dragging the resizer resizes the pane")
    void dragResizes(Page page) {
        long before = westWidth();

        drag(page, 80);

        long after = westWidth();
        assertTrue(Math.abs(after - before - 80) <= 2, "west pane width before=" + before + " after=" + after);
        // the cloned resizer used as helper is removed after the drag
        assertEquals(0L, (long) PrimeSelenium.<Number> executeScript("return $('.ui-layout-resizer-dragging').length;").longValue());
        assertNoJavascriptErrors();
    }

    @Test
    @Order(3)
    @DisplayName("Layout: dragging the resizer respects the maximum size")
    void dragRespectsMaxSize(Page page) {
        drag(page, 400);

        long after = westWidth();
        assertTrue(after <= 402, "west pane width " + after + " exceeds maxSize 400");
        assertNoJavascriptErrors();
    }

    @Test
    @Order(4)
    @DisplayName("Layout: disableResizable disables the resizer")
    void disableResizable(Page page) {
        long before = westWidth();

        PrimeSelenium.executeScript("PF('layoutWidget').layout.disableResizable('west');");
        assertTrue(page.resizer.getAttribute("class").contains("p-draggable-disabled"), page.resizer.getAttribute("class"));
        drag(page, 80);

        assertEquals(before, westWidth());
        assertNoJavascriptErrors();
    }

    private static long westWidth() {
        return ((Number) PrimeSelenium.executeScript("return $('.ui-layout-pane-west').outerWidth();")).longValue();
    }

    private static void drag(Page page, int distance) {
        Actions actions = new Actions(page.getWebDriver());
        actions.clickAndHold(page.resizer);
        int steps = distance / 10;
        for (int i = 0; i < steps; i++) {
            actions.moveByOffset(10, 0);
        }
        actions.release().perform();
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(css = ".ui-layout-resizer-west")
        WebElement resizer;

        @Override
        public String getLocation() {
            return "layout/layout001.xhtml";
        }
    }
}
