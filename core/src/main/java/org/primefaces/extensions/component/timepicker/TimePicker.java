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

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.PhaseId;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.BeforeShowEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.TimeSelectEvent;
import org.primefaces.util.LocaleUtils;

/**
 * <code>TimePicker</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.3
 */
@FacesComponent(value = TimePicker.COMPONENT_TYPE, namespace = TimePicker.COMPONENT_FAMILY)
@FacesComponentInfo(description = "TimePicker is a time selection input with popup, spinner or inline mode.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "timepicker/timepicker.css")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "timepicker/timepicker.js")
public class TimePicker extends TimePickerBaseImpl {

    public static final String CONTAINER_CLASS = "pe-timepicker ui-widget ui-corner-all";
    public static final String INPUT_CLASS = "ui-inputfield ui-state-default ui-corner-all";
    public static final String UP_BUTTON_CLASS = "ui-spinner-button ui-spinner-up ui-corner-tr ui-button ui-widget ui-state-default";
    public static final String DOWN_BUTTON_CLASS = "ui-spinner-button ui-spinner-down ui-corner-br ui-button ui-widget ui-state-default";
    public static final String UP_ICON_CLASS = "ui-icon ui-icon-triangle-1-n";
    public static final String DOWN_ICON_CLASS = "ui-icon ui-icon-triangle-1-s";
    public static final String BUTTON_TRIGGER_CLASS = "pe-timepicker-trigger ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only";
    public static final String BUTTON_TRIGGER_ICON_CLASS = "ui-button-icon-left ui-icon ui-icon-clock";
    public static final String BUTTON_TRIGGER_TEXT_CLASS = "ui-button-text";

    public static final String TIME_MESSAGE_KEY = "jakarta.faces.converter.DateTimeConverter.TIME";

    private final Map<String, AjaxBehaviorEvent> customEvents = new java.util.HashMap<>();
    private Locale appropriateLocale;

    @Override
    public String getInputClientId() {
        return getClientId(getFacesContext()) + "_input";
    }

    @Override
    public String getValidatableInputClientId() {
        return getInputClientId();
    }

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    public boolean isInline() {
        return getMode().equalsIgnoreCase("inline");
    }

    public boolean isSpinner() {
        return getMode().equalsIgnoreCase("spinner");
    }

    public boolean isShowOnButton() {
        return !"focus".equals(getShowOn());
    }

    public String getTimePatternWithoutHours() {
        return "mm";
    }

    public String getTimePatternWithoutMinutes() {
        return isShowPeriod() ? "hh" : "HH";
    }

    public String getTimePattern24() {
        return "HH" + getTimeSeparator() + "mm";
    }

    public String getTimePattern12() {
        return "hh" + getTimeSeparator() + "mm a";
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.timeSelect)) {
                customEvents.put(TimeSelectEvent.NAME, (AjaxBehaviorEvent) event);
                return;
            }
            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.beforeShow)) {
                final BeforeShowEvent beforeShowEvent = new BeforeShowEvent(this,
                            ((AjaxBehaviorEvent) event).getBehavior());
                beforeShowEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(beforeShowEvent);
                return;
            }
            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.close)) {
                final CloseEvent closeEvent = new CloseEvent(this, ((AjaxBehaviorEvent) event).getBehavior());
                closeEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(closeEvent);
                return;
            }
            else {
                // blur/focus/change events
                super.queueEvent(event);
                return;
            }
        }

        super.queueEvent(event);
    }

    @Override
    public void validate(final FacesContext fc) {
        super.validate(fc);

        if (isValid()) {
            for (final Entry<String, AjaxBehaviorEvent> entry : customEvents.entrySet()) {
                final AjaxBehaviorEvent behaviorEvent = entry.getValue();
                final TimeSelectEvent<Object> timeSelectEvent = new TimeSelectEvent<>(this, behaviorEvent.getBehavior(),
                            getValue());

                if (behaviorEvent.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES)) {
                    timeSelectEvent.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
                }
                else {
                    timeSelectEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
                }

                super.queueEvent(timeSelectEvent);
            }
        }
    }

    @Override
    public Object saveState(final FacesContext context) {
        appropriateLocale = null;
        customEvents.clear();
        return super.saveState(context);
    }
}
