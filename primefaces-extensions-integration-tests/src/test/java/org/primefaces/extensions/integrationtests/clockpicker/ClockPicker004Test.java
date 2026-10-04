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
import org.primefaces.selenium.component.Messages;
import org.primefaces.selenium.component.model.Severity;

public class ClockPicker004Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("ClockPicker: 12-hour mode AM/PM switching and round-trip")
    void amPmSwitchingRoundTrip(Page page) {
        ClockPicker ampmSwitch = page.ampmSwitch;
        CommandButton submit = page.submit;
        Messages messages = page.messages;

        WebElement input = ampmSwitch.getRoot().findElement(By.tagName("input"));

        // Arrange: rendered as 08:00 AM in 12-hour mode
        assertEquals("08:00AM", input.getAttribute("value"));

        // Act: open the picker and inspect default selection
        ampmSwitch.show();
        assertEquals("08", ampmSwitch.getHoursText());
        assertEquals("00", ampmSwitch.getMinutesText());
        assertEquals("AM", ampmSwitch.getAmPmText());

        // Act: switch to PM and close via done button
        ampmSwitch.clickPm();
        assertEquals("PM", ampmSwitch.getAmPmText());
        ampmSwitch.clickDone();

        // Assert: the value must now reflect PM
        assertEquals("08:00PM", input.getAttribute("value"));

        // Act: submit the form to persist the value
        submit.click();

        // Assert: the model value survives the round trip
        WebElement inputAfterSubmit = ampmSwitch.getRoot().findElement(By.tagName("input"));
        assertEquals("08:00PM", inputAfterSubmit.getAttribute("value"));

        // Assert: the server-side LocalTime was converted back correctly from 08:00PM
        assertFalse(messages.isEmpty(), "Expected an info message with the converted LocalTime value");
        assertTrue(messages.getMessagesBySeverity(Severity.INFO).size() > 0,
                    "Expected an INFO severity message after AM/PM submit");
        assertTrue(messages.getAllMessages().stream()
                    .anyMatch(msg -> Severity.INFO.equals(msg.getSeverity()) && msg.getDetail().contains("20:00")),
                    "Expected server-side converted LocalTime to be 20:00 from 08:00PM");
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(id = "form:ampmSwitch")
        ClockPicker ampmSwitch;

        @FindBy(id = "form:submitButton")
        CommandButton submit;

        @FindBy(id = "form:msgs")
        Messages messages;

        @Override
        public String getLocation() {
            return "clockpicker/clockpicker004.xhtml";
        }
    }
}
