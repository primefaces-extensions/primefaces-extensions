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

package org.primefaces.extensions.component.ajaxstatus;

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
 * <code>AjaxStatus</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "primefaces.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
                      })
public class AjaxStatus extends UIComponentBase implements Widget {

	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.AjaxStatusRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	public static final String PRESTART_FACET = "prestart";
	public static final String START_FACET = "start";
	public static final String SUCCESS_FACET = "success";
	public static final String COMPLETE_FACET = "complete";
	public static final String ERROR_FACET = "error";
	public static final String DEFAULT_FACET = "default";

	public static final String[] FACETS = {PRESTART_FACET, START_FACET, SUCCESS_FACET, COMPLETE_FACET, ERROR_FACET};

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		onstart,
		oncomplete,
		onprestart,
		onsuccess,
		onerror,
		style,
		styleClass;

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

	public AjaxStatus() {
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

	public String getOnstart() {
		return (String) getStateHelper().eval(PropertyKeys.onstart, null);
	}

	public void setOnstart(final String onstart) {
		setAttribute(PropertyKeys.onstart, onstart);
	}

	public String getOncomplete() {
		return (String) getStateHelper().eval(PropertyKeys.oncomplete, null);
	}

	public void setOncomplete(final String oncomplete) {
		setAttribute(PropertyKeys.oncomplete, oncomplete);
	}

	public String getOnprestart() {
		return (String) getStateHelper().eval(PropertyKeys.onprestart, null);
	}

	public void setOnprestart(final String onprestart) {
		setAttribute(PropertyKeys.onprestart, onprestart);
	}

	public String getOnsuccess() {
		return (String) getStateHelper().eval(PropertyKeys.onsuccess, null);
	}

	public void setOnsuccess(final String onsuccess) {
		setAttribute(PropertyKeys.onsuccess, onsuccess);
	}

	public String getOnerror() {
		return (String) getStateHelper().eval(PropertyKeys.onerror, null);
	}

	public void setOnerror(final String onerror) {
		setAttribute(PropertyKeys.onerror, onerror);
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
