/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.BeforeShowEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.TimeSelectEvent;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;
import org.primefaces.util.LocaleUtils;

/**
 * <code>TimePicker</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "timepicker/timepicker.css")
@ResourceDependency(library = "primefaces-extensions", name = "timepicker/timepicker.js")
public class TimePicker extends AbstractPrimeHtmlInputText implements Widget {

    public static final String CONTAINER_CLASS = "pe-timepicker ui-widget ui-corner-all";
    public static final String INPUT_CLASS = "ui-inputfield ui-state-default ui-corner-all";
    public static final String UP_BUTTON_CLASS = "ui-spinner-button ui-spinner-up ui-corner-tr ui-button ui-widget ui-state-default";
    public static final String DOWN_BUTTON_CLASS = "ui-spinner-button ui-spinner-down ui-corner-br ui-button ui-widget ui-state-default";
    public static final String UP_ICON_CLASS = "ui-icon ui-icon-triangle-1-n";
    public static final String DOWN_ICON_CLASS = "ui-icon ui-icon-triangle-1-s";
    public static final String BUTTON_TRIGGER_CLASS = "pe-timepicker-trigger ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only";
    public static final String BUTTON_TRIGGER_ICON_CLASS = "ui-button-icon-left ui-icon ui-icon-clock";
    public static final String BUTTON_TRIGGER_TEXT_CLASS = "ui-button-text";

    public static final String TIME_MESSAGE_KEY = "javax.faces.converter.DateTimeConverter.TIME";
    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.TimePicker";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimePickerRenderer";

    static final Collection<String> EVENT_NAMES = LangUtils
                .unmodifiableList("blur", "change", "valueChange", "select", "click", "dblclick", "focus", "keydown", "keypress", "keyup", "mousedown",
                            "mousemove", "mouseout", "mouseover", "mouseup", "wheel", "cut", "copy", "paste", "contextmenu", "input", "invalid", "reset",
                            "search", "drag", "dragend", "dragenter", "dragleave", "dragover", "dragstart", "drop", "scroll", BeforeShowEvent.NAME,
                            TimeSelectEvent.NAME, CloseEvent.NAME);

    private final Map<String, AjaxBehaviorEvent> customEvents = new HashMap<>();
    private Locale appropriateLocale;

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        //@formatter:off
        widgetVar,
        timeSeparator,
        showPeriod,
        dialogPosition,
        inputPosition,
        mode /* 'popup', 'spinner', 'inline' */,
        startHours,
        endHours,
        startMinutes,
        endMinutes,
        intervalMinutes,
        rows,
        showHours,
        showMinutes,
        showCloseButton,
        showNowButton,
        showDeselectButton,
        onHourShow,
        onMinuteShow,
        showOn,
        locale,
        minHour,
        minMinute,
        maxHour,
        maxMinute,
        readonlyInput,
        size
        //@formatter:on
    }

    public TimePicker() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getTimeSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.timeSeparator, ":");
    }

    public void setTimeSeparator(final String timeSeparator) {
        getStateHelper().put(PropertyKeys.timeSeparator, timeSeparator);
    }

    public boolean isShowPeriod() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showPeriod, false);
    }

    public void setShowPeriod(final boolean showPeriod) {
        getStateHelper().put(PropertyKeys.showPeriod, showPeriod);
    }

    public String getMode() {
        return (String) getStateHelper().eval(PropertyKeys.mode, "spinner");
    }

    public void setMode(final String mode) {
        getStateHelper().put(PropertyKeys.mode, mode);
    }

    public String getDialogPosition() {
        return (String) getStateHelper().eval(PropertyKeys.dialogPosition, "left top");
    }

    public void setDialogPosition(final String dialogPosition) {
        getStateHelper().put(PropertyKeys.dialogPosition, dialogPosition);
    }

    public String getInputPosition() {
        return (String) getStateHelper().eval(PropertyKeys.inputPosition, "left bottom");
    }

    public void setInputPosition(final String inputPosition) {
        getStateHelper().put(PropertyKeys.inputPosition, inputPosition);
    }

    public int getStartHours() {
        return (Integer) getStateHelper().eval(PropertyKeys.startHours, 0);
    }

    public void setStartHours(final int startHours) {
        getStateHelper().put(PropertyKeys.startHours, startHours);
    }

    public int getEndHours() {
        return (Integer) getStateHelper().eval(PropertyKeys.endHours, 23);
    }

    public void setEndHours(final int endHours) {
        getStateHelper().put(PropertyKeys.endHours, endHours);
    }

    public int getStartMinutes() {
        return (Integer) getStateHelper().eval(PropertyKeys.startMinutes, 0);
    }

    public void setStartMinutes(final int startMinutes) {
        getStateHelper().put(PropertyKeys.startMinutes, startMinutes);
    }

    public int getEndMinutes() {
        return (Integer) getStateHelper().eval(PropertyKeys.endMinutes, 55);
    }

    public void setEndMinutes(final int endMinutes) {
        getStateHelper().put(PropertyKeys.endMinutes, endMinutes);
    }

    public int getIntervalMinutes() {
        return (Integer) getStateHelper().eval(PropertyKeys.intervalMinutes, 5);
    }

    public void setIntervalMinutes(final int intervalMinutes) {
        getStateHelper().put(PropertyKeys.intervalMinutes, intervalMinutes);
    }

    public int getRows() {
        return (Integer) getStateHelper().eval(PropertyKeys.rows, 4);
    }

    public void setRows(final int rows) {
        getStateHelper().put(PropertyKeys.rows, rows);
    }

    public boolean isShowHours() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showHours, true);
    }

    public void setShowHours(final boolean showHours) {
        getStateHelper().put(PropertyKeys.showHours, showHours);
    }

    public boolean isShowMinutes() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showMinutes, true);
    }

    public void setShowMinutes(final boolean showMinutes) {
        getStateHelper().put(PropertyKeys.showMinutes, showMinutes);
    }

    public boolean isShowCloseButton() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showCloseButton, false);
    }

    public void setShowCloseButton(final boolean showCloseButton) {
        getStateHelper().put(PropertyKeys.showCloseButton, showCloseButton);
    }

    public boolean isShowDeselectButton() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showDeselectButton, false);
    }

    public void setShowDeselectButton(final boolean showDeselectButton) {
        getStateHelper().put(PropertyKeys.showDeselectButton, showDeselectButton);
    }

    public boolean isShowNowButton() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showNowButton, false);
    }

    public void setShowNowButton(final boolean showNowButton) {
        getStateHelper().put(PropertyKeys.showNowButton, showNowButton);
    }

    public String getOnHourShow() {
        return (String) getStateHelper().eval(PropertyKeys.onHourShow, null);
    }

    public void setOnHourShow(final String onHourShow) {
        getStateHelper().put(PropertyKeys.onHourShow, onHourShow);
    }

    public String getOnMinuteShow() {
        return (String) getStateHelper().eval(PropertyKeys.onMinuteShow, null);
    }

    public void setOnMinuteShow(final String onMinuteShow) {
        getStateHelper().put(PropertyKeys.onMinuteShow, onMinuteShow);
    }

    public String getShowOn() {
        return (String) getStateHelper().eval(PropertyKeys.showOn, "focus");
    }

    public void setShowOn(final String showOn) {
        getStateHelper().put(PropertyKeys.showOn, showOn);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public Integer getMinHour() {
        return (Integer) getStateHelper().eval(PropertyKeys.minHour, null);
    }

    public void setMinHour(final Integer minHour) {
        getStateHelper().put(PropertyKeys.minHour, minHour);
    }

    public Integer getMinMinute() {
        return (Integer) getStateHelper().eval(PropertyKeys.minMinute, null);
    }

    public void setMinMinute(final Integer minMinute) {
        getStateHelper().put(PropertyKeys.minMinute, minMinute);
    }

    public Integer getMaxHour() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxHour, null);
    }

    public void setMaxHour(final Integer maxHour) {
        getStateHelper().put(PropertyKeys.maxHour, maxHour);
    }

    public Integer getMaxMinute() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxMinute, null);
    }

    public void setMaxMinute(final Integer maxMinute) {
        getStateHelper().put(PropertyKeys.maxMinute, maxMinute);
    }

    public boolean isReadonlyInput() {
        return (Boolean) getStateHelper().eval(PropertyKeys.readonlyInput, false);
    }

    public void setReadonlyInput(final boolean _readonlyInput) {
        getStateHelper().put(PropertyKeys.readonlyInput, _readonlyInput);
    }

    @Override
    public int getSize() {
        return (Integer) getStateHelper().eval(PropertyKeys.size, 5);
    }

    @Override
    public void setSize(final int size) {
        getStateHelper().put(PropertyKeys.size, size);
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

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final String eventName = fc.getExternalContext().getRequestParameterMap()
                    .get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (isSelfRequest(fc) && event instanceof AjaxBehaviorEvent) {
            if (TimeSelectEvent.NAME.equals(eventName)) {
                customEvents.put(TimeSelectEvent.NAME, (AjaxBehaviorEvent) event);

                return;
            }
            else if (BeforeShowEvent.NAME.equals(eventName)) {
                final BeforeShowEvent beforeShowEvent = new BeforeShowEvent(this,
                            ((AjaxBehaviorEvent) event).getBehavior());
                beforeShowEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(beforeShowEvent);

                return;
            }
            else if (CloseEvent.NAME.equals(eventName)) {
                final CloseEvent closeEvent = new CloseEvent(this, ((AjaxBehaviorEvent) event).getBehavior());
                closeEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(closeEvent);

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
                final TimeSelectEvent<Object> timeSelectEvent = new TimeSelectEvent<>(this, behaviorEvent.getBehavior(), getValue());

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

    public String getTimePattern24() {
        return "HH" + getTimeSeparator() + "mm";
    }

    public String getTimePattern12() {
        return "hh" + getTimeSeparator() + "mm a";
    }

    private boolean isSelfRequest(final FacesContext fc) {
        return getClientId(fc).equals(
                    fc.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        appropriateLocale = null;
        customEvents.clear();

        return super.saveState(context);
    }
}
