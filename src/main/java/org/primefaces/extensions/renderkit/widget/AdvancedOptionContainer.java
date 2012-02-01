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

package org.primefaces.extensions.renderkit.widget;

import java.lang.reflect.Method;

/**
 * Advanced {@link OptionContainer} for internal usage.
 *
 * @author Thomas Andraschko / last modified by $Author:$
 * @version $Revision$
 * @since 0.3
 */
public class AdvancedOptionContainer extends OptionContainer {

	private String propertyName;
	private Method readMethod;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}

	public Method getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(final Method readMethod) {
		this.readMethod = readMethod;
	}
}
