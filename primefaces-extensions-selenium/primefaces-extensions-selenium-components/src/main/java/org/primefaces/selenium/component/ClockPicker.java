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
package org.primefaces.selenium.component;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.component.base.AbstractInputComponent;

/**
 * Component wrapper for the PrimeFaces Extensions {@code pe:clockpicker}.
 */
public abstract class ClockPicker extends AbstractInputComponent {

    private static final String POPOVER_CLASS = "clockpicker-popover";
    private static final String DONE_BUTTON_CLASS = "clockpicker-button";
    private static final String HOURS_SPAN_CLASS = "clockpicker-span-hours";
    private static final String MINUTES_SPAN_CLASS = "clockpicker-span-minutes";
    private static final String AM_BUTTON_CLASS = "am-button";
    private static final String PM_BUTTON_CLASS = "pm-button";
    private static final String TICK_CLASS = "clockpicker-tick";

    /**
     * Opens the clockpicker popover if not already open.
     */
    public void show() {
        if (!isShown()) {
            PrimeSelenium.executeScript(getWidgetByIdScript() + ".show();");
            waitForPopover();
        }
    }

    /**
     * Hides the clockpicker popover if open.
     */
    public void hide() {
        if (isShown()) {
            PrimeSelenium.executeScript(getWidgetByIdScript() + ".hide();");
            waitForPopoverHidden();
        }
    }

    /**
     * Clicks the done/close button in the popover.
     */
    public void clickDone() {
        WebElement doneButton = getDoneButton();
        PrimeSelenium.waitGui().until(ExpectedConditions.elementToBeClickable(doneButton));
        doneButton.click();
    }

    /**
     * Gets the done/close button element.
     */
    public WebElement getDoneButton() {
        return getPopover().findElement(By.className(DONE_BUTTON_CLASS));
    }

    /**
     * Gets the current hours text displayed in the popover.
     */
    public String getHoursText() {
        return getPopover().findElement(By.className(HOURS_SPAN_CLASS)).getText();
    }

    /**
     * Gets the current minutes text displayed in the popover.
     */
    public String getMinutesText() {
        return getPopover().findElement(By.className(MINUTES_SPAN_CLASS)).getText();
    }

    /**
     * Selects an hour by clicking the corresponding tick.
     *
     * @param hour the hour to select (0-23 for 24h, 1-12 for 12h)
     */
    public void selectHour(int hour) {
        WebElement tick = getPopover().findElement(By.cssSelector("." + TICK_CLASS));
        // Find tick by text content
        String hourText = hour == 0 ? "00" : String.valueOf(hour);
        tick = getPopover().findElement(By.xpath("//div[contains(@class,'" + TICK_CLASS + "') and text()='" + hourText + "']"));
        PrimeSelenium.waitGui().until(ExpectedConditions.elementToBeClickable(tick));
        tick.click();
    }

    /**
     * Selects a minute by clicking the corresponding tick.
     *
     * @param minute the minute to select, rounded to nearest 5
     */
    public void selectMinute(int minute) {
        int roundedMinute = Math.round(minute / 5.0f) * 5;
        if (roundedMinute == 60) {
            roundedMinute = 0;
        }
        String minuteText = roundedMinute < 10 ? "0" + roundedMinute : String.valueOf(roundedMinute);
        WebElement tick = getPopover().findElement(By.xpath("//div[contains(@class,'" + TICK_CLASS + "') and text()='" + minuteText + "']"));
        PrimeSelenium.waitGui().until(ExpectedConditions.elementToBeClickable(tick));
        tick.click();
    }

    /**
     * Clicks the AM button if in 12-hour mode.
     */
    public void clickAm() {
        getPopover().findElement(By.className(AM_BUTTON_CLASS)).click();
    }

    /**
     * Clicks the PM button if in 12-hour mode.
     */
    public void clickPm() {
        getPopover().findElement(By.className(PM_BUTTON_CLASS)).click();
    }

    /**
     * Whether the clockpicker popover is currently shown.
     */
    public boolean isShown() {
        try {
            return getPopover().isDisplayed();
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Gets the popover element.
     */
    public WebElement getPopover() {
        return getWebDriver().findElement(By.className(POPOVER_CLASS));
    }

    private void waitForPopover() {
        PrimeSelenium.waitGui().until(ExpectedConditions.visibilityOfElementLocated(By.className(POPOVER_CLASS)));
    }

    private void waitForPopoverHidden() {
        PrimeSelenium.waitGui().until(ExpectedConditions.invisibilityOfElementLocated(By.className(POPOVER_CLASS)));
    }
}
