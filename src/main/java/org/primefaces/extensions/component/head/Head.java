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
 */
package org.primefaces.extensions.component.head;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIOutput;

/**
 * Component class for the <code>Head</code> component.
 * Ordering of rendered resources:
 * 	- before facet
 * 	- JSF CSS resources
 * 	- PF theme
 *  - middle facet
 *  - JSF JS resources
 *  - head content (encoded by super class at encodeChildren)
 * 	- after facet
 *
 * @author Thomas Andraschko
 * @author Oleg Varaksin
 * @since 0.2
 */
public class Head extends UIOutput {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Head";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.HeadRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	protected enum PropertyKeys {
		title,
		shortcutIcon;

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

	public Head() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getTitle() {
		return (String) getStateHelper().eval(PropertyKeys.title, null);
	}

	public void setTitle(final String title) {
		setAttribute(PropertyKeys.title, title);
	}

	public String getShortcutIcon() {
		return (String) getStateHelper().eval(PropertyKeys.shortcutIcon, null);
	}

	public void setShortcutIcon(final String shortcutIcon) {
		setAttribute(PropertyKeys.shortcutIcon, shortcutIcon);
	}

	@SuppressWarnings("unchecked")
	public void setAttribute(final PropertyKeys property, final Object value) {
		getStateHelper().put(property, value);

		List<String> setAttributes = (List<String>) this.getAttributes().get(
				"javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put(
						"javax.faces.component.UIComponentBase.attributesThatAreSet",
						setAttributes);
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
