/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
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
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;
import org.primefaces.util.Constants;
import org.primefaces.util.LocaleUtils;

@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "clockpicker/clockpicker.css")
@ResourceDependency(library = "primefaces-extensions", name = "clockpicker/clockpicker.js")
public class ClockPicker extends HtmlInputText implements Widget {
    public static final String CONTAINER_CLASS = "pe-clockpicker ui-widget ui-corner-all input-group clockpicker";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ClockPicker";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ClockPickerRenderer";

    private Locale appropriateLocale;

    protected enum PropertyKeys {

        //@formatter:off
        widgetVar,
        placement,
        align,
        donetext,
        autoclose,
        locale,
        vibrate;

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
    
    public ClockPicker() {
        setRendererType(DEFAULT_RENDERER);
    }
    
    
    private boolean isSelfRequest(final FacesContext fc) {
        return this.getClientId(fc).equals(
                    fc.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
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
    
    public String getDonetext() {
        return (String) getStateHelper().eval(PropertyKeys.donetext, "Done");
    }
    
    public void setDonetext(final String donetext) {
        getStateHelper().put(PropertyKeys.donetext, donetext);
    }
    
    public Boolean getAutoclose() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoclose, false);
    }
    
    public void setAutoclose(final Boolean autoclose) {
        getStateHelper().put(PropertyKeys.autoclose, autoclose);
    }
    
    public Boolean getVibrate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.vibrate, true); 
    }
    
    public void setVibrate(final Boolean vibrate) {
        getStateHelper().put(PropertyKeys.vibrate, vibrate);
    }
    
    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }
    
    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }
}