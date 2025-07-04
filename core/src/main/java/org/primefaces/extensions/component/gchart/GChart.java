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
package org.primefaces.extensions.component.gchart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIOutput;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.util.json.GsonConverter;
import org.primefaces.util.Constants;

@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "gchart/gchart.css")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "gchart/gchart.js")
public class GChart extends UIOutput implements Widget, ClientBehaviorHolder {

    public static final String API_KEY = "primefaces.GOOGLE_MAPS_API_KEY";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.GChart";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.GChartRenderer";

    private static final String DEFAULT_EVENT = "select";
    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(DEFAULT_EVENT));

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        widgetVar, width, height, title, apiKey, language, extender
    }

    public GChart() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return DEFAULT_EVENT;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public Integer getWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.width, null);
    }

    public void setWidth(final Integer width) {
        getStateHelper().put(PropertyKeys.width, width);
    }

    public Integer getHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.height, null);
    }

    public void setHeight(final Integer width) {
        getStateHelper().put(PropertyKeys.height, width);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, null);
    }

    public void setTitle(final String title) {
        getStateHelper().put(PropertyKeys.title, title);
    }

    public String getApiKey() {
        return (String) getStateHelper().eval(PropertyKeys.apiKey, null);
    }

    public void setApiKey(final String apiKey) {
        getStateHelper().put(PropertyKeys.apiKey, apiKey);
    }

    public String getLanguage() {
        return (String) getStateHelper().eval(PropertyKeys.language, "en");
    }

    public void setLanguage(final String language) {
        getStateHelper().put(PropertyKeys.language, language);
    }

    public String getExtender() {
        return (String) getStateHelper().eval(PropertyKeys.extender, null);
    }

    public void setExtender(final String extender) {
        getStateHelper().put(PropertyKeys.extender, extender);
    }

    @Override
    public void queueEvent(final FacesEvent event) {

        final FacesContext context = getFacesContext();
        if (isRequestSource(context) && event instanceof AjaxBehaviorEvent) {
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

            if (eventName.equals(DEFAULT_EVENT)) {
                final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
                final String clientId = getClientId(context);

                final Object value = GsonConverter.getGson().fromJson(params.get(clientId + "_hidden"),
                            com.google.gson.JsonArray.class);

                final SelectEvent selectEvent = new SelectEvent(this, behaviorEvent.getBehavior(), value);
                selectEvent.setPhaseId(behaviorEvent.getPhaseId());

                super.queueEvent(selectEvent);
            }
        }
        else {
            super.queueEvent(event);
        }
    }

    public boolean isRequestSource(final FacesContext context) {
        final String partialSource = context.getExternalContext().getRequestParameterMap()
                    .get(Constants.RequestParams.PARTIAL_SOURCE_PARAM);

        return getClientId(context).equals(partialSource);
    }
}