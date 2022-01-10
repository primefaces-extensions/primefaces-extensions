/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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

import java.util.*;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.WaypointEvent;
import org.primefaces.util.Constants;

/**
 * <code>Waypoint</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.6
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "waypoint/waypoint.js")
public class Waypoint extends UIComponentBase implements Widget, ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Waypoint";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.WaypointRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(WaypointEvent.NAME));

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        // @formatter:off
      widgetVar,
      forValue("for"),
      forContext,
      enabled,
      horizontal,
      offset,
      continuous,
      triggerOnce;
      // @formatter:on

        private final String toString;

        PropertyKeys(String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
            toString = null;
        }

        @Override
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    public Waypoint() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultEventName() {
        return WaypointEvent.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processDecodes(final FacesContext fc) {
        if (isSelfRequest(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processValidators(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processValidators(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processUpdates(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext fc = FacesContext.getCurrentInstance();

        if (isSelfRequest(fc)) {
            final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (WaypointEvent.NAME.equals(eventName)) {
                final String direction = params.get(getClientId(fc) + "_direction");
                final String waypointId = params.get(getClientId(fc) + "_waypointId");

                final WaypointEvent waypointEvent = new WaypointEvent(this, behaviorEvent.getBehavior(),
                            direction != null ? WaypointEvent.Direction.valueOf(direction.toUpperCase(
                                        Locale.ENGLISH))
                                        : null,
                            waypointId);
                waypointEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(waypointEvent);

                return;
            }
        }

        super.queueEvent(event);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public void setFor(final String forValue) {
        getStateHelper().put(PropertyKeys.forValue, forValue);
    }

    public String getForContext() {
        return (String) getStateHelper().eval(PropertyKeys.forContext, null);
    }

    public void setForContext(final String forContext) {
        getStateHelper().put(PropertyKeys.forContext, forContext);
    }

    public boolean isEnabled() {
        return (Boolean) getStateHelper().eval(PropertyKeys.enabled, true);
    }

    public void setEnabled(final boolean enabled) {
        getStateHelper().put(PropertyKeys.enabled, enabled);
    }

    public boolean isHorizontal() {
        return (Boolean) getStateHelper().eval(PropertyKeys.horizontal, false);
    }

    public void setHorizontal(final boolean horizontal) {
        getStateHelper().put(PropertyKeys.horizontal, horizontal);
    }

    public String getOffset() {
        return (String) getStateHelper().eval(PropertyKeys.offset, null);
    }

    public void setOffset(final String offset) {
        getStateHelper().put(PropertyKeys.offset, offset);
    }

    public boolean isContinuous() {
        return (Boolean) getStateHelper().eval(PropertyKeys.continuous, true);
    }

    public void setContinuous(final boolean continuous) {
        getStateHelper().put(PropertyKeys.continuous, continuous);
    }

    public boolean isTriggerOnce() {
        return (Boolean) getStateHelper().eval(PropertyKeys.triggerOnce, false);
    }

    public void setTriggerOnce(final boolean triggerOnce) {
        getStateHelper().put(PropertyKeys.triggerOnce, triggerOnce);
    }

    private boolean isSelfRequest(final FacesContext fc) {
        return getClientId(fc)
                    .equals(fc.getExternalContext().getRequestParameterMap()
                                .get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

}
