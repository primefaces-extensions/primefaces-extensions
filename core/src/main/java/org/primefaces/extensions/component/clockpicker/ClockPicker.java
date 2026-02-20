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

import java.util.Locale;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.util.Constants;
import org.primefaces.util.LocaleUtils;

/**
 * <code>ClockPicker</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@FacesComponent(value = ClockPicker.COMPONENT_TYPE, namespace = ClockPicker.COMPONENT_FAMILY)
@FacesComponentInfo(description = "ClockPicker is a time picker component with clock-style UI.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "clockpicker/clockpicker.css")
@ResourceDependency(library = Constants.LIBRARY, name = "clockpicker/clockpicker.js")
public class ClockPicker extends ClockPickerBaseImpl {

    public static final String CONTAINER_CLASS = "pe-clockpicker ui-widget ui-corner-all clockpicker";
    public static final String BUTTON_TRIGGER_CLASS = "pe-clockpicker-trigger ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only input-group-addon";
    public static final String BUTTON_TRIGGER_ICON_CLASS = "ui-button-icon-left ui-icon ui-icon-clock";
    public static final String BUTTON_TRIGGER_TEXT_CLASS = "ui-button-text";

    private Locale appropriateLocale;

    @Override
    public String getInputClientId() {
        return getClientId(getFacesContext()) + "_input";
    }

    @Override
    public String getValidatableInputClientId() {
        return getInputClientId();
    }

    public boolean isShowOnButton() {
        return !"focus".equals(getShowOn());
    }

    public Locale calculateLocale(final FacesContext fc) {
        if (appropriateLocale == null) {
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    @Override
    public Object saveState(final FacesContext context) {
        appropriateLocale = null;
        return super.saveState(context);
    }
}
