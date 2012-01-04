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

package org.primefaces.extensions.component.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ScalarDataModel;

import org.primefaces.component.api.Widget;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.util.Constants;

/**
 * <code>Layout</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "primefaces.css"),
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "primefaces.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "layout/layout.css"),
                          @ResourceDependency(library = "primefaces-extensions", name = "layout/layout.js")
                      })
public class Layout extends UIComponentBase implements Widget, ClientBehaviorHolder {

	private static final Logger LOG = Logger.getLogger(Layout.class.getName());

	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LayoutRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	public static final String POSITION_NORTH = "north";
	public static final String POSITION_SOUTH = "south";
	public static final String POSITION_CENTER = "center";
	public static final String POSITION_WEST = "west";
	public static final String POSITION_EAST = "east";
	public static final String POSITION_SEPARATOR = "_";
	public static final String STYLE_CLASS_PANE = "ui-widget-content ui-corner-top";
	public static final String STYLE_CLASS_PANE_HEADER = "ui-widget-header ui-layout-pane-header ui-corner-top";
	public static final String STYLE_CLASS_PANE_CONTENT = "ui-layout-pane-content";

	private static final Collection<String> EVENT_NAMES =
	    Collections.unmodifiableCollection(Arrays.asList("open", "close", "resize"));

	private Map<String, UIComponent> layoutPanes;

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  ova / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		fullPage,
		style,
		styleClass,
		tabs,
		togglerTipOpen,
		togglerTipClose,
		resizerTip;

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

	/**
	 * The {@link javax.faces.model.DataModel} associated with this component to build tabs dynamically, lazily instantiated if
	 * requested. This object is not part of the saved and restored state of the component.
	 */
	private DataModel<MenuItem> dataModel = null;

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

	public void setWidgetVar(final String widgetVar) {
		setAttribute(PropertyKeys.widgetVar, widgetVar);
	}

	public boolean isFullPage() {
		return (Boolean) getStateHelper().eval(PropertyKeys.fullPage, true);
	}

	public void setFullPage(final boolean fullPage) {
		setAttribute(PropertyKeys.fullPage, fullPage);
	}

	public String getStyle() {
		return (String) getStateHelper().eval(PropertyKeys.style, null);
	}

	public void setStyle(final String style) {
		setAttribute(PropertyKeys.style, style);
	}

	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
	}

	public void setStyleClass(final String styleClass) {
		setAttribute(PropertyKeys.styleClass, styleClass);
	}

	/**
	 * Return tabs of the layout. This value must either be be of type {@link DataModel}, or a type that can be adapted into a
	 * {@link DataModel}:
	 *
	 * <ul>
	 *   <li>Arrays</li>
	 *   <li><code>java.util.List</code></li>
	 * </ul>
	 *
	 * <p>All other types will be adapted using the {@link javax.faces.model.ScalarDataModel} class, which will treat the object
	 * as a single tab.</p>
	 *
	 * @return Object
	 */
	public Object getTabs() {
		return getStateHelper().eval(PropertyKeys.tabs, null);
	}

	/**
	 * Set tabs of the layout. This items object must either be be of type {@link DataModel}, or a type that can be adapted into a
	 * {@link DataModel}.
	 *
	 * @param tabs items new items
	 */
	public void setTabs(final Object tabs) {
		setDataModel(null);
		setAttribute(PropertyKeys.tabs, tabs);
	}

	public String getTogglerTipOpen() {
		return (String) getStateHelper().eval(PropertyKeys.togglerTipOpen, null);
	}

	public void setTogglerTipOpen(final String togglerTipOpen) {
		setAttribute(PropertyKeys.togglerTipOpen, togglerTipOpen);
	}

	public String getTogglerTipClose() {
		return (String) getStateHelper().eval(PropertyKeys.togglerTipClose, null);
	}

	public void setTogglerTipClose(final String togglerTipClose) {
		setAttribute(PropertyKeys.togglerTipClose, togglerTipClose);
	}

	public String getResizerTip() {
		return (String) getStateHelper().eval(PropertyKeys.resizerTip, null);
	}

	public void setResizerTip(final String resizerTip) {
		setAttribute(PropertyKeys.resizerTip, resizerTip);
	}

	/**
	 * Sets data model.
	 *
	 * @param model data model
	 */
	public void setDataModel(final DataModel<MenuItem> model) {
		this.dataModel = model;
	}

	/**
	 * Gets data model.
	 *
	 * @return DataModel
	 */
	@SuppressWarnings("unchecked")
	public DataModel<MenuItem> getDataModel() {
		if (this.dataModel != null) {
			return dataModel;
		}

		Object tabs = getTabs();
		if (tabs == null) {
			setDataModel(new ListDataModel<MenuItem>(Collections.EMPTY_LIST));
		} else if (tabs instanceof DataModel<?>) {
			setDataModel((DataModel<MenuItem>) tabs);
		} else if (tabs instanceof List<?>) {
			setDataModel(new ListDataModel<MenuItem>((List<MenuItem>) tabs));
		} else if (MenuItem[].class.isAssignableFrom(tabs.getClass())) {
			setDataModel(new ArrayDataModel<MenuItem>((MenuItem[]) tabs));
		} else {
			setDataModel(new ScalarDataModel<MenuItem>((MenuItem) tabs));
		}

		return dataModel;
	}

	@Override
	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

	@Override
	public void processDecodes(final FacesContext context) {
		if (isSelfRequest(context)) {
			this.decode(context);
		} else {
			super.processDecodes(context);
		}
	}

	@Override
	public void processValidators(final FacesContext context) {
		if (!isSelfRequest(context)) {
			super.processValidators(context);
		}
	}

	@Override
	public void processUpdates(final FacesContext context) {
		if (!isSelfRequest(context)) {
			super.processUpdates(context);
		}
	}

	@Override
	public void queueEvent(final FacesEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
		String clientId = this.getClientId(context);

		if (isSelfRequest(context)) {
			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
			LayoutPane pane = (LayoutPane) getLayoutPanes().get(params.get(clientId + "_pane"));
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

	public Map<String, UIComponent> getLayoutPanes() {
		if (layoutPanes != null) {
			return layoutPanes;
		}

		layoutPanes = new HashMap<String, UIComponent>();

		for (UIComponent child : this.getChildren()) {
			if (child instanceof LayoutPane) {
				// layout pane on the first level
				pickLayoutPane(child, layoutPanes);
			} else if (child instanceof UIForm) {
				// a form is allowed here
				layoutPanes.put("form", child);

				for (UIComponent child2 : child.getChildren()) {
					if (child2 instanceof LayoutPane) {
						// layout pane on the first level
						pickLayoutPane(child2, layoutPanes);
					}
				}
			}
		}

		return layoutPanes;
	}

	private void pickLayoutPane(final UIComponent child, final Map<String, UIComponent> layoutPanes) {
		if (!child.isRendered()) {
			return;
		}

		String position = ((LayoutPane) child).getPosition();
		layoutPanes.put(position, child);

		boolean hasSubPanes = false;
		for (UIComponent subChild : child.getChildren()) {
			if (subChild instanceof LayoutPane) {
				if (!subChild.isRendered()) {
					continue;
				}

				// layout pane on the second level
				layoutPanes.put(position + POSITION_SEPARATOR + ((LayoutPane) subChild).getPosition(), subChild);
				hasSubPanes = true;
			}
		}

		if (hasSubPanes && layoutPanes.get(position + POSITION_SEPARATOR + POSITION_CENTER) == null) {
			throw new FacesException("Rendered 'center' layout pane inside of '" + position
			                         + "' layout pane is missing");
		}

		if (hasSubPanes) {
			((LayoutPane) child).setExistNestedPanes(true);
		}
	}

	private boolean isSelfRequest(final FacesContext context) {
		return this.getClientId(context)
		           .equals(context.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM));
	}

	@Override
	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}

	public void setAttribute(final PropertyKeys property, final Object value) {
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
