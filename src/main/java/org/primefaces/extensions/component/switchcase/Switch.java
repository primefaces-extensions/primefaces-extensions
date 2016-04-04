/*
 * Copyright 2011-2015 PrimeFaces Extensions
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

import javax.faces.component.UIComponentBase;

/**
 * Component class for the <code>Switch</code> component.
 *
 * @author Michael Gmeiner / last modified by $Author: $
 * @version $Revision: $
 * @since 0.6
 */
public class Switch extends UIComponentBase {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Switch";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SwitchRenderer";
	
	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author Michael Gmeiner / last modified by $Author: $
	 * @version $Revision: $
	 */
	protected enum PropertyKeys {
		
		value;

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
	
	public Switch() {
		setRendererType(DEFAULT_RENDERER);
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public Object getValue() {
		return getStateHelper().eval(PropertyKeys.value, null);
	}

	public void setValue(final Object value) {
		getStateHelper().put(PropertyKeys.value, value);
	}
}
