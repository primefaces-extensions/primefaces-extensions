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

	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		styleHeader,
		styleClassHeader,
		styleContent,
		styleClassContent,
		position,
		resizable,
		closable,
		size,
		minSize,
		maxSize,
		minWidth,
		maxWidth,
		minHeight,
		maxHeight,
		initClosed,
		statusbar;

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

	private boolean existNestedPanes = false;

	public LayoutPane() {
		setRendererType(null);
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

	/**
	 * Gets position ("north" | "south" | "west" | "east" | "center").
	 *
	 * @return String position
	 */
	public String getPosition() {
		return (String) getStateHelper().eval(PropertyKeys.position, "center");
	}

	/**
	 * Sets position ("north" | "south" | "west" | "east" | "center").
	 *
	 * @param position position to set
	 */
	public void setPosition(final String position) {
		setAttribute(PropertyKeys.position, position);
	}

	public boolean isResizable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.resizable, true);
	}

	public void setResizable(final boolean resizable) {
		setAttribute(PropertyKeys.resizable, resizable);
	}

	public boolean isClosable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.closable, true);
	}

	public void setClosable(final boolean closable) {
		setAttribute(PropertyKeys.closable, closable);
	}

	public String getSize() {
		return (String) getStateHelper().eval(PropertyKeys.size, null);
	}

	public void setSize(final String size) {
		setAttribute(PropertyKeys.size, size);
	}

	public String getMinSize() {
		return (String) getStateHelper().eval(PropertyKeys.minSize, null);
	}

	public void setMinSize(final String minSize) {
		setAttribute(PropertyKeys.minSize, minSize);
	}

	public String getMaxSize() {
		return (String) getStateHelper().eval(PropertyKeys.maxSize, null);
	}

	public void setMaxSize(final String maxSize) {
		setAttribute(PropertyKeys.maxSize, maxSize);
	}

	public String getMinWidth() {
		return (String) getStateHelper().eval(PropertyKeys.minWidth, null);
	}

	public void setMinWidth(final String minWidth) {
		setAttribute(PropertyKeys.minWidth, minWidth);
	}

	public String getMaxWidth() {
		return (String) getStateHelper().eval(PropertyKeys.maxWidth, null);
	}

	public void setMaxWidth(final String maxWidth) {
		setAttribute(PropertyKeys.maxWidth, maxWidth);
	}

	public String getMinHeight() {
		return (String) getStateHelper().eval(PropertyKeys.minHeight, null);
	}

	public void setMinHeight(final String minHeight) {
		setAttribute(PropertyKeys.minHeight, minHeight);
	}

	public String getMaxHeight() {
		return (String) getStateHelper().eval(PropertyKeys.maxHeight, null);
	}

	public void setMaxHeight(final String maxHeight) {
		setAttribute(PropertyKeys.maxHeight, maxHeight);
	}

	public boolean isInitClosed() {
		return (Boolean) getStateHelper().eval(PropertyKeys.initClosed, false);
	}

	public void setInitClosed(final boolean initClosed) {
		setAttribute(PropertyKeys.initClosed, initClosed);
	}

	public boolean isStatusbar() {
		return (Boolean) getStateHelper().eval(PropertyKeys.statusbar, false);
	}

	public void setStatusbar(final boolean statusbar) {
		setAttribute(PropertyKeys.statusbar, statusbar);
	}

	public boolean isExistNestedPanes() {
		return existNestedPanes;
	}

	public void setExistNestedPanes(final boolean existNestedPanes) {
		this.existNestedPanes = existNestedPanes;
	}

	public void setAttribute(final PropertyKeys property, final Object value) {
		getStateHelper().put(property, value);

		List<String> setAttributes =
		    (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
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
