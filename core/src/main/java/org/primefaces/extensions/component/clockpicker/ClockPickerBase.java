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
package org.primefaces.extensions.component.clockpicker;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.Widget;

/**
 * <code>ClockPicker</code> component base class.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@FacesComponentBase
public abstract class ClockPickerBase extends AbstractPrimeHtmlInputText implements Widget, InputHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ClockPicker";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ClockPickerRenderer";

    public ClockPickerBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Locale for time formatting. Can be a string or java.util.Locale instance.")
    public abstract Object getLocale();

    @Property(description = "Position of the popup: 'bottom', 'top', 'left', 'right'.", defaultValue = "bottom")
    public abstract String getPlacement();

    @Property(description = "Alignment of the popup: 'left', 'right', 'top', 'bottom'.", defaultValue = "left")
    public abstract String getAlign();

    @Property(description = "Whether to close the popup after selecting time.", defaultValue = "false")
    public abstract boolean isAutoClose();

    @Property(description = "Whether to vibrate on selection (mobile).", defaultValue = "true")
    public abstract boolean isVibrate();

    @Property(description = "Use 12-hour format with AM/PM.", defaultValue = "false")
    public abstract boolean isTwelveHour();

    @Property(description = "When to show the picker: 'focus' or 'button'.", defaultValue = "focus")
    public abstract String getShowOn();

    @Property(description = "Client-side callback before the picker is shown.")
    public abstract String getOnbeforeshow();

    @Property(description = "Client-side callback after the picker is shown.")
    public abstract String getOnaftershow();

    @Property(description = "Client-side callback before the picker is hidden.")
    public abstract String getOnbeforehide();

    @Property(description = "Client-side callback after the picker is hidden.")
    public abstract String getOnafterhide();

    @Property(description = "Client-side callback before hour is selected.")
    public abstract String getOnbeforehourselect();

    @Property(description = "Client-side callback after hour is selected.")
    public abstract String getOnafterhourselect();

    @Property(description = "Client-side callback after AM/PM is selected.")
    public abstract String getOnafterampmselect();

    @Property(description = "Client-side callback before done is applied.")
    public abstract String getOnbeforedone();

    @Property(description = "Client-side callback after done is applied.")
    public abstract String getOnafterdone();

    @Property(description = "Search expression for the element to append the popup to.", defaultValue = "@(body)")
    public abstract String getAppendTo();

}
