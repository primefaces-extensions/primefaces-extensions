/**
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.component.gchart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.util.json.GsonConverter;
import org.primefaces.util.Constants;

import com.google.gson.JsonArray;

@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
public class GChart extends UIOutput implements Widget, ClientBehaviorHolder {

    public final static String API_KEY = "primefaces.GOOGLE_MAPS_API_KEY";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.GChart";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.GChartRenderer";

    private static final String DEFAULT_TYPE = "select";
    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(DEFAULT_TYPE));

    protected enum PropertyKeys {
        widgetVar, width, height, title, apiKey, language, extender;
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
        return DEFAULT_TYPE;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public Integer getWidth() {
        return (Integer) this.getStateHelper().eval(PropertyKeys.width, null);
    }

    public void setWidth(final Integer width) {
        this.getStateHelper().put(PropertyKeys.width, width);
    }

    public Integer getHeight() {
        return (Integer) this.getStateHelper().eval(PropertyKeys.height, null);
    }

    public void setHeight(final Integer width) {
        this.getStateHelper().put(PropertyKeys.height, width);
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

            if (eventName.equals("select")) {
                final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
                final String clientId = this.getClientId(context);

                final Object value = GsonConverter.getGson().fromJson(params.get(clientId + "_hidden").toString(),
                            JsonArray.class);

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

        return this.getClientId(context).equals(partialSource);
    }
}
