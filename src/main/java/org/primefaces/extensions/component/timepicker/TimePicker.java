/**
 * Copyright 2011-2019 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.component.timepicker;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.BeforeShowEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.TimeSelectEvent;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.LocaleUtils;

/**
 * <code>TimePicker</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "components.css"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "timepicker/timepicker.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "timepicker/timepicker.js")
})
public class TimePicker extends HtmlInputText implements Widget {

    public static final String CONTAINER_CLASS = "pe-timepicker ui-widget ui-corner-all";
    public static final String INPUT_CLASS = "ui-inputfield pe-timepicker-input ui-state-default ui-corner-all";
    public static final String UP_BUTTON_CLASS = "pe-timepicker-button pe-timepicker-up ui-corner-tr ui-button ui-widget ui-state-default ui-button-text-only";
    public static final String DOWN_BUTTON_CLASS = "pe-timepicker-button pe-timepicker-down ui-corner-br ui-button ui-widget ui-state-default "
                + "ui-button-text-only";
    public static final String UP_ICON_CLASS = "ui-icon ui-icon-triangle-1-n";
    public static final String DOWN_ICON_CLASS = "ui-icon ui-icon-triangle-1-s";
    public static final String BUTTON_TRIGGER_CLASS = "pe-timepicker-trigger ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only";
    public static final String BUTTON_TRIGGER_ICON_CLASS = "ui-button-icon-left ui-icon ui-icon-clock";
    public static final String BUTTON_TRIGGER_TEXT_CLASS = "ui-button-text";

    public static final String TIME_MESSAGE_KEY = "javax.faces.converter.DateTimeConverter.TIME";
    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.TimePicker";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimePickerRenderer";

    static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(
                Arrays.asList("blur", "change", "valueChange", "click", "dblclick", "focus", "keydown",
                            "keypress", "keyup", "mousedown", "mousemove", "mouseout", "mouseover",
                            "mouseup", BeforeShowEvent.NAME, TimeSelectEvent.NAME, CloseEvent.NAME));

    private final Map<String, AjaxBehaviorEvent> customEvents = new HashMap<>();
    private Locale appropriateLocale;

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
        readonlyInput;
        //@formatter:on

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
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

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            appropriateLocale = LocaleUtils.resolveLocale(getLocale(), getClientId(FacesContext.getCurrentInstance()));
        }
        return appropriateLocale;
    }

    public boolean isInline() {
        return getMode().equalsIgnoreCase("inline");
    }

    public boolean isSpinner() {
        return getMode().equalsIgnoreCase("spinner");
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
                final TimeSelectEvent timeSelectEvent = new TimeSelectEvent(this, behaviorEvent.getBehavior(),
                            (Date) getValue());

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
        return this.getClientId(fc).equals(
                    fc.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }
}
