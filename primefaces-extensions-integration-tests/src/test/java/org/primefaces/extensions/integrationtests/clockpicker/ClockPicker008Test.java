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
package org.primefaces.extensions.integrationtests.clockpicker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.primefaces.extensions.integrationtests.AbstractPrimeExtensionsPageTest;
import org.primefaces.selenium.AbstractPrimePage;
import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.component.ClockPicker;

public class ClockPicker008Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("ClockPicker: align values position popover correctly")
    void alignValuesPositionPopoverCorrectly(Page page) {
        // Arrange: all five pickers share the same initial 24-hour value 10:00.
        // The view binds every picker to time8 so we can verify positioning
        // isolation without introducing extra model properties (see ClockPicker007Test).
        assertEquals("10:00", inputValue(page.alignLeft));
        assertEquals("10:00", inputValue(page.alignCenter));
        assertEquals("10:00", inputValue(page.alignRight));
        assertEquals("10:00", inputValue(page.alignTop));
        assertEquals("10:00", inputValue(page.alignBottom));

        // Assert: each widget's rendered align config matches the attribute.
        // Note: align="left" is the component default, so WidgetBuilder omits it
        // from the widget cfg to save bytes (wb.attr with default). Fall back to 'left'
        // when cfg.align is undefined, mirroring ClockPicker.DEFAULTS on the client.
        // Placement="bottom" is likewise the default (see ClockPicker007Test).
        assertEquals("left",
                    PrimeSelenium.executeScript("return window.PF(arguments[0]).cfg.align || 'left';", "alignLeftWidget"));
        assertEquals("center",
                    PrimeSelenium.executeScript("return window.PF(arguments[0]).cfg.align;", "alignCenterWidget"));
        assertEquals("right",
                    PrimeSelenium.executeScript("return window.PF(arguments[0]).cfg.align;", "alignRightWidget"));
        assertEquals("top",
                    PrimeSelenium.executeScript("return window.PF(arguments[0]).cfg.align;", "alignTopWidget"));
        assertEquals("bottom",
                    PrimeSelenium.executeScript("return window.PF(arguments[0]).cfg.align;", "alignBottomWidget"));

        // Act + Assert: align="left|center|right" shifts the popover horizontally
        // for placement="bottom". From ClockPicker.locate(): left anchors to the input
        // left, center subtracts half the width delta, right subtracts the full delta,
        // so with a popover wider than the input, left > center > right.
        double leftPos = popoverX("alignLeftWidget", "left");
        double centerPos = popoverX("alignCenterWidget", "center");
        double rightPos = popoverX("alignRightWidget", "right");

        assertTrue(leftPos > centerPos,
                    "Expected align='left' popover to be right of align='center', but left=" + leftPos + " center=" + centerPos);
        assertTrue(centerPos > rightPos,
                    "Expected align='center' popover to be right of align='right', but center=" + centerPos + " right=" + rightPos);

        // Act + Assert: align="top|bottom" shifts the popover vertically
        // for placement="right". From ClockPicker.locate(): top anchors to the input
        // top, bottom subtracts the height delta, so with a popover taller than the
        // input, top > bottom.
        double topPos = popoverY("alignTopWidget", "top");
        double bottomPos = popoverY("alignBottomWidget", "bottom");

        assertTrue(topPos > bottomPos,
                    "Expected align='top' popover top (" + topPos + ") to be below align='bottom' popover top ("
                                + bottomPos + ") because align='bottom' anchors the popover bottom to the input bottom");

        assertNoJavascriptErrors();
    }

    private static String inputValue(ClockPicker picker) {
        return picker.getRoot().findElement(By.tagName("input")).getAttribute("value");
    }

    // Opens the picker via its widget instance and returns the popover viewport X.
    // Uses window.PF() with a widget-owned popover lookup because the Selenium
    // component's show()/getPopover() resolve the first .clockpicker-popover in the
    // DOM, which is wrong with 5 pickers (see ClockPicker007Test).
    private double popoverX(String widgetVar, String expectedAlign) {
        showPopover(widgetVar);
        try {
            WebElement popover = getWidgetPopover(widgetVar);
            PrimeSelenium.waitGui().until(ExpectedConditions.visibilityOf(popover));
            assertTrue(popover.getAttribute("class").contains("clockpicker-align-" + expectedAlign),
                        "Expected popover class to contain 'clockpicker-align-" + expectedAlign + "', got: "
                                    + popover.getAttribute("class"));
            return popover.getRect().getX();
        }
        finally {
            hidePopover(widgetVar);
        }
    }

    // Opens the picker via its widget instance and returns the popover viewport Y.
    private double popoverY(String widgetVar, String expectedAlign) {
        showPopover(widgetVar);
        try {
            WebElement popover = getWidgetPopover(widgetVar);
            PrimeSelenium.waitGui().until(ExpectedConditions.visibilityOf(popover));
            assertTrue(popover.getAttribute("class").contains("clockpicker-align-" + expectedAlign),
                        "Expected popover class to contain 'clockpicker-align-" + expectedAlign + "', got: "
                                    + popover.getAttribute("class"));
            return popover.getRect().getY();
        }
        finally {
            hidePopover(widgetVar);
        }
    }

    private static void showPopover(String widgetVar) {
        PrimeSelenium.executeScript("window.PF(arguments[0]).show();", widgetVar);
    }

    private static void hidePopover(String widgetVar) {
        PrimeSelenium.executeScript("window.PF(arguments[0]).hide();", widgetVar);
    }

    // Returns the popover element owned by the widget instance.
    // This avoids matching an adjacent widget's popover when multiple pickers
    // are present on the page.
    private WebElement getWidgetPopover(String widgetVar) {
        return (WebElement) PrimeSelenium.executeScript(
                    "var widget = window.PF(arguments[0]);"
                                + "if (!widget) return null;"
                                + "var instance = $(widget.jqId).data('clockpicker');"
                                + "return instance && instance.popover ? instance.popover[0] : null;",
                    widgetVar);
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(id = "form:alignLeft")
        ClockPicker alignLeft;

        @FindBy(id = "form:alignCenter")
        ClockPicker alignCenter;

        @FindBy(id = "form:alignRight")
        ClockPicker alignRight;

        @FindBy(id = "form:alignTop")
        ClockPicker alignTop;

        @FindBy(id = "form:alignBottom")
        ClockPicker alignBottom;

        @Override
        public String getLocation() {
            return "clockpicker/clockpicker008.xhtml";
        }
    }
}
