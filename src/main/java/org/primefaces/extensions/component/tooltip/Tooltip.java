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

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.EnhancedAttachable;

/**
 * <code>Tooltip</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "primefaces.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "tooltip/tooltip.css"),
                          @ResourceDependency(library = "primefaces-extensions", name = "tooltip/tooltip.js")
                      })
public class Tooltip extends UIOutput implements Widget, EnhancedAttachable {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Tooltip";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TooltipRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		global,
		shared,
		autoShow,
		mouseTracking,
		adjustX,
		adjustY,
		atPosition,
		myPosition,
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

	public Tooltip() {
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

	public boolean isAutoShow() {
		return (Boolean) getStateHelper().eval(PropertyKeys.autoShow, false);
	}

	public void setAutoShow(boolean autoShow) {
		setAttribute(PropertyKeys.autoShow, autoShow);
	}

	public boolean isMouseTracking() {
		return (Boolean) getStateHelper().eval(PropertyKeys.mouseTracking, false);
	}

	public void setMouseTracking(boolean mouseTracking) {
		setAttribute(PropertyKeys.mouseTracking, mouseTracking);
	}

	public int getAdjustX() {
		return (Integer) getStateHelper().eval(PropertyKeys.adjustX, 0);
	}

	public void setAdjustX(int adjustX) {
		setAttribute(PropertyKeys.adjustX, adjustX);
	}

	public int getAdjustY() {
		return (Integer) getStateHelper().eval(PropertyKeys.adjustY, 0);
	}

	public void setAdjustY(int adjustY) {
		setAttribute(PropertyKeys.adjustY, adjustY);
	}

	public String getAtPosition() {
		return (String) getStateHelper().eval(PropertyKeys.atPosition, "bottom right");
	}

	public void setAtPosition(String atPosition) {
		setAttribute(PropertyKeys.atPosition, atPosition);
	}

	public String getMyPosition() {
		return (String) getStateHelper().eval(PropertyKeys.myPosition, "top left");
	}

	public void setMyPosition(String myPosition) {
		setAttribute(PropertyKeys.myPosition, myPosition);
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

	public void setFor(String forValue) {
		setAttribute(PropertyKeys.forValue, forValue);
	}

	public String getForSelector() {
		return (String) getStateHelper().eval(PropertyKeys.forSelector, null);
	}

	public void setForSelector(String forSelector) {
		setAttribute(PropertyKeys.forSelector, forSelector);
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
