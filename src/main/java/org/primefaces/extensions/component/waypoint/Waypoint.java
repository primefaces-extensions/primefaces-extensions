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
package org.primefaces.extensions.component.waypoint;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
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
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "waypoint/waypoint.js")
})
public class Waypoint extends UIComponentBase implements Widget, ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Waypoint";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.WaypointRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(WaypointEvent.NAME));

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
                final String direction = params.get(this.getClientId(fc) + "_direction");
                final String waypointId = params.get(this.getClientId(fc) + "_waypointId");

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
        return this.getClientId(fc)
                    .equals(fc.getExternalContext().getRequestParameterMap()
                                .get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

}
