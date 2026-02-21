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
package org.primefaces.extensions.component.gchart;

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.util.Constants;
import org.primefaces.extensions.util.json.GsonConverter;

import com.google.gson.JsonArray;

/**
 * <code>GChart</code> component (Google Charts).
 */
@FacesComponent(value = GChart.COMPONENT_TYPE, namespace = GChart.COMPONENT_FAMILY)
@FacesComponentInfo(description = "GChart is a Google Charts visualization component.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "gchart/gchart.css")
@ResourceDependency(library = Constants.LIBRARY, name = "gchart/gchart.js")
public class GChart extends GChartBaseImpl {

    /** Init parameter name for Google Maps/Charts API key. */
    public static final String API_KEY = "primefaces.GOOGLE_MAPS_API_KEY";

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.select)) {
                final String clientId = getClientId(context);
                final Object value = GsonConverter.getGson().fromJson(params.get(clientId + "_hidden"),
                            JsonArray.class);
                final SelectEvent<Object> selectEvent = new SelectEvent<>(this, behaviorEvent.getBehavior(), value);
                selectEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
                return;
            }
        }
        super.queueEvent(event);
    }
}
