/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.component.waypoint;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.WaypointEvent;
import org.primefaces.util.Constants;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import org.primefaces.util.ComponentUtils;

/**
 * Waypoint.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.6
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "primefaces.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "waypoint/waypoint.js")
                      })
public class Waypoint extends UIComponentBase implements Widget, ClientBehaviorHolder {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Waypoint";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.WaypointRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("reached"));

	/**
	 * PropertyKeys
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		forValue("for"),
		forContext,
        enabled,
        horizontal,
		offset,
		continuous,
		triggerOnce;

		private String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public Waypoint() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(String widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
	}

	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forValue, null);
	}

	public void setFor(String forValue) {
		getStateHelper().put(PropertyKeys.forValue, forValue);
	}

	public String getForContext() {
		return (String) getStateHelper().eval(PropertyKeys.forContext, null);
	}

	public void setForContext(String forContext) {
		getStateHelper().put(PropertyKeys.forContext, forContext);
	}
    
    public boolean isEnabled() {
   		return (Boolean) getStateHelper().eval(PropertyKeys.enabled, true);
   	}
   
   	public void setEnabled(boolean enabled) {
   		getStateHelper().put(PropertyKeys.enabled, enabled);
   	}
    
    public boolean isHorizontal() {
   		return (Boolean) getStateHelper().eval(PropertyKeys.horizontal, false);
   	}
   
   	public void setHorizontal(boolean horizontal) {
   		getStateHelper().put(PropertyKeys.horizontal, horizontal);
   	}    

	public String getOffset() {
		return (String) getStateHelper().eval(PropertyKeys.offset, null);
	}

	public void setOffset(String offset) {
		getStateHelper().put(PropertyKeys.offset, offset);
	}

	public boolean isContinuous() {
		return (Boolean) getStateHelper().eval(PropertyKeys.continuous, true);
	}

	public void setContinuous(boolean continuous) {
		getStateHelper().put(PropertyKeys.continuous, continuous);
	}

	public boolean isTriggerOnce() {
		return (Boolean) getStateHelper().eval(PropertyKeys.triggerOnce, false);
	}

	public void setTriggerOnce(boolean triggerOnce) {
		getStateHelper().put(PropertyKeys.triggerOnce, triggerOnce);
	}

	@Override
	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

	@Override
	public void processDecodes(FacesContext fc) {
		if (isSelfRequest(fc)) {
			this.decode(fc);
		} else {
			super.processDecodes(fc);
		}
	}

	@Override
	public void processValidators(FacesContext fc) {
		if (!isSelfRequest(fc)) {
			super.processValidators(fc);
		}
	}

	@Override
	public void processUpdates(FacesContext fc) {
		if (!isSelfRequest(fc)) {
			super.processUpdates(fc);
		}
	}

	@Override
	public void queueEvent(FacesEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();

		if (isSelfRequest(fc)) {
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

			if ("reached".equals(eventName)) {
				String direction = params.get(this.getClientId(fc) + "_direction");
				String waypointId = params.get(this.getClientId(fc) + "_waypointId");

				WaypointEvent waypointEvent =
				    new WaypointEvent(this, behaviorEvent.getBehavior(),
				                      (direction != null ? WaypointEvent.Direction.valueOf(direction.toUpperCase(
				                                                                               Locale.ENGLISH))
				                                         : null),
				                      waypointId);
				waypointEvent.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(waypointEvent);

				return;
			}
		}

		super.queueEvent(event);
	}

	private boolean isSelfRequest(FacesContext fc) {
		return this.getClientId(fc)
		           .equals(fc.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
	}

	public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
	}
}
