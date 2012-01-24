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

package org.primefaces.extensions.application;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.util.Constants;

/**
 * {@link ResourceHandlerWrapper} which wraps PrimeFaces Extensions resources and
 * appends the version of PrimeFaces Extensions in the {@link PrimeFacesExtensionsResource}.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
public class PrimeFacesExtensionsResourceHandler extends ResourceHandlerWrapper {

	public static final String[] UNCOMPRESSED_EXCLUDES = new String[] { "ckeditor/" };

	private final ResourceHandler wrapped;

	public PrimeFacesExtensionsResourceHandler(final ResourceHandler resourceHandler) {
		super();
		wrapped = resourceHandler;
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}

	@Override
	public Resource createResource(final String resourceName, final String libraryName) {
		Resource resource;

		if (libraryName != null && libraryName.equalsIgnoreCase(Constants.LIBRARY)) {

			//get uncompressed resource if project stage == development
			if (deliverUncompressedFile(resourceName)) {
				resource = super.createResource(resourceName, Constants.LIBRARY_UNCOMPRESSED);
			} else {
				resource = super.createResource(resourceName, libraryName);
			}

			if (resource != null) {
				resource = new PrimeFacesExtensionsResource(resource);
			}
		} else {
			resource = super.createResource(resourceName, libraryName);
		}

		return resource;
	}

	protected boolean deliverUncompressedFile(final String resourceName) {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (context.isProjectStage(ProjectStage.Development)) {
        	if (resourceName.endsWith(Constants.EXTENSION_CSS) || resourceName.endsWith(Constants.EXTENSION_JS)) {
	        	for (String exclude : UNCOMPRESSED_EXCLUDES) {
	        		if (resourceName.contains(exclude)) {
	        			return false;
	        		}
				}

	        	final ExternalContext externalContext = context.getExternalContext();
		        final String value = externalContext.getInitParameter(Constants.DELIVER_UNCOMPRESSED_RESOURCES_INIT_PARAM);
		        return value == null ? true : Boolean.valueOf(value);
	        }
        }

        return false;
    }
}
