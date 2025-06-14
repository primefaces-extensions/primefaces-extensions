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
package org.primefaces.extensions.component.inputplace;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.MixedClientBehaviorHolder;
import org.primefaces.component.api.RTLAware;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.inputplace.Place;
import org.primefaces.extensions.util.Constants;
import org.primefaces.util.LangUtils;

@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "inputplace/inputplace.css")
@ResourceDependency(library = Constants.LIBRARY, name = "inputplace/inputplace.js")
public class InputPlace extends AbstractPrimeHtmlInputText implements Widget, MixedClientBehaviorHolder, RTLAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputPlace";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputPlaceRenderer";
    public static final String STYLE_CLASS = "ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all";
    public static final String EVENT_PLACE_CHANGED = "placeChanged";
    private static final List<String> UNOBSTRUSIVE_EVENT_NAMES = LangUtils.unmodifiableList(EVENT_PLACE_CHANGED);
    private static final Collection<String> EVENT_NAMES = LangUtils.concat(AbstractPrimeHtmlInputText.EVENT_NAMES, UNOBSTRUSIVE_EVENT_NAMES);

    // @formatter:off
    @SuppressWarnings("java:S115")
    public enum PropertyKeys {
        apiType,
        apiKey,
        placeholder,
        restrictCountries,
        restrictTypes,
        onplacechanged,
        widgetVar
    }
    // @formatter:on

    public InputPlace() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getDefaultEventName() {
        return EVENT_PLACE_CHANGED;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public Collection<String> getUnobstrusiveEventNames() {
        return UNOBSTRUSIVE_EVENT_NAMES;
    }

    public String getPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.placeholder, null);
    }

    public void setPlaceholder(String placeholder) {
        getStateHelper().put(PropertyKeys.placeholder, placeholder);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getRestrictCountries() {
        return (String) getStateHelper().eval(PropertyKeys.restrictCountries, null);
    }

    public void setRestrictCountries(String restrictCountries) {
        getStateHelper().put(PropertyKeys.restrictCountries, restrictCountries);
    }

    public String getRestrictTypes() {
        return (String) getStateHelper().eval(PropertyKeys.restrictTypes, null);
    }

    public void setRestrictTypes(String restrictTypes) {
        getStateHelper().put(PropertyKeys.restrictTypes, restrictTypes);
    }

    public String getApiType() {
        return (String) getStateHelper().eval(PropertyKeys.apiType, "google");
    }

    public void setApiType(String apiType) {
        getStateHelper().put(PropertyKeys.apiType, apiType);
    }

    public String getApiKey() {
        return (String) getStateHelper().eval(PropertyKeys.apiKey, null);
    }

    public void setApiKey(String apiKey) {
        getStateHelper().put(PropertyKeys.apiKey, apiKey);
    }

    public String getOnplacechanged() {
        return (String) getStateHelper().eval(PropertyKeys.onplacechanged, null);
    }

    public void setOnplacechanged(final String _onplacechanged) {
        getStateHelper().put(PropertyKeys.onplacechanged, _onplacechanged);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(org.primefaces.util.Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && event instanceof AjaxBehaviorEvent) {
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (EVENT_PLACE_CHANGED.equals(eventName)) {
                final Place selectedPlace = new Place(getClientId(context), params);
                final SelectEvent<Place> selectEvent = new SelectEvent<>(this, ajaxBehaviorEvent.getBehavior(), selectedPlace);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
            }
            else {
                // e.g. blur, focus, change
                super.queueEvent(event);
            }
        }
        else {
            // e.g. valueChange
            super.queueEvent(event);
        }
    }
}