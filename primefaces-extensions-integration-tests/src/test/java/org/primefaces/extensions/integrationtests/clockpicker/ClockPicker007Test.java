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
import org.primefaces.extensions.integrationtests.AbstractPrimeExtensionsPageTest;
import org.primefaces.selenium.AbstractPrimePage;
import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.component.ClockPicker;
import org.primefaces.selenium.component.CommandButton;
import org.primefaces.selenium.component.Messages;
import org.primefaces.selenium.component.model.Severity;

public class ClockPicker007Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("ClockPicker: all four placement directions render widget config and position popover correctly")
    void allPlacementsRenderCorrectly(Page page) {
        ClockPicker placementTop = page.placementTop;
        ClockPicker placementBottom = page.placementBottom;
        ClockPicker placementLeft = page.placementLeft;
        ClockPicker placementRight = page.placementRight;
        CommandButton submit = page.submit;
        Messages messages = page.messages;

        WebElement inputTop = placementTop.getRoot().findElement(By.tagName("input"));
        WebElement inputBottom = placementBottom.getRoot().findElement(By.tagName("input"));
        WebElement inputLeft = placementLeft.getRoot().findElement(By.tagName("input"));
        WebElement inputRight = placementRight.getRoot().findElement(By.tagName("input"));

        // Arrange: all four pickers share the same initial 24-hour value 10:00
        assertEquals("10:00", inputTop.getAttribute("value"));
        assertEquals("10:00", inputBottom.getAttribute("value"));
        assertEquals("10:00", inputLeft.getAttribute("value"));
        assertEquals("10:00", inputRight.getAttribute("value"));

        // Assert: widget configuration carries the correct placement for each picker
        assertEquals("top", PrimeSelenium.executeScript("return window.PF('placementTopWidget').cfg.placement;"));
        assertEquals("bottom", PrimeSelenium.executeScript("return window.PF('placementBottomWidget').cfg.placement;"));
        assertEquals("left", PrimeSelenium.executeScript("return window.PF('placementLeftWidget').cfg.placement;"));
        assertEquals("right", PrimeSelenium.executeScript("return window.PF('placementRightWidget').cfg.placement;"));

        // Assert: showOn="button" containers must not stretch to full width.
        // This guards the defect where .ui-inputgroup made the picker 660px wide,
        // causing placement="right" to appear far away from its input.
        assertContainerWidth(placementTop, "placementTopWidget");
        assertContainerWidth(placementBottom, "placementBottomWidget");
        assertContainerWidth(placementLeft, "placementLeftWidget");
        assertContainerWidth(placementRight, "placementRightWidget");

        // Act + Assert: open each picker and verify popover positioning class
        placementTop.show();
        assertPopoverPlacement(placementTop, "top");

        placementBottom.show();
        assertPopoverPlacement(placementBottom, "bottom");

        placementLeft.show();
        assertPopoverPlacement(placementLeft, "left");

        placementRight.show();
        assertPopoverPlacement(placementRight, "right");

        // Act: select a new time on the top picker and close via done button
        placementTop.selectHour(11);
        placementTop.selectMinute(30);
        placementTop.clickDone();

        // Assert: only the top picker input reflects the new selection
        assertEquals("11:30", inputTop.getAttribute("value"));
        assertEquals("10:00", inputBottom.getAttribute("value"));
        assertEquals("10:00", inputLeft.getAttribute("value"));
        assertEquals("10:00", inputRight.getAttribute("value"));

        // Act: submit the form to persist the value
        submit.click();

        // Assert: the model value survives the round trip on the top picker
        assertEquals("11:30", inputTop.getAttribute("value"));

        // Assert: the server-side LocalTime was updated correctly
        assertFalse(messages.isEmpty(), "Expected an info message after placement submit");
        assertTrue(messages.getMessagesBySeverity(Severity.INFO).size() > 0,
                    "Expected an INFO severity message after placement selection submit");
        assertTrue(messages.getAllMessages().stream()
                    .anyMatch(msg -> Severity.INFO.equals(msg.getSeverity()) && msg.getDetail().contains("11:30")),
                    "Expected server-side message to contain converted LocalTime 11:30");

        assertNoJavascriptErrors();
    }

    private void assertPopoverPlacement(ClockPicker picker, String expectedPlacement) {
        WebElement popover = picker.getPopover();
        String popoverClass = popover.getAttribute("class");
        assertTrue(popoverClass.contains(expectedPlacement),
                    "Expected popover class to contain '" + expectedPlacement + "' for placement='" + expectedPlacement + "', got: " + popoverClass);
    }

    private void assertContainerWidth(ClockPicker picker, String widgetVar) {
        long width = (Long) PrimeSelenium.executeScript(
                    "var w = window.PF('" + widgetVar + "');"
                                + "if (!w) return -1;"
                                + "var el = $(w.jqId);"
                                + "return el ? el.outerWidth() : -1;");
        assertTrue(width > 0 && width < 200,
                    "Expected picker container width to be under 200px for '" + widgetVar + "' but was " + width + "px");
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(id = "form:placementTop")
        ClockPicker placementTop;

        @FindBy(id = "form:placementBottom")
        ClockPicker placementBottom;

        @FindBy(id = "form:placementLeft")
        ClockPicker placementLeft;

        @FindBy(id = "form:placementRight")
        ClockPicker placementRight;

        @FindBy(id = "form:submitButton")
        CommandButton submit;

        @FindBy(id = "form:msgs")
        Messages messages;

        @Override
        public String getLocation() {
            return "clockpicker/clockpicker007.xhtml";
        }
    }
}
