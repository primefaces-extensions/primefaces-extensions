/*
 * Copyright 2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.tristatecheckbox;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;

/**
 * TriStateCheckbox
 *
 * @author  Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "primefaces.css"),
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
public class TriStateCheckbox extends HtmlInputText implements Widget {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.TriStateCheckbox";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * PropertyKeys
	 *
	 * @author  ova / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		itemLabel,
		stateOneIcon,
		stateTwoIcon,
		stateThreeIcon;

		String toString;

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

	public TriStateCheckbox() {
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

	public String getItemLabel() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.itemLabel, null);
	}

	public void setItemLabel(final String itemLabel) {
		setAttribute(TriStateCheckbox.PropertyKeys.itemLabel, itemLabel);
	}

	public String getStateOneIcon() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.stateOneIcon, null);
	}

	public void setStateOneIcon(final String stateOneIcon) {
		setAttribute(TriStateCheckbox.PropertyKeys.stateOneIcon, stateOneIcon);
	}

	public String getStateTwoIcon() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.stateTwoIcon, null);
	}

	public void setStateTwoIcon(final String stateTwoIcon) {
		setAttribute(TriStateCheckbox.PropertyKeys.stateTwoIcon, stateTwoIcon);
	}

	public String getStateThreeIcon() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.stateThreeIcon, null);
	}

	public void setStateThreeIcon(final String stateThreeIcon) {
		setAttribute(TriStateCheckbox.PropertyKeys.stateThreeIcon, stateThreeIcon);
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
