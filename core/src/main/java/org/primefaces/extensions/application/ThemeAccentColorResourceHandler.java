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

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

/**
 * This resource handler replaces the accent color of the Arya, Saga and Vela themes with variables. Variables are named themeX, theme is the lower case theme
 * name, X is the index number of the color.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0.1
 */
public class ThemeAccentColorResourceHandler extends ResourceHandlerWrapper {

    protected static final String RESOURCE_NAME = "theme.css";
    protected static final String LIBRARY_PREFIX = "primefaces-";

    private final ResourceHandler wrapped;

    public ThemeAccentColorResourceHandler(final ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    protected boolean isPrimeFacesTheme(final String resourceName, final String libraryName) {
        return libraryName != null
                    && libraryName.startsWith(LIBRARY_PREFIX)
                    && RESOURCE_NAME.equals(resourceName);
    }

    @Override
    public Resource createResource(final String resourceName, final String libraryName) {
        if (isPrimeFacesTheme(resourceName, libraryName)) {
            return new ThemeAccentColorResource(super.createResource(resourceName, libraryName));
        }
        else {
            return getWrapped().createResource(resourceName, libraryName);
        }
    }

    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }

}
