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
package org.primefaces.extensions.component.cookiepolicy;

import java.io.IOException;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link CookiePolicy} component.
 *
 * @author Melloware mellowaredev@gmail.com / Frank Cornelis
 * @since 11.0.3
 */
@FacesRenderer(rendererType = CookiePolicy.DEFAULT_RENDERER, componentFamily = CookiePolicy.COMPONENT_FAMILY)
public class CookiePolicyRenderer extends CoreRenderer<CookiePolicy> {

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeBegin(final FacesContext context, final CookiePolicy component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
    }

    @Override
    public void encodeEnd(final FacesContext context, final CookiePolicy component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
    }

    @Override
    public void encodeChildren(final FacesContext context, final CookiePolicy component) throws IOException {
        if (component.hasCookiePolicyCookie(context)) {
            return;
        }
        for (UIComponent child : component.getChildren()) {
            child.encodeAll(context);
        }
    }
}
