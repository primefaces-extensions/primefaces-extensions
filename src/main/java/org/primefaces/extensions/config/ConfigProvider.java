/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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

	private static final ThreadLocal<ConfigContainer> CACHE = new ThreadLocal<ConfigContainer>();

	/**
	 * Gets the one and only {@link ConfigContainer} instance:
	 *
	 * - try to lookup it from a {@link ThreadLocal} cache
	 * - try to get it from the application map
	 * - create a new instance, store it in the {@link ThreadLocal} cache and in the application map
	 *
	 * @param context The {@link FacesContext}.
	 * @return The {@link ConfigContainer} instance.
	 */
	public static ConfigContainer getConfig(final FacesContext context) {
		return getConfig(context, true);
	}

	/**
	 * Gets the one and only {@link ConfigContainer} instance:
	 *
	 * - try to lookup it from a {@link ThreadLocal} cache
	 * - try to get it from the application map
	 * - create a new instance, store it in the {@link ThreadLocal} cache and in the application map
	 *
	 * @param context The {@link FacesContext}.
	 * @param If the {@link ConfigContainer} should be cached in the {@link ThreadLocal} variable.
	 * @return The {@link ConfigContainer} instance.
	 */
	public static ConfigContainer getConfig(final FacesContext context, final boolean cacheInThreadLocal) {

		ConfigContainer container = null;

		if (cacheInThreadLocal) {

			container = CACHE.get();
			if (container == null) {
				container = getConfigFromApplicationMap(context);
				CACHE.set(container);
			}

		} else {
			container = getConfigFromApplicationMap(context);
		}

		return container;
	}

	private static ConfigContainer getConfigFromApplicationMap(final FacesContext context) {

		ConfigContainer container = (ConfigContainer) context.getExternalContext().getApplicationMap().get(KEY);

		if (container == null) {
			container = new ConfigContainer(context);

			context.getExternalContext().getApplicationMap().put(KEY, container);
		}

		return container;
	}

	public static void cleanCache()
	{
		CACHE.remove();
	}
}
