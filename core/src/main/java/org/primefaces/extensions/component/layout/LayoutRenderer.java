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
package org.primefaces.extensions.component.layout;

import java.io.IOException;

import jakarta.el.ValueExpression;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

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
@FacesRenderer(rendererType = Layout.DEFAULT_RENDERER, componentFamily = Layout.COMPONENT_FAMILY)
public class LayoutRenderer extends CoreRenderer<Layout> {

    @Override
    public void decode(final FacesContext fc, final Layout component) {
        decodeBehaviors(fc, component);
    }

    @Override
    public void encodeBegin(final FacesContext fc, final Layout component) throws IOException {
        ResponseWriter writer = fc.getResponseWriter();

        final boolean buildOptions = component.getOptions() == null;
        component.setBuildOptions(buildOptions);

        if (buildOptions) {
            final FastStringWriter fsw = new FastStringWriter();
            component.setOriginalWriter(writer);
            component.setFastStringWriter(fsw);
            fc.setResponseWriter(writer.cloneWithWriter(fsw));
            writer = fc.getResponseWriter();
        }

        if (component.isElementLayout()) {
            writer.startElement("div", component);
            writer.writeAttribute("id", component.getClientId(fc), "id");

            if (component.getStyle() != null) {
                writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
            }

            if (component.getStyleClass() != null) {
                writer.writeAttribute(Attrs.CLASS, component.getStyleClass(), "styleClass");
            }
        }
    }

    @Override
    public void encodeEnd(final FacesContext fc, final Layout component) throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();

        if (component.isElementLayout()) {
            if (!component.isStateCookie()) {
                // render hidden field for server-side state saving
                final String clientId = component.getClientId(fc);
                renderHiddenInput(fc, clientId + "_state", null, false);
            }

            writer.endElement("div");
        }

        if (component.isBuildOptions()) {
            fc.setResponseWriter(component.getOriginalWriter());
            encodeScript(fc, component);
            fc.getResponseWriter().write(component.getFastStringWriter().toString());
            component.removeOptions();
            component.setOriginalWriter(null);
            component.setFastStringWriter(null);
        }
        else {
            encodeScript(fc, component);
        }

        component.setBuildOptions(false);
    }

    protected void encodeScript(final FacesContext fc, final Layout component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.init("ExtLayout", component);
        wb.attr("clientState", component.isStateCookie());
        wb.attr("full", component.isFullPage(), false);

        if (component.isNested()) {
            wb.attr("parent", component.getParent().getClientId(fc));
        }

        final ValueExpression stateVE = component.getValueExpression("state");
        if (stateVE != null && !component.isFullPage() && !component.isStateCookie()) {
            wb.attr("serverState", true);

            final String state = component.getState();
            if (LangUtils.isNotBlank(state)) {
                wb.attr("state", state);
            }
            else {
                wb.nativeAttr("state", "{}");
            }
        }
        else {
            wb.attr("serverState", false);
        }

        final Object layoutOptions = component.getOptions();
        if (layoutOptions instanceof LayoutOptions) {
            final LayoutOptions options = (LayoutOptions) layoutOptions;
            wb.append(",options:" + options.toJson());
        }
        else if (layoutOptions instanceof String) {
            // already serialized as JSON string
            wb.append(",options:" + layoutOptions);
        }
        else {
            wb.append(",options:{}");
        }

        encodeClientBehaviors(fc, component);

        wb.finish();
    }
}
