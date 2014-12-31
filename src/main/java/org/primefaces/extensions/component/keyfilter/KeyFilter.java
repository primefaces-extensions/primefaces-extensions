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
import org.primefaces.util.ComponentUtils;

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
		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
	}

	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forValue, null);
	}

	public void setFor(final String forValue) {
		getStateHelper().put(PropertyKeys.forValue, forValue);
	}

	public String getRegEx() {
		return (String) getStateHelper().eval(PropertyKeys.regEx, null);
	}

	public void setRegEx(final String regEx) {
		getStateHelper().put(PropertyKeys.regEx, regEx);
	}

	public String getMask() {
		return (String) getStateHelper().eval(PropertyKeys.mask, null);
	}

	public void setMask(final String mask) {
		getStateHelper().put(PropertyKeys.mask, mask);
	}

	public String getTestFunction() {
		return (String) getStateHelper().eval(PropertyKeys.testFunction, null);
	}

	public void setTestFunction(final String testFunction) {
		getStateHelper().put(PropertyKeys.testFunction, testFunction);
	}

	public Boolean getPreventPaste() {
		return (Boolean) getStateHelper().eval(PropertyKeys.preventPaste, Boolean.TRUE);
	}

	public void setPreventPaste(final Boolean preventPaste) {
		getStateHelper().put(PropertyKeys.preventPaste, preventPaste);
	}
	
	public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
	}
}
