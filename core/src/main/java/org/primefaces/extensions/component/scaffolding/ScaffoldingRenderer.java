/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.scaffolding;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.primefaces.context.PrimeRequestContext;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentTraversalUtils;

/**
 * Renderer for the {@link Scaffolding} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0.3
 */
public class ScaffoldingRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Scaffolding scaffolding = (Scaffolding) component;

        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = scaffolding.getClientId(context);

        if (params.containsKey(clientId)) {
            final ActionEvent event = new ActionEvent(scaffolding);
            scaffolding.queueEvent(event);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Scaffolding scaffolding = (Scaffolding) component;
        encodeMarkup(context, scaffolding);
        if (scaffolding.getValueExpression("loader") != null && !context.getPartialViewContext().isAjaxRequest()) {
            encodeScript(context, scaffolding);
        }
    }

    protected void encodeMarkup(final FacesContext context, final Scaffolding scaffolding) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = scaffolding.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(Scaffolding.STYLE_CLASS)
                    .add(scaffolding.getStyleClass())
                    .build();
        writer.startElement("div", scaffolding);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (scaffolding.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, scaffolding.getStyle(), Attrs.STYLE);
        }

        final String facetName = scaffolding.isReady() ? "ready" : "loading";
        scaffolding.getFacet(facetName).encodeAll(context);

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final Scaffolding scaffolding) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = scaffolding.getClientId(context);
        final String request = PrimeRequestContext.getCurrentInstance(context)
                    .getAjaxRequestBuilder()
                    .init()
                    .source(clientId)
                    .form(ComponentTraversalUtils.closestForm(context, scaffolding).getClientId(context))
                    .process(scaffolding, clientId)
                    .update(scaffolding, clientId)
                    .build();
        writer.startElement("script", scaffolding);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeAttribute("id", clientId, null);

        writer.write("$(function(){");
        writer.write(request);
        writer.write("});");

        writer.endElement("script");
    }

}
