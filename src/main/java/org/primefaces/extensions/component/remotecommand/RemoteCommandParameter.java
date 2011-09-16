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
package org.primefaces.extensions.component.remotecommand;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIOutput;

/**
 * Component class for the <code>RemoteCommandParameter</code> component.
 *
 * @author Thomas Andraschko
 * @since 0.2
 */
public class RemoteCommandParameter extends UIOutput {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.RemoteCommandParameter";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	protected enum PropertyKeys {
		name,
		applyTo;

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

	public RemoteCommandParameter() {
		setRendererType(null);
	}

	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getName() {
		return (String) getStateHelper().eval(PropertyKeys.name, null);
	}

	public void setName(final String name) {
		setAttribute(PropertyKeys.name, name);
	}

	public ValueExpression getApplyTo() {
		return (ValueExpression) getStateHelper().eval(PropertyKeys.applyTo, null);
	}

	public void setApplyTo(final ValueExpression applyTo) {
		setAttribute(PropertyKeys.applyTo, applyTo);
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
