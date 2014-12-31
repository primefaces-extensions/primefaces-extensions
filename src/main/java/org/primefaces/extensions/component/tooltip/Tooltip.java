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
import org.primefaces.util.ComponentUtils;

/**
 * <code>Tooltip</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
                          @ResourceDependency(library = "primefaces", name = "primefaces.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "tooltip/tooltip.css"),
                          @ResourceDependency(library = "primefaces-extensions", name = "tooltip/tooltip.js")
                      })
public class Tooltip extends UIOutput implements Widget {

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
		fixed,
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
		forValue("for");

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
		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
	}

	public boolean isGlobal() {
		return (Boolean) getStateHelper().eval(PropertyKeys.global, false);
	}

	public void setGlobal(boolean global) {
		getStateHelper().put(PropertyKeys.global, global);
	}

	public boolean isShared() {
		return (Boolean) getStateHelper().eval(PropertyKeys.shared, false);
	}

	public void setShared(boolean shared) {
		getStateHelper().put(PropertyKeys.shared, shared);
	}

	public boolean isAutoShow() {
		return (Boolean) getStateHelper().eval(PropertyKeys.autoShow, false);
	}

	public void setAutoShow(boolean autoShow) {
		getStateHelper().put(PropertyKeys.autoShow, autoShow);
	}

	public boolean isMouseTracking() {
		return (Boolean) getStateHelper().eval(PropertyKeys.mouseTracking, false);
	}

	public void setMouseTracking(boolean mouseTracking) {
		getStateHelper().put(PropertyKeys.mouseTracking, mouseTracking);
	}

	public boolean isFixed() {
		return (Boolean) getStateHelper().eval(PropertyKeys.fixed, false);
	}

	public void setFixed(boolean fixed) {
		getStateHelper().put(PropertyKeys.fixed, fixed);
	}

	public int getAdjustX() {
		return (Integer) getStateHelper().eval(PropertyKeys.adjustX, 0);
	}

	public void setAdjustX(int adjustX) {
		getStateHelper().put(PropertyKeys.adjustX, adjustX);
	}

	public int getAdjustY() {
		return (Integer) getStateHelper().eval(PropertyKeys.adjustY, 0);
	}

	public void setAdjustY(int adjustY) {
		getStateHelper().put(PropertyKeys.adjustY, adjustY);
	}

	public String getAtPosition() {
		return (String) getStateHelper().eval(PropertyKeys.atPosition, "bottom right");
	}

	public void setAtPosition(String atPosition) {
		getStateHelper().put(PropertyKeys.atPosition, atPosition);
	}

	public String getMyPosition() {
		return (String) getStateHelper().eval(PropertyKeys.myPosition, "top left");
	}

	public void setMyPosition(String myPosition) {
		getStateHelper().put(PropertyKeys.myPosition, myPosition);
	}

	public String getShowEvent() {
		return (String) getStateHelper().eval(PropertyKeys.showEvent, "mouseenter");
	}

	public void setShowEvent(String showEvent) {
		getStateHelper().put(PropertyKeys.showEvent, showEvent);
	}

	public int getShowDelay() {
		return (Integer) getStateHelper().eval(PropertyKeys.showDelay, 0);
	}

	public void setShowDelay(int showDelay) {
		getStateHelper().put(PropertyKeys.showDelay, showDelay);
	}

	public String getShowEffect() {
		return (String) getStateHelper().eval(PropertyKeys.showEffect, "fadeIn");
	}

	public void setShowEffect(String showEffect) {
		getStateHelper().put(PropertyKeys.showEffect, showEffect);
	}

	public int getShowEffectLength() {
		return (Integer) getStateHelper().eval(PropertyKeys.showEffectLength, 500);
	}

	public void setShowEffectLength(int showEffectLength) {
		getStateHelper().put(PropertyKeys.showEffectLength, showEffectLength);
	}

	public String getHideEvent() {
		return (String) getStateHelper().eval(PropertyKeys.hideEvent, "mouseleave");
	}

	public void setHideEvent(String hideEvent) {
		getStateHelper().put(PropertyKeys.hideEvent, hideEvent);
	}

	public int getHideDelay() {
		return (Integer) getStateHelper().eval(PropertyKeys.hideDelay, 0);
	}

	public void setHideDelay(int hideDelay) {
		getStateHelper().put(PropertyKeys.hideDelay, hideDelay);
	}

	public String getHideEffect() {
		return (String) getStateHelper().eval(PropertyKeys.hideEffect, "fadeOut");
	}

	public void setHideEffect(String hideEffect) {
		getStateHelper().put(PropertyKeys.hideEffect, hideEffect);
	}

	public int getHideEffectLength() {
		return (Integer) getStateHelper().eval(PropertyKeys.hideEffectLength, 500);
	}

	public void setHideEffectLength(int hideEffectLength) {
		getStateHelper().put(PropertyKeys.hideEffectLength, hideEffectLength);
	}

	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forValue, null);
	}

	public void setFor(String forValue) {
		getStateHelper().put(PropertyKeys.forValue, forValue);
	}

	public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
	}

}
