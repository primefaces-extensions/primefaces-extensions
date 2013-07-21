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

package org.primefaces.extensions.component.keyfilter;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;

/**
 * Component class for the <code>KeyFilter</code> component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "keyfilter/keyfilter.js")
})
public class KeyFilter extends UIComponentBase implements Widget {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.KeyFilter";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.KeyFilterRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		forValue("for"),
		regEx,
		mask,
		testFunction,
		preventPaste;

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

	public KeyFilter() {
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

	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forValue, null);
	}

	public void setFor(final String forValue) {
		setAttribute(PropertyKeys.forValue, forValue);
	}

	public String getRegEx() {
		return (String) getStateHelper().eval(PropertyKeys.regEx, null);
	}

	public void setRegEx(final String regEx) {
		setAttribute(PropertyKeys.regEx, regEx);
	}

	public String getMask() {
		return (String) getStateHelper().eval(PropertyKeys.mask, null);
	}

	public void setMask(final String mask) {
		setAttribute(PropertyKeys.mask, mask);
	}

	public String getTestFunction() {
		return (String) getStateHelper().eval(PropertyKeys.testFunction, null);
	}

	public void setTestFunction(final String testFunction) {
		setAttribute(PropertyKeys.testFunction, testFunction);
	}

	public Boolean getPreventPaste() {
		return (Boolean) getStateHelper().eval(PropertyKeys.preventPaste, Boolean.TRUE);
	}

	public void setPreventPaste(final Boolean preventPaste) {
		setAttribute(PropertyKeys.preventPaste, preventPaste);
	}
	
	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
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
