/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.component.layout;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.model.layout.LayoutOptions;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.FastStringWriter;
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

        if (!layout.isFullPage()) {
            writer.startElement("div", layout);
            writer.writeAttribute("id", layout.getClientId(fc), "id");

            if (layout.getStyle() != null) {
                writer.writeAttribute("style", layout.getStyle(), "style");
            }

            if (layout.getStyleClass() != null) {
                writer.writeAttribute("class", layout.getStyleClass(), "styleClass");
            }
        }
    }

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final Layout layout = (Layout) component;

        if (!layout.isFullPage()) {
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

    @Override
    public boolean getRendersChildren() {
        return false;
    }

    protected void encodeScript(final FacesContext fc, final Layout layout) throws IOException {
        final String clientId = layout.getClientId(fc);

        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.initWithDomReady("ExtLayout", layout.resolveWidgetVar(), clientId);
        wb.attr("clientState", layout.isStateCookie());

        if (layout.isFullPage()) {
            wb.attr("forTarget", "body");
        }
        else {
            wb.selectorAttr("forTarget", "#" + clientId);
        }

        final ValueExpression stateVE = layout.getValueExpression(Layout.PropertyKeys.state.toString());
        if (stateVE != null && !layout.isFullPage() && !layout.isStateCookie()) {
            wb.attr("serverState", true);

            final String state = layout.getState();
            if (StringUtils.isNotBlank(state)) {
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
            wb.append(",options:" + ((LayoutOptions) layoutOptions).toJson());
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
