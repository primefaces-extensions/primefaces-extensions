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
package org.primefaces.extensions.component.layout;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.model.layout.LayoutOptions;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.FastStringWriter;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Layout} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
public class LayoutRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext fc, final UIComponent component) {
        decodeBehaviors(fc, component);
    }

    @Override
    public void encodeBegin(final FacesContext fc, final UIComponent component) throws IOException {
        ResponseWriter writer = fc.getResponseWriter();
        final Layout layout = (Layout) component;

        final boolean buildOptions = layout.getOptions() == null;
        layout.setBuildOptions(buildOptions);

        if (buildOptions) {
            final FastStringWriter fsw = new FastStringWriter();
            layout.setOriginalWriter(writer);
            layout.setFastStringWriter(fsw);
            fc.setResponseWriter(writer.cloneWithWriter(fsw));
            writer = fc.getResponseWriter();
        }

        if (layout.isElementLayout()) {
            writer.startElement("div", layout);
            writer.writeAttribute("id", layout.getClientId(fc), "id");

            if (layout.getStyle() != null) {
                writer.writeAttribute(Attrs.STYLE, layout.getStyle(), Attrs.STYLE);
            }

            if (layout.getStyleClass() != null) {
                writer.writeAttribute(Attrs.CLASS, layout.getStyleClass(), "styleClass");
            }
        }
    }

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final Layout layout = (Layout) component;

        if (layout.isElementLayout()) {
            if (!layout.isStateCookie()) {
                // render hidden field for server-side state saving
                final String clientId = layout.getClientId(fc);
                writer.startElement("input", null);
                writer.writeAttribute("type", "hidden", null);
                writer.writeAttribute("id", clientId + "_state", null);
                writer.writeAttribute("name", clientId + "_state", null);
                writer.writeAttribute("autocomplete", "off", null);
                writer.endElement("input");
            }

            writer.endElement("div");
        }

        if (layout.isBuildOptions()) {
            fc.setResponseWriter(layout.getOriginalWriter());
            encodeScript(fc, layout);
            fc.getResponseWriter().write(layout.getFastStringWriter().toString());
            layout.removeOptions();
            layout.setOriginalWriter(null);
            layout.setFastStringWriter(null);
        }
        else {
            encodeScript(fc, layout);
        }

        layout.setBuildOptions(false);
    }

    protected void encodeScript(final FacesContext fc, final Layout layout) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.init("ExtLayout", layout);
        wb.attr("clientState", layout.isStateCookie());
        wb.attr("full", layout.isFullPage(), false);

        if (layout.isNested()) {
            wb.attr("parent", layout.getParent().getClientId(fc));
        }

        final ValueExpression stateVE = layout.getValueExpression(Layout.PropertyKeys.state.toString());
        if (stateVE != null && !layout.isFullPage() && !layout.isStateCookie()) {
            wb.attr("serverState", true);

            final String state = layout.getState();
            if (!LangUtils.isValueBlank(state)) {
                wb.attr("state", state);
            }
            else {
                wb.nativeAttr("state", "{}");
            }
        }
        else {
            wb.attr("serverState", false);
        }

        final Object layoutOptions = layout.getOptions();
        if (layoutOptions instanceof LayoutOptions) {
            final LayoutOptions options = (LayoutOptions) layoutOptions;
            wb.append(",options:" + options.toJson());
        }
        else if (layoutOptions instanceof String) {
            // already serialized as JSON string
            wb.append(",options:" + layoutOptions.toString());
        }
        else {
            wb.append(",options:{}");
        }

        encodeClientBehaviors(fc, layout);

        wb.finish();
    }
}
