/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.util;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;

import org.primefaces.application.resource.PrimeResource;
import org.primefaces.util.Constants;

/**
 * Appends the resource version of PrimeFaces ({@code v=}) to the URLs of all other resources too (showcase, theming, webjars, ...), so browsers don't keep
 * serving an old layout.css from their cache after a redeploy. The primefaces* libraries (including primefaces-extensions) are versioned by PrimeFaces itself.
 */
public class ShowcaseResourceHandler extends ResourceHandlerWrapper {

    public ShowcaseResourceHandler(final ResourceHandler wrapped) {
        super(wrapped);
    }

    @Override
    public Resource createResource(final String resourceName, final String libraryName) {
        return wrapResource(super.createResource(resourceName, libraryName), libraryName);
    }

    @Override
    public Resource createResource(final String resourceName, final String libraryName, final String contentType) {
        return wrapResource(super.createResource(resourceName, libraryName, contentType), libraryName);
    }

    private Resource wrapResource(final Resource resource, final String libraryName) {
        if (resource == null || (libraryName != null && libraryName.toLowerCase().startsWith(Constants.LIBRARY))) {
            return resource;
        }
        return new PrimeResource(resource);
    }
}
