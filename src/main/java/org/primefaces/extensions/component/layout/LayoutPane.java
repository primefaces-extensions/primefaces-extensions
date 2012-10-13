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
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;

/**
 * <code>LayoutPane</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class LayoutPane extends UIComponentBase {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.LayoutPane";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LayoutPaneRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		position,
		combinedPosition,
		styleHeader,
		styleClassHeader,
		styleContent,
		styleClassContent;

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

	public LayoutPane() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getStyleHeader() {
		return (String) getStateHelper().eval(PropertyKeys.styleHeader, null);
	}

	public void setStyleHeader(final String styleHeader) {
		setAttribute(PropertyKeys.styleHeader, styleHeader);
	}

	public String getStyleClassHeader() {
		return (String) getStateHelper().eval(PropertyKeys.styleClassHeader, null);
	}

	public void setStyleClassHeader(final String styleClassHeader) {
		setAttribute(PropertyKeys.styleClassHeader, styleClassHeader);
	}

	public String getStyleContent() {
		return (String) getStateHelper().eval(PropertyKeys.styleContent, null);
	}

	public void setStyleContent(final String styleContent) {
		setAttribute(PropertyKeys.styleContent, styleContent);
	}

	public String getStyleClassContent() {
		return (String) getStateHelper().eval(PropertyKeys.styleClassContent, null);
	}

	public void setStyleClassContent(final String styleClassContent) {
		setAttribute(PropertyKeys.styleClassContent, styleClassContent);
	}

	// position "north" | "south" | "west" | "east" | "center"
	public String getPosition() {
		return (String) getStateHelper().eval(PropertyKeys.position, "center");
	}

	public void setPosition(String position) {
		setAttribute(PropertyKeys.position, position);
	}

	public String getCombinedPosition() {
		return (String) getStateHelper().eval(PropertyKeys.combinedPosition, "center");
	}

	public void setCombinedPosition(String combinedPosition) {
		setAttribute(PropertyKeys.combinedPosition, combinedPosition);
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
