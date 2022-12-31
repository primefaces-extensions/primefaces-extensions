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
package org.primefaces.extensions.component.timeago;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * <code>TimeAgo</code> component renderer.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0.1
 */
public class TimeAgoRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final TimeAgo timeAgo = (TimeAgo) component;
        final Object value = timeAgo.getValue();
        if (value == null) {
            return;
        }
        encodeMarkup(context, timeAgo);
        encodeScript(context, timeAgo);
    }

    protected void encodeMarkup(final FacesContext context, final TimeAgo timeAgo) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = timeAgo.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(TimeAgo.STYLE_CLASS)
                    .add(timeAgo.getStyleClass())
                    .build();

        writer.startElement("span", timeAgo);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (timeAgo.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, timeAgo.getStyle(), Attrs.STYLE);
        }

        encodeTime(context, timeAgo);

        writer.endElement("span");
    }

    protected void encodeTime(final FacesContext context, final TimeAgo timeAgo) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String formattedForJs = timeAgo.formattedForJs();

        writer.startElement("time", null);
        writer.writeAttribute("datetime", formattedForJs, null);
        writer.writeText(timeAgo.getTitlePattern() == null ? formattedForJs : timeAgo.formattedForTitle(), null);
        writer.endElement("time");
    }

    protected void encodeScript(final FacesContext context, final TimeAgo timeAgo) throws IOException {
        final String locale = timeAgo.calculateLocale().getLanguage();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtTimeAgo", timeAgo);
        if (locale != null) {
            wb.attr("locale", locale);
        }
        wb.finish();
    }

}
