/*
 * Copyright 2011 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.component.timepicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.LocaleUtils;

import org.primefaces.component.api.Widget;
import org.primefaces.util.ArrayUtils;
import org.primefaces.util.HTML;

/**
 * <code>TimePicker</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "primefaces.css"),
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "primefaces.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "timepicker/timepicker.css"),
                          @ResourceDependency(library = "primefaces-extensions", name = "timepicker/timepicker.js")
                      })
public class TimePicker extends HtmlInputText implements Widget {

	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimePickerRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	public static final String CONTAINER_CLASS = "ui-timepicker ui-widget ui-corner-all";

	//public final static String PLAIN_INPUT_CLASS = "ui-timepicker-input";
	public static final String THEME_INPUT_CLASS = "ui-timepicker-input ui-inputfield ui-state-default ui-corner-all";
	public static final String UP_BUTTON_CLASS =
	    "ui-timepicker-button ui-timepicker-up ui-corner-tr ui-button ui-widget ui-state-default ui-button-text-only";
	public static final String DOWN_BUTTON_CLASS =
	    "ui-timepicker-button ui-timepicker-down ui-corner-br ui-button ui-widget ui-state-default ui-button-text-only";
	public static final String UP_ICON_CLASS = "ui-icon ui-icon-triangle-1-n";
	public static final String DOWN_ICON_CLASS = "ui-icon ui-icon-triangle-1-s";

	public static final String[] INPUT_TEXT_ATTRS =
	    ArrayUtils.concat(new String[] {
	                          "accesskey", "alt", "autocomplete", "dir", "lang", "maxlength", "size", "tabindex", "title"
	                      }, HTML.COMMON_EVENTS, HTML.CHANGE_SELECT_EVENTS, HTML.BLUR_FOCUS_EVENTS);
	public static final String TIME_PATTERN_24 = "HH:mm";
	public static final String TIME_PATTERN_12 = "hh:mm a";

	private Locale appropriateLocale;

	/**
	 * PropertyKeys
	 *
	 * @author  oleg / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		myPosition,
		atPosition,
		showPeriod,
		mode /* 'popup', 'spinner', 'inline' */,
		startHours,
		endHours,
		startMinutes,
		endMinutes,
		intervalMinutes,
		rows,
		locale;

		/*
		showPeriodLabels, showHoursLeadingZero, showMinutesLeadingZero, timeSeparator, showHours, showMinutes,
		onSelectCallback, onCloseCallback, onHourShow, onMinuteShow, showOn // 'focus', 'button', 'both',
		 */

		String toString;

		PropertyKeys(final String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
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
		setAttribute(PropertyKeys.widgetVar, widgetVar);
	}

	public String getMode() {
		return (String) getStateHelper().eval(PropertyKeys.mode, "spinner");
	}

	public void setMode(final String mode) {
		setAttribute(PropertyKeys.mode, mode);
	}

	public String getMyPosition() {
		return (String) getStateHelper().eval(PropertyKeys.myPosition, "left top");
	}

	public void setMyPosition(final String myPosition) {
		setAttribute(PropertyKeys.myPosition, myPosition);
	}

	public String getAtPosition() {
		return (String) getStateHelper().eval(PropertyKeys.atPosition, "left bottom");
	}

	public void setAtPosition(final String atPosition) {
		setAttribute(PropertyKeys.atPosition, atPosition);
	}

	public boolean isShowPeriod() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showPeriod, false);
	}

	public void setShowPeriod(final boolean showPeriod) {
		setAttribute(PropertyKeys.showPeriod, showPeriod);
	}

	public int getStartHours() {
		return (Integer) getStateHelper().eval(PropertyKeys.startHours, 0);
	}

	public void setStartHours(final int startHours) {
		setAttribute(PropertyKeys.startHours, startHours);
	}

	public int getEndHours() {
		return (Integer) getStateHelper().eval(PropertyKeys.endHours, 23);
	}

	public void setEndHours(final int endHours) {
		setAttribute(PropertyKeys.endHours, endHours);
	}

	public int getStartMinutes() {
		return (Integer) getStateHelper().eval(PropertyKeys.startMinutes, 0);
	}

	public void setStartMinutes(final int startMinutes) {
		setAttribute(PropertyKeys.startMinutes, startMinutes);
	}

	public int getEndMinutes() {
		return (Integer) getStateHelper().eval(PropertyKeys.endMinutes, 55);
	}

	public void setEndMinutes(final int endMinutes) {
		setAttribute(PropertyKeys.endMinutes, endMinutes);
	}

	public int getIntervalMinutes() {
		return (Integer) getStateHelper().eval(PropertyKeys.intervalMinutes, 5);
	}

	public void setIntervalMinutes(final int intervalMinutes) {
		setAttribute(PropertyKeys.intervalMinutes, intervalMinutes);
	}

	public int getRows() {
		return (Integer) getStateHelper().eval(PropertyKeys.rows, 4);
	}

	public void setRows(final int rows) {
		setAttribute(PropertyKeys.rows, rows);
	}

	public Object getLocale() {
		return getStateHelper().eval(PropertyKeys.locale, null);
	}

	public void setLocale(final Object locale) {
		setAttribute(PropertyKeys.locale, locale);
	}

	/*
	public String getShowOn() {
	    return (String) getStateHelper().eval(PropertyKeys.showOn, "focus");
	}

	public void setShowOn(String showOn) {
	    getStateHelper().put(PropertyKeys.showOn, showOn);
	}

	public boolean isShowHours() {
	    return (Boolean) getStateHelper().eval(PropertyKeys.showHours, true);
	}

	public void setShowHours(boolean showHours) {
	    getStateHelper().put(PropertyKeys.showHours, showHours);
	}

	public boolean isShowMinutes() {
	    return (Boolean) getStateHelper().eval(PropertyKeys.showMinutes, true);
	}

	public void setShowMinutes(boolean showMinutes) {
	    getStateHelper().put(PropertyKeys.showMinutes, showMinutes);
	}

	public boolean isShowPeriodLabels() {
	    return (Boolean) getStateHelper().eval(PropertyKeys.showPeriodLabels, false);
	}

	public void setShowPeriodLabels(boolean showPeriodLabels) {
	    getStateHelper().put(PropertyKeys.showPeriodLabels, showPeriodLabels);
	}

	public boolean isShowHoursLeadingZero() {
	    return (Boolean) getStateHelper().eval(PropertyKeys.showHoursLeadingZero, true);
	}

	public void setShowHoursLeadingZero(boolean showHoursLeadingZero) {
	    getStateHelper().put(PropertyKeys.showHoursLeadingZero, showHoursLeadingZero);
	}

	public boolean isShowMinutesLeadingZero() {
	    return (Boolean) getStateHelper().eval(PropertyKeys.showMinutesLeadingZero, true);
	}

	public void setShowMinutesLeadingZero(boolean showMinutesLeadingZero) {
	    getStateHelper().put(PropertyKeys.showMinutesLeadingZero, showMinutesLeadingZero);
	}

	public String getTimeSeparator() {
	    return (String) getStateHelper().eval(PropertyKeys.timeSeparator, ":");
	}

	public void setTimeSeparator(String timeSeparator) {
	    getStateHelper().put(PropertyKeys.timeSeparator, timeSeparator);
	}

	public String getOnSelectCallback() {
	    return (String) getStateHelper().eval(PropertyKeys.onSelectCallback, null);
	}

	public void setOnSelectCallback(String onSelectCallback) {
	    getStateHelper().put(PropertyKeys.onSelectCallback, onSelectCallback);
	}

	public String getOnCloseCallback() {
	    return (String) getStateHelper().eval(PropertyKeys.onCloseCallback, null);
	}

	public void setOnCloseCallback(String onCloseCallback) {
	    getStateHelper().put(PropertyKeys.onCloseCallback, onCloseCallback);
	}

	public String getOnHourShow() {
	    return (String) getStateHelper().eval(PropertyKeys.onHourShow, null);
	}

	public void setOnHourShow(String onHourShow) {
	    getStateHelper().put(PropertyKeys.onHourShow, onHourShow);
	}

	public String getOnMinuteShow() {
	    return (String) getStateHelper().eval(PropertyKeys.onMinuteShow, null);
	}

	public void setOnMinuteShow(String onMinuteShow) {
	    getStateHelper().put(PropertyKeys.onMinuteShow, onMinuteShow);
	}
	 */

	public java.util.Locale calculateLocale(final FacesContext fc) {
		if (appropriateLocale == null) {
			Object userLocale = getLocale();
			if (userLocale != null) {
				if (userLocale instanceof String) {
					appropriateLocale = LocaleUtils.toLocale((String) userLocale);
				} else if (userLocale instanceof java.util.Locale) {
					appropriateLocale = (Locale) userLocale;
				} else {
					throw new IllegalArgumentException(userLocale.getClass() + " is not a valid locale for timepicker:"
					                                   + this.getClientId(fc));
				}
			} else {
				appropriateLocale = fc.getViewRoot().getLocale();
			}
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
	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}

	public void setAttribute(final PropertyKeys property, final Object value) {
		getStateHelper().put(property, value);

		List<String> setAttributes =
		    (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
			}
		}

		if (setAttributes != null && value == null) {
			final String attributeName = property.toString();
			final ValueExpression ve = getValueExpression(attributeName);
			if (ve == null) {
				setAttributes.remove(attributeName);
			} else if (!setAttributes.contains(attributeName)) {
				setAttributes.add(attributeName);
			}
		}
	}
}
