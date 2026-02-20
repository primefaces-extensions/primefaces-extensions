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
package org.primefaces.extensions.component.timepicker;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.BeforeShowEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.TimeSelectEvent;

/**
 * <code>TimePicker</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.3
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "beforeShow", event = BeforeShowEvent.class,
                        description = "Fires before the time picker panel is shown."),
            @FacesBehaviorEvent(name = "timeSelect", event = TimeSelectEvent.class,
                        description = "Fires when a time is selected.", defaultEvent = true),
            @FacesBehaviorEvent(name = "close", event = CloseEvent.class,
                        description = "Fires when the time picker panel is closed.")
})
public abstract class TimePickerBase extends AbstractPrimeHtmlInputText implements Widget, InputHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.TimePicker";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimePickerRenderer";

    public TimePickerBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Separator between hours and minutes.", defaultValue = ":")
    public abstract String getTimeSeparator();

    @Property(description = "Use 12-hour AM/PM period.", defaultValue = "false")
    public abstract boolean isShowPeriod();

    @Property(description = "Display mode: popup, spinner, or inline.", defaultValue = "spinner")
    public abstract String getMode();

    @Property(description = "Position of the dialog.", defaultValue = "left top")
    public abstract String getDialogPosition();

    @Property(description = "Position of the input relative to dialog.", defaultValue = "left bottom")
    public abstract String getInputPosition();

    @Property(description = "First hour in range.", defaultValue = "0")
    public abstract int getStartHours();

    @Property(description = "Last hour in range.", defaultValue = "23")
    public abstract int getEndHours();

    @Property(description = "First minute in range.", defaultValue = "0")
    public abstract int getStartMinutes();

    @Property(description = "Last minute in range.", defaultValue = "55")
    public abstract int getEndMinutes();

    @Property(description = "Minute step interval.", defaultValue = "5")
    public abstract int getIntervalMinutes();

    @Property(description = "Number of rows in dropdown.", defaultValue = "4")
    public abstract int getRows();

    @Property(description = "Show hours column.", defaultValue = "true")
    public abstract boolean isShowHours();

    @Property(description = "Show minutes column.", defaultValue = "true")
    public abstract boolean isShowMinutes();

    @Property(description = "Show close button.", defaultValue = "false")
    public abstract boolean isShowCloseButton();

    @Property(description = "Show now button.", defaultValue = "false")
    public abstract boolean isShowNowButton();

    @Property(description = "Show deselect button.", defaultValue = "false")
    public abstract boolean isShowDeselectButton();

    @Property(description = "Client-side callback when hour list is shown.")
    public abstract String getOnHourShow();

    @Property(description = "Client-side callback when minute list is shown.")
    public abstract String getOnMinuteShow();

    @Property(description = "When to show picker: focus or button.", defaultValue = "focus")
    public abstract String getShowOn();

    @Property(description = "Locale for formatting (string or Locale).")
    public abstract Object getLocale();

    @Property(description = "Minimum hour.")
    public abstract Integer getMinHour();

    @Property(description = "Minimum minute.")
    public abstract Integer getMinMinute();

    @Property(description = "Maximum hour.")
    public abstract Integer getMaxHour();

    @Property(description = "Maximum minute.")
    public abstract Integer getMaxMinute();

    @Property(description = "Input is read-only.", defaultValue = "false")
    public abstract boolean isReadonlyInput();

    @Property(description = "Size of the input field.", defaultValue = "5")
    public abstract int getSize();
}
