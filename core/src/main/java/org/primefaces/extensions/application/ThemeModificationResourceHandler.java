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
package org.primefaces.extensions.application;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;

/**
 * A custom ResourceHandler that processes PrimeFaces theme CSS files to support dynamic accent colors. This handler specifically targets the Arya, Saga and
 * Vela themes, replacing their hardcoded accent colors with CSS variables. The variables follow the naming convention 'theme{X}', where: - 'theme' is the
 * lowercase theme name (e.g., arya, saga, vela) - 'X' is the index number of the color in the theme's palette
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0.1
 */
public class ThemeModificationResourceHandler extends ResourceHandlerWrapper {

    /**
     * The standard CSS resource name for PrimeFaces themes.
     */
    protected static final String RESOURCE_NAME = "theme.css";

    /**
     * The library name prefix that identifies PrimeFaces theme resources.
     */
    protected static final String LIBRARY_PREFIX = "primefaces-";

    /**
     * Constructs a new ThemeModificationResourceHandler.
     *
     * @param wrapped The underlying ResourceHandler being wrapped
     */
    public ThemeModificationResourceHandler(final ResourceHandler wrapped) {
        super(wrapped);
    }

    /**
     * Determines if a resource is a PrimeFaces theme CSS file that should be processed.
     * 
     * @param resourceName The name of the resource being requested
     * @param libraryName The library name of the resource
     * @return true if the resource is a PrimeFaces theme CSS file, false otherwise
     */
    protected boolean isPrimeFacesTheme(final String resourceName, final String libraryName) {
        return libraryName != null
                    && libraryName.startsWith(LIBRARY_PREFIX)
                    && RESOURCE_NAME.equals(resourceName);
    }

    /**
     * Creates a resource instance, wrapping PrimeFaces theme CSS resources with a ThemeModificationResource for processing. Non-theme resources are passed
     * through to the wrapped handler unchanged.
     *
     * @param resourceName The name of the resource to create
     * @param libraryName The library name of the resource
     * @return A Resource instance, either wrapped for theme processing or passed through
     */
    @Override
    public Resource createResource(final String resourceName, final String libraryName) {
        if (isPrimeFacesTheme(resourceName, libraryName)) {
            // Wrap PrimeFaces theme resources with our custom processor
            return new ThemeModificationResource(super.createResource(resourceName, libraryName));
        }
        else {
            // Pass through non-theme resources unchanged
            return getWrapped().createResource(resourceName, libraryName);
        }
    }
}