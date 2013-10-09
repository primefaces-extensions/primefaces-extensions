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
 * $Id: $
 */

package org.primefaces.extensions.component.switchcase;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UINamingContainer;

/**
 * Component class for the <code>DefaultCase</code> component.
 *
 * @author Michael Gmeiner / last modified by $Author: $
 * @version $Revision: $
 * @since 0.6
 */
public class DefaultCase extends UINamingContainer {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.DefaultCase";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author Michael Gmeiner / last modified by $Author: $
	 * @version $Revision: $
	 */
	protected enum PropertyKeys {
		
		style,
		styleClass;

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
	
	public DefaultCase() {
		setRendererType(null);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public String getStyle() {
		return (String) getStateHelper().eval(PropertyKeys.style, null);
	}

	public void setStyle(final String style) {
		getStateHelper().put(PropertyKeys.style, style);
	}
	
	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
	}

	public void setStyleClass(final String styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}
}
