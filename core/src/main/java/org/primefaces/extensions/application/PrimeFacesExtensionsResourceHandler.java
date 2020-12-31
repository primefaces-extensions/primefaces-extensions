/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.application;

import java.util.Locale;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

/**
 * {@link ResourceHandlerWrapper} which wraps PrimeFaces Extensions resources and appends the version of PrimeFaces Extensions in the
 * {@link PrimeFacesExtensionsResource}.
 *
 * @author Thomas Andraschko
 * @since 0.1
 * @since 9.0 will append e=x.x to all libs starting with "primefaces"
 */
public class PrimeFacesExtensionsResourceHandler extends ResourceHandlerWrapper {

    private final ResourceHandler wrapped;

    @SuppressWarnings("deprecation") // the default constructor is deprecated in JSF 2.3
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
        Resource resource = super.createResource(resourceName, libraryName);
        return wrapResource(resource, libraryName);
    }

    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        Resource resource = super.createResource(resourceName, libraryName, contentType);
        return wrapResource(resource, libraryName);
    }

    private static Resource wrapResource(Resource resource, String libraryName) {
        // libs starting with "primefaces" will get "&e=9.0" extension version appended
        if (resource != null && libraryName != null
                    && (libraryName.toLowerCase(Locale.getDefault()).startsWith(org.primefaces.util.Constants.LIBRARY))) {
            return new PrimeFacesExtensionsResource(resource);
        }
        else {
            return resource;
        }
    }
}