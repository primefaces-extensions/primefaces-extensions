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
package org.primefaces.extensions.util;

import java.util.List;

import javax.faces.context.FacesContext;

import org.primefaces.extensions.component.base.AbstractParameter;

/**
 * Extended {@link org.primefaces.util.AjaxRequestBuilder}.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class ExtAjaxRequestBuilder extends org.primefaces.util.AjaxRequestBuilder {

    private static final String KEY = ExtAjaxRequestBuilder.class.getName();

    public ExtAjaxRequestBuilder(FacesContext context) {
        super(context);
    }

    public static ExtAjaxRequestBuilder get(final FacesContext context) {

        ExtAjaxRequestBuilder arb = (ExtAjaxRequestBuilder) context.getExternalContext().getRequestMap().get(KEY);

        if (arb == null) {
            arb = new ExtAjaxRequestBuilder(context);

            context.getExternalContext().getRequestMap().put(KEY, arb);
        }

        return arb;
    }

    public ExtAjaxRequestBuilder params(final String clientId, final List<AbstractParameter> parameters) {
        boolean paramWritten = false;

        for (int i = 0; i < parameters.size(); i++) {
            final AbstractParameter param = parameters.get(i);

            if (paramWritten) {
                buffer.append(",");
            }
            else {
                paramWritten = true;
                buffer.append(",params:[");
            }

            buffer.append("{name:'");
            buffer.append(clientId).append('_').append(param.getName());
            buffer.append("',value:");
            buffer.append(param.getName());
            buffer.append("}");
        }

        if (paramWritten) {
            buffer.append("]");
        }

        return this;
    }
}
