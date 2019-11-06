/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.component.timeago;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
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
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        final TimeAgo timeAgo = (TimeAgo) component;
        final Object value = timeAgo.getValue();
        if (value == null) {
            return;
        }
        encodeMarkup(context, timeAgo);
        encodeScript(context, timeAgo);
    }

    protected void encodeMarkup(FacesContext context, TimeAgo timeAgo) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = timeAgo.getClientId(context);

        String styleClass = timeAgo.getStyleClass();
        styleClass = styleClass == null ? TimeAgo.STYLE_CLASS : TimeAgo.STYLE_CLASS + " " + styleClass;

        writer.startElement("span", timeAgo);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");

        if (timeAgo.getStyle() != null) {
            writer.writeAttribute("style", timeAgo.getStyle(), "style");
        }

        encodeTime(context, timeAgo, clientId);

        writer.endElement("span");
    }

    protected void encodeTime(FacesContext context, TimeAgo timeAgo, String clientId) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String formattedForJs = timeAgo.formattedForJs();

        writer.startElement("time", null);
        writer.writeAttribute("datetime", formattedForJs, null);
        writer.writeText(timeAgo.getTitlePattern() == null ? formattedForJs : timeAgo.formattedForTitle(), null);
        writer.endElement("time");
    }

    protected void encodeScript(FacesContext context, TimeAgo timeAgo) throws IOException {
        final String locale = timeAgo.calculateLocale().getLanguage();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtTimeAgo", timeAgo.resolveWidgetVar(), timeAgo.getClientId(context));
        if (locale != null) {
            wb.attr("locale", locale);
        }
        wb.finish();
    }

}
