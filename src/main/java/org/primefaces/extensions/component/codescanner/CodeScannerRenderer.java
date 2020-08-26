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
package org.primefaces.extensions.component.codescanner;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.api.InputHolder;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link CodeScanner} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 8.0.5
 */
public class CodeScannerRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final CodeScanner codeScanner = (CodeScanner) component;
        encodeMarkup(context, codeScanner);
        encodeScript(context, codeScanner);
    }

    protected void encodeMarkup(final FacesContext context, final CodeScanner codeScanner) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = codeScanner.getClientId(context);

        String styleClass = codeScanner.getStyleClass();
        styleClass = styleClass == null ? CodeScanner.STYLE_CLASS : CodeScanner.STYLE_CLASS + " " + styleClass;

        writer.startElement("span", codeScanner);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (codeScanner.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, codeScanner.getStyle(), Attrs.STYLE);
        }

        if (codeScanner.isVideo()) {
            encodeVideo(context, codeScanner);
        }

        writer.endElement("span");
    }

    protected void encodeVideo(final FacesContext context, final CodeScanner codeScanner) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("video", null);
        if (codeScanner.getWidth() != null) {
            writer.writeAttribute("width", codeScanner.getWidth(), null);
        }
        if (codeScanner.getHeight() != null) {
            writer.writeAttribute("height", codeScanner.getHeight(), null);
        }
        writer.endElement("video");
    }

    protected void encodeScript(final FacesContext context, final CodeScanner codeScanner) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtCodeScanner", codeScanner.resolveWidgetVar(), codeScanner.getClientId(context))
                    .attr("type", codeScanner.getTypeEnum().name())
                    .attr("autoStart", codeScanner.isAutoStart());
        if (codeScanner.getDeviceId() != null) {
            wb.attr("deviceId", codeScanner.getDeviceId());
        }
        if (codeScanner.getFor() != null) {
            String forInputClientId = getForInputClientId(context, codeScanner);
            if (forInputClientId != null) {
                wb.attr("forInput", forInputClientId);
            }
        }
        if (codeScanner.getOnsuccess() != null) {
            wb.callback("onsuccess", "function()", codeScanner.getOnsuccess());
        }
        if (codeScanner.getOnerror() != null) {
            wb.callback("onerror", "function()", codeScanner.getOnerror());
        }

        encodeClientBehaviors(context, codeScanner);

        wb.finish();
    }

    protected String getForInputClientId(final FacesContext context, final CodeScanner codeScanner) {
        UIComponent forComponent = SearchExpressionFacade.resolveComponent(context, codeScanner, codeScanner.getFor());
        if (forComponent == null) {
            return null;
        }
        if (forComponent instanceof InputHolder) {
            return ((InputHolder) forComponent).getInputClientId();
        }
        return forComponent.getClientId(context);
    }

}
