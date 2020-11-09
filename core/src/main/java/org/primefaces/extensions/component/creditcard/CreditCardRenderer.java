/*
 * Copyright 2011-2020 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    protected void encodeMarkup(FacesContext context, CreditCard creditCard) throws IOException {
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

    protected void encodeScript(FacesContext context, CreditCard creditCard) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtCreditCard", creditCard);
        wb.attr("width", creditCard.getWidth(), 350);
        wb.attr("formatting", creditCard.isFormatting(), true);

        wb.nativeAttr("messages", "{validDate:'" + creditCard.getLabelValidDate() + "',"
                    + "monthYear:'" + creditCard.getLabelMonthYear() + "'}");

        final StringBuilder placeholder = new StringBuilder(1024);
        placeholder.append("{name:'").append(creditCard.getPlaceholderName()).append("'");
        if (!LangUtils.isValueBlank(creditCard.getPlaceholderNumber())) {
            placeholder.append(",number:'").append(creditCard.getPlaceholderNumber()).append("'");
        }
        if (!LangUtils.isValueBlank(creditCard.getPlaceholderExpiry())) {
            placeholder.append(",expiry:'").append(creditCard.getPlaceholderExpiry()).append("'");
        }
        if (!LangUtils.isValueBlank(creditCard.getPlaceholderCvc())) {
            placeholder.append(",cvc:'").append(creditCard.getPlaceholderCvc()).append("'");
        }
        placeholder.append("}");
        wb.nativeAttr("placeholders", placeholder.toString());

        wb.finish();
    }

}
