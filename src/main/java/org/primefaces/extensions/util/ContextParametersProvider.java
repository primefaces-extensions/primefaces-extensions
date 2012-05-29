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
 * $Id: $
 */

package org.primefaces.extensions.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Class which provides servlet context config param values.
 * 
 * @author Thomas Andraschko / last modified by $Author: $
 * @version $Revision: $
 * @since 0.5
 */
public class ContextParametersProvider {

	private static ContextParametersProvider instance = null;

	private boolean deliverUncompressedResources = true;
	private boolean wrapPrimeFacesResources = false;

	/**
	 * Avoid instantiation.
	 */
	private ContextParametersProvider() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = context.getExternalContext();

		final String deliverUncompressedResourcesStringValue =
				externalContext.getInitParameter(Constants.DELIVER_UNCOMPRESSED_RESOURCES_INIT_PARAM);
		if (deliverUncompressedResourcesStringValue != null) {
			deliverUncompressedResources = Boolean.valueOf(deliverUncompressedResourcesStringValue);
		}

		final String wrapPrimeFacesResourcesStringValue =
				externalContext.getInitParameter(Constants.WRAP_PRIME_FACES_RESOURCES_INIT_PARAM);
		if (wrapPrimeFacesResourcesStringValue != null) {
			wrapPrimeFacesResources = Boolean.valueOf(wrapPrimeFacesResourcesStringValue);
		}
	}

	/**
	 * Gets the {@link ContextParametersProvider} instance.
	 * 
	 * @return The {@link ContextParametersProvider} instance.
	 */
	public static ContextParametersProvider getInstance() {
		if (instance == null) {
			instance = new ContextParametersProvider();
		}

		return instance;
	}

	/**
	 * @return the deliverUncompressedResources
	 */
	public boolean isDeliverUncompressedResources() {
		return deliverUncompressedResources;
	}

	/**
	 * @return the wrapPrimeFacesResources
	 */
	public boolean isWrapPrimeFacesResources() {
		return wrapPrimeFacesResources;
	}
}
