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
import java.util.Collections;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ScalarDataModel;

import org.primefaces.component.api.Widget;
import org.primefaces.component.menuitem.MenuItem;

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
                          @ResourceDependency(library = "primefaces-extensions", name = "core/core.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "layout/jquery.layout.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "layout/jquery.corner.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "layout/layout.css"),
                          @ResourceDependency(library = "primefaces-extensions", name = "layout/layout.js")
                      })
public class Layout extends UIComponentBase implements Widget {

	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LayoutRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  ova / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		tabs,
		togglerTipOpen,
		togglerTipClose,
		resizerTip;

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

	/**
	 * The {@link javax.faces.model.DataModel} associated with this component to build tabs dynamically, lazily instantiated if
	 * requested. This object is not part of the saved and restored state of the component.
	 */
	private DataModel<MenuItem> model = null;

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
		setAttribute(PropertyKeys.widgetVar, widgetVar);
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
	public void setTabs(Object tabs) {
		setDataModel(null);
		setAttribute(PropertyKeys.tabs, tabs);
	}

	public String getTogglerTipOpen() {
		return (String) getStateHelper().eval(PropertyKeys.togglerTipOpen, null);
	}

	public void setTogglerTipOpen(String togglerTipOpen) {
		setAttribute(PropertyKeys.togglerTipOpen, togglerTipOpen);
	}

	public String getTogglerTipClose() {
		return (String) getStateHelper().eval(PropertyKeys.togglerTipClose, null);
	}

	public void setTogglerTipClose(String togglerTipClose) {
		setAttribute(PropertyKeys.togglerTipClose, togglerTipClose);
	}

	public String getResizerTip() {
		return (String) getStateHelper().eval(PropertyKeys.resizerTip, null);
	}

	public void setResizerTip(String resizerTip) {
		setAttribute(PropertyKeys.resizerTip, resizerTip);
	}

	/**
	 * Sets data model.
	 *
	 * @param model data model
	 */
	public void setDataModel(DataModel<MenuItem> model) {
		this.model = model;
	}

	/**
	 * Gets data model.
	 *
	 * @return DataModel
	 */
	public DataModel<MenuItem> getDataModel() {
		if (this.model != null) {
			return model;
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

		return model;
	}

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
