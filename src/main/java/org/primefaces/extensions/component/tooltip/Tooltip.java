/*
 * Copyright 2011 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.tooltip;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 * Tooltip component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "core/core.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "core/core.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "tooltip/jquery.qtip.css"),
                          @ResourceDependency(library = "primefaces-extensions", name = "tooltip/jquery.qtip.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "tooltip/tooltip.js")
                      })
public class Tooltip extends UIOutput implements org.primefaces.component.api.Widget {

	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TooltipRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * PropertyKeys
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		global,
		shared,
		targetPosition,
		position,
		showEvent,
		showDelay,
		showEffect,
		showEffectLength,
		hideEvent,
		hideDelay,
		hideEffect,
		hideEffectLength,
		forValue("for"),
		forSelector;

		String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public Tooltip() {
		setRendererType(DEFAULT_RENDERER);
	}

	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(String widgetVar) {
		setAttribute(PropertyKeys.widgetVar, widgetVar);
	}

	public boolean isGlobal() {
		return (Boolean) getStateHelper().eval(PropertyKeys.global, false);
	}

	public void setGlobal(boolean global) {
		setAttribute(PropertyKeys.global, global);
	}

	public boolean isShared() {
		return (Boolean) getStateHelper().eval(PropertyKeys.shared, false);
	}

	public void setShared(boolean shared) {
		setAttribute(PropertyKeys.shared, shared);
	}

	public String getTargetPosition() {
		return (String) getStateHelper().eval(PropertyKeys.targetPosition, "bottom right");
	}

	public void setTargetPosition(String targetPosition) {
		setAttribute(PropertyKeys.targetPosition, targetPosition);
	}

	public String getPosition() {
		return (String) getStateHelper().eval(Tooltip.PropertyKeys.position, "top left");
	}

	public void setPosition(String position) {
		setAttribute(PropertyKeys.position, position);
	}

	public String getShowEvent() {
		return (String) getStateHelper().eval(PropertyKeys.showEvent, "mouseenter");
	}

	public void setShowEvent(String showEvent) {
		setAttribute(PropertyKeys.showEvent, showEvent);
	}

	public int getShowDelay() {
		return (Integer) getStateHelper().eval(PropertyKeys.showDelay, 0);
	}

	public void setShowDelay(int showDelay) {
		setAttribute(PropertyKeys.showDelay, showDelay);
	}

	public String getShowEffect() {
		return (String) getStateHelper().eval(PropertyKeys.showEffect, "fadeIn");
	}

	public void setShowEffect(String showEffect) {
		setAttribute(PropertyKeys.showEffect, showEffect);
	}

	public int getShowEffectLength() {
		return (Integer) getStateHelper().eval(PropertyKeys.showEffectLength, 500);
	}

	public void setShowEffectLength(int showEffectLength) {
		setAttribute(PropertyKeys.showEffectLength, showEffectLength);
	}

	public String getHideEvent() {
		return (String) getStateHelper().eval(PropertyKeys.hideEvent, "mouseleave");
	}

	public void setHideEvent(String hideEvent) {
		setAttribute(PropertyKeys.hideEvent, hideEvent);
	}

	public int getHideDelay() {
		return (Integer) getStateHelper().eval(PropertyKeys.hideDelay, 0);
	}

	public void setHideDelay(int hideDelay) {
		setAttribute(PropertyKeys.hideDelay, hideDelay);
	}

	public String getHideEffect() {
		return (String) getStateHelper().eval(PropertyKeys.hideEffect, "fadeOut");
	}

	public void setHideEffect(String hideEffect) {
		setAttribute(PropertyKeys.hideEffect, hideEffect);
	}

	public int getHideEffectLength() {
		return (Integer) getStateHelper().eval(PropertyKeys.hideEffectLength, 500);
	}

	public void setHideEffectLength(int hideEffectLength) {
		setAttribute(PropertyKeys.hideEffectLength, hideEffectLength);
	}

	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forValue, null);
	}

	public void setFor(String _for) {
		setAttribute(PropertyKeys.forValue, _for);
	}

	public String getForSelector() {
		return (String) getStateHelper().eval(PropertyKeys.forSelector, null);
	}

	public void setForSelector(String forSelector) {
		setAttribute(PropertyKeys.forSelector, forSelector);
	}

	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public String resolveWidgetVar() {
		FacesContext context = FacesContext.getCurrentInstance();
		String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		} else {
			return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
		}
	}

	@SuppressWarnings("unchecked")
	public void setAttribute(final PropertyKeys property, final Object value) {
		getStateHelper().put(property, value);

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
