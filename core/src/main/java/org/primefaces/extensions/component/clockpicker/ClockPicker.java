/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.util.Constants;
import org.primefaces.util.LocaleUtils;

@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "clockpicker/clockpicker.css")
@ResourceDependency(library = Constants.LIBRARY, name = "clockpicker/clockpicker.js")
public class ClockPicker extends AbstractPrimeHtmlInputText implements Widget, InputHolder {
    public static final String CONTAINER_CLASS = "pe-clockpicker ui-widget ui-corner-all clockpicker";
    public static final String BUTTON_TRIGGER_CLASS = "pe-clockpicker-trigger ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only input-group-addon";
    public static final String BUTTON_TRIGGER_ICON_CLASS = "ui-button-icon-left ui-icon ui-icon-clock";
    public static final String BUTTON_TRIGGER_TEXT_CLASS = "ui-button-text";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ClockPicker";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ClockPickerRenderer";

    private Locale appropriateLocale;

    protected enum PropertyKeys {

        //@formatter:off
        widgetVar,
        locale,
        placement,
        align,
        autoClose,
        vibrate,
        twelveHour,
        showOn,
        onbeforeshow,
        onaftershow,
        onbeforehide,
        onafterhide,
        onbeforehourselect,
        onafterhourselect,
        onafterampmselect,
        onbeforedone,
        onafterdone,
        appendTo
    }
    
    public ClockPicker() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getInputClientId() {
        return getClientId(getFacesContext()) + "_input";
    }

    @Override
    public String getValidatableInputClientId() {
        return getInputClientId();
    }

    @Override
    public String getLabelledBy() {
        return (String) getStateHelper().get("labelledby");
    }

    @Override
    public void setLabelledBy(String labelledBy) {
        getStateHelper().put("labelledby", labelledBy);
    }
    
    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }
    
    public String getPlacement() {
        return (String) getStateHelper().eval(PropertyKeys.placement, "bottom");
    }
    
    public void setPlacement(final String placement) {
        getStateHelper().put(PropertyKeys.placement, placement);
    }
    
    public String getAlign() {
        return (String) getStateHelper().eval(PropertyKeys.align, "left");
    }
    
    public void setAlign(final String align) {
        getStateHelper().put(PropertyKeys.align, align);
    }
    
    public Boolean isAutoClose() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoClose, false);
    }
    
    public void setAutoClose(final Boolean autoClose) {
        getStateHelper().put(PropertyKeys.autoClose, autoClose);
    }
    
    public Boolean getVibrate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.vibrate, true); 
    }
    
    public void setVibrate(final Boolean vibrate) {
        getStateHelper().put(PropertyKeys.vibrate, vibrate);
    }
    
    public Boolean isTwelveHour() {
        return (Boolean) getStateHelper().eval(PropertyKeys.twelveHour, false);
    }
    
    public void setTwelveHour(final Boolean twelveHour) {
        getStateHelper().put(PropertyKeys.twelveHour, twelveHour);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public String getShowOn() {
        return (String) getStateHelper().eval(PropertyKeys.showOn, "focus");
    }

    public void setShowOn(final String showOn) {
        getStateHelper().put(PropertyKeys.showOn, showOn);
    }

    public String getOnbeforeshow() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeshow, null);
    }

    public void setOnbeforeshow(final String beforeshow) {
        getStateHelper().put(PropertyKeys.onbeforeshow, beforeshow);
    }

    public String getOnaftershow() {
        return (String) getStateHelper().eval(PropertyKeys.onaftershow, null);
    }

    public void setOnaftershow(final String aftershow) {
        getStateHelper().put(PropertyKeys.onaftershow, aftershow);
    }

    public String getOnbeforehide() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforehide, null);
    }

    public void setOnbeforehide(final String beforehide) {
        getStateHelper().put(PropertyKeys.onbeforehide, beforehide);
    }

    public String getOnafterhide() {
        return (String) getStateHelper().eval(PropertyKeys.onafterhide, null);
    }

    public void setOnafterhide(final String afterhide) {
        getStateHelper().put(PropertyKeys.onafterhide, afterhide);
    }

    public String getOnbeforedone() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforedone, null);
    }

    public void setOnbeforedone(final String beforedone) {
        getStateHelper().put(PropertyKeys.onbeforedone, beforedone);
    }

    public String getOnafterdone() {
        return (String) getStateHelper().eval(PropertyKeys.onafterdone, null);
    }

    public void setOnafterdone(final String afterdone) {
        getStateHelper().put(PropertyKeys.onafterdone, afterdone);
    }

    public String getOnafterampmselect() {
        return (String) getStateHelper().eval(PropertyKeys.onafterampmselect, null);
    }

    public void setOnafterampmselect(final String afterampmselect) {
        getStateHelper().put(PropertyKeys.onafterampmselect, afterampmselect);
    }

    public String getOnafterhourselect() {
        return (String) getStateHelper().eval(PropertyKeys.onafterhourselect, null);
    }

    public void setOnafterhourselect(final String afterhourselect) {
        getStateHelper().put(PropertyKeys.onafterhourselect, afterhourselect);
    }

    public String getOnbeforehourselect() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforehourselect, null);
    }

    public void setOnbeforehourselect(final String beforehourselect) {
        getStateHelper().put(PropertyKeys.onbeforehourselect, beforehourselect);
    }

    public boolean isShowOnButton() {
        return !"focus".equals(getShowOn());
    }

    public String getAppendTo() {
        return (String) getStateHelper().eval(PropertyKeys.appendTo, "@(body)");
    }

    public void setAppendTo(String appendTo) {
        getStateHelper().put(PropertyKeys.appendTo, appendTo);
    }

    public Locale calculateLocale(FacesContext fc) {
        if (appropriateLocale == null) {
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        appropriateLocale = null;

        return super.saveState(context);
    }
}