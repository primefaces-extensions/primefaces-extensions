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

package org.primefaces.extensions.component.parameters;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;

import org.primefaces.extensions.component.base.AbstractParameter;

/**
 * Component class for the <code>AssignableParameter</code> component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class AssignableParameter extends AbstractParameter {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.AssignableParameter";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		assignTo;

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

	public AssignableParameter() {
		setRendererType(null);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public ValueExpression getAssignTo() {
		ValueExpression expression = (ValueExpression) getStateHelper().eval(PropertyKeys.assignTo, null);
		if (expression == null) {
			expression = getValueExpression(PropertyKeys.assignTo.toString());
		}

		return expression;
	}

	public void setAssignTo(final ValueExpression assignTo) {
		getStateHelper().put(PropertyKeys.assignTo, assignTo);
	}

	/**
	 * Enables converters to get the value type from the "value" expression.
	 *
	 * @param  name DOCUMENT_ME
	 * @return DOCUMENT_ME
	 */
	@Override
	public ValueExpression getValueExpression(final String name) {
		if ("value".equals(name)) {
			return getAssignTo();
		}

		return super.getValueExpression(name);
	}
}
