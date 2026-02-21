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
package org.primefaces.extensions.component.waypoint;

import java.util.Locale;
import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.WaypointEvent;
import org.primefaces.extensions.util.Constants;

/**
 * <code>Waypoint</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.6
 */
@FacesComponent(value = Waypoint.COMPONENT_TYPE, namespace = Waypoint.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Waypoint makes it easy to execute a custom logic whenever you scroll to an element.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "waypoint/waypoint.js")
public class Waypoint extends WaypointBaseImpl {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Waypoint";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    @Override
    public void processDecodes(final FacesContext fc) {
        if (isAjaxRequestSource(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    @Override
    public void processValidators(final FacesContext fc) {
        if (!isAjaxRequestSource(fc)) {
            super.processValidators(fc);
        }
    }

    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isAjaxRequestSource(fc)) {
            super.processUpdates(fc);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext fc = event.getFacesContext();
            final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            final String clientId = getClientId(fc);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.reached)) {
                final String direction = params.get(clientId + "_direction");
                final String waypointId = params.get(clientId + "_waypointId");

                final WaypointEvent waypointEvent = new WaypointEvent(this, behaviorEvent.getBehavior(),
                            direction != null ? WaypointEvent.Direction.valueOf(direction.toUpperCase(Locale.ENGLISH))
                                        : null,
                            waypointId);
                waypointEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(waypointEvent);
                return;
            }
        }

        super.queueEvent(event);
    }
}