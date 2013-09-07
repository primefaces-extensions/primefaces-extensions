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

package org.primefaces.extensions.component.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.apache.commons.lang3.StringUtils;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.util.FastStringWriter;
import org.primefaces.util.Constants;

/**
 * <code>Layout</code> component.
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
                          @ResourceDependency(library = "primefaces-extensions", name = "layout/layout.css"),
                          @ResourceDependency(library = "primefaces-extensions", name = "layout/layout.js")
                      })
public class Layout extends UIComponentBase implements Widget, ClientBehaviorHolder {

	private static final Logger LOG = Logger.getLogger(Layout.class.getName());

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Layout";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LayoutRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	public static final String POSITION_SEPARATOR = "_";
	public static final String STYLE_CLASS_PANE = "ui-widget-content ui-corner-all";
	public static final String STYLE_CLASS_PANE_WITH_SUBPANES = "ui-corner-all pe-layout-pane-withsubpanes";
	public static final String STYLE_CLASS_PANE_HEADER = "ui-widget-header ui-corner-top pe-layout-pane-header";
	public static final String STYLE_CLASS_PANE_CONTENT = "pe-layout-pane-content";
	public static final String STYLE_CLASS_LAYOUT_CONTENT = "ui-layout-content";
	public static final String PANE_POSITION_CENTER = "center";
	public static final String PANE_POSITION_NORTH = "north";
	public static final String PANE_POSITION_SOUTH = "south";
	public static final String PANE_POSITION_WEST = "west";
	public static final String PANE_POSITION_EAST = "east";

	private static final Collection<String> EVENT_NAMES =
	    Collections.unmodifiableCollection(Arrays.asList("open", "close", "resize"));

	private ResponseWriter originalWriter;
	private FastStringWriter fsw;
	private boolean buildOptions;

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	enum PropertyKeys {

		widgetVar,
		fullPage,
		options,
		style,
		styleClass,
		state,
		stateCookie,
		togglerTip_open,
		togglerTip_closed,
		resizerTip,
		maskPanesEarly;

		private String toString;

		PropertyKeys(final String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public Layout() {
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

	public boolean isFullPage() {
		return (Boolean) getStateHelper().eval(PropertyKeys.fullPage, true);
	}

	public void setFullPage(boolean fullPage) {
		getStateHelper().put(PropertyKeys.fullPage, fullPage);
	}

	public Object getOptions() {
		return getStateHelper().eval(PropertyKeys.options, null);
	}

	public void setOptions(Object options) {
		getStateHelper().put(PropertyKeys.options, options);
	}

	public String getStyle() {
		return (String) getStateHelper().eval(PropertyKeys.style, null);
	}

	public void setStyle(String style) {
		getStateHelper().put(PropertyKeys.style, style);
	}

	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
	}

	public void setStyleClass(String styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}

	public String getState() {
		return (String) getStateHelper().eval(PropertyKeys.state, null);
	}

	public void setState(String state) {
		getStateHelper().put(PropertyKeys.state, state);
	}

	public boolean isStateCookie() {
		return (Boolean) getStateHelper().eval(PropertyKeys.stateCookie, false);
	}

	public void setStateCookie(boolean stateCookie) {
		getStateHelper().put(PropertyKeys.stateCookie, stateCookie);
	}

	public String getTogglerTipOpen() {
		return (String) getStateHelper().eval(PropertyKeys.togglerTip_open, null);
	}

	public void setTogglerTipOpen(String togglerTipOpen) {
		getStateHelper().put(PropertyKeys.togglerTip_open, togglerTipOpen);
	}

	public String getTogglerTipClosed() {
		return (String) getStateHelper().eval(PropertyKeys.togglerTip_closed, null);
	}

	public void setTogglerTipClosed(String togglerTipClosed) {
		getStateHelper().put(PropertyKeys.togglerTip_closed, togglerTipClosed);
	}

	public String getResizerTip() {
		return (String) getStateHelper().eval(PropertyKeys.resizerTip, null);
	}

	public void setResizerTip(String resizerTip) {
		getStateHelper().put(PropertyKeys.resizerTip, resizerTip);
	}

	public boolean isMaskPanesEarly() {
		return (Boolean) getStateHelper().eval(PropertyKeys.maskPanesEarly, false);
	}

	public void setMaskPanesEarly(boolean maskPanesEarly) {
		getStateHelper().put(PropertyKeys.maskPanesEarly, maskPanesEarly);
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

		String state = fc.getExternalContext().getRequestParameterMap().get(this.getClientId(fc) + "_state");
		if (StringUtils.isNotBlank(state)) {
			ValueExpression stateVE = this.getValueExpression(PropertyKeys.state.toString());
			if (stateVE != null) {
				// save "state"
				stateVE.setValue(fc.getELContext(), state);
				getStateHelper().remove(PropertyKeys.state);
			}
		}
	}

	@Override
	public void queueEvent(FacesEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();

		if (isSelfRequest(context)) {
			Map<String, String> params = context.getExternalContext().getRequestParameterMap();
			String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
			String clientId = this.getClientId(context);

			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
			LayoutPane pane = getLayoutPane(this, params.get(clientId + "_pane"));
			if (pane == null) {
				LOG.warning("LayoutPane by request parameter '" + params.get(clientId + "_pane") + "' was not found");

				return;
			}

			if ("open".equals(eventName)) {
				OpenEvent openEvent = new OpenEvent(pane, behaviorEvent.getBehavior());
				openEvent.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(openEvent);

				return;
			} else if ("close".equals(eventName)) {
				CloseEvent closeEvent = new CloseEvent(pane, behaviorEvent.getBehavior());
				closeEvent.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(closeEvent);

				return;
			} else if ("resize".equals(eventName)) {
				double width = Double.valueOf(params.get(clientId + "_width"));
				double height = Double.valueOf(params.get(clientId + "_height"));

				ResizeEvent resizeEvent = new ResizeEvent(pane, behaviorEvent.getBehavior(), width, height);
				event.setPhaseId(behaviorEvent.getPhaseId());
				super.queueEvent(resizeEvent);

				return;
			}
		}

		super.queueEvent(event);
	}

	public LayoutPane getLayoutPane(UIComponent component, String combinedPosition) {
		for (UIComponent child : component.getChildren()) {
			if (child instanceof LayoutPane) {
				if (((LayoutPane) child).getCombinedPosition().equals(combinedPosition)) {
					return (LayoutPane) child;
				} else {
					LayoutPane pane = getLayoutPane(child, combinedPosition);
					if (pane != null) {
						return pane;
					}
				}
			}
		}

		return null;
	}

	public ResponseWriter getOriginalWriter() {
		return originalWriter;
	}

	public void setOriginalWriter(ResponseWriter originalWriter) {
		this.originalWriter = originalWriter;
	}

	public FastStringWriter getFastStringWriter() {
		return fsw;
	}

	public void setFastStringWriter(FastStringWriter fsw) {
		this.fsw = fsw;
	}

	public boolean isBuildOptions() {
		return buildOptions;
	}

	public void setBuildOptions(boolean buildOptions) {
		this.buildOptions = buildOptions;
	}

	public void removeOptions() {
		getStateHelper().remove(PropertyKeys.options);
	}

	private boolean isSelfRequest(FacesContext context) {
		return this.getClientId(context)
		           .equals(context.getExternalContext().getRequestParameterMap().get(
		   		                   Constants.RequestParams.PARTIAL_SOURCE_PARAM));
	}

	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}
}
