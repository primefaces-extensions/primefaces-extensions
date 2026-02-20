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
package org.primefaces.extensions.component.inputplace;

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.inputplace.Place;
import org.primefaces.extensions.util.Constants;

/**
 * <code>InputPlace</code> component.
 *
 * @since 14.0.0
 */
@FacesComponent(value = InputPlace.COMPONENT_TYPE, namespace = InputPlace.COMPONENT_FAMILY)
@FacesComponentInfo(description = "InputPlace is an address/place autocomplete input using Google or similar APIs.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "inputplace/inputplace.css")
@ResourceDependency(library = Constants.LIBRARY, name = "inputplace/inputplace.js")
public class InputPlace extends InputPlaceBaseImpl {

    public static final String STYLE_CLASS = "ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all";
    /** @deprecated Use {@link InputPlaceBaseImpl.ClientBehaviorEventKeys#placeChanged} */
    @Deprecated
    public static final String EVENT_PLACE_CHANGED = "placeChanged";

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.placeChanged)) {
                final Place selectedPlace = new Place(getClientId(context), params);
                final SelectEvent<Place> selectEvent = new SelectEvent<>(this, ajaxBehaviorEvent.getBehavior(), selectedPlace);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
                return;
            }
            else {
                // blur/focus/change events
                super.queueEvent(event);
                return;
            }
        }
        super.queueEvent(event);
    }
}
