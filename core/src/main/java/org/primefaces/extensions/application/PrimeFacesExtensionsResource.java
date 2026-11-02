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
import jakarta.faces.application.ResourceWrapper;
import jakarta.faces.context.FacesContext;

import org.primefaces.context.PrimeRequestContext;
import org.primefaces.util.Constants;

/**
 * {@link ResourceWrapper} which appends the version of PrimeFaces Extensions to the URL.
 *
 * @author Thomas Andraschko
 * @since 0.1
 */
public class PrimeFacesExtensionsResource extends ResourceWrapper {

    /** The version appended to the resource URLs ({@code &e=}), resolved once at startup. */
    private static final String RESOURCE_VERSION = resolveResourceVersion(PrimeFacesExtensionsResource.class.getPackage().getImplementationVersion());

    private final String version;

    public PrimeFacesExtensionsResource(final Resource resource) {
        super(resource);
        version = PrimeRequestContext.getCurrentInstance(FacesContext.getCurrentInstance()).isHideResourceVersion() ? Constants.EMPTY_STRING
                    : "&e=" + RESOURCE_VERSION;
    }

    /**
     * A SNAPSHOT keeps its version across builds, so the resource URLs would not change after a redeploy and the browser would serve the old CSS/JS from its
     * cache. Append the startup time to bust the cache on every start (same as PrimeFaces does for its {@code &v=}).
     *
     * @param buildVersion the version of PrimeFaces Extensions, e.g. "17.0.0" or "17.0.0-SNAPSHOT"
     * @return the build version, for a SNAPSHOT plus the startup time
     */
    static String resolveResourceVersion(final String buildVersion) {
        if (buildVersion != null && buildVersion.endsWith("-SNAPSHOT")) {
            return buildVersion + "." + Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
        }
        return buildVersion;
    }

    @Override
    public String getRequestPath() {
        return super.getRequestPath() + version;
    }

    @Override
    public String toString() {
        return getWrapped().toString();
    }
}
