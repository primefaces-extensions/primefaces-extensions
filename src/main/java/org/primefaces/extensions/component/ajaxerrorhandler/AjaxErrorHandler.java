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

package org.primefaces.extensions.component.ajaxerrorhandler;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * Component class for the <code>AjaxErrorHandler</code> component.
 *
 * @author Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
@ResourceDependencies({
	@ResourceDependency(library = "javax.faces", name = "jsf.js"),
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css"),
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
public class AjaxErrorHandler extends UIComponentBase implements Widget {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.AjaxErrorHandler";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.AjaxErrorHandlerRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author Pavol Slany / last modified by $Author$
	 * @version $Revision$
	 */
	static public enum PropertyKeys {
		type,
		title,
		body,
		button,
		buttonOnclick,
		onerror,
		mode,
		widgetVar;

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

	public AjaxErrorHandler() {
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

	public String getType() {
		return (String) getStateHelper().eval(PropertyKeys.type, null);
	}

	public void setType(final String type) {
		getStateHelper().put(PropertyKeys.type, type);
	}

	public String getTitle() {
		return (String) getStateHelper().eval(PropertyKeys.title, null);
	}

	public void setTitle(final String title) {
		getStateHelper().put(PropertyKeys.title, title);
	}

	public String getBody() {
		return (String) getStateHelper().eval(PropertyKeys.body, null);
	}

	public void setBody(final String body) {
		getStateHelper().put(PropertyKeys.body, body);
	}

	public String getButton() {
		return (String) getStateHelper().eval(PropertyKeys.button, null);
	}

	public void setButton(final String button) {
		getStateHelper().put(PropertyKeys.button, button);
	}

	public String getButtonOnclick() {
		return (String) getStateHelper().eval(PropertyKeys.buttonOnclick, null);
	}

	public void setButtonOnclick(final String buttonOnclick) {
		getStateHelper().put(PropertyKeys.buttonOnclick, buttonOnclick);
	}

	public String getOnerror() {
		return (String) getStateHelper().eval(PropertyKeys.onerror, null);
	}

	public void setOnerror(final String onerror) {
		getStateHelper().put(PropertyKeys.onerror, onerror);
	}

	public String getMode() {
		return (String) getStateHelper().eval(PropertyKeys.mode, null);
	}

	public void setMode(final String mode) {
		getStateHelper().put(PropertyKeys.mode, mode);
	}

	public String resolveWidgetVar() {
		return (String) getAttributes().get(PropertyKeys.widgetVar.toString());
	}
}
