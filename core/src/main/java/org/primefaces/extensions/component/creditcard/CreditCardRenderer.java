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
package org.primefaces.extensions.component.creditcard;

import java.io.IOException;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link CreditCard} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 8.0.1
 */
@FacesRenderer(rendererType = CreditCard.DEFAULT_RENDERER, componentFamily = CreditCard.COMPONENT_FAMILY)
public class CreditCardRenderer extends CoreRenderer<CreditCard> {

    @Override
    public void encodeEnd(final FacesContext context, final CreditCard component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    protected void encodeMarkup(final FacesContext context, final CreditCard component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        final String widgetVar = component.resolveWidgetVar();
        final String styleClass = CreditCard.STYLE_CLASS;

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final CreditCard component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtCreditCard", component);
        wb.attr("width", component.getWidth() != null ? component.getWidth() : 350, 350);
        wb.attr("formatting", component.isFormatting(), true);

        wb.nativeAttr("messages", "{validDate:'" + component.getLabelValidDate().replace("\n", "\\n") + "',"
                    + "monthYear:'" + component.getLabelMonthYear() + "'}");

        final StringBuilder placeholder = new StringBuilder(1024);
        placeholder.append("{name:'").append(component.getPlaceholderName()).append("'");
        if (!LangUtils.isBlank(component.getPlaceholderNumber())) {
            placeholder.append(",number:'").append(component.getPlaceholderNumber()).append("'");
        }
        if (!LangUtils.isBlank(component.getPlaceholderExpiry())) {
            placeholder.append(",expiry:'").append(component.getPlaceholderExpiry()).append("'");
        }
        if (!LangUtils.isBlank(component.getPlaceholderCvc())) {
            placeholder.append(",cvc:'").append(component.getPlaceholderCvc()).append("'");
        }
        placeholder.append("}");
        wb.nativeAttr("placeholders", placeholder.toString());

        wb.finish();
    }
}
