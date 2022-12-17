/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
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
 * @since 10.0 will append e=x.x to all libs starting with "primefaces"
 */
public class PrimeFacesExtensionsResourceHandler extends ResourceHandlerWrapper {

    public PrimeFacesExtensionsResourceHandler(final ResourceHandler resourceHandler) {
        super(resourceHandler);
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
                    &&
                    (libraryName.toLowerCase(Locale.getDefault()).startsWith(org.primefaces.util.Constants.LIBRARY))) {
            return new PrimeFacesExtensionsResource(resource);
        }
        else {
            return resource;
        }
    }
}