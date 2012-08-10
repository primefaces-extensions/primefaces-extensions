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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.EnhancedAttachable;
import org.primefaces.extensions.event.WaypointEvent;
import org.primefaces.util.Constants;

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
public class Waypoint extends UIComponentBase implements Widget, EnhancedAttachable, ClientBehaviorHolder {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Waypoint";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.WaypointRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("scroll"));

	/**
	 * PropertyKeys
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		forValue("for"),
		forSelector,
		forContext,
		forContextSelector,
		offset,
		continuous,
		onlyOnScroll,
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
		setAttribute(PropertyKeys.widgetVar, widgetVar);
	}

	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forValue, null);
	}

	public void setFor(String forValue) {
		setAttribute(PropertyKeys.forValue, forValue);
	}

	public String getForSelector() {
		return (String) getStateHelper().eval(PropertyKeys.forSelector, null);
	}

	public void setForSelector(String forSelector) {
		setAttribute(PropertyKeys.forSelector, forSelector);
	}

	public String getForContext() {
		return (String) getStateHelper().eval(PropertyKeys.forContext, null);
	}

	public void setForContext(String forContext) {
		setAttribute(PropertyKeys.forContext, forContext);
	}

	public String getForContextSelector() {
		return (String) getStateHelper().eval(PropertyKeys.forContextSelector, null);
	}

	public void setForContextSelector(String forContextSelector) {
		setAttribute(PropertyKeys.forContextSelector, forContextSelector);
	}

	public String getOffset() {
		return (String) getStateHelper().eval(PropertyKeys.offset, null);
	}

	public void setOffset(String offset) {
		setAttribute(PropertyKeys.offset, offset);
	}

	public boolean isContinuous() {
		return (Boolean) getStateHelper().eval(PropertyKeys.continuous, true);
	}

	public void setContinuous(boolean continuous) {
		setAttribute(PropertyKeys.continuous, continuous);
	}

	public boolean isOnlyOnScroll() {
		return (Boolean) getStateHelper().eval(PropertyKeys.onlyOnScroll, false);
	}

	public void setOnlyOnScroll(boolean onlyOnScroll) {
		setAttribute(PropertyKeys.onlyOnScroll, onlyOnScroll);
	}

	public boolean isTriggerOnce() {
		return (Boolean) getStateHelper().eval(PropertyKeys.triggerOnce, false);
	}

	public void setTriggerOnce(boolean triggerOnce) {
		setAttribute(PropertyKeys.triggerOnce, triggerOnce);
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
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

		if (isSelfRequest(fc)) {
			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

			if ("scroll".equals(eventName)) {
				String direction = params.get(this.getClientId(fc) + "_direction");
				WaypointEvent waypointEvent =
				    new WaypointEvent(this, behaviorEvent.getBehavior(),
				                      direction != null ? WaypointEvent.Direction.valueOf(direction.toUpperCase(Locale.ENGLISH))
				                                        : null);
				waypointEvent.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(waypointEvent);

				return;
			}
		}

		super.queueEvent(event);
	}

	private boolean isSelfRequest(FacesContext fc) {
		return this.getClientId(fc).equals(fc.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM));
	}

	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}

	public void setAttribute(PropertyKeys property, Object value) {
		getStateHelper().put(property, value);

		@SuppressWarnings("unchecked")
		List<String> setAttributes =
		    (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
			}
		}

		if (setAttributes != null && value == null) {
			final String attributeName = property.toString();
			final ValueExpression ve = getValueExpression(attributeName);
			if (ve == null) {
				setAttributes.remove(attributeName);
			} else if (!setAttributes.contains(attributeName)) {
				setAttributes.add(attributeName);
			}
		}
	}
}
