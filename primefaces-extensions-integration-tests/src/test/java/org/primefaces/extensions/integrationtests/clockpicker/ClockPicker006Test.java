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

public class ClockPicker006Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("ClockPicker: client-side lifecycle callbacks fire")
    void clientSideCallbacksFire(Page page) {
        ClockPicker callbacks = page.callbacks;

        WebElement input = callbacks.getRoot().findElement(By.tagName("input"));

        // Arrange: rendered as 08:00 AM in 12-hour mode
        assertEquals("08:00AM", input.getAttribute("value"));

        // Act: open the picker
        resetCallbackLog();
        callbacks.show();
        // Assert: show lifecycle callbacks fired
        assertTrue(callbackLog().contains("beforeshow"),
                    "Expected beforeShow callback to fire when the picker is shown");
        assertTrue(callbackLog().contains("aftershow"),
                    "Expected afterShow callback to fire when the picker is shown");

        // Act: select an hour
        resetCallbackLog();
        callbacks.selectHour(9);
        // Assert: hour selection callbacks fired
        assertTrue(callbackLog().contains("beforehourselect"),
                    "Expected beforeHourSelect callback to fire when an hour is selected");
        assertTrue(callbackLog().contains("afterhourselect"),
                    "Expected afterHourSelect callback to fire when an hour is selected");

        // Act: switch to PM
        resetCallbackLog();
        callbacks.clickPm();
        // Assert: AM/PM selection callback fired
        assertTrue(callbackLog().contains("afterampmselect"),
                    "Expected afterAmPmSelect callback to fire when AM/PM is switched");

        // Act: close via done button
        resetCallbackLog();
        callbacks.clickDone();
        // Assert: done and hide lifecycle callbacks fired
        assertTrue(callbackLog().contains("beforedone"),
                    "Expected beforeDone callback to fire when done is clicked");
        assertTrue(callbackLog().contains("afterdone"),
                    "Expected afterDone callback to fire when done is clicked");
        assertTrue(callbackLog().contains("beforehide"),
                    "Expected beforeHide callback to fire when the picker is hidden");
        assertTrue(callbackLog().contains("afterhide"),
                    "Expected afterHide callback to fire when the picker is hidden");

        // Assert: the input value reflects the selection
        assertEquals("09:00PM", input.getAttribute("value"));

        assertNoJavascriptErrors();
    }

    private void resetCallbackLog() {
        PrimeSelenium.executeScript("window.cpEvents = [];");
    }

    private String callbackLog() {
        return PrimeSelenium.executeScript("return window.cpEvents.join(',');");
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(id = "form:callbacks")
        ClockPicker callbacks;

        @Override
        public String getLocation() {
            return "clockpicker/clockpicker006.xhtml";
        }
    }
}
