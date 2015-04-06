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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.util.Constants;

/**
 * Container for all config parameters.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.6.3
 */
public class ConfigContainer {

	private boolean deliverUncompressedResources = true;

	/**
	 * Avoid instantiation.
	 */
	public ConfigContainer(final FacesContext context) {
		final ExternalContext externalContext = context.getExternalContext();

		final String deliverUncompressedResourcesStringValue =
				externalContext.getInitParameter(Constants.DELIVER_UNCOMPRESSED_RESOURCES_INIT_PARAM);
		if (deliverUncompressedResourcesStringValue != null) {
			deliverUncompressedResources = Boolean.valueOf(deliverUncompressedResourcesStringValue);
		}
	}

	public boolean isDeliverUncompressedResources() {
		return deliverUncompressedResources;
	}
}
