/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import jakarta.faces.FacesException;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.FacesContextFactory;
import jakarta.faces.lifecycle.Lifecycle;

/**
 * JSF generates all script tags with 'type="text/javascript"' which throws HTML5 validation warnings.
 * <p>
 * Register it as below in faces-config.xml:
 * </p>
 * 
 * <pre>
 *    &lt;faces-config&gt;
 *        &lt;factory&gt;
 *            &lt;faces-context-factory&gt;org.primefaces.extensions.application.Html5ContextFactory&lt;/faces-context-factory&gt;
 *        &lt;/factory&gt;
 *    &lt;/faces-config&gt;
 * </pre>
 *
 * @since 10.0.0
 */
public class Html5ContextFactory extends FacesContextFactory {

    public Html5ContextFactory(FacesContextFactory wrapped) {
        super(wrapped);
    }

    @Override
    public FacesContext getFacesContext(Object context, Object request, Object response, Lifecycle lifecycle)
                throws FacesException {
        FacesContext wrappedContext = getWrapped().getFacesContext(context, request, response, lifecycle);
        return wrappedContext instanceof Html5Context ? wrappedContext : new Html5Context(wrappedContext);
    }

}