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

package org.primefaces.extensions.component.growl;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.AutoUpdatable;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.AbstractNotification;

/**
 * Component class for the <code>Growl</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "primefaces.css"),
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js")
})
public class Growl extends AbstractNotification implements AutoUpdatable, Widget {

	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.GrowlRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	public static final String WARN_ICON = "growl/images/warn.png";
	public static final String ERROR_ICON = "growl/images/error.png";
	public static final String INFO_ICON = "growl/images/info.png";
	public static final String FATAL_ICON = "growl/images/fatal.png";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		globalOnly,
		sticky,
		life,
		warnIcon,
		infoIcon,
		errorIcon,
		fatalIcon,
		autoUpdate,
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

	public Growl() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public boolean isGlobalOnly() {
		return (Boolean) getStateHelper().eval(PropertyKeys.globalOnly, false);
	}

	public void setGlobalOnly(final boolean globalOnly) {
		setAttribute(PropertyKeys.globalOnly, globalOnly);
	}

	public boolean isSticky() {
		return (Boolean) getStateHelper().eval(PropertyKeys.sticky, false);
	}

	public void setSticky(final boolean sticky) {
		setAttribute(PropertyKeys.sticky, sticky);
	}

	public int getLife() {
		return (Integer) getStateHelper().eval(PropertyKeys.life, 6000);
	}

	public void setLife(final int life) {
		getStateHelper().put(PropertyKeys.life, life);
	}

	@Override
	public boolean isAutoUpdate() {
		return (Boolean) getStateHelper().eval(PropertyKeys.autoUpdate, false);
	}

	public void setAutoUpdate(final boolean autoUpdate) {
		setAttribute(PropertyKeys.autoUpdate, autoUpdate);
	}

	public String getWarnIcon() {
		return (String) getStateHelper().eval(PropertyKeys.warnIcon, null);
	}

	public void setWarnIcon(final String warnIcon) {
		setAttribute(PropertyKeys.warnIcon, warnIcon);
	}

	public String getInfoIcon() {
		return (String) getStateHelper().eval(PropertyKeys.infoIcon, null);
	}

	public void setInfoIcon(final String infoIcon) {
		setAttribute(PropertyKeys.infoIcon, infoIcon);
	}

	public String getErrorIcon() {
		return (String) getStateHelper().eval(PropertyKeys.errorIcon, null);
	}

	public void setErrorIcon(final String errorIcon) {
		setAttribute(PropertyKeys.errorIcon, errorIcon);
	}

	public String getFatalIcon() {
		return (String) getStateHelper().eval(PropertyKeys.fatalIcon, null);
	}

	public void setFatalIcon(final String fatalIcon) {
		setAttribute(PropertyKeys.fatalIcon, fatalIcon);
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
