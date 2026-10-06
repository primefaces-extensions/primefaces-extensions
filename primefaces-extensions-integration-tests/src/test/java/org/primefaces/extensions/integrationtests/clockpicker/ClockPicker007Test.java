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

        // Arrange: all four pickers share the same initial 24-hour value 10:00.
        // The view binds every picker to time8 so we can verify selection isolation
        // without introducing extra model properties.
        assertEquals("10:00", placementTop.getRoot().findElement(By.tagName("input")).getAttribute("value"));
        assertEquals("10:00", placementBottom.getRoot().findElement(By.tagName("input")).getAttribute("value"));
        assertEquals("10:00", placementLeft.getRoot().findElement(By.tagName("input")).getAttribute("value"));
        assertEquals("10:00", placementRight.getRoot().findElement(By.tagName("input")).getAttribute("value"));

        // Assert: each widget's rendered placement config matches the attribute.
        // Note: placement="bottom" is the component default, so WidgetBuilder omits it
        // from the widget cfg to save bytes (wb.attr with default). Fall back to 'bottom'
        // when cfg.placement is undefined, mirroring ClockPicker.DEFAULTS on the client.
        assertEquals("top", PrimeSelenium.executeScript("return window.PF('placementTopWidget').cfg.placement;"));
        assertEquals("bottom",
                    PrimeSelenium.executeScript("return window.PF('placementBottomWidget').cfg.placement || 'bottom';"));
        assertEquals("left", PrimeSelenium.executeScript("return window.PF('placementLeftWidget').cfg.placement;"));
        assertEquals("right", PrimeSelenium.executeScript("return window.PF('placementRightWidget').cfg.placement;"));

        // Assert: showOn="button" containers must not stretch to full width.
        // Without the CSS fix in 0-clockpicker.css, .ui-inputgroup forces a 660px width,
        // which makes placement="right" appear far from its input.
        assertContainerWidth(placementTop, "placementTopWidget");
        assertContainerWidth(placementBottom, "placementBottomWidget");
        assertContainerWidth(placementLeft, "placementLeftWidget");
        assertContainerWidth(placementRight, "placementRightWidget");

        // Act + Assert: verify each placement's popover class, coordinates, and proximity.
        // We use window.PF(widgetVar).show() instead of picker.show() because the Selenium
        // component's isShown()/getPopover() find the first .clockpicker-popover in the DOM,
        // which breaks when multiple pickers exist on the same page.
        assertPlacementPopover(placementTop, "placementTopWidget", "top");
        assertPlacementPopover(placementBottom, "placementBottomWidget", "bottom");
        assertPlacementPopover(placementLeft, "placementLeftWidget", "left");
        assertPlacementPopover(placementRight, "placementRightWidget", "right");

        // Act: select a new time on the right picker and close via done button.
        // CommandButton.click() guards the Ajax round-trip internally.
        // Use the right picker: placement="top" needs ~311px above the input,
        // which is off-screen when inputs are near the top of the page, so its
        // hour ticks are not clickable. The right popover opens to the side and
        // stays visible. Right is also last in the form, which matters because
        // all four pickers bind to the same time8 property: on submit the last
        // component wins, so only the last picker can survive the round-trip.
        // Use PF().show() and widget-owned popover lookups: the Selenium
        // ClockPicker.show()/selectHour()/getPopover() always resolve the first
        // .clockpicker-popover in the DOM, which is wrong with 4 pickers.
        PrimeSelenium.executeScript("window.PF('placementRightWidget').show();");
        selectHourInPopover("placementRightWidget", 11);
        selectMinuteInPopover("placementRightWidget", 30);
        clickDoneInPopover("placementRightWidget");

        // Assert: only the right picker input reflects the new selection.
        assertEquals("10:00", placementTop.getRoot().findElement(By.tagName("input")).getAttribute("value"));
        assertEquals("10:00", placementBottom.getRoot().findElement(By.tagName("input")).getAttribute("value"));
        assertEquals("10:00", placementLeft.getRoot().findElement(By.tagName("input")).getAttribute("value"));
        assertEquals("11:30", placementRight.getRoot().findElement(By.tagName("input")).getAttribute("value"));

        // Act: submit the form to persist the value.
        submit.click();

        // Assert: re-acquire inputs after Ajax rerender. The component tree is replaced
        // on the server, so cached WebElement references become stale.
        // All four pickers bind to the same time8 property, so after submit they
        // all rerender the persisted model value 11:30.
        WebElement inputTopAfter = placementTop.getRoot().findElement(By.tagName("input"));
        WebElement inputBottomAfter = placementBottom.getRoot().findElement(By.tagName("input"));
        WebElement inputLeftAfter = placementLeft.getRoot().findElement(By.tagName("input"));
        WebElement inputRightAfter = placementRight.getRoot().findElement(By.tagName("input"));

        assertEquals("11:30", inputTopAfter.getAttribute("value"));
        assertEquals("11:30", inputBottomAfter.getAttribute("value"));
        assertEquals("11:30", inputLeftAfter.getAttribute("value"));
        assertEquals("11:30", inputRightAfter.getAttribute("value"));

        // Assert: the server-side LocalTime was updated correctly.
        assertFalse(messages.isEmpty(), "Expected an info message after placement submit");
        assertTrue(messages.getMessagesBySeverity(Severity.INFO).size() > 0,
                    "Expected an INFO severity message after placement selection submit");
        assertTrue(messages.getAllMessages().stream()
                    .anyMatch(msg -> Severity.INFO.equals(msg.getSeverity()) && msg.getDetail().contains("11:30")),
                    "Expected server-side message to contain converted LocalTime 11:30");

        assertNoJavascriptErrors();
    }

    // Opens the picker via its widget instance and asserts popover placement.
    // Uses window.PF() because the Selenium component's show()/getPopover() rely on
    // querying the first .clockpicker-popover in the DOM, which is unreliable when
    // multiple pickers are present.
    private void assertPlacementPopover(ClockPicker picker, String widgetVar, String expectedPlacement) {
        PrimeSelenium.executeScript("window.PF('" + widgetVar + "').show();");

        WebElement input = picker.getRoot().findElement(By.tagName("input"));
        WebElement popover = getWidgetPopover(widgetVar);

        assertTrue(popover.getAttribute("class").contains(expectedPlacement),
                    "Expected popover class to contain '" + expectedPlacement + "' for placement='" + expectedPlacement + "', got: "
                                + popover.getAttribute("class"));

        assertPlacementCoordinates(input, popover, expectedPlacement);
        assertPlacementGap(input, popover, expectedPlacement);

        // Hide again so subsequent placement checks and hour selection run with
        // only one popover open. Otherwise getPopover() (first .clockpicker-popover
        // in the DOM) can resolve to the wrong picker and off-screen top popovers
        // make hour ticks unclickable.
        PrimeSelenium.executeScript("window.PF('" + widgetVar + "').hide();");
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

    // Clicks the hour tick inside the given widget's own popover.
    // Cannot use ClockPicker.selectHour(): it resolves the first popover in the DOM.
    private void selectHourInPopover(String widgetVar, int hour) {
        WebElement popover = getWidgetPopover(widgetVar);
        String hourText = hour == 0 ? "00" : String.valueOf(hour);
        WebElement tick = popover.findElement(By.xpath(
                    ".//div[contains(@class,'clockpicker-hours')]//div[contains(@class,'clockpicker-tick') and text()='"
                                + hourText + "']"));
        PrimeSelenium.waitGui().until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(tick));
        tick.click();
        // Wait for the hours->minutes view transition (see ClockPicker005Test):
        // minute ticks are not clickable while the hours dial is still visible.
        PrimeSelenium.waitGui().until(driver -> {
            try {
                WebElement hoursView = getWidgetPopover(widgetVar)
                            .findElement(By.cssSelector(".clockpicker-hours"));
                return "hidden".equals(hoursView.getCssValue("visibility"));
            }
            catch (Exception e) {
                return true;
            }
        });
    }

    // Clicks the minute tick inside the given widget's own popover.
    private void selectMinuteInPopover(String widgetVar, int minute) {
        int roundedMinute = Math.round(minute / 5.0f) * 5;
        if (roundedMinute == 60) {
            roundedMinute = 0;
        }
        String minuteText = roundedMinute < 10 ? "0" + roundedMinute : String.valueOf(roundedMinute);
        WebElement popover = getWidgetPopover(widgetVar);
        WebElement tick = popover.findElement(By.xpath(
                    ".//div[contains(@class,'clockpicker-minutes')]//div[contains(@class,'clockpicker-tick') and text()='"
                                + minuteText + "']"));
        PrimeSelenium.waitGui().until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(tick));
        tick.click();
    }

    // Clicks done inside the given widget's own popover.
    private void clickDoneInPopover(String widgetVar) {
        WebElement popover = getWidgetPopover(widgetVar);
        WebElement doneButton = popover.findElement(By.cssSelector(".clockpicker-button.btn-block"));
        PrimeSelenium.waitGui().until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(doneButton));
        doneButton.click();
    }

    // Verifies that the popover is on the correct side of the input.
    private void assertPlacementCoordinates(WebElement input, WebElement popover, String expectedPlacement) {
        var inputRect = input.getRect();
        var popoverRect = popover.getRect();

        switch (expectedPlacement) {
            case "top":
                assertTrue(popoverRect.y + popoverRect.height <= inputRect.y,
                            "Expected popover above input for placement='top', but popover top=" + popoverRect.y
                                        + " and input top=" + inputRect.y);
                break;
            case "bottom":
                assertTrue(popoverRect.y >= inputRect.y + inputRect.height,
                            "Expected popover below input for placement='bottom', but popover top=" + popoverRect.y
                                        + " and input bottom=" + (inputRect.y + inputRect.height));
                break;
            case "left":
                assertTrue(popoverRect.x + popoverRect.width <= inputRect.x,
                            "Expected popover left of input for placement='left', but popover left=" + popoverRect.x
                                        + " and input left=" + inputRect.x);
                break;
            case "right":
                assertTrue(popoverRect.x >= inputRect.x + inputRect.width,
                            "Expected popover right of input for placement='right', but popover left=" + popoverRect.x
                                        + " and input right=" + (inputRect.x + inputRect.width));
                break;
            default:
                fail("Unexpected placement: " + expectedPlacement);
        }
    }

    // Verifies that the popover is close to the input, not positioned far away.
    // Without the CSS fix, placement="right" gap is ~600px because .ui-inputgroup
    // stretches the container to full width.
    private void assertPlacementGap(WebElement input, WebElement popover, String expectedPlacement) {
        var inputRect = input.getRect();
        var popoverRect = popover.getRect();
        int gap;
        switch (expectedPlacement) {
            case "top":
                gap = inputRect.y - (popoverRect.y + popoverRect.height);
                break;
            case "bottom":
                gap = popoverRect.y - (inputRect.y + inputRect.height);
                break;
            case "left":
                gap = inputRect.x - (popoverRect.x + popoverRect.width);
                break;
            case "right":
                gap = popoverRect.x - (inputRect.x + inputRect.width);
                break;
            default:
                throw new IllegalArgumentException("Unexpected placement: " + expectedPlacement);
        }
        assertTrue(gap >= 0 && gap < 120,
                    "Expected popover gap for placement='" + expectedPlacement + "' to be under 120px but was " + gap + "px");
    }

    // The picker container must not expand to full width in button-trigger mode.
    // This guards the CSS defect described in assertPlacementGap.
    private void assertContainerWidth(ClockPicker picker, String widgetVar) {
        Number width = (Number) PrimeSelenium.executeScript(
                    "var w = window.PF('" + widgetVar + "');"
                                + "if (!w) return -1;"
                                + "var el = $(w.jqId);"
                                + "return el ? el.outerWidth() : -1;");
        assertTrue(width.doubleValue() > 0 && width.doubleValue() < 200,
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
