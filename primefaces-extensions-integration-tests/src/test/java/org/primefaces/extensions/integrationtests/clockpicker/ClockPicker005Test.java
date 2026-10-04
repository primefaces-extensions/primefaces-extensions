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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.primefaces.extensions.integrationtests.AbstractPrimeExtensionsPageTest;
import org.primefaces.selenium.AbstractPrimePage;
import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.component.ClockPicker;
import org.primefaces.selenium.component.CommandButton;
import org.primefaces.selenium.component.Messages;
import org.primefaces.selenium.component.model.Severity;

public class ClockPicker005Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("ClockPicker: 24-hour mode with focus trigger and time selection round-trip")
    void twentyFourHourFocusSelectionRoundTrip(Page page) {
        ClockPicker focus24h = page.focus24h;
        CommandButton submit = page.submit;
        Messages messages = page.messages;

        WebElement input = focus24h.getRoot().findElement(By.tagName("input"));

        // Arrange: rendered with initial 24-hour value 14:30
        assertEquals("14:30", input.getAttribute("value"));

        // Act: open the picker via focus (showOn="focus") and verify initial popover state
        focus24h.show();
        PrimeSelenium.waitGui().until((ExpectedCondition<Boolean>) driver -> {
            try {
                return "14".equals(focus24h.getHoursText());
            }
            catch (Exception e) {
                return false;
            }
        });
        assertEquals("14", focus24h.getHoursText());
        assertEquals("30", focus24h.getMinutesText());

        // Act: select a new hour and wait for the view transition to complete
        focus24h.selectHour(16);
        PrimeSelenium.waitGui().until((ExpectedCondition<Boolean>) driver -> {
            try {
                WebElement hoursView = focus24h.getPopover().findElement(By.cssSelector(".clockpicker-hours"));
                String visibility = hoursView.getCssValue("visibility");
                return "hidden".equals(visibility);
            }
            catch (NoSuchElementException e) {
                return true;
            }
        });
        assertEquals("16", focus24h.getHoursText());
        assertEquals("30", focus24h.getMinutesText());

        // Act: select a new minute and close via done button
        focus24h.selectMinute(45);
        assertEquals("45", focus24h.getMinutesText());
        focus24h.clickDone();

        // Assert: the input value reflects the new selection
        assertEquals("16:45", input.getAttribute("value"));

        // Act: submit the form to persist the value
        submit.click();

        // Assert: the model value survives the round trip
        WebElement inputAfterSubmit = focus24h.getRoot().findElement(By.tagName("input"));
        assertEquals("16:45", inputAfterSubmit.getAttribute("value"));

        // Assert: the server-side LocalTime was converted correctly to 16:45
        assertFalse(messages.isEmpty(), "Expected an info message with the converted LocalTime value");
        assertTrue(messages.getMessagesBySeverity(Severity.INFO).size() > 0,
                    "Expected an INFO severity message after time selection submit");
        assertTrue(messages.getAllMessages().stream()
                    .anyMatch(msg -> Severity.INFO.equals(msg.getSeverity()) && msg.getDetail().contains("16:45")),
                    "Expected server-side message to contain converted LocalTime 16:45");
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(id = "form:focus24h")
        ClockPicker focus24h;

        @FindBy(id = "form:submitButton")
        CommandButton submit;

        @FindBy(id = "form:msgs")
        Messages messages;

        @Override
        public String getLocation() {
            return "clockpicker/clockpicker005.xhtml";
        }
    }
}
