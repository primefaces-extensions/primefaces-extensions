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
import org.primefaces.selenium.component.ClockPicker;
import org.primefaces.selenium.component.CommandButton;

public class ClockPicker003Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("ClockPicker: 12-hour midnight value must keep the AM indicator")
    void midnightKeepsAm(Page page) {
        ClockPicker midnight = page.midnight;
        CommandButton submit = page.submit;

        // Arrange: midnight is rendered as 12:00 AM in 12-hour mode
        WebElement input = midnight.getRoot().findElement(By.tagName("input"));
        assertEquals("12:00AM", input.getAttribute("value"));

        // Act: open the picker and inspect the popover
        midnight.show();
        assertEquals("12", midnight.getHoursText());
        assertEquals("00", midnight.getMinutesText());
        assertEquals("AM", midnight.getAmPmText());

        // Act: close the picker via the done button without changing anything
        midnight.clickDone();

        // Assert: the value must still be midnight, not noon
        assertEquals("12:00AM", input.getAttribute("value"));

        // Act: submit the form to persist the value
        submit.click();

        // Assert: the model value survives the round trip
        WebElement inputAfterSubmit = midnight.getRoot().findElement(By.tagName("input"));
        assertEquals("12:00AM", inputAfterSubmit.getAttribute("value"));
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(id = "form:midnight")
        ClockPicker midnight;

        @FindBy(id = "form:submitButton")
        CommandButton submit;

        @Override
        public String getLocation() {
            return "clockpicker/clockpicker003.xhtml";
        }
    }
}
