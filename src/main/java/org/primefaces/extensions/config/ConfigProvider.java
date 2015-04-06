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
 * $Id$
 */

package org.primefaces.extensions.config;

import javax.faces.context.FacesContext;

/**
 * Provider for the {@link ConfigContainer}.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.6.3
 */
public class ConfigProvider {

	private static final String KEY = ConfigContainer.class.getName();

	/**
	 * Gets the one and only {@link ConfigContainer} instance.
	 *
	 * @param context The {@link FacesContext}.
	 * @return The {@link ConfigContainer} instance.
	 */
	public static ConfigContainer getConfig(final FacesContext context) {

		ConfigContainer container = (ConfigContainer) context.getExternalContext().getApplicationMap().get(KEY);

		if (container == null) {
			container = new ConfigContainer(context);

			context.getExternalContext().getApplicationMap().put(KEY, container);
		}

		return container;
	}
}
