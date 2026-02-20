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
package org.primefaces.extensions.component.counter;

import java.util.Locale;
import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.util.Constants;
import org.primefaces.util.LocaleUtils;

/**
 * <code>Counter</code> component.
 *
 * @author https://github.com/aripddev
 * @since 8.0.1
 */
@FacesComponent(value = Counter.COMPONENT_TYPE, namespace = Counter.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Counter animates a number from a start value to an end value.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "components.js")
@ResourceDependency(library = Constants.LIBRARY, name = "counter/counter.js")
public class Counter extends CounterBaseImpl {

    public static final String STYLE_CLASS = "ui-counter";

    private Locale appropriateLocale;

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String clientId = getClientId(context);

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.start) || isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.end)) {
                final Double value = Double.parseDouble(params.get(clientId + "_value"));
                final SelectEvent<Double> selectEvent = new SelectEvent<>(this, behaviorEvent.getBehavior(), value);
                selectEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(selectEvent);
                return;
            }
        }
        super.queueEvent(event);
    }

    @Override
    public Object saveState(final FacesContext context) {
        appropriateLocale = null;
        return super.saveState(context);
    }
}
