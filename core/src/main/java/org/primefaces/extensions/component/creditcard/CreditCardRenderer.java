/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

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
public class CreditCardRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final CreditCard creditCard = (CreditCard) component;
        encodeMarkup(context, creditCard);
        encodeScript(context, creditCard);
    }

    protected void encodeMarkup(final FacesContext context, final CreditCard creditCard) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = creditCard.getClientId(context);
        final String widgetVar = creditCard.resolveWidgetVar();
        final String styleClass = CreditCard.STYLE_CLASS;

        writer.startElement("div", creditCard);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final CreditCard creditCard) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtCreditCard", creditCard);
        wb.attr("width", creditCard.getWidth(), 350);
        wb.attr("formatting", creditCard.isFormatting(), true);

        wb.nativeAttr("messages", "{validDate:'" + creditCard.getLabelValidDate() + "',"
                    + "monthYear:'" + creditCard.getLabelMonthYear() + "'}");

        final StringBuilder placeholder = new StringBuilder(1024);
        placeholder.append("{name:'").append(creditCard.getPlaceholderName()).append("'");
        if (!LangUtils.isBlank(creditCard.getPlaceholderNumber())) {
            placeholder.append(",number:'").append(creditCard.getPlaceholderNumber()).append("'");
        }
        if (!LangUtils.isBlank(creditCard.getPlaceholderExpiry())) {
            placeholder.append(",expiry:'").append(creditCard.getPlaceholderExpiry()).append("'");
        }
        if (!LangUtils.isBlank(creditCard.getPlaceholderCvc())) {
            placeholder.append(",cvc:'").append(creditCard.getPlaceholderCvc()).append("'");
        }
        placeholder.append("}");
        wb.nativeAttr("placeholders", placeholder.toString());

        wb.finish();
    }

}
